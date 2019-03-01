package bean;

/*
 * 登陆信息
 *
 *
 */
public class User {
    private String systemtype;//系统类型：0-金库服务；1-上门服务。请求
    private String useroneid;//人员1id。请求：扫描或录入的信息；响应：真实id（后续交易使用）
    private String useronename; //人员1姓名。响应：真实姓名（后续交易使用）
    private String useronepassword; //人员1密码。请求：md5后传输
    private String usertwoid; //人员2id。请求：扫描或录入的信息；响应：真实id（后续交易使用）
    private String usertwoname; //人员2姓名。响应：真实姓名（后续交易使用）
    private String usertwopassword;//人员2密码。请求：md5后传输
    private String deptcode;//所属机构id。请求：设置信息中的机构
    private String deptname;//所属机构名称。
    private String subdeptcode;//所属子机构代码。
    private String linecode;//线路编号。响应：上门服务时返回，作为默认线路编号
    private String linename;//线路名称。响应：上门服务时返回，作为默认线路名称
    public String getSystemtype() {
        return systemtype;
    }
    public void setSystemtype(String systemtype) {
        this.systemtype = systemtype;
    }
    public String getUseroneid() {
        return useroneid;
    }
    public void setUseroneid(String useroneid) {
        this.useroneid = useroneid;
    }
    public String getUseronename() {
        return useronename;
    }
    public void setUseronename(String useronename) {
        this.useronename = useronename;
    }
    public String getUseronepassword() {
        return useronepassword;
    }
    public void setUseronepassword(String useronepassword) {
        this.useronepassword = useronepassword;
    }
    public String getUsertwoid() {
        return usertwoid;
    }
    public void setUsertwoid(String usertwoid) {
        this.usertwoid = usertwoid;
    }
    public String getUsertwoname() {
        return usertwoname;
    }
    public void setUsertwoname(String usertwoname) {
        this.usertwoname = usertwoname;
    }
    public String getUsertwopassword() {
        return usertwopassword;
    }
    public void setUsertwopassword(String usertwopassword) {
        this.usertwopassword = usertwopassword;
    }
    public String getDeptcode() {
        return deptcode;
    }
    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }
    public String getDeptname() {
        return deptname;
    }
    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }
    public String getSubdeptcode() {
        return subdeptcode;
    }
    public void setSubdeptcode(String subdeptcode) {
        this.subdeptcode = subdeptcode;
    }
    public String getLinecode() {
        return linecode;
    }
    public void setLinecode(String linecode) {
        this.linecode = linecode;
    }
    public String getLinename() {
        return linename;
    }
    public void setLinename(String linename) {
        this.linename = linename;
    }
}
