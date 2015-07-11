package action;

import java.io.IOException;	
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import tools.DBManager;
@WebServlet("/AddStudentInfo")
public class AddStudentInfo extends HttpServlet{
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	private static final long serialVersionUID = 1L;

	public AddStudentInfo() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String studentId = request.getParameter("studentId");
		//System.out.println(studentId);
		String studentName = request.getParameter("studentName");
		String sex = request.getParameter("sex");
		String classId = request.getParameter("classId");
		String age = request.getParameter("age");
		String password = request.getParameter("password");
		PrintWriter out = response.getWriter();
		DBManager manager = new DBManager();
		boolean flag = manager.insertStudent(studentId, studentName, sex, classId, password, age);
		if (!flag) {
			out.println("<script>alert(\"插入失败\")</script>");
			return;
		}
		out.println("<script>alert(\"插入成功\")</script>");
		//跳转到学生显示信息页面
		//response.addHeader("refresh", "0;URL=teacher_query_score.jsp?courseId="+courseId);
		
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
