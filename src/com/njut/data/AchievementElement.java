package com.njut.data;

/*成绩相关的数据的抽象*/
public class AchievementElement {
	private String courseName;
	private double score;
	private String type;
	private double credit;
	private String grade;
	private double point;
	//补考或重修
	private String achievementType;
	//补考或重修成绩
	private double newestMark;

	public AchievementElement(String courseName, double score, String type,
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

	public double getScore() {
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

	public void setScore(double score) {
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
		if (grade.trim().equals("优秀")) {
			score = 95;
		} else if (grade.trim().equals("良好")) {
			score = 85;
		} else if (grade.trim().equals("中") || grade.trim().equals("中等")) {
			score = 75;
		} else if (grade.trim().equals("及格")) {
			score = 65;
		} else {
			score = 50;
		}
	}

	public String getAchievementType() {
		return achievementType;
	}

	public void setAchievementType(String achievementType) {
		this.achievementType = achievementType;
	}

	public double getNewestMark() {
		return newestMark;
	}

	public void setNewestMark(double newestMark) {
		this.newestMark = newestMark;
	}
}