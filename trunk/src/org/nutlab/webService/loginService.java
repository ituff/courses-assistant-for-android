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

import com.njut.data.CookieElement;

import android.util.Log;

public class loginService {

	private String TAG = "LOGIN_SERVICE";

	public String login(String userNameStr, String passWordStr) {
		HttpClient httpclient = new DefaultHttpClient();
		// ���URL
		HttpPost httppost = new HttpPost("http://" + kczlApplication.ServerUri
				+ "/timetable/login.action");
		String strResult = "";
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			// Your DATA
			nameValuePairs.add(new BasicNameValuePair("email", userNameStr));
			nameValuePairs.add(new BasicNameValuePair("password", passWordStr));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf8"));
			HttpResponse response = httpclient.execute(httppost);
			/* ��״̬��Ϊ200��Post�ɹ� */
			if (response.getStatusLine().getStatusCode() == 200) {
				/* ���������� */
				strResult = EntityUtils.toString(response.getEntity());
				List<Cookie> cookies =  ((AbstractHttpClient) httpclient).getCookieStore().getCookies(); 
				if (cookies.isEmpty()) {
					Log.i(TAG, "-------Cookie NONE---------");
				} else {
					CookieElement cookie = new CookieElement();
					for (int i = 0; i < cookies.size(); i++) {
						// ����cookie
						if (cookies.get(i).getName().toUpperCase()
								.equals("JSESSIONID")) {
							cookie.setJSESSIONID(cookies.get(i).getValue());
						}
						if (cookies.get(i).getName().toUpperCase()
								.equals("EMAIL")) {
							cookie.setEmail(cookies.get(i).getValue());
						}
						if (cookies.get(i).getName().toUpperCase()
								.equals("SESSIONCODE")) {
							cookie.setSessioncode(cookies.get(i).getValue());
						}
						Log.d(TAG, cookies.get(i).getName() + "="
								+ cookies.get(i).getValue());
					}
					kczlApplication.sCookie=cookie;
					kczlApplication.Cookies=((AbstractHttpClient)httpclient).getCookieStore();//��cookie
				}
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
