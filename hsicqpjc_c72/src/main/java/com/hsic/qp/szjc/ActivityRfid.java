package com.hsic.qp.szjc;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.RfidUtils;
import util.ToastUtil;
import util.UiUtil;

import com.hsic.qp.szjc.adapter.RfidItemAdapter;
import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.RFIDWithUHF.BankEnum;

import bean.NewGZWFQPCheck;
import bean.ResponseData;
import bean.Rfid;
import bean.RfidItem;
import bll.sqlite.NewGZWFQPCheck_DB;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import hsic.ui.ConfirmDialog;
import hsic.ui.HsicActivity;

public class ActivityRfid extends HsicActivity{
	private final static String MenuHOME = "钢质无缝气瓶检验";
	private RFIDWithUHF mReader;
	private boolean CanRfid = false;
	private boolean CanRead = false;
	static class mView{
		TextView rfid_1;
		TextView rfid_2;
		TextView rfid_3;
		ListView rfid_lv;

		Button rfid_back;
		Button rfid_check;
	}
	mView mV;

	NewGZWFQPCheck mData;
	NewGZWFQPCheck_DB db;
	List<RfidItem> DjCodeList = new ArrayList<RfidItem>();
	RfidItemAdapter riAdapter;
	int CheckNum = 0;
	int today;

	private Context getContext(){
		return ActivityRfid.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rfid);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(MenuHOME);

		initViews();
		setListener();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		today = Integer.valueOf(sdf.format(new Date()));

		try {
			mData = (NewGZWFQPCheck) util.json.JSONUtils.toObjectWithGson(getIntent().getExtras().getString("json"), NewGZWFQPCheck.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		db = new NewGZWFQPCheck_DB(getContext());

		if(mData==null){
			ToastUtil.showToast(getContext(), "检验数据错误");
			finish();
		}else{
			new InitTask(getContext()).execute();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mReader != null) {
			boolean free = mReader.free();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ToastUtil.showToast(getContext(), "设备下电");
		}

		super.onDestroy();
	}

	private void initViews(){
		mV = new mView();
		mV.rfid_1 = (TextView) findViewById(R.id.rfid_1);
		mV.rfid_2 = (TextView) findViewById(R.id.rfid_2);
		mV.rfid_3 = (TextView) findViewById(R.id.rfid_3);
		mV.rfid_lv = (ListView) findViewById(R.id.rfid_lv);

		mV.rfid_back = (Button) findViewById(R.id.rfid_back);
		mV.rfid_check = (Button) findViewById(R.id.rfid_check);

		mV.rfid_3.setText(String.valueOf(CheckNum));
		riAdapter = new RfidItemAdapter(getContext(), DjCodeList);
		mV.rfid_lv.setAdapter(riAdapter);
	}

	private void setListener(){
		mV.rfid_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callReturn();
			}
		});

		mV.rfid_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScanRfid();
			}
		});
	}

	private void callReturn(){
		Intent resultIntent = new Intent();
		resultIntent.putExtra("Txt", "钢质无缝气瓶检验");
		this.setResult(RESULT_OK, resultIntent);
		finish();
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

			String txt = PreferenceManager.getDefaultSharedPreferences(mContext).getString("power_w", mContext.getResources().getString(R.string.config_power_w));
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
			mV.rfid_back.setEnabled(true);

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
				CanRfid = true;
				ToastUtil.showToast(getContext(), "RFID设备开启");
				mV.rfid_check.setEnabled(true);
				CanRead = true;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			//mV.btn1.setEnabled(false);
			mV.rfid_check.setEnabled(false);
			mV.rfid_back.setEnabled(false);

			mypDialog = new ProgressDialog(mContext);
			mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mypDialog.setMessage("正在打开RFID设备...");
			mypDialog.setCanceledOnTouchOutside(false);
			mypDialog.show();
		}
	}

	@Override
	public void ScanRfid(){
		if(CanRfid){
			if(CanRead){
				CanRead = false;
				mV.rfid_1.setText("");
				mV.rfid_2.setText("");

				String uii = mReader.inventorySingleTag();
				if (!TextUtils.isEmpty(uii)){
					String epc = mReader.convertUiiToEPC(uii);
					Log.e("uii", uii);
					Log.e("epc", epc);

					String tagID = mReader.readData("00000000",
							BankEnum.valueOf("UII"), 4*8, 96, epc,
							BankEnum.valueOf("TID"), 0, 6);
					if(tagID==null){
						ToastUtil.showToast(getContext(), "读取标签TagID失败");
						CanRead = true;
						return ;
					}
					if(tagID.length()!=24){
						ToastUtil.showToast(getContext(), "读取标签TagID失败");
						CanRead = true;
						return ;
					}

					Log.e("tagID", tagID);

					String User = mReader.readData("00000000",
							BankEnum.valueOf("TID"), 0, 96, tagID,
							BankEnum.valueOf("USER"),0, 13);
					if(User==null){
						ToastUtil.showToast(getContext(), "读取标签信息失败");
						CanRead = true;
						return ;
					}
					String UserX34 = RfidUtils.xorHex(User, "34");

					byte[] Data = RfidUtils.hexStringToBytes(UserX34);
					String bitString = "";
					for (int i = 0; i < Data.length; i++) {
						bitString+=RfidUtils.byteToBit(Data[i]);
					}

					//24-44
					String MadeDate = String.format("%06d", RfidUtils.binaryToDecimal(bitString.substring(24, 44)));
					int yy = Integer.valueOf(MadeDate.substring(0,2));
					String YY = "20";
					if(yy>=80) YY = "19";
					MadeDate = YY+ MadeDate.substring(0,2) + "-" + MadeDate.substring(2,4) + "-" + MadeDate.substring(4,6);

					byte[] d = RfidUtils.hexStringToBytes(UserX34.substring(14, 38));
					String GPNO = "";
					try {
						GPNO = (new String(d,"UTF-8")).replace(" ", "");
						Log.e("GPNO", GPNO);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mV.rfid_2.setText(GPNO);

					getRfid(RfidUtils.getDataFromEPC(epc), MadeDate, GPNO, tagID);
				}else{
					ToastUtil.showToast(getContext(), "未读到标签");
					CanRead = true;
					return ;
				}
			}
		}
	}

	Rfid rfid;
	private void getRfid(String txt, String MadeDate, String GPNO, String TagID){
		rfid = null;

		try {
			rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(txt, Rfid.class);
			mV.rfid_1.setText(rfid.getQPDJCode());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ToastUtil.showToast(getContext(), "标签数据错误");
			CanRead = true;
			return ;
		}

		if(rfid==null ||rfid.getCQDW()==null || rfid.getLabelNo()==null){
			mV.rfid_1.setText("");
			mV.rfid_2.setText("");
			ToastUtil.showToast(getContext(), "未读到标签");
			CanRead = true;
			return ;
		}
		if(!rfid.getCQDW().equals("1702")){
			ToastUtil.showToast(getContext(), "非申中气体电子标签");
			CanRead = true;
			return ;
		}
		if(!RfidUtils.isBound(rfid.getEPC())){
			ToastUtil.showToast(getContext(), "该电子标签未进行过绑定");
			CanRead = true;
			return ;
		}

		for (int i = 0; i < DjCodeList.size(); i++) {
			if(DjCodeList.get(i).getQPDJCODE().equals(rfid.getQPDJCode())){
				ToastUtil.showToast(getContext(), "该气瓶本次检验已完成");
				CanRead = true;
				return ;
			}
		}
		rfid.setMadeDate(MadeDate);
		rfid.setGPNO(GPNO);
		rfid.setTagID(TagID);

		if(mData.getC_ZL().equals("0")){
			//超期
			int limit = Integer.valueOf(MadeDate.replace("-", "")) + 300000;
			int next = Integer.valueOf(mData.getXCJCRQ().replace("-", ""));
			Log.e("today="+today, "limit="+limit);
			if(today>=limit){
				RfidItem ri = new RfidItem();
				ri.setQPDJCODE(rfid.getQPDJCode());
				ri.setGNO(GPNO);
				ri.setNEXTCHECKDATE(MadeDate);
				ri.setColor(1);
				ri.setMSG("超过使用年限");
				DjCodeList.add(0, ri);
				riAdapter.notifyDataSetChanged();
				ToastUtil.showToast(getContext(), "该气瓶超过使用年限");
				CanRead = true;
				return ;
			}else if(next > limit){
				Log.e("next="+next, "下次检验日期超出使用年限");
				int year = Integer.valueOf(MadeDate.substring(0, 4)) + 30;
				rfid.setLimitDate(String.valueOf(year) + MadeDate.substring(4, MadeDate.length()));
			}
		}

		//查DB
		NewGZWFQPCheck ret = db.isExist(rfid.getQPDJCode(), mData.getJCRQ());
		if(ret!=null){
			String msg = "该气瓶检验记录已存在，是否更新记录？";
			if(ret.getState().equals("1")) msg = "该气瓶检验记录已上传，是否重新记录？";
			ConfirmDialog dialog = new ConfirmDialog(getContext());
			dialog.setTitle("重复提示");
			dialog.setMessage(msg);
			dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//Task
					NewGZWFQPCheck newCheck = new  NewGZWFQPCheck();
					newCheck = util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(mData), NewGZWFQPCheck.class);
					new RfidTask(getContext(), rfid, newCheck, db, false).execute(mReader);
				}

			});
			dialog.setCancelButton(new android.content.DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					CanRead = true;
					dialog.dismiss();
				}
			});
			dialog.show();
		}else{
			//Task
			NewGZWFQPCheck newCheck = new  NewGZWFQPCheck();
			newCheck = util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(mData), NewGZWFQPCheck.class);
			new RfidTask(getContext(), rfid, newCheck, db, true).execute(mReader);
		}
	}

	public class RfidTask  extends AsyncTask<RFIDWithUHF, Void, ResponseData> {
		Context mContext;
		Rfid mRfid;
		NewGZWFQPCheck mCheck;
		NewGZWFQPCheck_DB mDb;
		boolean mIsNew;

		ProgressDialog dialog;

		public RfidTask(Context context, Rfid rfid, NewGZWFQPCheck check, NewGZWFQPCheck_DB db, boolean isNew){
			this.mContext = context;
			this.mRfid = rfid;
			this.mCheck = check;
			this.mDb = db;
			this.mIsNew = isNew;
		}

		@Override
		protected ResponseData doInBackground(RFIDWithUHF... params) {
			// TODO Auto-generated method stub
			ResponseData msg = new ResponseData();

			//写EPC
			String State = "00";
			if(mCheck.getC_ZL().equals("1")) State ="01";//(0:合格;1:报废)
			String NextCheckDate = mCheck.getXCJCRQ();
			if(mRfid.getLimitDate()!=null) NextCheckDate = mRfid.getLimitDate();
			Log.e("NextCheckDate", NextCheckDate);
			String NextCheckDateRfid = NextCheckDate.replaceAll("-", "").substring(2,6);
			BigInteger next = new BigInteger(NextCheckDateRfid, 10);

			BigInteger w = new BigInteger(RfidUtils.LeftAddString(next.toString(2), 14, "0") + State, 2);
			byte[] write = RfidUtils.hexStringToBytes(RfidUtils.LeftAddString(w.toString(16), 4, "0"));
			Log.e("write", RfidUtils.bytesToHexString(write));

			for (int i = 0; i < write.length; i++) {
				write[i] = (byte) (write[i] ^ 0x34);
			}
			try {
				boolean wepc = params[0].writeData("31064434",
						BankEnum.valueOf("TID"), 0, 96, mRfid.getTagID(),
						BankEnum.valueOf("UII"),6, 1,
						RfidUtils.bytesToHexString(write));
				Log.e("wepc", String.valueOf(wepc));
				if(!wepc){
					msg.setRespCode(1);
					msg.setRespMsg("标签写入失败！");
					return msg;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				msg.setRespCode(1);
				msg.setRespMsg("标签写入异常！");
				return msg;
			}


			mCheck.setTagID(mRfid.getTagID());
			mCheck.setGPNO(mRfid.getGPNO());
			mCheck.setMadeDate(mRfid.getMadeDate());
			mCheck.setQPDJCODE(mRfid.getQPDJCode());
			mCheck.setCZDW(mRfid.getCQDW());
			mCheck.setPropertyUnitCode(mRfid.getCQDW());
			mCheck.setIssYear(mRfid.getLabelNo().substring(0, 2));
			mCheck.setUseRegCode(mRfid.getLabelNo().substring(2, mRfid.getLabelNo().length()));
			mCheck.setMediumCode(mRfid.getCZJZCode());
			mCheck.setXCJCRQ(NextCheckDate);
			mRfid.setNextCheckDate(NextCheckDate);
			if(mIsNew){
				if(mDb.insertCheck(mCheck)){
					msg.setRespCode(0);
					msg.setRespMsg("添加检验记录成功");
				}else{
					msg.setRespCode(1);
					msg.setRespMsg("添加检验记录失败");
				}
			}else{
				if(mDb.updateCheck(mCheck)){
					msg.setRespCode(0);
					msg.setRespMsg("更新检验记录成功");
				}else{
					msg.setRespCode(1);
					msg.setRespMsg("更新检验记录失败");
				}
			}

			return msg;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			dialog = new ProgressDialog(mContext);
			dialog.setMessage("请勿移动，正在写标签...");
			dialog.setCancelable(false);
			dialog.show();

			mV.rfid_check.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(ResponseData result) {
			// TODO Auto-generated method stub
			mV.rfid_check.setEnabled(true);
			dialog.setCancelable(true);

			if(result.getRespCode()==0){
				dialog.dismiss();
				ToastUtil.showToast(getContext(), result.getRespMsg());

				util.SoundUtil.play();

				RfidItem ri = new RfidItem();
				ri.setQPDJCODE(mRfid.getQPDJCode());
				ri.setGNO(mRfid.getGPNO());
				ri.setNEXTCHECKDATE(mRfid.getNextCheckDate());
				if(mData.getC_ZL().equals("0")){
					if(mRfid.getLimitDate()!=null){
						ri.setColor(2);
						ri.setMSG("已检验,下检超限");
					}else{
						ri.setColor(0);
						ri.setMSG("已检验");
					}
				}else{
					ri.setMSG("已检验");
					ri.setColor(9);
				}
				DjCodeList.add(0, ri);
				CheckNum ++;
				riAdapter.notifyDataSetChanged();
				mV.rfid_3.setText(String.valueOf(CheckNum));

			}else{
				dialog.setMessage("失败："+result.getRespMsg());
				UiUtil.CloseDiag(dialog);
			}
			CanRead = true;
			super.onPostExecute(result);
		}
	}
}
