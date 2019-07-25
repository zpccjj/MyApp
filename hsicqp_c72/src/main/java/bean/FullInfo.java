package bean;

import java.util.List;

public class FullInfo {
    private String FullID;//充装批次号 YYYYMMDD+4位输入数字
    private String GoodsCode ;//商品代码
    private String GoodsName ;
    private String MediumCode ;//介质代码
    private String CZJZ;
    private int FullNum = 0;//充装数量
    //充装
    private String FaultDm;//错误代码
    private String FullDate;//充装日期 yyyy-MM-DD HH:mm:ss
    private int FullTime;//充装持续时间(分钟)
    private String WorkMpa ;//压力(MPa)
    private String Temperature ;//温度(℃)
    private String Weight ;//重量(KG)

    private String DeviceSeq;//设备编号
    private String OPID;//操作员

    //充装扫描
    private List<Rfid> rList;

    public String getFullID() {
        return FullID;
    }

    public void setFullID(String fullID) {
        this.FullID = fullID;
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

    public int getFullNum() {
        return FullNum;
    }

    public void setFullNum(int fullNum) {
        FullNum = fullNum;
    }

    public String getFaultDm() {
        return FaultDm;
    }

    public void setFaultDm(String faultDm) {
        FaultDm = faultDm;
    }

    public String getFullDate() {
        return FullDate;
    }

    public void setFullDate(String fullDate) {
        FullDate = fullDate;
    }

    public int getFullTime() {
        return FullTime;
    }

    public void setFullTime(int fullTime) {
        FullTime = fullTime;
    }

    public String getWorkMpa() {
        return WorkMpa;
    }

    public void setWorkMpa(String workMpa) {
        WorkMpa = workMpa;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public List<Rfid> getrList() {
        return rList;
    }

    public void setrList(List<Rfid> rList) {
        this.rList = rList;
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


}
