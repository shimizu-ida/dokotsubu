package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Formatter;

/**
 * パスワードのハッシュ化とソルト生成に関連するユーティリティクラスです。
 */
public class PasswordUtil {

    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * 安全なランダムソルトを生成します。
     * 
     * @return 生成された16進数文字列のソルト（32文字）。
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // 16バイトのソルト
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

    /**
     * 指定されたパスワードとソルトを使用してパスワードハッシュを計算します。
     * ハッシュアルゴリズムにはSHA-256を使用します。
     * 
     * @param password ハッシュ化する平文のパスワード。
     * @param salt     ハッシュ化に使用するソルト（16進数文字列）。
     * @return 計算されたパスワードハッシュ（16進数文字列、SHA-256の場合は64文字）。
     *         ハッシュ化に失敗した場合はnullを返すことがあります。
     */
    public static String hashPassword(String password, String salt) {
        try {
            // ソルトをパスワードの前に付加してハッシュ化 (他の方法でも可)
            String saltedPassword = salt + password; 
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashedBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            // この例外は、HASH_ALGORITHMがシステムでサポートされていない場合に発生します。
            // SHA-256は標準的なので通常は発生しません。
            System.err.println("Failed to hash password using " + HASH_ALGORITHM + ": " + e.getMessage());
            // 実際のアプリケーションでは、より堅牢なエラーハンドリングやロギングを検討してください。
            return null; 
        }
    }

    /**
     * バイト配列を16進数文字列に変換します。
     * 
     * @param bytes 変換するバイト配列。
     * @return 変換された16進数文字列。
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        formatter.close(); // リソースを解放
        return sb.toString();
    }

    // 動作確認用のmainメソッド (任意)
    /*
    public static void main(String[] args) {
        String salt = generateSalt();
        System.out.println("Generated Salt: " + salt);
        
        String password = "testPassword123";
        String hashedPassword = hashPassword(password, salt);
        System.out.println("Password: " + password);
        System.out.println("Hashed Password: " + hashedPassword);

        // 検証例
        String saltForCheck = salt; // DBから取得したソルトを想定
        String passwordAttempt = "testPassword123";
        String hashedAttempt = hashPassword(passwordAttempt, saltForCheck);
        System.out.println("Attempted Hash: " + hashedAttempt);
        System.out.println("Match: " + hashedPassword.equals(hashedAttempt));

        String wrongPasswordAttempt = "wrongPassword";
        String wrongHashedAttempt = hashPassword(wrongPasswordAttempt, saltForCheck);
        System.out.println("Wrong Attempted Hash: " + wrongHashedAttempt);
        System.out.println("Match (wrong): " + hashedPassword.equals(wrongHashedAttempt));
    }
    */
}
