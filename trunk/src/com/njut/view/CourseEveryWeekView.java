package com.njut.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.njut.R;
import com.njut.activity.CourseEveryMonthActivity;
import com.njut.activity.CourseEveryWeekActivity;
import com.njut.utility.AppUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 日历gridview中的每一个item显示的textview
 * 
 * @author jack_peng
 * 
 */
public class CourseEveryWeekView extends BaseAdapter {

	private Context context;
	private Resources res;
	private Date[] datesOfWeek = new Date[7];
	private Date selectedDate;

	public CourseEveryWeekView(Context context, Resources rs, int jumpWeek,
			Date firstDateOfWeek, Date selectedDate) {
		this.selectedDate = selectedDate;
		for (int i = 0; i < datesOfWeek.length; i++) {
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(firstDateOfWeek);
			gc.add(Calendar.DATE, jumpWeek * 7 + i);
			datesOfWeek[i] = gc.getTime();
		}
		this.context = context;
		this.res = rs;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return datesOfWeek.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.calendar_item, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.tvtext);
		SimpleDateFormat fymd = new SimpleDateFormat("d");
		String d = fymd.format(datesOfWeek[position]);

		SpannableString sp = new SpannableString(d);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
				d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f), 0, d.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		textView.setText(d);
		textView.setTextColor(Color.GRAY);
		Drawable db1 = res.getDrawable(R.drawable.light_separator_rotated);
		db1.setBounds(0, 0, 2, 38);
		textView.setCompoundDrawables(null, null, db1, null);
		if (AppUtils.areDatesSame(datesOfWeek[position], selectedDate)) {
			textView.setBackgroundResource(R.drawable.calendar_list_overlay);
			textView.setTextColor(Color.BLACK);

		}
		return convertView;
	}

	/**
	 * 点击每一个item时返回item中的日期
	 * 
	 * @param position
	 * @return
	 */
	public Date getDateByClickItem(int position) {
		return datesOfWeek[position];
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public boolean contain(Date date) {
		for (int i = 0; i < datesOfWeek.length; i++) {
			if (AppUtils.areDatesSame(date, datesOfWeek[i]))
				return true;
		}
		return false;
	}
}
