package action;

import java.io.IOException;	
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import course.Course;
import tools.DBManager;
@WebServlet("/QueryAction")
public class QueryAction extends HttpServlet{
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	private static final long serialVersionUID = 1L;

	public QueryAction() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=gb2312");
		request.setCharacterEncoding("gb2312");
		String course = request.getParameter("course");
		PrintWriter out = response.getWriter();
		
		DBManager manager = new DBManager();
		request.setAttribute("course", course);
		Course courseInfo = new Course();
		courseInfo = manager.getCourseInfoByCourseId(course);
		
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
