package org.nutlab.kczl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.content.Context;
import android.content.SharedPreferences;

import com.njut.data.AchievementElement;
import com.njut.data.CookieElement;
import com.njut.data.CourseElement;
import com.njut.data.Curriculum;
import com.njut.data.PersonElement;
import com.njut.utility.JsonParse;

public class kczlApplication extends android.app.Application {
	/** 用户名 **/
	public static String UserName;
	/** 密码 **/
	public static String PassWord;
	/** 服务器地址 **/
	public static String ServerUri = "202.119.248.68";
	/** 是否登录 **/
	public static int IsLogined = 0;
	/** 学生身份 **/
	public static String PersonString;
	public static PersonElement Person;
	/** Cookie信息 **/
	public static CookieElement sCookie;
	public static CookieStore Cookies;
	/** 课程信息 **/
	public static List<Curriculum> Curriculums=new ArrayList<Curriculum>();
	public static String CurriculumsString;
	public static List<CourseElement> CourseElements=new ArrayList<CourseElement>();
	/** 成绩信息 **/
	public static List<AchievementElement> AchievementElements=new ArrayList<AchievementElement>();
	public static String AchievementString;

	public void onCreate() {
		getParms();

}

	public void onTerminate() {
		saveParms();
		super.onTerminate();
	}

	public void onLowMemory() {
		saveParms();
		super.onLowMemory();
	}
	
	private void getParms() {
		String PREFS_NAME = "org.nutlab.kczl";		  
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 		  
		PersonString = settings.getString("PersonString", PersonString);
		UserName = settings.getString("UserName", UserName);  
		PassWord = settings.getString("PassWord", PassWord);  
		ServerUri = settings.getString("ServerUri", ServerUri); 
		IsLogined = settings.getInt("IsLogined", IsLogined);
		AchievementString=settings.getString("AchievementString",  AchievementString);
		sCookie.setEmail(settings.getString("email", sCookie.getEmail())); 
		sCookie.setSessioncode(settings.getString("sessioncode", sCookie.getSessioncode()));
		JsonParse jp=new JsonParse();
		Person = jp.jsonToPersonElement(settings.getString("PersonString", PersonString));
		CurriculumsString=settings.getString("CurriculumsString",CurriculumsString ); 
	}
	
	private void  saveParms() {
		String PREFS_NAME = "org.nutlab.kczl";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("PersonString", PersonString);
		editor.putString("UserName", UserName);
		editor.putString("PassWord", PassWord);
		editor.putInt("IsLogined", IsLogined);
		editor.putString("ServerUri", ServerUri);
		editor.putString("email", sCookie.getEmail());
		editor.putString("sessioncode", sCookie.getSessioncode());
		editor.putString("CurriculumsString",CurriculumsString );
		editor.putString("AchievementString",  AchievementString);
		editor.commit();
	}

}
