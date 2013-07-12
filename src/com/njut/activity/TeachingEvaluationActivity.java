package com.njut.activity;


import com.njut.R;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;


/*评教系统*/
public class TeachingEvaluationActivity extends Activity {
	private Button backButton;
	private Button okButton;
	private TextView learningEffectText;
	private RatingBar learningEffectRating;
	private String learningEffect[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teaching_evaluation);
		learningEffect = getResources()
				.getStringArray(R.array.learning_effect_array);
		learningEffectText = (TextView) findViewById(R.id.learning_effect_textView);
		learningEffectRating = (RatingBar) findViewById(R.id.ratingbar1);
		backButton = (Button) findViewById(R.id.abort_button);
		okButton = (Button) findViewById(R.id.ok_button);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TeachingEvaluationActivity.this.finish();
			}
		});
		
		okButton.setOnClickListener(new View.OnClickListener() {//完成按钮点击事件

			@Override
			public void onClick(View v) {
				TeachingEvaluationActivity.this.finish();
			}
		});
		
		
		learningEffectRating
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// doing actions
						int rate=(int)rating;
						learningEffectText.setText(learningEffect[rate]);
					}
				});

	}

}
