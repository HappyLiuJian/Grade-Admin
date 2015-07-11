package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder.In;

import course.*;
import user.*;

public class DBManager {
	private final String sql_url = "jdbc:mysql://localhost:3306/schoolmanager";
	private final String sql_username = "root";
	private final String sql_password = "lj199411";

	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(sql_url, sql_username, sql_password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	public String userVerify(String userId, String password, String userType) {
		String name = "";
		Connection conn = this.getConnection();
		String pstmt = "select name from " + userType + "info where " + userType + "Id=" + userId + " and password="
				+ password;
		try {
			Statement sql = conn.createStatement();
			ResultSet res = (ResultSet) sql.executeQuery(pstmt);
			if (res.next()) {
				name = res.getString("name");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return name;
	}
	public boolean updatePassword(String userId, String userType, String newPassword) {
		Connection conn = this.getConnection();
		String pstmt = "update " + userType + "info set password=" + newPassword + " where "+userType+"Id=" + userId;
		try {
			Statement sql = conn.createStatement();
			if(sql.executeUpdate(pstmt)==1){
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return false;
	}
	public User getInfo(String userId, String userType) {
		Connection conn = this.getConnection();
		User user = null;
		if (userType.equals("student")) {
			user = new Student();
			String pstmt = "SELECT academy,major,studentinfo.classId,sex,name,studentId,age FROM classinfo INNER JOIN studentinfo ON studentinfo.classid = classinfo.classId where studentId="
					+ userId;
			try {
				Statement sql = conn.createStatement();
				ResultSet res = (ResultSet) sql.executeQuery(pstmt);
				if (res.next()) {
					((Student) user).setAge(res.getInt("age"));
					((Student) user).setAcademy(res.getString("academy"));
					((Student) user).setId(res.getInt("studentId"));
					((Student) user).setClassId(res.getInt("classId"));
					((Student) user).setMajor(res.getString("major"));
					((Student) user).setName(res.getString("name"));
					((Student) user).setSex(res.getString("sex"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				this.closeConnection(conn);
			}
		} else if (userType.equals("teacher")) {
			user = new Teacher();
			String pstmt = "SELECT * FROM teacherInfo where teacherId=" + userId;
			try {
				Statement sql = conn.createStatement();
				ResultSet res = (ResultSet) sql.executeQuery(pstmt);
				if (res.next()) {
					((Teacher) user).setId(res.getInt("teacherId"));
					((Teacher) user).setName(res.getString("name"));
					((Teacher) user).setSex(res.getString("sex"));
					((Teacher) user).setAge(res.getInt("age"));
					((Teacher) user).setPosition(res.getString("position"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				this.closeConnection(conn);
			}
		} else if (userType.equals("admin")) {
			user = new Admin();
			String pstmt = "SELECT * FROM admininfo where adminId=" + userId;
			try {
				Statement sql = conn.createStatement();
				ResultSet res = (ResultSet) sql.executeQuery(pstmt);
				if (res.next()) {
					((Admin) user).setId(res.getInt("adminId"));
					((Admin) user).setName(res.getString("name"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				this.closeConnection(conn);
			}
		}
		return user;
	}

	public ArrayList<Course> getCourseByTeacherId(String teacherId) {
		ArrayList<Course> courseList = new ArrayList<Course>();
		Connection conn = this.getConnection();
		String pstmt = "SELECT courseName,courseInfo.courseId,classHour,credit FROM teachercourse INNER JOIN courseinfo ON teachercourse.courseId = courseinfo.courseId where teacherId="
				+ teacherId;
		try {
			Statement sql = conn.createStatement();
			ResultSet res = (ResultSet) sql.executeQuery(pstmt);
			while (res.next()) {
				Course tmp = new Course();
				tmp.setClassHour(res.getInt("classHour"));
				tmp.setCredit(res.getInt("credit"));
				tmp.setCourseId(res.getInt("courseInfo.courseId"));
				tmp.setCourseName(res.getString("courseName"));
				courseList.add(tmp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return courseList;
	}

	public ArrayList<Score> getScore(String teacherId, String courseId) {
		ArrayList<Score> scoreList = new ArrayList<Score>();
		Connection conn = this.getConnection();
		String pstmt = "SELECT studentcourse.studentId,studentcourse.score,studentinfo.name,studentinfo.classid"
				+ " FROM teachercourse INNER JOIN studentcourse ON studentcourse.courseId = teachercourse.courseId INNER JOIN studentinfo ON studentcourse.studentId = studentinfo.studentId"
				+ " WHERE teacherId=" + teacherId + " and teachercourse.courseId=" + courseId;
		try {
			Statement sql = conn.createStatement();
			ResultSet res = (ResultSet) sql.executeQuery(pstmt);
			while (res.next()) {
				Score tmp = new Score();
				tmp.setClassId(res.getInt("studentinfo.classid"));
				tmp.setName(res.getString("studentinfo.name"));
				tmp.setScore(res.getInt("studentcourse.score"));
				tmp.setStudentId(res.getInt("studentcourse.studentId"));
				scoreList.add(tmp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return scoreList;
	}

	public ArrayList<Student> getStudentByTeacher(String teacherId, String courseId) {
		ArrayList<Student> studentList = new ArrayList<Student>();
		Connection conn = this.getConnection();
		String pstmt = "SELECT studentInfo.studentId,studentInfo.name,studentInfo.classId,studentInfo.age,studentInfo.sex,classInfo.academy,classInfo.major "
				+ "FROM teachercourse INNER JOIN studentcourse ON studentcourse.courseId = teachercourse.courseId "
				+ "INNER JOIN studentinfo ON studentcourse.studentId = studentinfo.studentId "
				+ "INNER JOIN classInfo ON classInfo.classId=studentInfo.classId "
				+ "WHERE teacherId=" + teacherId + " and teachercourse.courseId=" + courseId;
		try {
			Statement sql = conn.createStatement();
			ResultSet res = (ResultSet) sql.executeQuery(pstmt);
			while (res.next()) {
				Student tmp = new Student();
				tmp.setClassId(res.getInt("studentInfo.classId"));
				tmp.setName(res.getString("studentInfo.name"));
				tmp.setAcademy(res.getString("classInfo.academy"));
				tmp.setAge(res.getInt("studentInfo.age"));
				tmp.setId(res.getInt("studentInfo.studentId"));
				tmp.setSex(res.getString("studentInfo.sex"));
				tmp.setMajor(res.getString("classInfo.major"));
				studentList.add(tmp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return studentList;
	}

	public ArrayList<Score> getUnregist(String teacherId, String courseId) {
		ArrayList<Score> scoreList = new ArrayList<Score>();
		Connection conn = this.getConnection();
		String pstmt = "SELECT studentcourse.studentId,studentcourse.score,studentinfo.name,studentinfo.classid"
				+ " FROM teachercourse INNER JOIN studentcourse ON studentcourse.courseId = teachercourse.courseId INNER JOIN studentinfo ON studentcourse.studentId = studentinfo.studentId"
				+ " WHERE teacherId=" + teacherId + " and teachercourse.courseId=" + courseId
				+ " and studentcourse.score is null";
		try {
			Statement sql = conn.createStatement();
			ResultSet res = (ResultSet) sql.executeQuery(pstmt);
			while (res.next()) {
				Score tmp = new Score();
				tmp.setClassId(res.getInt("studentinfo.classid"));
				tmp.setName(res.getString("studentinfo.name"));
				tmp.setScore(res.getInt("studentcourse.score"));
				tmp.setStudentId(res.getInt("studentcourse.studentId"));
				scoreList.add(tmp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return scoreList;
	}

	public boolean updateScore(String courseId, String studentId, String score) {
		Connection conn = this.getConnection();
		String pstmt = "update studentcourse set score=" + score + " where studentId=" + studentId + " and courseId="
				+ courseId;
		try {
			Statement sql = conn.createStatement();
			int res = sql.executeUpdate(pstmt);
			if (res == 1) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return false;
	}
	
	public boolean addStudentToCourse(String courseId, String studentId) {
		Connection conn = this.getConnection();
		String pstmt = "INSERT into studentcourse(courseId,studentId) values (" + courseId + "," + studentId + ")";
		try {
			Statement sql = conn.createStatement();
			int res = sql.executeUpdate(pstmt);
			if (res == 1) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return false;
	}
	
	public boolean deleteStudentInCourse(String courseId, String studentId) {
		Connection conn = this.getConnection();
		String pstmt = "DELETE FROM studentcourse WHERE courseId=" + courseId + " and studentId=" + studentId;
		try {
			Statement sql = conn.createStatement();
			int res = sql.executeUpdate(pstmt);
			if (res == 1) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return false;
	}
	/*获取所有课程*/
	public ArrayList<Course> getAllCourse() {
		ArrayList<Course> allCourseInfo = new ArrayList<Course>();
		Connection conn = this.getConnection();
		String pstmt = "select * from courseinfo";
		try {
			Statement sql = conn.createStatement();
			ResultSet res = (ResultSet) sql.executeQuery(pstmt);
			while(res.next()) {
				Course tmp = new Course();
				tmp.setClassHour(res.getInt("classHour"));
				tmp.setCourseId(res.getInt("courseId"));
				tmp.setCourseName(res.getString("courseName"));
				tmp.setCredit(res.getInt("credit"));
				allCourseInfo.add(tmp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return allCourseInfo;
		
	}
	
	public Course getCourseInfoByCourseId(String course) {
		Course courseInfo = new Course();
		Connection conn = this.getConnection();
		String pstmt = "select * from courseinfo where courseName = " + course;
		try {
			Statement sql = conn.createStatement();
			ResultSet res = (ResultSet) sql.executeQuery(pstmt);
			if (res.next()) {
				courseInfo.setCourseId(res.getInt("courseId"));
				courseInfo.setCourseName(res.getString("courseName"));
				courseInfo.setCredit(res.getInt("credit"));
				courseInfo.setClassHour(res.getInt("classHour"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return courseInfo;
		
	}
	/*插入新的学生信息*/
	public boolean insertStudent(String studentId,String studentName,String sex,String classId,String password,String age) {
		Connection conn = this.getConnection();
		String pstmt = "insert into studentinfo values(" + studentId + ", '" +  studentName + "','" + sex + "'," + classId + "," + age + "," + password + ")";
		try {
			Statement sql = conn.createStatement();
			int res = sql.executeUpdate(pstmt);
			if (res == 1) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return false;
		
	}
	/*插入新的教师信息*/
	public boolean insertTeacher(String teacherId,String teacherName,String sex,String position,String password,String age) {
		Connection conn = this.getConnection();
		String pstmt = "insert into teacherInfo values(" + teacherId + ", '" +  teacherName + "','" + sex + "'," + age + "," + position + "," + password + ")";
		try {
			Statement sql = conn.createStatement();
			int res = sql.executeUpdate(pstmt);
			if (res == 1) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return false;
		
	}
	/*插入新的管理员信息*/
	public boolean insertAdmin(String adminId,String adminName,String password) {
		Connection conn = this.getConnection();
		String pstmt = "insert into admininfo values(" + adminId + ", " +  password + ",'" + adminName + "')";
		try {
			Statement sql = conn.createStatement();
			int res = sql.executeUpdate(pstmt);
			if (res == 1) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return false;
		
	}
	/*插入新的课程信息*/
	public boolean insertCourse(String courseId,String courseName,String credit,String classHour) {
		Connection conn = this.getConnection();
		String pstmt = "insert into courseinfo values(" + courseId + ",' " +  courseName + "'," + credit + "," + classHour + ")";
		try {
			Statement sql = conn.createStatement();
			int res = sql.executeUpdate(pstmt);
			if (res == 1) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return false;
		
	}
	public ArrayList<sscore> GetScore(String studentId) {
		ArrayList<sscore> scorelist = new ArrayList<sscore>();
		Connection conn = this.getConnection();
		String pstmt = "select studentcourse.courseId, courseName, credit, score from courseinfo, studentcourse where courseinfo.courseId=studentcourse.courseId and studentcourse.studentId=201501";//+studentId;
		try {
			Statement sql = conn.createStatement();
			ResultSet res = (ResultSet) sql.executeQuery(pstmt);
			while (res.next()) {
				sscore tmp = new sscore();
				tmp.setCourseId(res.getInt("studentcourse.courseId"));
				tmp.setCoursename(res.getString("courseName"));
				tmp.setCredit(res.getInt("credit"));
				tmp.setScore(res.getInt("score"));
				scorelist.add(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return scorelist;
	}
	/*获取课程的统计信息*/
	public ArrayList<Integer> getCountInScoreRange(String courseId) {
		ArrayList<Integer> countList = new ArrayList<Integer>();
		Connection conn = this.getConnection();
		String[] pstmt = new String[5];
		int a = 0;
		int b = 59;
		for (int i = 0; i < 5; i++) {
			pstmt[i] = "select count(studentId) from studentcourse where score between " + a + " and " + b
					+ " and courseId = " + courseId;
			a = b + 1;
			if (i != 3) {
				b = a + 9;
			} else {
				b = a + 10;
			}
		}
		try {
			Statement sql = conn.createStatement();
			for(int i=0;i<5;i++){
				ResultSet res = (ResultSet) sql.executeQuery(pstmt[i]);
				if(res.next()){
					countList.add(res.getInt("count(studentId)"));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			this.closeConnection(conn);
		}
		return countList;
	}

}
























