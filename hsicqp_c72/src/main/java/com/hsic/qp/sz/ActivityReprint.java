package com.hsic.qp.sz;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.hsic.qp.sz.adapter.TaskAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.SaleInfoTask;
import java.util.List;
import bean.Sale;
import bean.TruckNoInfo;
import hsic.ui.HsicActivity;
import util.ActivityUtils;
import util.UiUtil;

public class ActivityReprint extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";
	ListView lv;

	List<Sale> tList ;
	String Truck;
	String Persons;

	private Context getContext(){
		return ActivityReprint.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reprintlist);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		intiView();

		new SaleInfoTask(getContext(), 1, this).execute(getApp().getLogin().getUserID());
	}

	private void intiView(){
		lv = (ListView) findViewById(R.id.reprint_list);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub

				ActivityUtils.JumpToPrint(getContext(), util.json.JSONUtils.toJsonWithGson(UiUtil.getPrintInfo(tList.get(position), Truck, Persons)), tList.get(position).getSaleID());

			}
		});
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if( isSuccess){
			TruckNoInfo Info = (TruckNoInfo) util.json.JSONUtils.toObjectWithGson(retData, TruckNoInfo.class);
			if(Info!=null){
				Truck = Info.getLicense();
				Persons = Info.getDriverName()!=null ? Info.getDriverName() : "";
				if(Persons.length()>0) Persons +=", ";
				Persons += Info.getEscortName()!=null ? Info.getEscortName() : "";

				tList = Info.getSale();
				lv.setAdapter(new TaskAdapter(getContext(), tList));
			}
		}
	}
}
