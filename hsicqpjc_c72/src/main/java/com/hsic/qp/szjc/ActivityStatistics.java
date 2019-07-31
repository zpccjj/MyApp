package com.hsic.qp.szjc;

import hsic.ui.HsicActivity;

import com.hsic.qp.szjc.adapter.ItemStatisticsAdapter;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import bll.sqlite.NewGZWFQPCheck_DB;

public class ActivityStatistics extends HsicActivity{
	private final static String MenuHOME = "检验记录统计";

	static class mView{
		ListView lv;
		Button btn_back;
	}
	mView mV;

	private Context getContext(){
		return ActivityStatistics.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(MenuHOME);

		initViews();

		NewGZWFQPCheck_DB db = new NewGZWFQPCheck_DB(getContext());
		mV.lv.setAdapter(new ItemStatisticsAdapter(getContext(), db.getStatistics()));
	}

	private void initViews(){
		mV = new mView();
		mV.lv = (ListView) findViewById(R.id.statistics_list);
		mV.btn_back = (Button) findViewById(R.id.statistics_back);
		mV.btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
