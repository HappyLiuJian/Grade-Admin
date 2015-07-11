package course;

public class Course {
	private int courseId;
	private String courseName;
	private int credit;
	private int classHour;

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getClassHour() {
		return classHour;
	}

	public void setClassHour(int classHour) {
		this.classHour = classHour;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
}
