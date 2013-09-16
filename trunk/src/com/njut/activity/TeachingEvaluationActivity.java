package com.njut.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.nutlab.kczl.kczlApplication;
import org.nutlab.webService.evaluationService;
import org.nutlab.webService.loginService;
import org.nutlab.webService.summaryService;

import com.njut.R;
import com.njut.activity.LoginActivity.LoginThread;
import com.njut.data.Evaluation;
import com.umeng.analytics.MobclickAgent;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar.OnRatingBarChangeListener;

/*评教系统*/
public class TeachingEvaluationActivity extends Activity {
	private Button backButton;
	private Button okButton;
	private TextView learningEffectText;
	private RatingBar learningEffectRating;
	private String learningEffect[];
	private String courseName;
	private String courseNature;
	private String endtime;
	private String credit;
	private String ctid;
	private TextView courseNameTextView;
	private TextView courseNatureTextView;
	private TextView crediTextView;
	private TextView teachingEvaluationTextView;
	private TextView summaryTextView;
	private LinearLayout summaryLinearLayout;
	private LinearLayout summaryLine;
	private LinearLayout teachingEvaluationLine;
	private ScrollView teachingEvaluationLinearLayout;
	private String summaryString;
	private int index=1;
	// private Evaluation eva;
	// 学习效果
	private String effect;
	// 教学纪律
	private String discipline;
	// 到课情况
	private String attendance;
	// 教学进度
	private String speed;
	// 其他建议
	private String advice;
	private ProgressDialog progressDialog;
	private String TAG = "TeachingEvaluationActivity";
	protected final int EVALUATE_FINISHED = 1;
	protected final String RETURN_STRING = "returnString";
	RadioGroup attendacerRadioGroup;
	RadioButton attendancerRadioButton;
	RadioGroup speedRadioGroup;
	RadioButton speedRadioButton;
	RadioGroup disciplineRadioGroup;
	RadioButton disciplineRadioButton;
	EditText adviceEditText;
	
	private EditText summaryEditText;

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (progressDialog != null)
				progressDialog.dismiss();
			switch (msg.what) {
			case EVALUATE_FINISHED: {
				Bundle bundle = msg.getData();
				try {
					finishEvaluateOperation(bundle.getString(RETURN_STRING));
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
				break;
			}
			super.handleMessage(msg);
		}
	};

	class EvaluateThread extends Thread {
		public void run() {
			Message message = new Message();
			message.what = EVALUATE_FINISHED;
			evaluationService es = new evaluationService();
			String msg = es.upload(ctid, effect, discipline, attendance, speed,
					advice, endtime);
			//summaryService ss=new summaryService();
			//ss.upload(ctid, summaryString);
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
	
	class SummaryThread extends Thread {
		public void run() {
			Message message = new Message();
			message.what = EVALUATE_FINISHED;
			summaryService ss=new summaryService();
			String msg=ss.upload(ctid, summaryString);
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

	protected void finishEvaluateOperation(String mStringReturnStr) {
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(mStringReturnStr);
			new AlertDialog.Builder(this).setTitle("提交结果").setMessage(
					jsonObj.getString("msg")).setPositiveButton("是",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							TeachingEvaluationActivity.this.finish();
						}
					}).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		courseName = bundle.getString("coursename");
		courseNature = bundle.getString("courseNature");
		credit = bundle.getString("credit");
		ctid = bundle.getString("ctid");
		endtime = bundle.getString("endtime");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teaching_evaluation);
		learningEffect = getResources().getStringArray(
				R.array.learning_effect_array);
		learningEffectText = (TextView) findViewById(R.id.learning_effect_textView);
		learningEffectRating = (RatingBar) findViewById(R.id.ratingbar1);
		disciplineRadioGroup = (RadioGroup) findViewById(R.id.tearching_arrangement_radioGroup);
		attendacerRadioGroup = (RadioGroup) findViewById(R.id.attendance_radioGroup);
		speedRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		backButton = (Button) findViewById(R.id.abort_button);
		adviceEditText = (EditText) findViewById(R.id.evaluation_EditText);
		okButton = (Button) findViewById(R.id.ok_button);
		courseNameTextView = (TextView) findViewById(R.id.course_name_textView);
		courseNameTextView.setText(courseName);
		courseNatureTextView = (TextView) findViewById(R.id.type_textView);
		courseNatureTextView.setText(courseNature);
		crediTextView = (TextView) findViewById(R.id.credit_textView);
		crediTextView.setText(credit + "学分");
		teachingEvaluationTextView = (TextView) findViewById(R.id.teaching_evaluation_TextView);
		summaryTextView = (TextView) findViewById(R.id.summary_TextView);
		summaryLinearLayout = (LinearLayout) findViewById(R.id.summary_View);
		teachingEvaluationLinearLayout = (ScrollView) findViewById(R.id.teaching_evaluation_View);
		summaryLine= (LinearLayout) findViewById(R.id.summary_Line);
		teachingEvaluationLine= (LinearLayout) findViewById(R.id.teaching_evaluation_Line);
		summaryEditText=(EditText)findViewById(R.id.summary_EditText);
		backButton.setOnClickListener(new View.OnClickListener() {
     
			@Override
			public void onClick(View v) {
				TeachingEvaluationActivity.this.finish();
			}
		});

		okButton.setOnClickListener(new View.OnClickListener() {// 完成按钮点击事件
					@Override
					public void onClick(View v) {
						if (teachingEvaluationLinearLayout.getVisibility() == View.GONE) {
						summaryString=summaryEditText.getText().toString().trim();
						SummaryThread ST = new SummaryThread();
						progressDialog = ProgressDialog.show(
								TeachingEvaluationActivity.this,
								getString(R.string.state),
								getString(R.string.uploading), true);
						ST.start();
						}else{
						// eva.setCtid(ctid);
						int attendanceId = attendacerRadioGroup
								.getCheckedRadioButtonId();
						attendancerRadioButton = (RadioButton) findViewById(attendanceId);
						// eva.setAttendance(attendancerRadioButton.getText().toString());
						attendance = attendancerRadioButton.getText()
								.toString();
						if (attendance.equals("<50%"))
							attendance = "1";
						else {
							if (attendance.equals("≈70%"))
								attendance = "3";
							else if (attendance.equals(">90%"))
								attendance = "5";
						}

						int speedId = speedRadioGroup.getCheckedRadioButtonId();
						speedRadioButton = (RadioButton) findViewById(speedId);
						// eva.setSpeed(speedRadioButton.getText().toString());
						speed = speedRadioButton.getText().toString();
						if (speed.equals("较慢"))
							speed = "1";
						else {
							if (speed.equals("适中"))
								speed = "3";
							else if (speed.equals("较快"))
								speed = "5";
						}

						int disciplineId = disciplineRadioGroup
								.getCheckedRadioButtonId();
						disciplineRadioButton = (RadioButton) findViewById(disciplineId);
						// eva.setDiscipline(disciplineRadioButton.getText().toString());
						discipline = disciplineRadioButton.getText().toString();
						if (discipline.equals("正常"))
							discipline = "1";
						else {
							if (discipline.equals("调课"))
								discipline = "3";
							else if (discipline.equals("停课"))
								discipline = "5";
						}
						// eva.setEffect(String.valueOf(learningEffectRating.getRating()));
						effect = String.valueOf((int) learningEffectRating
								.getRating());

						// eva.setAdvice(adviceEditText.getText().toString());
						advice = adviceEditText.getText().toString();
						EvaluateThread ET = new EvaluateThread();
						progressDialog = ProgressDialog.show(
								TeachingEvaluationActivity.this,
								getString(R.string.state),
								getString(R.string.uploading), true);
						ET.start();
						// TeachingEvaluationActivity.this.finish();

					}}
				});

		learningEffectRating
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// doing actions
						int rate = (int) rating;
						learningEffectText.setText(learningEffect[rate - 1]);
					}
				});
		disciplineRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (checkedId != R.id.teaching_arrangement1) {
							for (int i = 0; i < 3; i++) {
								attendacerRadioGroup.getChildAt(i).setEnabled(
										false);
								attendacerRadioGroup.getChildAt(i).setBackgroundResource(R.drawable.evaluation_option_button_disabled);
								speedRadioGroup.getChildAt(i).setEnabled(false);
								speedRadioGroup.getChildAt(i).setBackgroundResource(R.drawable.evaluation_option_button_disabled);
							}
							learningEffectRating.setEnabled(false);
							adviceEditText.setEnabled(false);
							summaryEditText.setEnabled(false);
						} else {
							for (int i = 0; i < 3; i++) {
								attendacerRadioGroup.getChildAt(i).setEnabled(
										true);
								speedRadioGroup.getChildAt(i).setEnabled(true);
								attendacerRadioGroup.getChildAt(i).setBackgroundResource(R.drawable.evaluation_option_button);
								speedRadioGroup.getChildAt(i).setBackgroundResource(R.drawable.evaluation_option_button);
							}
							learningEffectRating.setEnabled(true);
							adviceEditText.setEnabled(true);
							summaryEditText.setEnabled(true);
						}
					}
				});

		teachingEvaluationTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (teachingEvaluationLinearLayout.getVisibility() == View.GONE) {
					summaryTextView.setTextColor(Color.GRAY);
					teachingEvaluationTextView.setTextColor(Color.WHITE);
					summaryLinearLayout.setVisibility(View.GONE);
					teachingEvaluationLinearLayout.setVisibility(View.VISIBLE);
					summaryLine.setVisibility(View.GONE);
					teachingEvaluationLine.setVisibility(View.VISIBLE);
					index=1;
				}
			}
		});

		summaryTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (summaryLinearLayout.getVisibility() == View.GONE) {
					summaryTextView.setTextColor(Color.WHITE);
					teachingEvaluationTextView.setTextColor(Color.GRAY);
					teachingEvaluationLinearLayout.setVisibility(View.GONE);
					summaryLinearLayout.setVisibility(View.VISIBLE);
					summaryLine.setVisibility(View.VISIBLE);
					teachingEvaluationLine.setVisibility(View.GONE);
					index=2;
				}

			}
		});
	}

}
