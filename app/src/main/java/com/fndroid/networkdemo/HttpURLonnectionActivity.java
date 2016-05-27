package com.fndroid.networkdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLonnectionActivity extends AppCompatActivity {
	private static final int FINISH = 1;
	private TextView response;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == FINISH){
				response.setText(msg.obj.toString());
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http_urlonnection);
		response = (TextView) findViewById(R.id.tv_response);
	}

	public void sendRequest(View view){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try{
//					URL url = new URL("http://www.hubwiz.com/");
					URL url = new URL("http://192.168.1.110:8080/test/data.xml");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					InputStream is = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					StringBuilder builder = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null){
						builder.append(line);
					}
					Message msg = new Message();
					msg.what = FINISH;
					msg.obj = builder.toString();
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (connection != null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
