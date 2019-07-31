package bean;

public class UserInfo {
	String UserLoginID;//用户(登录)ID
	String UserName;//用户名称
	String UserPWD;//用户密码 MD5码
	String UserDepart;//用户所属部门代码
	String DepartName;//用户所属部门名称
	String RoleID;//员工类型(角色)代码 J9001:移动端用户,J9002:检验人员,J9003:审核人员
	String RoleName;//员工类型(角色)名称
	public String getUserLoginID() {
		return UserLoginID;
	}
	public void setUserLoginID(String userLoginID) {
		UserLoginID = userLoginID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserPWD() {
		return UserPWD;
	}
	public void setUserPWD(String userPWD) {
		UserPWD = userPWD;
	}
	public String getUserDepart() {
		return UserDepart;
	}
	public void setUserDepart(String userDepart) {
		UserDepart = userDepart;
	}
	public String getDepartName() {
		return DepartName;
	}
	public void setDepartName(String departName) {
		DepartName = departName;
	}
	public String getRoleID() {
		return RoleID;
	}
	public void setRoleID(String roleID) {
		RoleID = roleID;
	}
	public String getRoleName() {
		return RoleName;
	}
	public void setRoleName(String roleName) {
		RoleName = roleName;
	}

	@Override
	public String toString() {
		return UserName;
	}
}
