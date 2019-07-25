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
		else if(code==8) mFun = "getInitInfo";
		else if(code==9) mFun = "AddGasBaseInfo";
		else if(code==10) mFun = "SearchSaleInfo";
		else if(code==11) mFun = "getQPInfo";
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
		if(mCode==1) dialog.setMessage("正在获取车辆信息...");
		else if(mCode==2) dialog.setMessage("正在上传出厂登记...");
		else if(mCode==3) dialog.setMessage("正在上传进场登记...");
		else if(mCode==4) dialog.setMessage("正在上传充装登记...");
		else if(mCode==5) dialog.setMessage("正在上传气瓶检验信息...");
		else if(mCode==6) dialog.setMessage("正在上传采集信息...");
		else if(mCode==7) dialog.setMessage("正在获取气瓶信息...");
		else if(mCode==8) dialog.setMessage("正在获取基础数据...");
		else if(mCode==9) dialog.setMessage("正在提交基本信息...");
		else if(mCode==10) dialog.setMessage("正在获取打印信息...");
		else if(mCode==11) dialog.setMessage("正在校验气瓶充装商品...");

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

			HashMap<String, Object> map3 = new HashMap<String, Object>();
			map3.put("propertyName", "iType");
			map3.put("propertyValue", params[1].equals("0")? 7 : 1);
			propertyList.add(map3);

		}else if(mCode==7){
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("propertyName", "GPNO");
			map2.put("propertyValue", params[0]);
			propertyList.add(map2);
		}else if(mCode==8){
		}else if(mCode==10){
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("propertyName", "SaleID");
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

		Log.e(mFun, "mCode = " + mCode);

		return WsUtils.CallWs(mContext, mFun, propertyList);
	}

	@Override
	protected void onPostExecute(ResponseData result) {
		// TODO Auto-generated method stub
		dialog.setCancelable(true);
		//Log.e("CallRfidWsTask", util.json.JSONUtils.toJsonWithGson(result));
		if(result.getRespCode()==0){
			dialog.dismiss();
			if(mListener!=null) mListener.WsFinish(true, mCode, result.getRespMsg());
		}else{
			//	dialog.setMessage("错误："+result.getRespMsg());
			if(mCode==1 || mCode==2 || mCode==3 || mCode==7 || mCode==8 || mCode==9 || mCode==10){
				dialog.setMessage("错误："+result.getRespMsg());
				if(mCode==1) dialog.setMessage(result.getRespMsg());
				UiUtil.CloseDiag(dialog);
				if(mListener!=null) mListener.WsFinish(false, mCode, result.getRespMsg());
			}else{
				dialog.dismiss();
				if(mListener!=null) mListener.WsFinish(false, mCode, result.getRespMsg());
			}
		}

		super.onPostExecute(result);
	}
}
