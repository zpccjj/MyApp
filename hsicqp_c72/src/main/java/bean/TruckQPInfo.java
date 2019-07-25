package bean;

import java.util.ArrayList;
import java.util.List;

public class TruckQPInfo {
    private int TruckNoID;// 车次号
    private String TruckID;//车辆序号
    private String License;//车牌号
    private String DriverID;//驾驶员
    private String EscortID;//押运员
    private int iStatus ;//状态标识位 0：未发车，1：已发车，2：已结束，3：作废，4强制结束；
    private String OPID;//操作人

    private List<TruckGoods> QPGoods = new ArrayList<TruckGoods>();
    private List<Rfid> rfidList = new ArrayList<Rfid>();

    @Override
    public String toString(){
        return License;
    }

    public int getTruckNoID() {
        return TruckNoID;
    }
    public void setTruckNoID(int truckNoID) {
        TruckNoID = truckNoID;
    }
    public String getTruckID() {
        return TruckID;
    }
    public void setTruckID(String truckID) {
        TruckID = truckID;
    }
    public String getLicense() {
        return License;
    }
    public void setLicense(String license) {
        License = license;
    }
    public String getDriverID() {
        return DriverID;
    }
    public void setDriverID(String driverID) {
        DriverID = driverID;
    }
    public String getEscortID() {
        return EscortID;
    }
    public void setEscortID(String escortID) {
        EscortID = escortID;
    }
    public int getiStatus() {
        return iStatus;
    }
    public void setiStatus(int iStatus) {
        this.iStatus = iStatus;
    }
    public List<TruckGoods> getQPGoods() {
        return QPGoods;
    }

    public void setQPGoods(List<TruckGoods> qPGoods) {
        QPGoods = qPGoods;
    }

    public List<Rfid> getRfidList() {
        return rfidList;
    }
    public void setRfidList(List<Rfid> rfidList) {
        this.rfidList = rfidList;
    }

    public String getOPID() {
        return OPID;
    }

    public void setOPID(String oPID) {
        OPID = oPID;
    }


}
