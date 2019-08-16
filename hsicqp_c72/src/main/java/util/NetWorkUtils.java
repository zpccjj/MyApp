package util;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import bean.ResponseData;

public class NetWorkUtils {

	/*
	 * 检查网络状态
	 */
	public static ResponseData isNetWork(Context mContext){
		ResponseData msg = new ResponseData();
		msg.setRespCode(1);
		try {
			if (!checkNetworkStatus(mContext)) {
				msg.setRespMsg("未连接网络");
				return msg;
			}else{
				msg.setRespCode(0);
				return msg;
			}
		} catch (NetworkErrorException e1) {
			e1.printStackTrace();
			msg.setRespMsg("网络连接异常");
			return msg;
		}
	}

	private static boolean checkNetworkStatus(Context mContext) throws NetworkErrorException {
		boolean result = false;
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiAvail = ni.isAvailable();
		boolean isWifiConn = ni.isConnected();
		if (isWifiAvail && isWifiConn) {
			result = true;
		} else {
			ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			boolean isMobileAvail = ni.isAvailable();
			boolean isMobileConn = ni.isConnected();
			if (isMobileAvail && isMobileConn) {
				result = true;
			}

		}
		return result;
	}
}
