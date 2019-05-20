package com.hsic.qp.sz;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hsic.qp.sz.adapter.PrintAdapter;
import com.hsic.qp.sz.task.PrintfTask;

import java.util.List;

import bean.InfoItem;
import hsic.ui.HsicActivity;
import util.ToastUtil;

public class ActivityPrint extends HsicActivity{
	private final static String TITLE = "单据打印";

	static class mView{
		TextView txt;
		ListView list;
		Button printBtn;
		Button backBtn;
	}
	mView mV;
	
	List<InfoItem> infoList ;
	String SaleID;
	
	private Context getContext(){
		return ActivityPrint.this;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print);
		
		infoList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("PrintListInfo"),  new TypeToken<List<InfoItem>>(){}.getType());
		SaleID = getIntent().getExtras().getString("SaleID");
		
		
		ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(TITLE);
        
        initViews();
        setListener();
        
        String btMac = getContext().getSharedPreferences("DeviceSetting", 0).getString("bluetooth_mac", "");
        if(btMac==null || btMac.length()==0)
        	mV.printBtn.setEnabled(false);
	}
	
	private void initViews(){
		mV = new mView();
		mV.txt = (TextView) findViewById(R.id.print_1);
		mV.list = (ListView) findViewById(R.id.print_list);
		mV.printBtn = (Button) findViewById(R.id.print_btn);
		mV.backBtn = (Button) findViewById(R.id.print_back);
		
		if(BluetoothAdapter.getDefaultAdapter()==null){
			mV.printBtn.setEnabled(false);
		}
		mV.txt.setText("������:"+SaleID);
		mV.list.setAdapter(new PrintAdapter(getContext(), infoList));
	}
	
	private void setListener(){
		mV.backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mV.printBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SaleID==null || SaleID.length()==0){
					ToastUtil.showToast(getContext(), "无订单号");
					return ;
				}
				if(infoList==null || infoList.size()==0){
					ToastUtil.showToast(getContext(), "无打印信息");
					return ;
				}
					
				new PrintfTask(getContext(), infoList, SaleID).execute();
				
			}
		});
	}
	
}
