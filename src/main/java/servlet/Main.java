package servlet;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.owasp.html.PolicyFactory; // OWASP Sanitizer
import org.owasp.html.Sanitizers;   // OWASP Sanitizer

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.GetMutterListLogic;
import model.Mutter;
import model.PostMutterLogic;
import model.User;

@WebServlet("/Main")
@MultipartConfig
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("mutterList", new GetMutterListLogic().execute());
		
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		
		if(loginUser == null) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		}else {
			request.getRequestDispatcher("WEB-INF/jsp/main.jsp").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String text = request.getParameter("text");
		
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}
		
		// XSS対策: HTMLサニタイズ (OWASP Java HTML Sanitizer)
		// <a>, <p>, <br>, <li>, <ol>, <ul>, <h1>-<h6>, <b>, <i>, <u>, <s>, <blockquote>, <hr> などを許可するポリシーの例
        // 必要に応じて .and(Sanitizers.IMAGES) や .and(Sanitizers.STYLES) も追加可能
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(Sanitizers.BLOCKS)
                                .and(Sanitizers.TABLES); // Example: allow tables as well
		String safeText = policy.sanitize(text);
		
		String imagePath = null;
		try {
			Part filePart = request.getPart("image");
			if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName() != null && !filePart.getSubmittedFileName().isEmpty()) {
				String originalFileName = filePart.getSubmittedFileName();
                String cleanOriginalFileName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
				String uniqueFileName = UUID.randomUUID().toString() + "_" + cleanOriginalFileName;
				
				String uploadPath = getServletContext().getRealPath("/uploads/images");
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}
				
				filePart.write(uploadPath + File.separator + uniqueFileName);
				imagePath = "uploads/images/" + uniqueFileName;
			}
		} catch (ServletException e) {
            e.printStackTrace(); 
            request.setAttribute("errorMsg", "画像のアップロードに失敗しました: " + e.getMessage());
        }

		if(safeText != null && safeText.length() != 0) {
			Mutter mutter = new Mutter(loginUser.getName(), safeText, loginUser.getId(), imagePath);
			PostMutterLogic postMutterLogic = new PostMutterLogic();
			postMutterLogic.execute(mutter);
		} else if (imagePath == null) { 
			request.setAttribute("errorMsg", "つぶやきが空です");
		} else if (safeText == null || safeText.length() == 0) {
            Mutter mutter = new Mutter(loginUser.getName(), "", loginUser.getId(), imagePath);
			PostMutterLogic postMutterLogic = new PostMutterLogic();
			postMutterLogic.execute(mutter);
        }
		
		request.setAttribute("mutterList", new GetMutterListLogic().execute());
		request.getRequestDispatcher("WEB-INF/jsp/main.jsp").forward(request, response);
	}
}
