package bean;

public class NewLHJWFQPCheck {
	//铝合金无缝气瓶检验信息类 3
	private String State="0";//0未上传 1上传成功 2上传失败
	//气瓶基本信息
	private String QPDJCODE;//气瓶使用登记代码(EPC)
	private String KEYGPNO;//气瓶唯一标识码-不使用
	private String GPNO;//气瓶编号/集格编号 (USER)
	private String YHNO;//企业自编号-不使用
	private String MadeNo;//气瓶制造单位代码-不使用
	private String MadeDate;//气瓶制造日期 (USER)
	private String CZDW;//气瓶充装单位代码 (同产权单位EPC)
	private String QPGG;//气瓶类型编号
	private String QPGGName;//气瓶类型名中文描述
	private String BottleKindCode;//气瓶类型代码
	private String BoroughCode;//行政区划代码
	private String PropertyUnitCode;//产权单位代码(EPC)
	private String IssYear;//使用登记入编年份 (19000001) =19
	private String UseRegCode;//气瓶使用登记代码序列号 =000001
	private String MediumCode;//气瓶充装气体介质代码 (EPC)
	private String TagID;//电子标签识别符 (EPC)
	private String SenBaoDate;//气瓶使用代码申报时间
	private String BanFaDate;//气瓶使用登记证颁发时间
	private String SHFLAG;//申报状态
	private String QPSYDJZBM;//气瓶使用登记证编码
	private String JGType;//集格类型编号
	private String LABELType;//数据库记录对应电子标签类型标识
	private String JG;//气瓶所属集格编号
	//检验基本信息
	private String CheckTime;//气瓶检验时间 yyyy-MM-dd HH:mm:ss
	private String JCRQ;//气瓶检验日期 yyyy-MM-dd
	private String XCJCRQ;//气瓶下次检验日期 yyyy-MM-dd
	private String C_ZL;//检验结果(0:合格;1:报废)
	private String JCX0;//外观检查结果(1:合格;0:报废)
	private String JCX1;//硬度检查结果(1:合格;0:报废)
	private String JCX2;//瓶口螺纹检查结果(1:合格;0:报废)
	private String JCX3;//内部检查结果(1:合格;0:报废)
	private String JCX4;//重量与容积测定结果(1:合格;0:报废)
	private String JCX5;//水压试验结果(1:合格;0:报废)
	private String JCX6;//气密性试验结果(1:合格;0:报废)
	private String BFYY;//气瓶报废原因描述
	private String BFYYDM;//气瓶报废原因标记
	//检验检测数据
	private String KSHSSYBH;//0磕伤划伤凹坑处剩余壁厚
	private String SJBH;//1设计壁厚
	private String QXXMSYBH;//2缺陷修磨后剩余壁厚
	private String SYBH;//3剩余壁厚
	private String AXSD;//4凹陷深度
	private String AXDJ;//5凹陷短径
	private String QXSD;//6缺陷深度
	private String FSCSYBH;//7腐蚀处剩余壁厚
	private String JIANJU;//8底座支撑面与瓶底最低点之间距离
	private String TTJMWJC;//9筒体截面最大与最小外径差
	private String TTJMPJWJ;//10筒体截面平均外径
	private String TTZXD;//11筒体直线度
	private String PTZXCD;//12瓶体直线长度
	private String YQKS;//13牙缺口数
	private String QKCD;//14缺口长度
	private String YZ;//15圆周
	private String QKSD;//16缺口深度
	private String YG;//17牙高
	private String SCZL;//18实测重量
	private String GYBJZL;//19钢印标记重量
	private String MINBH;//20最小壁厚
	private String SCRJ;//21实测容积
	private String GCRJ;//22公称容积
	private String GYBJRJ;//23钢印标记容积
	//检验操作人员
	private String C_WGJC;//外观检验员
	private String C_WGSH;//外观审核员
	private String C_YDJC;//硬度检验员
	private String C_YDSH;//硬度审核员
	private String C_PKLWJC;//瓶口螺纹检验员
	private String C_PKLWSH;//瓶口螺纹审核员
	private String C_NBJC;//内部检验员
	private String C_NBSH;//内部审核员
	private String C_RJJC;//重量与容积检验员
	private String C_RJSH;//重量与容积审核员
	private String C_SYJC;//水压检验员
	private String C_SYSH;//水压审核员
	private String C_QMJC;//气密检验员
	private String C_QMSH;//气密审核员
	//操作信息
	private String PosID;//设备编号
	private String LogTime;//上传时间;

	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getQPDJCODE() {
		return QPDJCODE;
	}
	public void setQPDJCODE(String qPDJCODE) {
		QPDJCODE = qPDJCODE;
	}
	public String getKEYGPNO() {
		return KEYGPNO;
	}
	public void setKEYGPNO(String kEYGPNO) {
		KEYGPNO = kEYGPNO;
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
	public String getMadeNo() {
		return MadeNo;
	}
	public void setMadeNo(String madeNo) {
		MadeNo = madeNo;
	}
	public String getMadeDate() {
		return MadeDate;
	}
	public void setMadeDate(String madeDate) {
		MadeDate = madeDate;
	}
	public String getCZDW() {
		return CZDW;
	}
	public void setCZDW(String cZDW) {
		CZDW = cZDW;
	}
	public String getQPGG() {
		return QPGG;
	}
	public void setQPGG(String qPGG) {
		QPGG = qPGG;
	}
	public String getQPGGName() {
		return QPGGName;
	}
	public void setQPGGName(String qPGGName) {
		QPGGName = qPGGName;
	}
	public String getBottleKindCode() {
		return BottleKindCode;
	}
	public void setBottleKindCode(String bottleKindCode) {
		BottleKindCode = bottleKindCode;
	}
	public String getBoroughCode() {
		return BoroughCode;
	}
	public void setBoroughCode(String boroughCode) {
		BoroughCode = boroughCode;
	}
	public String getPropertyUnitCode() {
		return PropertyUnitCode;
	}
	public void setPropertyUnitCode(String propertyUnitCode) {
		PropertyUnitCode = propertyUnitCode;
	}
	public String getIssYear() {
		return IssYear;
	}
	public void setIssYear(String issYear) {
		IssYear = issYear;
	}
	public String getUseRegCode() {
		return UseRegCode;
	}
	public void setUseRegCode(String useRegCode) {
		UseRegCode = useRegCode;
	}
	public String getMediumCode() {
		return MediumCode;
	}
	public void setMediumCode(String mediumCode) {
		MediumCode = mediumCode;
	}
	public String getTagID() {
		return TagID;
	}
	public void setTagID(String tagID) {
		TagID = tagID;
	}
	public String getSenBaoDate() {
		return SenBaoDate;
	}
	public void setSenBaoDate(String senBaoDate) {
		SenBaoDate = senBaoDate;
	}
	public String getBanFaDate() {
		return BanFaDate;
	}
	public void setBanFaDate(String banFaDate) {
		BanFaDate = banFaDate;
	}
	public String getSHFLAG() {
		return SHFLAG;
	}
	public void setSHFLAG(String sHFLAG) {
		SHFLAG = sHFLAG;
	}
	public String getQPSYDJZBM() {
		return QPSYDJZBM;
	}
	public void setQPSYDJZBM(String qPSYDJZBM) {
		QPSYDJZBM = qPSYDJZBM;
	}
	public String getJGType() {
		return JGType;
	}
	public void setJGType(String jGType) {
		JGType = jGType;
	}
	public String getLABELType() {
		return LABELType;
	}
	public void setLABELType(String lABELType) {
		LABELType = lABELType;
	}
	public String getJG() {
		return JG;
	}
	public void setJG(String jG) {
		JG = jG;
	}
	public String getCheckTime() {
		return CheckTime;
	}
	public void setCheckTime(String checkTime) {
		CheckTime = checkTime;
	}
	public String getJCRQ() {
		return JCRQ;
	}
	public void setJCRQ(String jCRQ) {
		JCRQ = jCRQ;
	}
	public String getXCJCRQ() {
		return XCJCRQ;
	}
	public void setXCJCRQ(String xCJCRQ) {
		XCJCRQ = xCJCRQ;
	}
	public String getC_ZL() {
		return C_ZL;
	}
	public void setC_ZL(String c_ZL) {
		C_ZL = c_ZL;
	}
	public String getJCX0() {
		return JCX0;
	}
	public void setJCX0(String jCX0) {
		JCX0 = jCX0;
	}
	public String getJCX1() {
		return JCX1;
	}
	public void setJCX1(String jCX1) {
		JCX1 = jCX1;
	}
	public String getJCX2() {
		return JCX2;
	}
	public void setJCX2(String jCX2) {
		JCX2 = jCX2;
	}
	public String getJCX3() {
		return JCX3;
	}
	public void setJCX3(String jCX3) {
		JCX3 = jCX3;
	}
	public String getJCX4() {
		return JCX4;
	}
	public void setJCX4(String jCX4) {
		JCX4 = jCX4;
	}
	public String getJCX5() {
		return JCX5;
	}
	public void setJCX5(String jCX5) {
		JCX5 = jCX5;
	}
	public String getJCX6() {
		return JCX6;
	}
	public void setJCX6(String jCX6) {
		JCX6 = jCX6;
	}
	public String getBFYY() {
		return BFYY;
	}
	public void setBFYY(String bFYY) {
		BFYY = bFYY;
	}
	public String getBFYYDM() {
		return BFYYDM;
	}
	public void setBFYYDM(String bFYYDM) {
		BFYYDM = bFYYDM;
	}
	public String getKSHSSYBH() {
		return KSHSSYBH;
	}
	public void setKSHSSYBH(String kSHSSYBH) {
		KSHSSYBH = kSHSSYBH;
	}
	public String getSJBH() {
		return SJBH;
	}
	public void setSJBH(String sJBH) {
		SJBH = sJBH;
	}
	public String getQXXMSYBH() {
		return QXXMSYBH;
	}
	public void setQXXMSYBH(String qXXMSYBH) {
		QXXMSYBH = qXXMSYBH;
	}
	public String getSYBH() {
		return SYBH;
	}
	public void setSYBH(String sYBH) {
		SYBH = sYBH;
	}
	public String getAXSD() {
		return AXSD;
	}
	public void setAXSD(String aXSD) {
		AXSD = aXSD;
	}
	public String getAXDJ() {
		return AXDJ;
	}
	public void setAXDJ(String aXDJ) {
		AXDJ = aXDJ;
	}
	public String getQXSD() {
		return QXSD;
	}
	public void setQXSD(String qXSD) {
		QXSD = qXSD;
	}
	public String getFSCSYBH() {
		return FSCSYBH;
	}
	public void setFSCSYBH(String fSCSYBH) {
		FSCSYBH = fSCSYBH;
	}
	public String getJIANJU() {
		return JIANJU;
	}
	public void setJIANJU(String jIANJU) {
		JIANJU = jIANJU;
	}
	public String getTTJMWJC() {
		return TTJMWJC;
	}
	public void setTTJMWJC(String tTJMWJC) {
		TTJMWJC = tTJMWJC;
	}
	public String getTTJMPJWJ() {
		return TTJMPJWJ;
	}
	public void setTTJMPJWJ(String tTJMPJWJ) {
		TTJMPJWJ = tTJMPJWJ;
	}
	public String getTTZXD() {
		return TTZXD;
	}
	public void setTTZXD(String tTZXD) {
		TTZXD = tTZXD;
	}
	public String getPTZXCD() {
		return PTZXCD;
	}
	public void setPTZXCD(String pTZXCD) {
		PTZXCD = pTZXCD;
	}
	public String getYQKS() {
		return YQKS;
	}
	public void setYQKS(String yQKS) {
		YQKS = yQKS;
	}
	public String getQKCD() {
		return QKCD;
	}
	public void setQKCD(String qKCD) {
		QKCD = qKCD;
	}
	public String getYZ() {
		return YZ;
	}
	public void setYZ(String yZ) {
		YZ = yZ;
	}
	public String getQKSD() {
		return QKSD;
	}
	public void setQKSD(String qKSD) {
		QKSD = qKSD;
	}
	public String getYG() {
		return YG;
	}
	public void setYG(String yG) {
		YG = yG;
	}
	public String getSCZL() {
		return SCZL;
	}
	public void setSCZL(String sCZL) {
		SCZL = sCZL;
	}
	public String getGYBJZL() {
		return GYBJZL;
	}
	public void setGYBJZL(String gYBJZL) {
		GYBJZL = gYBJZL;
	}
	public String getMINBH() {
		return MINBH;
	}
	public void setMINBH(String mINBH) {
		MINBH = mINBH;
	}
	public String getSCRJ() {
		return SCRJ;
	}
	public void setSCRJ(String sCRJ) {
		SCRJ = sCRJ;
	}
	public String getGCRJ() {
		return GCRJ;
	}
	public void setGCRJ(String gCRJ) {
		GCRJ = gCRJ;
	}
	public String getGYBJRJ() {
		return GYBJRJ;
	}
	public void setGYBJRJ(String gYBJRJ) {
		GYBJRJ = gYBJRJ;
	}
	public String getC_WGJC() {
		return C_WGJC;
	}
	public void setC_WGJC(String c_WGJC) {
		C_WGJC = c_WGJC;
	}
	public String getC_WGSH() {
		return C_WGSH;
	}
	public void setC_WGSH(String c_WGSH) {
		C_WGSH = c_WGSH;
	}
	public String getC_YDJC() {
		return C_YDJC;
	}
	public void setC_YDJC(String c_YDJC) {
		C_YDJC = c_YDJC;
	}
	public String getC_YDSH() {
		return C_YDSH;
	}
	public void setC_YDSH(String c_YDSH) {
		C_YDSH = c_YDSH;
	}
	public String getC_PKLWJC() {
		return C_PKLWJC;
	}
	public void setC_PKLWJC(String c_PKLWJC) {
		C_PKLWJC = c_PKLWJC;
	}
	public String getC_PKLWSH() {
		return C_PKLWSH;
	}
	public void setC_PKLWSH(String c_PKLWSH) {
		C_PKLWSH = c_PKLWSH;
	}
	public String getC_NBJC() {
		return C_NBJC;
	}
	public void setC_NBJC(String c_NBJC) {
		C_NBJC = c_NBJC;
	}
	public String getC_NBSH() {
		return C_NBSH;
	}
	public void setC_NBSH(String c_NBSH) {
		C_NBSH = c_NBSH;
	}
	public String getC_RJJC() {
		return C_RJJC;
	}
	public void setC_RJJC(String c_RJJC) {
		C_RJJC = c_RJJC;
	}
	public String getC_RJSH() {
		return C_RJSH;
	}
	public void setC_RJSH(String c_RJSH) {
		C_RJSH = c_RJSH;
	}
	public String getC_SYJC() {
		return C_SYJC;
	}
	public void setC_SYJC(String c_SYJC) {
		C_SYJC = c_SYJC;
	}
	public String getC_SYSH() {
		return C_SYSH;
	}
	public void setC_SYSH(String c_SYSH) {
		C_SYSH = c_SYSH;
	}
	public String getC_QMJC() {
		return C_QMJC;
	}
	public void setC_QMJC(String c_QMJC) {
		C_QMJC = c_QMJC;
	}
	public String getC_QMSH() {
		return C_QMSH;
	}
	public void setC_QMSH(String c_QMSH) {
		C_QMSH = c_QMSH;
	}
	public String getPosID() {
		return PosID;
	}
	public void setPosID(String posID) {
		PosID = posID;
	}
	public String getLogTime() {
		return LogTime;
	}
	public void setLogTime(String logTime) {
		LogTime = logTime;
	}
}
