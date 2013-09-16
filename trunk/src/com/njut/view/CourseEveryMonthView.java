package com.njut.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.njut.R;
import com.njut.activity.CourseEveryMonthActivity;
import com.njut.utility.LunarCalendar;
import com.njut.utility.SpecialCalendar;

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
public class CourseEveryMonthView extends BaseAdapter {

	private boolean isLeapyear = false; // 是否为闰年
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int lastDaysOfMonth = 0; // 上一个月的总天数
	private Context context;
	private String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中
	private SpecialCalendar sc = null;
	private LunarCalendar lc = null;
	private Resources res = null;
	private Drawable drawable = null;

	private String currentYear = "";
	private String currentMonth = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

	private int showYear; // 用于在头部显示的年份
	private int showMonth; // 用于在头部显示的月份
	private String animalsYear = "";
	private String leapMonth = ""; // 闰哪一个月
	private String cyclical = ""; // 天干地支
	// 系统当前时间
	private String sysDate = "";
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";

	private Date selectedDate;
	private int mCount;

	public CourseEveryMonthView() {
		Date date = new Date();
		sysDate = sdf.format(date); // 当期日期
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];

	}

	public CourseEveryMonthView(Context context, Resources rs, int jumpMonth,
			Date selectedDate) {
		this();
		this.selectedDate = selectedDate;
		this.context = context;
		sc = new SpecialCalendar();
		lc = new LunarCalendar();
		this.res = rs;
		int stepYear = Integer.parseInt(sys_year);
		int stepMonth = Integer.parseInt(sys_month) + jumpMonth;
		if (stepMonth > 0) {
			// 往下一个月滑动
			if (stepMonth % 12 == 0) {
				stepYear = stepYear + stepMonth / 12 - 1;
				stepMonth = 12;
			} else {
				stepYear = stepYear + stepMonth / 12;
				stepMonth = stepMonth % 12;
			}
		} else {
			// 往上一个月滑动
			stepYear = stepYear - 1 + stepMonth / 12;
			stepMonth = stepMonth % 12 + 12;
			if (stepMonth % 12 == 0) {

			}
		}

		currentYear = String.valueOf(stepYear);// 得到当前的年份
		currentMonth = String.valueOf(stepMonth); // 得到本月
													// （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）

		getCalendar(Integer.parseInt(currentYear), Integer
				.parseInt(currentMonth));

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
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
		if (position == 0)
		{
			mCount++;
		}
		else
		{
			mCount = 0;
		}
		
		if (mCount > 1)
		{
			return convertView;
		}
		Log.v(this.getClass().getName().toString(), Integer.toString(position));
		TextView textView = (TextView) convertView.findViewById(R.id.tvtext);
		String d = dayNumber[position];
		SpannableString sp = new SpannableString(d);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
				d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f), 0, d.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(sp);
		textView.setTextColor(Color.LTGRAY);

		if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
			// 当前月信息显示
			textView.setTextColor(Color.GRAY);// 当月字体设黑
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(selectedDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;

		if (year == showYear && month == showMonth
				&& SpecialCalendar.getPositionInMonth(selectedDate) == position) {
			// 设置选中的背景
			drawable = res.getDrawable(R.drawable.calendar_item_bg);
			textView.setBackgroundDrawable(drawable);
			textView.setTextColor(Color.BLACK);
		}
		return convertView;
	}

	// 得到某年的某月的天数且这月的第一天是星期几
	public void getCalendar(int year, int month) {
		isLeapyear = sc.isLeapYear(year); // 是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
		getweek(year, month);
	}

	// 将一个月中的每一天的值添加入数组dayNuber中
	private void getweek(int year, int month) {
		int j = 1;
		for (int i = 0; i < dayNumber.length; i++) {

			if (i < dayOfWeek) { // 前一个月
				int temp = lastDaysOfMonth - dayOfWeek + 1;
				dayNumber[i] = Integer.toString((temp + i));
			} else if (i < daysOfMonth + dayOfWeek) { // 本月
				String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期
				dayNumber[i] = day;
				setShowYear(year);
				setShowMonth(month);
				setAnimalsYear(lc.animalsYear(year));
			} else { // 下一个月
				dayNumber[i] =Integer.toString(j) ;
				j++;
			}
		}
	}


	/**
	 * 点击每一个item时返回item中的日期
	 * 
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position) {
		return dayNumber[position];
	}

	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 * 
	 * @return
	 */
	public int getStartPositon() {
		return dayOfWeek;
	}

	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 * 
	 * @return
	 */
	public int getEndPosition() {
		return (dayOfWeek + daysOfMonth) - 1;
	}

	public int getShowYear() {
		return showYear;
	}

	public void setShowYear(int showYear) {
		this.showYear = showYear;
	}

	public int getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(int showMonth) {
		this.showMonth = showMonth;
	}

	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}

	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}

	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}
}
