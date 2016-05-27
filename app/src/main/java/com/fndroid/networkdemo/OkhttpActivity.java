package com.fndroid.networkdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fndroid.networkdemo.gson.student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpActivity extends AppCompatActivity {
	private static final String TAG = "OkhttpActivity";
	private static final int FINISH = 1;
	private TextView tv_response;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == FINISH) {
				tv_response.setText(msg.obj.toString());
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_okhttp);
		tv_response = (TextView) findViewById(R.id.ok_response);
	}

	public void ok_send(View view) {
		OkHttpClient client = new OkHttpClient();
//		final Request request = new Request.Builder().url("http://www.hubwiz.com/").build();
//		final Request request = new Request.Builder().url("http://192.168.1.222:8080/test/data" +
//				".xml").build();
		final Request request = new Request.Builder().url("http://192.168.1.222:8080/test/data" +
				".json").build();
		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String str = response.body().string();
//				pullXML(str);
				gsonJson(str);
				Message msg = new Message();
				msg.what = FINISH;
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		});
	}

	public void pullXML(String response) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(new StringReader(response));
			int eventType = parser.getEventType();
			String id = "";
			String name = "";
			String age = "";
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String node = parser.getName();
				switch (eventType) {
					case XmlPullParser.START_TAG: {
						if ("id".equals(node)) {
							id = parser.nextText();
						} else if ("name".equals(node)) {
							name = parser.nextText();
						} else if ("age".equals(node)) {
							age = parser.nextText();
						}
						break;
					}
					case XmlPullParser.END_TAG: {
						if ("student".equals(node)) {
							Log.d(TAG, "id=" + id + " name=" + name + " age=" + age);
						}
						break;
					}
					default:
						break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gsonJson(String json) {
		Gson gson = new Gson();
		Type type = new TypeToken<List<student>>() {}.getType();
		List<student> students = gson.fromJson(json, type);
		for (student s : students) {
			Log.d(TAG, "解析得到的数据为: id=" + s.getId() + " name=" + s.getName() + " age=" + s.getAge
					());
		}
	}
}
