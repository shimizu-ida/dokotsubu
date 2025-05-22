package model;

import java.io.Serializable;

/**
 * 投稿（つぶやき）情報を表すクラスです。
 * 各投稿の内容、投稿者名、ユーザーID、画像パスなどを保持します。
 */
public class Mutter implements Serializable {
    private static final long serialVersionUID = 1L; // SerializableのためのバージョンID

    private int id;
    private String userName;
    private String text;
    private byte[] imageData; // 投稿に関連付けられた画像のバイト配列
    private int userId;    // この投稿を行ったユーザーのID

    /**
     * デフォルトコンストラクタ。
     */
    public Mutter() {
    }

    /**
     * 画像なしの新規投稿用コンストラクタ。
     * 投稿IDはデータベースで自動生成されることを想定しています。
     * @param userName 投稿者名
     * @param text 投稿内容
     * @param userId 投稿したユーザーのID
     */
    public Mutter(String userName, String text, int userId) {
        this.userName = userName;
        this.text = text;
        this.userId = userId;
        this.imageData = null; // 画像なしの場合はnull
    }

    /**
     * 画像ありの新規投稿用コンストラクタ。
     * 投稿IDはデータベースで自動生成されることを想定しています。
     * @param userName 投稿者名
     * @param text 投稿内容
     * @param userId 投稿したユーザーのID
     * @param imageData 投稿画像のバイト配列
     */
    public Mutter(String userName, String text, int userId, byte[] imageData) {
        this.userName = userName;
        this.text = text;
        this.userId = userId;
        this.imageData = imageData;
    }

    /**
     * データベースからの投稿取得や更新用コンストラクタ（画像なしの場合）。
     * @param id 投稿ID
     * @param userName 投稿者名
     * @param text 投稿内容
     * @param userId 投稿したユーザーのID
     */
    public Mutter(int id, String userName, String text, int userId) {
        this.id = id;
        this.userName = userName;
        this.text = text;
        this.userId = userId;
        this.imageData = null; // 画像なしの場合はnull
    }
    
    /**
     * データベースからの投稿取得や更新用コンストラクタ（画像ありの場合）。
     * @param id 投稿ID
     * @param userName 投稿者名
     * @param text 投稿内容
     * @param userId 投稿したユーザーのID
     * @param imageData 投稿画像のバイト配列
     */
    public Mutter(int id, String userName, String text, int userId, byte[] imageData) {
        this.id = id;
        this.userName = userName;
        this.text = text;
        this.userId = userId;
        this.imageData = imageData;
    }

    /**
     * 投稿IDを取得します。
     * @return 投稿ID
     */
    public int getId() {
        return id;
    }

    /**
     * 投稿者名を取得します。
     * @return 投稿者名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 投稿内容のテキストを取得します。
     * @return 投稿内容
     */
    public String getText() {
        return text;
    }

    /**
     * 投稿画像のバイト配列を取得します。
     * @return 画像のバイト配列。画像がない場合はnull。
     */
    public byte[] getImageData() {
        return imageData;
    }

    /**
     * 投稿画像のバイト配列を設定します。
     * @param imageData 設定する画像のバイト配列
     */
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
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
}
