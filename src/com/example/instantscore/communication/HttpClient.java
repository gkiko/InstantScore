package com.example.instantscore.communication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpClient {
	private static DefaultHttpClient httpclient = null;
	private static final int TIMEOUT = 30 * 1000;
	private static final String USER_AGENT = "Mozilla/5.0";

	private static String composeUrl(final String url,final Map<String, String> params) {
		StringBuilder strBld = new StringBuilder(url);
		strBld.append("?");
		if (params != null) {
			for (String key : params.keySet()) {
				strBld.append(key);
				strBld.append("=");
				strBld.append(params.get(key));
				strBld.append("&");
			}
		}
		strBld.deleteCharAt(strBld.length() - 1);
		return strBld.toString();

		// Logger.d(URLEncoder.encode(strBld.toString()));
		// return URLEncoder.encode(strBld.toString());
	}

	public static byte[] getDoGetResponseByteArray(final String url,
			final Map<String, String> params) throws IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(composeUrl(url, params));

		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		byte[] result = streamToByteArray(entity.getContent());

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		// httpclient.getConnectionManager().shutdown();

		return result;
	}

	public static Bitmap getDoGetResponseBitmap(final String imgUrl)
			throws IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(imgUrl);

		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		Bitmap result = streamToBitmap(is);
		is.close();

		return result;
	}

	private static Bitmap streamToBitmap(InputStream content)
			throws IOException {
		BufferedInputStream bis = new BufferedInputStream(content);
		Bitmap IMG = BitmapFactory.decodeStream(bis);
		bis.close();
		return IMG;
	}

	public static String getHttpClientDoGetResponse(final String url,final Map<String, String> params) throws ClientProtocolException,IOException {
		if (httpclient == null)
			httpclient = newMultiThreadedHttpClient();
		HttpGet httpget = new HttpGet(composeUrl(url, params));

		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();

		String result = streamToString(entity.getContent());
//        System.out.println("res "+result);

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		// httpclient.getConnectionManager().shutdown();

		return result;
	}

	public static String streamToString(InputStream is) throws IOException {
		return new String(streamToByteArray(is), "UTF-8");
	}

	public static byte[] streamToByteArray(InputStream is) throws IOException {
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream buff = new ByteArrayOutputStream();

		int numRead = 0;
		while ((numRead = is.read(bytes)) >= 0) {
			buff.write(bytes, 0, numRead);
		}

		return buff.toByteArray();
	}
	
	public static String doPost(String url, String urlParameters) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	private static DefaultHttpClient newMultiThreadedHttpClient() {
		// Create and initialize HTTP parameters
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT);
		ConnManagerParams.setMaxTotalConnections(params, 20);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		// Create an HttpClient with the ThreadSafeClientConnManager.
		// This connection manager must be used if more than one thread will
		// be using the HttpClient.
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);

		return new DefaultHttpClient(cm, params);
	}

}
