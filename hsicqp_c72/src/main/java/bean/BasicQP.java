package bean;


public class BasicQP{
    //配置数据
    private String Id;// 钢瓶类型代码
    private String Name;//钢瓶类型
    private String JZId;//介质ID
    private String JZName;//介质名
    private String RJ;//容积
    private String BH;//壁厚
    private String WaterMpa;//水压试验压力
    private String WorkMpa;//公称工作压力
    private String Kg;//实际重量
    private String QPPZ;//皮重
    private String BTSL;//瓶体材料
    private String TLKXL;//填料孔隙率
    private String JCZQ;//检测周期
    private String SYQX ;//使用期限
    //


    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getJZId() {
        return JZId;
    }
    public void setJZId(String jZId) {
        JZId = jZId;
    }
    public String getJZName() {
        return JZName;
    }
    public void setJZName(String jZName) {
        JZName = jZName;
    }
    public String getRJ() {
        return RJ;
    }
    public void setRJ(String rJ) {
        RJ = rJ;
    }
    public String getBH() {
        return BH;
    }
    public void setBH(String bH) {
        BH = bH;
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
    public String getBTSL() {
        return BTSL;
    }
    public void setBTSL(String bTSL) {
        BTSL = bTSL;
    }
    public String getTLKXL() {
        return TLKXL;
    }
    public void setTLKXL(String tLKXL) {
        TLKXL = tLKXL;
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
}
