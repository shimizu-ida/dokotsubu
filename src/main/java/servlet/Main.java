package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.GetMutterListLogic;
import model.Mutter;
import model.PostMutterLogic;
import model.User;

@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setAttribute("mutterList", new GetMutterListLogic().execute());
		
		if(request.getSession().getAttribute("loginUser") == null) {
			response
				.sendRedirect("index.jsp");
		}else {
			request
				.getRequestDispatcher("WEB-INF/jsp/main.jsp")
				.forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String text = request.getParameter("text");
		
		if(text != null && text.length()!=0) {
			
			User u = (User)request.getSession().getAttribute("loginUser");
			
			text = text.replace("<", "&lt;");
			
			new PostMutterLogic().execute(new Mutter(u.getName(), text));
		}
		
		request.setAttribute("mutterList", new GetMutterListLogic().execute());
		
		request
			.getRequestDispatcher("WEB-INF/jsp/main.jsp")
			.forward(request, response);
	}

}
