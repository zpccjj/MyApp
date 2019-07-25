package com.hsic.qp.sz;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hsic.qp.sz.adapter.ReceiveAdapter;

import java.util.ArrayList;
import java.util.List;

import bean.QPGoods;
import bean.QPInfo;
import hsic.ui.HsicActivity;
import util.ActivityUtils;
import util.ToastUtil;

public class ActivityQPReceive  extends HsicActivity {
	private final static String TITLE = "收瓶";

	static class mView{
		ListView list;
		Button addBtn;
		Button backBtn;
		Button rfidBtn;
	}
	private mView mV;

	private List<QPGoods> mList;
	List<QPInfo> rList = new ArrayList<QPInfo>();
	private ReceiveAdapter rAdapter;
	private String sList;

	private Context getContext(){
		return ActivityQPReceive.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive);

		mList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("mList"),  new TypeToken<List<QPGoods>>(){}.getType());
		rList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("rList"),  new TypeToken<List<QPInfo>>(){}.getType());
		sList = getIntent().getExtras().getString("sList");

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(TITLE);

		initViews();
		setListener();
	}

	private void initViews(){
		mV = new mView();
		mV.list = (ListView) findViewById(R.id.r_list);
		mV.addBtn = (Button) findViewById(R.id.r_btn1);
		mV.backBtn = (Button) findViewById(R.id.r_btn0);
		mV.rfidBtn = (Button) findViewById(R.id.r_btn2);

		rAdapter = new ReceiveAdapter(getContext(), mList);
		mV.list.setAdapter(rAdapter);
	}

	private void setListener(){
		mV.list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				if(listAlertDialog==null || !listAlertDialog.isShowing())
					GoodDiag(false, position);
			}
		});

		mV.addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listAlertDialog==null || !listAlertDialog.isShowing())
					GoodDiag(true, -1);
			}
		});

		mV.backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callReturn();
			}
		});

		mV.rfidBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(hasQP()){
					ActivityUtils.JumpToSendReceive(getContext(), ActivityQPReceive.this, 1,
							util.json.JSONUtils.toJsonWithGson(rList),
							sList,
							util.json.JSONUtils.toJsonWithGson(mList));
				}else{
					ToastUtil.showToast(getContext(), "请输入回收数量！");
					return ;
				}
			}
		});
	}

	private boolean hasQP(){
		for (int i = 0; i < mList.size(); i++) {
			if(mList.get(i).getGoodsNum()>0) return true;
		}
		return false;
	}

	private AlertDialog listAlertDialog;
	private void GoodDiag(final boolean isNew, final int id ){

		AlertDialog.Builder builder = new Builder(getContext());

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

		final View modifyView = inflater.inflate(R.layout.diag_receive,null);
		TableRow tr = (TableRow) modifyView.findViewById(R.id.diag_receive_0);
		final TextView tv = (TextView) modifyView.findViewById(R.id.diag_receive_0s);
		final EditText num = (EditText) modifyView.findViewById(R.id.diag_receive_2);
		final Spinner good = (Spinner) modifyView.findViewById(R.id.diag_receive_1);
		final TextView ps = (TextView) modifyView.findViewById(R.id.msg_ps);
		Button yes = (Button) modifyView.findViewById(R.id.diag_receive_btn1);
		Button no = (Button) modifyView.findViewById(R.id.diag_receive_btn2);
		Button delete = (Button) modifyView.findViewById(R.id.diag_receive_btn3);

		String Title = "添加收瓶";
		if(isNew){
			final ArrayAdapter<QPGoods> orgAdapter = new ArrayAdapter<QPGoods>(getContext(),android.R.layout.simple_spinner_item, getApp().getLogin().getGoodsList());
			orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			good.setAdapter(orgAdapter);

			good.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int position, long id) {
					// TODO Auto-generated method stub
					QPGoods goodInfo = (QPGoods) orgAdapter.getItem(position);
					if(goodInfo.getIsJG()==1){
						tv.setText("集格数量:");
						ps.setVisibility(View.VISIBLE);
					}else{
						tv.setText(getResources().getString(R.string.txt_string_12u));
						ps.setVisibility(View.GONE);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});

			delete.setVisibility(View.GONE);
		}else{
			Title = mList.get(id).getGoodsName();
			tr.setVisibility(View.GONE);
			if(mList.get(id).getGoodsNum()!=0)
				num.setText(String.valueOf(mList.get(id).getGoodsNum()));
			if(mList.get(id).getIsDelete()==0) delete.setVisibility(View.GONE);
			else delete.setVisibility(View.VISIBLE);

			if(mList.get(id).getIsJG()==1){
				tv.setText("集格数量:");
				ps.setVisibility(View.VISIBLE);
			}
		}

		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listAlertDialog.dismiss();

			}
		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mList.get(id).getGoodsNum()!=0)
					rList.clear();

				mList.remove(id);
				rAdapter.notifyDataSetChanged();
				listAlertDialog.dismiss();
			}
		});

		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(num.getText().toString().trim().length()==0 ){
					ToastUtil.showToast(getContext(), "请输入回收数量！");
					return ;
				}

				if(isNew){
					QPGoods selectGood = (QPGoods) good.getSelectedItem();
					for (int i = 0; i < mList.size(); i++) {
						if(mList.get(i).getGoodsCode().equals(selectGood.getGoodsCode())){
							ToastUtil.showToast(getContext(), "该商品已存在！");
							return ;
						}
					}
					QPGoods newGoods = new QPGoods();
					newGoods.setGoodsCode(selectGood.getGoodsCode());
					newGoods.setGoodsName(selectGood.getGoodsName());
					newGoods.setGoodsNum(Integer.valueOf(num.getText().toString().trim()));
					newGoods.setMediumCode(selectGood.getMediumCode());
					newGoods.setIsJG(selectGood.getIsJG());
					newGoods.setNum(selectGood.getNum());
					newGoods.setIsDelete(1);
					mList.add(newGoods);
				}else{
					mList.get(id).setGoodsNum(Integer.valueOf(num.getText().toString().trim()));
					rList.clear();
				}

				rAdapter.notifyDataSetChanged();
				listAlertDialog.dismiss();
			}
		});

		builder.setView(modifyView);

		listAlertDialog = builder.create();
		listAlertDialog.setTitle(Title);
		listAlertDialog.setCancelable(false);
		listAlertDialog.show();
	}


	private void callReturn(){
		Intent resultIntent = new Intent();
		resultIntent.putExtra("mList", util.json.JSONUtils.toJsonWithGson(mList));
		resultIntent.putExtra("rList", util.json.JSONUtils.toJsonWithGson(rList));
		this.setResult(RESULT_OK, resultIntent);
		finish();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1){
			if(resultCode == Activity.RESULT_OK){
				Bundle bundle = data.getExtras();
				rList = util.json.JSONUtils.toListWithGson(bundle.getString("rList"),  new TypeToken<List<QPInfo>>(){}.getType());
			}
		}
	}
}
