package bean;

import java.util.List;

public class DownBasicInfo {
	private int UserInfoCount;
	private List<UserInfo> UserInfos;
	public int getUserInfoCount() {
		return UserInfoCount;
	}
	public void setUserInfoCount(int userInfoCount) {
		UserInfoCount = userInfoCount;
	}
	public List<UserInfo> getUserInfos() {
		return UserInfos;
	}
	public void setUserInfos(List<UserInfo> userInfos) {
		UserInfos = userInfos;
	}
	
	
}
