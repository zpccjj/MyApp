package com.hsic.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

import com.hsic.qp.sz.R;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import bean.InfoItem;
import bean.ResponseData;
import hsic.ui.ConfirmDialog;
import util.ToastUtil;
import util.UiUtil;

public class FragmentConfig extends PreferenceFragment {
	private SharedPreferences mPreferences;
	BluetoothAdapter mBluetooth;

	List<InfoItem> bList = new ArrayList<InfoItem>();
	String btMac;

	private Context getContext(){
		return getActivity();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.config);

		btMac = getContext().getSharedPreferences("DeviceSetting", 0).getString("bluetooth_mac", "");
		String DeviceID = getContext().getSharedPreferences("DeviceSetting", 0).getString("DeviceID", getActivity().getResources().getString(R.string.config_no));

		mPreferences = getPreferenceScreen().getSharedPreferences();

		findPreference("webServer").setSummary(mPreferences.getString("webServer", getActivity().getResources().getString(R.string.config_ws)));

		findPreference("overTime").setSummary(mPreferences.getString("overTime", getActivity().getResources().getString(R.string.config_time)));

		findPreference("power_r").setSummary(mPreferences.getString("power_r", getActivity().getResources().getString(R.string.config_power_r)));

		findPreference("power_w").setSummary(mPreferences.getString("power_w", getActivity().getResources().getString(R.string.config_power_w)));

		findPreference("APKServer").setSummary(mPreferences.getString("APKServer", getActivity().getResources().getString(R.string.xml_default)));
		findPreference("APKServerPort").setSummary(mPreferences.getString("APKServerPort", getActivity().getResources().getString(R.string.xml_port_default)));

		findPreference("DeviceID").setSummary(DeviceID);

		findPreference("bluetooth_mac").setSummary(btMac);

		String verString = "";
		try {
			PackageInfo packageinfo = getActivity().getPackageManager()
					.getPackageInfo("com.hsic.qp.sz", 0);
			verString = "V " + packageinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		findPreference("version").setSummary(verString);

		Log.e("bluetooth_mac", "="+btMac);
		mBluetooth = BluetoothAdapter.getDefaultAdapter();

		if (mBluetooth != null) {
			if(!mBluetooth.isEnabled()){
				ConfirmDialog dialog = new ConfirmDialog(getActivity());
				dialog.setTitle("提示");
				dialog.setMessage("蓝牙打印设备未配置，是否打开蓝牙配置？");
				dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
						startActivity(intent);
						return;
					}

				});
				dialog.show();
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mBluetooth != null) {
//			if(!mBluetooth.isEnabled()){
//				ToastUtil.showToast(getContext(), "请打开蓝牙并配对打印设备");
//				Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
//		        startActivity(intent);
//		        return;
//			}
			if(mBluetooth.isEnabled()){
				bList.clear();
				Set<BluetoothDevice> devices = mBluetooth.getBondedDevices();
				bList = UiUtil.getBluetoothList(devices);
				if(btMac==null || btMac.length()==0){
					if (bList.size()>0) {
						DialogChoice(getContext(), bList, btMac);
					}else{
						ToastUtil.showToast(getContext(), "请先配对打印设备");
//						Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
//				        startActivity(intent);
					}
				}else{
					if (bList.size()==0) {
						ToastUtil.showToast(getContext(), "请先配对打印设备");
//						Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
//				        startActivity(intent);
					}else{
						if(!UiUtil.hasBluetoothInfoInDeviceList(bList, btMac)){
							ToastUtil.showToast(getContext(), "默认蓝牙设备已被移除配对列表，请重新选择打印设备");
							DialogChoice(getContext(), bList, btMac);
						}
					}
				}
			}
		}
	}

	AlertDialog mDialogChoice;
	private void DialogChoice(final Context context, final List<InfoItem> list, final String Mac) {
		final int[] ChoiceID = {-1};
		final String items[] = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			items[i] = list.get(i).getValue();
			if(Mac!=null && Mac.length()>0 && list.get(i).getKey().equals(Mac)){
				ChoiceID[0] = i;
			}
		}

		Log.e("Mac="+Mac, util.json.JSONUtils.toJsonWithGson(items));

		AlertDialog.Builder builder = new AlertDialog.Builder(context,3);
		builder.setTitle("选择蓝牙设备");
		builder.setSingleChoiceItems(items, ChoiceID[0],
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ChoiceID[0] = which;
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.e("ChoiceID[0]", "="+ChoiceID[0]);
				if(ChoiceID[0]==-1){
					ToastUtil.showToast(getContext(),  "请选择设备");
					UiUtil.setDiagBtn(dialog, false);
				}else{
					Log.e("select", list.get(ChoiceID[0]).getValue());

					setBtMac(list.get(ChoiceID[0]).getKey());

					UiUtil.setDiagBtn(dialog, true);
				}
			}
		});
		builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UiUtil.setDiagBtn(dialog, true);
			}
		});
		builder.setNeutralButton("打印测试", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				UiUtil.setDiagBtn(dialog, false);
				if(ChoiceID[0]==-1)
					ToastUtil.showToast(getContext(),  "请选择设备");
				else{
					new SocketTask(getContext()).execute(list.get(ChoiceID[0]).getKey());
				}
			}
		});

		mDialogChoice = builder.create();
		mDialogChoice.show();
	}

	private void setBtMac(String Mac){
		Preference pref = findPreference("bluetooth_mac");
		pref.setSummary(Mac);

		SharedPreferences.Editor mEditor = getContext().getSharedPreferences("DeviceSetting", 0).edit();
		mEditor.putString("bluetooth_mac", Mac);
		mEditor.commit();
		btMac = Mac;
		ToastUtil.showToast(getContext(),  "保存默认打印设备");
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
										 Preference preference) {
		// TODO Auto-generated method stub
		if (preference.getKey().equals("bluetooth_mac")) {
			//已配对蓝牙选择
			DialogChoice(getContext(), bList, btMac);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(
						mOnSharedPreferenceChangeListener);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(
						mOnSharedPreferenceChangeListener);
	}

	private OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			Preference pref = findPreference(key);
			if (pref instanceof EditTextPreference) {
				EditTextPreference etp = (EditTextPreference) pref;
				pref.setSummary(etp.getText());
			}
		}
	};

	class SocketTask extends AsyncTask<String, Void, ResponseData> {
		ProgressDialog dialog;
		Context mContext;
		public SocketTask(Context context){
			this.mContext = context;
		}

		@Override
		protected ResponseData doInBackground(String... params) {
			// TODO Auto-generated method stub
			ResponseData res = new ResponseData();
			UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
			BluetoothDevice device = mBluetooth.getRemoteDevice(params[0]);
			BluetoothSocket bluetoothSocket;
			OutputStream outputStream;
			try {
				bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);

				bluetoothSocket.connect();

				outputStream = bluetoothSocket.getOutputStream();
			}catch (Exception ex) {
				System.out.print("远程获取设备出现异常");
				res.setRespCode(1);
				res.setRespMsg("连接设备失败");
				ex.printStackTrace();
				return res;
			}

			try{
				String Data = params[0] + "\n\n\n";
				byte[] data = Data.getBytes("gbk");
				outputStream.write(data, 0, data.length);
				outputStream.flush();
				res.setRespCode(0);
			}catch (Exception ex) {
				System.out.print("远程获取设备出现异常");
				res.setRespCode(1);
				res.setRespMsg("打印异常");
				ex.printStackTrace();
			}finally{
				try {
					if (bluetoothSocket != null) {
						bluetoothSocket.close();
					}

					if (outputStream != null) {
						outputStream.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			return res;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog = new ProgressDialog(mContext);
			dialog.setMessage("打印测试...");
			dialog.setCancelable(false);
			dialog.show();

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(ResponseData result) {
			// TODO Auto-generated method stub
			dialog.setCancelable(true);
			if(result.getRespCode()==0){
				dialog.dismiss();
				ToastUtil.showToast(getContext(),  "打印测试成功");
			}else{
				dialog.setMessage(result.getRespMsg());
			}

			super.onPostExecute(result);
		}
	}

}
