package com.hsic.qp.sz;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.hsic.qp.sz.adapter.TaskAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.SaleInfoTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.QPGoods;
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
		TextView tv7;
		TextView tv8;
		ListView lv;

		Button btn1;
		Button btn2;

		TableRow tr;
	}

	mView mV;
	int K;

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

		K = getIntent().getExtras().getInt("K", 0);

		intiView();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GetSaleList();
	}

	private void GetSaleList(){
		new SaleInfoTask(getContext(), K, this).execute(getApp().getLogin().getUserID());
	}

	private void intiView(){
		mV = new mView();
		mV.tv1 = (TextView) findViewById(R.id.tasklist_1);
		mV.tv2 = (TextView) findViewById(R.id.tasklist_2);
		mV.tv3 = (TextView) findViewById(R.id.tasklist_3);
		mV.tv4 = (TextView) findViewById(R.id.tasklist_4);
		mV.tv5 = (TextView) findViewById(R.id.tasklist_5);
		mV.tv6 = (TextView) findViewById(R.id.tasklist_6);
		mV.tv7 = (TextView) findViewById(R.id.tasklist_7);
		mV.tv8 = (TextView) findViewById(R.id.tasklist_8);
		mV.lv = (ListView) findViewById(R.id.task_list);
		mV.tr = (TableRow) findViewById(R.id.task_tr);
		mV.btn1 = (Button) findViewById(R.id.tasklist_btn1);
		mV.btn2 = (Button) findViewById(R.id.tasklist_btn2);

		if(K==3) mV.tr.setVisibility(View.GONE);

		mV.lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				if(K==0)
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

	private String getQpAll(){
		String all = "";
		List<QPGoods> list = new ArrayList<QPGoods>();//GoodsCode,GoodsName,GoodsNum

		for (int i = 0; i < tList.size(); i++) {
			if(tList.get(i).getSaleDetail()!=null && tList.get(i).getSaleDetail().size()>0){
				for (int j = 0; j < tList.get(i).getSaleDetail().size(); j++) {
					boolean isHas = false;
					for (int j2 = 0; j2 < list.size(); j2++) {
						if(list.get(j2).getGoodsCode().equals(tList.get(i).getSaleDetail().get(j).getGoodsCode())){
							list.get(j2).setGoodsNum( list.get(j2).getGoodsNum() + tList.get(i).getSaleDetail().get(j).getPlanSendNum());
							isHas = true;
							break;
						}
					}
					if(!isHas){
						if(tList.get(i).getSaleDetail().get(j).getGoodsType()==1){
							QPGoods qpgood = new QPGoods();
							qpgood.setGoodsCode(tList.get(i).getSaleDetail().get(j).getGoodsCode());
							qpgood.setGoodsName(tList.get(i).getSaleDetail().get(j).getGoodsName());
							qpgood.setGoodsNum(tList.get(i).getSaleDetail().get(j).getPlanSendNum());
							list.add(qpgood);
						}
					}

				}
			}
		}

		for (int i = 0; i < list.size(); i++) {
			all += list.get(i).getGoodsName() + "："+String.valueOf(list.get(i).getGoodsNum()) + "。";
		}

		return all;
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
				mV.tv2.setText(getContext().getSharedPreferences("DeviceSetting", 0).getString("DeviceID", getContext().getResources().getString(R.string.config_no)));
				mV.tv3.setText(getApp().getLogin().getStationName()!=null ? getApp().getLogin().getStationName() : "");

				mV.tv4.setText(Info.getLicense()!=null ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : "");
				if(K==0) mV.tv6.setText(getResources().getString(R.string.txt_string_11s) + (Info.getETD()!=null ? Info.getETD() : ""));
				else mV.tv6.setText("装瓶："+ getQpAll());
				mV.lv.setAdapter(new TaskAdapter(getContext(), tList));

				int num = 0;
				if(Info.getSale()!=null && Info.getSale().size()>0){
					for (int i = 0; i < Info.getSale().size(); i++) {
						if(Info.getSale().get(i).getSaleDetail()!=null && Info.getSale().get(i).getSaleDetail().size()>0){
							for (int j = 0; j < Info.getSale().get(i).getSaleDetail().size(); j++) {
								if(Info.getSale().get(i).getSaleDetail().get(j).getGoodsType()==1){
									if(Info.getSale().get(i).getSaleDetail().get(j).getIsJG()==1)
										num += Info.getSale().get(i).getSaleDetail().get(j).getPlanSendNum() * Info.getSale().get(i).getSaleDetail().get(j).getNum();
									else num += Info.getSale().get(i).getSaleDetail().get(j).getPlanSendNum();
								}
							}
						}
					}
				}
				mV.tv7.setText(String.valueOf(num));
				mV.tv8.setText(String.valueOf(Info.getTruckNoID()));

				if(tList!=null)
					mV.tv5.setText(String.valueOf(tList.size()));
				else mV.tv5.setText("0");
			}
		}
	}
}
