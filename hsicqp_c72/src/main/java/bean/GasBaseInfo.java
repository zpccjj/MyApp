package bean;

public class GasBaseInfo {//气瓶基本信息
	private String StandNo;
	private String GPNO;//钢瓶号
	private String MakeDate;
	private String CZDW ;//产权单位
	private String JCZQ ;//检测周期
	private String SYQX ;
	private String MediumCode ;
	private String CheckDate ;
	private String NextCheckDate ;
	private String QPDJCode;
	private String TagID;
	private String iStatus;//0表示未绑定，1表示已绑定
	private String MadeNo;
	private String JGType;

	public String getStandNo() {
		return StandNo;
	}
	public void setStandNo(String standNo) {
		StandNo = standNo;
	}
	public String getGPNO() {
		return GPNO;
	}
	public void setGPNO(String gPNO) {
		GPNO = gPNO;
	}
	public String getMakeDate() {
		return MakeDate;
	}
	public void setMakeDate(String makeDate) {
		MakeDate = makeDate;
	}
	public String getCZDW() {
		return CZDW;
	}
	public void setCZDW(String cZDW) {
		CZDW = cZDW;
	}
	public String getJCZQ() {
		return JCZQ;
	}
	public void setJCZQ(String jCZQ) {
		JCZQ = jCZQ;
	}
	public String getSYQX() {
		return SYQX;
	}
	public void setSYQX(String sYQX) {
		SYQX = sYQX;
	}
	public String getMediumCode() {
		return MediumCode;
	}
	public void setMediumCode(String mediumCode) {
		MediumCode = mediumCode;
	}
	public String getCheckDate() {
		return CheckDate;
	}
	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}
	public String getNextCheckDate() {
		return NextCheckDate;
	}
	public void setNextCheckDate(String nextCheckDate) {
		NextCheckDate = nextCheckDate;
	}
	public String getJGType() {
		return JGType;
	}
	public void setJGType(String jGType) {
		JGType = jGType;
	}
	public String getQPDJCode() {
		return QPDJCode;
	}
	public void setQPDJCode(String qPDJCode) {
		QPDJCode = qPDJCode;
	}
	public String getTagID() {
		return TagID;
	}
	public void setTagID(String tagID) {
		TagID = tagID;
	}
	public String getiStatus() {
		return iStatus;
	}
	public void setiStatus(String iStatus) {
		this.iStatus = iStatus;
	}
	public String getMadeNo() {
		return MadeNo;
	}
	public void setMadeNo(String madeNo) {
		MadeNo = madeNo;
	}


}
