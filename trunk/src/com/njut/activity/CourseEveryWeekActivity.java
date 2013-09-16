package com.njut.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutlab.kczl.kczlApplication;
import org.nutlab.webService.curriculumService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.njut.R;
import com.njut.data.CourseElement;
import com.njut.data.Curriculum;
import com.njut.utility.AppUtils;
import com.njut.utility.CalendarHelper;
import com.njut.utility.ClassCoverClass;
import com.njut.utility.JsonParse;
import com.njut.utility.SpecialCalendar;
import com.njut.view.CourseAdapter;
import com.njut.view.CourseEveryWeekView;
import com.njut.view.OverlayView;
import com.umeng.analytics.MobclickAgent;

/**
 * 日历按周显示，显示课程
 * 
 * 
 * 
 */
public class CourseEveryWeekActivity extends Activity implements
		OnGestureListener {

	private String TAG = "CourseEveryWeekActivity";

	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CourseEveryWeekView calV = null;
	private GridView gridView = null;
	private TextView dateText = null;
	private TextView weekText;
	private TextView dayText;
	private Button backButton;
	private Button updateButton;
	private ProgressDialog progressDialog;
	private TextView topText = null;
	private Date firstDateOfWeek = null;

	private int jumpWeek = 0;

	private ListView courseListView;
	private List<CourseElement> list;
	private ReturnToTodayReceiver receiver;

	private Date selectedDate;
	private CourseAdapter adapter;
	private OverlayView overlayView;

	protected final int CURRICULUM_GET_FINISHED = 2;

	protected final String RETURN_STRING = "returnString";

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (progressDialog != null)
				progressDialog.dismiss();
			switch (msg.what) {
			case CURRICULUM_GET_FINISHED: {
				Bundle bundle = msg.getData();
				try {

					finishCurriculumGetOperation(bundle
							.getString(RETURN_STRING));
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
				break;
			}
			super.handleMessage(msg);
		}
	};

	protected void finishCurriculumGetOperation(String mStringReturnStr)
			throws JSONException {
		/*
		 * new AlertDialog.Builder(this).setTitle("数据")
		 * .setMessage(mStringReturnStr).setPositiveButton("是", null)
		 * .setNegativeButton("否", null).show();
		 */
		kczlApplication.CurriculumsString = mStringReturnStr;
		String PREFS_NAME = "org.nutlab.kczl";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("CurriculumsString", mStringReturnStr);
		editor.commit();
		JSONArray jsonObjs = new JSONArray(mStringReturnStr);
		List<Curriculum> curriculums = new ArrayList<Curriculum>();
		for (int i = 0; i < jsonObjs.length(); i++) {
			JsonParse jp = new JsonParse();
			curriculums.add(jp.jsonToCurriculum((JSONObject) jsonObjs.opt(i)));
		}
		kczlApplication.Curriculums = curriculums;
	}

	protected void finishCurriculumGetOperation(String mStringReturnStr,
			String currentDate) throws JSONException {
		/*
		 * new AlertDialog.Builder(this).setTitle("数据")
		 * .setMessage(mStringReturnStr).setPositiveButton("是", null)
		 * .setNegativeButton("否", null).show();
		 */
		kczlApplication.CurriculumsString = mStringReturnStr;
		String PREFS_NAME = "org.nutlab.kczl";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("CurriculumsString", mStringReturnStr);
		JSONArray jsonObjs = new JSONArray(mStringReturnStr);
		List<Curriculum> curriculums = new ArrayList<Curriculum>();
		for (int i = 0; i < jsonObjs.length(); i++) {
			JsonParse jp = new JsonParse();
			Curriculum curriculum = jp.jsonToCurriculum((JSONObject) jsonObjs
					.opt(i));
			CalendarHelper cHelper = new CalendarHelper();
			int day = cHelper.getDay(currentDate,
					kczlApplication.Person.getBegindate());
			if (day != curriculum.getDay())
				continue;
			int week = cHelper.getWeek(currentDate,
					kczlApplication.Person.getBegindate());
			if (week < curriculum.getBeginWeek()
					|| week > curriculum.getEndWeek())
				continue;
			curriculums.add(curriculum);
		}
		kczlApplication.Curriculums = curriculums;
	}

	class CurriculumGetThread extends Thread {
		public void run() {
			Message message = new Message();
			message.what = CURRICULUM_GET_FINISHED;
			curriculumService cs = new curriculumService();
			String msg = cs.get();
			String PREFS_NAME = "org.nutlab.kczl";
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("IsLogined", kczlApplication.IsLogined);
			editor.commit();
			Bundle bundle = new Bundle();
			bundle.putString(RETURN_STRING, msg);
			message.setData(bundle);
			myHandler.sendMessage(message);
		}
	}

	private void refreshAchievement() {
		courseListView = (ListView) findViewById(R.id.course_ListView);
		ClassCoverClass c2c = new ClassCoverClass();
		CalendarHelper cHelper = new CalendarHelper();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		int day = cHelper.getDay(sf.format(selectedDate),
				kczlApplication.Person.getBegindate());
		int week = cHelper.getWeek(sf.format(selectedDate),
				kczlApplication.Person.getBegindate());
		list = c2c.curriculumsToCourseElements(kczlApplication.Curriculums,
				day, week);
		adapter = new CourseAdapter(this, list);
		courseListView.setAdapter(adapter);
	}

	public CourseEveryWeekActivity() {

		Date date = new Date();
		selectedDate = date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, -(SpecialCalendar.getWeekdayOfDate(date)));
		firstDateOfWeek = gc.getTime();

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.course_week);
		dayText = (TextView) findViewById(R.id.day_textView);
		topText = (TextView) findViewById(R.id.date_textView);
		addTextToTopTextView(topText, selectedDate);
		setDayText(selectedDate);
		setWeekText(selectedDate);
		gestureDetector = new GestureDetector(this);
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		flipper.removeAllViews();
		calV = new CourseEveryWeekView(this, getResources(), jumpWeek,
				firstDateOfWeek, selectedDate);

		addGridView();
		gridView.setAdapter(calV);

		flipper.addView(gridView, 0);

		dateText = (TextView) findViewById(R.id.date_textView);
		courseListView = (ListView) findViewById(R.id.course_ListView);
		/*
		 * list = new ArrayList<CourseElement>(); list.add(new
		 * CourseElement("数据结构", 0, "刘斌", "厚学317", "15:40-17:20"));//将数据显示到列表中
		 */
		ClassCoverClass c2c = new ClassCoverClass();
		CalendarHelper cHelper = new CalendarHelper();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		int day = cHelper.getDay(sf.format(selectedDate),
				kczlApplication.Person.getBegindate());
		int week = cHelper.getWeek(sf.format(selectedDate),
				kczlApplication.Person.getBegindate());
		list = c2c.curriculumsToCourseElements(kczlApplication.Curriculums,
				day, week);
		adapter = new CourseAdapter(this, list);
		courseListView.setAdapter(adapter);
		courseListView.setOnItemClickListener(OnCourseListItemlistener);
		backButton = (Button) findViewById(R.id.toolbar_nav_button1);
		updateButton = (Button) findViewById(R.id.toolbar_update_button);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AppUtils.menu.showMenu();
			}
		});
		updateButton.setOnClickListener(new View.OnClickListener() {// 更新按钮点击事件

					@Override
					public void onClick(View v) {
						CurriculumGetThread CGT = new CurriculumGetThread();
						progressDialog = ProgressDialog.show(
								CourseEveryWeekActivity.this.getParent(),
								getString(R.string.state),
								getString(R.string.loading), true);
						CGT.start();
						// 刷新courseListView
						refreshAchievement();
					}
				});

		receiver = new ReturnToTodayReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CourseEveryWeekActivity.class.getName());
		registerReceiver(receiver, filter);
		RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
		overlayView = new OverlayView(this);
		root.addView(overlayView);
		setTopString();
	}

	private OnItemClickListener OnCourseListItemlistener = new OnItemClickListener() {// 课程列表项点击事件
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			CourseElement mCourseElement = list.get(arg2);
			Intent intent = new Intent(CourseEveryWeekActivity.this,
					TeachingEvaluationActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("coursename", mCourseElement.getCourseName());
			bundle.putString("courseNature", mCourseElement.getCourseNature());
			bundle.putString("credit", mCourseElement.getCredit());
			bundle.putString("ctid", mCourseElement.getCtid());
			bundle.putString("endtime", mCourseElement.getEndtime());
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);
		}
	};

	// 添加gridview
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		// 取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int Width = display.getWidth();
		int Height = display.getHeight();
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		// gridView.setColumnWidth(46);
		// if (Width == 480 && Height == 800) {
		gridView.setColumnWidth(Width / 7 + 1);
		// }
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框

		gridView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return CourseEveryWeekActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() { // gridView中的每一个item的点击事件

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Date date = calV.getDateByClickItem(position);
				SimpleDateFormat fymd = new SimpleDateFormat("yy/MM/dd");
				String d = fymd.format(date);
				setNewBg(selectedDate, date);
				selectedDate = date;
				calV.setSelectedDate(date);
				addTextToTopTextView(topText, selectedDate);
				setDayText(selectedDate);
				setWeekText(selectedDate);
				refreshAchievement();
			}

		});
		gridView.setLayoutParams(params);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,// 滑动事件
			float velocityY) {
		int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 50) {
			// 像左滑动
			overlayView.setVisibility(View.INVISIBLE);
			addGridView(); // 添加一个gridView
			jumpWeek++; // 下一个月

			calV = new CourseEveryWeekView(this, getResources(), jumpWeek,
					firstDateOfWeek, selectedDate);
			gridView.setAdapter(calV);
			gvFlag++;
			flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			this.flipper.showNext();
			flipper.removeViewAt(0);
			setTopString();
			return true;
		} else if (e1.getX() - e2.getX() < -50) {
			// 向右滑动
			overlayView.setVisibility(View.INVISIBLE);
			addGridView(); // 添加一个gridView
			jumpWeek--; // 上一个月

			calV = new CourseEveryWeekView(this, getResources(), jumpWeek,
					firstDateOfWeek, selectedDate);
			gridView.setAdapter(calV);
			gvFlag++;
			// addTextTodateTextView(dateText);
			// flipper.addView(gridView);
			flipper.addView(gridView, gvFlag);

			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
		}
		return false;
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	// // 添加头部的年份 闰哪月等信息
	// public void addTextTodateTextView(TextView view) {
	// StringBuffer textDate = new StringBuffer();
	// // draw = getResources().getDrawable(R.drawable.top_day);
	// // view.setBackgroundDrawable(draw);
	// textDate.append(calV.getShowYear()).append("\t").append("年").append(
	// calV.getShowMonth()).append("\t").append("月");
	// ;
	// view.setText(textDate);
	//
	// }

	private void addTextToTopTextView(TextView view, Date date) {
		StringBuffer textDate = new StringBuffer();
		textDate.append(date.getYear() + 1900).append(" ").append("年")
				.append(date.getMonth() + 1).append(" ").append("月");
		view.setText(textDate.toString());

	}

	private void setTopString() {
		dateText.setText(calV.getTopString());

	}

	private class ReturnToTodayReceiver extends BroadcastReceiver {// 回到今天

		@Override
		public void onReceive(Context context, Intent intent) {
			Date date = new Date();
			setDayText(date);
			setWeekText(selectedDate);
			addTextToTopTextView(topText, selectedDate);
			refreshAchievement();
			if (jumpWeek == 0) {
				if (!AppUtils.areDatesSame(date, selectedDate)) {
					setNewBg(selectedDate, date);
					selectedDate = date;
				}
				return;
			}
			selectedDate = date;

			addGridView(); // 添加一个gridView
			int temp = jumpWeek;
			int gvFlag = 0;
			jumpWeek = 0;

			calV = new CourseEveryWeekView(CourseEveryWeekActivity.this,
					getResources(), 0, firstDateOfWeek, selectedDate);
			gridView.setAdapter(calV);
			flipper.addView(gridView, ++gvFlag);

			if (temp < 0) {
				CourseEveryWeekActivity.this.flipper
						.setInAnimation(AnimationUtils.loadAnimation(
								CourseEveryWeekActivity.this,
								R.anim.push_left_in));
				CourseEveryWeekActivity.this.flipper
						.setOutAnimation(AnimationUtils.loadAnimation(
								CourseEveryWeekActivity.this,
								R.anim.push_left_out));
				CourseEveryWeekActivity.this.flipper.showNext();
			} else {

				CourseEveryWeekActivity.this.flipper
						.setInAnimation(AnimationUtils.loadAnimation(
								CourseEveryWeekActivity.this,
								R.anim.push_right_in));
				CourseEveryWeekActivity.this.flipper
						.setOutAnimation(AnimationUtils.loadAnimation(
								CourseEveryWeekActivity.this,
								R.anim.push_right_out));
				CourseEveryWeekActivity.this.flipper.showPrevious();
			}
			CourseEveryWeekActivity.this.flipper.removeViewAt(0);
			setTopString();
		}
	}

	private void setNewBg(Date oldDate, Date newDate) {
		if (calV.contain(oldDate)) {
			int last = SpecialCalendar.getWeekdayOfDate(oldDate);
			TextView lastText = (TextView) ((gridView.getChildAt(last))
					.findViewById(R.id.tvtext));
			lastText.setBackgroundColor(Color.WHITE);
			lastText.setTextColor(Color.GRAY);
		}
		int current = SpecialCalendar.getWeekdayOfDate(newDate);
		TextView CurrentText = (TextView) ((gridView.getChildAt(current))
				.findViewById(R.id.tvtext));
		darwOverlay(current);
		CurrentText.setTextColor(Color.BLACK);
	}

	private void setDayText(Date date) {
		if (AppUtils.areDatesSame(new Date(), date)) {
			dayText.setText(R.string.today);
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
		dayText.setText(sdf.format(date));
	}

	private void setWeekText(Date date) {
		weekText = (TextView) findViewById(R.id.week_textView);
		CalendarHelper cHelper = new CalendarHelper();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (sf.parse(kczlApplication.Person.getBegindate()).getTime() > selectedDate
					.getTime()) {
				weekText.setText("");
			} else {
				int week = cHelper.getWeek(sf.format(selectedDate),
						kczlApplication.Person.getBegindate());
				weekText.setText("第" + week + "周");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	public void darwOverlay(int position) {
		if (overlayView.getVisibility() != View.VISIBLE)
			overlayView.setVisibility(View.VISIBLE);
		overlayView.darwOverlay(position);

	}

}