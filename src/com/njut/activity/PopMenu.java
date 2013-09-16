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

/*ѧ�ں�ѧ�굯����ʵ��*/
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
		final String years[] = new String[] { "����ѧ��","2009-2010","2010-2011","2011-2012", "2012-2013",// ѧ�����ݰ�
				"2013-2014", "2014-2015" };

		year.setAdapter(new ArrayWheelAdapter<String>(years));

		final String terms[] = new String[] { "����ѧ��","��һѧ��", "�ڶ�ѧ��" };
		final WheelView term = (WheelView) (view.findViewById(R.id.term));// ѧ�����ݰ�
		WheelAdapter a = new ArrayWheelAdapter<String>(terms, 8);
		term.setAdapter(a);
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı�����������ģ�
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		((QueryAchievementActivity) PopMenu.this.context).setYear(years[0]);
		((QueryAchievementActivity) PopMenu.this.context).setTerm(terms[0]);
		year.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (PopMenu.this.context instanceof QueryAchievementActivity) {// �����¼�
					((QueryAchievementActivity) PopMenu.this.context)
							.setYear(years[newValue]);
				}
			}
		});
		term.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {// �����¼�
				if (PopMenu.this.context instanceof QueryAchievementActivity) {
					((QueryAchievementActivity) PopMenu.this.context)
							.setTerm(terms[newValue]);
				}
			}
		});
	}

	// ����ʽ ���� pop�˵� parent ���½�
	public void showAsDropDown(View parent) {
		float scale = context.getResources().getDisplayMetrics().density;
		int width = (int) (318 * scale + 0.5f); 
		int xoffInPixels = width / 2 - parent.getWidth() / 2;
		int yoffInPixels = (int) (4 * scale + 0.5f);
		popupWindow.showAsDropDown(parent, -xoffInPixels, -yoffInPixels);
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// ˢ��״̬
		popupWindow.update();
	}

	// ���ز˵�
	public void dismiss() {
		popupWindow.dismiss();
	}

	public View getView() {
		return this.view;
	}
}
