package com.fndroid.networkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void openWebView(View view){
		startActivity(new Intent(this, WebviewActivity.class));
	}

	public void httpURLConnection(View view){
		startActivity(new Intent(this, HttpURLonnectionActivity.class));
	}

	public void okHttp(View view){
		startActivity(new Intent(this, OkhttpActivity.class));
	}

}
