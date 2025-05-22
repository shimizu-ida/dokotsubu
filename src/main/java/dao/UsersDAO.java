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

// import org.mindrot.jbcrypt.BCrypt; // jBCryptは新しいハッシュ化ロジックに置き換わるため不要
import model.User;
import util.PasswordUtil; // 新しいPasswordUtilをインポート

/**
 * ユーザー情報に関するデータベース操作を担当するDAOクラスです。
 * ユーザーの検索や新規作成処理を行います。
 * パスワードはSHA-256とソルトを使用してハッシュ化されます。
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
     * @return 見つかった場合はUserオブジェクト（ID、ユーザー名、ハッシュ化パスワード、ソルトを含む）。
     *         見つからない場合やエラー発生時はnull。
     */
    public User findByUsername(String username) {
        User user = null;
        String sql = "SELECT id, username, password_hash, salt FROM users WHERE username = ?"; // saltも取得
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {
            
            pStmt.setString(1, username);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("username"); 
                    String passwordHash = rs.getString("password_hash"); 
                    String salt = rs.getString("salt"); // saltを取得
                    user = new User(id, name, passwordHash, salt); // saltを含めてUserオブジェクトを生成
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user by username: " + username, e);
        }
        return user;
    }

    /**
     * 新しいユーザーをデータベースに作成（登録）します。
     * ユーザーのパスワードはPasswordUtilを使用してハッシュ化され、ソルトと共に保存されます。
     * 
     * @param user 作成するユーザーの情報（ユーザー名のみが使用される）。
     * @param plainPassword ユーザーの平文パスワード。
     * @return ユーザー作成に成功した場合はtrue、失敗した場合はfalse。
     */
    public boolean createUser(User user, String plainPassword) {
        String sql = "INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {
            
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(plainPassword, salt);

            if (hashedPassword == null) {
                logger.log(Level.SEVERE, "Failed to hash password for user: " + user.getName() + ". User creation aborted.");
                return false;
            }

            pStmt.setString(1, user.getName());
            pStmt.setString(2, hashedPassword);
            pStmt.setString(3, salt);
            
            int result = pStmt.executeUpdate();
            if (result != 1) {
                logger.log(Level.WARNING, "Failed to create user: " + user.getName() + ", affected rows: " + result);
                return false;
            }
            // 成功した場合、渡されたUserオブジェクトにID以外の永続化情報を設定することも検討できるが、
            // このメソッドの責務はDBへの保存とし、呼び出し元で必要なら再度findByUsernameを呼ぶなどする。
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating user: " + user.getName(), e);
            return false;
        }
    }
}
