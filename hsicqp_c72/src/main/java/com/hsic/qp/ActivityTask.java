package com.hsic.qp;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.hsic.gps.GPSHelper;
import com.hsic.qp.adapter.QpAdapter;
import com.hsic.qp.listener.WsListener;
import com.hsic.qp.task.SubmitTask;

import util.ActivityUtils;
import util.ToastUtil;
import util.UiUtil;

import bean.InfoItem;
import bean.QPGoods;
import bean.QPInfo;
import bean.Sale;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import hsic.ui.ConfirmDialog;
import hsic.ui.HsicActivity;

public class ActivityTask extends HsicActivity implements WsListener{
	private final static String MenuHOME = "退出任务";
	Sale info;
	String Truck;
	String Persons;

	List<QPInfo> rList = new ArrayList<QPInfo>();//收瓶标签列表
	List<QPInfo> sList = new ArrayList<QPInfo>();//发瓶标签列表

	List<QPGoods> mList = new ArrayList<QPGoods>();

	static class mView{
		TextView txt1;
		TextView txt2;
		TextView txt3;
		TextView txt4;
		ListView lv;
		Button btnS;
		Button btnR;
		Button btnSubmit;
	}

	mView mV;

	GPSHelper gpsHelper;

	private Context getContext(){
		return ActivityTask.this;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		info = util.json.JSONUtils.toObjectWithGson(getIntent().getExtras().getString("TaskInfo"), Sale.class);

		Truck = getIntent().getExtras().getString("Truck");
		Persons = getIntent().getExtras().getString("Persons");

		initViews();
		setListener();

		Log.i("GPS","Start");
		gpsHelper = new GPSHelper(getContext());
		gpsHelper.GPSStart();//开始定位
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(gpsHelper!=null){
			try {
				gpsHelper.GPSStop();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			gpsHelper = null;
		}

		super.onDestroy();
	}

	private void initViews(){
		mV = new mView();
		mV.txt1 = (TextView) findViewById(R.id.task_1);
		mV.txt2 = (TextView) findViewById(R.id.task_2);
		mV.txt3 = (TextView) findViewById(R.id.task_3);
		mV.txt4 = (TextView) findViewById(R.id.task_4);

		mV.lv = (ListView) findViewById(R.id.qp_list);

		mV.btnR = (Button) findViewById(R.id.task_btn1);
		mV.btnS = (Button) findViewById(R.id.task_btn2);
		mV.btnSubmit = (Button) findViewById(R.id.task_btn3);

		mV.txt1.setText(info.getCustomerName()!=null ? info.getCustomerName() : "");
		mV.txt2.setText(info.getCustomerTelephone()!=null ? info.getCustomerTelephone() : "");
		mV.txt3.setText(info.getAddress()!=null ? info.getAddress() : "");
		mV.txt4.setText("0");

		for (int i = 0; i < info.getSaleDetail().size(); i++) {
			if(info.getSaleDetail().get(i).getGoodsType()==1){
				QPGoods goods = new QPGoods();
				goods.setGoodsCode(info.getSaleDetail().get(i).getGoodsCode());
				goods.setGoodsName(info.getSaleDetail().get(i).getGoodsName());
				mList.add(goods);
			}
		}

		setListView();
	}

	private void setListView(){
		if(info.getSaleDetail()!=null && info.getSaleDetail().size()>0){
			List<InfoItem> list = new ArrayList<InfoItem>();

			for (int i = 0; i < info.getSaleDetail().size(); i++) {
				InfoItem item = new InfoItem();
				item.setKey( "商品名称："+(info.getSaleDetail().get(i).getGoodsName()!=null ? info.getSaleDetail().get(i).getGoodsName() : "") );
				if(info.getSaleDetail().get(i).getGoodsType()==1){
					int num = 0;
					for (int j = 0; j < sList.size(); j++) {
						if(sList.get(j).getQPType()!=null && info.getSaleDetail().get(i).getGoodsCode()!=null
								&& sList.get(j).getQPType().equals(info.getSaleDetail().get(i).getGoodsCode())){
							num++;
						}
					}
					if(info.getCustomerType()!=null && info.getCustomerType().equals("CT02"))
						item.setName("气瓶单价：" + String.valueOf(info.getSaleDetail().get(i).getGoodsPrice()));

					item.setValue("发瓶数量：" + String.valueOf(num) + " / " + String.valueOf(info.getSaleDetail().get(i).getPlanSendNum()));
				}else if(info.getSaleDetail().get(i).getGoodsType()==6){
					if(info.getCustomerType()!=null && info.getCustomerType().equals("CT02"))
						item.setName("配送费：" + String.valueOf(info.getSaleDetail().get(i).getGoodsPrice()));
				}

				list.add(item);
			}
			mV.lv.setAdapter(new QpAdapter(getContext(), list));
		}else{
			mV.lv.setAdapter(null);
		}
	}

	private boolean isFinishSend(){
		boolean ret = true;
		if(info.getSaleDetail()!=null && info.getSaleDetail().size()>0){
			for (int i = 0; i < info.getSaleDetail().size(); i++) {
				if(info.getSaleDetail().get(i).getGoodsType()==1){
					int num = 0;
					for (int j = 0; j < sList.size(); j++) {
						if(sList.get(j).getQPType()!=null && info.getSaleDetail().get(i).getGoodsCode()!=null
								&& sList.get(j).getQPType().equals(info.getSaleDetail().get(i).getGoodsCode())){
							num++;
						}
					}
					if(num<info.getSaleDetail().get(i).getPlanSendNum()){
						ret = false;
						break;
					}
				}
			}
		}
		return ret;
	}

	private boolean hasOtherQp(){
		boolean ret = false;
//		if(rList==null || rList.size()==0) return ret;
//		for (int i = 0; i < rList.size(); i++) {
//			if(rList.get(i).getOpType()==9){
//				ret = true;
//				break;
//			}
//		}

		return ret;
	}

	private void setListener(){
		//key 1=Receive,2=Send
		mV.btnR.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

//				ActivityUtils.JumpToSendReceive(getContext(), ActivityTask.this, 1,
//						util.json.JSONUtils.toJsonWithGson(rList),
//						util.json.JSONUtils.toJsonWithGson(sList),
//						util.json.JSONUtils.toJsonWithGson(info.getSaleDetail()));

				ActivityUtils.JumpToReceive(getContext(), ActivityTask.this, util.json.JSONUtils.toJsonWithGson(mList));
			}
		});

		mV.btnS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityUtils.JumpToSendReceive(getContext(), ActivityTask.this, 2,
						util.json.JSONUtils.toJsonWithGson(rList),
						util.json.JSONUtils.toJsonWithGson(sList),
						util.json.JSONUtils.toJsonWithGson(info.getSaleDetail()));
			}
		});

		mV.btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("===GPS",
						"经度:"+gpsHelper.getGpsInfo().getLONGITUDE()+" "+"纬度:"+ gpsHelper.getGpsInfo().getLATITUDE()+" "+
								"地址:"+gpsHelper.getGpsInfo().getADDRESS());

				if(!isFinishSend()){
					//	ToastUtil.showToast(getContext(), "发瓶任务未完成");
					ConfirmDialog dialog = new ConfirmDialog(getContext());
					dialog.setTitle("提示");
					dialog.setMessage("发瓶扫描数量未满，是否由系统自动补录？");
					dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SubmitDiag(hasOtherQp());
						}

					});
					dialog.show();
				}else{
					SubmitDiag(hasOtherQp());
				}
			}
		});
	}

	private void Submit(String OtherQPPirce, String PayType){
		//info
		info.setGPS_J(String.valueOf(gpsHelper.getGpsInfo().getLONGITUDE()));
		info.setGPS_W(String.valueOf(gpsHelper.getGpsInfo().getLATITUDE()));
		info.setMatch("0");
//		List<QPInfo> qPList = new ArrayList<QPInfo>();
//		qPList.addAll(sList);
//		qPList.addAll(rList);
		info.setQPInfo(sList);

		List<QPGoods> list = new ArrayList<QPGoods>();
		for (int i = 0; i < mList.size(); i++) {
			if(mList.get(i).getGoodsNum()>0)
				list.add(mList.get(i));
		}
		info.setGoodsList(list);

		if(info.getSaleDetail()!=null && info.getSaleDetail().size()>0){
			for (int i = 0; i < info.getSaleDetail().size(); i++) {
				info.getSaleDetail().get(i).setRealGoodsPrice(info.getSaleDetail().get(i).getGoodsPrice());
				info.getSaleDetail().get(i).setSendNum(info.getSaleDetail().get(i).getPlanSendNum());
			}
		}
		if(OtherQPPirce!=null && OtherQPPirce.length()>0)
			info.setOtherQPPirce(BigDecimal.valueOf(Double.valueOf(OtherQPPirce)));
		info.setPayType(PayType);

		Log.e("SubmitTask", util.json.JSONUtils.toJsonWithGson(info));

		//submitTask
		new SubmitTask(getContext(), this).execute(util.json.JSONUtils.toJsonWithGson(info));

		//test
//		ToastUtil.showToast(getContext(), "销售单记录提交成功");
//		ActivityUtils.JumpToPrint(getContext(), info.getSaleID());
//		finish();
	}

	private AlertDialog listAlertDialog;
	private void SubmitDiag(final boolean hasOtherQP){
		AlertDialog.Builder builder = new Builder(getContext());

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

		final View modifyView = inflater.inflate(R.layout.diag_submit,null);

		TableRow tr = (TableRow) modifyView.findViewById(R.id.submit_tr);
		final EditText other = (EditText) modifyView.findViewById(R.id.submit_1);
		final Spinner paytype = (Spinner) modifyView.findViewById(R.id.submit_2);

		Button yes = (Button) modifyView.findViewById(R.id.submit_btn1);
		Button no = (Button) modifyView.findViewById(R.id.submit_btn2);

		if(info.getCustomerType()!=null && info.getCustomerType().equals("CT01")){
			paytype.setSelection(2);
			paytype.setEnabled(false);
		}

		if(!hasOtherQP) tr.setVisibility(View.GONE);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String OtherQPPirce = null;
				if(hasOtherQP){
					UiUtil.CloseKey(ActivityTask.this);
					OtherQPPirce = other.getText().toString().trim();
					if(OtherQPPirce==null || OtherQPPirce.length()==0){
						ToastUtil.showToast(getContext(), "请填写非本站气瓶总价");
						return ;
					}
				}

				Submit(OtherQPPirce, String.valueOf(paytype.getSelectedItemPosition()));
				listAlertDialog.dismiss();
			}
		});

		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listAlertDialog.dismiss();
			}
		});

		builder.setView(modifyView);

		listAlertDialog = builder.create();
		listAlertDialog.setTitle("任务提交");
		listAlertDialog.setCancelable(false);
		listAlertDialog.show();
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case android.R.id.home:
				escTask();
				break;
		}
		return true;
	}

//	public void onBackPressed() {
//		escTask();
//	}

	private void escTask(){
		ConfirmDialog dialog = new ConfirmDialog(getContext());
		dialog.setTitle("退出提示");
		dialog.setMessage("确定退出任务？");
		dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		dialog.show();
	}
	//requestCode 1=Receive,2=Send
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1){
			if(resultCode == Activity.RESULT_OK){
				Bundle bundle = data.getExtras();
				mList = util.json.JSONUtils.toListWithGson(bundle.getString("rList"),  new TypeToken<List<QPGoods>>(){}.getType());

				int num = 0;
				for (int i = 0; i < mList.size(); i++) {
					num += mList.get(i).getGoodsNum();
				}

				mV.txt4.setText(String.valueOf(num));
			}
		}else if(requestCode == 2){
			if(resultCode == Activity.RESULT_OK){
				Bundle bundle = data.getExtras();
				sList = util.json.JSONUtils.toListWithGson(bundle.getString("sList"),  new TypeToken<List<QPInfo>>(){}.getType());

				setListView();
			}
		}
	}
	@Override
	public void WsFinish(boolean isSuccess,int code, String retData) {
		// TODO Auto-generated method stub
		if( isSuccess){
			ToastUtil.showToast(getContext(), "销售单记录提交成功");
			ActivityUtils.JumpToPrint(getContext(), util.json.JSONUtils.toJsonWithGson(UiUtil.getPrintInfo(info, Truck, Persons)), info.getSaleID());
			finish();
		}

	}

}
