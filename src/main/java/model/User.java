package model;

import java.io.Serializable;

/**
 * ユーザー情報を表すクラスです。
 * ユーザーID、ユーザー名、ハッシュ化されたパスワード、およびソルトを保持します。
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String passwordHash; // ハッシュ化されたパスワード
    private String salt;         // パスワードハッシュ化に使用するソルト

    /**
     * デフォルトコンストラクタ。
     */
    public User() {
    }

    /**
     * ユーザー名、ハッシュ化パスワード、ソルトを指定してUserオブジェクトを生成します。
     * 主に新規ユーザー作成時や、データベースからユーザー情報を読み込む際に使用されます。
     * 
     * @param name ユーザー名
     * @param passwordHash ハッシュ化されたパスワード
     * @param salt パスワードハッシュ化に使用されたソルト
     */
    public User(String name, String passwordHash, String salt) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    /**
     * ID、ユーザー名、ハッシュ化パスワード、ソルトを指定してUserオブジェクトを生成します。
     * 主にデータベースから既存のユーザー情報を完全に読み込む際に使用されます。
     * 
     * @param id ユーザーID
     * @param name ユーザー名
     * @param passwordHash ハッシュ化されたパスワード
     * @param salt パスワードハッシュ化に使用されたソルト
     */
    public User(int id, String name, String passwordHash, String salt) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    /**
     * ユーザーIDを取得します。
     * @return ユーザーID
     */
    public int getId() {
        return id;
    }

    /**
     * ユーザーIDを設定します。
     * @param id 設定するユーザーID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * ユーザー名を取得します。
     * @return ユーザー名
     */
    public String getName() {
        return name;
    }

    /**
     * ユーザー名を設定します。
     * @param name 設定するユーザー名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ハッシュ化されたパスワードを取得します。
     * @return ハッシュ化されたパスワード
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * ハッシュ化されたパスワードを設定します。
     * @param passwordHash 設定するハッシュ化されたパスワード
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * ソルトを取得します。
     * @return ソルト
     */
    public String getSalt() {
        return salt;
    }

    /**
     * ソルトを設定します。
     * @param salt 設定するソルト
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }
}
