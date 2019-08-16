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
import util.UiUtil;
import util.WsUtils;

public class GetTruckListTask extends AsyncTask<String, Void, ResponseData> {
	private Context mContext;
	private WsListener mListener;
	ProgressDialog dialog;

	public GetTruckListTask(Context context, WsListener listener){
		this.mContext = context;
		this.mListener = listener;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("正在获取车辆信息...");
		dialog.setCancelable(false);
		dialog.show();

		super.onPreExecute();
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

		Log.e("propertyList", util.json.JSONUtils.toJsonWithGson(propertyList));

		return WsUtils.CallWs(mContext, "getPlateNumber", propertyList);
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
