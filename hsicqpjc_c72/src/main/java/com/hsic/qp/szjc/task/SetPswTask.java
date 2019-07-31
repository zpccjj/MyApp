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

import bean.ResponseData;
import bll.sqlite.UserInfo_DB;
import util.MD5Utils;
import util.UiUtil;
import util.WsUtils;

public class SetPswTask extends AsyncTask<String, Void, ResponseData> {

	private Context mContext;
	private WsListener mListener;
	ProgressDialog dialog;

	public SetPswTask(Context context, WsListener listener){
		this.mContext = context;
		this.mListener = listener;
	}

	@Override
	protected ResponseData doInBackground(String... params) {
		// TODO Auto-generated method stub
		ResponseData ret = new ResponseData();

		String DeviceID = PreferenceManager.getDefaultSharedPreferences(mContext).getString("DeviceID", "");

		List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("propertyName", "DeviceID");
		map1.put("propertyValue", DeviceID);
		propertyList.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("propertyName", "UserLoginID");
		map2.put("propertyValue", params[0]);
		propertyList.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("propertyName", "UserPWD");
		map3.put("propertyValue", MD5Utils.MD5(params[1]));
		propertyList.add(map3);

		ret = WsUtils.CallWs(mContext, "UpdateUserPassword", propertyList);

		if(ret.getRespCode()==0){
			//更新数据库用户密码
			UserInfo_DB db = new UserInfo_DB(mContext);
			if(db.UpdatePsw(params[0], MD5Utils.MD5(params[1]))){
				ret.setRespMsg(params[1]);//新密码返回
			}else{
				ret.setRespCode(1);
				ret.setRespMsg("本地更新密码失败");
			}
		}
		return ret;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("正在修改用户密码...");
		dialog.setCancelable(false);
		dialog.show();

		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ResponseData result) {
		// TODO Auto-generated method stub
		dialog.setCancelable(true);
		if(result.getRespCode()==0){
			Log.e("result", util.json.JSONUtils.toJsonWithGson(result));
			dialog.dismiss();
			if(mListener!=null) mListener.WsFinish(true, 0, result.getRespMsg());
		}else{
			dialog.setMessage("失败："+result.getRespMsg());
			UiUtil.CloseDiag(dialog);
		}

		super.onPostExecute(result);
	}
}
