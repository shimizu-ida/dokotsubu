package model;

import org.mindrot.jbcrypt.BCrypt; // jBCryptのインポート
import dao.UsersDAO;             // UsersDAOのインポート

/**
 * ログイン処理に関するビジネスロジックを担当するクラスです。
 */
public class LoginLogic {

    /**
     * ユーザー認証を実行します。
     * 提供されたユーザー名とパスワードを使用してデータベースと照合し、
     * 認証が成功した場合は完全なユーザー情報（IDを含む）を返します。
     * 
     * @param user 認証を試みるユーザーの情報（ユーザー名とパスワードがセットされていること）
     * @return 認証成功の場合はデータベースから取得したUserオブジェクト（ID、ユーザー名、ハッシュ化パスワードを含む）。
     *         認証失敗の場合はnull。
     */
    public User execute(User user) {
        UsersDAO usersDao = new UsersDAO();
        // データベースからユーザー名でユーザー情報を取得
        User dbUser = usersDao.findByUsername(user.getName());

        // ユーザーが存在し、かつ提供されたパスワードがデータベースのハッシュ化パスワードと一致するか確認
        if (dbUser != null && BCrypt.checkpw(user.getPass(), dbUser.getPass())) {
            // 認証成功時、DBから取得したユーザー情報(ID含む)を返す
            return dbUser;
        }
        // 認証失敗時
        return null;
    }
}
