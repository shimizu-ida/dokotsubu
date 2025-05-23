package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Mutter;

/**
 * 投稿（つぶやき）に関するデータベース操作を担当するDAOクラスです。
 */
public class MutterDAO {
    private static Properties dbProperties = new Properties();
    private static String JDBC_URL;
    private static String DB_USER;
    private static String DB_PASS;
    private static String DB_DRIVER;
    private static final Logger logger = Logger.getLogger(MutterDAO.class.getName());

    static {
        try (InputStream is = MutterDAO.class.getClassLoader().getResourceAsStream("db.properties")) {
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
            throw new RuntimeException("Failed to initialize MutterDAO", e);
        }
    }

    /**
     * すべての投稿（つぶやき）をデータベースから取得し、IDの降順でソートされたリストとして返します。
     * 
     * @return 投稿のリスト。取得に失敗した場合は空のリスト。
     */
    public List<Mutter> findAll() {
        List<Mutter> list = new ArrayList<>();
        String sql = "SELECT id, name, text, user_id, image_path FROM mutter ORDER BY id DESC";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql);
             ResultSet rs = pStmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String text = rs.getString("text");
                int userId = rs.getInt("user_id");
                String imagePath = rs.getString("image_path");
                Mutter m = new Mutter(id, name, text, userId, imagePath);
                list.add(m);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while finding all mutters.", e);
        }
        return list;
    }

    /**
     * 新しい投稿（つぶやき）をデータベースに作成（保存）します。
     * 
     * @param m 保存するMutterオブジェクト。ユーザー名、テキスト、ユーザーID、画像パスが含まれていること。
     * @return 保存に成功した場合はtrue、失敗した場合はfalse。
     */
    public boolean create(Mutter m) {
        String sql = "INSERT INTO mutter(name, text, user_id, image_path) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setString(1, m.getUserName());
            pStmt.setString(2, m.getText());
            pStmt.setInt(3, m.getUserId());
            pStmt.setString(4, m.getImagePath());

            int res = pStmt.executeUpdate();
            if (res != 1) {
                logger.log(Level.WARNING, "Failed to create mutter, affected rows: " + res + " for mutter by user: " + m.getUserName());
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while creating mutter for user: " + m.getUserName(), e);
            return false;
        }
        return true;
    }

    /**
     * 指定された投稿IDとユーザーIDに一致する投稿をデータベースから検索します。
     * これは、特定のユーザーが所有する特定の投稿を取得するために使用されます（例：編集時）。
     * 
     * @param mutterId 検索する投稿のID。
     * @param userId   投稿を所有するユーザーのID。
     * @return 見つかった場合はMutterオブジェクト、見つからない場合やエラー時はnull。
     */
    public Mutter findByIdAndUserId(int mutterId, int userId) {
        Mutter mutter = null;
        String sql = "SELECT id, name, text, user_id, image_path FROM mutter WHERE id = ? AND user_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setInt(1, mutterId);
            pStmt.setInt(2, userId);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String text = rs.getString("text");
                    int currentUserId = rs.getInt("user_id");
                    String imagePath = rs.getString("image_path");
                    mutter = new Mutter(id, name, text, currentUserId, imagePath);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding mutter by id " + mutterId + " and userId " + userId, e);
        }
        return mutter;
    }

    /**
     * 既存の投稿（つぶやき）のテキスト内容を更新します。
     * 画像の更新はこのメソッドではサポートされていません。
     * 
     * @param m 更新するMutterオブジェクト。ID、更新後のテキスト、所有者ユーザーIDが含まれていること。
     * @return 更新に成功した場合はtrue、失敗した場合はfalse。
     */
    public boolean update(Mutter m) {
        String sql = "UPDATE mutter SET text = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setString(1, m.getText());
            pStmt.setInt(2, m.getId());
            pStmt.setInt(3, m.getUserId());

            int result = pStmt.executeUpdate();
            if (result != 1) {
                logger.log(Level.WARNING, "Failed to update mutter, affected rows: " + result + " for mutter id: " + m.getId());
                return false;
            }
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating mutter for id: " + m.getId(), e);
            return false;
        }
    }
}
