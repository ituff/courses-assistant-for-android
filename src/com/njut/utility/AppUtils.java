package com.njut.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class AppUtils {

	public static String getRGBByScore(int score) {
		int red, green;
		if (score <= 60) {
			red = 255;
			green = 0;
		} else {
			red = 255 - (int) (((score - 60) / 40.0) * 255);
			green = 255 - (int) (((100 - score) / 40.0) * 255);
		}
		String redStr = Integer.toHexString(red).length() < 2 ? "0"
				+ Integer.toHexString(red) : Integer.toHexString(red);
		String greenStr = Integer.toHexString(green).length() < 2 ? "0"
				+ Integer.toHexString(green) : Integer.toHexString(green);
		return "#6C" + redStr + greenStr + "00";
	}

	public static String getRGBByScore1(int score) {
		int red, green;
		if (score <= 60) {
			red = 255;
			green = 0;
		} else {
			red = 255 - (int) (((score - 60) / 40.0) * 255);
			green = 255 - (int) (((100 - score) / 40.0) * 255);
		}
		String redStr = Integer.toHexString(red).length() < 2 ? "0"
				+ Integer.toHexString(red) : Integer.toHexString(red);
		String greenStr = Integer.toHexString(green).length() < 2 ? "0"
				+ Integer.toHexString(green) : Integer.toHexString(green);
		return "#FF" + redStr + greenStr + "00";
	}

	public static boolean areDatesSame(Date date1, Date date2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

		return sdf.format(date1).equals(sdf.format(date2));
	}

	public static SlidingMenu menu;

}
