package com.njut.data;

/*成绩相关的数据的抽象*/
public class AchievementElement {
	private String courseName;
	private int score;
	private String type;
	private float credit;

	public AchievementElement(String courseName, int score, String type,
			float credit) {
		this.courseName = courseName;
		this.score = score;
		this.type = type;
		this.credit = credit;
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

	public float getCredit() {
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

	public void setCredit(float credit) {
		this.credit = credit;
	}

}