package com.hsic.qp.sz;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;
import com.hsic.qp.sz.task.ScanTask;
import com.rscja.deviceapi.RFIDWithUHF;

import java.util.ArrayList;
import java.util.List;

import bean.QPGoods;
import bean.QPInfo;
import bean.Rfid;
import bean.SaleDetail;
import hsic.ui.ConfirmDialog;
import hsic.ui.HsicActivity;
import util.ToastUtil;
import util.UiUtil;

public class ActivitySendReceive extends HsicActivity implements WsListener{
	private String TITLE = "发瓶扫描";
	List<QPGoods> qpList = new ArrayList<QPGoods>();//商品信息列表

	List<QPInfo> rList = new ArrayList<QPInfo>();//收瓶标签列表
	List<QPInfo> sList = new ArrayList<QPInfo>();//发瓶标签列表

	int key;//key 1=Receive,2=Send

	static class mView{
		ListView lv;
		ScrollView sv;
		EditText number;
		TableRow tr;
		Spinner good;
		CheckBox others;
		Button btnScan;
		Button btnClear;
		Button btnBack;
		Button btnWrite;
		Button btnNo;
		Button btnYes;
	}


	mView mV;
	SRAdapter mAdapter;
	ActionBar actionBar;

	private RFIDWithUHF mReader;
	ScanTask rfidTask;
	boolean isStart = false;
	boolean canRfid = false;

	private Context getContext(){
		return ActivitySendReceive.this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sr);
		actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);

		key = getIntent().getExtras().getInt("key");
		if(key==1) TITLE = "收瓶扫描";
		Log.e("ActivitySendReceive", "key = " + key);
		actionBar.setTitle(TITLE);
		try {
			rList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("rList"),  new TypeToken<List<QPInfo>>(){}.getType());
			sList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("sList"),  new TypeToken<List<QPInfo>>(){}.getType());

			if(key==2){
				List<SaleDetail> SaleDetail = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("Detail"),  new TypeToken<List<SaleDetail>>(){}.getType());
				for (int i = 0; i < SaleDetail.size(); i++) {
					QPGoods qp = new QPGoods();
					qp.setMediumCode(SaleDetail.get(i).getMediumCode());
					qp.setCZJZ(SaleDetail.get(i).getCZJZ());
					qp.setGoodsNum(SaleDetail.get(i).getSendNum());
					qp.setGoodsCode(SaleDetail.get(i).getGoodsCode());
					qp.setGoodsName(SaleDetail.get(i).getGoodsName());
					qp.setIsJG(SaleDetail.get(i).getIsJG());
					qpList.add(qp);
				}
				Log.e("发", util.json.JSONUtils.toJsonWithGson(qpList));
			}else{
				List<QPGoods> mList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("Detail"),  new TypeToken<List<QPGoods>>(){}.getType());
				qpList.addAll(mList);
				Log.e("收", util.json.JSONUtils.toJsonWithGson(qpList));
			}

			String num;
			if(key==1){
				num = String.valueOf(rList.size());
			}else{
				num = String.valueOf(sList.size());
			}

			actionBar.setTitle(TITLE + "   " + num);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		initViews();
		setListener();
		new InitTask(getContext()).execute();
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
		rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());

		if(!rfid.getVersion().equals("0101")) return ;

//		if(!rfid.getCQDW().equals(getApp().getLogin().getStation())){
//			ToastUtil.showToast(getContext(), "非产权单位标签");
//			return;
//		}
		int ret = CheckQP(rfid.getQPDJCode());
		if(ret==1){
			ToastUtil.showToast(getContext(), "该气瓶已收瓶");
			return;
		}
		if(ret==2){
			ToastUtil.showToast(getContext(), "该气瓶已发瓶");
			return;
		}
		QPInfo qp = new QPInfo();

		String MediumCode = ""; String MediumName="";
		int CodeNum=0;
//		String GoodsCode=""; String GoodsName="";
		for (int i = 0; i < qpList.size(); i++) {
			if(qpList.get(i).getMediumCode().equals(rfid.getCZJZCode()) && qpList.get(i).getIsJG()==rfid.getIsJG()){
				MediumCode = qpList.get(i).getMediumCode();
				MediumName = qpList.get(i).getCZJZ();
				CodeNum++;
//				GoodsCode = qpList.get(i).getGoodsCode();
//				GoodsName = qpList.get(i).getGoodsName();
			}
		}
//
		if(CodeNum==0){
			ToastUtil.showToast(getContext(), "介质不符");
			return;
		}
//		else if(CodeNum==1){//介质+是否集格 找到唯一商品
//			qp.setLabelNo(rfid.getLabelNo());
//			qp.setCQDW(rfid.getCQDW());
//			qp.setIsJG(rfid.getIsJG());
//			qp.setQPType(GoodsCode);
//			qp.setMsg("商品名称："+ GoodsName);
//			qp.setMediumCode(MediumCode);
//			qp.setIsByHand(0);
//
//			List<QPInfo> cList = new ArrayList<QPInfo>();
//			if(key==1) cList.addAll(rList);
//			else cList.addAll(sList);
//
//			int res = isFull(qp, true, cList, true);
//			if(res==1){
//				ToastUtil.showToast(getContext(), "该商品扫描数量已满");
//				return;
//			}else if(res==2){
//				ToastUtil.showToast(getContext(), "该介质扫描数量已满");
//				return;
//			}
//
//			if(key==1){
//				qp.setOpType(0);
//				rList.add(qp);
//			}else{
//				qp.setOpType(1);
//				sList.add(qp);
//			}
//		}else{//介质+是否集格 找到多个商品
//			qp.setLabelNo(rfid.getLabelNo());
//			qp.setCQDW(rfid.getCQDW());
//			qp.setIsJG(rfid.getIsJG());
//			qp.setQPType("");
//			qp.setMsg("充装介质："+ MediumName);
//			qp.setMediumCode(MediumCode);
//			qp.setIsByHand(0);
//
//			List<QPInfo> cList = new ArrayList<QPInfo>();
//			if(key==1) cList.addAll(rList);
//			else cList.addAll(sList);
//
//			int res = isFull(qp, false, cList, false);
//			if(res==1){
//				ToastUtil.showToast(getContext(), "该商品扫描数量已满");
//				return;
//			}else if(res==2){
//				ToastUtil.showToast(getContext(), "该介质扫描数量已满");
//				return;
//			}
//			if(key==1){
//				qp.setOpType(0);
//				rList.add(qp);
//			}else{
//				qp.setOpType(1);
//				sList.add(qp);
//			}
//		}

		qp.setLabelNo(rfid.getLabelNo());
		qp.setCQDW(rfid.getCQDW());
		qp.setIsJG(rfid.getIsJG());
		qp.setQPType("");
		qp.setMsg("充装介质："+ MediumName);
		qp.setMediumCode(MediumCode);
		qp.setIsByHand(0);

		List<QPInfo> cList = new ArrayList<QPInfo>();
		if(key==1) cList.addAll(rList);
		else cList.addAll(sList);

		int res = isFull(qp, false, cList, false);
		if(res==1){
			ToastUtil.showToast(getContext(), "该商品扫描数量已满");
			return;
		}else if(res==2){
			ToastUtil.showToast(getContext(), "该介质扫描数量已满");
			return;
		}else if(res==3){
			ToastUtil.showToast(getContext(), "该商品扫描数量已超出");
			return;
		}else if(res==4){
			ToastUtil.showToast(getContext(), "该介质数量为0，无需扫描");
			return;
		}else if(res==5){
			ToastUtil.showToast(getContext(), "该商品数量为0，无需扫描");
			return;
		}
		if(key==1){
			qp.setOpType(0);
			rList.add(qp);
		}else{
			qp.setOpType(1);
			sList.add(qp);
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

			String txt = PreferenceManager.getDefaultSharedPreferences(mContext).getString("power_r", mContext.getResources().getString(R.string.config_power_r));
			int power = 30;
			try {
				power = Integer.valueOf(txt);
				if(power>30) power = 30;
				else if (power<5) power = 5;
			} catch (Exception e) {
				// TODO: handle exception
			}
			boolean pow = mReader.setPower(power);
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
				mV.btnScan.setEnabled(false);
			}else{
				ToastUtil.showToast(getContext(), "RFID设备开启");
				canRfid = true;
				mV.btnScan.setEnabled(true);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mV.btnScan.setEnabled(false);
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
		mV.btnScan = (Button) findViewById(R.id.sr_btn5);

		//初始化Spinner
		ArrayAdapter<QPGoods> orgAdapter = new ArrayAdapter<QPGoods>(getContext(),android.R.layout.simple_spinner_item, qpList);
		orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mV.good.setAdapter(orgAdapter);

		if(key==1){//收
			mV.others.setVisibility(View.GONE);
			mAdapter = new SRAdapter(getContext(), rList, 1);
			mV.lv.setAdapter(mAdapter);

		}else{//发
			mV.others.setVisibility(View.GONE);
			mAdapter = new SRAdapter(getContext(), sList, 2);
			mV.lv.setAdapter(mAdapter);
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

		String num;
		if(key==1){
			num = String.valueOf(rList.size());
		}else{
			num = String.valueOf(sList.size());
		}

		actionBar.setTitle(TITLE + "   " + num);
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

	AlertDialog mDialogChoice;
	private void DialogChoice(final Context context, final int id){
		QPInfo old;
		if(key==1) old = rList.get(id);
		else old = sList.get(id);

		final List<QPGoods> list = new ArrayList<QPGoods>();
		for (int i = 0; i < qpList.size(); i++) {
			if(qpList.get(i).getMediumCode().equals(old.getMediumCode()) && qpList.get(i).getIsJG()==old.getIsJG()){
				list.add(qpList.get(i));
			}
		}

		final int[] ChoiceID = {-1};
		final String items[] = new String[list.size()];


		for (int i = 0; i < list.size(); i++) {
			items[i] = list.get(i).getGoodsName();
			if(old.getQPType()!=null && old.getQPType().length()>0 && old.getQPType().equals(list.get(i).getGoodsCode()))
				ChoiceID[0] = i;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context,3);
		builder.setTitle("修改");
		builder.setSingleChoiceItems(items, ChoiceID[0],
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ChoiceID[0] = which;
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.e("ChoiceID[0]", "="+ChoiceID[0]);
				if(ChoiceID[0]==-1){
					ToastUtil.showToast(getContext(),  "请选择商品");
					UiUtil.setDiagBtn(dialog, false);
				}else{
					Log.e("select", list.get(ChoiceID[0]).getGoodsCode());

					if(key==1){
						rList.get(id).setQPType(list.get(ChoiceID[0]).getGoodsCode());
						rList.get(id).setMsg("商品名称："+list.get(ChoiceID[0]).getGoodsName());
					}else{
						sList.get(id).setQPType(list.get(ChoiceID[0]).getGoodsCode());
						sList.get(id).setMsg("商品名称："+list.get(ChoiceID[0]).getGoodsName());
					}
					reflishListView();
					UiUtil.setDiagBtn(dialog, true);
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UiUtil.setDiagBtn(dialog, true);
			}
		});
		builder.setNeutralButton("删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				UiUtil.setDiagBtn(dialog, true);
				if(key==1) rList.remove(id);
				else sList.remove(id);
				reflishListView();
			}
		});

		mDialogChoice = builder.create();
		mDialogChoice.show();
	}

	private void setListener(){
		mV.btnScan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isStart){
					if(mReader!=null && rfidTask!=null && rfidTask.getStatus()==AsyncTask.Status.RUNNING){
						rfidTask.cancel(true);
					}
					rfidTask = null;
					isStart = false;
					mV.btnScan.setText(getResources().getString(R.string.btn_string_12));
					mV.btnBack.setEnabled(true);
					mV.btnClear.setEnabled(true);
					mV.btnWrite.setEnabled(true);
					//校验标签
					CheckRfid();
				}else{
					ScanRfid();
				}
			}
		});


		mV.lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				deleteSelect(position);

//				QPInfo qp = (QPInfo)parent.getAdapter().getItem(position);
//				if(qp.getIsByHand()==1) deleteSelect(position);
//				else DialogChoice(getContext(), position);
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

				String lno = mV.number.getText().toString().trim();
				if(lno.length()<8){
					String txt = "";
					for(int i=0; i<(8-lno.length()); i++){
						txt += "0";
					}
					mV.number.setText(txt + lno);
				}
				int ret = CheckQP("1702"+mV.number.getText().toString().trim());
				if(ret==1){
					ToastUtil.showToast(getContext(), "该气瓶已收瓶");
					return;
				}
				if(ret==2){
					ToastUtil.showToast(getContext(), "该气瓶已发瓶");
					return;
				}
				if((QPGoods) mV.good.getAdapter().getItem(mV.good.getSelectedItemPosition()) == null){
					ToastUtil.showToast(getContext(), "请选择商品");
					return;
				}
				addQPByHand(mV.number.getText().toString().trim(), mV.good.getSelectedItemPosition());

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

	private void addQPByHand(String LabelNo, int position){
		QPGoods goodInfo = (QPGoods) mV.good.getAdapter().getItem(position);
		QPInfo qp = new QPInfo();
		qp.setCQDW("1702");
		qp.setLabelNo(LabelNo);
		qp.setQPType(goodInfo.getGoodsCode());//goodInfo.getGoodsCode()
		qp.setMsg("商品名称："+goodInfo.getGoodsName());
//		qp.setMsg("充装介质："+goodInfo.getCZJZ());
		qp.setMediumCode(goodInfo.getMediumCode());
		qp.setIsJG(goodInfo.getIsJG());
		qp.setIsByHand(1);//
		qp.setOpType(0);

		int CodeNum=0;
		for (int i = 0; i < qpList.size(); i++) {
			if(qpList.get(i).getMediumCode().equals(goodInfo.getMediumCode()) && qpList.get(i).getIsJG()==goodInfo.getIsJG()){
				CodeNum++;
			}
		}
		boolean uniqueGood = false;
		if(CodeNum==1) uniqueGood = true;

		List<QPInfo> cList = new ArrayList<QPInfo>();
		if(key==1) cList.addAll(rList);
		else cList.addAll(sList);
//		Log.e("介质："+ goodInfo.getCZJZ(), "是否唯一："+uniqueGood);
		int ret = isFull(qp, true, cList, uniqueGood);
//		int ret = isFull(qp, false, cList, false);
		if(ret==1){
			ToastUtil.showToast(getContext(), "该商品扫描数量已满");
			return;
		}else if(ret==2){
			ToastUtil.showToast(getContext(), "该介质扫描数量已满");
			return;
		}else if(ret==3){
			ToastUtil.showToast(getContext(), "该商品扫描数量已超出");
			return;
		}else if(ret==4){
			ToastUtil.showToast(getContext(), "该介质数量为0，无需扫描");
			return;
		}else if(ret==5){
			ToastUtil.showToast(getContext(), "该商品数量为0，无需扫描");
			return;
		}

		if(key==1){
			qp.setOpType(0);
			rList.add(qp);
		}else{
			qp.setOpType(1);
			sList.add(qp);
		}
		mV.number.setText("");
		setView(true);
		reflishListView();

		CheckRfid();
	}

	private int isFull(QPInfo addInfo, boolean isGood, List<QPInfo> cList, boolean uniqueGood){//0可添加 1商品已满 2介质已满 3商品超出
		//isGood是否已知商品code uniqueGood介质+集是否唯一
		int ret = 0;
		if(uniqueGood){//uniqueGood介质+集 唯一
			int num = 0;
			for (int i = 0; i < cList.size(); i++) {
				if(addInfo.getQPType().equals(cList.get(i).getQPType())) num++;
			}
			for (int i = 0; i < qpList.size(); i++) {
				if(qpList.get(i).getGoodsCode().equals(addInfo.getQPType())){
					if(qpList.get(i).getGoodsNum()==0) return 5;
					if(num<qpList.get(i).getGoodsNum()) return 0;
					else if(num==qpList.get(i).getGoodsNum()) return 1;
					else return 3;
				}
			}
		}else{//uniqueGood介质+集 不唯一
			if(isGood){//已知商品code
				int num = 0;
				for (int i = 0; i < cList.size(); i++) {
					if(addInfo.getQPType().equals(cList.get(i).getQPType())) num++;
				}
				for (int i = 0; i < qpList.size(); i++) {
					if(qpList.get(i).getGoodsCode().equals(addInfo.getQPType())){
						if(qpList.get(i).getGoodsNum()==0) return 5;
						if(num<qpList.get(i).getGoodsNum()) ret = 0;
						else return 1;
					}
				}
			}

			int sum = 0;//该介质总数
			for (int i = 0; i < qpList.size(); i++) {
				if(qpList.get(i).getMediumCode().equals(addInfo.getMediumCode())
						&& qpList.get(i).getIsJG()==addInfo.getIsJG()){
					sum += qpList.get(i).getGoodsNum();
				}
			}
			if(sum==0) return 4;

			int num = 0;//已扫描到该介质数量
			for (int i = 0; i < cList.size(); i++) {
				if(addInfo.getMediumCode().equals(cList.get(i).getMediumCode())
						&& addInfo.getIsJG()==cList.get(i).getIsJG()) num++;
			}
			Log.e("addInfo", util.json.JSONUtils.toJsonWithGson(addInfo));
			Log.e("MediumCode="+addInfo.getMediumCode(), String.valueOf(num) + " , " + String.valueOf(sum));
			if(num<sum) ret = 0;
			else return 2;
		}

		return ret;
	}


	private int CheckQP(String QPDJCode){
		int ret = 0;
		for (int i = 0; i < rList.size(); i++) {
			if(QPDJCode.equals(rList.get(i).getCQDW()+rList.get(i).getLabelNo())){
				ret = 1;
				return ret;
			}
		}

		for (int i = 0; i < sList.size(); i++) {
			if(QPDJCode.equals(sList.get(i).getCQDW()+sList.get(i).getLabelNo())){
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

	private void CheckRfid(){
		List<QPInfo> res = new ArrayList<QPInfo>();
		if(key==1){
			res.addAll(rList);
		}else{
			res.addAll(sList);
		}
		if(res.size()>0){
			Log.e("CheckRfid", util.json.JSONUtils.toJsonWithGson(res));
			new CallRfidWsTask(getContext(), this, 11).execute(util.json.JSONUtils.toJsonWithGson(res));
		}
	}

	@Override
	public void ScanRfid(){
		if(canRfid){
			if(isStart){
				//			if(mReader!=null && rfidTask!=null && rfidTask.getStatus()==AsyncTask.Status.RUNNING){
				//				rfidTask.cancel(true);
				//			}
				//			rfidTask = null;
				//			isStart = false;
				//			mV.btn1.setText(getResources().getString(R.string.btn_string_12));
				//			mV.btn3.setEnabled(true);
			}else{
				if(mReader!=null && rfidTask==null){
					isStart = true;
					mV.btnScan.setText(getResources().getString(R.string.btn_string_13));
					rfidTask = new ScanTask(myHandler);
					rfidTask.execute(mReader);
					mV.btnBack.setEnabled(false);
					mV.btnClear.setEnabled(false);
					mV.btnWrite.setEnabled(false);
				}
			}
		}
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			if(key==1){
				rList.clear();
				//reflishListView();

				List<QPInfo> res = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<QPInfo>>(){}.getType());
				rList.addAll(CheckGoods(res));
				Log.e("---", util.json.JSONUtils.toJsonWithGson(rList));
				reflishListView();
			}else{
				sList.clear();
				//reflishListView();

				List<QPInfo> res = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<QPInfo>>(){}.getType());
				sList.addAll(CheckGoods(res));
				Log.e("---", util.json.JSONUtils.toJsonWithGson(sList));
				reflishListView();
			}


		}else{
			ToastUtil.showToast(getContext(), retData);
		}
	}

	//
	private List<QPInfo> CheckGoods(List<QPInfo> list){
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getQPType()!=null && list.get(i).getQPType().length()>0){
				//找到商品
				boolean isFind = false;
				for (int j = 0; j < qpList.size(); j++) {
					if(list.get(i).getQPType().equals(qpList.get(j).getGoodsCode())){
						list.get(i).setMsg("商品名称："+qpList.get(j).getGoodsName());
						list.get(i).setColor(1);
						isFind = true;
						break;
					}
				}
				if(!isFind){
					list.get(i).setColor(2);
					for (int j = 0; j < getApp().getLogin().getGoodsList().size(); j++) {
						if(getApp().getLogin().getGoodsList().get(j).getGoodsCode().equals(list.get(i).getQPType())){
							list.get(i).setMsg("商品名称："+getApp().getLogin().getGoodsList().get(j).getGoodsName());
							break;
						}else{
							list.get(i).setMsg("商品名称：未知商品,代码("+list.get(i).getQPType()+")");
						}
					}
				}else{

				}
			}else{
				for (int j = 0; j < qpList.size(); j++) {
					if(list.get(i).getMediumCode().equals(qpList.get(j).getMediumCode())){
						list.get(i).setMsg("充装介质："+qpList.get(j).getCZJZ());
						list.get(i).setColor(0);
						break;
					}
				}
			}
		}
		return list;
	}
}
