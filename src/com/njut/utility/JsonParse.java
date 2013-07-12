package com.njut.utility;

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
		} catch (JSONException e) {
			Log.e("jsonToPersonElement", e.toString());
		}
		return person;
	}
	
	public Curriculum jsonToCurriculum(String jsonStr){
		Curriculum curriculum = new Curriculum();
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			curriculum.setEndTime(jsonObj.getString("endTime"));
			curriculum.setCourseBelonging(jsonObj.getString("courseBelonging"));
			curriculum.setStartTime(jsonObj.getString("startTime"));
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
			curriculum.setCredit(jsonObj.getDouble("credit"));
			curriculum.setPlace(jsonObj.getString("place"));
			curriculum.setChooseNumber(jsonObj.getString("chooseNumber"));
			curriculum.setCtid(jsonObj.getString("ctid"));
			curriculum.setTeacher(jsonObj.getString("teacher"));
		} catch (JSONException e) {
			Log.e("jsonToCurriculum", e.toString());
		}
		return curriculum;
	}
	
	public Curriculum jsonToCurriculum(JSONObject jsonObj){
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
			curriculum.setCredit(jsonObj.getDouble("credit"));
			curriculum.setPlace(jsonObj.getString("place"));
			curriculum.setChooseNumber(jsonObj.getString("choosenumber"));
			curriculum.setCtid(jsonObj.getString("ctid"));
			curriculum.setTeacher(jsonObj.getString("teacher"));
		} catch (JSONException e) {
			Log.e("jsonToCurriculum", e.toString());
		}
		return curriculum;
	}
	
	public AchievementElement jsonToAchievementElement(JSONObject jsonObj){
		AchievementElement achievementElement=new AchievementElement();
		try{
			achievementElement.setCourseName(jsonObj.getString("coursename"));
			achievementElement.setCredit(Float.parseFloat(jsonObj.getString("credit")));
			achievementElement.setScore(jsonObj.getInt("score"));
			achievementElement.setType(jsonObj.getString("courseNature"));
	} catch (JSONException e) {
		Log.e("jsonToAchievementElement", e.toString());
	}
	return achievementElement;
		
	}

}
