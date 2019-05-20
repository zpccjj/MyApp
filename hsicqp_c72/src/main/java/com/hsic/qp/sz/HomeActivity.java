package com.hsic.qp.sz;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import bll.MyApplication;
import hsic.tmj.gridview.MyGridAdapter;
import hsic.tmj.gridview.MyGridView;
import hsic.ui.ConfirmDialog;

public class HomeActivity extends Activity {
	private TextView tv1, tv2;
	private MyGridView gridview;
	private int[] img_text = { R.string.txt_home_1, R.string.txt_home_2,
			R.string.txt_home_3, R.string.txt_home_4,
			R.string.txt_home_5, R.string.txt_home_6};

	private int[] imgs = { R.drawable.icon_ccdj, R.drawable.icon_jcdj,
			R.drawable.icon_clps, R.drawable.icon_qpbd,
			R.drawable.icon_qpjy, R.drawable.icon_qpbd};

	private Intent[] Intents = new Intent[6];
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

		InitIntents();

		tv1 = (TextView) findViewById(R.id.home_tv1);
		tv2 = (TextView) findViewById(R.id.home_tv2);

		gridview=(MyGridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyGridAdapter(this, img_text, imgs));

		gridview.setOnItemClickListener(MyGridViewOnItemClickListener);

		tv1.setText(getApp().getLogin().getUserName()!=null ? getApp().getLogin().getUserName() : "");
		tv2.setText(getApp().getLogin().getStationName()!=null ? getApp().getLogin().getStationName() : "");

	}

	private void InitIntents(){
		for (int i = 0; i < Intents.length; i++) {
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
					Intents[i] =  new Intent(HomeActivity.this, ActivityTaskList.class);
					break;
				case 3:
					Intents[i] =  new Intent(HomeActivity.this, ActivityFull.class);
					break;
				case 4:
					Intents[i] =  new Intent(HomeActivity.this, ActivityCheck.class);
					break;
				case 5:
					Intents[i] =  new Intent(HomeActivity.this, ActivityRfid.class);
					break;
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
