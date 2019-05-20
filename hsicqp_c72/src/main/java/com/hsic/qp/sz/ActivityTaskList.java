package com.hsic.qp.sz;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hsic.qp.sz.adapter.TaskAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.SaleInfoTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bean.Sale;
import bean.TruckNoInfo;
import hsic.ui.HsicActivity;
import util.ActivityUtils;

public class ActivityTaskList extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";

	static class mView{
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
		TextView tv6;
		ListView lv;

		Button btn1;
		Button btn2;
	}

	mView mV;

	List<Sale> tList ;
	String Truck;
	String Persons;

	private Context getContext(){
		return ActivityTaskList.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasklist);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		intiView();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GetSaleList();
	}

	private void GetSaleList(){
		new SaleInfoTask(getContext(), 0, this).execute(getApp().getLogin().getUserID());
	}

	private void intiView(){
		mV = new mView();
		mV.tv1 = (TextView) findViewById(R.id.tasklist_1);
		mV.tv2 = (TextView) findViewById(R.id.tasklist_2);
		mV.tv3 = (TextView) findViewById(R.id.tasklist_3);
		mV.tv4 = (TextView) findViewById(R.id.tasklist_4);
		mV.tv5 = (TextView) findViewById(R.id.tasklist_5);
		mV.tv6 = (TextView) findViewById(R.id.tasklist_6);
		mV.lv = (ListView) findViewById(R.id.task_list);

		mV.btn1 = (Button) findViewById(R.id.tasklist_btn1);
		mV.btn2 = (Button) findViewById(R.id.tasklist_btn2);

		mV.lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub

				ActivityUtils.JumpToTask(getContext(), util.json.JSONUtils.toJsonWithGson(tList.get(position)), Truck, Persons);

			}
		});

		mV.btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityUtils.JumpToReprint(getContext());
			}
		});

		mV.btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetSaleList();
			}
		});
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if( isSuccess){
			TruckNoInfo Info = (TruckNoInfo) util.json.JSONUtils.toObjectWithGson(retData, TruckNoInfo.class);
			Log.e("TruckNoInfo", util.json.JSONUtils.toJsonWithGson(Info));
			if(Info!=null){
				Truck = Info.getLicense();
				Persons = Info.getDriverName()!=null ? Info.getDriverName() : "";
				if(Persons.length()>0) Persons +=", ";
				Persons += Info.getEscortName()!=null ? Info.getEscortName() : "";

				tList = Info.getSale();
				mV.tv1.setText(Info.getLicense()!=null ? Info.getLicense() : "");
				mV.tv2.setText(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("DeviceID", getContext().getResources().getString(R.string.config_no)));
				mV.tv3.setText(getApp().getLogin().getStationName()!=null ? getApp().getLogin().getStationName() : "");

				mV.tv4.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				mV.tv6.setText(Info.getRemark()!=null ? Info.getRemark() : "");
				mV.lv.setAdapter(new TaskAdapter(getContext(), tList));

				if(tList!=null)
					mV.tv5.setText(String.valueOf(tList.size()));
				else mV.tv5.setText("0");
			}
		}
	}
}
