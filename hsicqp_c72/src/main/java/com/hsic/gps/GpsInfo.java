package com.hsic.gps;

/**
 * Created by Administrator on 2018/8/20.
 */

public class GpsInfo {
	private double LONGITUDE=0.0;//经度
	private double LATITUDE=0.0;//纬度
	private String ADDRESS="";//地址
	public double getLONGITUDE() {
		return LONGITUDE;
	}
	public void setLONGITUDE(double lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}
	public double getLATITUDE() {
		return LATITUDE;
	}
	public void setLATITUDE(double lATITUDE) {
		LATITUDE = lATITUDE;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}


}
