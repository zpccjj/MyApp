package bean;

public class QPGoods {
	private String GoodsCode ;//商品代码
	private String GoodsName ;
	private int GoodsNum = 0;//收瓶数量

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

	@Override
	public String toString(){
		return GoodsName;
	}
}
