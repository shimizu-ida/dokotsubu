# どこつぶ アプリケーション

## 概要

どこつぶは、日々の出来事や思ったことを短いメッセージで投稿できるシンプルなWebアプリケーションです。
ユーザーはアカウントを作成し、ログインすることで、メッセージの投稿、閲覧ができます。
また、自分の投稿を編集したり、画像を添付して投稿することも可能です。

## 主な機能

*   ユーザー登録・ログイン・ログアウト機能
*   投稿の作成（テキスト・画像）
*   投稿の一覧表示
*   自分の投稿の編集機能 (テキストのみ)

## 使用技術

*   Java (Servlet, JSP)
*   JDBC
*   MariaDB (または MySQL)
*   HTML, CSS (Bootstrap 5 - CDN経由 ※現状のJSPに残っている場合)
*   JSTL (※現状のJSPで使用されています)
*   パスワードハッシュ: Java標準 `java.security.MessageDigest` (SHA-256) + ソルト

## セットアップ手順

### 1. データベースの準備

1.  MariaDB (または MySQL) サーバーを起動してください。
2.  プロジェクトルートにある `database_setup.sql` ファイルを使用して、データベースとテーブルを作成し、初期データを投入します。
    *   MySQLクライアントやphpMyAdminなどのツールからこのSQLファイルを実行してください。
    *   **注意:** `database_setup.sql` にはユーザーのダミーデータINSERT文がコメントアウトされています。アプリケーション起動後、まず手動でユーザーを3名程度登録してください (例: `user1`, `user2`, `user3` / パスワードは任意)。その後、ダミー投稿が必要な場合は、登録したユーザーのIDを確認し、`database_setup.sql` 内の投稿INSERT文の `user_id` を実際のIDに置き換えてから実行してください。

### 2. 画像アップロード用ディレクトリの作成

以下のディレクトリを手動で作成してください。

*   **ダミー画像用:** `src/main/webapp/uploads/images/dummy/`
    *   `database_setup.sql` 内のダミー投稿データが参照する画像 (例: `dummy1.jpg`, `dummy2.jpg`, `dummy3.png`) をこのディレクトリに配置してください。適当なサンプル画像で構いません。
*   **ユーザー投稿画像用:** `src/main/webapp/uploads/images/user/`
    *   ユーザーが実際に画像を投稿した際に、このディレクトリに保存されます。アプリケーションが書き込み権限を持つようにしてください。 (注意: `Main.java` 内の画像保存パスが `getServletContext().getRealPath("/uploads/images")` となっており、この下に `user` ディレクトリが自動生成されるか、または直接 `/uploads/images/` に保存される可能性があります。実際の動作に合わせて調整してください。現状は `/uploads/images/` 直下に保存される想定です。)

### 3. ライブラリの配置 (手動)

以下のライブラリは `src/main/webapp/WEB-INF/lib/` ディレクトリに手動で配置する必要があります。
(MavenやGradleなどのビルドツールは使用していません)

*   **MariaDB JDBCドライバ:** `mariadb-java-client-X.X.X.jar` (X.X.X はバージョン)
    *   プロジェクトには `mariadb-java-client-3.5.3.jar` が含まれているようです。
*   **JSTL (結果的に使用中):**
    *   `jakarta.servlet.jsp.jstl-api-X.X.X.jar`
    *   `jakarta.servlet.jsp.jstl-X.X.X.jar` (例: `org.glassfish.web.jakarta.servlet.jsp.jstl-X.X.X.jar`)
*   **注意:** 以前のステップでjBCryptやOWASP Java HTML Sanitizerの配置指示がありましたが、最終的な方針変更により、これらのライブラリは不要となりました。

### 4. ビルドとデプロイ (Eclipse Dynamic Web Project の場合)

1.  Eclipse IDEで本プロジェクトを「Dynamic Web Project」としてインポートします。
2.  プロジェクトを右クリックし、「実行」 > 「サーバーで実行」を選択します。
3.  Apache Tomcatなどの適切なサーバーを選択し、デプロイします。
4.  ブラウザで `http://localhost:(ポート番号)/(プロジェクト名)/` にアクセスします。

## 注意事項

*   **XSS対策:** 現在の実装では、ユーザー入力に対するXSS (クロスサイトスクリプティング) 対策は行われていません。入力されたHTMLタグはそのまま表示される可能性があります。
*   **エラーハンドリング:**基本的なエラーページは用意されていますが、詳細なエラーログの記録やユーザーへのフィードバックは限定的です。
*   **JSPの変更について:** Tailwind CSSへの移行およびJSTLの完全廃止は、開発環境の制約により未完了です。JSPは現状のままとなっています。
