package model;

// import java.util.List; // List<Mutter> list パラメータを持つ execute メソッドは現在使用されていないためコメントアウト
import dao.MutterDAO;

/**
 * 新しい投稿（つぶやき）を処理するビジネスロジックを担当するクラスです。
 */
public class PostMutterLogic {

    // 現在このシグネチャのメソッドはアプリケーション内で直接使用されていないようです。
    // もし将来的に使用する予定がなければ削除を検討しても良いでしょう。
    // List<Mutter> を引数に取る execute メソッドは、以前のバージョンで
    // アプリケーションスコープに投稿リストを保存していた際の名残である可能性があります。
    /**
     * 指定された投稿リストの先頭に新しい投稿を追加します。
     * <p>
     * 注意: このメソッドは現在アプリケーションの主要な投稿フローでは使用されていません。
     * {@link #execute(Mutter)} がデータベースへの保存に使用されます。
     * </p>
     * @param m 追加するMutterオブジェクト
     * @param list Mutterオブジェクトのリスト
     */
    /* // コメントアウト: 現在このメソッドは使用されていません。
    public void execute(Mutter m, List<Mutter> list) {
        list.add(0, m);
    }
    */
    
    /**
     * 新しい投稿（つぶやき）をデータベースに保存します。
     * @param m 保存するMutterオブジェクト（ユーザー名、テキスト、ユーザーID、画像パスが設定されていること）
     */
    public void execute(Mutter m) {
        MutterDAO dao = new MutterDAO();
        dao.create(m);
    }
}
