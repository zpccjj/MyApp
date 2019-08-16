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
import bean.InfoItem;
import bean.ResponseData;

public class GetPrintInfoTask extends AsyncTask<String, Void, ResponseData> {
	private Context mContext;
	private WsListener mListener;
	ProgressDialog dialog;

	public GetPrintInfoTask(Context context, WsListener listener){
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

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("propertyName", "SaleID");
		map2.put("propertyValue", params[0]);
		propertyList.add(map2);

		Log.e("propertyList", util.json.JSONUtils.toJsonWithGson(propertyList));

		//return WsUtils.CallWs(mContext, "PrintInfo", propertyList);

		//test
		ResponseData result = new ResponseData();
		result.setRespCode(0);

		List<InfoItem> infoList = new ArrayList<InfoItem>();
		InfoItem item = new InfoItem();
		item.setKey("1");
		item.setName("销售单号：");
		item.setValue(params[0]);
		infoList.add(item);

		item = new InfoItem();
		item.setKey("2");
		item.setName("客户名称：");
		item.setValue("测试员");
		infoList.add(item);

		item = new InfoItem();
		item.setKey("3");
		item.setName("联系电话：");
		item.setValue("12345678901");
		infoList.add(item);

		item = new InfoItem();
		item.setKey("4");
		item.setName("发瓶号：");
		item.setValue("XXXXXXX");
		infoList.add(item);

		item = new InfoItem();
		item.setKey("5");
		item.setName("收瓶号：");
		item.setValue("YYYYYYYYY");
		infoList.add(item);

		item = new InfoItem();
		item.setKey("6");
		item.setName("销售员：");
		item.setValue("测试销售");
		infoList.add(item);

		item = new InfoItem();
		item.setKey("7");
		item.setName("客户签名：");
		item.setValue("");
		infoList.add(item);

		item = new InfoItem();
		item.setKey("8");
		item.setName("销售日期：");
		item.setValue("2018-09-25");
		infoList.add(item);

		result.setRespMsg(util.json.JSONUtils.toJsonWithGson(infoList));

		return result;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("正在获取打印信息...");
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
			dialog.setMessage(result.getRespMsg());
		}

		super.onPostExecute(result);
	}
}
