package com.groupon.mobile.conn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.groupon.mobile.exception.GrouponException;
import com.groupon.mobile.utils.StringUtils;

public class ConnectionUtils {
	public static String makePostRequest(String url, Map<String, String> attributeMap) throws GrouponException {
		List<NameValuePair> params = new LinkedList<NameValuePair>();

		params.add(new BasicNameValuePair("", ""));

		if (attributeMap != null) {
			Set<String> keys = attributeMap.keySet();
			for (String key : keys) {
				params.add(new BasicNameValuePair(key, attributeMap.get(key)));
			}
		}

		String paramString = URLEncodedUtils.format(params, "utf-8");
		if (!url.endsWith("?"))
			url += "?";
		url += paramString;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
			HttpResponse response = client.execute(post);
			if (response != null) {
				String respContent = getResponseContent(response);
				checkServerMessage(respContent);
				return respContent;
			} else {
				return "";
			}
		} catch (ClientProtocolException e) {
			throw new GrouponException("Client Protol Exception when connecting to Server.");
		} catch (IOException e) {
			throw new GrouponException("IO Exception occurred when posting request.");
		} catch (GrouponException e) {
			throw e;
		} catch (Exception e) {
			throw new GrouponException("Exception occurred while executing your request.", e);
		}
	}

	public static String makeGetRequest(String url, Map<String, String> attributeMap) throws GrouponException {
		List<NameValuePair> params = new LinkedList<NameValuePair>();

		if (attributeMap != null) {
			Set<String> keys = attributeMap.keySet();
			for (String key : keys) {
				params.add(new BasicNameValuePair(key, attributeMap.get(key)));
			}
		}

		String paramString = URLEncodedUtils.format(params, "utf-8");
		if (!url.endsWith("?"))
			url += "?";
		url += paramString;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			get.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
			HttpResponse response = client.execute(get);
			if (response != null) {
				String respContent = getResponseContent(response);
				checkServerMessage(respContent);
				return respContent;
			} else {
				return "";
			}
		} catch (ClientProtocolException e) {
			throw new GrouponException("Client Protol Exception when connecting to Netmera.");
		} catch (IOException e) {
			throw new GrouponException("IO Exception occurred when posting request.");
		} catch (GrouponException e) {
			throw e;
		} catch (Exception e) {
			throw new GrouponException("Exception occurred while executing your request.", e);
		}
	}

	private static String getResponseContent(HttpResponse response) {
		String respContent = "";
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			try {
				InputStream instream = entity.getContent();
				String line = "";
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "utf8"));
				while ((line = reader.readLine()) != null) {
					respContent += line;
				}
				instream.close();
			} catch (Exception e) {
			}
		}
		return respContent;
	}

	private static void checkServerMessage(String respContent) throws GrouponException {
		if (StringUtils.isNotBlank(respContent)) {
			try {
				JSONObject object = new JSONObject(respContent);
				if (object.has("code") && object.has("message")) {
					String code = object.getString("code");
					String message = object.getString("message");
					if (!code.equals("1000")) {
						throw new GrouponException(message);
					}
				}
			} catch (JSONException e) {
				throw new GrouponException("Not a valid response!");
			}
		}
	}
}
