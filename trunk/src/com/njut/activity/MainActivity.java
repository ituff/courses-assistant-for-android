package com.njut.activity;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.nutlab.kczl.kczlApplication;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.njut.R;
import com.njut.utility.AppUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*主界面，侧边栏菜单的实现*/
public class MainActivity extends ActivityGroup {
	/** Called when the activity is first created. */

	private SlidingMenu menu;
	private RelativeLayout timetable_Layout, achievement_layout;

	private LinearLayout linear;
 
	private TextView timetableText;
	private TextView achievementtableText;
	private RelativeLayout config_layout,message_layout;
	private TextView configText;
	private ImageView faceImage;
	
	private TextView nameTextView;
	private TextView classTextView;
	private TextView specialtyTextView;

	private  Boolean isExit =false;      
    private  Boolean hasTask =false;
    Timer tExit =new Timer();
    TimerTask task =new TimerTask() {
            @Override          
            public void run() {              
                    isExit =false;              
                    hasTask =true;          
                    }      
            };
    @Override
    public boolean dispatchKeyEvent( KeyEvent event) {
                    // TODO Auto-generated method stub
    	if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                            if(isExit ==false ) {
                                    isExit =true; 
                                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                                    if(!hasTask) {
                                            tExit.schedule(task, 2000);  
                                    }
                            } else {
//                            	Intent intent = new Intent(
//    									this,
//    									WelcomeActivity.class);
//                            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    							startActivity(intent);
                            	for(Iterator it=kczlApplication.activitiesList.iterator();it.hasNext();){
                            		Activity mActivity=(Activity)it.next();
                            		if(mActivity!=null){
                            			mActivity.finish();
                            		}
                            		
                            	}
                            } 
                        
                            return false; 
                    }                  
    	return super.dispatchKeyEvent(event);          
    }
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			return true;
//		}
//		return super.dispatchKeyEvent(event);
//	}

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
		kczlApplication.activitiesList.add(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); 
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.update(this);
		setContentView(R.layout.main);
		linear = (LinearLayout) findViewById(R.id.mian);
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
		timetableText.setTextColor(Color.BLACK);
		this.setListener();
		linear.removeAllViews();
		Intent intent = new Intent(MainActivity.this,
				WeekAndMonthTabActivity.class);
		View view = MainActivity.this.getLocalActivityManager()
				.startActivity("suibian", intent).getDecorView();
		linear.addView(view);
		AppUtils.menu = menu;
		nameTextView=(TextView)findViewById(R.id.name_TextView);
		classTextView=(TextView)findViewById(R.id.class_TextView);
		specialtyTextView=(TextView)findViewById(R.id.specialty_TextView);
		nameTextView.setText(kczlApplication.Person.getRealname());
		classTextView.setText(kczlApplication.Person.getNatureclassname());
		specialtyTextView.setText(kczlApplication.Person.getFieldName());
	}

	public void findViews() {
		timetable_Layout = (RelativeLayout) findViewById(R.id.timetable_RelativeLayout);
		achievement_layout = (RelativeLayout) findViewById(R.id.achievement_RelativeLayout);
		message_layout=(RelativeLayout) findViewById(R.id.message_RelativeLayout);
		faceImage=(ImageView) findViewById(R.id.face_ImageView);
		Drawable drawable = getResources().getDrawable(R.drawable.face_default);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		BitmapDrawable bbb = new BitmapDrawable(toRoundCorner(bitmap, 10));
		faceImage.setBackgroundDrawable(bbb);
		timetableText = (TextView) timetable_Layout
				.findViewById(R.id.timetable_menu_TextView);
		achievementtableText = (TextView) achievement_layout
				.findViewById(R.id.achievement_menu_TextView);
		config_layout = (RelativeLayout) findViewById(R.id.config_RelativeLayout);
		configText = (TextView) config_layout
				.findViewById(R.id.config_TextView);
	}

	public void setListener() {
		timetable_Layout.setOnClickListener(clickListener_home);
		achievement_layout.setOnClickListener(clickListener_style);
		config_layout.setOnClickListener(clickListener_config);
		message_layout.setOnClickListener(clickListener_message);
	}

	private OnClickListener clickListener_home = new OnClickListener() {
		@Override
		public void onClick(View v) {

			linear.removeAllViews();
//			timetableText.setTextColor(Color.BLACK);
//			achievementtableText.setTextColor(Color.WHITE);
//			configText.setTextColor(Color.WHITE);
			Intent intent = new Intent(MainActivity.this,
					WeekAndMonthTabActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			View view = MainActivity.this.getLocalActivityManager()
					.startActivity("suibian", intent).getDecorView(); // 两个不能一样的
			linear.addView(view, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			menu.showContent();
		}
	};

	private OnClickListener clickListener_style = new OnClickListener() {
		@Override
		public void onClick(View v) {
			linear.removeAllViews();
//			timetableText.setTextColor(Color.WHITE);
//			achievementtableText.setTextColor(Color.BLACK);
//			configText.setTextColor(Color.WHITE);
			Intent intent = new Intent(MainActivity.this,
					QueryAchievementActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			View view = MainActivity.this.getLocalActivityManager()
					.startActivity("suibian1", intent).getDecorView(); // 两个不能一样的
			linear.addView(view, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			menu.showContent();

		}
	};

	private OnClickListener clickListener_config = new OnClickListener() {
		@Override
		public void onClick(View v) {
			linear.removeAllViews();
//			timetableText.setTextColor(Color.WHITE);
//			achievementtableText.setTextColor(Color.WHITE);
//			configText.setTextColor(Color.BLACK);
			Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			View view = MainActivity.this.getLocalActivityManager()
					.startActivity("suibian2", intent).getDecorView(); // 两个不能一样的
			linear.addView(view, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			menu.showContent();

		}
	};
	
	private OnClickListener clickListener_message= new OnClickListener() {
		@Override
		public void onClick(View v) {
			linear.removeAllViews();
//			timetableText.setTextColor(Color.WHITE);
//			achievementtableText.setTextColor(Color.WHITE);
//			configText.setTextColor(Color.BLACK);
			Intent intent = new Intent(MainActivity.this, MessageActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			View view = MainActivity.this.getLocalActivityManager()
					.startActivity("suibian3", intent).getDecorView(); // 两个不能一样的
			linear.addView(view, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			menu.showContent();

		}
	};
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}