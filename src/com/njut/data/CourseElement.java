package com.njut.data;

/*课程相关的数据抽象*/
public class CourseElement {
	private String courseName;
	private int state;
	private String teacherName;
	private String classroomName;
	private String time;
	private String ctid;
	
	public CourseElement(String courseName, int state, String teacherName,
			String classroomName,String time) {
		this.courseName = courseName;
		this.state = state;
		this.teacherName = teacherName;
		this.classroomName = classroomName;
		this.time=time;
	}
	
	public CourseElement(String courseName, int state, String teacherName,
			String classroomName,String time,String ctid) {
		this.courseName = courseName;
		this.state = state;
		this.teacherName = teacherName;
		this.classroomName = classroomName;
		this.time=time;
		this.ctid=ctid;
	}

	public String getCourseName() {
		return courseName;
	}

	public int getState() {
		return state;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public String getTime() {
		return time;
	}

	public String getCtid() {
		return ctid;
	}
}