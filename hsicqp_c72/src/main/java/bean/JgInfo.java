package bean;

import java.util.List;

public class JgInfo {

	private int JgNum;//集格内瓶数

	private String StandNo;//气瓶类型
	private String GPNO;//气瓶编号
	private String YHNO;//企业自编号
	private String MadeName;//制造单位名称
	private String MakeDate;//制造日期
	private String MediumCode;//介质代码
	private String CZDW;//充装单位，同产权单位
	private String CQDW;//产权单位
	private String JCDW;//检测单位
	private String JCZQ;//检测周期
	private String SYQX;//使用期限
	private String JCCS;//检测次数
	private String CheckDate;//
	private String NextCheckDate;//下次检验日期
	private String MM;//壁厚
	private String L;//容积
	private String WaterMpa;//水压试验压力
	private String WorkMpa;//公称工作压力
	private String Kg;//实际重量
	private String QPPZ;//皮重
	private String TLKXL;//填料孔隙率
	private String BTSL;//瓶体材料
	private String OPID;//操作人

	private List<SubmitQP> QpList;

	public int getJgNum() {
		return JgNum;
	}

	public void setJgNum(int jgNum) {
		JgNum = jgNum;
	}

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

	public String getYHNO() {
		return YHNO;
	}

	public void setYHNO(String yHNO) {
		YHNO = yHNO;
	}

	public String getMadeName() {
		return MadeName;
	}

	public void setMadeName(String madeName) {
		MadeName = madeName;
	}

	public String getMakeDate() {
		return MakeDate;
	}

	public void setMakeDate(String makeDate) {
		MakeDate = makeDate;
	}

	public String getMediumCode() {
		return MediumCode;
	}

	public void setMediumCode(String mediumCode) {
		MediumCode = mediumCode;
	}

	public String getCZDW() {
		return CZDW;
	}

	public void setCZDW(String cZDW) {
		CZDW = cZDW;
	}

	public String getCQDW() {
		return CQDW;
	}

	public void setCQDW(String cQDW) {
		CQDW = cQDW;
	}

	public String getJCDW() {
		return JCDW;
	}

	public void setJCDW(String jCDW) {
		JCDW = jCDW;
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

	public String getJCCS() {
		return JCCS;
	}

	public void setJCCS(String jCCS) {
		JCCS = jCCS;
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

	public String getMM() {
		return MM;
	}

	public void setMM(String mM) {
		MM = mM;
	}

	public String getL() {
		return L;
	}

	public void setL(String l) {
		L = l;
	}

	public String getWaterMpa() {
		return WaterMpa;
	}

	public void setWaterMpa(String waterMpa) {
		WaterMpa = waterMpa;
	}

	public String getWorkMpa() {
		return WorkMpa;
	}

	public void setWorkMpa(String workMpa) {
		WorkMpa = workMpa;
	}

	public String getKg() {
		return Kg;
	}

	public void setKg(String kg) {
		Kg = kg;
	}

	public String getQPPZ() {
		return QPPZ;
	}

	public void setQPPZ(String qPPZ) {
		QPPZ = qPPZ;
	}

	public String getTLKXL() {
		return TLKXL;
	}

	public void setTLKXL(String tLKXL) {
		TLKXL = tLKXL;
	}

	public String getBTSL() {
		return BTSL;
	}

	public void setBTSL(String bTSL) {
		BTSL = bTSL;
	}

	public String getOPID() {
		return OPID;
	}

	public void setOPID(String oPID) {
		OPID = oPID;
	}

	public List<SubmitQP> getQpList() {
		return QpList;
	}

	public void setQpList(List<SubmitQP> qpList) {
		QpList = qpList;
	}


}
