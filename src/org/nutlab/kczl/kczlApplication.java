package org.nutlab.kczl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.CookieStore;
import org.nutlab.kczl.notification.MyNoticeReceiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import cn.jpush.android.api.JPushInterface;

import com.njut.data.AchievementElement;
import com.njut.data.CookieElement;
import com.njut.data.CourseElement;
import com.njut.data.Curriculum;
import com.njut.data.PersonElement;
import com.njut.utility.JsonParse;
import com.umeng.update.UmengUpdateAgent;

public class kczlApplication extends android.app.Application {
	/** 用户名 **/
	public static String UserName;
	/** 密码 **/
	public static String PassWord;
	/** 服务器地址 **/
	public static String ServerUri = "app.njut.org.cn";
	/** 是否登录 **/
	public static int IsLogined = 0;
	/** 学生身份 **/
	public static String PersonString;
	public static PersonElement Person;
	/** Cookie信息 **/
	public static CookieElement sCookie;
	public static CookieStore Cookies;
	/** 课程信息 **/
	public static List<Curriculum> Curriculums = new ArrayList<Curriculum>();
	public static String CurriculumsString;
	public static List<CourseElement> CourseElements = new ArrayList<CourseElement>();
	/** 成绩信息 **/
	public static List<AchievementElement> AchievementElements = new ArrayList<AchievementElement>();
	public static String AchievementString;
	/** 是否离线 **/
	public static int IsOffLine = 0;
	/** 定时时间 **/
	public static int isClocked=1;
	/** 推送开关 **/
	public static int isNotification=1;
	/**学期信息**/
	public static String Year;
	public static String Term;

	public void onCreate() {
		// getParms();
		super.onCreate();
		String PREFS_NAME = "org.nutlab.kczl";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		kczlApplication.PersonString = settings.getString("PersonString",
				kczlApplication.PersonString);
		if(kczlApplication.PersonString!=null){
		JsonParse jp = new JsonParse();
		kczlApplication.Person = jp.jsonToPersonElement(settings.getString(
				"PersonString", kczlApplication.PersonString));
		JPushInterface.setDebugMode(true);
		Set<String> tagSet=new HashSet<String>();
		tagSet.add(new String(kczlApplication.Person.getBirthday()));
		tagSet.add(new String(kczlApplication.Person.getCampusname()));
		tagSet.add(new String(kczlApplication.Person.getFieldName()));
		tagSet.add(new String(kczlApplication.Person.getGrade()));
		tagSet.add(new String(kczlApplication.Person.getNatureclassname()));
		tagSet.add(new String(kczlApplication.Person.getSex()));
		tagSet.add(new String(kczlApplication.Person.getUniversityname()));
		JPushInterface.setAliasAndTags(this, kczlApplication.Person.getSchoolnumber(),tagSet );}
		JPushInterface.init(this);
		/*String PREFS_NAME = "org.nutlab.kczl";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		isClocked=settings.getInt("isClocked", isClocked);
		isNotification=settings.getInt("isNotification", isNotification);
		if(isNotification==1){
		if(isClocked==0){
		setReminder();
		isClocked=1;
		String PREFS_NAME2 = "org.nutlab.kczl";
		SharedPreferences settings2 = getSharedPreferences(PREFS_NAME2, 0);
		SharedPreferences.Editor editor = settings2.edit();
		editor.putInt("isClocked", isClocked);
		editor.commit();
		}}*/
	}

	public void onTerminate() {
		// saveParms();
		super.onTerminate();
	}

	public void onLowMemory() {
		// saveParms();
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
		AchievementString = settings.getString("AchievementString",
				AchievementString);
		sCookie.setEmail(settings.getString("email", sCookie.getEmail()));
		sCookie.setSessioncode(settings.getString("sessioncode",
				sCookie.getSessioncode()));
		JsonParse jp = new JsonParse();
		Person = jp.jsonToPersonElement(settings.getString("PersonString",
				PersonString));
		CurriculumsString = settings.getString("CurriculumsString",
				CurriculumsString);
	}

	private void saveParms() {
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
		editor.putString("CurriculumsString", CurriculumsString);
		editor.putString("AchievementString", AchievementString);
		editor.commit();
	}
	
	private void setReminder() {  
        
        // get the AlarmManager instance   
        AlarmManager am= (AlarmManager) getSystemService(ALARM_SERVICE);  
        // create a PendingIntent that will perform a broadcast  
        PendingIntent pi= PendingIntent.getBroadcast(kczlApplication.this, 0, new Intent(kczlApplication.this,MyNoticeReceiver.class), 0);  
        //7点响
        am.setRepeating(AlarmManager.RTC_WAKEUP,25200000 ,86400000, pi); 
        //Calendar c=Calendar.getInstance();
        //am.set(AlarmManager.RTC_WAKEUP,SystemClock.elapsedRealtime() +1000, pi); 
        //am.cancel(pi);
    }  

	public static List<Activity> activitiesList=new ArrayList<Activity>();
}
