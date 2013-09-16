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
 * ����gridview�е�ÿһ��item��ʾ��textview
 * 
 * @author jack_peng
 * 
 */
public class CourseEveryMonthView extends BaseAdapter {

	private boolean isLeapyear = false; // �Ƿ�Ϊ����
	private int daysOfMonth = 0; // ĳ�µ�����
	private int dayOfWeek = 0; // ����ĳһ�������ڼ�
	private int lastDaysOfMonth = 0; // ��һ���µ�������
	private Context context;
	private String[] dayNumber = new String[42]; // һ��gridview�е����ڴ����������
	private SpecialCalendar sc = null;
	private LunarCalendar lc = null;
	private Resources res = null;
	private Drawable drawable = null;

	private String currentYear = "";
	private String currentMonth = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

	private int showYear; // ������ͷ����ʾ�����
	private int showMonth; // ������ͷ����ʾ���·�
	private String animalsYear = "";
	private String leapMonth = ""; // ����һ����
	private String cyclical = ""; // ��ɵ�֧
	// ϵͳ��ǰʱ��
	private String sysDate = "";
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";

	private Date selectedDate;
	private int mCount;

	public CourseEveryMonthView() {
		Date date = new Date();
		sysDate = sdf.format(date); // ��������
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
			// ����һ���»���
			if (stepMonth % 12 == 0) {
				stepYear = stepYear + stepMonth / 12 - 1;
				stepMonth = 12;
			} else {
				stepYear = stepYear + stepMonth / 12;
				stepMonth = stepMonth % 12;
			}
		} else {
			// ����һ���»���
			stepYear = stepYear - 1 + stepMonth / 12;
			stepMonth = stepMonth % 12 + 12;
			if (stepMonth % 12 == 0) {

			}
		}

		currentYear = String.valueOf(stepYear);// �õ���ǰ�����
		currentMonth = String.valueOf(stepMonth); // �õ�����
													// ��jumpMonthΪ�����Ĵ�����ÿ����һ�ξ�����һ�»��һ�£�

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
			// ��ǰ����Ϣ��ʾ
			textView.setTextColor(Color.GRAY);// �����������
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(selectedDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;

		if (year == showYear && month == showMonth
				&& SpecialCalendar.getPositionInMonth(selectedDate) == position) {
			// ����ѡ�еı���
			drawable = res.getDrawable(R.drawable.calendar_item_bg);
			textView.setBackgroundDrawable(drawable);
			textView.setTextColor(Color.BLACK);
		}
		return convertView;
	}

	// �õ�ĳ���ĳ�µ����������µĵ�һ�������ڼ�
	public void getCalendar(int year, int month) {
		isLeapyear = sc.isLeapYear(year); // �Ƿ�Ϊ����
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // ĳ�µ�������
		dayOfWeek = sc.getWeekdayOfMonth(year, month); // ĳ�µ�һ��Ϊ���ڼ�
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // ��һ���µ�������
		getweek(year, month);
	}

	// ��һ�����е�ÿһ���ֵ���������dayNuber��
	private void getweek(int year, int month) {
		int j = 1;
		for (int i = 0; i < dayNumber.length; i++) {

			if (i < dayOfWeek) { // ǰһ����
				int temp = lastDaysOfMonth - dayOfWeek + 1;
				dayNumber[i] = Integer.toString((temp + i));
			} else if (i < daysOfMonth + dayOfWeek) { // ����
				String day = String.valueOf(i - dayOfWeek + 1); // �õ�������
				dayNumber[i] = day;
				setShowYear(year);
				setShowMonth(month);
				setAnimalsYear(lc.animalsYear(year));
			} else { // ��һ����
				dayNumber[i] =Integer.toString(j) ;
				j++;
			}
		}
	}


	/**
	 * ���ÿһ��itemʱ����item�е�����
	 * 
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position) {
		return dayNumber[position];
	}

	/**
	 * �ڵ��gridViewʱ���õ�������е�һ���λ��
	 * 
	 * @return
	 */
	public int getStartPositon() {
		return dayOfWeek;
	}

	/**
	 * �ڵ��gridViewʱ���õ�����������һ���λ��
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
