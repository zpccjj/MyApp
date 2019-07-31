package com.hsic.qp.szjc.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hsic.qp.szjc.listener.WsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.NewGZWFQPCheck;
import bean.ResponseData;
import bll.sqlite.NewGZWFQPCheck_DB;
import util.NetWorkUtils;
import util.UiUtil;
import util.WsUtils;

public class UploadTask extends AsyncTask<String, Void, ResponseData> {

	private Context mContext;
	private WsListener mListener;
	private boolean isAll;
	ProgressDialog dialog;
	private int failNum = -1;

	public UploadTask(Context context, WsListener listener, boolean isall){
		this.mContext = context;
		this.mListener = listener;
		this.isAll = isall;
	}

	@Override
	protected ResponseData doInBackground(String... params) {
		// TODO Auto-generated method stub
		String DeviceID = PreferenceManager.getDefaultSharedPreferences(mContext).getString("DeviceID", "");
		ResponseData ret = new ResponseData();

		NewGZWFQPCheck_DB db = new NewGZWFQPCheck_DB(mContext);
		boolean existDate = false;
		if(isAll){
			if(db.isExist()){
				existDate = true;
			}
		}else{
			if(db.isExist("0")){
				existDate = true;
			}
		}

		if(existDate){
			//检查网络
			ret = NetWorkUtils.isNetWork(mContext);
			if(ret.getRespCode()!=0) return ret;

			List<NewGZWFQPCheck> list = db.getUploadCheck(isAll);
			failNum = 0;
			for (int i = 0; i < list.size(); i++) {
				String code = list.get(i).getQPDJCODE();
				list.get(i).setQPDJCODE("0131" + code);
				Log.e("记录 "+ i, util.json.JSONUtils.toJsonWithGson(list.get(i)));


				List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("propertyName", "DeviceID");
				map1.put("propertyValue", DeviceID);
				propertyList.add(map1);

				ResponseData info = new ResponseData();
				info.setRespCode(2);
				info.setRespMsg(util.json.JSONUtils.toJsonWithGson(list.get(i)));

				HashMap<String, Object> map2 = new HashMap<String, Object>();
				map2.put("propertyName", "RequestData");
				map2.put("propertyValue", util.json.JSONUtils.toJsonWithGson(info));
				propertyList.add(map2);

				ret = WsUtils.CallWs(mContext, "UpQPJYInfo", propertyList);
				Log.e("ret", util.json.JSONUtils.toJsonWithGson(ret));
				if(ret.getRespCode()==0){
					if(list.get(i).getState().equals("0")){
						db.updateCheckState(code, list.get(i).getJCRQ());
					}
				}else{
					failNum++;
				}
			}
		}else{
			ret.setRespCode(2);
			ret.setRespMsg("无上传记录");
			return ret;
		}

		if(failNum==0){
			ret.setRespCode(0);
			ret.setRespMsg("上传成功");
		}else{
			ret.setRespCode(2);
			ret.setRespMsg("上传完成，上传失败记录数："+failNum);
		}
		return ret;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("正在上传检验记录...");
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
		}else if(result.getRespCode()==2){
			dialog.setMessage(result.getRespMsg());
			UiUtil.CloseDiag(dialog);
			if(mListener!=null) mListener.WsFinish(true, 2, result.getRespMsg());
		}else {
			dialog.setMessage("错误："+result.getRespMsg());
			UiUtil.CloseDiag(dialog);
		}

		super.onPostExecute(result);
	}

}
