package com.hsic.qp.sz.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hsic.qp.sz.listener.WsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.ResponseData;
import util.WsUtils;

public class SubmitTask extends AsyncTask<String, Void, ResponseData> {

	private Context mContext;
	private WsListener mListener;
	ProgressDialog dialog;

	public SubmitTask(Context context, WsListener listener){
		this.mContext = context;
		this.mListener = listener;
	}

	@Override
	protected ResponseData doInBackground(String... params) {
		// TODO Auto-generated method stub
		String DeviceID = mContext.getSharedPreferences("DeviceSetting", 0).getString("DeviceID", "");

		List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("propertyName", "DeviceID");
		map1.put("propertyValue", DeviceID);
		propertyList.add(map1);

		ResponseData info = new ResponseData();
		info.setRespMsg(params[0]);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("propertyName", "RequestData");
		map2.put("propertyValue", util.json.JSONUtils.toJsonWithGson(info));
		propertyList.add(map2);

		return WsUtils.CallWs(mContext, "updateSaleInfo", propertyList);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("正在提交销售单...");
		dialog.setCancelable(false);
		dialog.show();

		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ResponseData result) {
		// TODO Auto-generated method stub
		dialog.setCancelable(true);
		Log.e("提交销售单返回", util.json.JSONUtils.toJsonWithGson(result));
		if(result.getRespCode()==0){
			dialog.dismiss();
			if(mListener!=null) mListener.WsFinish(true, 0, result.getRespMsg());
		}else{
			dialog.setMessage(result.getRespMsg());
		}

		super.onPostExecute(result);
	}
}
