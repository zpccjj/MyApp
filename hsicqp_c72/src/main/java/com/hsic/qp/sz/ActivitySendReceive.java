package com.hsic.qp.sz;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;

import com.google.gson.reflect.TypeToken;
import com.hsic.qp.sz.adapter.SRAdapter;
import com.hsic.qp.sz.task.ScanTask;
import com.rscja.deviceapi.RFIDWithUHF;

import java.util.ArrayList;
import java.util.List;

import bean.InfoItem;
import bean.QPInfo;
import bean.Rfid;
import bean.SaleDetail;
import hsic.ui.ConfirmDialog;
import hsic.ui.HsicActivity;
import util.ToastUtil;
import util.UiUtil;

public class ActivitySendReceive extends HsicActivity {
	private String TITLE = "发瓶";

	List<QPInfo> rList = new ArrayList<QPInfo>();//收瓶标签列表
	List<QPInfo> sList = new ArrayList<QPInfo>();//发瓶标签列表

	List<SaleDetail> SaleDetail;

	int key;//key 1=Receive,2=Send

	static class mView{
		ListView lv;
		ScrollView sv;
		EditText number;
		TableRow tr;
		Spinner good;
		CheckBox others;
		Button btnClear;
		Button btnBack;
		Button btnWrite;
		Button btnNo;
		Button btnYes;
	}

	mView mV;
	SRAdapter mAdapter;

	private RFIDWithUHF mReader;
	ScanTask rfidTask;
	boolean isInit = false;

	private Context getContext(){
		return ActivitySendReceive.this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sr);

		key = getIntent().getExtras().getInt("key");
		if(key==1) TITLE = "收瓶";
		Log.e("ActivitySendReceive", "key = " + key);
		try {
			rList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("rList"),  new TypeToken<List<QPInfo>>(){}.getType());
			sList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("sList"),  new TypeToken<List<QPInfo>>(){}.getType());

			if(key==2){
				SaleDetail = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("Detail"),  new TypeToken<List<SaleDetail>>(){}.getType());
			}else{
				Log.e( "rlist " , getIntent().getExtras().getString("rList"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(TITLE);

		initViews();
		setListener();

		new InitTask(getContext()).execute();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		if(mReader!=null && rfidTask==null && isInit){
			rfidTask = new ScanTask(myHandler);
			rfidTask.execute(mReader);
		}

		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if(mReader!=null && rfidTask!=null && rfidTask.getStatus()==AsyncTask.Status.RUNNING){
			rfidTask.cancel(true);
		}

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mReader != null) {
			mReader.free();
			ToastUtil.showToast(getContext(), "设备下电");
		}

		super.onDestroy();
	}

	@Override
	public void getRFID(String txt) {
		// TODO Auto-generated method stub
		super.getRFID(txt);
		Rfid rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(txt, Rfid.class);


		if(!rfid.getVersion().equals("0101")) return ;

		if(!rfid.getCQDW().equals(getApp().getLogin().getStation())){
			ToastUtil.showToast(getContext(), "非产权单位标签");
			return;
		}

		int ret = CheckQP(rfid.getLabelNo());
		if(ret==1){
			ToastUtil.showToast(getContext(), "该气瓶已收瓶");
			return;
		}
		if(ret==2){
			ToastUtil.showToast(getContext(), "该气瓶已发瓶");
			return;
		}

		if(key==1){
			QPInfo qp = new QPInfo();
			qp.setLabelNo(rfid.getLabelNo());
			qp.setCQDW(rfid.getCQDW());
			qp.setOpType(mV.others.isChecked() ? 9 : 0);
			qp.setIsByHand(0);
			rList.add(qp);
		}else{
			String goodcode = isFull(null, rfid.getCZJZCode());
			if(goodcode.length()>1){
				QPInfo qp = new QPInfo();
				qp.setLabelNo(rfid.getLabelNo());
				qp.setCQDW(rfid.getCQDW());
				qp.setQPType(goodcode);
				qp.setMsg("商品名称："+ getGoodeName(goodcode));
				qp.setMediumCode(rfid.getCZJZCode());
				qp.setIsByHand(0);
				qp.setOpType(1);
				sList.add(qp);
			}else if(goodcode.length()==1){
				ToastUtil.showToast(getContext(), "该类气瓶已配送完毕");
				return;
			}else{
				ToastUtil.showToast(getContext(), "充装介质错误");
				return;
			}
		}
		reflishListView();
		util.SoundUtil.play();
	}

	@Override
	public void closeRFID(){
		super.closeRFID();
		rfidTask = null;
	}

	/**
	 * 设备上电异步类
	 */
	private class InitTask extends AsyncTask<String, Integer, Integer> {
		ProgressDialog mypDialog;
		Context mContext;
		public InitTask(Context context){
			mContext = context;
		}

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				mReader = RFIDWithUHF.getInstance();
			} catch (Exception ex) {

				return 1;
			}

			boolean init = mReader.init();
			if(!init) return 2;

			boolean pow = mReader.setPower(15);
			if(!pow) return 3;

			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			mypDialog.cancel();

			if (result!=0){
				String txt="设备打开失败：";
				switch (result) {
					case 1:
						txt+="初始化失败";
						break;
					case 2:
						txt+="上电失败";
						break;
					case 3:
						txt+="设置频率失败";
						break;
				}
				ToastUtil.showToast(getContext(), txt);
			}else{
				ToastUtil.showToast(getContext(), "RFID设备开启");

				isInit = true;
				rfidTask = new ScanTask(myHandler);
				rfidTask.execute(mReader);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			mypDialog = new ProgressDialog(mContext);
			mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mypDialog.setMessage("正在打开RFID设备...");
			mypDialog.setCanceledOnTouchOutside(false);
			mypDialog.show();
		}
	}




	private void initViews(){
		mV = new mView();
		mV.lv = (ListView) findViewById(R.id.sr_list);
		mV.sv = (ScrollView)findViewById(R.id.sr_sv);

		mV.number = (EditText) findViewById(R.id.sr_1);
		mV.tr = (TableRow) findViewById(R.id.sr_tr);
		mV.good = (Spinner) findViewById(R.id.sr_2);
		mV.others = (CheckBox) findViewById(R.id.sr_3);

		mV.btnClear = (Button) findViewById(R.id.sr_btn4);
		mV.btnBack = (Button) findViewById(R.id.sr_btn0);
		mV.btnWrite = (Button) findViewById(R.id.sr_btn1);
		mV.btnNo = (Button) findViewById(R.id.sr_btn2);
		mV.btnYes = (Button) findViewById(R.id.sr_btn3);

		//初始化Spinner
		if(key==1){//收
			mV.tr.setVisibility(View.GONE);
			mV.others.setVisibility(View.VISIBLE);
			mAdapter = new SRAdapter(getContext(), rList, 1);
			mV.lv.setAdapter(mAdapter);
		}else{//发
			mV.tr.setVisibility(View.VISIBLE);
			mV.others.setVisibility(View.GONE);
			mAdapter = new SRAdapter(getContext(), sList, 2);
			mV.lv.setAdapter(mAdapter);

			List<InfoItem> items = new ArrayList<InfoItem>();

			for (int i = 0; i < SaleDetail.size(); i++) {
				if(SaleDetail.get(i).getGoodsType()==1){
					InfoItem item = new InfoItem();
					item.setKey(SaleDetail.get(i).getGoodsCode());
					item.setName(SaleDetail.get(i).getGoodsName());
					item.setValue(SaleDetail.get(i).getMediumCode());

					items.add(item);
				}
			}

			ArrayAdapter<InfoItem> orgAdapter = new ArrayAdapter<InfoItem>(getContext(),android.R.layout.simple_spinner_item, items);
			orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mV.good.setAdapter(orgAdapter);
		}
	}

	private void setView(boolean isList){
		if(isList){
			mV.lv.setVisibility(View.VISIBLE);
			mV.sv.setVisibility(View.GONE);

			mV.btnClear.setVisibility(View.VISIBLE);
			mV.btnBack.setVisibility(View.VISIBLE);
			mV.btnWrite.setVisibility(View.VISIBLE);

			mV.btnNo.setVisibility(View.GONE);
			mV.btnYes.setVisibility(View.GONE);
		}else{
			mV.lv.setVisibility(View.GONE);
			mV.sv.setVisibility(View.VISIBLE);

			mV.btnClear.setVisibility(View.GONE);
			mV.btnBack.setVisibility(View.GONE);
			mV.btnWrite.setVisibility(View.GONE);

			mV.btnNo.setVisibility(View.VISIBLE);
			mV.btnYes.setVisibility(View.VISIBLE);
		}
	}

	private void reflishListView(){
		mAdapter.notifyDataSetChanged();
	}

	private void deleteSelect(final int id){
		ConfirmDialog dialog = new ConfirmDialog(getContext());
		dialog.setTitle("删除提示");
		if(key==1) dialog.setMessage("确定删除标签 "+ ( rList.get(id).getCQDW()!=null ? rList.get(id).getCQDW() : "" ) + ( rList.get(id).getLabelNo()!=null ? rList.get(id).getLabelNo() : "" ));
		else dialog.setMessage("确定删除标签 "+ ( sList.get(id).getCQDW()!=null ? sList.get(id).getCQDW() : "" ) + ( sList.get(id).getLabelNo()!=null ? sList.get(id).getLabelNo() : "" ));
		dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(key==1) rList.remove(id);
				else sList.remove(id);
				reflishListView();
			}

		});
		dialog.show();

	}

	private void setListener(){
		mV.lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				deleteSelect(position);
			}
		});

		mV.btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callReturn();
			}
		});

		mV.btnWrite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setView(false);
			}
		});

		mV.btnNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//UiUtil.CloseKey(ActivitySendReceive.this);
				setView(true);
			}
		});

		mV.btnYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UiUtil.CloseKey(ActivitySendReceive.this);
				if(mV.number.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入标签号");
					return ;
				}

				int ret = CheckQP(mV.number.getText().toString().trim());
				if(ret==1){
					ToastUtil.showToast(getContext(), "该气瓶已收瓶");
					return;
				}
				if(ret==2){
					ToastUtil.showToast(getContext(), "发瓶列表已存在此气瓶");
					return;
				}
				String lno = mV.number.getText().toString().trim();
				if(lno.length()<8){
					String txt = "";
					for(int i=0; i<(8-lno.length()); i++){
						txt += "0";
					}
					mV.number.setText(txt + lno);
				}
				if(key==1)//收
					addQPByHand(mV.number.getText().toString().trim(), mV.others.isChecked() ? 9 : 0);
				else{
					if((InfoItem) mV.good.getAdapter().getItem(mV.good.getSelectedItemPosition()) == null){
						ToastUtil.showToast(getContext(), "请选择商品");
						return;
					}

					addQPByHand(mV.number.getText().toString().trim(), mV.good.getSelectedItemPosition());
				}
			}
		});

		mV.btnClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(key==1){
					rList.clear();
				}else{
					sList.clear();
				}
				reflishListView();
			}
		});

	}

	private void addQPByHand(String LabelNo, int code){
		if(key==1){
			QPInfo qp = new QPInfo();
			qp.setLabelNo(LabelNo);
			qp.setOpType(code);
			if(code==0) qp.setCQDW(getApp().getLogin().getStation());
			else qp.setCQDW("");

			qp.setIsByHand(1);
			rList.add(qp);
		}else{
			InfoItem goodInfo = (InfoItem) mV.good.getAdapter().getItem(code);
			if(isFull(goodInfo.getKey(), goodInfo.getValue()).length()>0){
				QPInfo qp = new QPInfo();
				qp.setCQDW(getApp().getLogin().getStation());
				qp.setLabelNo(LabelNo);
				qp.setQPType(goodInfo.getKey());
				qp.setMsg("商品名称："+goodInfo.getName());
				qp.setMediumCode(goodInfo.getValue());
				qp.setIsByHand(1);
				qp.setOpType(1);
				sList.add(qp);
			}else{
				ToastUtil.showToast(getContext(), "该类气瓶已配送完毕");
				return;
			}
		}
		mV.number.setText("");
		setView(true);
		reflishListView();
	}

	private String isFull(String GoodCode, String MediumCode){
		//goodcode
		if(GoodCode!=null){
			int snum = 0;
			for (int i = 0; i < sList.size(); i++) {
				if(sList.get(i).getQPType().equals(GoodCode)) snum ++;
			}
			for (int i = 0; i < SaleDetail.size(); i++) {
				if(SaleDetail.get(i).getGoodsCode().equals(GoodCode)){
					if(snum==SaleDetail.get(i).getPlanSendNum()) return "";
				}
			}
			return GoodCode;

		}else{
			String ret = "";
			for(int i = 0; i < SaleDetail.size(); i++) {
				if(SaleDetail.get(i).getMediumCode().equals(MediumCode)){
					ret = "1";
					int snum = 0;
					for (int j = 0; j < sList.size(); j++) {
						if(sList.get(j).getQPType().equals(SaleDetail.get(i).getGoodsCode())) snum ++;
					}
					if(snum<SaleDetail.get(i).getPlanSendNum()) return SaleDetail.get(i).getGoodsCode();
				}
			}

			return ret;
		}
	}

	private String getGoodeName(String GoodCode){
		for (int i = 0; i < SaleDetail.size(); i++) {
			if(SaleDetail.get(i).getGoodsCode().equals(GoodCode)){
				return SaleDetail.get(i).getGoodsName();
			}
		}

		return "";
	}

	private int CheckQP(String LabelNo){
		int ret = 0;
		for (int i = 0; i < rList.size(); i++) {
			if(rList.get(i).getLabelNo().equals(LabelNo)){
				ret = 1;
				return ret;
			}
		}

		for (int i = 0; i < sList.size(); i++) {
			if(sList.get(i).getLabelNo().equals(LabelNo)){
				ret = 2;
				return ret;
			}
		}

		return ret = 0;
	}

	private void callReturn(){
		Intent resultIntent = new Intent();
		if(key==1) resultIntent.putExtra("rList", util.json.JSONUtils.toJsonWithGson(rList));
		else resultIntent.putExtra("sList", util.json.JSONUtils.toJsonWithGson(sList));

		this.setResult(RESULT_OK, resultIntent);
		finish();
	}

}
