package com.lokiy.demo.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lokiy.demo.R;
import com.lokiy.widget.XImageView;

public class XImageViewActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ximageview);

		((XImageView) findViewById(R.id.ximageview)).setImageURL("http://img0.bdstatic.com/img/image/shouye/xiaoxiao/%E5%AE%A0%E7%89%A983.jpg");
	}
}
