package model;

import java.io.Serializable;
import java.sql.Timestamp; // Timestampのインポート

/**
 * 投稿（つぶやき）情報を表すクラスです。
 * 各投稿の内容、投稿者名、ユーザーID、画像パス、作成日時などを保持します。
 */
public class Mutter implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String userName;
    private String text;
    private String imagePath;
    private int userId;
    private Timestamp createdAt; // 投稿作成日時

    /**
     * デフォルトコンストラクタ。
     */
    public Mutter() {
    }

    /**
     * 新規投稿用コンストラクタ。
     * IDとcreatedAtはデータベースで自動生成されることを想定しています。
     * 
     * @param userName 投稿者名
     * @param text 投稿内容
     * @param userId 投稿したユーザーのID
     * @param imagePath 投稿画像のパス (画像がない場合はnull)
     */
    public Mutter(String userName, String text, int userId, String imagePath) {
        this.userName = userName;
        this.text = text;
        this.userId = userId;
        this.imagePath = imagePath;
        // this.createdAt はDBで自動設定されるか、DAOで設定
    }

    /**
     * データベースからの投稿取得や更新用コンストラクタ。
     * 
     * @param id 投稿ID
     * @param userName 投稿者名
     * @param text 投稿内容
     * @param userId 投稿したユーザーのID
     * @param imagePath 投稿画像のパス (画像がない場合はnull)
     * @param createdAt 投稿の作成日時
     */
    public Mutter(int id, String userName, String text, int userId, String imagePath, Timestamp createdAt) {
        this.id = id;
        this.userName = userName;
        this.text = text;
        this.userId = userId;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    /**
     * 投稿IDを取得します。
     * @return 投稿ID
     */
    public int getId() {
        return id;
    }

    /**
     * 投稿IDを設定します。
     * @param id 設定する投稿ID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * 投稿者名を取得します。
     * @return 投稿者名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 投稿者名を設定します。
     * @param userName 設定する投稿者名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 投稿内容のテキストを取得します。
     * @return 投稿内容
     */
    public String getText() {
        return text;
    }

    /**
     * 投稿内容のテキストを設定します。
     * @param text 設定する投稿内容
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 投稿画像のパスを取得します。
     * @return 画像のパス。画像がない場合はnull。
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * 投稿画像のパスを設定します。
     * @param imagePath 設定する画像のパス
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * この投稿を行ったユーザーのIDを取得します。
     * @return ユーザーID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * この投稿を行ったユーザーのIDを設定します。
     * @param userId 設定するユーザーID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 投稿の作成日時を取得します。
     * @return 投稿の作成日時 (java.sql.Timestamp)
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * 投稿の作成日時を設定します。
     * @param createdAt 設定する作成日時 (java.sql.Timestamp)
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
