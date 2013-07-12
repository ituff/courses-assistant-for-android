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
import com.njut.data.CourseElement;
import com.njut.utility.AppUtils;

public class CourseAdapter extends BaseAdapter {
	private List<CourseElement> list;
	private LayoutInflater inflater;
	private Context context;
private static final int imageId[]={R.drawable.blue_circle,R.drawable.yellow_circle,R.drawable.red_circle,R.drawable.green_circle};
	public CourseAdapter(Context context, List<CourseElement> list) {
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
					.inflate(R.layout.course_list_item, null);
		}
		TextView courseName = (TextView) convertView
				.findViewById(R.id.course_name_TextView);

		TextView teacherName = (TextView) convertView
				.findViewById(R.id.teacher_name_Textview);

		TextView classroomName = (TextView) convertView.findViewById(R.id.classroom_name_Textview);

		TextView time = (TextView) convertView
				.findViewById(R.id.course_time_Textview);
		ImageView state = (ImageView) convertView
		.findViewById(R.id.state_Imageview);
		courseName.setText(list.get(position).getCourseName());

		teacherName.setText(list.get(position).getTeacherName());

		classroomName.setText(list.get(position).getClassroomName());

		time.setText(list.get(position).getTime());
		state.setBackgroundResource(imageId[list.get(position).getState()]);
	


		return convertView;

	}

}
