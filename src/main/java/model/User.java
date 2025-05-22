package model;

import java.io.Serializable;

/**
 * ユーザー情報を表すクラスです。
 * ログイン情報やユーザー識別のために使用されます。
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L; // SerializableのためのバージョンID

    private int id;
    private String name;
    private String pass; // 主にログイン時の一時的なパスワード保持、またはハッシュ化されたパスワードの格納に使用

    /**
     * デフォルトコンストラクタ。
     */
    public User() {
    }

    /**
     * ユーザー名とパスワードを指定してUserオブジェクトを生成します。
     * 主にログインフォームからのデータや新規ユーザー作成時に使用されます。
     * @param name ユーザー名
     * @param pass パスワード（平文またはハッシュ化済み）
     */
    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    /**
     * ID、ユーザー名、パスワードを指定してUserオブジェクトを生成します。
     * 主にデータベースからユーザー情報を取得した際に使用されます。
     * @param id ユーザーID
     * @param name ユーザー名
     * @param pass パスワード（通常はハッシュ化済み）
     */
    public User(int id, String name, String pass) {
        this.id = id;
        this.name = name;
        this.pass = pass;
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
     * パスワード（またはパスワードハッシュ）を取得します。
     * @return パスワード（またはパスワードハッシュ）
     */
    public String getPass() {
        return pass;
    }

    /**
     * パスワード（またはパスワードハッシュ）を設定します。
     * @param pass 設定するパスワード（またはパスワードハッシュ）
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
}
