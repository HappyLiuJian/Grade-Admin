package action;

import java.io.IOException;	
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tools.DBManager;

/**
 * Servlet implementation class LoginAction
 */
@WebServlet("/LoginAction")
public class LoginAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=gb2312");
		request.setCharacterEncoding("gb2312");
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String userType = request.getParameter("userType");

		DBManager manager = new DBManager();
		HttpSession session = request.getSession();
		
		session.setAttribute("userId", userId);
		session.setAttribute("userType", userType);
		String name=manager.userVerify(userId, password, userType);
		if (!name.isEmpty()) {
			session.setAttribute("name", name);
			response.sendRedirect("main.jsp");
		} else {
			session.setAttribute("message", "用户名或密码不匹配。");
			response.sendRedirect("failed.jsp");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
