package org.nutlab.webService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.nutlab.kczl.kczlApplication;

import android.util.Log;

import com.njut.data.CookieElement;

public class curriculumService {
	
	private String TAG = "CURRICULUM_SERVICE";

	public String get() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 你的URL
		HttpPost httppost = new HttpPost("http://" + kczlApplication.ServerUri
				+ "/timetable/curriculum.action");
		String strResult = "";
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			// Your DATA
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf8"));
			httpclient.setCookieStore(kczlApplication.Cookies);
			HttpResponse response = httpclient.execute(httppost);
			/* 若状态码为200，Post成功 */
			if (response.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				strResult = EntityUtils.toString(response.getEntity());				
			} else {
				strResult = "Error Response:"
						+ response.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		strResult = strResult.trim();
		return strResult;
	}

}
