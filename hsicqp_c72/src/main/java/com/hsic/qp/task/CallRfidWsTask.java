package com.hsic.qp.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.UiUtil;
import util.WsUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import bean.ResponseData;

import com.hsic.qp.R;
import com.hsic.qp.listener.WsListener;

public class CallRfidWsTask extends AsyncTask<String, Void, ResponseData> {
	private Context mContext;
	private WsListener mListener;
	ProgressDialog dialog;
	private int mCode;
	private String mFun;
	
	public CallRfidWsTask(Context context, WsListener listener, int code){
		this.mContext = context;
		this.mListener = listener;
		this.mCode = code;
		
		if(code==1) mFun = "getPlateNumber";
		else if(code==2) mFun = "SendQP";
		else if(code==3) mFun = "ReceiveQP";
		else if(code==4) mFun = "FullQP";
		else if(code==5) mFun = "CBFullQP";
		else if(code==6) mFun = "QPXXCJ";
		else if(code==7) mFun = "getGasBaseInfo";
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
		if(mCode==1) dialog.setMessage("���ڻ�ȡ������Ϣ...");
		else if(mCode==2) dialog.setMessage("�����ϴ������Ǽ�...");
		else if(mCode==3) dialog.setMessage("�����ϴ������Ǽ�...");
		else if(mCode==4) dialog.setMessage("�����ϴ���װ�Ǽ�...");
		else if(mCode==5) dialog.setMessage("�����ϴ���ƿ������Ϣ...");
		else if(mCode==7) dialog.setMessage("���ڻ�ȡ��ƿ��Ϣ...");
		
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

	    if(mCode==1){
	    	HashMap<String, Object> map2 = new HashMap<String, Object>();
		    map2.put("propertyName", "Station");
		    map2.put("propertyValue", params[0]);
		    propertyList.add(map2);
	    }else if(mCode==7){
	    	HashMap<String, Object> map2 = new HashMap<String, Object>();
		    map2.put("propertyName", "GPNO");
		    map2.put("propertyValue", params[0]);
		    propertyList.add(map2);
	    }else{
		    ResponseData info = new ResponseData();
		    info.setRespMsg(params[0]);
		    HashMap<String, Object> map2 = new HashMap<String, Object>();
		    map2.put("propertyName", "RequestData");
		    map2.put("propertyValue", util.json.JSONUtils.toJsonWithGson(info));
		    propertyList.add(map2);
	    }
	    
	    Log.e(mFun, util.json.JSONUtils.toJsonWithGson(propertyList));
	    
		return WsUtils.CallWs(mContext, mFun, propertyList);
	}
	
	@Override
	protected void onPostExecute(ResponseData result) {
		// TODO Auto-generated method stub
		dialog.setCancelable(true);
		if(result.getRespCode()==0){
	    	dialog.dismiss();
	    	if(mListener!=null) mListener.WsFinish(true, mCode, result.getRespMsg());
		}else{
		//	dialog.setMessage("����"+result.getRespMsg());
			if(mCode==1 || mCode==7){
				dialog.setMessage("����"+result.getRespMsg());
				UiUtil.CloseDiag(dialog);
			}
			else{
				dialog.dismiss();
				if(mListener!=null) mListener.WsFinish(false, mCode, result.getRespMsg());
			}
		}
		
		super.onPostExecute(result);
	}
}
