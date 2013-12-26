package com.luckysun.electorch.menu;

/*
 * Copyright 2013 Csaba Szugyiczki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdView;
import cn.domob.android.ads.DomobAdManager.ErrorCode;

import com.luckysun.electorch.ElecTorchActivity;
import com.luckysun.electorch.R;
import com.luckysun.electorch.menu.CircleImageView;
import com.luckysun.electorch.menu.CircleLayout;
import com.luckysun.electorch.menu.CircleLayout.OnItemClickListener;
import com.luckysun.electorch.menu.CircleLayout.OnItemSelectedListener;

public class MenuActivity extends Activity implements OnItemSelectedListener,
		OnItemClickListener {
	TextView selectedTextView;
	public static final String PUBLISHER_ID = "56OJzfGouNVdQNNTvf";
	private Button mMenu;
	RelativeLayout mAdContainer;
	DomobAdView mAdview320x50;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 全屏设置，隐藏窗口所有装饰
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置屏幕显示无标题，必须启动就要设置好，否则不能再次被设置
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.layout_menu);

		CircleLayout circleMenu = (CircleLayout) findViewById(R.id.main_circle_layout);
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);

		
		selectedTextView = (TextView) findViewById(R.id.main_selected_textView);
		selectedTextView.setText(((CircleImageView) circleMenu
				.getSelectedItem()).getName());
		
		mMenu = (Button) findViewById(R.id.btn_menu);
		mMenu.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
		// 创建一个320x50的广告View
		mAdview320x50 = new DomobAdView(this, ElecTorchActivity.PUBLISHER_ID,
				DomobAdView.INLINE_SIZE_320X50);
//		mAdview320x50.setKeyword("game");
//		mAdview320x50.setUserGender("male");
//		mAdview320x50.setUserBirthdayStr("2000-08-08");
//		mAdview320x50.setUserPostcode("123456");

		// 设置广告view的监听器。
		mAdview320x50.setAdEventListener(new DomobAdEventListener() {

			@Override
			// 成功接收到广告返回回调
			public void onDomobAdReturned(DomobAdView adView) {
				// TODO Auto-generated method stub
				Log.i("DomobSDKDemo", "onDomobAdReturned");
			}

			@Override
			// Landing Page成功打开回调
			public void onDomobAdOverlayPresented(DomobAdView adView) {
				// TODO Auto-generated method stub
				Log.i("DomobSDKDemo", "overlayPresented");
			}

			@Override
			// Landing Page关闭回调
			public void onDomobAdOverlayDismissed(DomobAdView adView) {
				// TODO Auto-generated method stub
				Log.i("DomobSDKDemo", "Overrided be dismissed");
			}

			@Override
			// 广告点击回调
			public void onDomobAdClicked(DomobAdView arg0) {
				// TODO Auto-generated method stub
				Log.i("DomobSDKDemo", "onDomobAdClicked");
			}

			@Override
			// 广告请求失败回调
			public void onDomobAdFailed(DomobAdView arg0, ErrorCode arg1) {
				// TODO Auto-generated method stub
				Log.i("DomobSDKDemo", "onDomobAdFailed");
			}

			@Override
			// 离开应用回调
			public void onDomobLeaveApplication(DomobAdView arg0) {
				// TODO Auto-generated method stub
				Log.i("DomobSDKDemo", "onDomobLeaveApplication");
			}

			@Override
			// 返回当前的Context
			public Context onDomobAdRequiresCurrentContext() {
				// TODO Auto-generated method stub
				return MenuActivity.this;
			}
		});
		// 将广告View增加到视图中。
		mAdContainer.addView(mAdview320x50);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onItemSelected(View view, int position, long id, String name) {
		selectedTextView.setText(name);
	}

	@Override
	public void onItemClick(View view, int position, long id, String name) {
		
		Log.e("other", "position = " + position);
		Log.e("other", "name = " + name);
		switch (position) {
		case 0:
			setStyle(ElecTorchActivity.MIUI);
			break;
		case 1:
			setStyle(ElecTorchActivity.IPHONE);
			break;
		case 2:
			setStyle(ElecTorchActivity.LIGHT);
			break;
		case 3:
			setStyle(ElecTorchActivity.MIUIV5);
			break;
		}
		
		Intent intent = new Intent(this,ElecTorchActivity.class);
		this.setResult(position,intent);
		this.finish();
		Toast.makeText(getApplicationContext(),getResources().getString(R.string.change_theme) + " ：" + name,
				Toast.LENGTH_SHORT).show();
	}

	public void setStyle(int style) {
			SharedPreferences settings = getSharedPreferences("sunflashlight",Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("style", style);
			editor.commit();
	}

}
