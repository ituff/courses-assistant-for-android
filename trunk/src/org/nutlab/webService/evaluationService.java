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

public class evaluationService {
	private String TAG = "EVALUTION_SERVICE";

	public String upload(String ctid,String effect,String discipline,String attendance,String speed,String advice,String endtime) {
		if(kczlApplication.IsOffLine==1){
			loginService ls = new loginService();
			String msg = ls.login(kczlApplication.UserName,
					kczlApplication.PassWord);
			if(!msg.contains("birthday")){
				kczlApplication.IsLogined=0;
				return "Error";
			}			
		}
		HttpClient httpclient = new DefaultHttpClient();
		// 你的URL
		HttpPost httppost = new HttpPost("http://" + kczlApplication.ServerUri
				+ "/timetable/evaluation.action");
		String strResult = "";
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			// Your DATA
			nameValuePairs.add(new BasicNameValuePair("ctid", ctid));
			nameValuePairs.add(new BasicNameValuePair("effect", effect));
			nameValuePairs.add(new BasicNameValuePair("attendance", attendance));
			nameValuePairs.add(new BasicNameValuePair("speed", speed));
			nameValuePairs.add(new BasicNameValuePair("advice", advice));
			nameValuePairs.add(new BasicNameValuePair("discipline", discipline));
			nameValuePairs.add(new BasicNameValuePair("schoolnumber", kczlApplication.Person.getSchoolnumber()));
			nameValuePairs.add(new BasicNameValuePair("endtime", endtime));
			((AbstractHttpClient) httpclient).setCookieStore(kczlApplication.Cookies);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf8"));
			HttpResponse response = httpclient.execute(httppost);
			/* 若状态码为200，Post成功 */
			if (response.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				strResult = EntityUtils.toString(response.getEntity());
				List<Cookie> cookies =  ((AbstractHttpClient) httpclient).getCookieStore().getCookies(); 
				if (cookies.isEmpty()) {
					Log.i(TAG, "-------Cookie NONE---------");
				} else {
					CookieElement cookie = new CookieElement();
					/*for (int i = 0; i < cookies.size(); i++) {
						// 保存cookie
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
					kczlApplication.Cookies=((AbstractHttpClient)httpclient).getCookieStore();//读cookie */
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
