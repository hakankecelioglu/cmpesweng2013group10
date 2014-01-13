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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.groupon.mobile.exception.GrouponException;
import com.groupon.mobile.utils.Constants;
import com.groupon.mobile.utils.StringUtils;
/**
 * Connection utils for the class that make post and get get requests
 * @author sedrik
 *
 */
public class ConnectionUtils {
	/**
	 * Function that makes a post request with specified parameters and returns JSONObject of response
	 * @param url	Post address
	 * @param attributeMap	map that specifies query string
	 * @param authToken	Authorization token of user
	 * @return	server response to post as JSon
	 * @throws GrouponException
	 */
	public static JSONObject makePostRequest(String url, Map<String, String> attributeMap, String authToken) throws GrouponException {
		return makePostRequest(url, attributeMap, null, authToken);
	}
	/**
	 * Function that makes a post request with specified parameters and returns JSONObject of response
	 * @param url	Post address
	 * @param attributeMap	map that specifies query string
	 * @param postBody	JSON data of post
	 * @param authToken	Authorization token of user
	 * @return	server response to post as JSon
	 * @throws GrouponException
	 */
	public static JSONObject makePostRequest(String url, Map<String, String> attributeMap, JSONObject postBody, String authToken) throws GrouponException {
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
			HttpPost post = new HttpPost(url);
			post.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");

			if (authToken != null && StringUtils.isNotBlank(authToken)) {
				post.addHeader(Constants.REQUEST_AUTH_HEADER, authToken);
			}

			if (postBody != null) {
				StringEntity entity = new StringEntity(postBody.toString(), HTTP.UTF_8);
				entity.setContentType("application/json");
				post.setEntity(entity);
			}

			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				JSONObject errorResponse = new JSONObject(getResponseContent(response));
				if (errorResponse.has("error")) {
					throw new GrouponException(errorResponse.getString("error"));
				}
				throw new GrouponException("Error!!!");
			}

			String respContent = getResponseContent(response);
			return new JSONObject(respContent);
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
	/**
	 * Function that makes a get request with specified parameters and returns JSONObject of response
	 * @param url	GET address
	 * @param attributeMap	map that specifies query string
	 * @param authToken	Authorization token of user
	 * @return	server JSON response to GET 
	 * @throws GrouponException
	 */
	public static JSONObject makeGetRequest(String url, Map<String, String> attributeMap, String authToken) throws GrouponException {
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

			if (authToken != null && StringUtils.isNotBlank(authToken)) {
				get.addHeader(Constants.REQUEST_AUTH_HEADER, authToken);
			}

			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() != 200) {
				JSONObject errorResponse = new JSONObject(getResponseContent(response));
				if (errorResponse.has("error")) {
					throw new GrouponException(errorResponse.getString("error"));
				}
				throw new GrouponException("Error!!!");
			}

			String respContent = getResponseContent(response);
			return new JSONObject(respContent);
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

	/**
	 * Converts HttpResponse to String
	 * @param response	HttpResponse which will be converted to String
	 * @return	String of HttpResponse
	 * @throws GrouponException
	 */
	private static String getResponseContent(HttpResponse response) throws GrouponException {
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
				throw new GrouponException("Exception occured while getting response content!");
			}
		}
		return respContent;
	}

}
