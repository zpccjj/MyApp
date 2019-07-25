package com.hsic.qp.sz.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hsic.qp.sz.listener.WsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.EmployeeInfo;
import bean.ResponseData;
import util.UiUtil;
import util.WsUtils;

public class LoginTask extends AsyncTask<String, Void, ResponseData> {

	private Context mContext;
	private WsListener mListener;
	ProgressDialog dialog;

	public LoginTask(Context context, WsListener listener){
		this.mContext = context;
		this.mListener = listener;
	}

	@Override
	protected ResponseData doInBackground(String... params) {
		// TODO Auto-generated method stub
		//params[0];params[1];
		String DeviceID = mContext.getSharedPreferences("DeviceSetting", 0).getString("DeviceID", "");

		List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("propertyName", "DeviceID");
		map1.put("propertyValue", DeviceID);
		propertyList.add(map1);

		EmployeeInfo user = new EmployeeInfo();
		user.setUserID(params[0]);
		user.setPassword(params[1]);

		ResponseData info = new ResponseData();
		info.setRespMsg(util.json.JSONUtils.toJsonWithGson(user));

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("propertyName", "RequestData");
		map2.put("propertyValue", util.json.JSONUtils.toJsonWithGson(info));
		propertyList.add(map2);

		return WsUtils.CallWs(mContext, "EmployeeLogin", propertyList);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("正在登陆...");
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
			if(mListener!=null) mListener.WsFinish(true, 1, result.getRespMsg());
		}else{
			dialog.setMessage("错误："+result.getRespMsg());
			UiUtil.CloseDiag(dialog);
		}

		super.onPostExecute(result);
	}
}
