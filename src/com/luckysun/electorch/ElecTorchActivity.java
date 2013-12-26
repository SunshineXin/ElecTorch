package com.luckysun.electorch;

import java.io.IOException;

import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdView;
import cn.domob.android.ads.DomobUpdater;
import cn.domob.android.ads.DomobAdManager.ErrorCode;

import com.luckysun.electorch.R;
import com.luckysun.electorch.menu.MenuActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ElecTorchActivity extends Activity {
	public static final String PUBLISHER_ID = "56OJzfGouNVdQNNTvf";
	
	public  static final int IPHONE = 1;
	public  static final int LIGHT = 2;
	public  static final int MIUI = 0;
	public  static final int MIUIV5 = 3;
	private int mCurrentStyle = MIUI;
	
	private Button mMenu;
	private boolean mIsOpen = false;
	private Button lightBtn = null;
	private Camera camera = null;
	private Parameters parameters = null;
	public static boolean kaiguan = true; // 定义开关状态，状态为false，打开状态，状态为true，关闭状态
	// public static boolean action = false;
	// //定义的状态，状态为false，当前界面不退出，状态为true，当前界面退出
	private int back = 0;// 判断按几次back

	private OnClickListener mImageViewListener = null;

	private Camera mCamera = null;
	RelativeLayout mAdContainer;
	DomobAdView mAdview320x50;

	private static final String[] KeyLightOptions = new String[] {
			"Close Camera LED", "Open CameraLED", };
	private static final int[] LightValue = new int[] { 0, 1, };
	
	private int[] mLightImage ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏设置，隐藏窗口所有装饰
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置屏幕显示无标题，必须启动就要设置好，否则不能再次被设置
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);

		//友盟数据统计
		MobclickAgent.onEventBegin(this, "flshlight_on");
		
		DomobUpdater.checkUpdate(this, PUBLISHER_ID);
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
				return ElecTorchActivity.this;
			}
		});
		// 将广告View增加到视图中。
//		mAdContainer.addView(mAdview320x50);
		
		SharedPreferences sp = getSharedPreferences("sunflashlight",Activity.MODE_PRIVATE);
		mCurrentStyle = sp.getInt("style",MIUI);
		Log.e("other", "mCurrentStyle = " +mCurrentStyle);
		if (!init()) {
			finish();
		}


	}

	
	public void getStyle(int style){
		switch (style) {
		case LIGHT:
			mLightImage = new int[] { R.drawable.bg, R.drawable.bg1 };
			break;
		case IPHONE:
			mLightImage = new int[] { R.drawable.device1, R.drawable.device };
			break;
		case MIUI:
			mLightImage = new int[] { R.drawable.shou_off, R.drawable.shou_on };
			break;
		case MIUIV5:
			mLightImage = new int[] { R.drawable.miuiv5_off, R.drawable.miuiv5_on };
			break;
		}
	}
	
	public boolean init() {
		lightBtn = (Button) findViewById(R.id.btn_light);
		mMenu = (Button) findViewById(R.id.btn_menu);
		getStyle(mCurrentStyle);
		lightBtn.setBackgroundResource(mLightImage[0]);
		mMenu.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(ElecTorchActivity.this,MenuActivity.class), 1);
			}
		});
		
		
		mImageViewListener = new OnClickListener() {
			public void onClick(View v) {
				if (mIsOpen) {
					mIsOpen = false;
					setLightValue(0);
				} else {
					mIsOpen = true;
					setLightValue(1);
				}
			}
		};
		lightBtn.setOnClickListener(mImageViewListener);

		try {
			mCamera = Camera.open();
			mCamera.startPreview();
		} catch (RuntimeException e) {
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
			return false;
		}
		try {
			mCamera.setPreviewDisplay(null);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
			return false;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		mCamera.setParameters(parameters);
		return true;
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		int lightid = (int) id;

		if (lightid >= 0 && lightid < LightValue.length) {
			setLightValue(LightValue[lightid]);
		}
	}

	void setLightValue(int val) {
		try {
			if (val == 0) {
				lightBtn.setBackgroundResource(mLightImage[0]);
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(parameters);
			} else {
				lightBtn.setBackgroundResource(mLightImage[1]);
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(parameters);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			onListItemClick((ListView) parent, v, position, id);
		}
	};

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCurrentStyle = resultCode;

		init();
		
		Log.e("other", "onActivityResult");
		Log.e("other", "resultCode = " +resultCode);
		Log.e("other", "requestCode = " +requestCode);
		
		
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MobclickAgent.onEventEnd(this, "flshlight_on");
		
		mIsOpen = false;
		if (mCamera != null) {
			setLightValue(0);
			mCamera.release();
			mCamera = null;
		}

	}

	@Override
	protected void onPause() {
		mIsOpen = false;
		if (mCamera != null) {
			setLightValue(0);
			mCamera.release();
			mCamera = null;
		}
		MobclickAgent.onPause(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		 MobclickAgent.onResume(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back++;
			switch (back) {
			case 1:
				Toast.makeText(ElecTorchActivity.this,
						getString(R.string.again_exit), Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				back = 0;
				Myback();
				break;
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
		
	}

	public void Myback() { // 关闭程序
		if (kaiguan) {// 开关关闭时
			ElecTorchActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// 关闭进程
		} else if (!kaiguan) {// 开关打开时
			camera.release();
			ElecTorchActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// 关闭进程
			kaiguan = true;// 避免，打开开关后退出程序，再次进入不打开开关直接退出时，程序错误
		}
	}
}