package com.hsic.gps;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import android.content.Context;

public class GPSHelper {

	private LocationClient mLocationClient = null;
	private GpsInfo gpsInfo;
	private MyLocationListenner myListener ;
	private static String TAG = "GPSHelper";

	private Context mContext;

	public GpsInfo getGpsInfo(){
		return gpsInfo;
	}

	public GPSHelper(Context context) {
		mContext = context;
		mLocationClient = new LocationClient(mContext);
		myListener = new MyLocationListenner();
		mLocationClient.registerLocationListener(myListener);

		gpsInfo = new GpsInfo();

		initLocation();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPriority(LocationClientOption.GpsFirst);//NetWorkFirst

		option.setCoorType("bd09ll");
		//可选，默认gcj02，设置返回的定位结果坐标系

		option.setScanSpan(5000);
		//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的


		option.setOpenGps(true);
		//可选，默认false,设置是否使用gps

		option.setPoiNumber(10);
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.disableCache(true);

		mLocationClient.setLocOption(option);
	}

	public void GPSStart() {
		// Log.d(TAG, "Start");
		if(mLocationClient!=null)
			mLocationClient.start();
	}

	public void GPSStop() {
		// Log.d(TAG, "Stop");
		if (mLocationClient!=null || mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}

	class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			gpsInfo.setLONGITUDE(location.getLongitude());
			gpsInfo.setLATITUDE(location.getLatitude());
			gpsInfo.setADDRESS(location.getAddrStr());
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			gpsInfo.setLONGITUDE(poiLocation.getLongitude());
			gpsInfo.setLATITUDE(poiLocation.getLatitude());
			gpsInfo.setADDRESS(poiLocation.getAddrStr());
		}
	}


}
