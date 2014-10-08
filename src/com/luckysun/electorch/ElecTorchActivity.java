package com.luckysun.electorch;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdManager.ErrorCode;
import cn.domob.android.ads.DomobAdView;
import cn.domob.android.ads.DomobUpdater;

import com.luckysun.electorch.menu.MenuActivity;
import com.umeng.analytics.MobclickAgent;

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
	public static boolean kaiguan = true; // 锟斤拷锟藉开锟斤拷状态锟斤拷状态为false锟斤拷锟斤拷状态锟斤拷状态为true锟斤拷锟截憋拷状态
	// public static boolean action = false;
	// //锟斤拷锟斤拷锟阶刺�锟斤拷状态为false锟斤拷锟斤拷前锟斤拷锟芥不锟剿筹拷锟斤拷状态为true锟斤拷锟斤拷前锟斤拷锟斤拷锟剿筹拷
	private int back = 0;// 锟叫断帮拷锟斤拷锟斤拷back

	private OnClickListener mImageViewListener = null;

	private Camera mCamera = null;
	RelativeLayout mAdContainer;
	DomobAdView mAdview320x50;
	SurfaceView surfaceview;
	SurfaceHolder holder;
	
//	private static final String[] KeyLightOptions = new String[] {"Close Camera LED", "Open CameraLED", };
//	private static final int[] LightValue = new int[] { 0, 1, };
	
	private int[] mLightImage ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全锟斤拷锟斤拷锟矫ｏ拷锟斤拷锟截达拷锟斤拷锟斤拷锟斤拷装锟斤拷
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 锟斤拷锟斤拷锟斤拷幕锟斤拷示锟睫憋拷锟解，锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷要锟斤拷锟矫好ｏ拷锟斤拷锟斤拷锟斤拷锟劫次憋拷锟斤拷锟斤拷
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main);

		//锟斤拷锟斤拷锟斤拷锟酵筹拷锟�
		MobclickAgent.onEventBegin(this, "flshlight_on");
		
		/**
		 * Domob ad.
		 */
		DomobUpdater.checkUpdate(this, PUBLISHER_ID);
		mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
		mAdview320x50 = new DomobAdView(this, ElecTorchActivity.PUBLISHER_ID,DomobAdView.INLINE_SIZE_320X50);
//		mAdview320x50.setKeyword("game");
//		mAdview320x50.setUserGender("male");
//		mAdview320x50.setUserBirthdayStr("2000-08-08");
//		mAdview320x50.setUserPostcode("123456");

		mAdview320x50.setAdEventListener(new DomobAdEventListener() {

			@Override
			public void onDomobAdReturned(DomobAdView adView) {
				Log.i("DomobSDKDemo", "onDomobAdReturned");
			}

			@Override
			public void onDomobAdOverlayPresented(DomobAdView adView) {
				Log.i("DomobSDKDemo", "overlayPresented");
			}

			@Override
			public void onDomobAdOverlayDismissed(DomobAdView adView) {
				Log.i("DomobSDKDemo", "Overrided be dismissed");
			}

			@Override
			public void onDomobAdClicked(DomobAdView arg0) {
				Log.i("DomobSDKDemo", "onDomobAdClicked");
			}

			@Override
			public void onDomobAdFailed(DomobAdView arg0, ErrorCode arg1) {
				Log.i("DomobSDKDemo", "onDomobAdFailed");
			}

			@Override
			public void onDomobLeaveApplication(DomobAdView arg0) {
				Log.i("DomobSDKDemo", "onDomobLeaveApplication");
			}

			@Override
			public Context onDomobAdRequiresCurrentContext() {
				return ElecTorchActivity.this;
			}
		});
		
//		mAdContainer.addView(mAdview320x50);
		
		SharedPreferences sp = getSharedPreferences("sunflashlight",Activity.MODE_PRIVATE);
		mCurrentStyle = sp.getInt("style",MIUI);
		Log.e("other", "mCurrentStyle = " +mCurrentStyle);
		
		initSurfaceView();
	}
	
	private void initSurfaceView() {
		surfaceview = (SurfaceView) findViewById(R.id.surfaceView);
		surfaceview.setZOrderOnTop(true);
		surfaceview.setBackgroundColor(-2);
		holder = surfaceview.getHolder();
		holder.addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
			}
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
			}
		});
		holder.setFormat(-2);
		
		
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
					if(!initCamera()){
					}
					setLightValue(0);
				} else {
					mIsOpen = true;
					if(!initCamera()){
					}
					setLightValue(1);
				}
			}
		};
		lightBtn.setOnClickListener(mImageViewListener);
		
	}

	
	public void getStyle(int style){
		mLightImage = null;
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
	

	private boolean initCamera(){
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
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
			return false;
		}
		
//		Camera.Parameters parameters = mCamera.getParameters();
//		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//		mCamera.setParameters(parameters);
		return true;
	}
	
	
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		int lightid = (int) id;
//
//		if (lightid >= 0 && lightid < LightValue.length) {
//			setLightValue(LightValue[lightid]);
//		}
//	}

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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(mCurrentStyle != resultCode){
			mCurrentStyle = resultCode;
			getStyle(mCurrentStyle);
			lightBtn.setBackgroundResource(mLightImage[0]);
		}
		

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

	long mExitTime;
	
	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(this, R.string.again_exit, Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			Myback();
		}
		
	}
	
	public void Myback() { // 锟截闭筹拷锟斤拷
		if (kaiguan) {// 锟斤拷锟截关憋拷时
			ElecTorchActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// 锟截闭斤拷锟�
		} else if (!kaiguan) {// 锟斤拷锟截达拷时
			camera.release();
			ElecTorchActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// 锟截闭斤拷锟�
			kaiguan = true;// 锟斤拷锟解，锟津开匡拷锟截猴拷锟剿筹拷锟斤拷锟斤拷锟劫次斤拷锟诫不锟津开匡拷锟斤拷直锟斤拷锟剿筹拷时锟斤拷锟斤拷锟斤拷锟斤拷锟�
		}
	}
	
}