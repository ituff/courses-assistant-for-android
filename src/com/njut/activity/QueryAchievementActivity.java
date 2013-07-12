package com.njut.activity;

import java.util.ArrayList;
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
import com.njut.utility.JsonParse;
import com.njut.view.AchievementAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	private String year;//学年
	private String term;//学期	
	
	protected final int ACHIEVEMENT_GET_FINISHED = 1;
	protected final String RETURN_STRING = "returnString";
	
	private String TAG="QueryAchievementActivity";
	
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (progressDialog != null)
				progressDialog.dismiss();
			switch (msg.what) {
			case ACHIEVEMENT_GET_FINISHED: {
				Bundle bundle = msg.getData();
				try {
					finishAchievementGetOperation(bundle.getString(RETURN_STRING));
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	class AchievementGetThread extends Thread {
		public void run() {
			Message message = new Message();
			message.what = ACHIEVEMENT_GET_FINISHED;
			achievementService as = new achievementService();
			String termStr;
			if(term.equals("第一学期")) termStr="1";
			else termStr="2";
			String msg = as.get(year,termStr,"",kczlApplication.Person.getSchoolnumber());
			Bundle bundle = new Bundle();
			bundle.putString(RETURN_STRING, msg);
			message.setData(bundle);
			myHandler.sendMessage(message);
		}
	}

	private void finishAchievementGetOperation(String mStringReturnStr) throws JSONException{
		kczlApplication.AchievementString = mStringReturnStr;
		JSONArray jsonObjs = new JSONArray(mStringReturnStr);
		List<AchievementElement> achievementElements = new ArrayList<AchievementElement>();
		JsonParse jp = new JsonParse();
		for (int i = 0; i < 5; i++) {
	    //如果成绩数量太大，会在Nexus S上强制关闭，但是像Note2这样的2GB内存的机器不会发生这样的情况，建议优化UI部分代码
		//for (int i = 0; i < jsonObjs.length(); i++) {
			achievementElements.add(jp.jsonToAchievementElement((JSONObject) jsonObjs.opt(i)));
		}
		kczlApplication.AchievementElements = achievementElements;
		refreshAchievement();
	}
	
	private void refreshAchievement(){
		achievementListView = (ListView) findViewById(R.id.achievement_ListView);
		List<AchievementElement> list = new ArrayList<AchievementElement>();
		list=kczlApplication.AchievementElements;
		AchievementAdapter adapter = new AchievementAdapter(this, list);
		achievementListView.setAdapter(adapter);
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
		final RelativeLayout ls = (RelativeLayout) findViewById(R.id.RLayout1);
		TextView button1 = (TextView) findViewById(R.id.term_textView);
		backButton = (Button) findViewById(R.id.toolbar_nav_button);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu.showAsDropDown(ls);
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
				okButton.setOnClickListener(new View.OnClickListener() {//确定按钮点击事件，根据学年学期。。。。

					@Override
					public void onClick(View v) {
						AchievementGetThread  AGT = new AchievementGetThread ();
						progressDialog = ProgressDialog.show(QueryAchievementActivity.this,
								getString(R.string.state), getString(R.string.loading),
								true);
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
		List<AchievementElement> list = new ArrayList<AchievementElement>();
		list=kczlApplication.AchievementElements;
		/*
		list.add(new AchievementElement("微机原理与接口技术", 63, "必修课", 4));//成绩数据绑定
		list.add(new AchievementElement("专业英语", 86, "必修课", 2));
		list.add(new AchievementElement("算法设计与分析", 70, "必修课", 4));
		list.add(new AchievementElement("数据结构", 98, "必修课", 2));*/
		AchievementAdapter adapter = new AchievementAdapter(this, list);
		achievementListView.setAdapter(adapter);

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

	public void setYear(String year){
		this.year=year;
	}
	
	public void setTerm(String term){
		this.term=term;
	}
}
