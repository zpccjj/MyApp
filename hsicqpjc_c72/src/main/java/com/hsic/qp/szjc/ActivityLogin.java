package com.hsic.qp.szjc;

import util.ActivityUtils;
import util.MD5Utils;
import util.ToastUtil;
import util.UiUtil;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import bean.UserInfo;
import bll.sqlite.UserInfo_DB;

import com.actionbarsherlock.view.MenuItem;
import com.hsic.qp.szjc.R;
import com.hsic.qp.szjc.listener.WsListener;
import com.hsic.qp.szjc.task.DownloadTask;

import hsic.ui.HsicActivity;

public class ActivityLogin extends HsicActivity implements WsListener{
	private final static String MenuHOME = "设置";

	static class mView{
		EditText user;
		EditText psw;
		Button loginBtn;
		Button login_info;
		CheckBox cb;

		TextView tv1;
		TextView tv2;
	}

	mView mV;
	String DeviceID = "";

	private Context getContext(){
		return ActivityLogin.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//初始化读卡声音
		util.SoundUtil.initSoundPool(getContext());

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		initViews();
		setListener();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				ActivityUtils.JumpToConfig(getContext());
				break;
		}
		return true;
	}

	private void initViews(){
		mV = new mView();
		mV.user = (EditText) findViewById(R.id.login_1);
		mV.psw = (EditText) findViewById(R.id.login_2);

		mV.loginBtn = (Button) findViewById(R.id.login_btn);
		mV.login_info = (Button) findViewById(R.id.login_info);

		mV.cb = (CheckBox) findViewById(R.id.login_3);

		mV.tv1 = (TextView) findViewById(R.id.login_tv1);
		mV.tv2 = (TextView) findViewById(R.id.login_tv2);

		String verString = "";
		try {
			PackageInfo packageinfo = getPackageManager().getPackageInfo("com.hsic.qp.szjc", 0);
			verString = "V " + packageinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		mV.tv1.setText("应用版本:"+verString);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		DeviceID = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("DeviceID", "");
		mV.tv2.setText("设备编号:"+DeviceID);
		getLogin();
	}

	private void getLogin(){
		SharedPreferences settings = this.getSharedPreferences("szjc_user", 0);
		boolean issave = settings.getBoolean("issave", false);
		String user = settings.getString("user", "");
		String psw = settings.getString("psw", "");

		mV.cb.setChecked(issave);
		if(issave){
			mV.user.setText(user);
			mV.psw.setText(psw);
		}
	}

	private void setLogin(boolean isSave, String user, String psw){
		SharedPreferences settings = getSharedPreferences("szjc_user",0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putBoolean("issave", isSave);
		if(isSave){
			editor.putString("user", user);
			editor.putString("psw", psw);
		}
		editor.commit();
	}

	private void setListener(){
		mV.loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mV.user.getText().toString().trim().length()==0 || mV.psw.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), getResources().getString(R.string.wrong_login));
					return ;
				}

				DoLogin(mV.user.getText().toString().trim(), mV.psw.getText().toString().trim());

			}
		});

		mV.login_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(DeviceID==null || DeviceID.length()==0){
					ToastUtil.showToast(getContext(), "设备编号未设置");
					return ;
				}
				new DownloadTask(getContext(), ActivityLogin.this).execute(DeviceID);
			}
		});
	}

	private void DoLogin(String ID, String PSW){
		UserInfo_DB db = new UserInfo_DB(getContext());

		UserInfo loginUser = db.Login(ID, MD5Utils.MD5(PSW));

		if(loginUser==null){
			ToastUtil.showToast(getContext(), getResources().getString(R.string.wrong_login2));
			return ;
		}

		getApp().setJCList(db.GetUserList("J9002"));
		getApp().setSHList(db.GetUserList("J9003"));
		getApp().setLoginer(loginUser);
		setLogin(mV.cb.isChecked(), mV.user.getText().toString().trim(), mV.psw.getText().toString().trim());

		ActivityUtils.JumpToMenu(getContext());
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			ToastUtil.showToast(getContext(), "基本信息下载成功");
		}
	}

	private boolean isExit = false;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!isExit) {
			isExit = true;
			ToastUtil.showToast(getContext(), "再按一次退出");
			mHandler.sendEmptyMessageDelayed(0, 3000);
		} else {
			finish();
		}
	}
}
