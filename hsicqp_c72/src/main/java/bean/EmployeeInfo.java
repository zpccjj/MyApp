package bean;

import java.util.ArrayList;
import java.util.List;

public class EmployeeInfo {
	private String UserID;//账号
	private String Password;//密码 MD5加密
	private String PrePassword;
	private String UserName;//用户姓名
	private String UserCardID;//员工卡号 暂无用，可为空
	private String Telphone;//联系方式
	private String Station;//所属站点号
	private String StationName;//
	private String UserType;//员工类型 0：配送员工，1：门售员工，2：站点管理员，9：超级管理员
	private List<QPGoods> GoodsList = new ArrayList<QPGoods>();
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getPrePassword() {
		return PrePassword;
	}
	public void setPrePassword(String prePassword) {
		PrePassword = prePassword;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserCardID() {
		return UserCardID;
	}
	public void setUserCardID(String userCardID) {
		UserCardID = userCardID;
	}
	public String getTelphone() {
		return Telphone;
	}
	public void setTelphone(String telphone) {
		Telphone = telphone;
	}
	public String getStation() {
		return Station;
	}
	public void setStation(String station) {
		Station = station;
	}
	public String getStationName() {
		return StationName;
	}
	public void setStationName(String stationName) {
		StationName = stationName;
	}
	public String getUserType() {
		return UserType;
	}
	public void setUserType(String userType) {
		UserType = userType;
	}
	public List<QPGoods> getGoodsList() {
		return GoodsList;
	}
	public void setGoodsList(List<QPGoods> goodsList) {
		GoodsList = goodsList;
	}


}
