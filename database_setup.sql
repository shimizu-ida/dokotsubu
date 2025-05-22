-- データベースが存在しない場合は作成 (例: dokotsubu)
-- CREATE DATABASE IF NOT EXISTS dokotsubu CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE dokotsubu;

-- users テーブル
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(128) NOT NULL, -- SHA-512を想定 (128文字の16進数表現)
    salt VARCHAR(32) NOT NULL,          -- ソルト用のランダムな文字列 (例: 16バイトの16進数表現で32文字)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- mutter テーブル
DROP TABLE IF EXISTS mutter;
CREATE TABLE mutter (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    text VARCHAR(1000) NOT NULL,
    image_path VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ダミーデータ挿入

-- ユーザー (パスワードはハッシュ化前のもの。実際のINSERTではハッシュ化された値とソルトを入れる)
-- パスワード 'pass123' に対するハッシュとソルトの例 (SHA-512)
-- user1: salt1, hash_for_pass123_with_salt1
-- user2: salt2, hash_for_pass123_with_salt2
-- user3: salt3, hash_for_pass123_with_salt3
-- これらは後続のステップでJavaコードにより生成・挿入されるか、手動で適切な値を設定する必要があります。
-- ここではプレースホルダーとして記述します。アプリケーション実行前に手動でUserDAO経由などでユーザー作成を推奨。
-- INSERT INTO users (username, password_hash, salt) VALUES
--  ('テストユーザー1', ' (ここにuser1のハッシュ値) ', ' (ここにuser1のソルト) '),
--  ('テストユーザー2', ' (ここにuser2のハッシュ値) ', ' (ここにuser2のソルト) '),
--  ('テストユーザー3', ' (ここにuser3のハッシュ値) ', ' (ここにuser3のソルト) ');
-- 現時点では、DAO経由でユーザー作成を促すため、SQLでの直接挿入はコメントアウトしておきます。
-- アプリケーション起動後、手動で3ユーザー (例: user1, user2, user3 / パスワードは全て password) を登録してください。

-- 投稿 (上記のユーザーが登録された後、そのIDを user_id に使用してください)
-- 以下のダミー投稿は、ユーザーが手動登録された後のIDを想定しています (例: user1がID=1, user2がID=2, user3がID=3)
-- 画像パスのダミー画像は、src/main/webapp/uploads/images/dummy/ に配置することを想定しています。
-- 例: dummy1.jpg, dummy2.jpg, dummy3.png など。
-- ユーザーにこれらの画像を配置してもらう必要があります。

-- INSERT INTO mutter (user_id, text, image_path, created_at) VALUES
--  (1, 'テストユーザー1の最初の投稿です！こんにちは！', NULL, NOW() - INTERVAL 5 DAY),
--  (1, 'これはテストユーザー1の画像付き投稿です。見てください！', 'uploads/images/dummy/dummy1.jpg', NOW() - INTERVAL 4 DAY),
--  (1, '今日の天気は晴れです。気持ちいいですね。', NULL, NOW() - INTERVAL 3 HOUR),
--  (2, 'テストユーザー2です。よろしくお願いします。', NULL, NOW() - INTERVAL 3 DAY),
--  (2, '面白い画像を見つけました。', 'uploads/images/dummy/dummy2.jpg', NOW() - INTERVAL 2 DAY),
--  (2, '週末の予定は何ですか？私は映画を見に行きます。', NULL, NOW() - INTERVAL 1 HOUR),
--  (3, 'テストユーザー3、参加しました！', NULL, NOW() - INTERVAL 1 DAY),
--  (3, 'きれいな景色です。', 'uploads/images/dummy/dummy3.png', NOW() - INTERVAL 12 HOUR),
--  (3, 'おすすめの本を教えてください。', NULL, NOW() - INTERVAL 2 HOUR);

-- 注意: 上記のダミーユーザーとダミー投稿のINSERT文は、
-- パスワードハッシュ/ソルトの生成と、ユーザーIDの動的な割り当てが必要なため、
-- SQLファイル内ではコメントアウトしています。
-- アプリケーションを起動し、まずユーザーを手動で3人登録してください。
-- その後、もしダミー投稿が必要な場合は、登録されたユーザーIDを確認し、
-- 上記INSERT文の user_id を実際のIDに置き換えてから実行してください。
-- ダミー画像 (dummy1.jpg, dummy2.jpg, dummy3.png) は、
-- `src/main/webapp/uploads/images/dummy/` ディレクトリを作成して配置してください。
