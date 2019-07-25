package bean;

import java.util.List;

public class TruckNoInfo {
	private int TruckNoID;// 车次号
	private String TruckID;//车辆序号
	private String License;//车牌号
	private String DriverID;//驾驶员
	private String EscortID;//押运员
	private String DriverName;//
	private String EscortName;//
	private int iStatus ;//状态标识位 0：未发车，1：已发车，2：已结束，3：作废，4强制结束；
	private String FullQPNo;//车上满瓶瓶号
	private String OPID;//操作人
	private int CustomerNum ;//客户数量
	private String Remark;//备注
	private String ETD;//下次发车信息

	private List<Sale> Sale;//销售详细信息表

	@Override
	public String toString(){
		return License;
	}

	public int getTruckNoID() {
		return TruckNoID;
	}

	public void setTruckNoID(int truckNoID) {
		TruckNoID = truckNoID;
	}

	public String getTruckID() {
		return TruckID;
	}

	public void setTruckID(String truckID) {
		TruckID = truckID;
	}

	public String getLicense() {
		return License;
	}

	public void setLicense(String license) {
		License = license;
	}

	public String getDriverID() {
		return DriverID;
	}

	public void setDriverID(String driverID) {
		DriverID = driverID;
	}

	public String getEscortID() {
		return EscortID;
	}

	public void setEscortID(String escortID) {
		EscortID = escortID;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	public int getiStatus() {
		return iStatus;
	}

	public void setiStatus(int iStatus) {
		this.iStatus = iStatus;
	}

	public String getFullQPNo() {
		return FullQPNo;
	}

	public void setFullQPNo(String fullQPNo) {
		FullQPNo = fullQPNo;
	}

	public String getOPID() {
		return OPID;
	}

	public void setOPID(String oPID) {
		OPID = oPID;
	}

	public int getCustomerNum() {
		return CustomerNum;
	}

	public void setCustomerNum(int customerNum) {
		CustomerNum = customerNum;
	}

	public String getEscortName() {
		return EscortName;
	}

	public void setEscortName(String escortName) {
		EscortName = escortName;
	}

	public List<Sale> getSale() {
		return Sale;
	}

	public void setSale(List<Sale> sale) {
		Sale = sale;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getETD() {
		return ETD;
	}

	public void setETD(String eTD) {
		ETD = eTD;
	}


}
