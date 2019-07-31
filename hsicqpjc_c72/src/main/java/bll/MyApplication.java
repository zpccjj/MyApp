package bll;


import android.app.Application;

import java.util.List;

import bean.UserInfo;

public class MyApplication extends Application{

//	@Override
//    public void onCreate() {
//        super.onCreate();
//
//
//    }

    private UserInfo Loginer;
    private List<UserInfo> JCList;
    private List<UserInfo> SHList;

    public UserInfo getLoginer() {
        return Loginer;
    }

    public void setLoginer(UserInfo loginer) {
        Loginer = loginer;
    }

    public List<UserInfo> getJCList() {
        return JCList;
    }

    public void setJCList(List<UserInfo> jCList) {
        JCList = jCList;
    }

    public List<UserInfo> getSHList() {
        return SHList;
    }

    public void setSHList(List<UserInfo> sHList) {
        SHList = sHList;
    }

}
