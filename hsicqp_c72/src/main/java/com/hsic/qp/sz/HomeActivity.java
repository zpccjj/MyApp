package com.hsic.qp.sz;

import bll.MyApplication;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import hsic.tmj.gridview.MyGridAdapter;
import hsic.tmj.gridview.MyGridView;
import hsic.ui.ConfirmDialog;

public class HomeActivity extends Activity {
	private TextView tv1, tv2;
	private MyGridView gridview;

	private String UserType = "0";

	private int[] img_text;

	private int[] imgs;

	private Intent[] Intents;
//    	{ActivityRfid.class, ActivityRfid.class,
//    		ActivityTaskList.class, ActivityRfid.class,
//    		ActivityRfid.class, ActivityRfid.class};

	private MyApplication getApp(){
		return (MyApplication) super.getApplication();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);

		Log.e(getApp().getLogin().getUserName(), "="+String.valueOf(getApp().getLogin().getUserType()));
		if(getApp().getLogin().getUserType()!=null && getApp().getLogin().getUserType().length()>0)
			UserType = getApp().getLogin().getUserType();

		if(UserType.equals("5") || UserType.equals("6")){
			//只有销售：5：驾驶员；6：押运员；
			img_text = new int[2];
			img_text[0] = R.string.txt_home_18;
			img_text[1] = R.string.txt_home_3;

			imgs = new int[2];
			imgs[0] = R.drawable.icon_clps;
			imgs[1] = R.drawable.icon_clps;
			Intents = new Intent[2];
			InitIntents(1);
		}else if(UserType.equals("11") || UserType.equals("12") || UserType.equals("13")){
			//除销售意外所有：11：收发业务；12：充装业务；13：检验业务；
			img_text = new int[5];
			img_text[0] = R.string.txt_home_1; img_text[1] = R.string.txt_home_2;
			img_text[2] = R.string.txt_home_4; img_text[3] = R.string.txt_home_5; img_text[4] = R.string.txt_home_17;
			imgs = new int[5];
			imgs[0] = R.drawable.icon_ccdj; imgs[1] = R.drawable.icon_jcdj;
			imgs[2] = R.drawable.icon_qpbd; imgs[3] = R.drawable.icon_qpjy; imgs[4] = R.drawable.icon_qpbd;
			Intents = new Intent[5];
			InitIntents(5);
		}else{
			//全功能：0超级管理员 1：站点管理
			img_text = new int[6];
			img_text[0] = R.string.txt_home_1; img_text[1] = R.string.txt_home_2;
			img_text[2] = R.string.txt_home_3; img_text[3] = R.string.txt_home_4;
			img_text[4] = R.string.txt_home_5; img_text[5] = R.string.txt_home_17;
			imgs = new int[6];
			imgs[0] = R.drawable.icon_ccdj; imgs[1] = R.drawable.icon_jcdj;
			imgs[2] = R.drawable.icon_clps; imgs[3] = R.drawable.icon_qpbd;
			imgs[4] = R.drawable.icon_qpjy; imgs[5] = R.drawable.icon_qpbd;
			Intents = new Intent[6];
			InitIntents(6);
		}



		tv1 = (TextView) findViewById(R.id.home_tv1);
		tv2 = (TextView) findViewById(R.id.home_tv2);

		gridview=(MyGridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyGridAdapter(this, img_text, imgs));

		gridview.setOnItemClickListener(MyGridViewOnItemClickListener);

		tv1.setText(getApp().getLogin().getUserName()!=null ? getApp().getLogin().getUserName() : "");
		tv2.setText(getApp().getLogin().getStationName()!=null ? getApp().getLogin().getStationName() : "");

	}

	private void InitIntents(int code){
		for (int i = 0; i < Intents.length; i++) {
			if(code==1){
				Bundle bundle = new Bundle();
				switch (i) {
					case 0:
						bundle.putInt("K", 3);
						Intents[i] =  new Intent(HomeActivity.this, ActivityTaskList.class);
						Intents[i].putExtras(bundle);
						break;

					case 1:
						bundle.putInt("K", 0);
						Intents[i] =  new Intent(HomeActivity.this, ActivityTaskList.class);
						Intents[i].putExtras(bundle);
						break;
				}
			}else if(code==5){
				Bundle bundle = new Bundle();
				switch (i) {
					case 0:
						bundle.putInt("IO", 0);
						Intents[i] =  new Intent(HomeActivity.this, ActivityTruckInOut.class);
						Intents[i].putExtras(bundle);
						break;
					case 1:
						bundle.putInt("IO", 1);
						Intents[i] =  new Intent(HomeActivity.this, ActivityTruckInOut.class);
						Intents[i].putExtras(bundle);
						break;
					case 2:
						Intents[i] =  new Intent(HomeActivity.this, ActivityFull.class);
						break;
					case 3:
						Intents[i] =  new Intent(HomeActivity.this, ActivityCheck.class);
						break;
					case 4:
						Intents[i] =  new Intent(HomeActivity.this, ActivityQP.class);
						break;
				}
			}else{
				Bundle bundle = new Bundle();
				switch (i) {
					case 0:
						bundle.putInt("IO", 0);
						Intents[i] =  new Intent(HomeActivity.this, ActivityTruckInOut.class);
						Intents[i].putExtras(bundle);
						break;
					case 1:
						bundle.putInt("IO", 1);
						Intents[i] =  new Intent(HomeActivity.this, ActivityTruckInOut.class);
						Intents[i].putExtras(bundle);
						break;
					case 2:
						bundle.putInt("K", 0);
						Intents[i] =  new Intent(HomeActivity.this, ActivityTaskList.class);
						Intents[i].putExtras(bundle);
						break;
					case 3:
						Intents[i] =  new Intent(HomeActivity.this, ActivityFull.class);
						break;
					case 4:
						Intents[i] =  new Intent(HomeActivity.this, ActivityCheck.class);
						break;
					case 5:
						Intents[i] =  new Intent(HomeActivity.this, ActivityQP.class);
						break;
				}
			}
		}

	}

	OnItemClickListener MyGridViewOnItemClickListener =  new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			// TODO Auto-generated method stub
			startActivity(Intents[position]);

		}
	};

	public void onBackPressed() {
		ConfirmDialog dialog = new ConfirmDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("确定退出？");
		dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		dialog.show();
	}
}
