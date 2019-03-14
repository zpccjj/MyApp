package bean;

import java.math.BigDecimal;
import java.util.List;

public class Sale {
	private String SaleID ;//销售单号

	private String CustomerID ;// 所属客户号
	private String CustomerName ;// 所属客户
	private String CustomerTelephone ;// 所属客户电话

	private String Address  ;//

	private List<QPInfo> QPInfo;// 发瓶和收瓶详细信息表
	private List<QPGoods> GoodsList;//收瓶数量明细表

	private String PS   ;//备注--
	private String Match   ;// 是否符合计划 0：符合，1：不符合

	private BigDecimal OtherQPPirce;// 抵扣价格 （收到非本站钢瓶总价）

	private String PayType;// 0：现金，1：气票，2：月结

	private String GPS_J;
	private String GPS_W;

	private String SaleAddress  ;//地址---
	private String Remark ;

	private String Station;//所属站点号
	private String StationName;

	private String CustomerType;//CT01月结客户  CT02个体客户
	private String CustomerTypeName;

	private List<SaleDetail> SaleDetail;


	public String getSaleID() {
		return SaleID;
	}
	public void setSaleID(String saleID) {
		SaleID = saleID;
	}
	public String getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public String getCustomerTelephone() {
		return CustomerTelephone;
	}
	public void setCustomerTelephone(String customerTelephone) {
		CustomerTelephone = customerTelephone;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public List<QPInfo> getQPInfo() {
		return QPInfo;
	}
	public void setQPInfo(List<QPInfo> qPInfo) {
		QPInfo = qPInfo;
	}
	public String getPS() {
		return PS;
	}
	public void setPS(String pS) {
		PS = pS;
	}
	public String getMatch() {
		return Match;
	}
	public void setMatch(String match) {
		Match = match;
	}
	public BigDecimal getOtherQPPirce() {
		return OtherQPPirce;
	}
	public void setOtherQPPirce(BigDecimal otherQPPirce) {
		OtherQPPirce = otherQPPirce;
	}
	public String getPayType() {
		return PayType;
	}
	public void setPayType(String payType) {
		PayType = payType;
	}
	public String getGPS_J() {
		return GPS_J;
	}
	public void setGPS_J(String gPS_J) {
		GPS_J = gPS_J;
	}
	public String getGPS_W() {
		return GPS_W;
	}
	public void setGPS_W(String gPS_W) {
		GPS_W = gPS_W;
	}
	public String getSaleAddress() {
		return SaleAddress;
	}
	public void setSaleAddress(String saleAddress) {
		SaleAddress = saleAddress;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public List<SaleDetail> getSaleDetail() {
		return SaleDetail;
	}
	public void setSaleDetail(List<SaleDetail> saleDetail) {
		SaleDetail = saleDetail;
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
	public String getCustomerType() {
		return CustomerType;
	}
	public void setCustomerType(String customerType) {
		CustomerType = customerType;
	}
	public String getCustomerTypeName() {
		return CustomerTypeName;
	}
	public void setCustomerTypeName(String customerTypeName) {
		CustomerTypeName = customerTypeName;
	}
	public List<QPGoods> getGoodsList() {
		return GoodsList;
	}
	public void setGoodsList(List<QPGoods> goodsList) {
		GoodsList = goodsList;
	}
}
