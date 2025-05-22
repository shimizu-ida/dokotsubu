package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

import model.User;

/**
 * ユーザー情報に関するデータベース操作を担当するDAOクラスです。
 * ユーザーの検索や新規作成処理を行います。
 */
public class UsersDAO {
    private static Properties dbProperties = new Properties();
    private static String JDBC_URL;
    private static String DB_USER;
    private static String DB_PASS;
    private static String DB_DRIVER;
    private static final Logger logger = Logger.getLogger(UsersDAO.class.getName());

    static {
        try (InputStream is = UsersDAO.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                logger.log(Level.SEVERE, "db.properties not found in classpath");
                throw new RuntimeException("db.properties not found in classpath");
            }
            dbProperties.load(is);
            JDBC_URL = dbProperties.getProperty("db.url");
            DB_USER = dbProperties.getProperty("db.user");
            DB_PASS = dbProperties.getProperty("db.password");
            DB_DRIVER = dbProperties.getProperty("db.driver");
            Class.forName(DB_DRIVER);
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to load DB properties or JDBC driver.", e);
            throw new RuntimeException("Failed to initialize UsersDAO", e);
        }
    }

    /**
     * 指定されたユーザー名を持つユーザーをデータベースから検索します。
     * 
     * @param username 検索するユーザー名。
     * @return 見つかった場合はUserオブジェクト（ID、ユーザー名、ハッシュ化パスワードを含む）。
     *         見つからない場合やエラー発生時はnull。
     */
    public User findByUsername(String username) {
        User user = null;
        String sql = "SELECT id, username, password_hash FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {
            
            pStmt.setString(1, username);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("username"); 
                    String passwordHash = rs.getString("password_hash"); 
                    user = new User(id, name, passwordHash); 
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user by username: " + username, e);
        }
        return user;
    }

    /**
     * 新しいユーザーをデータベースに作成（登録）します。
     * ユーザーのパスワードはjBCryptを使用してハッシュ化されて保存されます。
     * 
     * @param user 作成するユーザーの情報（ユーザー名と平文パスワードがセットされていること）。
     * @return ユーザー作成に成功した場合はtrue、失敗した場合はfalse。
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {
            
            pStmt.setString(1, user.getName());
            String hashedPassword = BCrypt.hashpw(user.getPass(), BCrypt.gensalt());
            pStmt.setString(2, hashedPassword);
            int result = pStmt.executeUpdate();
            if (result != 1) {
                logger.log(Level.WARNING, "Failed to create user: " + user.getName() + ", affected rows: " + result);
                return false;
            }
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating user: " + user.getName(), e);
            return false;
        }
    }
}
