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
 * Servlet implementation class AddCourseInfo
 */
@WebServlet("/AddCourseInfo")
public class AddCourseInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCourseInfo() {
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
		
		String courseId = request.getParameter("courseId");
		System.out.println(courseId);
		String courseName = request.getParameter("courseName");
		String credit = request.getParameter("credit");
		String classHour = request.getParameter("classHourl");
		PrintWriter out = response.getWriter();
		DBManager manager = new DBManager();
		boolean flag = manager.insertCourse(courseId, courseName, credit,classHour);
		if (!flag) {
			out.println("<script>alert(\"插入失败\")</script>");
			return;
		}
		out.println("<script>alert(\"插入成功\")</script>");
		//跳转到学生显示信息页面
		//response.addHeader("refresh", "0;URL=teacher_query_score.jsp?courseId="+courseId);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
