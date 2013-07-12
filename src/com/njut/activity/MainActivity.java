package com.njut.activity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.njut.R;
import com.njut.utility.AppUtils;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*主界面，侧边栏菜单的实现*/
public class MainActivity extends ActivityGroup {
    /** Called when the activity is first created. */
	
	private SlidingMenu menu;
	private RelativeLayout timetable_Layout, achievement_layout;
	
	private LinearLayout linear;
	
	private TextView timetableText;
	private TextView achievementtableText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		linear=(LinearLayout)findViewById(R.id.mian);
		menu = new SlidingMenu(this);
	        menu.setMode(SlidingMenu.LEFT);
	        menu.setTouchmodeMarginThreshold(100);
	        menu.setShadowWidthRes(R.dimen.shadow_width);
	        menu.setShadowDrawable(R.drawable.shadow);
	        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	        menu.setFadeDegree(0.1f);
	        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	        menu.setMenu(R.layout.slide_menu);
	        this.findViews();
	        timetableText.setTextColor(Color.BLUE);
	        this.setListener();
	        linear.removeAllViews();
			Intent intent=new Intent(MainActivity.this,WeekAndMonthTabActivity.class);
			View view = MainActivity.this.getLocalActivityManager()
			.startActivity("suibian", intent).getDecorView();			
			linear.addView(view);
			AppUtils.menu=menu;
    }
    
    public void findViews() {
		timetable_Layout = (RelativeLayout) findViewById(R.id.timetable_RelativeLayout);
		achievement_layout = (RelativeLayout) findViewById(R.id.achievement_RelativeLayout);
		timetableText = (TextView)timetable_Layout.findViewById(R.id.timetable_menu_TextView);
		achievementtableText = (TextView) achievement_layout.findViewById(R.id.achievement_menu_TextView);
	
	}
    public void setListener() {
		timetable_Layout.setOnClickListener(clickListener_home);
		achievement_layout.setOnClickListener(clickListener_style);
		
	}
    
    private OnClickListener clickListener_home = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			linear.removeAllViews();
		//	achievementtableText.setTextColor(R.drawable.white);
			//timetableText.setTextColor(R.drawable.blue);
			timetableText.setTextColor(Color.BLUE);
			achievementtableText.setTextColor(Color.WHITE);
			Intent intent=new Intent(MainActivity.this,WeekAndMonthTabActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			View view = MainActivity.this.getLocalActivityManager()
			.startActivity("suibian", intent).getDecorView();	//两个不能一样的
			linear.addView(view,new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			menu.showContent();
		}
	};
	
	private OnClickListener clickListener_style = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			//achievementtableText.setTextColor(R.drawable.blue);
			linear.removeAllViews();
			timetableText.setTextColor(Color.WHITE);
			achievementtableText.setTextColor(Color.BLUE);
			Intent intent=new Intent(MainActivity.this,QueryAchievementActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			View view = MainActivity.this.getLocalActivityManager()
			.startActivity("suibian1", intent).getDecorView();	//两个不能一样的
			linear.addView(view,new LinearLayout.LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			menu.showContent();
			
		}
	};
}