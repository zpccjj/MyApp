package com.hsic.qp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import hsic.ui.EditDate;
import hsic.ui.HsicActivity;
import util.ToastUtil;
import util.UiUtil;

public class ActivityFull extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";

	RfidAdapter mAdapter;
	List<Rfid> rList = new ArrayList<Rfid>();
	String overDue="";

	private RFIDWithUHF mReader;
	ScanTask rfidTask;
	boolean isStart = false;

	static class mView{
		TextView full_0;
		EditDate full_1;
		EditText full_2;
		EditText full_3;
		EditText full_4;
		EditText full_5;
		Spinner full_6;
		TextView full_7;
		ListView lv;

		Button btn1;
		Button btn2;
		Button btn3;
	}
	mView mV;

	private Context getContext(){
		return ActivityFull.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full);

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
		mV.full_0 = (TextView) findViewById(R.id.full_0);
		mV.full_1 = (EditDate) findViewById(R.id.full_1);
		mV.full_2 = (EditText) findViewById(R.id.full_2);
		mV.full_3 = (EditText) findViewById(R.id.full_3);
		mV.full_4 = (EditText) findViewById(R.id.full_4);
		mV.full_5 = (EditText) findViewById(R.id.full_5);
		mV.full_6 = (Spinner) findViewById(R.id.full_6);
		mV.full_7 = (TextView) findViewById(R.id.full_7);
		mV.lv = (ListView) findViewById(R.id.full_list);

		mV.btn1 = (Button) findViewById(R.id.full_btn1);
		mV.btn2 = (Button) findViewById(R.id.full_btn2);
		mV.btn3 = (Button) findViewById(R.id.full_btn3);

		mV.full_1.setText( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) );

		mAdapter = new RfidAdapter(getContext(), rList);
		mV.lv.setAdapter(mAdapter);

		mV.full_0.setText("扫描数量:0");
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
				UiUtil.CloseKey(ActivityFull.this);
				cleanView();
			}
		});

		mV.btn3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UiUtil.CloseKey(ActivityFull.this);
				if(isStart){
					ToastUtil.showToast(getContext(), "请先停止扫描");
					return;
				}
				if(rList.size()==0){
					ToastUtil.showToast(getContext(), "请扫描标签");
					return;
				}

				if(mV.full_1.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请填写充装日期");
					return;
				}
				if(mV.full_2.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请填写充装时间");
					return;
				}
				if(mV.full_3.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请填压力");
					return;
				}
				if(mV.full_4.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请填温度");
					return;
				}
				if(mV.full_5.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请填写重量");
					return;
				}

				String DeviceSeq = getSharedPreferences("DeviceSetting", 0).getString("DeviceID", "");
				String OPID = getApp().getLogin().getUserID();
				for (int i = 0; i < rList.size(); i++) {
					rList.get(i).setDeviceSeq(DeviceSeq);
					rList.get(i).setOPID(OPID);
					rList.get(i).setFaultDm(String.valueOf(mV.full_6.getSelectedItemPosition()));
					rList.get(i).setFullDate(mV.full_1.getText().toString().trim());
					rList.get(i).setFullTime(Integer.valueOf(mV.full_2.getText().toString().trim()));
					rList.get(i).setWorkMpa(mV.full_3.getText().toString().trim());
					rList.get(i).setTemperature(mV.full_4.getText().toString().trim());
					rList.get(i).setWeight(mV.full_5.getText().toString().trim());
				}
				//	ToastUtil.showToast(getContext(), util.json.JSONUtils.toJsonWithGson(rList));

				new CallRfidWsTask(getContext(), ActivityFull.this, 4).execute(util.json.JSONUtils.toJsonWithGson(rList));
			}
		});
	}

	private void cleanView(){
		rList.clear();
//		mV.full_1.setText("");
//		mV.full_2.setText("");
//		mV.full_3.setText("");
//		mV.full_4.setText("");
//		mV.full_5.setText("");
		mV.full_7.setText("");
		overDue = "";
		reflishView(false);
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
			ToastUtil.showToast(getContext(), "无充装介质信息:"+String.valueOf(rfid.getCZJZCode()));
			return ;
		}

		if(overDue.contains(rfid.getQPDJCode())) return ;

		if(ConfigData.IsOverdue(rfid.getNextCheckDate()) == ConfigData.OVERDUE){
			overDue += rfid.getQPDJCode() + ",";
			if(overDue.length()>0) mV.full_7.setText("超期气瓶:" + overDue);
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

		mV.full_0.setText("扫描数量:" + String.valueOf(rList.size()));
	}


	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			rList.clear();
			reflishView(false);
			ToastUtil.showToast(getContext(), "充装登记成功");
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
						mV.full_7.setText("错误:"+err);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				//	mV.full_7.setText(retData);
			}
		}
	}

	@Override
	public void ScanRfid(){
		UiUtil.CloseKey(ActivityFull.this);
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
