package bean;

public class NewYSPCheck {
	//液化石油气钢瓶检验信息类 0
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
	private String JCX1;//焊接接头检查结果(1:合格;0:报废)
	private String JCX2;//阀座检查结果(1:合格;0:报废)
	private String JCX3;//壁厚测定结果(1:合格;0:报废)
	private String JCX4;//容积测定结果(1:合格;0:报废)
	private String JCX5;//水压试验结果(1:合格;0:报废)
	private String JCX6;//气密性试验结果(1:合格;0:报废)
	private String BFYY;//气瓶报废原因描述
	private String BFYYDM;//气瓶报废原因标记
	//检验检测数据
	private String KSHSSYBH;//0磕伤划伤凹坑处剩余壁厚
	private String SJBH;//1设计壁厚
	private String AXSD;//2凹陷深度
	private String AXDJ;//3凹陷短径
	private String AXQXSYBH;//4凹陷缺陷处剩余壁厚
	private String QXXMSYBH;//5缺陷修磨后剩余壁厚
	private String FSCSYBH;//6腐蚀处剩余壁厚
	private String JIANJU;//7底座支撑面与瓶底中心的间距
	private String HFXMSYBH;//8焊缝热影响区修磨后剩余壁厚
	private String HFAXSD;//9焊缝热影响区凹陷深度
	private String CFSTJC;//10磁粉渗透检测合格级别
	private String SXJC;//11射线检测合格级别
	private String SYBH;//12剩余壁厚
	private String SCRJ;//13实测容积
	private String GCRJ;//14公称容积
	//检验操作人员
	private String C_WGJC;//外观检验员
	private String C_WGSH;//外观审核员
	private String C_HJJTJC;//焊接接头检验员
	private String C_HJJTSH;//焊接接头审核员
	private String C_FZJC;//阀座检验员
	private String C_FZSH;//阀座审核员
	private String C_BHJC;//壁厚检验员
	private String C_BHSH;//壁厚审核员
	private String C_RJJC;//容积检验员
	private String C_RJSH;//容积审核员
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
	public String getAXQXSYBH() {
		return AXQXSYBH;
	}
	public void setAXQXSYBH(String aXQXSYBH) {
		AXQXSYBH = aXQXSYBH;
	}
	public String getQXXMSYBH() {
		return QXXMSYBH;
	}
	public void setQXXMSYBH(String qXXMSYBH) {
		QXXMSYBH = qXXMSYBH;
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
	public String getHFXMSYBH() {
		return HFXMSYBH;
	}
	public void setHFXMSYBH(String hFXMSYBH) {
		HFXMSYBH = hFXMSYBH;
	}
	public String getHFAXSD() {
		return HFAXSD;
	}
	public void setHFAXSD(String hFAXSD) {
		HFAXSD = hFAXSD;
	}
	public String getCFSTJC() {
		return CFSTJC;
	}
	public void setCFSTJC(String cFSTJC) {
		CFSTJC = cFSTJC;
	}
	public String getSXJC() {
		return SXJC;
	}
	public void setSXJC(String sXJC) {
		SXJC = sXJC;
	}
	public String getSYBH() {
		return SYBH;
	}
	public void setSYBH(String sYBH) {
		SYBH = sYBH;
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
	public String getC_HJJTJC() {
		return C_HJJTJC;
	}
	public void setC_HJJTJC(String c_HJJTJC) {
		C_HJJTJC = c_HJJTJC;
	}
	public String getC_HJJTSH() {
		return C_HJJTSH;
	}
	public void setC_HJJTSH(String c_HJJTSH) {
		C_HJJTSH = c_HJJTSH;
	}
	public String getC_FZJC() {
		return C_FZJC;
	}
	public void setC_FZJC(String c_FZJC) {
		C_FZJC = c_FZJC;
	}
	public String getC_FZSH() {
		return C_FZSH;
	}
	public void setC_FZSH(String c_FZSH) {
		C_FZSH = c_FZSH;
	}
	public String getC_BHJC() {
		return C_BHJC;
	}
	public void setC_BHJC(String c_BHJC) {
		C_BHJC = c_BHJC;
	}
	public String getC_BHSH() {
		return C_BHSH;
	}
	public void setC_BHSH(String c_BHSH) {
		C_BHSH = c_BHSH;
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
