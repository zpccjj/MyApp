package com.hsic.qp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hsic.qp.adapter.RfidAdapter;
import com.hsic.qp.listener.WsListener;
import com.hsic.qp.task.CallRfidWsTask;
import com.hsic.qp.task.ScanTask;
import com.rscja.deviceapi.RFIDWithUHF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.FHLX;
import bean.Rfid;
import data.ConfigData;
import hsic.ui.HsicActivity;
import util.ToastUtil;

public class ActivityCheck extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";

	RfidAdapter mAdapter;
	List<Rfid> rList = new ArrayList<Rfid>();
	String overDue="";

	private RFIDWithUHF mReader;
	ScanTask rfidTask;
	boolean isStart = false;

	static class mView{
		TextView check_0;
		Spinner check_1;
		TextView check_2;
		ListView lv;

		Button btn1;
		Button btn2;
		Button btn3;
	}
	mView mV;

	private Context getContext(){
		return ActivityCheck.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		intiView();
		setListener();

		new InitTask(getContext()).execute();
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

	private void intiView(){
		mV = new mView();
		mV.check_0 = (TextView) findViewById(R.id.check_0);
		mV.check_1 = (Spinner) findViewById(R.id.check_1);
		mV.check_2 = (TextView) findViewById(R.id.check_2);
		mV.lv = (ListView) findViewById(R.id.check_list);

		mV.btn1 = (Button) findViewById(R.id.check_btn1);
		mV.btn2 = (Button) findViewById(R.id.check_btn2);
		mV.btn3 = (Button) findViewById(R.id.check_btn3);

		mAdapter = new RfidAdapter(getContext(), rList);
		mV.lv.setAdapter(mAdapter);

		mV.check_0.setText("扫描数量:0");
	}

	private void setListener(){
		mV.btn1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScanRfid();
			}
		});
		mV.btn2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cleanView();
			}
		});

		mV.btn3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isStart){
					ToastUtil.showToast(getContext(), "请先停止扫描");
					return;
				}
				if(rList.size()==0){
					ToastUtil.showToast(getContext(), "请扫描标签");
					return;
				}

				String DeviceSeq = getSharedPreferences("DeviceSetting", 0).getString("DeviceID", "");
				String OPID = getApp().getLogin().getUserID();
				for (int i = 0; i < rList.size(); i++) {
					rList.get(i).setDeviceSeq(DeviceSeq);
					rList.get(i).setOPID(OPID);
					rList.get(i).setFaultDm(String.valueOf(mV.check_1.getSelectedItemPosition()));
				}

				new CallRfidWsTask(getContext(), ActivityCheck.this, 5).execute(util.json.JSONUtils.toJsonWithGson(rList));
			}
		});
	}

	String LastCode="";
	String LastName="";
	@Override
	public void getRFID(String txt) {
		// TODO Auto-generated method stub
		super.getRFID(txt);

		Rfid rfid;

		try {
			rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(txt, Rfid.class);
		} catch (Exception e) {
			// TODO: handle exception
			return ;
		}
		if(rfid==null ||rfid.getCQDW()==null || rfid.getLabelNo()==null) return ;
		rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());

		if(!rfid.getVersion().equals("0101")) return ;

		if(!rfid.getCQDW().equals(getApp().getLogin().getStation())){
			ToastUtil.showToast(getContext(), "非产权单位标签");
			return;
		}
		String MediaName = ConfigData.getMediaName(rfid.getCZJZCode());
		if(MediaName.length()==0) {
			ToastUtil.showToast(getContext(), "无充装介质信息");
			return ;
		}

		if(overDue.contains(rfid.getQPDJCode())) return ;

		if(ConfigData.IsOverdue(rfid.getNextCheckDate()) == ConfigData.OVERDUE){
			overDue += rfid.getQPDJCode() + ",";
			if(overDue.length()>0) mV.check_2.setText("超期气瓶:" + overDue);
			return ;
		}


		for (int i = 0; i < rList.size(); i++) {
			if(rList.get(i).getQPDJCode().equals(rfid.getQPDJCode())) return ;
		}

		//介质代码获取名称
		if(LastCode.equals(rfid.getCZJZCode())){
			rfid.setMediumName(LastName);
		}else{
			LastCode = rfid.getCZJZCode();
			LastName = ConfigData.getMediaName(rfid.getCZJZCode());
			rfid.setMediumName(LastName);
		}
		rfid.setCheckDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		rList.add(rfid);
		reflishView(false);
		util.SoundUtil.play();
	}

	@Override
	public void closeRFID(){
		super.closeRFID();
		rfidTask = null;
		isStart = false;
		mV.btn1.setText(getResources().getString(R.string.btn_string_12));
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

			boolean pow = mReader.setPower(30);//5-30
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
				mV.btn1.setEnabled(true);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			mV.btn1.setEnabled(false);

			mypDialog = new ProgressDialog(mContext);
			mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mypDialog.setMessage("正在打开RFID设备...");
			mypDialog.setCanceledOnTouchOutside(false);
			mypDialog.show();
		}
	}



	private void reflishView(boolean isDelete){
		if(isDelete){
			mAdapter = new RfidAdapter(getContext(), rList);
			mV.lv.setAdapter(mAdapter);
		}else
			mAdapter.notifyDataSetChanged();

		mV.check_0.setText("扫描数量:" + String.valueOf(rList.size()));
	}

	private void cleanView(){
		rList.clear();
		mV.check_2.setText("");
		overDue = "";
		reflishView(false);
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			rList.clear();
			reflishView(false);
			ToastUtil.showToast(getContext(), "上传气瓶检验信息成功");
		}else{
			rList.clear();
			reflishView(false);

			try {
				List<FHLX> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<FHLX>>(){}.getType());
				if(list!=null && list.size()>0){
					String err = "";
					for (int i = 0; i < list.size(); i++) {
						if(!list.get(i).getType().equals("成功"))
							err += list.get(i).getType()+":"+list.get(i).getNum()+"，" + list.get(i).getTagID() + "。";
					}
					if(err.length()>0)
						mV.check_2.setText("错误:"+err);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mV.check_2.setText(retData);
			}
		}
	}

	@Override
	public void ScanRfid(){
		if(isStart){
			if(mReader!=null && rfidTask!=null && rfidTask.getStatus()==AsyncTask.Status.RUNNING){
				rfidTask.cancel(true);
			}
			rfidTask = null;
			isStart = false;
			mV.btn1.setText(getResources().getString(R.string.btn_string_12));
		}else{
			if(mReader!=null && rfidTask==null){
				isStart = true;
				mV.btn1.setText(getResources().getString(R.string.btn_string_13));
				rfidTask = new ScanTask(myHandler);
				rfidTask.execute(mReader);
			}
		}
	}

}
