package com.lokiy.demo.widget;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lokiy.demo.BaseActivity;
import com.lokiy.demo.R;
import com.lokiy.demo.Utils;
import com.lokiy.widget.GuideView;

public class GuideActivity extends BaseActivity {

	private Button btnTest;
	private Button btnTest2;
	private GuideView guideView;
	private GuideView guideView3;
	private GuideView guideView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		btnTest = (Button) findViewById(R.id.btn_test);
		btnTest2 = (Button) findViewById(R.id.btn_test2);
		btnTest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createGuide1().show();
			}
		});
	}

	private GuideView createGuide1() {
		if (guideView == null) {
			// 使用图片
			final ImageView iv = new ImageView(this);
			iv.setImageResource(R.drawable.img_new_task_guide);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.topMargin = Utils.dp2px(70);
			params.rightMargin = Utils.dp2px(70);
			iv.setLayoutParams(params);

			guideView = GuideView.Builder.newInstance(this).setTargetView(getWindow().getDecorView().findViewById(R.id.action_settings))//设置目标
					.setCustomGuideView(iv).setDirection(GuideView.Direction.LEFT_BOTTOM).setShape(GuideView.MyShape.CIRCULAR)   // 设置圆形显示区域，
					.setBgColor(getResources().getColor(R.color.shadow)).setOnclickListener(new GuideView.OnClickCallback() {
						@Override
						public void onClickedGuideView() {
							guideView.hide();
							createGuide2().show();
						}
					}).build();
		}
		return guideView;
	}

	private GuideView createGuide2() {
		if (guideView2 == null) {
			// 使用文字
			TextView tv = new TextView(GuideActivity.this);
			tv.setText("欢迎使用");
			tv.setTextColor(getResources().getColor(R.color.white));
			tv.setTextSize(30);
			tv.setGravity(Gravity.CENTER);
			guideView2 = GuideView.Builder.newInstance(GuideActivity.this).setTargetView(btnTest).setCustomGuideView(tv).setDirection(GuideView.Direction.LEFT_BOTTOM).setShape(GuideView.MyShape.ELLIPSE)   // 设置椭圆形显示区域，
					.setBgColor(getResources().getColor(R.color.shadow)).setOnclickListener(new GuideView.OnClickCallback() {
						@Override
						public void onClickedGuideView() {
							guideView2.hide();
							createGuide3().show();
						}
					}).build();
		}
		return guideView2;
	}

	private GuideView createGuide3() {
		if (guideView3 == null) {
			// 使用文字
			final TextView tv2 = new TextView(GuideActivity.this);
			tv2.setText("欢迎使用2");
			tv2.setTextColor(getResources().getColor(R.color.white));
			tv2.setTextSize(30);
			tv2.setGravity(Gravity.CENTER);
			guideView3 = GuideView.Builder.newInstance(GuideActivity.this).setTargetView(btnTest2).setCustomGuideView(tv2).setDirection(GuideView.Direction.LEFT_BOTTOM).setShape(GuideView.MyShape.RECTANGULAR)   // 设置矩形显示区域，
					.setRadius(80)          // 设置圆形或矩形透明区域半径，默认是targetView的显示矩形的半径，如果是矩形，这里是设置矩形圆角大小
					.setBgColor(getResources().getColor(R.color.shadow)).setOnclickListener(new GuideView.OnClickCallback() {
						@Override
						public void onClickedGuideView() {
							guideView3.hide();
							createGuide1().show();
						}
					}).build();
		}
		return guideView3;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main2, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (guideView != null && guideView.isShown()) {
			guideView.hide();
		} else if (guideView2 != null && guideView2.isShown()) {
			guideView2.hide();
		} else if (guideView3 != null && guideView3.isShown()) {
			guideView3.hide();
		} else {
			super.onBackPressed();
		}
	}
}
