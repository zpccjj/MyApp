package com.hsic.qp.szjc.task;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hsic.qp.szjc.listener.WsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.DownBasicInfo;
import bean.ResponseData;
import bll.sqlite.UserInfo_DB;
import util.UiUtil;
import util.WsUtils;

public class DownloadTask extends AsyncTask<String, Void, ResponseData> {

	private Context mContext;
	private WsListener mListener;
	ProgressDialog dialog;

	public DownloadTask(Context context, WsListener listener){
		this.mContext = context;
		this.mListener = listener;
	}

	@Override
	protected ResponseData doInBackground(String... params) {
		// TODO Auto-generated method stub
		ResponseData ret = new ResponseData();
		UserInfo_DB db = new UserInfo_DB(mContext);
		if(!db.deleteAll()){
			ret.setRespCode(0);
			ret.setRespMsg("删除基本信息数据失败");
		}


		List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("propertyName", "DeviceID");
		map1.put("propertyValue", params[0]);
		propertyList.add(map1);

		ret = WsUtils.CallWs(mContext, "DownBasicInfo", propertyList);
		if(ret.getRespCode()==0){
			DownBasicInfo basicInfo = (DownBasicInfo) util.json.JSONUtils.toObjectWithGson(ret.getRespMsg(), DownBasicInfo.class);

			if(basicInfo!=null && basicInfo.getUserInfoCount()>0 && basicInfo.getUserInfos()!=null && basicInfo.getUserInfos().size()>0){
				boolean isOk = db.Download(basicInfo.getUserInfos());
				if(isOk){
					ret.setRespCode(0);
					ret.setRespMsg("基本信息数据下载成功");
				}else{
					ret.setRespCode(1);
					ret.setRespMsg("基本信息数据保存失败");
				}

			}else{
				ret.setRespCode(1);
				ret.setRespMsg("无基本信息数据");
			}

		}

		return ret;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("正在下载基本信息...");
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
			if(mListener!=null) mListener.WsFinish(true, 0, result.getRespMsg());
		}else{
			dialog.setMessage("错误："+result.getRespMsg());
			UiUtil.CloseDiag(dialog);
		}

		super.onPostExecute(result);
	}
}
