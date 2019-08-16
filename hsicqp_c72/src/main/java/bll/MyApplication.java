package bll;

import android.app.Application;
import java.util.List;
import bean.EmployeeInfo;
import bean.MediaGoods;

public class MyApplication extends Application{

//	@Override
//    public void onCreate() {
//        super.onCreate();
//
//
//    }

	private EmployeeInfo Login;

	private List<MediaGoods> MediaInfo;

	public EmployeeInfo getLogin() {
		return Login;
	}

	public void setLogin(EmployeeInfo login) {
		Login = login;
	}

	public List<MediaGoods> getMediaInfo() {
		return MediaInfo;
	}

	public void setMediaInfo(List<MediaGoods> mediaInfo) {
		MediaInfo = mediaInfo;
	}

}
