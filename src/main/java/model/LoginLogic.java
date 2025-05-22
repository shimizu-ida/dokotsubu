package model;

// import org.mindrot.jbcrypt.BCrypt; // jBCryptは新しいハッシュ化ロジックに置き換わるため不要
import dao.UsersDAO;
import util.PasswordUtil; // 新しいPasswordUtilをインポート

/**
 * ログイン処理に関するビジネスロジックを担当するクラスです。
 * PasswordUtilを使用してパスワードの検証を行います。
 */
public class LoginLogic {

    /**
     * ユーザー認証を実行します。
     * 提供されたユーザー名と平文パスワードを使用してデータベースと照合し、
     * 認証が成功した場合は完全なユーザー情報（ID、ユーザー名、ハッシュ化パスワード、ソルトを含む）を返します。
     * 
     * @param userAttempt 認証を試みるユーザーの情報。
     *                    userAttempt.getName() でユーザー名を取得します。
     *                    userAttempt.getPasswordHash() で平文のパスワード試行を取得します (このフィールドの命名は不自然ですが、
     *                    Loginサーブレットの既存のUserオブジェクト生成方法 new User(name, pass) との互換性を考慮し、
     *                    Userモデルの passwordHash フィールドを一時的に平文パスワード保持に使用していると解釈します)。
     *                    より理想的なのは、LoginLogicのexecuteメソッドのシグネチャを execute(String username, String plainPassword) に変更することです。
     * @return 認証成功の場合はデータベースから取得したUserオブジェクト（ID、ユーザー名、ハッシュ化パスワード、ソルトを含む）。
     *         認証失敗の場合はnull。
     */
    public User execute(User userAttempt) { 
        UsersDAO usersDao = new UsersDAO();
        // データベースからユーザー名でユーザー情報を取得 (これにはソルトとハッシュ化済みパスワードが含まれる)
        User storedUser = usersDao.findByUsername(userAttempt.getName());

        if (storedUser != null) {
            // Loginサーブレットから渡されたUserオブジェクトの passwordHash フィールドには、
            // ユーザーが入力した平文パスワードが格納されていると想定 (Userモデルの変更に依存)
            // 本来であれば、Userモデルに getRawPassword() のような一時的なメソッドがあるか、
            // LoginLogicの引数を (String username, String plainPassword) とする方が明確。
            // ここでは、userAttempt.getPasswordHash() が平文パスワードを返すという前提で進めます。
            // もし User モデルの passwordHash が常にハッシュ値を持つ仕様の場合、
            // Login サーブレットの new User(name, pass) の pass を User オブジェクトの
            // 一時的な非永続フィールド (例: private String plainPasswordAttempt) にセットし、
            // それを取得する getter を User モデルに用意する必要があります。
            // 今回の User モデルの変更 (passフィールド削除、passwordHashとsalt追加) を考慮すると、
            // Loginサーブレットで new User(name, passFromForm, null) のように生成し、
            // userAttempt.getPasswordHash() で passFromForm を取得するのが最も近い形となります。
            
            String plainPasswordAttempt = userAttempt.getPasswordHash(); // 平文パスワード試行を取得
            
            if (plainPasswordAttempt == null) { 
                // Login.javaで new User(name,pass) とした場合、Userモデルのコンストラクタが
                // (String name, String passwordHash, String salt) になっているため、
                // pass は passwordHash フィールドにセットされる。
                // よって、plainPasswordAttempt は userAttempt.getPasswordHash() で取得できる。
                // もし、User(name, pass) コンストラクタが name フィールドのみをセットし、
                // passwordHashとsaltをnullにする場合、ここでの取得方法を変える必要がある。
                // 現状のUser(String name, String passwordHash, String salt)コンストラクタを想定。
                // Loginサーブレットで `new User(name, pass, null)` としている場合、これでOK。
            }


            String expectedHash = storedUser.getPasswordHash();
            String salt = storedUser.getSalt();
            
            // 入力された平文パスワードとDBから取得したソルトでハッシュを計算
            String actualHashAttempt = PasswordUtil.hashPassword(plainPasswordAttempt, salt);

            if (actualHashAttempt != null && actualHashAttempt.equals(expectedHash)) {
                // 認証成功時、DBから取得した完全なユーザー情報(ID, username, passwordHash, salt を含む)を返す
                return storedUser; 
            }
        }
        // ユーザーが存在しない、またはパスワードが一致しない場合は認証失敗
        return null;
    }
}
