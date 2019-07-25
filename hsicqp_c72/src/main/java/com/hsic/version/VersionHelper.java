package com.hsic.version;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;

import java.io.File;

//import com.hsic.ftp.MySFTP;
//import com.jcraft.jsch.ChannelSftp;

public class VersionHelper {
	public Context context;
	public VersionHelper(Context context){
		this.context=context;
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//			StrictMode.setVmPolicy(builder.build());
//		}
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
	}
	/**
	 *
	 * @param context
	 * @return
	 */
	public  int getVersionCode(Context context) {
		PackageManager packageManager =context. getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return packInfo.versionCode;
	}

	/**
	 * ����apk
	 * @return
	 */
//    public boolean downLoadApk(){
//    	boolean flag=false;
//    	MySFTP mysftp = new MySFTP(context);
//		ChannelSftp connect = mysftp.connect();
//		if(connect==null){
//			return flag;
//		}else {
//			File saveFile=new File(Environment.getExternalStorageDirectory()+"/downLoad");
//			if(!saveFile.exists()){
//				saveFile.mkdirs();
//			}
//			File file=new File(saveFile.getAbsolutePath()+"/boilerandroid.apk");
//			if(file.exists()){
//				file.delete();
//			}
//			boolean download = mysftp.download("/APP/", "FXManagerMobile.apk", file.getAbsolutePath(), connect);
//			if(download){
//				flag=true;
//			}
//		}
//		return flag;
//
//    }
	/**
	 */
	public void installApk(String APK_NAME){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "/downLoad/"+APK_NAME)),
				"application/vnd.android.package-archive");
		context. startActivity(intent);

	}
	/**
	 * ж��Apk
	 */
	public void uninstallApk(){
		Uri packageURI = Uri.parse("com.hsic.qp.sz");
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}

}
