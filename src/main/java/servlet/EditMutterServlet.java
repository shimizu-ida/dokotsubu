package servlet;

import java.io.IOException;

// import org.owasp.html.PolicyFactory; // OWASP Sanitizer 削除
// import org.owasp.html.Sanitizers;   // OWASP Sanitizer 削除

import dao.MutterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Mutter;
import model.User;

/**
 * 投稿（つぶやき）の編集処理を行うサーブレットです。
 * GETリクエストで編集対象の投稿情報を取得・表示し、
 * POSTリクエストで投稿内容の更新を行います。
 * XSS対策はJSP側での表示時に行うことを想定しています。
 */
@WebServlet("/EditMutterServlet")
public class EditMutterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * GETリクエストを処理します。
     * 指定されたIDの投稿を編集用に取得し、editMutter.jspにフォワードします。
     * ログインしていない場合や、投稿が存在しない・編集権限がない場合はエラーメッセージを設定します。
     * 
     * @param request  クライアントからのHttpServletRequestオブジェクト
     * @param response クライアントへのHttpServletResponseオブジェクト
     * @throws ServletException サーブレットがGETリクエストを処理中にエラーが発生した場合
     * @throws IOException GETリクエストの処理中に入出力エラーが発生した場合
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mutterIdStr = request.getParameter("id");
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        if (mutterIdStr == null || mutterIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "投稿IDが指定されていません。");
            request.getRequestDispatcher("/WEB-INF/jsp/editMutter.jsp").forward(request, response);
            return;
        }

        try {
            int mutterId = Integer.parseInt(mutterIdStr);
            MutterDAO mutterDao = new MutterDAO();
            Mutter mutter = mutterDao.findByIdAndUserId(mutterId, loginUser.getId());

            if (mutter != null) {
                request.setAttribute("mutterToEdit", mutter);
            } else {
                request.setAttribute("errorMessage", "編集権限がないか、投稿が存在しません。");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "無効な投稿IDです。");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/editMutter.jsp").forward(request, response);
    }

    /**
     * POSTリクエストを処理します。
     * フォームから送信された投稿IDと新しいテキスト内容で投稿を更新します。
     * ログインしていない場合、必須パラメータが不足している場合、
     * または更新対象の投稿が存在しない・権限がない場合はエラーメッセージを設定して編集ページに再フォワードします。
     * 更新成功後はメインページにリダイレクトします。
     * 
     * @param request  クライアントからのHttpServletRequestオブジェクト
     * @param response クライアントへのHttpServletResponseオブジェクト
     * @throws ServletException サーブレットがPOSTリクエストを処理中にエラーが発生した場合
     * @throws IOException POSTリクエストの処理中に入出力エラーが発生した場合
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String mutterIdStr = request.getParameter("mutterId");
        String text = request.getParameter("text"); // サニタイズ処理を削除し、元の値を使用

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        MutterDAO mutterDao = new MutterDAO();
        Mutter mutterToUpdate = null; 

        if (mutterIdStr == null || mutterIdStr.isEmpty() || text == null || text.trim().isEmpty()) {
            request.setAttribute("errorMessage", "投稿IDまたは内容が空です。");
            if (mutterIdStr != null && !mutterIdStr.isEmpty()) {
                try {
                    int mutterId = Integer.parseInt(mutterIdStr);
                    Mutter existingMutter = mutterDao.findByIdAndUserId(mutterId, loginUser.getId());
                    request.setAttribute("mutterToEdit", existingMutter); 
                } catch (NumberFormatException e) {
                    // Error already set by initial check or will be by subsequent try-catch
                }
            }
            request.getRequestDispatcher("/WEB-INF/jsp/editMutter.jsp").forward(request, response);
            return;
        }
        
        try {
            int mutterId = Integer.parseInt(mutterIdStr);
            Mutter existingMutter = mutterDao.findByIdAndUserId(mutterId, loginUser.getId());

            if (existingMutter == null) {
                request.setAttribute("errorMessage", "編集対象の投稿が見つからないか、権限がありません。");
                request.getRequestDispatcher("/WEB-INF/jsp/editMutter.jsp").forward(request, response);
                return;
            }

            // XSS対策処理を削除
            // String safeText = text; // 直接textを使用

            // imagePathは既存のものを保持
            mutterToUpdate = new Mutter(existingMutter.getId(), loginUser.getName(), text, loginUser.getId(), existingMutter.getImagePath(), existingMutter.getCreatedAt()); 
            
            boolean success = mutterDao.update(mutterToUpdate);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/Main");
            } else {
                request.setAttribute("errorMessage", "投稿の更新に失敗しました。");
                request.setAttribute("mutterToEdit", mutterToUpdate); 
                request.getRequestDispatcher("/WEB-INF/jsp/editMutter.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
             request.setAttribute("errorMessage", "無効な投稿IDです。");
             // mutterToUpdateがnullの可能性があるため、再度DBから取得を試みるか、
             // フォームの再表示に必要な情報をセットする。
             // ここでは、エラーメッセージと共に編集ページにフォワードする。
             // フォームの再表示のため、もしmutterIdStrが有効なら再度読み込みを試みる。
             if (mutterIdStr != null && !mutterIdStr.isEmpty()){
                 try {
                    int mId = Integer.parseInt(mutterIdStr);
                    Mutter originalMutter = mutterDao.findByIdAndUserId(mId, loginUser.getId());
                    if(originalMutter != null) request.setAttribute("mutterToEdit", originalMutter);
                 } catch (NumberFormatException nfe2) { /* Error message already set */ }
             }
             request.getRequestDispatcher("/WEB-INF/jsp/editMutter.jsp").forward(request, response);
        }
    }
}
