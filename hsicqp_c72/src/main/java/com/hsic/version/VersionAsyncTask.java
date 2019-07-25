package com.hsic.version;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hsic.qp.sz.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *  * @author Administrator
 *
 */
public class VersionAsyncTask extends
		AsyncTask<Void, Void, Map<String, String>> {

	VersionHelper versionHelper = null;
	String endPointDefaultString = "", path = "",
			fileName = "sz.apk", file_MD5 = "", port = "";
	String min_version = "", version_explain = "",IP="";
	Context context;
	Activity activity;

	public VersionAsyncTask(Context context, Activity activity) {
		this.activity = activity;
		this.context = context;
		versionHelper = new VersionHelper(context);
		endPointDefaultString = context.getResources().getString(
				R.string.xml_default);
		port = context.getResources().getString(R.string.xml_port_default);
	}

	@Override
	protected Map<String, String> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		try {
			IP=PreferenceManager.getDefaultSharedPreferences(context).getString("APKServer", endPointDefaultString);
			
			 String path = "http://"
					 + IP
					 +":"
					 +PreferenceManager.getDefaultSharedPreferences(context) .getString("APKServerPort", port)
					 +"/SZWs/apk/AppVersion.xml";
			Log.e("path",path);
			URL url = new URL(path);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection.setConnectTimeout(5000);
			httpConnection.setRequestMethod("GET");
			httpConnection.setReadTimeout(5000);
			int responseCode = httpConnection.getResponseCode();
			
			if (responseCode == 200) {
				InputStream in = httpConnection.getInputStream();
				String[] versionInfo = XmlParser.getVersionInfo(in);
				if (versionInfo != null && versionInfo.length > 0) {
					
					map.put("newVersion", versionInfo[0]);
					map.put("file_real_path", versionInfo[1]);
					map.put("file_MD5", versionInfo[2]);
					map.put("min_version", versionInfo[3]);
					map.put("version_explain", versionInfo[4]);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return map;
		}
		return map;
	}

	@Override
	protected void onPostExecute(Map<String, String> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result.size() > 0) {
			Message message = Message.obtain();
			message.obj = result;
			handler.sendMessage(message);
		}

	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) (msg.obj);
			String newVersion = map.get("newVersion");
			path = map.get("file_real_path");//http://47.100.12.195/SZWs/apk/SZ_CV1.apk
			file_MD5 = map.get("file_MD5");
			min_version = map.get("min_version");
			version_explain = map.get("version_explain");		
			
			String[] tmp = path.split("/");
			fileName = tmp[tmp.length-1];
			
			int older = versionHelper.getVersionCode(context);
			String olderVersion = String.valueOf(older);
			float newVer = Float.parseFloat(newVersion);
			float olderVer = Float.parseFloat(olderVersion);
			
			if(min_version!=null){
				if(!min_version.equals("")){
					float minVer = Float.parseFloat(min_version);
					Log.e("olderVer="+olderVer, "minVer="+minVer);
					if (olderVer < minVer) {
						if(!activity.isFinishing()){
							updateVersion();
							return;
						}
						
						
					}
				}
			}
			if (newVer > olderVer) {
				if (!activity.isFinishing()) {					
					showNoticeDialog();
				}

			}
		};
	};

	/**
	 * 
	 * @param
	 * @param
	 * @param
	 * @param
	 */
	public void showNoticeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context); //
		builder.setTitle("版本更新"); //
		builder.setMessage("是否更新新版本"); //
		builder.setCancelable(false);
		builder.setNegativeButton("确认更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				try{
					if(!path.contains(IP)){
						String[] temp=path.split("//");// http://10.123.16.81/SHLPGPhoneWs/apk/QPManagerMoblieV52.apk
						path=temp[0]+"//"+IP+"/";
						String[] temp2=temp[1].split("/");
						int len=temp2.length;
						if(len>1){
							for(int i=1;i<len;i++){
								path=path+temp2[i]+"/";
							}							
						}
						len=path.length();
						path=path.substring(0, len-1);
					}
					if(version_explain!=null){
						if(!version_explain.equals("")){
							updateExplain(version_explain);
						}
					}
					else{
						DownLoadVersionApk downLoadApk = new DownLoadVersionApk(context);
						downLoadApk.execute(path, fileName, file_MD5);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
							
			}
		});
		builder.setPositiveButton("暂不更新",
				new DialogInterface.OnClickListener() { // ����ȷ����ť
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss(); //

					}
				});

		builder.create().show();
	}

	public void updateVersion() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context); //
		builder.setTitle("强制版本更新"); //
		builder.setMessage("目前版本小于最小版本，必须执行强制更新"); //
		builder.setCancelable(false);
		builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				DownLoadVersionApk downLoadApk = new DownLoadVersionApk(context);
				downLoadApk.execute(path, fileName, file_MD5);

			}
		});
		builder.create().show();
	}

	public void updateExplain(String explain) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context); //
		builder.setTitle("版本更新说明"); //
		builder.setMessage(explain); //
		builder.setCancelable(false);
		builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				DownLoadVersionApk downLoadApk = new DownLoadVersionApk(context);
				downLoadApk.execute(path, fileName, file_MD5);

			}
		});
		builder.setPositiveButton("暂不更新",
				new DialogInterface.OnClickListener() { //
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss(); //

					}
				});

		builder.create().show();
	}
}
