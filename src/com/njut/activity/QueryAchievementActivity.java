package com.njut.activity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutlab.kczl.kczlApplication;
import org.nutlab.webService.achievementService;
import org.nutlab.webService.curriculumService;

import com.njut.R;
import com.njut.R.string;
import com.njut.activity.LoginActivity.CurriculumGetThread;
import com.njut.data.AchievementElement;
import com.njut.data.Curriculum;
import com.njut.utility.AppUtils;
import com.njut.utility.ComparatorAchievementElement;
import com.njut.utility.GPACal;
import com.njut.utility.JsonParse;
import com.njut.utility.sortHelper;
import com.njut.view.AchievementAdapter;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/*查询成绩*/
public class QueryAchievementActivity extends Activity {
	private PopMenu popMenu;
	private Context context;
	private ListView achievementListView;
	private Button backButton;
	private Button okButton;
	private View abortButton;
	private ProgressDialog progressDialog;
	private String year;// 学年
	private String term;// 学期
	private TextView gpaTV;

	protected final int ACHIEVEMENT_GET_FINISHED = 1;
	protected final String RETURN_STRING = "returnString";

	private String TAG = "QueryAchievementActivity";

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (progressDialog != null)
				progressDialog.dismiss();
			switch (msg.what) {
			case ACHIEVEMENT_GET_FINISHED: {
				refreshAchievement();
			}
				break;
			}
			super.handleMessage(msg);
		}
	};
	private RelativeLayout tip;
	private List<AchievementElement> list;
	private AchievementAdapter adapter;

	class AchievementGetThread extends Thread {
		public void run() {
			Message message = new Message();
			message.what = ACHIEVEMENT_GET_FINISHED;
			achievementService as = new achievementService();

			String termStr;
			String msg;
			if(year.equals("所有学年")){
				msg = as.get(kczlApplication.Person.getSchoolnumber());			
			}else{if(term.equals("所有学期")){
				msg = as.get(kczlApplication.Person.getSchoolnumber(),year);		
			}else{
			if (term.equals("第一学期"))
				termStr = "1";
			else
				termStr = "2";			
			msg = as.get(year, termStr, "",
					kczlApplication.Person.getSchoolnumber());}}
			String PREFS_NAME = "org.nutlab.kczl";
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("IsLogined", kczlApplication.IsLogined);
			editor.commit();

			try {
				finishAchievementGetOperation(msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myHandler.sendMessage(message);
		}
	}

	private void finishAchievementGetOperation(String mStringReturnStr)// 不要写在UI线程上
			throws JSONException {
		list.clear();
		kczlApplication.AchievementString = mStringReturnStr;
		JSONArray jsonObjs = new JSONArray(mStringReturnStr);
		JsonParse jp = new JsonParse();
		for (int i = 0; i < jsonObjs.length(); i++) {
			list.add(jp.jsonToAchievementElement((JSONObject) jsonObjs.opt(i)));
		}
		//ComparatorAchievementElement comparator = new ComparatorAchievementElement();
		//Collections.sort(list, comparator);
		sortHelper sHelper=new sortHelper();
		list=sHelper.AchievementElementDESC(list);
		kczlApplication.AchievementElements = list;

	}

	private void refreshAchievement() {// 删除冗余代码		
		adapter.notifyDataSetChanged();
		gpaTV=(TextView) findViewById(R.id.gpa);
		gpaTV.setVisibility(View.VISIBLE);
		DecimalFormat df = new DecimalFormat("#0.00");   
		GPACal gpaCal=new GPACal();
		double wholeGPA=gpaCal.getWholeGPA(list);
		double degreeGPA=gpaCal.getDegreeGPA(list);
		if(degreeGPA==-1000.0) gpaTV.setText("全程GPA："+df.format(wholeGPA)+"\n仅提供2010级以后学位GPA查询。");
		else
		gpaTV.setText("全程GPA："+df.format(wholeGPA)+"\n学位GPA："+df.format(degreeGPA));
	}
	
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(QueryAchievementActivity.this,
					CourseEveryWeekActivity.class);
		    startActivityForResult(intent, 0);  
			//QueryAchievementActivity.this.finish();
			
		}
		return super.dispatchKeyEvent(event);
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.query_achievement);
		context = QueryAchievementActivity.this;
		popMenu = new PopMenu(context);
		// 菜单项点击监听器
		// popMenu.setOnItemClickListener(popmenuItemClickListener);
		tip = (RelativeLayout) findViewById(R.id.tip_RelativeLayout);
		final TextView button1 = (TextView) findViewById(R.id.term_textView);
		backButton = (Button) findViewById(R.id.toolbar_nav_button);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				year="所有学年";
				term="所有学年";
				popMenu.showAsDropDown(button1);
				abortButton = (Button) popMenu.getView().findViewById(
						R.id.abort_popup_button);
				abortButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						popMenu.dismiss();
					}
				});
				okButton = (Button) popMenu.getView().findViewById(
						R.id.ok_popup_button);
				okButton.setOnClickListener(new View.OnClickListener() {// 确定按钮点击事件，根据学年学期。。。。

					@Override
					public void onClick(View v) {
						if (tip.getVisibility() == View.VISIBLE)
							tip.setVisibility(View.GONE);
						AchievementGetThread AGT = new AchievementGetThread();
						progressDialog = ProgressDialog.show(
								QueryAchievementActivity.this,
								getString(R.string.state),
								getString(R.string.loading), true);
						AGT.start();

						Log.v("hahah", year);
						Log.v("hahah", term);
						popMenu.dismiss();
					}
				});
			}
		});
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AppUtils.menu.showMenu();

			}
		});
		achievementListView = (ListView) findViewById(R.id.achievement_ListView);
		list = new ArrayList<AchievementElement>();
		// list = kczlApplication.AchievementElements;
		/*
		 * list.add(new AchievementElement("微机原理与接口技术", 63, "必修课", 4));//成绩数据绑定
		 * list.add(new AchievementElement("专业英语", 86, "必修课", 2)); list.add(new
		 * AchievementElement("算法设计与分析", 70, "必修课", 4)); list.add(new
		 * AchievementElement("数据结构", 98, "必修课", 2));
		 */
		
		String PREFS_NAME = "org.nutlab.kczl";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String Year=settings.getString("Year","");
		String Term=settings.getString("Term","");
		adapter = new AchievementAdapter(this, list);
		achievementListView.setAdapter(adapter);
		if(Year.length()>1&&Term.length()>1){
			year=Year;
			term=Term;
			AchievementGetThread AGT = new AchievementGetThread();
			progressDialog = ProgressDialog.show(
					QueryAchievementActivity.this,
					getString(R.string.state),
					getString(R.string.loading), true);
			AGT.start();
	
		}

		

	}

	// 弹出菜单监听器
	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// System.out.println("下拉菜单点击" + position);
			Toast.makeText(view.getContext(), "press--->>", Toast.LENGTH_LONG)
					.show();
			popMenu.dismiss();
		}
	};

	public void setYear(String year) {
		this.year = year;
	}

	public void setTerm(String term) {
		this.term = term;
	}
}
