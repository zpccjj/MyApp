package bean;

public class Rfid {
	private String EPC;
	private String Version;//0101 标签类别 + 规范版本
	private String CQDW;//产权单位代码 4位
	private String LabelNo;//电子标签号8位
	private String MediumName;
	private String NextCheckDate;//下次检验日期
	private String LimitDate;//下次检验日期超过使用期限，后记录到期日
	private String State;//钢瓶状态 00合格 01 报废 10 停用
	private String Type;//气瓶种类 00散瓶01集格10集格内瓶
	private int IsJG=0;//气瓶类型 0散瓶 1集格
	//基本信息
	private String QPDJCode;//气瓶预付码11位  CQDW+LabelNo
	private String DeviceSeq;//设备编号
	private String OPID;//操作员
	private String CZJZCode;//充装介质代码

	private String GPNO;
	private String MadeDate;
	private String TagID;

	public String getEPC() {
		return EPC;
	}
	public void setEPC(String ePC) {
		EPC = ePC;
	}
	public String getVersion() {
		return Version;
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
	public String getLimitDate() {
		return LimitDate;
	}
	public void setLimitDate(String limitDate) {
		LimitDate = limitDate;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public int getIsJG() {
		return IsJG;
	}
	public void setIsJG(int isJG) {
		IsJG = isJG;
	}
	public String getQPDJCode() {
		return QPDJCode;
	}
	public void setQPDJCode(String qPDJCode) {
		QPDJCode = qPDJCode;
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
	public String getGPNO() {
		return GPNO;
	}
	public void setGPNO(String gPNO) {
		GPNO = gPNO;
	}
	public String getMadeDate() {
		return MadeDate;
	}
	public void setMadeDate(String madeDate) {
		MadeDate = madeDate;
	}
	public String getTagID() {
		return TagID;
	}
	public void setTagID(String tagID) {
		TagID = tagID;
	}

}
