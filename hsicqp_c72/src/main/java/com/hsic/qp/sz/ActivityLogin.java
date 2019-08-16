package com.hsic.qp.sz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import util.ActivityUtils;
import util.MD5Utils;
import util.ToastUtil;
import util.UiUtil;
import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import bean.InfoItem;
import bean.EmployeeInfo;
import bean.MediaGoods;
import com.actionbarsherlock.view.MenuItem;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.LoginTask;
import com.hsic.version.VersionAsyncTask;

import hsic.ui.HsicActivity;

public class ActivityLogin extends HsicActivity implements WsListener{
	private final static String MenuHOME = "设置";

	static class mView{
		EditText user;
		EditText psw;
		Button loginBtn;
		Button backBtn;
		CheckBox cb;
	}

	mView mV;

	private Context getContext(){
		return ActivityLogin.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		initViews();
		setListener();

		SharedPreferences settings = this.getSharedPreferences("DeviceSetting", 0);
		String id = settings.getString("DeviceID", getResources().getString(R.string.config_no));
		if(id==null || id.length()==0){
			TelephonyManager tm = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);
			String deviceN0 = "00000000";
			deviceN0 += tm.getDeviceId();

			SharedPreferences.Editor editor = settings.edit();

			editor.putString("DeviceID", deviceN0.substring(deviceN0.length()-8, deviceN0.length()));
			editor.commit();
		}
		util.SoundUtil.initSoundPool(getContext());
		getBluetoothMac();

		new VersionAsyncTask(getContext(), ActivityLogin.this).execute();
	}


	BluetoothAdapter mBluetooth;
	private void getBluetoothMac(){
		mBluetooth = BluetoothAdapter.getDefaultAdapter();
		if (mBluetooth == null) {
			ToastUtil.showToast(getContext(), "本机未找到蓝牙功能");
			return ;
		}

		String btMac = getContext().getSharedPreferences("DeviceSetting", 0).getString("bluetooth_mac", "");
		Set<BluetoothDevice> devices = mBluetooth.getBondedDevices();
		List<InfoItem> bList = UiUtil.getBluetoothList(devices);
		Log.e("bluetooth_mac", "="+btMac);
		if(!mBluetooth.isEnabled() || btMac==null || btMac.length()==0 || bList.size()==0 || !UiUtil.hasBluetoothInfoInDeviceList(bList, btMac)){
			//	ActivityUtils.JumpToConfig(getContext());
			ToastUtil.showToast(getContext(), "未设置蓝牙打印设备");
		}
	}

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
		mV.backBtn = (Button) findViewById(R.id.login_back);

		mV.cb = (CheckBox) findViewById(R.id.login_3);

		getLogin();
	}

	private void getLogin(){
		SharedPreferences settings = this.getSharedPreferences("DeviceSetting", 0);
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
		SharedPreferences settings = getSharedPreferences("DeviceSetting",0);
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
				UiUtil.CloseKey(ActivityLogin.this);

				if(mV.user.getText().toString().trim().length()==0 || mV.psw.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), getResources().getString(R.string.wrong_login));
					return ;
				}

				CallLogin(mV.user.getText().toString().trim(), mV.psw.getText().toString().trim());

//				downing();
			}
		});

		mV.backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void CallLogin(String ID, String PSW){
		new LoginTask(getContext(), this).execute(ID, MD5Utils.MD5(PSW));
		//	ActivityUtils.JumpToTaskList(getContext());
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			EmployeeInfo user = (EmployeeInfo) util.json.JSONUtils.toObjectWithGson(retData, EmployeeInfo.class);
			//EmployeeInfo user = new EmployeeInfo();
			user.setUserID(mV.user.getText().toString().trim());
			//user.setPassword(MD5Utils.MD5(mV.psw.getText().toString().trim()));

			List<MediaGoods> MG = new ArrayList<MediaGoods>();
			for (int i = 0; i < user.getGoodsList().size(); i++) {
				boolean hasMedia = false;

				for (int j = 0; j < MG.size(); j++) {
					if(MG.get(j).getMediaCode().equals(user.getGoodsList().get(i).getMediumCode())){
						hasMedia = true;
						MG.get(j).getGoods().add(user.getGoodsList().get(i));
						break;
					}
				}
				if(!hasMedia){
					MediaGoods mgGoods = new MediaGoods();
					mgGoods.setMediaCode(user.getGoodsList().get(i).getMediumCode());
					mgGoods.setMediaName(user.getGoodsList().get(i).getCZJZ());
					mgGoods.getGoods().add(user.getGoodsList().get(i));
					MG.add(mgGoods);
				}
			}
			Log.e("MG", util.json.JSONUtils.toJsonWithGson(MG));
			getApp().setMediaInfo(MG);
			getApp().setLogin(user);

			ActivityUtils.JumpToMain(getContext());

			setLogin(mV.cb.isChecked(), mV.user.getText().toString().trim(), mV.psw.getText().toString().trim());
		}
	}
}
