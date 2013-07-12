package com.njut.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.njut.R;
import com.njut.data.AchievementElement;
import com.njut.utility.AppUtils;

public class AchievementAdapter extends BaseAdapter {
	private List<AchievementElement> list;
	private LayoutInflater inflater;
	private Context context;

	public AchievementAdapter(Context context, List<AchievementElement> list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.achievement_list_item, null);
		}
		LinearLayout linear = (LinearLayout) convertView
				.findViewById(R.id.linear);
		TextView courseName = (TextView) convertView
				.findViewById(R.id.course_name_Textview);

		TextView credit = (TextView) convertView
				.findViewById(R.id.credit_Textview);

		TextView type = (TextView) convertView.findViewById(R.id.type_Textview);

		TextView score = (TextView) convertView
				.findViewById(R.id.score_Textview);

		courseName.setText(list.get(position).getCourseName());

		credit.setText(list.get(position).getCredit() + "学分");

		type.setText(list.get(position).getType());

		score.setText(Integer.toString(list.get(position).getScore()));
		String colorStr = AppUtils.getRGBByScore(list.get(position).getScore());
		String colorStr1 = AppUtils.getRGBByScore1(list.get(position).getScore());
		 GradientDrawable grad = new GradientDrawable(//渐变色  
				            Orientation.TOP_BOTTOM,  
				           new int[]{Color.parseColor(colorStr), Color.parseColor(colorStr1)}  
				       );  

		linear.setBackgroundDrawable(grad);

		return convertView;

	}

}
