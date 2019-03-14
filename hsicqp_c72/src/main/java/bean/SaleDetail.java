package bean;

import java.math.BigDecimal;

public class SaleDetail {
	private String SaleID ;// 销售单号
	private String GoodsCode ;//商品代码
	private String GoodsName ;
	private BigDecimal GoodsPrice;//计划气瓶价格 --
	private BigDecimal RealGoodsPrice;//实际气瓶价格

	private int PlanSendNum;// 计划卖气瓶数量 <=PlanSendNum

	private int SendNum;//送瓶数量
	private int ReceiveNum;//回收气瓶数量

	private int GoodsType;//1气瓶 2液体 6配送费
	private String MediumCode;//介质代码
	private String CZJZ;//充装介质

	public String getSaleID() {
		return SaleID;
	}

	public void setSaleID(String saleID) {
		SaleID = saleID;
	}

	public String getGoodsCode() {
		return GoodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		GoodsCode = goodsCode;
	}

	public String getGoodsName() {
		return GoodsName;
	}

	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}

	public BigDecimal getGoodsPrice() {
		return GoodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		GoodsPrice = goodsPrice;
	}

	public BigDecimal getRealGoodsPrice() {
		return RealGoodsPrice;
	}

	public void setRealGoodsPrice(BigDecimal realGoodsPrice) {
		RealGoodsPrice = realGoodsPrice;
	}

	public int getPlanSendNum() {
		return PlanSendNum;
	}

	public void setPlanSendNum(int planSendNum) {
		PlanSendNum = planSendNum;
	}

	public int getSendNum() {
		return SendNum;
	}

	public void setSendNum(int sendNum) {
		SendNum = sendNum;
	}

	public int getGoodsType() {
		return GoodsType;
	}

	public void setGoodsType(int goodsType) {
		GoodsType = goodsType;
	}

	public String getMediumCode() {
		return MediumCode;
	}

	public void setMediumCode(String mediumCode) {
		MediumCode = mediumCode;
	}

	public String getCZJZ() {
		return CZJZ;
	}

	public void setCZJZ(String cZJZ) {
		CZJZ = cZJZ;
	}

	public int getReceiveNum() {
		return ReceiveNum;
	}

	public void setReceiveNum(int receiveNum) {
		ReceiveNum = receiveNum;
	}

}
