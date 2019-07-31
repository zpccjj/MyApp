package com.hsic.qp.szjc;

import java.util.List;

import util.ActivityUtils;
import util.MD5Utils;
import util.ToastUtil;
import hsic.ui.ConfirmDialog;
import hsic.ui.HsicActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import bean.NewGZWFQPCheck;
import bll.sqlite.NewGZWFQPCheck_DB;

import com.hsic.qp.szjc.ActivityRfid.RfidTask;
import com.hsic.qp.szjc.listener.WsListener;
import com.hsic.qp.szjc.task.SetPswTask;
import com.hsic.qp.szjc.task.UploadTask;

public class ActivityMenu extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回登录";
	static class mView{
		TextView menu_txt_1;
		TextView menu_txt_2;
		Button menu_1;
		Button menu_2;
		Button menu_3;
		Button menu_4;
		Button menu_5;
		Button menu_6;
	}

	mView mV;

	NewGZWFQPCheck_DB db;

	private Context getContext(){
		return ActivityMenu.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		initViews();
		setListener();

		db = new NewGZWFQPCheck_DB(getContext());
	}

	public void onBackPressed() {
		finish();
	}

	private void initViews(){
		mV = new mView();
		mV.menu_txt_1 = (TextView) findViewById(R.id.menu_txt_1);
		mV.menu_txt_2 = (TextView) findViewById(R.id.menu_txt_2);
		mV.menu_1 = (Button) findViewById(R.id.menu_1);
		mV.menu_2 = (Button) findViewById(R.id.menu_2);
		mV.menu_3 = (Button) findViewById(R.id.menu_3);
		mV.menu_4 = (Button) findViewById(R.id.menu_4);
		mV.menu_5 = (Button) findViewById(R.id.menu_5);
		mV.menu_6 = (Button) findViewById(R.id.menu_6);

		mV.menu_txt_1.setText(getResources().getString(R.string.txt_string_03)+
				(getApp().getLoginer().getUserName()!=null ? getApp().getLoginer().getUserName() : ""));
		mV.menu_txt_2.setText(getResources().getString(R.string.txt_string_04)+
				(getApp().getLoginer().getDepartName()!=null ? getApp().getLoginer().getDepartName() : ""));
	}

	private void setListener(){
		mV.menu_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityUtils.JumpToCheck1(getContext());
			}
		});
		mV.menu_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(db.isExist("0")){
					ConfirmDialog dialog = new ConfirmDialog(getContext());
					dialog.setTitle("上传");
					dialog.setMessage("存在未上传的检验记录，是否上传？");
					dialog.setConfirmButtonWithTxt("上传",new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new UploadTask(getContext(), ActivityMenu.this, false).execute();
						}
					});
//			        dialog.setNeutralButtonWithTxt("强制上传",new android.content.DialogInterface.OnClickListener(){
//
//			            @Override
//			            public void onClick(DialogInterface dialog, int which) {
//			                // TODO Auto-generated method stub
//			            	new UploadTask(getContext(), ActivityMenu.this, true).execute();
//			            }
//			        });
					dialog.show();
				}else{
					ToastUtil.showToast(getContext(), "无未上传记录");
				}


			}
		});
		mV.menu_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(db.isExist()){
//					int all = db.getUploadCheck(true).size();
//					int num = db.getUploadCheck(false).size();
//					ToastUtil.showToast(getContext(), "总记录数: "+ all + "， 未上传数："+num);
					ActivityUtils.JumpToStatistics(getContext());
				}else{
					ToastUtil.showToast(getContext(), "无检验记录");
				}
			}
		});
		mV.menu_4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<NewGZWFQPCheck> list = db.getUploadCheck(false);
				if(list!=null && list.size()>0){
					//delete
					ConfirmDialog dialog = new ConfirmDialog(getContext());
					dialog.setTitle("清除提示");
					dialog.setMessage("存在未上传的检验记录，确定删清除记录？");
					dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							ToastUtil.showToast(getContext(), "清除记录" + (db.deleteAllCheck(null) ? "成功" : "失败"));
						}
					});
					dialog.show();
				}else{
					if(db.isExist())
						ToastUtil.showToast(getContext(), "清除记录" + (db.deleteAllCheck("1") ? "成功" : "失败"));
					else ToastUtil.showToast(getContext(), "无检验记录");
				}
			}
		});
		mV.menu_5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityUtils.JumpToRead(getContext());
			}
		});
		mV.menu_6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setPsw();
			}
		});
	}

	private AlertDialog PswAlertDialog;
	private void setPsw(){
		AlertDialog.Builder builder = new Builder(getContext());
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

		final View modifyView = inflater.inflate(R.layout.diag_setpsw,null);
		final EditText psw_1 = (EditText) modifyView.findViewById(R.id.psw_1);
		final EditText psw_2 = (EditText) modifyView.findViewById(R.id.psw_2);
		final EditText psw_3 = (EditText) modifyView.findViewById(R.id.psw_3);
		final Button yes = (Button) modifyView.findViewById(R.id.psw_yes);
		final Button no = (Button) modifyView.findViewById(R.id.psw_no);

		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PswAlertDialog.dismiss();
			}
		});

		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(psw_1.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入原密码");
					return ;
				}
				if(psw_2.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入新密码");
					return ;
				}
				if(psw_3.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入确认密码");
					return ;
				}
				Log.e(MD5Utils.MD5(psw_1.getText().toString().trim()), getApp().getLoginer().getUserPWD());
				if(!MD5Utils.MD5(psw_1.getText().toString().trim()).equals(getApp().getLoginer().getUserPWD())){
					ToastUtil.showToast(getContext(), "原密码错误");
					return ;
				}
				if(!MD5Utils.MD5(psw_2.getText().toString().trim()).equals(MD5Utils.MD5(psw_3.getText().toString().trim()))){
					ToastUtil.showToast(getContext(), "新密码与确认密码不相同");
					return ;
				}

				new SetPswTask(getContext(), ActivityMenu.this).execute(getApp().getLoginer().getUserLoginID(), psw_2.getText().toString().trim());
			}
		});


		builder.setView(modifyView);
		PswAlertDialog = builder.create();
		PswAlertDialog.setTitle("修改密码");
		PswAlertDialog.setCancelable(false);
		PswAlertDialog.show();
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			if(code==0){
				getApp().getLoginer().setUserPWD(MD5Utils.MD5(retData));
				PswAlertDialog.dismiss();
				ToastUtil.showToast(getContext(), "密码修改成功");
				SharedPreferences settings = this.getSharedPreferences("szjc_user", 0);
				boolean issave = settings.getBoolean("issave", false);
				if(issave){
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("psw", retData);
					editor.commit();
				}
			}else {
				if(code==1)
					ToastUtil.showToast(getContext(), "上传检验记录成功");
			}
		}
	}

}
