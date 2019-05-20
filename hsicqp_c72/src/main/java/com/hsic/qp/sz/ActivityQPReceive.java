package com.hsic.qp.sz;

import android.app.ActionBar;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;

import com.google.gson.reflect.TypeToken;
import com.hsic.qp.sz.adapter.ReceiveAdapter;

import java.util.List;

import bean.QPGoods;
import hsic.ui.HsicActivity;
import util.ToastUtil;

public class ActivityQPReceive  extends HsicActivity {
	private final static String TITLE = "收瓶";

	static class mView{
		ListView list;
		Button addBtn;
		Button backBtn;
	}
	private mView mV;

	private List<QPGoods> mList;
	private ReceiveAdapter rAdapter;

	private Context getContext(){
		return ActivityQPReceive.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive);

		mList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("rList"),  new TypeToken<List<QPGoods>>(){}.getType());

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
	}

	private AlertDialog listAlertDialog;
	private void GoodDiag(final boolean isNew, final int id ){

		AlertDialog.Builder builder = new Builder(getContext());

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

		final View modifyView = inflater.inflate(R.layout.diag_receive,null);
		TableRow tr = (TableRow) modifyView.findViewById(R.id.diag_receive_0);
		final EditText num = (EditText) modifyView.findViewById(R.id.diag_receive_2);
		final Spinner good = (Spinner) modifyView.findViewById(R.id.diag_receive_1);

		Button yes = (Button) modifyView.findViewById(R.id.diag_receive_btn1);
		Button no = (Button) modifyView.findViewById(R.id.diag_receive_btn2);
		Button delete = (Button) modifyView.findViewById(R.id.diag_receive_btn3);

		String Title = "添加收瓶";
		if(isNew){
			ArrayAdapter<QPGoods> orgAdapter = new ArrayAdapter<QPGoods>(getContext(),android.R.layout.simple_spinner_item, getApp().getLogin().getGoodsList());
			orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			good.setAdapter(orgAdapter);

			delete.setVisibility(View.GONE);
		}else{
			Title = mList.get(id).getGoodsName();
			tr.setVisibility(View.GONE);
			num.setText(String.valueOf(mList.get(id).getGoodsNum()));
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
				mList.remove(id);
				rAdapter.notifyDataSetChanged();
				listAlertDialog.dismiss();
			}
		});

		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(num.getText().toString().trim().length()==0 || Integer.valueOf(num.getText().toString().trim())==0){
					ToastUtil.showToast(getContext(), "请输入收瓶数量！");
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
					mList.add(newGoods);
				}else{
					mList.get(id).setGoodsNum(Integer.valueOf(num.getText().toString().trim()));
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
		resultIntent.putExtra("rList", util.json.JSONUtils.toJsonWithGson(mList));

		this.setResult(RESULT_OK, resultIntent);
		finish();
	}
}
