package bean;

public class Rfid {
	private String EPC;
	private String Version;//0101 标签类别 + 规范版本
	private String CQDW;//产权单位代码 4位
	private String LabelNo;//电子标签号7位
	private String MediumName;
	private String NextCheckDate;//下次检验日期
	private String State;//钢瓶状态 00合格 01 报废 10 停用

	//基本信息
	private String QPDJCode;//气瓶预付码11位  CQDW+LabelNo
	private String DeviceSeq;//设备编号
	private String OPID;//操作员
	private String CZJZCode;//充装介质代码
	//收瓶 / 发瓶
	private String License;//车牌号
	private String ReceiveDate;//收瓶日期
	private String SendDate;//发瓶日期
	//充装
	private String FaultDm;//错误代码
	private String FullDate;//充装日期 yyyy-MM-DD HH:mm:ss
	private int FullTime;//充装持续时间(分钟)
	private String WorkMpa ;//压力(MPa)
	private String Temperature ;//温度(℃)
	private String Weight ;//重量(KG)
	//充前检
	private String QPNO  ;//钢瓶编号
	private String CheckDate   ;//检测日期
	//错误代码 FaultDm



	public String getVersion() {
		return Version;
	}
	public String getEPC() {
		return EPC;
	}
	public void setEPC(String ePC) {
		EPC = ePC;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getCQDW() {
		return CQDW;
	}
	public void setCQDW(String cQDW) {
		CQDW = cQDW;
	}
	public String getLabelNo() {
		return LabelNo;
	}
	public void setLabelNo(String labelNo) {
		LabelNo = labelNo;
	}
	public String getMediumName() {
		return MediumName;
	}
	public void setMediumName(String mediumName) {
		MediumName = mediumName;
	}
	public String getNextCheckDate() {
		return NextCheckDate;
	}
	public void setNextCheckDate(String nextCheckDate) {
		NextCheckDate = nextCheckDate;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getQPDJCode() {
		return QPDJCode;
	}
	public void setQPDJCode(String qPDJCode) {
		QPDJCode = qPDJCode;
	}
	public String getLicense() {
		return License;
	}
	public void setLicense(String license) {
		License = license;
	}
	public String getReceiveDate() {
		return ReceiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		ReceiveDate = receiveDate;
	}
	public String getSendDate() {
		return SendDate;
	}
	public void setSendDate(String sendDate) {
		SendDate = sendDate;
	}
	public String getDeviceSeq() {
		return DeviceSeq;
	}
	public void setDeviceSeq(String deviceSeq) {
		DeviceSeq = deviceSeq;
	}
	public String getOPID() {
		return OPID;
	}
	public void setOPID(String oPID) {
		OPID = oPID;
	}
	public String getCZJZCode() {
		return CZJZCode;
	}
	public void setCZJZCode(String cZJZCode) {
		CZJZCode = cZJZCode;
	}
	public String getFaultDm() {
		return FaultDm;
	}
	public void setFaultDm(String faultDm) {
		FaultDm = faultDm;
	}
	public String getFullDate() {
		return FullDate;
	}
	public void setFullDate(String fullDate) {
		FullDate = fullDate;
	}
	public int getFullTime() {
		return FullTime;
	}
	public void setFullTime(int fullTime) {
		FullTime = fullTime;
	}
	public String getWorkMpa() {
		return WorkMpa;
	}
	public void setWorkMpa(String workMpa) {
		WorkMpa = workMpa;
	}
	public String getTemperature() {
		return Temperature;
	}
	public void setTemperature(String temperature) {
		Temperature = temperature;
	}
	public String getWeight() {
		return Weight;
	}
	public void setWeight(String weight) {
		Weight = weight;
	}
	public String getQPNO() {
		return QPNO;
	}
	public void setQPNO(String qPNO) {
		QPNO = qPNO;
	}
	public String getCheckDate() {
		return CheckDate;
	}
	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}

}
