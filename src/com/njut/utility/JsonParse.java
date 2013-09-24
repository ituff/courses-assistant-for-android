package com.njut.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.njut.data.AchievementElement;
import com.njut.data.Curriculum;
import com.njut.data.PersonElement;

public class JsonParse {

	public PersonElement jsonToPersonElement(String jsonStr) {
		PersonElement person = new PersonElement();
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			person.setBirthday(jsonObj.getString("birthday"));
			person.setSex(jsonObj.getString("sex"));
			person.setSchoolnumber(jsonObj.getString("schoolnumber"));
			person.setTel(jsonObj.getString("tel"));
			person.setCampusname(jsonObj.getString("campusname"));
			person.setUniversityname(jsonObj.getString("universityname"));
			person.setNatureclassname(jsonObj.getString("natureclassname"));
			person.setEmail(jsonObj.getString("email"));
			person.setSchoolCalendar(jsonObj.getString("schoolCalendar"));
			person.setGrade(jsonObj.getString("grade"));
			person.setCollegename(jsonObj.getString("collegename"));
			person.setRealname(jsonObj.getString("realname"));
			person.setBegindate(jsonObj.getString("begindate"));
			person.setSessioncode(jsonObj.getString("sessioncode"));
			person.setFieldName(jsonObj.getString("fieldname"));
		} catch (JSONException e) {
			Log.e("jsonToPersonElement", e.toString());
		}
		return person;
	}

	public Curriculum jsonToCurriculum(String jsonStr) {
		Curriculum curriculum = new Curriculum();
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			curriculum.setEndTime(jsonObj.getString("endtime"));
			curriculum.setCourseBelonging(jsonObj.getString("courseBelonging"));
			curriculum.setStartTime(jsonObj.getString("starttime"));
			curriculum.setCourseNature(jsonObj.getString("courseNature"));
			curriculum.setExamMethod(jsonObj.getString("examMethod"));
			curriculum.setBeginSection(jsonObj.getInt("beginSection"));
			curriculum.setCourseName(jsonObj.getString("courseName"));
			curriculum.setCourseCategory(jsonObj.getString("courseCategory"));
			curriculum.setEndWeek(jsonObj.getInt("endWeek"));
			curriculum.setEndSection(jsonObj.getInt("endSection"));
			curriculum.setOddorReven(jsonObj.getString("oddorReven"));
			curriculum.setBeginWeek(jsonObj.getInt("beginWeek"));
			curriculum.setDay(jsonObj.getInt("day"));
			curriculum.setCredit(jsonObj.getString("credit"));
			curriculum.setPlace(jsonObj.getString("place"));
			curriculum.setChooseNumber(jsonObj.getString("chooseNumber"));
			curriculum.setCtid(jsonObj.getString("ctid"));
			curriculum.setTeacher(jsonObj.getString("teachername"));
		} catch (JSONException e) {
			Log.e("jsonToCurriculum", e.toString());
		}
		return curriculum;
	}

	public Curriculum jsonToCurriculum(JSONObject jsonObj) {
		Curriculum curriculum = new Curriculum();
		try {
			curriculum.setEndTime(jsonObj.getString("endtime"));
			curriculum.setCourseBelonging(jsonObj.getString("courseBelonging"));
			curriculum.setStartTime(jsonObj.getString("starttime"));
			curriculum.setCourseNature(jsonObj.getString("courseNature"));
			curriculum.setExamMethod(jsonObj.getString("examMethod"));
			curriculum.setBeginSection(jsonObj.getInt("beginsection"));
			curriculum.setCourseName(jsonObj.getString("coursename"));
			curriculum.setCourseCategory(jsonObj.getString("courseCategory"));
			curriculum.setEndWeek(jsonObj.getInt("endweek"));
			curriculum.setEndSection(jsonObj.getInt("endsection"));
			curriculum.setOddorReven(jsonObj.getString("oddoreven"));
			curriculum.setBeginWeek(jsonObj.getInt("beginweek"));
			curriculum.setDay(jsonObj.getInt("day"));
			curriculum.setCredit(jsonObj.getString("credit"));
			curriculum.setPlace(jsonObj.getString("place"));
			curriculum.setChooseNumber(jsonObj.getString("choosenumber"));
			curriculum.setCtid(jsonObj.getString("ctid"));
			curriculum.setTeacher(jsonObj.getString("teachername"));
		} catch (JSONException e) {
			Log.e("jsonToCurriculum", e.toString());
		}
		return curriculum;
	}

	public AchievementElement jsonToAchievementElement(JSONObject jsonObj) {
		AchievementElement achievementElement = new AchievementElement();
		try {
			achievementElement.setCourseName(jsonObj.getString("coursename"));
			// achievementElement.setPoint(jsonObj.getDouble("point"));
			achievementElement.setCredit(jsonObj.getDouble("credit"));
			achievementElement.setType(jsonObj.getString("courseNature"));
			if (jsonObj.getString("examMethod").equals("学位课")) {
				achievementElement.setType("学位课");
			}
			String score = jsonObj.getString("score");
			Pattern pattern = Pattern.compile("^[0-9]+\\.{0,1}[0-9]{0,2}$");
			Matcher isNum = pattern.matcher(score);
			if (isNum.matches()) {
				achievementElement.setScore(Double.parseDouble(score));
			} else {
				achievementElement.setGrade(score);
			}
			/*
			 * 五级制
			 */
			/*
			if (achievementElement.getScore() >= 90) {
				achievementElement.setPoint(4.5);
			} else {
				if (achievementElement.getScore() >= 80) {
					achievementElement.setPoint(3.5);
				} else {
					if (achievementElement.getScore() >= 70) {
						achievementElement.setPoint(2.5);
					} else {
						if (achievementElement.getScore() >= 60) {
							achievementElement.setPoint(1.5);
						} else {
							achievementElement.setPoint(0.0);
						}
					}
				}
			
			
			}*/
			/*
			 * 常规制
			 */
			
			if (achievementElement.getScore() >= 50) {
				achievementElement.setPoint((achievementElement.getScore()-50)/10.0);				
				}else achievementElement.setPoint(0.0);

			achievementElement.setNewestMark(jsonObj.getDouble("makeupScore"));
		} catch (JSONException e) {
			achievementElement.setNewestMark(0.0);
			Log.e("jsonToAchievementElement", e.toString());

		}
		return achievementElement;

	}
}
