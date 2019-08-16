package com.hsic.qp.sz;

import java.util.List;
import util.ToastUtil;
import util.UiUtil;
import bean.BasicInfo;
import bean.BasicUnit;
import bean.GasBaseInfo;
import com.google.gson.reflect.TypeToken;
import com.hsic.qp.sz.adapter.ItemReadAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import hsic.ui.HsicActivity;

public class ActivitySearchQP extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";
	private Context getContext(){
		return this;
	}

	static class mView{
		Spinner spinner;
		EditText edittext;
		ListView listview;

		Button search;
	}
	mView mV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchqp);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		initView();
		new CallRfidWsTask(getContext(), this, 8).execute("");
	}

	private void initView(){
		mV = new mView();
		mV.spinner = (Spinner) findViewById(R.id.searchqp_1);
		mV.edittext = (EditText) findViewById(R.id.searchqp_2);
		mV.listview = (ListView) findViewById(R.id.searchqp_list);
		mV.search = (Button) findViewById(R.id.searchqp_btn);
		mV.search.setOnClickListener(onSearchClickListener);
	}

	OnClickListener onSearchClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BasicUnit CQDW = (BasicUnit) mV.spinner.getSelectedItem();
			if(CQDW==null){
				ToastUtil.showToast(getContext(), "请选择产权单位");
				return ;
			}
			if(mV.edittext.getText().toString().trim().length()==0){
				ToastUtil.showToast(getContext(), "请输入8位标签号");
				return ;
			}
			if(mV.edittext.getText().toString().trim().length()!=8){
				ToastUtil.showToast(getContext(), "请输入8位标签号");
				return ;
			}
			UiUtil.CloseKey(ActivitySearchQP.this);
			mV.listview.setAdapter(null);
			new CallRfidWsTask(getContext(), ActivitySearchQP.this, 13).execute(CQDW.getId() + mV.edittext.getText().toString().trim());
		}
	};

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			if(code==8){
				try {
					BasicInfo bi = (BasicInfo) util.json.JSONUtils.toObjectWithGson(retData, BasicInfo.class);
					ArrayAdapter<BasicUnit> CQDWAdapter = new ArrayAdapter<BasicUnit>(getContext(),android.R.layout.simple_spinner_item, bi.getCQDW());
					CQDWAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					mV.spinner.setAdapter(CQDWAdapter);
					for (int k = 0; k < bi.getCQDW().size(); k++) {
						if(bi.getCQDW().get(k).getId().equals("1702")){
							mV.spinner.setSelection(k);
							break;
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					ToastUtil.showToast(getContext(), "基础数据错误");
					return ;
				}
			}else if(code==13){
				try {
					List<GasBaseInfo> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<GasBaseInfo>>(){}.getType());

					mV.listview.setAdapter(new ItemReadAdapter(getContext(), list));
				} catch (Exception e) {
					// TODO: handle exception
					ToastUtil.showToast(getContext(), "集格内气瓶信息错误");
					mV.listview.setAdapter(null);
				}
			}
		}else{
			if(code==13){
				ToastUtil.showToast(getContext(), retData);
			}
		}
	}
}
