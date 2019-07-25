package bean;

public class QPGoods {
	private String GoodsCode ;//商品代码
	private String GoodsName ;
	private int GoodsNum = 0;//收瓶数量
	private String MediumCode ;//介质代码
	private String CZJZ;

	private int Num=1;//集格瓶每组数量，默认1，散瓶1 集格>1
	private int IsJG=0;//气瓶类型 0散瓶 1集格

	private int isDelete;//0不可删， 1可删

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
	public int getGoodsNum() {
		return GoodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		GoodsNum = goodsNum;
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
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public int getNum() {
		return Num;
	}
	public void setNum(int num) {
		Num = num;
	}
	public int getIsJG() {
		return IsJG;
	}
	public void setIsJG(int isJG) {
		IsJG = isJG;
	}
	@Override
	public String toString(){
		return GoodsName;
	}
}
