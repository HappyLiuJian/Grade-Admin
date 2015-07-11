package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.DBManager;

/**
 * Servlet implementation class PasswordModifyAction
 */
@WebServlet("/PasswordModifyAction")
public class PasswordModifyAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordModifyAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String userId = request.getParameter("userId");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String userType = request.getParameter("userType");
		PrintWriter writer = response.getWriter();
		DBManager manager = new DBManager();
		String name=manager.userVerify(userId, oldPassword, userType);
		if (!name.isEmpty()) {
			if(manager.updatePassword(userId, userType, newPassword)){
				writer.println("<script>alert(\"密码修改成功！\")</script>");
			}else{
				writer.println("<script>alert(\"密码修改失败！\")</script>");
			}
		} else {
			writer.println("<script>alert(\"密码错误！\")</script>");
		}
		
		response.addHeader("refresh", "0;URL=password_modify.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
