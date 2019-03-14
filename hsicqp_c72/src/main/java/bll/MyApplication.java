package bll;

import bean.EmployeeInfo;


import android.app.Application;

public class MyApplication extends Application{
	
//	@Override
//    public void onCreate() {
//        super.onCreate();
//
// 
//    }
	
	private EmployeeInfo Login;

	public EmployeeInfo getLogin() {
		return Login;
	}

	public void setLogin(EmployeeInfo login) {
		Login = login;
	}
	
	


}
