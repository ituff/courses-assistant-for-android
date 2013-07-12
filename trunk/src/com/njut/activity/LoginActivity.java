package com.njut.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutlab.kczl.kczlApplication;
import org.nutlab.webService.curriculumService;
import org.nutlab.webService.loginService;

import com.njut.R;
import com.njut.data.CourseElement;
import com.njut.data.Curriculum;
import com.njut.utility.JsonParse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/*登陆*/
public class LoginActivity extends Activity {

	private String TAG = "LoginActivity";

	private Button loginButton;
	private EditText userNameET;
	private EditText passWordET;
	private ProgressDialog progressDialog;

	protected final int LOGIN_FINISHED = 1;
	protected final int CURRICULUM_GET_FINISHED = 2;

	protected final String RETURN_STRING = "returnString";

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (progressDialog != null)
				progressDialog.dismiss();
			switch (msg.what) {
			case LOGIN_FINISHED: {
				Bundle bundle = msg.getData();
				try {
					finishLoginOperation(bundle.getString(RETURN_STRING));
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
				break;
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

	class CurriculumGetThread extends Thread {
		public void run() {
			Message message = new Message();
			message.what = CURRICULUM_GET_FINISHED;
			curriculumService cs = new curriculumService();
			String msg = cs.get();
			Bundle bundle = new Bundle();
			bundle.putString(RETURN_STRING, msg);
			message.setData(bundle);
			myHandler.sendMessage(message);
		}
	}

	class LoginThread extends Thread {
		public void run() {
			Message message = new Message();
			message.what = LOGIN_FINISHED;
			loginService ls = new loginService();
			kczlApplication.UserName = userNameET.getText().toString();
			kczlApplication.PassWord = passWordET.getText().toString();
			String msg = ls.login(kczlApplication.UserName,
					kczlApplication.PassWord);
			Bundle bundle = new Bundle();
			bundle.putString(RETURN_STRING, msg);
			message.setData(bundle);
			myHandler.sendMessage(message);
		}
	}

	protected void finishCurriculumGetOperation(String mStringReturnStr)
			throws JSONException {
		/*new AlertDialog.Builder(this).setTitle("数据")
				.setMessage(mStringReturnStr).setPositiveButton("是", null)
				.setNegativeButton("否", null).show();*/
		kczlApplication.CurriculumsString = mStringReturnStr;
		JSONArray jsonObjs = new JSONArray(mStringReturnStr);
		List<Curriculum> curriculums = new ArrayList<Curriculum>();
		for (int i = 0; i < jsonObjs.length(); i++) {
			JsonParse jp = new JsonParse();
			curriculums.add(jp.jsonToCurriculum((JSONObject) jsonObjs.opt(i)));
		}
		kczlApplication.Curriculums = curriculums;

		//进入系统
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);

	}

	protected void finishLoginOperation(String mStringReturnStr)
			throws JSONException {

		JSONObject jsonObj = new JSONObject(mStringReturnStr);
		if (((String) jsonObj.keys().next()).equals("msg")) {
			new AlertDialog.Builder(this).setTitle("登录状态")
					.setMessage(jsonObj.getString("msg"))
					.setPositiveButton("是", null).setNegativeButton("否", null)
					.show();

		} else {
			/*
			 * new AlertDialog.Builder(this) .setTitle("登录状态")
			 * .setMessage(mStringReturnStr) .setPositiveButton("是", null)
			 * .setNegativeButton("否", null) .show();
			 */
			kczlApplication.IsLogined=1;
			JsonParse jp = new JsonParse();
			kczlApplication.Person = jp.jsonToPersonElement(mStringReturnStr);
			kczlApplication.PersonString = mStringReturnStr;
			// 登录成功后开始载入课程表信息
			CurriculumGetThread CGT = new CurriculumGetThread();
			progressDialog = ProgressDialog.show(LoginActivity.this,
					getString(R.string.state), getString(R.string.loading),
					true);
			CGT.start();

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(kczlApplication.IsLogined==1){
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
		}else{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
		Drawable drawable = getResources().getDrawable(R.drawable.login_bg);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		BitmapDrawable bbb = new BitmapDrawable(toRoundCorner(bitmap, 30));
		layout.setBackgroundDrawable(bbb);

		userNameET = (EditText) findViewById(R.id.editText1);
		passWordET = (EditText) findViewById(R.id.editText2);
		loginButton = (Button) findViewById(R.id.login_button);

		loginButton.setOnClickListener(new View.OnClickListener() {// 登陆按钮点击事件

					@Override
					public void onClick(View v) {
						LoginThread LT = new LoginThread();
						progressDialog = ProgressDialog.show(
								LoginActivity.this, getString(R.string.state),
								getString(R.string.logining), true);
						LT.start();

						/*
						 * Intent intent=new
						 * Intent(LoginActivity.this,MainActivity.class);
						 * startActivity(intent);
						 */
					}
				});
		}
	}

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
