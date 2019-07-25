package bean;

public class TruckGoods {
    private String GoodsCode ;//商品代码
    private String GoodsName ;
    private String MediumCode ;//介质代码
    private String CZJZ;

    private int GoodsNum = 0;//出厂满瓶数/回收满瓶数手输
    private int EmptyNum = 0;//回收空瓶数

    private int Num=1;//集格瓶每组数量，默认1，散瓶1 集格>1
    private int IsJG=0;//气瓶类型 0散瓶 1集格

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
    public int getGoodsNum() {
        return GoodsNum;
    }
    public void setGoodsNum(int goodsNum) {
        GoodsNum = goodsNum;
    }
    public int getEmptyNum() {
        return EmptyNum;
    }
    public void setEmptyNum(int emptyNum) {
        EmptyNum = emptyNum;
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



}
