package com.njut.activity;

import com.njut.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.view.Window;

import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

/*周和月tab*/
public class WeekAndMonthTabActivity extends TabActivity {

	private TabHost tabHost;

	private View view1;
	private View view2;
	Dialog accountAlertDialog;
	  private Button todayButton;

	/** Called when the activity is first created. */
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabhost);
		tabHost = this.getTabHost();
		Intent intent = new Intent(this, CourseEveryWeekActivity.class);
		

		Intent intent1 = new Intent(this, CourseEveryMonthActivity.class);
		
		view1 = View.inflate(WeekAndMonthTabActivity.this, R.layout.tabcontent, null);
		TextView tabLeft = (TextView) view1.findViewById(R.id.tab_TextView);
		tabLeft.setText(R.string.week);
		TabHost.TabSpec spec1 = tabHost.newTabSpec(
				getResources().getString(R.string.week)).setIndicator(view1)
				.setContent(intent);
		tabHost.addTab(spec1);
		view2 = View.inflate(WeekAndMonthTabActivity.this, R.layout.tabcontent, null);
		TextView tabRight = (TextView) view2.findViewById(R.id.tab_TextView);
		tabRight.setText(R.string.month);
		tabRight.setBackgroundResource(R.drawable.tab_right);
		TabHost.TabSpec spec2 = tabHost.newTabSpec(
				getResources().getString(R.string.month)).setIndicator(view2)
				.setContent(intent1);
		tabHost.addTab(spec2);
		
		todayButton=(Button) findViewById(R.id.today_Button);
		todayButton.setOnClickListener(new View.OnClickListener() {//今天按钮点击事件

					@Override
					public void onClick(View v) {
						Intent intent=new Intent();
						 if(tabHost.getCurrentTab()==0){
							 intent.setAction(CourseEveryWeekActivity.class.getName());
						 }
						 else{
							 intent.setAction(CourseEveryMonthActivity.class.getName());
						 }
						 Log.v("Action", intent.getAction());
						 sendBroadcast(intent);
					}
				});

	}
	
}