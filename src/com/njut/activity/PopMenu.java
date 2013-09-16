package com.njut.activity;

import java.util.ArrayList;
import java.util.List;

import com.njut.R;
import com.njut.widget.ArrayWheelAdapter;
import com.njut.widget.OnWheelChangedListener;
import com.njut.widget.WheelAdapter;
import com.njut.widget.WheelView;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/*学期和学年弹出框实现*/
public class PopMenu {
	private Context context;
	private PopupWindow popupWindow;
	private View view;

	// private OnItemClickListener listener;

	public PopMenu(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;

		View view = LayoutInflater.from(context).inflate(R.layout.choose_term,
				null);
		this.view = view;
		WheelView year = (WheelView) (view.findViewById(R.id.year));
		final String years[] = new String[] { "所有学年","2009-2010","2010-2011","2011-2012", "2012-2013",// 学年数据绑定
				"2013-2014", "2014-2015" };

		year.setAdapter(new ArrayWheelAdapter<String>(years));

		final String terms[] = new String[] { "所有学期","第一学期", "第二学期" };
		final WheelView term = (WheelView) (view.findViewById(R.id.term));// 学期数据绑定
		WheelAdapter a = new ArrayWheelAdapter<String>(terms, 8);
		term.setAdapter(a);
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		((QueryAchievementActivity) PopMenu.this.context).setYear(years[0]);
		((QueryAchievementActivity) PopMenu.this.context).setTerm(terms[0]);
		year.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (PopMenu.this.context instanceof QueryAchievementActivity) {// 滑动事件
					((QueryAchievementActivity) PopMenu.this.context)
							.setYear(years[newValue]);
				}
			}
		});
		term.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {// 滑动事件
				if (PopMenu.this.context instanceof QueryAchievementActivity) {
					((QueryAchievementActivity) PopMenu.this.context)
							.setTerm(terms[newValue]);
				}
			}
		});
	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		float scale = context.getResources().getDisplayMetrics().density;
		int width = (int) (318 * scale + 0.5f); 
		int xoffInPixels = width / 2 - parent.getWidth() / 2;
		int yoffInPixels = (int) (4 * scale + 0.5f);
		popupWindow.showAsDropDown(parent, -xoffInPixels, -yoffInPixels);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	public View getView() {
		return this.view;
	}
}
