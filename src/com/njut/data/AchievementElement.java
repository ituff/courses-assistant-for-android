package com.njut.data;

/*�ɼ���ص����ݵĳ���*/
public class AchievementElement {
	private String courseName;
	private int score;
	private String type;
	private double credit;
	private String grade;
	private double point;

	public AchievementElement(String courseName, int score, String type,
			double credit,double point) {
		this.courseName = courseName;
		this.score = score;
		this.type = type;
		this.credit = credit;
		this.point=point;
	}
	
	public AchievementElement () {
		
	}

	public String getCourseName() {
		return courseName;
	}

	public int getScore() {
		return score;
	}

	public String getType() {
		return type;
	}

	public double getCredit() {
		return credit;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public String getGrade() {
		return grade;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public void setGrade(String grade) {
		this.grade = grade;
		if (grade.trim().equals("����")) {
			score = 95;
		} else if (grade.trim().equals("����")) {
			score = 85;
		} else if (grade.trim().equals("��") || grade.trim().equals("�е�")) {
			score = 75;
		} else if (grade.trim().equals("����")) {
			score = 65;
		} else {
			score = 50;
		}
	}
}