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
        // GETリクエストはログインページ (index.jsp) にフォワード
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    /**
     * POSTリクエストを処理します。
     * ユーザー名とパスワードを受け取り、{@link LoginLogic} を使用して認証を行います。
     * 認証成功時には、ユーザー情報をセッションに保存します。
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
        String pass = request.getParameter("pass");
        
        User userToLogin = new User(name, pass);
        LoginLogic loginLogic = new LoginLogic();
        User loginUser = loginLogic.execute(userToLogin); // Userオブジェクトまたはnullが返る
        
        HttpSession session = request.getSession();
        if (loginUser != null) { // 認証成功
            session.setAttribute("loginUser", loginUser); // DBから取得したUserオブジェクトをセッションに保存
        } else {
            // 認証失敗の場合、既存のloginUserセッション属性を削除することを検討できますが、
            // loginResult.jspで loginUser が null かどうかで表示を切り替えるため、
            // ここで明示的に削除しなくても問題ありません。
            // session.removeAttribute("loginUser"); 
        }
        
        // 認証結果ページへフォワード
        request.getRequestDispatcher("WEB-INF/jsp/loginResult.jsp").forward(request, response);
    }
}
