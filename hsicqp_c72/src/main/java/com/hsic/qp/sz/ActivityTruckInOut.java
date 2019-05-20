package com.hsic.qp.sz;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hsic.qp.sz.adapter.RfidAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;
import com.hsic.qp.sz.task.ScanTask;
import com.rscja.deviceapi.RFIDWithUHF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.FHLX;
import bean.InfoItem;
import bean.MediumStatistics;
import bean.Rfid;
import bean.TruckNoInfo;
import data.ConfigData;
import hsic.ui.ConfirmDialog;
import hsic.ui.HsicActivity;
import util.ToastUtil;

public class ActivityTruckInOut extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";

	static class mView{
		TextView truck_1;
		Spinner truck_2;
		TextView truck_3;
		TextView truck_4;
		TextView truck_5;
		TextView truck_6;
		TextView truck_7;
		ListView lv;

		Button btn1;
		Button btn2;
		Button btn3;
	}
	mView mV;
	int IO=0;

	RfidAdapter mAdapter;
	List<Rfid> rList = new ArrayList<Rfid>();
	String overDue="";

	private RFIDWithUHF mReader;
	ScanTask rfidTask;
	boolean isStart = false;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Context getContext(){
		return ActivityTruckInOut.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_truckinout);

		IO = getIntent().getExtras().getInt("IO");

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		intiView();
		setListener();

		new InitTask(getContext()).execute();

		new CallRfidWsTask(getContext(), ActivityTruckInOut.this, 1).execute(getApp().getLogin().getStation());
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
		mV.truck_1 = (TextView) findViewById(R.id.truck_1);
		mV.truck_2 = (Spinner) findViewById(R.id.truck_2);
		mV.truck_3 = (TextView) findViewById(R.id.truck_3);
		mV.truck_4 = (TextView) findViewById(R.id.truck_4);
		mV.truck_5 = (TextView) findViewById(R.id.truck_5);
		mV.truck_6 = (TextView) findViewById(R.id.truck_6);
		mV.truck_7 = (TextView) findViewById(R.id.truck_7);
		mV.lv = (ListView) findViewById(R.id.truck_list);

		mV.btn1 = (Button) findViewById(R.id.truck_btn1);
		mV.btn2 = (Button) findViewById(R.id.truck_btn2);
		mV.btn3 = (Button) findViewById(R.id.truck_btn3);

		if(IO!=0){
			mV.truck_1.setText(getResources().getString(R.string.txt_home_2));
			mV.truck_7.setVisibility(View.GONE);
		}

		mV.truck_3.setText(getApp().getLogin().getUserName()!=null ? getApp().getLogin().getUserName() : "");

		mV.truck_4.setText(getResources().getString(R.string.txt_home_9)+0);
		mV.truck_5.setText(getResources().getString(R.string.txt_home_10)+"0 / 0");


		mAdapter = new RfidAdapter(getContext(), rList);
		mV.lv.setAdapter(mAdapter);
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
				rList.clear();
				mV.truck_6.setText("");
				reflishView(false);
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
				if(((InfoItem) mV.truck_2.getSelectedItem())==null){
					ToastUtil.showToast(getContext(), "请选择车辆");
					return;
				}
//				if(IO==0){
//					if(getOverdueInfo()[0] == rList.size()){
//						ToastUtil.showToast(getContext(), "所有气瓶都超期");
//						return;
//					}
//				}

				String DeviceSeq = getSharedPreferences("DeviceSetting", 0).getString("DeviceID", "");
				String OPID = getApp().getLogin().getUserID();
				String License = ((InfoItem) mV.truck_2.getSelectedItem()).getKey();
				for (int i = 0; i < rList.size(); i++) {
					rList.get(i).setDeviceSeq(DeviceSeq);
					rList.get(i).setOPID(OPID);
					rList.get(i).setLicense(License);
				}

//				if(IO==0){
//					OverdueMsg = getOverdueMsg();
//					if(OverdueMsg.length()>0){
//						submitRfid();
//						return;
//					}else{
//
//						OverdueMsg = "可出厂数量:" + rList.size();
//					}
//				}

				new CallRfidWsTask(getContext(), ActivityTruckInOut.this, IO==0 ? 2 : 3).execute(util.json.JSONUtils.toJsonWithGson(rList));
			}
		});

		mV.lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				deleteRfid(position);
			}
		});
	}

	private void deleteRfid(final int id){
		ConfirmDialog dialog = new ConfirmDialog(getContext());
		dialog.setTitle("删除提示");
		dialog.setMessage("确定删除标签 "+ rList.get(id).getLabelNo());
		dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				rList.remove(id);
				reflishView(true);
			}

		});
		dialog.show();
	}

	private void submitRfid(){
		ConfirmDialog dialog = new ConfirmDialog(getContext());
		dialog.setTitle("提示");
		dialog.setMessage("注意：存在超期气瓶");
		dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//            	List<Rfid> list = new ArrayList<Rfid>();
//            	for (int i = 0; i < rList.size(); i++) {
//        			if(rList.get(i)!=null && rList.get(i).getNextCheckDate()!=null){
//        				int ret = ConfigData.IsOverdue(rList.get(i).getNextCheckDate());
//        				if(ret != ConfigData.OVERDUE)
//        					list.add(rList.get(i));
//        			}
//        		}
//            	OverdueMsg = "可出厂数量:" + list.size() + ", "+OverdueMsg;
//            	mV.truck_7.setText(OverdueMsg);
//
//            	new CallRfidWsTask(getContext(), ActivityTruckInOut.this, 2).execute(util.json.JSONUtils.toJsonWithGson(list));
			}

		});
		dialog.show();
	}

	String LastCode="";
	String LastName="";
	@Override
	public void getRFID(String txt) {
		// TODO Auto-generated method stub
		super.getRFID(txt);
		Rfid rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(txt, Rfid.class);
		rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());
		if(!rfid.getVersion().equals("0101")){
			//	ToastUtil.showToast(getContext(), "版本号错误");
			return;
		}

		if(!rfid.getCQDW().equals(getApp().getLogin().getStation())){
			ToastUtil.showToast(getContext(), "非产权单位标签");
			return;
		}
		String MediaName = ConfigData.getMediaName(rfid.getCZJZCode());
		if(MediaName.length()==0) {
			ToastUtil.showToast(getContext(), "无充装介质信息");
			return ;
		}
		if(IO==0){
			if(overDue.contains(rfid.getQPDJCode()))
				return ;

			if(ConfigData.IsOverdue(rfid.getNextCheckDate()) == ConfigData.OVERDUE){
				overDue += rfid.getQPDJCode() + ",";
				if(overDue.length()>0) mV.truck_7.setText("超期气瓶:" + overDue);
				return ;
			}
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

		if(IO==0){
			rfid.setSendDate(sdf.format(new Date()));
		}else{
			rfid.setReceiveDate(sdf.format(new Date()));
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

			boolean pow = mReader.setPower(30);
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

		mV.truck_4.setText(getResources().getString(R.string.txt_home_9)+ rList.size());

		int[] overdue = getOverdueInfo();
		mV.truck_5.setText(getResources().getString(R.string.txt_home_10)+ overdue[0] + " / " + overdue[1]);

		mV.truck_6.setText(getMediumInfo());
	}

	private String getMediumInfo(){
		String info = "";

		List<MediumStatistics> mlist = new ArrayList<MediumStatistics>();

		for (int i = 0; i < rList.size(); i++) {
			boolean isNew = true;
			for (int j = 0; j < mlist.size(); j++) {
				if(mlist.get(j).getMediumCode().equals(rList.get(i).getCZJZCode())){
					isNew = false;
					mlist.get(j).setMediumNameCount(mlist.get(j).getMediumNameCount()+1);
					break;
				}
			}
			if(isNew){
				MediumStatistics ms = new MediumStatistics();
				ms.setMediumCode(rList.get(i).getCZJZCode());
				ms.setMediumName(rList.get(i).getMediumName());
				ms.setMediumNameCount(1);
				mlist.add(ms);
			}
		}

		for (int i = 0; i < mlist.size(); i++) {
			info += mlist.get(i).getMediumName()+":"+String.valueOf( mlist.get(i).getMediumNameCount()) + ",";
		}

		return info;
	}

	private int[] getOverdueInfo(){
		int[] ret = {0, 0};

		for (int i = 0; i < rList.size(); i++) {
			if(rList.get(i)!=null && rList.get(i).getNextCheckDate()!=null){
				int res = ConfigData.IsOverdue(rList.get(i).getNextCheckDate());
				if(res == ConfigData.OVERDUE) ret[0]++;
				else if(res == ConfigData.FORTHCOMING) ret[1]++;
			}
		}

		return ret;
	}

	private String getOverdueMsg(){
		int overdue = 0;
		String txt = "";
		for (int i = 0; i < rList.size(); i++) {
			if(rList.get(i)!=null && rList.get(i).getNextCheckDate()!=null){
				int ret = ConfigData.IsOverdue(rList.get(i).getNextCheckDate());
				if(ret == ConfigData.OVERDUE){
					txt+=rList.get(i).getQPDJCode() + ",";
					overdue++;
				}
			}
		}
		if(overdue==0)
			return "";
		else return "超期数量:" + overdue + " , 明细:" + txt;
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if( isSuccess){
			if(code==1){
				List<TruckNoInfo> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<TruckNoInfo>>(){}.getType());
				List<InfoItem> items = new ArrayList<InfoItem>();
				for (int i = 0; i < list.size(); i++) {
					InfoItem info = new InfoItem();
					info.setKey(list.get(i).getTruckID());
					info.setName(list.get(i).getLicense());
					items.add(info);
				}
				ArrayAdapter<InfoItem> orgAdapter = new ArrayAdapter<InfoItem>(getContext(),android.R.layout.simple_spinner_item, items);
				orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mV.truck_2.setAdapter(orgAdapter);
			}else{
				rList.clear();
				reflishView(false);
				mV.truck_6.setText("");
				ToastUtil.showToast(getContext(), code==2 ? "出厂登记成功" : "进场登记成功");
			}
		}else{
			if(code==2 || code==3){
				rList.clear();
				reflishView(false);
//				OverdueMsg = OverdueMsg + "\n" + retData;

				try {
					List<FHLX> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<FHLX>>(){}.getType());
					if(list!=null && list.size()>0){
						String err = "";
						for (int i = 0; i < list.size(); i++) {
							if(!list.get(i).getType().equals("成功"))
								err += list.get(i).getType()+":"+list.get(i).getNum()+"，" + list.get(i).getTagID() + "。";
						}
						if(err.length()>0)
							mV.truck_6.setText("错误:"+err);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					//	mV.truck_6.setText(retData);
				}
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
