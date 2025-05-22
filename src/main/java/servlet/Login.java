package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.LoginLogic;
import model.User;

/**
 * ユーザーのログイン処理を行うサーブレットです。
 * GETリクエストではログインページにフォワードし、
 * POSTリクエストでユーザー名とパスワードを受け取り認証処理を実行します。
 * 認証には {@link LoginLogic} を使用し、パスワード検証は {@link util.PasswordUtil} に基づきます。
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * GETリクエストを処理します。
     * 通常、ログインフォームの表示のために呼び出されます。
     * 処理としては、index.jsp（ログインフォームがあるページ）にフォワードします。
     * 
     * @param request  クライアントからのHttpServletRequestオブジェクト
     * @param response クライアントへのHttpServletResponseオブジェクト
     * @throws ServletException サーブレットがGETリクエストを処理中にエラーが発生した場合
     * @throws IOException GETリクエストの処理中に入出力エラーが発生した場合
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    /**
     * POSTリクエストを処理します。
     * ユーザー名とパスワードを受け取り、{@link LoginLogic} を使用して認証を行います。
     * 認証のために {@link LoginLogic} に渡すUserオブジェクトは、
     * passwordHashフィールドに平文パスワードを一時的に格納して使用します。
     * 認証成功時には、データベースから取得した完全なユーザー情報（ソルトやハッシュ化パスワードを含む）をセッションに保存します。
     * 認証結果に関わらず、loginResult.jspにフォワードします。
     * 
     * @param request  クライアントからのHttpServletRequestオブジェクト（ユーザー名とパスワードを含む）
     * @param response クライアントへのHttpServletResponseオブジェクト
     * @throws ServletException サーブレットがPOSTリクエストを処理中にエラーが発生した場合
     * @throws IOException POSTリクエストの処理中に入出力エラーが発生した場合
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String pass = request.getParameter("pass"); // This is the plain text password from the form
        
        // LoginLogicに渡すためのUserオブジェクト。
        // Userモデルのコンストラクタ User(String name, String passwordHash, String salt) に合わせ、
        // 平文パスワード(pass)を passwordHash フィールドに、salt は null として一時的に格納します。
        // LoginLogic側では、この passwordHash フィールドから平文パスワードを取得して認証処理を行います。
        User userAttempt = new User(name, pass, null); 
        
        LoginLogic loginLogic = new LoginLogic();
        // loginLogic.execute は認証成功ならDBから取得した完全なUserオブジェクト(ID, name, hash, salt を含む)を返す
        User authenticatedUser = loginLogic.execute(userAttempt); 
        
        HttpSession session = request.getSession();
        if (authenticatedUser != null) { // 認証成功
            session.setAttribute("loginUser", authenticatedUser); // DBから取得した完全なUserオブジェクトをセッションに保存
        } else {
            // 認証失敗
            // loginResult.jspで authenticatedUser (セッションスコープのloginUser) がnullかどうかで表示を切り替える
            session.removeAttribute("loginUser"); // 念のため既存のセッション情報をクリア
        }
        
        request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp").forward(request, response);
    }
}
