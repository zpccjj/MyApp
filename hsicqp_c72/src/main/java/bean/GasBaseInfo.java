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


}
