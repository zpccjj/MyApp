package com.hsic.qp.szjc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.ActivityUtils;
import util.ToastUtil;

import com.google.gson.reflect.TypeToken;

import bean.ItemCheck;
import bean.NewGZWFQPCheck;
import bean.UserInfo;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import hsic.ui.ConfirmDialog;
import hsic.ui.HsicActivity;

public class ActivityNewGZWFQPCheck extends HsicActivity{
	private final static String MenuHOME = "钢质无缝气瓶检验";
	private final static String PART = "1";
	static class mView{
		TextView check1_1;
		Spinner check1_2;
		RadioGroup check1_3;
		RadioButton check1_3_y;
		RadioButton check1_3_n;
		Spinner check1_4;
		Spinner check1_5;

		Button check1_6_0;
		Button check1_6_1;
		Button check1_6_2;
		Button check1_6_3;
		Button check1_6_4;
		Button check1_6_5;
		Button check1_6_6;

		Button btnRfid;
		Button btnBack;
	}

	mView mV;

	NewGZWFQPCheck mData = new NewGZWFQPCheck();

	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd");
	String JCRQ;
	String[] BFYYDM = {"00000000000000000", "0", "00", "0", "00", "000", "00"};
	String[] BFYY = {"", "", "", "", "", "", ""};
	String DeviceID;
	private Context getContext(){
		return ActivityNewGZWFQPCheck.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check1);

		DeviceID = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("DeviceID", "");

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(MenuHOME);

		initViews();
		setListener();
		initData();
	}

	private void initViews(){
		mV = new mView();
		mV.check1_1 = (TextView) findViewById(R.id.check1_1);
		mV.check1_2 = (Spinner) findViewById(R.id.check1_2);
		mV.check1_3 = (RadioGroup) findViewById(R.id.check1_3);
		mV.check1_3_y = (RadioButton) findViewById(R.id.check1_3_y);
		mV.check1_3_n = (RadioButton) findViewById(R.id.check1_3_n);
		mV.check1_4 = (Spinner) findViewById(R.id.check1_4);
		mV.check1_5 = (Spinner) findViewById(R.id.check1_5);
		mV.check1_6_0 = (Button) findViewById(R.id.check1_6_0);
		mV.check1_6_1 = (Button) findViewById(R.id.check1_6_1);
		mV.check1_6_2 = (Button) findViewById(R.id.check1_6_2);
		mV.check1_6_3 = (Button) findViewById(R.id.check1_6_3);
		mV.check1_6_4 = (Button) findViewById(R.id.check1_6_4);
		mV.check1_6_5 = (Button) findViewById(R.id.check1_6_5);
		mV.check1_6_6 = (Button) findViewById(R.id.check1_6_6);
		mV.btnRfid = (Button) findViewById(R.id.check1_btn1);
		mV.btnBack = (Button) findViewById(R.id.check1_btn2);
	}

	private void initData(){
		JCRQ = sdf2.format(new Date());
		mV.check1_1.setText(JCRQ);

		int year = Integer.valueOf(JCRQ.substring(0, 4));
		String[] next_check = {String.valueOf(year+2), String.valueOf(year+3), String.valueOf(year+5)};
		ArrayAdapter<String> nextAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, next_check);
		nextAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mV.check1_2.setAdapter(nextAdapter);
		mV.check1_2.setSelection(1);
		for (int i = 0; i < getApp().getJCList().size(); i++) {
			Log.e(String.valueOf(getApp().getJCList().get(i).getUserLoginID()), String.valueOf(getApp().getJCList().get(i).getUserName()));
		}
		ArrayAdapter<UserInfo> jcAdapter = new ArrayAdapter<UserInfo>(getContext(),android.R.layout.simple_spinner_item, getApp().getJCList());
		jcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mV.check1_4.setAdapter(jcAdapter);

		ArrayAdapter<UserInfo> shAdapter = new ArrayAdapter<UserInfo>(getContext(),android.R.layout.simple_spinner_item, getApp().getSHList());
		shAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mV.check1_5.setAdapter(shAdapter);

		mV.check1_3_y.setChecked(true);
	}

	private void setListener(){
		mV.check1_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.check1_3_y){
					mV.check1_6_0.setEnabled(false);
					mV.check1_6_1.setEnabled(false);
					mV.check1_6_2.setEnabled(false);
					mV.check1_6_3.setEnabled(false);
					mV.check1_6_4.setEnabled(false);
					mV.check1_6_5.setEnabled(false);
					mV.check1_6_6.setEnabled(false);
				}else{
					mV.check1_6_0.setEnabled(true);
					mV.check1_6_1.setEnabled(true);
					mV.check1_6_2.setEnabled(true);
					mV.check1_6_3.setEnabled(true);
					mV.check1_6_4.setEnabled(true);
					mV.check1_6_5.setEnabled(true);
					mV.check1_6_6.setEnabled(true);
				}
			}
		});

		mV.check1_6_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//外观检查
				List<ItemCheck>mList = new ArrayList<ItemCheck>();
				Log.e("==BFYYDM[0]", BFYYDM[0]);
				ItemCheck ic; int i = 0;
				ic = new ItemCheck("1.1", "1.1 制造企业未取得特种设备制造许可", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.2", "1.2 制造标志模糊不清或项目不全而又无据可查", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.3", "1.3 特种设备安全监督管理部门规定不准再用", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.4", "1.4 超过设计使用年限", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.5", "1.5 瓶体存在裂纹、鼓包、夹层等缺陷及肉眼可见的容积变形", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.6", "1.6 瓶体磕伤、划伤处的剩余壁厚小于设计壁厚", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.7", "1.7 瓶体凹陷深度大于2mm或大于凹陷短径1/30", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.8", "1.8 瓶体磕伤或划伤长度等于或大于凹陷短径，且凹陷深度大于1.5mm或大于凹陷短径的1/35", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.9", "1.9 未达到报废条件的缺陷，修磨后的剩余壁厚小于或等于设计壁厚", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.10", "1.10 瓶体存在弧疤、焊迹或存在可能使金属受损的明显火焰烧灼迹象", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.11", "1.11 瓶体上孤立的点腐蚀、线状腐蚀、局部腐蚀及普遍腐蚀处的剩余壁厚小于设计壁厚", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.12", "1.12 腐蚀严重，无法确定腐蚀深度和范围", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.13", "1.13 颈圈松动无法加固，或颈圈损伤且无法更换", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.14", "1.14 底座松动、倾斜、破裂、磨损或其支撑面与瓶底最低点之间距离小于10mm", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.15", "1.15 筒体同一截面上最大与最小外径之差，超过该截面平均外径的3.0% ", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.16", "1.16 筒体直线度超过瓶体直线段长度的0.4%，且弯曲深度大于5mm", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("1.17", "1.17 瓶体垂直度超过瓶体直线段长度的1%", 0, (BFYYDM[0].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("KSHSSYBH", "磕伤划伤处剩余壁厚", 1, false, mData.getKSHSSYBH()); mList.add(ic);
				ic = new ItemCheck("SJBH", "设计壁厚", 1, false, mData.getSJBH()); mList.add(ic);
				ic = new ItemCheck("AXSD", "凹陷深度", 1, false, mData.getAXSD()); mList.add(ic);
				ic = new ItemCheck("AXDJ", "凹陷短径", 1, false, mData.getAXDJ()); mList.add(ic);
				ic = new ItemCheck("KSHSSD", "磕伤划伤长度", 1, false, mData.getKSHSSD()); mList.add(ic);
				ic = new ItemCheck("QXXMSYBH", "缺陷修磨后剩余壁厚", 1, false, mData.getQXXMSYBH()); mList.add(ic);
				ic = new ItemCheck("FSCSYBH", "腐蚀处剩余壁厚", 1, false, mData.getFSCSYBH()); mList.add(ic);
				ic = new ItemCheck("JIANJU", "底座支撑面与瓶底最低点之间距离", 1, false, mData.getJIANJU()); mList.add(ic);
				ic = new ItemCheck("TTJMWJC", "筒体截面最大与最小外径差", 1, false, mData.getTTJMWJC()); mList.add(ic);
				ic = new ItemCheck("TTJMPJWJ", "筒体截面平均外径", 1, false, mData.getTTJMPJWJ()); mList.add(ic);
				ic = new ItemCheck("TTZXD", "筒体直线度", 1, false, mData.getTTZXD()); mList.add(ic);
				ic = new ItemCheck("PTZXCD", "瓶体直线长度", 1, false, mData.getPTZXCD()); mList.add(ic);
				ic = new ItemCheck("WQSD", "弯曲深度", 1, false, mData.getWQSD()); mList.add(ic);
				ic = new ItemCheck("PTCZD", "瓶体垂直度", 1, false, mData.getPTCZD()); mList.add(ic);

				doCheck(PART+"-0", util.json.JSONUtils.toJsonWithGson(mList), 0);
			}
		});

		mV.check1_6_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//音响检查
				List<ItemCheck>mList = new ArrayList<ItemCheck>();
				ItemCheck ic; int i = 0;
				ic = new ItemCheck("2.1", "2.1 音响十分混浊低沉，余韵重而短，并伴有破壳音响", 0, (BFYYDM[1].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;

				doCheck(PART+"-1", util.json.JSONUtils.toJsonWithGson(mList), 1);
			}

		});

		mV.check1_6_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//瓶口螺纹检查
				List<ItemCheck>mList = new ArrayList<ItemCheck>();
				ItemCheck ic; int i = 0;

				ic = new ItemCheck("3.1", "3.1 瓶口螺纹有裂纹性缺陷", 0, (BFYYDM[2].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("3.2", "3.2 瓶口螺纹的轻度腐蚀、磨损或其他损伤，经修理后检查结果不合格", 0, (BFYYDM[2].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;

				doCheck(PART+"-2", util.json.JSONUtils.toJsonWithGson(mList), 2);
			}

		});

		mV.check1_6_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//内部检查
				List<ItemCheck>mList = new ArrayList<ItemCheck>();
				ItemCheck ic; int i = 0;

				ic = new ItemCheck("4.1", "4.1 内表面有裂纹、皱折、夹层及瓶肩内有明显沟痕或皱折", 0, (BFYYDM[3].substring(i,i+1).equals("1") ? true : false), ""); mList.add(ic); i++;

				doCheck(PART+"-3", util.json.JSONUtils.toJsonWithGson(mList), 3);
			}

		});

		mV.check1_6_4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//重量与容积测定
				List<ItemCheck>mList = new ArrayList<ItemCheck>();
				ItemCheck ic; int i = 0;
				ic = new ItemCheck("5.1", "5.1 钢印标记重量与实测重量的差值大于钢印标记重量的5%，且瓶壁最小壁厚小于设计壁厚", 0, (BFYYDM[4].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("5.2", "5.2 实测容积值大于钢印标记容积值10%以上", 0, (BFYYDM[4].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("SCZL", "实测重量", 1, false, mData.getSCZL()); mList.add(ic);
				ic = new ItemCheck("GYBJZL", "钢印标记重量", 1, false, mData.getGYBJZL()); mList.add(ic);
				ic = new ItemCheck("MINBH", "最小壁厚", 1, false, mData.getMINBH()); mList.add(ic);
				ic = new ItemCheck("SJBH", "设计壁厚", 1, false, mData.getSJBH()); mList.add(ic);
				ic = new ItemCheck("SCRJ", "实测容积", 1, false, mData.getSCRJ()); mList.add(ic);
				ic = new ItemCheck("GYBJRJ", "钢印标记容积", 1, false, mData.getGYBJRJ()); mList.add(ic);

				doCheck(PART+"-4", util.json.JSONUtils.toJsonWithGson(mList), 4);
			}

		});

		mV.check1_6_5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//水压试验
				List<ItemCheck>mList = new ArrayList<ItemCheck>();
				BFYYDM[5].substring(0,1).equals("0");
				ItemCheck ic; int i = 0;
				ic = new ItemCheck("6.1", "6.1 瓶体出现渗漏、明显变形或保压期间压力有回降现象", 0, (BFYYDM[5].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("6.2", "6.2 容积残余变形率超过6%，且最小壁厚小于设计壁厚", 0, (BFYYDM[5].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("6.3", "6.3 容积残余变形率超过10%", 0, (BFYYDM[5].substring(i,i+1).equals("1") ? true : false), ""); mList.add(ic); i++;
				ic = new ItemCheck("RJCYBXL", "容积残余变形率", 1, false, mData.getRJCYBXL()); mList.add(ic);
				ic = new ItemCheck("MINBH", "最小壁厚", 1, false, mData.getMINBH()); mList.add(ic);
				ic = new ItemCheck("SJBH", "设计壁厚", 1, false, mData.getSJBH()); mList.add(ic);

				doCheck(PART+"-5", util.json.JSONUtils.toJsonWithGson(mList), 5);
			}

		});

		mV.check1_6_6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//气密性试验
				List<ItemCheck>mList = new ArrayList<ItemCheck>();
				ItemCheck ic; int i = 0;
				ic = new ItemCheck("7.1", "7.1 保压期间有泄露或压力回降现象", 0, (BFYYDM[6].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;
				ic = new ItemCheck("7.2", "7.2 瓶体泄漏", 0, (BFYYDM[6].substring(i,i+1).equals("1") ? true : false), "");
				mList.add(ic); i++;

				doCheck(PART+"-6", util.json.JSONUtils.toJsonWithGson(mList), 6);
			}

		});

		mV.btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConfirmDialog dialog = new ConfirmDialog(getContext());
				dialog.setTitle("退出提示");
				dialog.setMessage("确定退出检验？");
				dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}

				});
				dialog.show();
			}
		});

		mV.btnRfid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mV.check1_3_y.isChecked()){
					mData = new NewGZWFQPCheck ();
					mData.setC_ZL("0");
				}else{
					mData.setC_ZL("1");
				}

				mData.setJCX0(BFYYDM[0].contains("1") ? "0" : "1");
				mData.setJCX1(BFYYDM[1].contains("1") ? "0" : "1");
				mData.setJCX2(BFYYDM[2].contains("1") ? "0" : "1");
				mData.setJCX3(BFYYDM[3].contains("1") ? "0" : "1");
				mData.setJCX4(BFYYDM[4].contains("1") ? "0" : "1");
				mData.setJCX5(BFYYDM[5].contains("1") ? "0" : "1");
				mData.setJCX6(BFYYDM[6].contains("1") ? "0" : "1");
				mData.setBFYYDM(BFYYDM[0]+"|"+BFYYDM[1]+"|"+BFYYDM[2]+"|"+BFYYDM[3]+"|"+BFYYDM[4]+"|"+BFYYDM[5]+"|"+BFYYDM[6]);
				mData.setBFYY(BFYY[0]+BFYY[1]+BFYY[2]+BFYY[3]+BFYY[4]+BFYY[5]+BFYY[6]);

				if(mData.getC_ZL().equals("1") && !mData.getBFYYDM().contains("1")){
					ToastUtil.showToast(getContext(), "请选择报废原因");
					return ;
				}

				mData.setJCRQ(JCRQ);
				mData.setCheckTime(sdf1.format(new Date()));
				String nextdate = sdf3.format(new Date()); if(nextdate.equals("02-29")) nextdate = "02-28";
				String XCJCRQ = mV.check1_2.getSelectedItem().toString() + "-" + nextdate;
				mData.setXCJCRQ(XCJCRQ);
				mData.setPosID(DeviceID);

				UserInfo JC = (UserInfo) mV.check1_4.getSelectedItem();
				UserInfo SH = (UserInfo) mV.check1_5.getSelectedItem();

				mData.setC_WGJC(JC.getUserLoginID());
				mData.setC_WGSH(SH.getUserLoginID());
				mData.setC_YSJC(JC.getUserLoginID());
				mData.setC_YSSH(SH.getUserLoginID());
				mData.setC_PKLWJC(JC.getUserLoginID());
				mData.setC_PKLWSH(SH.getUserLoginID());
				mData.setC_NBJC(JC.getUserLoginID());
				mData.setC_NBSH(SH.getUserLoginID());
				mData.setC_RJJC(JC.getUserLoginID());
				mData.setC_RJSH(SH.getUserLoginID());
				mData.setC_SYJC(JC.getUserLoginID());
				mData.setC_SYSH(SH.getUserLoginID());
				mData.setC_QMJC(JC.getUserLoginID());
				mData.setC_QMSH(SH.getUserLoginID());

				Log.e("JumpToRfid", util.json.JSONUtils.toJsonWithGson(mData));
				ActivityUtils.JumpToRfid(getContext(), util.json.JSONUtils.toJsonWithGson(mData), ActivityNewGZWFQPCheck.this);
			}
		});
	}

	private void doCheck(String part, String data, int requestCode){
		ActivityUtils.JumpToCheck1_Item(getContext(), part, data, requestCode, this);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			if(requestCode==99){//读写标签返回
				mData = new NewGZWFQPCheck();
				BFYYDM[0] = "00000000000000000"; BFYYDM[1] = "0"; BFYYDM[2] = "00"; BFYYDM[3] =  "0";
				BFYYDM[4] = "00"; BFYYDM[5] = "000"; BFYYDM[6] = "00";
				for (int i = 0; i < BFYY.length; i++) {
					BFYY[i] = "";
				}
			}else{
				Bundle bundle = data.getExtras();
				String part = bundle.getString("part");
				List<ItemCheck> mList = util.json.JSONUtils.toListWithGson(bundle.getString("data"),  new TypeToken<List<ItemCheck>>(){}.getType());
				Log.e(part, bundle.getString("data"));
				Log.e("requestCode", String.valueOf(requestCode));
				switch (requestCode) {
					case 0:
						String[] mk = new String[17];
						BFYY[requestCode] = "";
						for (int i = 0; i < mList.size(); i++) {
							if(mList.get(i).isCheck()) BFYY[requestCode]+=mList.get(i).getText()+";";

							if(mList.get(i).getKey().equals("1.1")) mk[0] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.2")) mk[1] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.3")) mk[2] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.4")) mk[3] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.5")) mk[4] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.6")) mk[5] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.7")) mk[6] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.8")) mk[7] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.9")) mk[8] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.10")) mk[9] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.11")) mk[10] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.12")) mk[11] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.13")) mk[12] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.14")) mk[13] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.15")) mk[14] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.16")) mk[15] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("1.17")) mk[16] = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("KSHSSYBH")) mData.setKSHSSYBH(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("SJBH")) mData.setSJBH(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("AXSD")) mData.setAXSD(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("AXDJ")) mData.setAXDJ(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("KSHSSD")) mData.setKSHSSD(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("QXXMSYBH")) mData.setQXXMSYBH(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("FSCSYBH")) mData.setFSCSYBH(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("JIANJU")) mData.setJIANJU(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("TTJMWJC")) mData.setTTJMWJC(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("TTJMPJWJ")) mData.setTTJMPJWJ(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("TTZXD")) mData.setTTZXD(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("PTZXCD")) mData.setPTZXCD(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("WQSD")) mData.setWQSD(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("PTCZD")) mData.setPTCZD(mList.get(i).getValue());
						}
						String MK = "";
						for (int i = 0; i < mk.length; i++) {
							MK += mk[i];
						}
						BFYYDM[0] = MK;
						Log.e("BFYYDM[0]", BFYYDM[0]);
						break;
					case 1:
						BFYY[requestCode] = "";
						for (int i = 0; i < mList.size(); i++) {
							if(mList.get(i).isCheck()) BFYY[requestCode]+=mList.get(i).getText()+";";

							if(mList.get(i).getKey().equals("2.1")) BFYYDM[1] = !mList.get(i).isCheck() ? "0" : "1";
						}
						break;
					case 2:
						String mk31 = "1"; String mk32 = "1";
						BFYY[requestCode] = "";
						for (int i = 0; i < mList.size(); i++) {
							if(mList.get(i).isCheck()) BFYY[requestCode]+=mList.get(i).getText()+";";

							if(mList.get(i).getKey().equals("3.1")) mk31 = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("3.2")) mk32 = !mList.get(i).isCheck() ? "0" : "1";
						}
						BFYYDM[2] = mk31 + mk32;
						break;
					case 3:
						BFYY[requestCode] = "";
						for (int i = 0; i < mList.size(); i++) {
							if(mList.get(i).isCheck()) BFYY[requestCode]+=mList.get(i).getText()+";";

							if(mList.get(i).getKey().equals("4.1")) BFYYDM[3] = !mList.get(i).isCheck() ? "0" : "1";
						}
						break;
					case 4:
						String mk51 = "1"; String mk52 = "1";
						BFYY[requestCode] = "";
						for (int i = 0; i < mList.size(); i++) {
							if(mList.get(i).isCheck()) BFYY[requestCode]+=mList.get(i).getText()+";";

							if(mList.get(i).getKey().equals("5.1")) mk51 = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("5.2")) mk52 = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("SCZL")) mData.setSCZL(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("GYBJZL")) mData.setGYBJZL(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("MINBH")) mData.setMINBH(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("SJBH")) mData.setSJBH(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("SCRJ")) mData.setSCRJ(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("GYBJRJ")) mData.setGYBJRJ(mList.get(i).getValue());
						}
						BFYYDM[4] = mk51 + mk52;
						break;
					case 5:
						String mk61 = "1"; String mk62 = "1"; String mk63 = "1";
						BFYY[requestCode] = "";
						for (int i = 0; i < mList.size(); i++) {
							if(mList.get(i).isCheck()) BFYY[requestCode]+=mList.get(i).getText()+";";

							if(mList.get(i).getKey().equals("6.1")) mk61 = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("6.2")) mk62 = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("6.3")) mk63 = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("RJCYBXL")) mData.setRJCYBXL(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("MINBH")) mData.setMINBH(mList.get(i).getValue());
							else if(mList.get(i).getKey().equals("SJBH")) mData.setSJBH(mList.get(i).getValue());
						}
						BFYYDM[5] = mk61 + mk62 + mk63;
						break;
					case 6:
						String mk71 = "1"; String mk72 = "1";
						BFYY[requestCode] = "";
						for (int i = 0; i < mList.size(); i++) {
							if(mList.get(i).isCheck()) BFYY[requestCode]+=mList.get(i).getText()+";";

							if(mList.get(i).getKey().equals("7.1")) mk71 = !mList.get(i).isCheck() ? "0" : "1";
							else if(mList.get(i).getKey().equals("7.2")) mk72 = !mList.get(i).isCheck() ? "0" : "1";
						}
						BFYYDM[6] = mk71 + mk72;
						break;
					default:
						break;
				}
			}
		}

	}
}
