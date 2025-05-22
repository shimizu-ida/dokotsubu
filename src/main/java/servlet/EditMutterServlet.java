package servlet;

import java.io.IOException;

import org.owasp.html.PolicyFactory; // OWASP Sanitizer
import org.owasp.html.Sanitizers;   // OWASP Sanitizer

import dao.MutterDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Mutter;
import model.User;

@WebServlet("/EditMutterServlet")
public class EditMutterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String mutterIdStr = request.getParameter("mutterId");
        String text = request.getParameter("text");

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
                    // Error already set
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

            // XSS対策: HTMLサニタイズ (OWASP Java HTML Sanitizer)
            PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(Sanitizers.BLOCKS);
            String safeText = policy.sanitize(text);

            mutterToUpdate = new Mutter(existingMutter.getId(), loginUser.getName(), safeText, loginUser.getId(), existingMutter.getImagePath()); 
            
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
             if (mutterToUpdate != null) { // mutterToUpdate might be null if NumberFormatException happens early
                 request.setAttribute("mutterToEdit", mutterToUpdate);
             } else if (mutterIdStr != null && !mutterIdStr.isEmpty()){ // Try to repopulate with original if possible
                 try {
                    int mId = Integer.parseInt(mutterIdStr);
                    Mutter originalMutter = mutterDao.findByIdAndUserId(mId, loginUser.getId());
                    if(originalMutter != null) request.setAttribute("mutterToEdit", originalMutter);
                 } catch (NumberFormatException nfe2) { /* Already handling error */ }
             }
             request.getRequestDispatcher("/WEB-INF/jsp/editMutter.jsp").forward(request, response);
        }
    }
}
