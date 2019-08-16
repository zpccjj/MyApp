package com.hsic.qp.sz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.ToastUtil;
import bean.FHLX;
import bean.MediumStatistics;
import bean.QPInfo;
import bean.Rfid;
import bean.TruckGoods;
import bean.TruckQPInfo;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.reflect.TypeToken;
import com.hsic.qp.sz.adapter.RfidAdapter;
import com.hsic.qp.sz.adapter.TruckGoodsAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;
import com.hsic.qp.sz.task.ScanTask;
import com.rscja.deviceapi.RFIDWithUHF;
import data.ConfigData;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import hsic.ui.ConfirmDialog;
import hsic.ui.HsicActivity;

public class ActivityTruckInOut extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";

	static class mView{
		LinearLayout truck_ll;
		TextView truck_1;
		Spinner truck_2;
		TextView truck_3;
		TextView truck_4;
		TextView truck_5;
		TextView truck_6;
		TextView truck_7;
		ListView qplv;
		ListView lv;

		Button btn1;
		Button btn2;
		Button btn3;
		Button btn4;

		RadioGroup truck_group;
		RadioButton truck_rb_1;
		RadioButton truck_rb_2;
	}
	mView mV;
	int IO=0;//出0 进1
	boolean isRfid = false;
	boolean canRfid = false;

	List<TruckGoods> GoodsList = new ArrayList<TruckGoods>();
	TruckGoodsAdapter tAdapter;

	RfidAdapter rAdapter;
	List<Rfid> rList = new ArrayList<Rfid>();
	RfidAdapter wAdapter;
	List<Rfid> wList = new ArrayList<Rfid>();

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
		mV.truck_ll = (LinearLayout) findViewById(R.id.truck_ll);
		mV.truck_1 = (TextView) findViewById(R.id.truck_1);
		mV.truck_2 = (Spinner) findViewById(R.id.truck_2);
		mV.truck_3 = (TextView) findViewById(R.id.truck_3);
		mV.truck_4 = (TextView) findViewById(R.id.truck_4);
		mV.truck_5 = (TextView) findViewById(R.id.truck_5);
		mV.truck_6 = (TextView) findViewById(R.id.truck_6);
		mV.truck_7 = (TextView) findViewById(R.id.truck_7);
		mV.lv = (ListView) findViewById(R.id.truck_list);
		mV.qplv = (ListView) findViewById(R.id.truckqp_list);

		mV.btn1 = (Button) findViewById(R.id.truck_btn1);
		mV.btn2 = (Button) findViewById(R.id.truck_btn2);
		mV.btn3 = (Button) findViewById(R.id.truck_btn3);
		mV.btn4 = (Button) findViewById(R.id.truck_btn4);

		mV.truck_group = (RadioGroup) findViewById(R.id.truck_group);
		mV.truck_rb_1 = (RadioButton)findViewById(R.id.truck_rb_1);
		mV.truck_rb_2 = (RadioButton)findViewById(R.id.truck_rb_2);

		if(IO!=0){
			mV.truck_1.setText(getResources().getString(R.string.txt_home_2));
		}

		mV.truck_3.setText(getApp().getLogin().getUserName()!=null ? getApp().getLogin().getUserName() : "");

		mV.truck_4.setText(getResources().getString(R.string.txt_home_9)+0);
		mV.truck_5.setText(getResources().getString(R.string.txt_home_10)+"0 / 0");

		wAdapter = new RfidAdapter(getContext(), wList);
		rAdapter = new RfidAdapter(getContext(), rList);
		mV.lv.setAdapter(rAdapter);

		tAdapter = new TruckGoodsAdapter(getContext(), GoodsList, IO);
		mV.qplv.setAdapter(tAdapter);

		mV.truck_rb_1.setChecked(true);
	}

	private void setListener(){
		mV.truck_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.truck_rb_1){
					mV.lv.setAdapter(rAdapter);
				}else if(checkedId == R.id.truck_rb_2){
					mV.lv.setAdapter(wAdapter);
				}
			}
		});


		mV.truck_2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				TruckQPInfo tInfo = (TruckQPInfo) parent.getAdapter().getItem(position);

				GoodsList = tInfo.getQPGoods();
				tAdapter = new TruckGoodsAdapter(getContext(), GoodsList, IO);
				mV.qplv.setAdapter(tAdapter);
				Log.e("GoodsList===", util.json.JSONUtils.toJsonWithGson(GoodsList));
				rList.clear();
				reflishView(false);

				if(GoodsList==null || GoodsList.size()==0){
					mV.btn1.setEnabled(false);
					mV.btn2.setEnabled(false);
					mV.btn3.setEnabled(false);
					mV.btn4.setEnabled(false);
					ToastUtil.showToast(getContext(), "无气瓶信息");
				}else{
					mV.btn1.setEnabled(canRfid);
					mV.btn2.setEnabled(true);
					mV.btn3.setEnabled(true);
					mV.btn4.setEnabled(true);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		mV.qplv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				setNum(position, IO);
			}
		});

		mV.btn1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isStart){
					if(mReader!=null && rfidTask!=null && rfidTask.getStatus()==AsyncTask.Status.RUNNING){
						rfidTask.cancel(true);
					}
					rfidTask = null;
					isStart = false;
					mV.btn1.setText(getResources().getString(R.string.btn_string_12));
					mV.btn3.setEnabled(true);

					CheckRfid();
				}else{
					ScanRfid();
				}
			}

		});

		mV.btn2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clean();
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

				TruckQPInfo tInfo = (TruckQPInfo) mV.truck_2.getSelectedItem();
				if(tInfo==null){
					ToastUtil.showToast(getContext(), "请选择车辆");
					return;
				}
//				if(IO==0){
//					if(getOverdueInfo()[0] == rList.size()){
//						ToastUtil.showToast(getContext(), "所有气瓶都超期");
//						return;
//					}
//				}
				if(wList.size()>0){
					ConfirmDialog dialog = new ConfirmDialog(getContext());
					dialog.setTitle("提示");
					dialog.setMessage("存在错误的气瓶标签，是否继续提交？");
					dialog.setConfirmButtonWithTxt("继续",new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							CheckGoods();
						}

					});
					dialog.show();
					return;
				}
				CheckGoods();
//				if(rList.size()>getNumOfQP(true)){
//					ToastUtil.showToast(getContext(), "扫描数超出登记总数");
//					return;
//				}
			}
		});

		mV.btn4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isRfid){
					isRfid = false;
					mV.btn4.setText(getResources().getString(R.string.btn_string_20));
					mV.truck_group.setVisibility(View.GONE);
					mV.lv.setVisibility(View.GONE);
					mV.truck_ll.setVisibility(View.VISIBLE);
					mV.qplv.setVisibility(View.VISIBLE);
				}else{
					isRfid = true;
					mV.btn4.setText(getResources().getString(R.string.back_string_2));
					mV.truck_group.setVisibility(View.VISIBLE);
					mV.lv.setVisibility(View.VISIBLE);
					mV.truck_ll.setVisibility(View.GONE);
					mV.qplv.setVisibility(View.GONE);
				}
			}
		});

		mV.lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				if(mV.truck_rb_1.isChecked())
					deleteRfid(position);
			}
		});
	}

	private void Subimt(){
		TruckQPInfo tInfo = (TruckQPInfo) mV.truck_2.getSelectedItem();
		String DeviceSeq = getSharedPreferences("DeviceSetting", 0).getString("DeviceID", "");
		String OPID = getApp().getLogin().getUserID();
		String License = tInfo.getLicense();
		for (int i = 0; i < rList.size(); i++) {
			rList.get(i).setDeviceSeq(DeviceSeq);
			rList.get(i).setOPID(OPID);
			rList.get(i).setLicense(License);
		}
		tInfo.setOPID(OPID);
		tInfo.setRfidList(rList);
		Log.e((IO==0 ? "出" : "进"), util.json.JSONUtils.toJsonWithGson(tInfo));

		new CallRfidWsTask(getContext(), ActivityTruckInOut.this, IO==0 ? 2 : 3).execute(util.json.JSONUtils.toJsonWithGson(tInfo));
	}

	private void CheckGoods(){
		String ret = "";

		for (int i = 0; i < GoodsList.size(); i++) {
			int goodnum = GoodsList.get(i).getGoodsNum() + GoodsList.get(i).getEmptyNum();
			int sum = 0;
			for (int j = 0; j < rList.size(); j++) {
				if(rList.get(j).getGoodsCode()!=null && rList.get(j).getGoodsCode().length()>0
						&& rList.get(j).getGoodsCode().equals(GoodsList.get(i).getGoodsCode())){
					sum++;
				}
			}

			if(goodnum!=sum){
				if(ret.length()>0) ret+="、";
				ret += GoodsList.get(i).getGoodsName();
			}
		}
		if(ret.length()>0){
			ret = "错误提示："+ret+"，登记数量与扫描数量不相符";
			ConfirmDialog dialog = new ConfirmDialog(getContext());
			dialog.setTitle("存在不符情况，是否继续");
			dialog.setMessage(ret);
			dialog.setConfirmButtonWithTxt("继续",new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Subimt();
				}

			});
			dialog.show();
		}else{
			Subimt();
		}
	}

	private void clean(){
		rList.clear();
		wList.clear();
		reflishView(false);
		mV.truck_6.setText("");
		mV.truck_7.setText("");
	}

	private AlertDialog numAlertDialog;
	private void setNum(final int id, final int io){
		Log.e("setNum", String.valueOf(id) +","+String.valueOf(io));
		AlertDialog.Builder builder = new Builder(getContext());

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

		final View modifyView = inflater.inflate(R.layout.diag_io,null);
		TextView diag_io_1 = (TextView) modifyView.findViewById(R.id.diag_io_1);
		TextView diag_io_1s = (TextView) modifyView.findViewById(R.id.diag_io_1s);
		final EditText diag_io_2 = (EditText) modifyView.findViewById(R.id.diag_io_2);
		TableRow diag_io_tr = (TableRow) modifyView.findViewById(R.id.diag_io_tr);
		final EditText diag_io_3 = (EditText) modifyView.findViewById(R.id.diag_io_3);
		Button diag_io_btn1 = (Button) modifyView.findViewById(R.id.diag_io_btn1);
		Button diag_io_btn2 = (Button) modifyView.findViewById(R.id.diag_io_btn2);
		final TextView ps = (TextView) modifyView.findViewById(R.id.msg_ps);
		if(io==0){//出
			diag_io_tr.setVisibility(View.GONE);
			diag_io_2.setText(String.valueOf(GoodsList.get(id).getGoodsNum()));
			if(GoodsList.get(id).getIsJG()==1){
				diag_io_1.setText("集格数:");
				ps.setVisibility(View.VISIBLE);
			}
		}else{//进
			if(GoodsList.get(id).getIsJG()==1){
				diag_io_1.setText("空瓶集格数:");
				diag_io_1s.setText("满瓶集格数:");
				ps.setVisibility(View.VISIBLE);
			}else{
				diag_io_1.setText(getResources().getString(R.string.txt_string_21));
			}
			diag_io_2.setText(String.valueOf(GoodsList.get(id).getEmptyNum()));
			diag_io_3.setText(String.valueOf(GoodsList.get(id).getGoodsNum()));
		}

		diag_io_btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				numAlertDialog.dismiss();
			}
		});

		diag_io_btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(io==0){
					if(diag_io_2.getText().toString().trim().length()==0){
						ToastUtil.showToast(getContext(), "请填写瓶数");
						return ;
					}
					GoodsList.get(id).setGoodsNum(Integer.valueOf(diag_io_2.getText().toString().trim()));
					tAdapter.notifyDataSetChanged();
				}else{
					if(diag_io_2.getText().toString().trim().length()==0){
						ToastUtil.showToast(getContext(), "请填写空瓶数");
						return ;
					}
					if(diag_io_3.getText().toString().trim().length()==0){
						ToastUtil.showToast(getContext(), "请填写满瓶数");
						return ;
					}
					GoodsList.get(id).setEmptyNum(Integer.valueOf(diag_io_2.getText().toString().trim()));
					GoodsList.get(id).setGoodsNum(Integer.valueOf(diag_io_3.getText().toString().trim()));
					tAdapter.notifyDataSetChanged();
				}

				mV.truck_4.setText(getResources().getString(R.string.txt_home_9)+ String.valueOf(getNumOfQP()));

				numAlertDialog.dismiss();
			}
		});

		builder.setView(modifyView);

		numAlertDialog = builder.create();
		numAlertDialog.setTitle("修改数量");
		numAlertDialog.setCancelable(false);
		numAlertDialog.show();
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

	private void submitRfid(final TruckQPInfo info){
		ConfirmDialog dialog = new ConfirmDialog(getContext());
		dialog.setTitle("提示");
		dialog.setMessage("注意：存在超期气瓶");
		dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new CallRfidWsTask(getContext(), ActivityTruckInOut.this, IO==0 ? 2 : 3).execute(util.json.JSONUtils.toJsonWithGson(info));
			}

		});
		dialog.show();
	}

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

//		if(!rfid.getCQDW().equals(getApp().getLogin().getStation())){
//			ToastUtil.showToast(getContext(), "非产权单位标签");
//			return;
//		}
//		String MediaName = ConfigData.getMediaName(rfid.getCZJZCode(), getApp().getMediaInfo());
//		if(MediaName.length()==0) {
//			ToastUtil.showToast(getContext(), "无充装介质信息");
//			return ;
//		}
		//标签在错误列表已已存在
		for (int i = 0; i < wList.size(); i++) {
			if(wList.get(i).getQPDJCode().equals(rfid.getQPDJCode())) return;
		}
		//标签在正确列表已已存在
		for (int i = 0; i < rList.size(); i++) {
			if(rList.get(i).getQPDJCode().equals(rfid.getQPDJCode())) return ;
		}

		//（瓶种：散瓶，集格瓶）
		//标签(介质+瓶种)是否在商品(介质+瓶种)中存在
		int hasCJJZ = 0;//0不存在 1介质符合，瓶种不符 2介质+瓶种都符合
		for (int i = 0; i < GoodsList.size(); i++) {
			if (GoodsList.get(i).getMediumCode().equals(rfid.getCZJZCode())) {
				rfid.setMediumName(GoodsList.get(i).getCZJZ());
				if(GoodsList.get(i).getIsJG()==rfid.getIsJG()){
					hasCJJZ = 2;
					break;
				}else{
					hasCJJZ = 1;
				}
			}
		}

		if(hasCJJZ==0 || hasCJJZ==1){
			//不存在，检索该介质名称
			rfid.setColor(2);
			if(hasCJJZ==0){
				String Media = ConfigData.getMediaName(rfid.getCZJZCode(), getApp().getMediaInfo());
				rfid.setMediumName(Media);
				if(Media.length()==0) rfid.setNextCheckDate(null);
			}
			wList.add(0,rfid);
			reflishView(false);
			return ;
		}else{
//			//存在 介质+瓶种 一样的   （瓶种：散瓶，集格瓶）
//			//标签(介质+瓶种) ，在GoodsList中，(介质+瓶种) 是否有重复，无重复可确定商品
//			for (int i = 0; i < GoodsList.size(); i++) {
//				if(GoodsList.get(i).getMediumCode().equals(rfid.getCZJZCode()) &&  GoodsList.get(i).getIsJG()==rfid.getIsJG()){
//					if(i+1 == GoodsList.size()){
//						rfid.setGoodsCode(GoodsList.get(i).getGoodsCode());
//						rfid.setGoodsName(GoodsList.get(i).getGoodsName());
//						break;
//					}else{
//						boolean hasSame = false;
//						for (int j = i+1; j < GoodsList.size(); j++) {
//							if(GoodsList.get(j).getMediumCode().equals(rfid.getCZJZCode()) &&  GoodsList.get(j).getIsJG()==rfid.getIsJG()){
//								hasSame = true;
//								break;//(介质+瓶种) 有重复
//							}
//						}
//						if(!hasSame){
//							rfid.setGoodsCode(GoodsList.get(i).getGoodsCode());
//							rfid.setGoodsName(GoodsList.get(i).getGoodsName());
//						}else{
//							break;//(介质+瓶种) 有重复
//						}
//					}
//				}
//			}
		}

		if(IO==0){

			if(ConfigData.IsOverdue(rfid.getNextCheckDate()) == ConfigData.OVERDUE){
				wList.add(0,rfid);
				reflishView(false);
				return ;
			}

			rfid.setSendDate(sdf.format(new Date()));
		}else{
			rfid.setReceiveDate(sdf.format(new Date()));
		}

		rList.add(0,rfid);


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
				canRfid=true;
			}

			new CallRfidWsTask(getContext(), ActivityTruckInOut.this, 1).execute(getApp().getLogin().getStation(), String.valueOf(IO));
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
			rAdapter = new RfidAdapter(getContext(), rList);
			mV.lv.setAdapter(rAdapter);
		}else{
			rAdapter.notifyDataSetChanged();
			wAdapter.notifyDataSetChanged();
		}

		mV.truck_4.setText(getResources().getString(R.string.txt_home_9)+ String.valueOf(getNumOfQP()));

		int[] overdue = getOverdueInfo();
		mV.truck_5.setText(getResources().getString(R.string.txt_home_10)+ overdue[0] + " / " + overdue[1]);

		mV.truck_6.setText(getMediumInfo());

		mV.truck_rb_1.setText("正确明细("+String.valueOf(rList.size())+")");
		mV.truck_rb_2.setText("错误明细("+String.valueOf(wList.size())+")");
		mV.truck_7.setText("正确数量:" + String.valueOf(rList.size()) + ", 错误数量:" + String.valueOf(wList.size()) );

	}

	private int getNumOfQP(){
		int num = 0;
		for (int i = 0; i < GoodsList.size(); i++) {
			num += (GoodsList.get(i).getGoodsNum() + GoodsList.get(i).getEmptyNum()) * GoodsList.get(i).getNum();
		}

		return num;
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
			info += mlist.get(i).getMediumName()+":"+String.valueOf( mlist.get(i).getMediumNameCount()) + ", ";
		}

		return info;
	}

	private int[] getOverdueInfo(){
		int[] ret = {0, 0};
		if (IO==0) {
			for (int i = 0; i < rList.size(); i++) {
				if(rList.get(i)!=null && rList.get(i).getNextCheckDate()!=null){
					int res = ConfigData.IsOverdue(rList.get(i).getNextCheckDate());
					if(res == ConfigData.FORTHCOMING) ret[1]++;
				}
			}
			for (int i = 0; i < wList.size(); i++) {
				if(wList.get(i)!=null && wList.get(i).getNextCheckDate()!=null){
					int res = ConfigData.IsOverdue(wList.get(i).getNextCheckDate());
					if(res == ConfigData.OVERDUE) ret[0]++;
				}
			}
		}else{
			for (int i = 0; i < rList.size(); i++) {
				if(rList.get(i)!=null && rList.get(i).getNextCheckDate()!=null){
					int res = ConfigData.IsOverdue(rList.get(i).getNextCheckDate());
					if(res == ConfigData.OVERDUE) ret[0]++;
					else if(res == ConfigData.FORTHCOMING) ret[1]++;
				}
			}
		}

		return ret;
	}

	private void CheckRfid(){
		List<QPInfo> res = new ArrayList<QPInfo>();
		for (int i = 0; i < rList.size(); i++) {
			QPInfo qp = new QPInfo();
			qp.setCQDW(rList.get(i).getCQDW());
			qp.setLabelNo(rList.get(i).getLabelNo());
			qp.setMediumCode(rList.get(i).getCZJZCode());
			qp.setIsJG(rList.get(i).getIsJG());
			qp.setIsByHand(0);
			res.add(qp);
		}

		if(res.size()>0){
			new CallRfidWsTask(getContext(), this, 11).execute(util.json.JSONUtils.toJsonWithGson(res));
		}
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if( isSuccess){
			if(code==1){
				try {
					List<TruckQPInfo> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<TruckQPInfo>>(){}.getType());
					ArrayAdapter<TruckQPInfo> orgAdapter = new ArrayAdapter<TruckQPInfo>(getContext(),android.R.layout.simple_spinner_item, list);
					orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					if(list!=null && list.size()>0){
						Log.e("list===", util.json.JSONUtils.toJsonWithGson(list));
						mV.btn1.setEnabled(canRfid);
						mV.btn2.setEnabled(true);
						mV.btn3.setEnabled(true);
						mV.btn4.setEnabled(true);
						mV.truck_2.setAdapter(orgAdapter);
					}else{
						mV.truck_2.setAdapter(null);
						mV.btn1.setEnabled(false);
						mV.btn2.setEnabled(false);
						mV.btn3.setEnabled(false);
						mV.btn4.setEnabled(false);
						ToastUtil.showToast(getContext(), "无车辆信息");
						clean();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					mV.btn1.setEnabled(false);
					mV.btn2.setEnabled(false);
					mV.btn3.setEnabled(false);
					mV.btn4.setEnabled(false);
					ToastUtil.showToast(getContext(), "错误:车辆信息数据错误");
					clean();
				}
			}else if(code==11){
				List<QPInfo> res = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<QPInfo>>(){}.getType());
				List<Rfid> newList = new ArrayList<Rfid>();
				List<Rfid> oldList = util.json.JSONUtils.toListWithGson(util.json.JSONUtils.toJsonWithGson(rList),  new TypeToken<List<Rfid>>(){}.getType());
				Log.e("res", util.json.JSONUtils.toJsonWithGson(res));

				for (int i = 0; i < oldList.size(); i++) {
					for (int j = 0; j < res.size(); j++) {
						if(oldList.get(i).getCQDW().equals(res.get(j).getCQDW()) && oldList.get(i).getLabelNo().equals(res.get(j).getLabelNo())){
							oldList.get(i).setGoodsCode(res.get(j).getQPType());//获取最近一次的充装商品
							if(oldList.get(i).getGoodsCode()!=null && oldList.get(i).getGoodsCode().length()>0){//找到
								//查询该商品在商品登记列表中是否存在
								boolean isExist = false;
								for (int j2 = 0; j2 < GoodsList.size(); j2++) {
									if(oldList.get(i).getGoodsCode().equals(GoodsList.get(j2).getGoodsCode())){
										oldList.get(i).setGoodsName(GoodsList.get(j2).getGoodsName());
										isExist = true;
										oldList.get(i).setColor(1);
										newList.add(oldList.get(i));
										break;
									}
								}
								if(!isExist){
									for (int k = 0; k < getApp().getLogin().getGoodsList().size(); k++) {
										if(getApp().getLogin().getGoodsList().get(k).getGoodsCode().equals(oldList.get(i).getGoodsCode())){
											oldList.get(i).setGoodsName(getApp().getLogin().getGoodsList().get(k).getGoodsName());
											break;
										}
									}
									oldList.get(i).setColor(1);
									wList.add(0,oldList.get(i));
								}
							}else{
								wList.add(0,oldList.get(i));
							}

							break;
						}
					}
				}
				rList.clear();
				rList.addAll(newList);

				Log.e("rlist", util.json.JSONUtils.toJsonWithGson(rList));
				Log.e("wList", util.json.JSONUtils.toJsonWithGson(wList));
				reflishView(false);
			}else{
				clean();
				ToastUtil.showToast(getContext(), code==2 ? "出厂登记成功" : "进场登记成功");
				GoodsList.clear();
				tAdapter.notifyDataSetChanged();
				mV.truck_4.setText(getResources().getString(R.string.txt_home_9)+0);
				mV.truck_5.setText(getResources().getString(R.string.txt_home_10)+"0 / 0");

				try {
					List<FHLX> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<FHLX>>(){}.getType());
					if(list!=null && list.size()>0){
						String err = "";
						for (int i = 0; i < list.size(); i++) {
							err += list.get(i).getType()+",数量:"+list.get(i).getNum()+"，" + list.get(i).getTagID() + "。";
						}
						if(err.length()>0)
							mV.truck_7.setText(err);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//重新查询车辆信息
				new CallRfidWsTask(getContext(), ActivityTruckInOut.this, 1).execute(getApp().getLogin().getStation(), String.valueOf(IO));
			}
		}else{
			if(code==1){
				mV.truck_2.setAdapter(null);
				canRfid=false;
				mV.btn1.setEnabled(false);
				mV.btn2.setEnabled(false);
				mV.btn3.setEnabled(false);
				mV.btn4.setEnabled(false);
				ToastUtil.showToast(getContext(), "无车辆信息");
				clean();
			}
//			else{
//				ToastUtil.showToast(getContext(), code==2 ? "出厂登记错误" : "进场登记错误");
//
//			}
		}

	}

	private boolean hasQP(){
		for (int i = 0; i < GoodsList.size(); i++) {
			if(GoodsList.get(i).getGoodsNum()>0 || GoodsList.get(i).getEmptyNum()>0){
				return true;
			}
		}

		return false;
	}

	@Override
	public void ScanRfid(){
		if(canRfid){
			if(isStart){
//				if(mReader!=null && rfidTask!=null && rfidTask.getStatus()==AsyncTask.Status.RUNNING){
//					rfidTask.cancel(true);
//				}
//				rfidTask = null;
//				isStart = false;
//				mV.btn1.setText(getResources().getString(R.string.btn_string_12));
//				mV.btn3.setEnabled(true);
			}else{
				if(mReader!=null && rfidTask==null){
					isStart = true;
					mV.btn1.setText(getResources().getString(R.string.btn_string_13));
					rfidTask = new ScanTask(myHandler);
					rfidTask.execute(mReader);
					mV.btn3.setEnabled(false);
				}
			}
		}else{
			ToastUtil.showToast(getContext(), "无车辆信息");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if(isRfid){
					isRfid = false;
					mV.btn4.setText(getResources().getString(R.string.btn_string_20));
					mV.truck_group.setVisibility(View.GONE);
					mV.lv.setVisibility(View.GONE);
					mV.truck_ll.setVisibility(View.VISIBLE);
					mV.qplv.setVisibility(View.VISIBLE);
				}else{
					if(rList!=null && rList.size()>0){
						ConfirmDialog dialog = new ConfirmDialog(this);
						dialog.setTitle("提示");
						dialog.setMessage("已扫描到气瓶，确定退出？");
						dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								finish();
							}

						});
						dialog.show();
					}else{
						finish();
					}
				}
				break;
		}
		return true;
	}
}
