package com.hsic.qp.sz;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.hsic.qp.sz.adapter.RfidAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;
import com.hsic.qp.sz.task.ScanTask;
import com.hsic.qp.sz.ui.SelectDialog;
import com.rscja.deviceapi.RFIDWithUHF;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.FullInfo;
import bean.MediaGoods;
import bean.QPGoods;
import bean.Rfid;
import data.ConfigData;
import hsic.ui.EditTime;
import hsic.ui.HsicActivity;
import util.ToastUtil;
import util.UiUtil;

public class ActivityFull extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";

	RfidAdapter mAdapter;
	RfidAdapter wAdapter;
	List<Rfid> rList = new ArrayList<Rfid>();
	List<Rfid> wList = new ArrayList<Rfid>();

	private RFIDWithUHF mReader;
	ScanTask rfidTask;
	boolean isStart = false;
	boolean isRfid = false;

	static class mView{
		Spinner full_a;
		//Spinner full_b;
		EditText full_b;
		TextView full_c;
		TextView full_d;

		EditTime full_1;
		EditText full_2;
		EditText full_3;
		EditText full_4;
		EditText full_5;
		Spinner full_6;
		TextView full_7;
		ListView lv;
		ScrollView sv;

		Button btn1;
		Button btn2;
		Button btn3;
		Button btn4;

		RadioGroup full_group;
		RadioButton full_rb_1;
		RadioButton full_rb_2;
	}
	mView mV;
	List<QPGoods> selectGoods = null;
	QPGoods nowGood = new QPGoods();


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
		mV.full_a = (Spinner) findViewById(R.id.full_a);
		mV.full_b = (EditText) findViewById(R.id.full_b);
		mV.full_c = (TextView) findViewById(R.id.full_c);
		mV.full_d = (TextView) findViewById(R.id.full_d);
		mV.full_1 = (EditTime) findViewById(R.id.full_1);
		mV.full_2 = (EditText) findViewById(R.id.full_2);
		mV.full_3 = (EditText) findViewById(R.id.full_3);
		mV.full_4 = (EditText) findViewById(R.id.full_4);
		mV.full_5 = (EditText) findViewById(R.id.full_5);
		mV.full_6 = (Spinner) findViewById(R.id.full_6);
		mV.full_7 = (TextView) findViewById(R.id.full_7);
		mV.lv = (ListView) findViewById(R.id.full_list);
		mV.sv = (ScrollView) findViewById(R.id.full_sv);

		mV.btn1 = (Button) findViewById(R.id.full_btn1);
		mV.btn2 = (Button) findViewById(R.id.full_btn2);
		mV.btn3 = (Button) findViewById(R.id.full_btn3);
		mV.btn4 = (Button) findViewById(R.id.full_btn4);

		mV.full_group = (RadioGroup) findViewById(R.id.full_group);
		mV.full_rb_1 = (RadioButton)findViewById(R.id.full_rb_1);
		mV.full_rb_2 = (RadioButton)findViewById(R.id.full_rb_2);

		mV.full_1.setText( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) );

		wAdapter = new RfidAdapter(getContext(), wList);
		mAdapter = new RfidAdapter(getContext(), rList);
		mV.lv.setAdapter(mAdapter);

		mV.full_rb_1.setChecked(true);

		ArrayAdapter<MediaGoods> aAdapter = new ArrayAdapter<MediaGoods>(getContext(), android.R.layout.simple_spinner_item, getApp().getMediaInfo());
		aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mV.full_a.setAdapter(aAdapter);
	}

	private void setListener(){
		mV.full_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.full_rb_1){
					mV.lv.setAdapter(mAdapter);
				}else if(checkedId == R.id.full_rb_2){
					mV.lv.setAdapter(wAdapter);
				}
			}
		});


		mV.full_a.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				UiUtil.CloseKey(ActivityFull.this);
				cleanView();

				MediaGoods mgGoods = (MediaGoods) parent.getAdapter().getItem(position);
//				ArrayAdapter<QPGoods> bAdapter = new ArrayAdapter<QPGoods>(getContext(), android.R.layout.simple_spinner_item, mgGoods.getGoods());
//				bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//				mV.full_b.setAdapter(bAdapter);
				selectGoods = mgGoods.getGoods();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

//		mV.full_b.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				UiUtil.CloseKey(ActivityFull.this);
//				cleanView();
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//
//			}
//		});
		mV.full_b.setFocusable(false);
		mV.full_b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				nowGood = new QPGoods();
				SelectDialog dialog = new SelectDialog(getContext(), mV.full_b, selectGoods, nowGood);
				dialog.show();
			}
		});


		mV.btn1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaGoods mgGoods = (MediaGoods)mV.full_a.getSelectedItem();
				if(mgGoods==null || mgGoods.getMediaCode()==null){
					ToastUtil.showToast(getContext(), "请选择充装介质");
					return;
				}

				if(mV.full_b.getText().toString().trim().length()==0 || nowGood==null){
					ToastUtil.showToast(getContext(), "请选择商品");
					return;
				}
				if(isStart){
					if(mReader!=null && rfidTask!=null && rfidTask.getStatus()==AsyncTask.Status.RUNNING){
						rfidTask.cancel(true);
					}
					rfidTask = null;
					isStart = false;
					mV.btn1.setText(getResources().getString(R.string.btn_string_12));
					mV.btn3.setEnabled(true);
					mV.full_a.setEnabled(true);
					mV.full_b.setEnabled(true);
				}else{
					ScanRfid();
				}
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

		mV.btn4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UiUtil.CloseKey(ActivityFull.this);
				if(isRfid){
					mV.btn4.setText(getResources().getString(R.string.btn_string_20));
					mV.lv.setVisibility(View.GONE);
					mV.full_group.setVisibility(View.GONE);
					mV.sv.setVisibility(View.VISIBLE);
					isRfid = false;
				}else{
					mV.btn4.setText(getResources().getString(R.string.back_string_2));
					mV.sv.setVisibility(View.GONE);
					mV.lv.setVisibility(View.VISIBLE);
					mV.full_group.setVisibility(View.VISIBLE);
					isRfid = true;
				}
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
				MediaGoods mgGoods = (MediaGoods)mV.full_a.getSelectedItem();
				if(mgGoods==null || mgGoods.getMediaCode()==null){
					ToastUtil.showToast(getContext(), "请选择充装介质");
					return;
				}
				if(mV.full_b.getText().toString().trim().length()==0 || nowGood==null){
					ToastUtil.showToast(getContext(), "请选择商品");
					return;
				}

				if(mV.full_c.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请填写充装批次号");
					return;
				}
				if(mV.full_d.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请填写充装数量");
					return;
				}
				int fullnum = 0;
				try {
					fullnum = Integer.valueOf(mV.full_d.getText().toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(fullnum==0){
					ToastUtil.showToast(getContext(), "请填写充装数量");
					return;
				}

				if(fullnum<rList.size()){
					ToastUtil.showToast(getContext(), "充装数量不可小于正确数量");
					return;
				}

//				if(rList.size()==0){
//					ToastUtil.showToast(getContext(), "请扫描标签");
//					return;
//				}

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
//				if(mV.full_5.getText().toString().trim().length()==0){
//					ToastUtil.showToast(getContext(), "请填写重量");
//					return;
//				}


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
					rList.get(i).setGoodsCode(nowGood.getGoodsCode());
				}

				FullInfo fullinfo = new FullInfo();
				fullinfo.setDeviceSeq(DeviceSeq);
				fullinfo.setOPID(OPID);
				fullinfo.setFaultDm(String.valueOf(mV.full_6.getSelectedItemPosition()));
				fullinfo.setFullDate(mV.full_1.getText().toString().trim());
				fullinfo.setFullTime(Integer.valueOf(mV.full_2.getText().toString().trim()));
				fullinfo.setWorkMpa(mV.full_3.getText().toString().trim());
				fullinfo.setTemperature(mV.full_4.getText().toString().trim());
				fullinfo.setWeight(mV.full_5.getText().toString().trim());

				DecimalFormat df = new DecimalFormat("0000");
				String fullid = new SimpleDateFormat("yyyyMMdd").format(new Date()) + df.format(Integer.parseInt(mV.full_c.getText().toString().trim()));
				fullinfo.setFullID(fullid);
				fullinfo.setFullNum(fullnum);
				fullinfo.setMediumCode(mgGoods.getMediaCode());
				fullinfo.setCZJZ(mgGoods.getMediaName());
				fullinfo.setGoodsCode(nowGood.getGoodsCode());
				fullinfo.setGoodsName(nowGood.getGoodsName());

				fullinfo.setrList(rList);

				//验证 rList 今日是否已经重装过
				Log.e("===FullInfo===", util.json.JSONUtils.toJsonWithGson(fullinfo));

				new CallRfidWsTask(getContext(), ActivityFull.this, 4).execute(util.json.JSONUtils.toJsonWithGson(fullinfo));
			}
		});
	}

	private void cleanView(){
		rList.clear();
		wList.clear();

		nowGood = new QPGoods();
		mV.full_b.setText("");

		mV.full_c.setText("");
		mV.full_d.setText("");
		mV.full_1.setText("");
		mV.full_2.setText("");
		mV.full_3.setText("");
		mV.full_4.setText("");
		mV.full_5.setText("");
		mV.full_7.setText("");
		reflishView();
	}

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

//		if(!rfid.getCQDW().equals(getApp().getLogin().getStation())){
//			ToastUtil.showToast(getContext(), "非产权单位标签");
//			return;
//		}

		for (int i = 0; i < wList.size(); i++) {
			if(wList.get(i).getQPDJCode().equals(rfid.getQPDJCode())) return;
		}
		if(rfid.getCZJZCode().equals(nowGood.getMediumCode()) && rfid.getIsJG()==nowGood.getIsJG()){
		}else{
			String Media = ConfigData.getMediaName(rfid.getCZJZCode(), getApp().getMediaInfo());
			rfid.setMediumName(Media);
			if(Media.length()==0) rfid.setNextCheckDate(null);
			wList.add(rfid);
			reflishView();
			return ;
		}

		if(ConfigData.IsOverdue(rfid.getNextCheckDate()) == ConfigData.OVERDUE){
			wList.add(rfid);
			reflishView();
			return ;
		}


		for (int i = 0; i < rList.size(); i++) {
			if(rList.get(i).getQPDJCode().equals(rfid.getQPDJCode())) return ;
		}
		rfid.setMediumName(nowGood.getCZJZ());
		rfid.setGoodsCode(nowGood.getGoodsCode());
		rfid.setGoodsName(nowGood.getGoodsName());

//		//介质代码获取名称
//		if(LastCode.equals(rfid.getCZJZCode())){
//			rfid.setMediumName(LastName);
//		}else{
//			LastCode = rfid.getCZJZCode();
//			LastName = mgGoods.getMediaName();//ConfigData.getMediaName(rfid.getCZJZCode(), getApp().getMediaInfo());
//			rfid.setMediumName(LastName);
//		}

		rList.add(rfid);
		reflishView();
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

	private void reflishView(){
		mAdapter.notifyDataSetChanged();
		wAdapter.notifyDataSetChanged();

		mV.full_rb_1.setText("正确明细("+String.valueOf(rList.size())+")");
		mV.full_rb_2.setText("错误明细("+String.valueOf(wList.size())+")");

		mV.full_7.setText("正确数量:" + String.valueOf(rList.size()) + ", 错误数量:" + String.valueOf(wList.size()) );
	}



	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			cleanView();
			ToastUtil.showToast(getContext(), "充装登记成功");
		}else{
//			try {
//				List<FHLX> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<FHLX>>(){}.getType());
//				if(list!=null && list.size()>0){
//					String err = "";
//					for (int i = 0; i < list.size(); i++) {
//						if(!list.get(i).getType().equals("成功"))
//							err = list.get(i).getNum()+"，" + list.get(i).getTagID() + "。";
//					}
//					if(err.length()>0)
//						mV.full_7.setText("存在今日已充装气瓶:"+err);
//				}
//				ToastUtil.showToast(getContext(), "充装登记失败");
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//				ToastUtil.showToast(getContext(), "充装登记失败:"+retData);
//			}
			ToastUtil.showToast(getContext(), "充装登记失败:"+retData);
		}
	}

	@Override
	public void ScanRfid(){
		UiUtil.CloseKey(ActivityFull.this);
		if(isStart){
//			if(mReader!=null && rfidTask!=null && rfidTask.getStatus()==AsyncTask.Status.RUNNING){
//				rfidTask.cancel(true);
//			}
//			rfidTask = null;
//			isStart = false;
//			mV.btn1.setText(getResources().getString(R.string.btn_string_12));
//			mV.btn3.setEnabled(true);
//			mV.full_a.setEnabled(true);
//			mV.full_b.setEnabled(true);
		}else{
			if(mReader!=null && rfidTask==null){
				isStart = true;
				mV.btn1.setText(getResources().getString(R.string.btn_string_13));
				rfidTask = new ScanTask(myHandler);
				rfidTask.execute(mReader);
				mV.btn3.setEnabled(false);
				mV.full_a.setEnabled(false);
				mV.full_b.setEnabled(false);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if(isRfid){
					mV.btn4.setText(getResources().getString(R.string.btn_string_20));
					mV.lv.setVisibility(View.GONE);
					mV.full_group.setVisibility(View.GONE);
					mV.sv.setVisibility(View.VISIBLE);
					isRfid = false;
				}else{
					finish();
				}
				break;
		}
		return true;
	}
}
