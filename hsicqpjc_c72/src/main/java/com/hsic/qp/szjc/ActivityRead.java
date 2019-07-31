package com.hsic.qp.szjc;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.RfidUtils;
import util.ToastUtil;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import bean.Rfid;

import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.RFIDWithUHF.BankEnum;

import hsic.ui.HsicActivity;

public class ActivityRead extends HsicActivity{
	private final static String MenuHOME = "标签信息浏览";
	private RFIDWithUHF mReader;
	private boolean CanRfid = false;
	private boolean CanRead = false;
	static class mView{
		TextView read_1;
		TextView read_2;
		TextView read_3;
		TextView read_4;
		TextView read_5;
		TextView read_6;
		Button read_back;
		Button read_rfid;
	}
	mView mV;
	int today;

	private Context getContext(){
		return ActivityRead.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(MenuHOME);

		initViews();
		setListener();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		today = Integer.valueOf(sdf.format(new Date()));

		new InitTask(getContext()).execute();
	}

	private void initViews(){
		mV = new mView();
		mV.read_1 = (TextView) findViewById(R.id.read_1);
		mV.read_2 = (TextView) findViewById(R.id.read_2);
		mV.read_3 = (TextView) findViewById(R.id.read_3);
		mV.read_4 = (TextView) findViewById(R.id.read_4);
		mV.read_5 = (TextView) findViewById(R.id.read_5);
		mV.read_6 = (TextView) findViewById(R.id.read_6);

		mV.read_back = (Button) findViewById(R.id.read_back);
		mV.read_rfid = (Button) findViewById(R.id.read_rfid);
	}

	private void setListener(){
		mV.read_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mV.read_rfid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScanRfid();
			}
		});
	}


	@Override
	public void ScanRfid(){
		if(CanRfid){
			if(CanRead){
				CanRead = false;
				mV.read_1.setText(""); mV.read_2.setText(""); mV.read_3.setText("");
				mV.read_4.setText(""); mV.read_5.setText(""); mV.read_6.setText("");


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
					getRfid(RfidUtils.getDataFromEPC(epc), MadeDate, GPNO, tagID);
				}else{
					ToastUtil.showToast(getContext(), "未读到标签");
					CanRead = true;
					return ;
				}
			}
		}
	}

	private void getRfid(String txt, String MadeDate, String GPNO, String TagID){
		Rfid rfid = null;

		try {
			rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(txt, Rfid.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ToastUtil.showToast(getContext(), "标签数据错误");
			CanRead = true;
			return ;
		}

		if(rfid==null ||rfid.getCQDW()==null || rfid.getLabelNo()==null){
			ToastUtil.showToast(getContext(), "未读到标签");
			CanRead = true;
			return ;
		}
		mV.read_1.setText(rfid.getQPDJCode());

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

		mV.read_2.setText(GPNO);


		int yy = Integer.valueOf(rfid.getNextCheckDate().substring(0,2));
		String YY = "20";
		if(yy>=80) YY = "19";
		String Next = YY+ rfid.getNextCheckDate().substring(0,2) + "-" + rfid.getNextCheckDate().substring(2,4);
		mV.read_3.setText(Next);
		mV.read_4.setText(MadeDate);
		if(rfid.getState()!=null){
			if(rfid.getState().equals("00")){
				mV.read_5.setText("合格");
				mV.read_5.setTextColor(Color.rgb(0, 0, 0));

			}else if(rfid.getState().equals("01")){
				mV.read_5.setText("报废");
				mV.read_5.setTextColor(Color.rgb(255, 0, 0));
			}
		}
		//超限
		int limit = Integer.valueOf(MadeDate.replace("-", "")) + 300000;
		Log.e("today="+today, "limit="+limit);
		if(today>=limit){
			mV.read_6.setText("是");
			mV.read_6.setTextColor(Color.rgb(255, 0, 0));
		}else{
			mV.read_6.setText("否");
			mV.read_6.setTextColor(Color.rgb(0, 0, 0));
		}
		util.SoundUtil.play();
		CanRead = true;
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
				mV.read_rfid.setEnabled(true);
				CanRead = true;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			//mV.btn1.setEnabled(false);
			mV.read_rfid.setEnabled(false);

			mypDialog = new ProgressDialog(mContext);
			mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mypDialog.setMessage("正在打开RFID设备...");
			mypDialog.setCanceledOnTouchOutside(false);
			mypDialog.show();
		}
	}
}
