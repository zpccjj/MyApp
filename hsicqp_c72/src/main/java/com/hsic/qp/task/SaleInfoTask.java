package com.hsic.qp.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import util.UiUtil;
import util.WsUtils;

import com.hsic.qp.R;
import com.hsic.qp.listener.WsListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import bean.ResponseData;

public class SaleInfoTask extends AsyncTask<String, Void, ResponseData> {

	private Context mContext;
	private int mType;
	private WsListener mListener;
	ProgressDialog dialog;
	
	public SaleInfoTask(Context context, int iType, WsListener listener){
		this.mContext = context;
		this.mType = iType;
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
	    
	    HashMap<String, Object> map2 = new HashMap<String, Object>();
	    map2.put("propertyName", "Userid");
	    map2.put("propertyValue", params[0]);
	    propertyList.add(map2);
	    
	    HashMap<String, Object> map3 = new HashMap<String, Object>();
	    map3.put("propertyName", "iType");
	    map3.put("propertyValue", mType);
	    propertyList.add(map3);
	    
	    HashMap<String, Object> map4 = new HashMap<String, Object>();
	    map4.put("propertyName", "CheckPassword");
	    map4.put("propertyValue", "hsic888888");
	    propertyList.add(map4);
	    
	    Log.e("propertyList", util.json.JSONUtils.toJsonWithGson(propertyList));
	    
		return WsUtils.CallWs(mContext, "SearchAssignSaleInfo", propertyList);
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
	    dialog.setMessage("正在获取销售单信息...");
	    if(this.mType==1) dialog.setMessage("正在获取补打信息...");
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
