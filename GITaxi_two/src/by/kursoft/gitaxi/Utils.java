package by.kursoft.gitaxi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utils {
	public static String getJSONString(Context context, String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response;
		InputStream inStream = null;
		String result = "";
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				inStream = entity.getContent();
				result = convertStreamToString(inStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT)
					.show();
		} finally {
			closeStream(inStream);
		}

		return result;
	}

	public static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			line = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeStream(is);
		}
		return line;
		
	}

	private static void closeStream(InputStream stream) {
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
