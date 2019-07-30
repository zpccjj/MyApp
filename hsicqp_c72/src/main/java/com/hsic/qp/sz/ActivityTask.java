package com.hsic.qp.sz;


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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hsic.gps.GPSHelper;
import com.hsic.qp.sz.adapter.QpAdapter;
import com.hsic.qp.sz.adapter.QpInfoAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;
import com.hsic.qp.sz.task.SubmitTask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import bean.InfoItem;
import bean.ItemQpInfo;
import bean.QPGoods;
import bean.QPInfo;
import bean.Sale;
import hsic.ui.ConfirmDialog;
import hsic.ui.HsicActivity;
import util.ActivityUtils;
import util.ToastUtil;
import util.UiUtil;

public class ActivityTask extends HsicActivity implements WsListener{
	private final static String MenuHOME = "退出任务";
	Sale info;
	String Truck;
	String Persons;

	boolean Submitted = false;

	List<QPInfo> rList = new ArrayList<QPInfo>();//收瓶标签列表
	List<QPInfo> sList = new ArrayList<QPInfo>();//发瓶标签列表

	List<QPGoods> mList = new ArrayList<QPGoods>();

	List<QPGoods> r_Medium = new ArrayList<QPGoods>();//收瓶重复介质 GoodsNum相同介质数量
	List<QPGoods> s_Medium = new ArrayList<QPGoods>();//发瓶重复介质

	static class mView{
		TextView txt1;
		TextView txt2;
		TextView txt3;
		TextView txt4;
		TextView txt4s;
		TextView txt4a;
		TextView txt4b;
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
		mV.txt4s = (TextView) findViewById(R.id.task_4s);
		mV.txt4a = (TextView) findViewById(R.id.task_4a);
		mV.txt4b = (TextView) findViewById(R.id.task_4b);

		mV.lv = (ListView) findViewById(R.id.qp_list);

		mV.btnR = (Button) findViewById(R.id.task_btn1);
		mV.btnS = (Button) findViewById(R.id.task_btn2);
		mV.btnSubmit = (Button) findViewById(R.id.task_btn3);

		mV.txt1.setText(info.getCustomerName()!=null ? info.getCustomerName() : "");
		mV.txt2.setText(info.getCustomerTelephone()!=null ? info.getCustomerTelephone() : "");
		mV.txt3.setText(info.getAddress()!=null ? info.getAddress() : "");
		mV.txt4a.setText("0");
		mV.txt4b.setText("0");

		int num = 0;
		if(info.getSaleDetail()!=null && info.getSaleDetail().size()>0){
			for (int i = 0; i < info.getSaleDetail().size(); i++) {
				info.getSaleDetail().get(i).setRealGoodsPrice(info.getSaleDetail().get(i).getGoodsPrice());
				info.getSaleDetail().get(i).setSendNum(info.getSaleDetail().get(i).getPlanSendNum());//

				if(info.getSaleDetail().get(i).getGoodsType()==6){
					info.getSaleDetail().get(i).setSendNum(1);
				}

				if(info.getSaleDetail().get(i).getGoodsType()==1){
					QPGoods goods = new QPGoods();
					goods.setGoodsCode(info.getSaleDetail().get(i).getGoodsCode());
					goods.setGoodsName(info.getSaleDetail().get(i).getGoodsName());
					goods.setMediumCode(info.getSaleDetail().get(i).getMediumCode());
					goods.setCZJZ(info.getSaleDetail().get(i).getCZJZ());
					goods.setIsJG(info.getSaleDetail().get(i).getIsJG());
					goods.setNum(info.getSaleDetail().get(i).getNum());
					goods.setGoodsNum(info.getSaleDetail().get(i).getPlanReceiveNum());
					mList.add(goods);

					if(info.getSaleDetail().get(i).getIsJG()==1) num+= info.getSaleDetail().get(i).getPlanReceiveNum() * info.getSaleDetail().get(i).getNum();
					else num+= info.getSaleDetail().get(i).getPlanReceiveNum();

					//计算发瓶介质列表
					boolean isExist = false;
					for (int j = 0; j < s_Medium.size(); j++) {
						if(s_Medium.get(j).getMediumCode().equals(info.getSaleDetail().get(i).getMediumCode())
								&& s_Medium.get(j).getIsJG()==info.getSaleDetail().get(i).getIsJG()){
							s_Medium.get(j).setGoodsNum( s_Medium.get(j).getGoodsNum()+1 );
							isExist = true;
							break;
						}
					}
					if(!isExist){
						QPGoods mQPGoods = new QPGoods();
						mQPGoods.setMediumCode(info.getSaleDetail().get(i).getMediumCode());
						mQPGoods.setIsJG(info.getSaleDetail().get(i).getIsJG());
						mQPGoods.setGoodsNum(1);
						s_Medium.add(mQPGoods);
					}
				}
			}
		}
		mV.txt4.setText(String.valueOf(num));
		setListView();
	}

	private void setListView(){
		if(info.getSaleDetail()!=null && info.getSaleDetail().size()>0){
			List<InfoItem> list = new ArrayList<InfoItem>();
			int pnum = 0;
			for (int i = 0; i < info.getSaleDetail().size(); i++) {
				InfoItem item = new InfoItem();
				item.setKey( "商品名称："+(info.getSaleDetail().get(i).getGoodsName()!=null ? info.getSaleDetail().get(i).getGoodsName() : "") );
				if(info.getSaleDetail().get(i).getGoodsType()==1){
					int rnum = 0;
					for (int j = 0; j < rList.size(); j++) {
						if(rList.get(j).getQPType()!=null && mList.get(i).getGoodsCode()!=null
								&& rList.get(j).getQPType().equals(mList.get(i).getGoodsCode())){
							rnum++;
						}
					}

					int snum = 0;
					for (int j = 0; j < sList.size(); j++) {
						if(sList.get(j).getQPType()!=null && info.getSaleDetail().get(i).getGoodsCode()!=null
								&& sList.get(j).getQPType().equals(info.getSaleDetail().get(i).getGoodsCode())){
							snum++;
						}
					}
					if(info.getCustomerType()!=null && info.getCustomerType().equals("CT02"))
						item.setName("气瓶单价：" + String.valueOf(info.getSaleDetail().get(i).getGoodsPrice()));
					else{
						item.setName("-");
					}

					if(info.getSaleDetail().get(i).getIsJG()==1){
						item.setValue2("预收数/实收数/收瓶扫描："
								+ String.valueOf(info.getSaleDetail().get(i).getPlanReceiveNum()) + "x"+String.valueOf(info.getSaleDetail().get(i).getNum())
								+ " / " + String.valueOf(mList.get(i).getGoodsNum()) + "x"+String.valueOf(info.getSaleDetail().get(i).getNum())
								+ " / " + String.valueOf(rnum));

						pnum += info.getSaleDetail().get(i).getPlanReceiveNum() *  info.getSaleDetail().get(i).getNum();

						item.setValue("订购数/实发数/发瓶扫描："
								+ String.valueOf(info.getSaleDetail().get(i).getPlanSendNum()) + "x"+String.valueOf(info.getSaleDetail().get(i).getNum())
								+ " / " + String.valueOf(info.getSaleDetail().get(i).getSendNum()) + "x"+String.valueOf(info.getSaleDetail().get(i).getNum())
								+ " / " + String.valueOf(snum));
					}
					else{
						item.setValue2("预收数/实收数/收瓶扫描："
								+ String.valueOf(info.getSaleDetail().get(i).getPlanReceiveNum())
								+ " / " + String.valueOf(mList.get(i).getGoodsNum())
								+ " / " + String.valueOf(rnum));
						pnum += info.getSaleDetail().get(i).getPlanReceiveNum();

						item.setValue("订购数/实发数/发瓶扫描："
								+ String.valueOf(info.getSaleDetail().get(i).getPlanSendNum())
								+ " / " + String.valueOf(info.getSaleDetail().get(i).getSendNum())
								+ " / " + String.valueOf(snum) );
					}


				}else if(info.getSaleDetail().get(i).getGoodsType()==6){
					if(info.getCustomerType()!=null && info.getCustomerType().equals("CT02")){
						item.setName("配送费：" + String.valueOf(info.getSaleDetail().get(i).getGoodsPrice()));
						item.setValue("-");
					}else{
						item.setName("-");
						item.setValue("-");
					}
				}

				list.add(item);
			}
			mV.lv.setAdapter(new QpAdapter(getContext(), list));

			mV.txt4s.setText(String.valueOf(pnum));
		}else{
			mV.lv.setAdapter(null);
		}
	}

	AlertDialog SbDialog;
	private void showDialog(){
		AlertDialog.Builder builder = new Builder(getContext());
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
		final View modifyView = inflater.inflate(R.layout.diag_sb,null);

		List<ItemQpInfo> sbList = new ArrayList<ItemQpInfo>();
		for (int i = 0; i < info.getSaleDetail().size(); i++) {
			if(info.getSaleDetail().get(i).getGoodsType()==1){
				ItemQpInfo itemInfo = new ItemQpInfo();
				itemInfo.setGoodsName(info.getSaleDetail().get(i).getGoodsName());
				itemInfo.setSendNum(String.valueOf(info.getSaleDetail().get(i).getSendNum()) );
				itemInfo.setReceiveNum(String.valueOf(mList.get(i).getGoodsNum()) + (mList.get(i).getIsJG()==1 ? "x" + String.valueOf(mList.get(i).getNum()) : ""));
				sbList.add(itemInfo);
			}
		}
		if(mList.size()>info.getSaleDetail().size()){
			for (int i = info.getSaleDetail().size(); i < mList.size(); i++) {
				ItemQpInfo itemInfo = new ItemQpInfo();
				itemInfo.setGoodsName(mList.get(i).getGoodsName());
				itemInfo.setSendNum("-");
				itemInfo.setReceiveNum(String.valueOf(mList.get(i).getGoodsNum()) + (mList.get(i).getIsJG()==1 ? "x" + String.valueOf(mList.get(i).getNum()) : ""));
				sbList.add(itemInfo);
			}
		}
		((ListView) modifyView.findViewById(R.id.sb_list)).setAdapter(new QpInfoAdapter(getContext(), sbList));
		((Button) modifyView.findViewById(R.id.sb_yes)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TaskCheckReceive();
				SbDialog.dismiss();
			}

		});
		((Button) modifyView.findViewById(R.id.sb_no)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SbDialog.dismiss();
			}
		});
		builder.setView(modifyView);

		SbDialog = builder.create();
		SbDialog.setTitle("收发确认");
		SbDialog.setCancelable(false);
		SbDialog.show();
	}
	AlertDialog OtherDialog;
	private void other(){
		AlertDialog.Builder builder = new Builder(getContext());

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

		final View modifyView = inflater.inflate(R.layout.diag_other,null);

		final EditText Remark = (EditText) modifyView.findViewById(R.id.other_1);
		final EditText OtherPirce = (EditText) modifyView.findViewById(R.id.other_2);

		((Button) modifyView.findViewById(R.id.other_yes)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Remark.getText().toString().trim().length()==0 && OtherPirce.getText().toString().trim().length()>0){
					ToastUtil.showToast(getContext(), "请填写备注");
					return ;
				}else if(Remark.getText().toString().trim().length()>0 && OtherPirce.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请填写费用");
					return ;
				}
				info.setRemark(Remark.getText().toString().trim().length()>0 ? Remark.getText().toString().trim() : null);
				info.setOtherPirce(OtherPirce.getText().toString().trim().length()>0 ? new BigDecimal(OtherPirce.getText().toString().trim()) : null);

				SubmitDiag(hasOtherQp());
				OtherDialog.dismiss();

			}
		});
		((Button) modifyView.findViewById(R.id.other_no)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OtherDialog.dismiss();
			}
		});

		builder.setView(modifyView);

		OtherDialog = builder.create();
		OtherDialog.setTitle("其他费用");
		OtherDialog.setCancelable(false);
		OtherDialog.show();
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
					if(num<info.getSaleDetail().get(i).getSendNum()){
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

				Log.e("rList", util.json.JSONUtils.toJsonWithGson(rList));
				Log.e("sList", util.json.JSONUtils.toJsonWithGson(sList));
				ActivityUtils.JumpToReceive(getContext(), ActivityTask.this,
						util.json.JSONUtils.toJsonWithGson(mList),
						util.json.JSONUtils.toJsonWithGson(rList),
						util.json.JSONUtils.toJsonWithGson(sList));
			}
		});

		mV.btnS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("rList", util.json.JSONUtils.toJsonWithGson(rList));
				Log.e("sList", util.json.JSONUtils.toJsonWithGson(sList));
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
				if(!Submitted){
					showDialog();
				}else{
					new CallRfidWsTask(getContext(), ActivityTask.this, 10).execute(info.getSaleID());
				}
			}
		});

		mV.lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				if(info.getSaleDetail().get(position).getGoodsType()==1)
					setNumDialog(position);
			}
		});

	}

	AlertDialog mDialog;
	private void setNumDialog(final int position){
		AlertDialog.Builder builder = new Builder(getContext());

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

		final View modifyView = inflater.inflate(R.layout.diag_send,null);
		final EditText num = (EditText) modifyView.findViewById(R.id.diag_send_1);
		TextView txt = (TextView) modifyView.findViewById(R.id.diag_send_0);
		TextView ps = (TextView) modifyView.findViewById(R.id.msg_ps);
		Button yes = (Button) modifyView.findViewById(R.id.diag_send_btn1);
		Button no = (Button) modifyView.findViewById(R.id.diag_send_btn2);

		if(info.getSaleDetail().get(position).getIsJG()==1){
			txt.setText("集格数：");
			ps.setVisibility(View.VISIBLE);
		}
		else txt.setText("瓶数：");

		num.setText(String.valueOf(info.getSaleDetail().get(position).getSendNum()));

		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});

		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UiUtil.CloseKey(ActivityTask.this);
				int NUM = 0;
				if(num.getText().toString().trim().length()>0){
					try {
						NUM = Integer.valueOf(num.getText().toString().trim());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

//				if(NUM==0){
//					ToastUtil.showToast(getContext(), "请输入实发数量");
//					return ;
//				}
//				if(NUM>info.getSaleDetail().get(position).getPlanSendNum()){
//					ToastUtil.showToast(getContext(), "发瓶数量不能大于订购数量");
//					return ;
//				}

				info.getSaleDetail().get(position).setSendNum(NUM);
				sList.clear();
				setListView();

				mDialog.dismiss();
			}
		});

		builder.setView(modifyView);

		mDialog = builder.create();
		mDialog.setTitle("修改实发数量");
		mDialog.setCancelable(false);
		mDialog.show();
	}

	private void CheckSubmit(){
		String txt = CheckSubmitData();
		if(txt.length()>0){
			ConfirmDialog dialog = new ConfirmDialog(getContext());
			dialog.setTitle("错误提示");
			dialog.setMessage(txt);
			dialog.setConfirmButtonWithTxt("继续", new android.content.DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					other();
				}

			});
			dialog.show();
		}else{
			other();
		}
	}

	private String CheckSubmitData(){
		String ret1 = "";
		String ret2 = "";
		for (int i = 0; i < info.getSaleDetail().size(); i++) {
			int num = 0;
			for (int j = 0; j < sList.size(); j++) {
				if(info.getSaleDetail().get(i).getGoodsCode().equals(sList.get(j).getQPType())){
					num++;
				}
			}
			if(num != info.getSaleDetail().get(i).getSendNum()){
				if(ret1.length()>0) ret1 += "、";
				ret1 += info.getSaleDetail().get(i).getGoodsName();
			}
		}
		if(ret1.length()>0){
			ret1 = "发瓶错误提示：" +  ret1 + "扫描数与实发数不符。";
		}
		//收瓶校对
		for (int i = 0; i < mList.size(); i++) {
			int num = 0;
			for (int j = 0; j < rList.size(); j++) {
				if(mList.get(i).getGoodsCode().equals(rList.get(j).getQPType())){
					num++;
				}
			}
			if(num != mList.get(i).getGoodsNum()){
				if(ret2.length()>0) ret2 += "、";
				ret2 += mList.get(i).getGoodsName();
			}
		}
		if(ret2.length()>0){
			ret2 = "收瓶错误提示：" +  ret2 + "扫描数与实收数不符。";
		}

		return ret1 + ret2;
	}

	private void TaskCheckReceive(){
		int num = 0;
		if(mList!=null && mList.size()>0){
			for (int i = 0; i < mList.size(); i++) {
				num += mList.get(i).getGoodsNum();
			}
		}
		if(num==0){//0收瓶
			ConfirmDialog dialog = new ConfirmDialog(getContext());
			dialog.setTitle("提示");
			dialog.setMessage("收瓶数为0，是否确认提交？");
			dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					CheckSubmit();
				}

			});
			dialog.show();
		}else{
			CheckSubmit();
		}
	}

	private void TaskFinish(){
		if(!isFinishSend()){
			//	ToastUtil.showToast(getContext(), "发瓶任务未完成");
			ConfirmDialog dialog = new ConfirmDialog(getContext());
			dialog.setTitle("提示");
			dialog.setMessage("发瓶扫描数量未满，请确认发瓶是否正确？");
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

	private void Submit(String OtherQPPirce, String PayType){
		//info
		info.setGPS_J(String.valueOf(gpsHelper.getGpsInfo().getLONGITUDE()));
		info.setGPS_W(String.valueOf(gpsHelper.getGpsInfo().getLATITUDE()));
		info.setMatch("0");

		List<QPInfo> qPList = new ArrayList<QPInfo>();
		qPList.addAll(sList);
		qPList.addAll(rList);
		info.setQPInfo(qPList);

		List<QPGoods> list = new ArrayList<QPGoods>();
		for (int i = 0; i < mList.size(); i++) {
			if(mList.get(i).getGoodsNum()>0)
				list.add(mList.get(i));
		}
		info.setGoodsList(list);

		if(OtherQPPirce!=null && OtherQPPirce.length()>0)
			info.setOtherQPPirce(BigDecimal.valueOf(Double.valueOf(OtherQPPirce)));
		info.setPayType(PayType);

		Log.e("SubmitTask", util.json.JSONUtils.toJsonWithGson(info));

		//test
		new SubmitTask(getContext(), this).execute(util.json.JSONUtils.toJsonWithGson(info));

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
				mList = util.json.JSONUtils.toListWithGson(bundle.getString("mList"),  new TypeToken<List<QPGoods>>(){}.getType());
				rList = util.json.JSONUtils.toListWithGson(bundle.getString("rList"),  new TypeToken<List<QPInfo>>(){}.getType());
				Log.e("mList", util.json.JSONUtils.toJsonWithGson(mList));
				int num = 0;
				for (int i = 0; i < mList.size(); i++) {
					if(mList.get(i).getIsJG()==1)
						num += mList.get(i).getGoodsNum() * mList.get(i).getNum();
					else num += mList.get(i).getGoodsNum();
				}
				mV.txt4.setText(String.valueOf(num));

				mV.txt4a.setText(String.valueOf(rList.size()));

				setListView();
			}
		}else if(requestCode == 2){
			if(resultCode == Activity.RESULT_OK){
				Bundle bundle = data.getExtras();
				sList = util.json.JSONUtils.toListWithGson(bundle.getString("sList"),  new TypeToken<List<QPInfo>>(){}.getType());

				mV.txt4b.setText(String.valueOf(sList.size()));

				setListView();
			}
		}
	}
	@Override
	public void WsFinish(boolean isSuccess,int code, String retData) {
		// TODO Auto-generated method stub
		if( isSuccess){
			if(code==0){
				Submitted = true;
				mV.btnSubmit.setText("打印");
				ToastUtil.showToast(getContext(), "销售单记录提交成功");
				//	ActivityUtils.JumpToPrint(getContext(), util.json.JSONUtils.toJsonWithGson(UiUtil.getPrintInfo(info, Truck, Persons)), info.getSaleID());
				//	finish();
				//	new SaleInfoTask(getContext(), 1, this).execute(getApp().getLogin().getUserID());
				new CallRfidWsTask(getContext(), this, 10).execute(info.getSaleID());
			}else if(code==10){
				try {
					Sale sinfo  = (Sale) util.json.JSONUtils.toObjectWithGson(retData, Sale.class);

					Log.e("打印 Sale", util.json.JSONUtils.toJsonWithGson(sinfo));

					ActivityUtils.JumpToPrint(getContext(), util.json.JSONUtils.toJsonWithGson(UiUtil.getPrintInfo(sinfo, Truck, Persons)), info.getSaleID());
					finish();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					ToastUtil.showToast(getContext(), "打印信息数据错误");
				}
			}
		}
	}

}
