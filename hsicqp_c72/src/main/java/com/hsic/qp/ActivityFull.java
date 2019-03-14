package com.hsic.qp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.ToastUtil;
import util.UiUtil;
import hsic.ui.EditDate;
import hsic.ui.HsicActivity;

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
import bean.FHLX;
import bean.InfoItem;
import bean.Rfid;

import com.google.gson.reflect.TypeToken;
import com.hsic.qp.ActivityTruckInOut.mView;
import com.hsic.qp.adapter.RfidAdapter;
import com.hsic.qp.listener.WsListener;
import com.hsic.qp.task.CallRfidWsTask;
import com.hsic.qp.task.ScanTask;
import com.rscja.deviceapi.RFIDWithUHF;

import data.ConfigData;

public class ActivityFull extends HsicActivity implements WsListener{
	private final static String MenuHOME = "����";

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
			ToastUtil.showToast(getContext(), "�豸�µ�");
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
		
		mV.full_0.setText("ɨ������:0");
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
					ToastUtil.showToast(getContext(), "����ֹͣɨ��");
					return;
				}
				if(rList.size()==0){
					ToastUtil.showToast(getContext(), "��ɨ���ǩ");
					return;
				}
				
				if(mV.full_1.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "����д��װ����");
					return;
				}
				if(mV.full_2.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "����д��װʱ��");
					return;
				}
				if(mV.full_3.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "����ѹ��");
					return;
				}
				if(mV.full_4.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "�����¶�");
					return;
				}
				if(mV.full_5.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "����д����");
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
			ToastUtil.showToast(getContext(), "�ǲ�Ȩ��λ��ǩ");
			return;
		}
		String MediaName = ConfigData.getMediaName(rfid.getCZJZCode());
		if(MediaName.length()==0) {
			ToastUtil.showToast(getContext(), "�޳�װ������Ϣ:"+String.valueOf(rfid.getCZJZCode()));
			return ;
		}
		
		if(overDue.contains(rfid.getQPDJCode())) return ;
		
		if(ConfigData.IsOverdue(rfid.getNextCheckDate()) == ConfigData.OVERDUE){
			overDue += rfid.getQPDJCode() + ",";
			if(overDue.length()>0) mV.full_7.setText("������ƿ:" + overDue);
			return ;
		}
		
		
		for (int i = 0; i < rList.size(); i++) {
			if(rList.get(i).getQPDJCode().equals(rfid.getQPDJCode())) return ;
		}
		
		//���ʴ����ȡ����
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
	 * �豸�ϵ��첽��
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
			
			boolean pow = mReader.setPower(30);
			if(!pow) return 3;
			
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			mypDialog.cancel();
			
			if (result!=0){
				String txt="�豸��ʧ�ܣ�";
				switch (result) {
				case 1:
					txt+="��ʼ��ʧ��";
					break;
				case 2:
					txt+="�ϵ�ʧ��";
					break;
				case 3:
					txt+="����Ƶ��ʧ��";
					break;
				}
				ToastUtil.showToast(getContext(), txt);
			}else{
				ToastUtil.showToast(getContext(), "RFID�豸����");
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
			mypDialog.setMessage("���ڴ�RFID�豸...");
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
		
		mV.full_0.setText("ɨ������:" + String.valueOf(rList.size()));
	}
	
	
	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			rList.clear();
			reflishView(false);
			ToastUtil.showToast(getContext(), "��װ�Ǽǳɹ�");
		}else{
			rList.clear();
			reflishView(false);
			
			try {
				List<FHLX> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<FHLX>>(){}.getType());
				if(list!=null && list.size()>0){
					String err = "";
					for (int i = 0; i < list.size(); i++) {
						if(!list.get(i).getType().equals("�ɹ�"))
							err += list.get(i).getType()+":"+list.get(i).getNum()+"��" + list.get(i).getTagID() + "��";
					}
					if(err.length()>0)
						mV.full_7.setText("����:"+err);
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
