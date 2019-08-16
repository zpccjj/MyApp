package com.hsic.qp.sz;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.AllCapTransformationMethod;
import util.RfidUtils;
import util.ToastUtil;
import util.UiUtil;
import util.WsUtils;
import hsic.ui.ConfirmDialog;
import hsic.ui.EditDate;
import hsic.ui.HsicActivity;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import bean.BasicInfo;
import bean.BasicQPInfo;
import bean.BasicQPInfo2;
import bean.BasicUnit;
import bean.JgInfo;
import bean.QPDJCode;
import bean.ResponseData;
import bean.Rfid;
import bean.SubmitQP;
import com.actionbarsherlock.view.MenuItem;
import com.hsic.qp.sz.adapter.SubmitQPAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;
import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.RFIDWithUHF.BankEnum;

public class ActivityQpInfo3 extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";

	InfoView mView;
	private RFIDWithUHF mReader;
	private boolean hasRfid = false;
	private boolean CanRead = false;

	List<SubmitQP> QpList = new ArrayList<SubmitQP>();
	SubmitQPAdapter sqAdapter;

	List<BasicUnit> ZZDW;//制造单位配置
	SubmitQP selectQP = null;
	JgInfo JG = null;//集格登记信息

	private int Part = 1;

	private Context getContext(){
		return ActivityQpInfo3.this;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qpinfo3);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		initView();
		setListener();

		new InitTask(getContext()).execute();
		new CallRfidWsTask(getContext(), ActivityQpInfo3.this, 8).execute("");
	}

	private void initView(){
		mView = new InfoView();

		mView.qpinfo3_ll1 = (LinearLayout) findViewById(R.id.qpinfo3_ll1);//集格信息
		mView.qpinfo3_tag = (EditText) findViewById(R.id.qpinfo3_tag);
		mView.qpinfo3_jgid = (EditText) findViewById(R.id.qpinfo3_jgid);
		mView.qpinfo3_jgnum = (Spinner) findViewById(R.id.qpinfo3_jgnum);
		mView.qpinfo3_0 = (Spinner) findViewById(R.id.qpinfo3_0);//介质
		mView.qpinfo3_1 = (Spinner) findViewById(R.id.qpinfo3_1);//产权单位
		mView.qpinfo3_2 = (Spinner) findViewById(R.id.qpinfo3_2);//气瓶规格
		mView.qpinfo3_3 = (Spinner) findViewById(R.id.qpinfo3_3);//定检机构
		mView.qpinfo3_5 = (EditDate) findViewById(R.id.qpinfo3_5);//下次检验日期

		mView.qpinfo3_list = (hsic.ui.MyListView) findViewById(R.id.qpinfo3_list);//气瓶列表

		mView.qpinfo3_clean = (Button) findViewById(R.id.qpinfo3_clean);//清空
		mView.qpinfo3_add = (Button) findViewById(R.id.qpinfo3_add);//添加集格气瓶
		mView.qpinfo3_save = (Button) findViewById(R.id.qpinfo3_save);//提交登记
		mView.qpinfo3_read = (Button) findViewById(R.id.qpinfo3_read);//绑定集格

		//======
		mView. qpinfo3_ll2 = (LinearLayout) findViewById(R.id.qpinfo3_ll2);//气瓶信息
		mView. info3_item_1 = (EditText) findViewById(R.id.info3_item_1);//瓶号
		mView. info3_item_2 = (EditText) findViewById(R.id.info3_item_2);//自编号
		mView. info3_item_3 = (EditText) findViewById(R.id.info3_item_3);//单位代码
		mView. info3_item_3s = (EditText) findViewById(R.id.info3_item_3s);//单位名称
		mView. info3_item_4  = (hsic.ui.EditDate) findViewById(R.id.info3_item_4);//制造日期
		mView. info3_item_5 = (EditText) findViewById(R.id.info3_item_5);//容积
		mView. info3_item_7 = (EditText) findViewById(R.id.info3_item_7);//壁厚
		mView. info3_item_8 = (EditText) findViewById(R.id.info3_item_8);//皮重
		mView. info3_item_9 = (EditText) findViewById(R.id.info3_item_9);//公称压力
		mView. info3_item_10 = (EditText) findViewById(R.id.info3_item_10);//水压
		mView. info3_item_11 = (EditText) findViewById(R.id.info3_item_11);//材质
		mView. info3_item_12 = (EditText) findViewById(R.id.info3_item_12);//孔隙
		mView. info3_item_13 = (EditText) findViewById(R.id.info3_item_13);//周期
		mView. info3_item_14 = (EditText) findViewById(R.id.info3_item_14);//年限
		mView. info3_item_6 = (EditText) findViewById(R.id.info3_item_6);//检验次数

		mView. info3_item_back = (Button) findViewById(R.id.info3_item_back);//返回
		mView. info3_item_save = (Button) findViewById(R.id.info3_item_save);//添加气瓶

		//
		sqAdapter = new SubmitQPAdapter(getContext(), QpList);
		mView.qpinfo3_list.setAdapter(sqAdapter);
	}

	private void CleanList(){
		QpList.clear();
		sqAdapter.notifyDataSetChanged();
	}

	private void setListener(){
		//选择气瓶规格，检索对应介质
		mView.qpinfo3_2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				BasicQPInfo selectItem = (BasicQPInfo) parent.getSelectedItem();
				if(selectItem!=null && selectItem.getList()!=null){
					ArrayAdapter<BasicQPInfo2> list2Adapter = new ArrayAdapter<BasicQPInfo2>(getContext(),android.R.layout.simple_spinner_item,
							((BasicQPInfo) parent.getSelectedItem()).getList());
					list2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					mView.qpinfo3_0.setAdapter(list2Adapter);
					selectQP = null;
					CleanList();
				}else{
					mView.qpinfo3_0.setAdapter(null);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});

		//选择介质，获取默认信息
		mView.qpinfo3_0.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				// TODO Auto-generated method stub
				BasicQPInfo2 bqInfo2 = (BasicQPInfo2) parent.getSelectedItem();
				CleanList();
				if(bqInfo2!=null){
					selectQP = new SubmitQP();
					selectQP.setMediumCode(bqInfo2.getId());
					selectQP.setL(bqInfo2.getRJ());
					selectQP.setMM(bqInfo2.getBH());
					selectQP.setQPPZ(bqInfo2.getQPPZ());
					selectQP.setWorkMpa(bqInfo2.getWorkMpa());
					selectQP.setWaterMpa(bqInfo2.getWaterMpa());
					selectQP.setBTSL(bqInfo2.getBTSL());
					selectQP.setTLKXL(bqInfo2.getTLKXL());
					selectQP.setJCZQ(bqInfo2.getJCZQ());
					selectQP.setSYQX(bqInfo2.getSYQX());
				}else{
					selectQP = null;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		mView.qpinfo3_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									final int position, long id) {
				// TODO Auto-generated method stub
				if(!CanRead){
					ConfirmDialog dialog = new ConfirmDialog(getContext());
					dialog.setCancelable(false);
					dialog.setTitle("删除气瓶");
					dialog.setMessage("气瓶编号: " + QpList.get(position).getGPNO());
					dialog.setConfirmButton("删除", new android.content.DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							QpList.remove(position);
							sqAdapter.notifyDataSetChanged();
						}

					});

					dialog.show();
				}

			}
		});


		mView.qpinfo3_clean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mView.qpinfo3_5.setText("");
				CleanList();
			}
		});

		mView.qpinfo3_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(selectQP==null){
					ToastUtil.showToast(getContext(), "请选择介质");
					return ;
				}
				if(mView.qpinfo3_jgnum.getSelectedItem() == null){
					ToastUtil.showToast(getContext(), "请选择集格内气瓶数量");
					return ;
				}
				int num = Integer.valueOf(mView.qpinfo3_jgnum.getSelectedItem().toString());
				if(num==QpList.size()){
					ToastUtil.showToast(getContext(), "气瓶数量已满");
					return ;
				}
				if(num<QpList.size()){
					ToastUtil.showToast(getContext(), "气瓶数量超量");
					return ;
				}

				mView.qpinfo3_ll1.setVisibility(View.GONE);
				mView.qpinfo3_ll2.setVisibility(View.VISIBLE);
				Part = 2;

				if(mView.info3_item_5.getText().toString().trim().length()==0) mView.info3_item_5.setText(selectQP.getL());
				if(mView.info3_item_7.getText().toString().trim().length()==0) mView.info3_item_7.setText(selectQP.getMM());
				if(mView.info3_item_8.getText().toString().trim().length()==0) mView.info3_item_8.setText(selectQP.getQPPZ());
				if(mView.info3_item_9.getText().toString().trim().length()==0) mView.info3_item_9.setText(selectQP.getWorkMpa());
				if(mView.info3_item_10.getText().toString().trim().length()==0) mView.info3_item_10.setText(selectQP.getWaterMpa());
				if(mView.info3_item_11.getText().toString().trim().length()==0) mView.info3_item_11.setText(selectQP.getBTSL());
				if(mView.info3_item_12.getText().toString().trim().length()==0) mView.info3_item_12.setText(selectQP.getTLKXL());
				if(mView.info3_item_13.getText().toString().trim().length()==0) mView.info3_item_13.setText(selectQP.getJCZQ());
				if(mView.info3_item_14.getText().toString().trim().length()==0) mView.info3_item_14.setText(selectQP.getSYQX());
			}
		});

		mView.qpinfo3_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mView.qpinfo3_jgnum.getSelectedItem() == null){
					ToastUtil.showToast(getContext(), "请选择集格内气瓶数量");
					return ;
				}

				BasicUnit CQDW = (BasicUnit) mView.qpinfo3_1.getSelectedItem();
				if(CQDW==null){
					ToastUtil.showToast(getContext(), "请选择产权单位");
					return ;
				}

				BasicQPInfo StandNo = (BasicQPInfo) mView.qpinfo3_2.getSelectedItem();
				if(StandNo==null){
					ToastUtil.showToast(getContext(), "请选择气瓶类型");
					return ;
				}

				if(selectQP==null){
					ToastUtil.showToast(getContext(), "请选择介质");
					return ;
				}

				BasicUnit JCDW = (BasicUnit) mView.qpinfo3_3.getSelectedItem();
				if(JCDW==null){
					ToastUtil.showToast(getContext(), "请选择定期检验单位");
					return ;
				}

				if(mView.qpinfo3_5.getText().toString().trim().length() == 0){
					ToastUtil.showToast(getContext(), "请输入下次检验日期");
					return ;
				}

				int num = Integer.valueOf(mView.qpinfo3_jgnum.getSelectedItem().toString());
				if(num > QpList.size()){
					ToastUtil.showToast(getContext(), "气瓶数量不足，请继续加瓶");
					return ;
				}else if(num < QpList.size()){
					ToastUtil.showToast(getContext(), "气瓶数量大于集格内瓶数，请确认集格内瓶数或删除多余气瓶");
					return ;
				}else{
					String minMakeDate = null;
					int minID = 0;
					for (int i = 0; i < QpList.size(); i++) {
						QpList.get(i).setNextCheckDate(mView.qpinfo3_5.getText().toString().trim());
						QpList.get(i).setMediumCode(selectQP.getMediumCode());
						QpList.get(i).setCQDW(CQDW.getId());
						QpList.get(i).setCZDW(CQDW.getId());
						QpList.get(i).setJCDW(JCDW.getId());
						QpList.get(i).setStandNo(StandNo.getId());
						QpList.get(i).setOPID(getApp().getLogin().getUserID());

						if(QpList.get(i).getJCCS().equals("0")){
							QpList.get(i).setCheckDate(QpList.get(i).getMakeDate());
						}else{
							int zq = Integer.valueOf(QpList.get(i).getJCZQ());
							int jccs = Integer.valueOf(QpList.get(i).getJCCS());
							int made = Integer.valueOf(QpList.get(i).getMakeDate().substring(0, 4));
							int check = made + zq*jccs;
							QpList.get(i).setCheckDate(String.valueOf(check) + QpList.get(i).getNextCheckDate().substring(4, QpList.get(i).getMakeDate().length()));
						}

						if(minMakeDate==null){
							minMakeDate = QpList.get(i).getMakeDate();
						}
						else{
							if(QpList.get(i).getMakeDate().compareTo(minMakeDate)<0){
								minMakeDate = QpList.get(i).getMakeDate();
								minID  = i;
							}
						}
					}
					JG = new JgInfo();
					JG = util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(QpList.get(minID)), JgInfo.class);

					JG.setJgNum(num);
					List<SubmitQP> qpList = new ArrayList<SubmitQP>();
					qpList.addAll(QpList);
					JG.setQpList(qpList);

					Log.e("JgInfo", util.json.JSONUtils.toJsonWithGson(JG));

					new CallRfidWsTask(getContext(), ActivityQpInfo3.this, 12).execute(util.json.JSONUtils.toJsonWithGson(JG));
				}
			}
		});

		mView.qpinfo3_read.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScanRfid();
			}
		});

		//===
		mView. info3_item_1.setTransformationMethod(new AllCapTransformationMethod ());
		mView. info3_item_3.setTransformationMethod(new AllCapTransformationMethod ());

		//输入制造单位代码，检索名称
		mView.info3_item_3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(ZZDW!=null){
					for (int i = 0; i < ZZDW.size(); i++) {
						if(ZZDW.get(i).getId().equals(mView.info3_item_3.getText().toString().trim().toUpperCase())){
							mView.info3_item_3s.setText(ZZDW.get(i).getName());
							break;
						}else{
							mView.info3_item_3s.setText("");
						}
					}
				}
			}
		});

		mView.info3_item_4.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//计算检验次数 (现在-制造日期) / 周期
				if(s.toString().length()>0 && mView.info3_item_13.getText().toString().length()>0){
					try {
						int start = Integer.valueOf(s.toString().substring(0, 4));
						int zq = Integer.valueOf(mView.info3_item_13.getText().toString());
						int now = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));

						mView.info3_item_6.setText(String.valueOf( ((now-start) / zq )) );
					} catch (Exception e) {
						// TODO: handle exception
						mView.info3_item_6.setText("0");
					}
				}else{
					mView.info3_item_6.setText("0");
				}
			}
		});

		mView.info3_item_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sqAdapter.notifyDataSetChanged();
				mView.qpinfo3_ll1.setVisibility(View.VISIBLE);
				mView.qpinfo3_ll2.setVisibility(View.GONE);
				Part = 1;
				CleanItem();
			}
		});

		mView.info3_item_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mView.info3_item_1.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入瓶号");
					return ;
				}
				for (int i = 0; i < QpList.size(); i++) {
					if(QpList.get(i).getGPNO().equals(mView.info3_item_1.getText().toString().trim())){
						ToastUtil.showToast(getContext(), "该瓶号已存在");
						return ;
					}
				}

				if(mView.info3_item_3.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入制造单位代码");
					return ;
				}
				if(mView.info3_item_3s.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "制造单位代码错误");
					return ;
				}
				if(mView.info3_item_4.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入制造日期");
					return ;
				}
				if(mView.info3_item_5.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入实际容积");
					return ;
				}
				if(mView.info3_item_7.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "瓶体壁厚");
					return ;
				}
				if(mView.info3_item_8.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入皮重");
					return ;
				}
				if(mView.info3_item_9.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入公称工作压力");
					return ;
				}
				if(mView.info3_item_10.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入水压测试压力");
					return ;
				}

				SubmitQP info = new SubmitQP();
				info.setGPNO(mView.info3_item_1.getText().toString().trim().toUpperCase());
				info.setYHNO(mView.info3_item_2.getText().toString().trim());
				info.setMadeName(mView.info3_item_3s.getText().toString().trim());
				info.setMakeDate(mView.info3_item_4.getText().toString().trim());
				info.setL(mView.info3_item_5.getText().toString().trim());
				info.setJCCS(mView.info3_item_6.getText().toString().trim());
				info.setMM(mView.info3_item_7.getText().toString().trim());
				info.setQPPZ(mView.info3_item_8.getText().toString().trim());
				info.setKg(mView.info3_item_8.getText().toString().trim());
				info.setWorkMpa(mView.info3_item_9.getText().toString().trim());
				info.setWaterMpa(mView.info3_item_10.getText().toString().trim());
				info.setBTSL(mView.info3_item_11.getText().toString().trim());
				info.setTLKXL(mView.info3_item_12.getText().toString().trim());
				info.setJCZQ(mView.info3_item_13.getText().toString().trim());
				info.setSYQX(mView.info3_item_14.getText().toString().trim());
				QpList.add(0, info);

				ToastUtil.showToast(getContext(), "添加气瓶成功");

				int num = Integer.valueOf(mView.qpinfo3_jgnum.getSelectedItem().toString());
				if(num==QpList.size()){
					sqAdapter.notifyDataSetChanged();
					mView.qpinfo3_ll1.setVisibility(View.VISIBLE);
					mView.qpinfo3_ll2.setVisibility(View.GONE);
					CleanItem();
				}else{
					mView.info3_item_1.setText("");
					mView.info3_item_4.setText("");
					mView.info3_item_1.setFocusable(true);
					mView.info3_item_1.requestFocus();
				}
			}
		});
	}

	private void CleanItem(){
		mView.info3_item_1.setText("");
		mView.info3_item_2.setText("");
		mView.info3_item_3.setText("");
		mView.info3_item_3s.setText("");
		mView.info3_item_4.setText("");
		mView.info3_item_5.setText("");
		mView.info3_item_7.setText("");
		mView.info3_item_8.setText("");
		mView.info3_item_9.setText("");
		mView.info3_item_10.setText("");
		mView.info3_item_11.setText("");
		mView.info3_item_12.setText("");
		mView.info3_item_13.setText("");
		mView.info3_item_14.setText("");
		mView.info3_item_10.setText("");
		mView.info3_item_6.setText("");
	}

	private void setViewsEnabled(boolean ret){
		mView.qpinfo3_jgnum.setEnabled(ret);
		mView.qpinfo3_1.setEnabled(ret);
		mView.qpinfo3_2.setEnabled(ret);
		mView.qpinfo3_0.setEnabled(ret);
		mView.qpinfo3_3.setEnabled(ret);
		mView.qpinfo3_5.setEnabled(ret);
		mView.qpinfo3_clean.setEnabled(ret);
		mView.qpinfo3_add.setEnabled(ret);
		mView.qpinfo3_save.setEnabled(ret);
	}

	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			if(code==8){
				setData(retData);
			}else if(code == 12){
				mView.qpinfo3_jgid.setText(retData);
				setViewsEnabled(false);
				CanRead = true;
				if(hasRfid)
					mView.qpinfo3_read.setEnabled(true);
			}
		}
	}


	private void setData(String jsonStr){
		BasicInfo bi = new BasicInfo();
		try {
			bi = (BasicInfo) util.json.JSONUtils.toObjectWithGson(jsonStr, BasicInfo.class);

			ZZDW = bi.getZZDW();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ToastUtil.showToast(getContext(), "基础数据错误");
			return ;
		}

		//数据整理
		List<BasicQPInfo> mInfo = new ArrayList<BasicQPInfo>();

		for (int i = 0; i < bi.getQpList().size(); i++) {
			boolean hasQP = false;
			for (int j = 0; j < mInfo.size(); j++) {
				if(mInfo.get(j).getId().equals(bi.getQpList().get(i).getId())){
					hasQP = true;

					boolean hasOp2 = false;
					for (int x = 0; x < mInfo.get(j).getList().size(); x++) {
						if(mInfo.get(j).getList().get(x).getId().equals(bi.getQpList().get(i).getJZId())){
							hasOp2 = true;
							break;
						}
					}
					if(!hasOp2){
						BasicQPInfo2 qi2 = new BasicQPInfo2();
						qi2.setId(bi.getQpList().get(i).getJZId());
						qi2.setName(bi.getQpList().get(i).getJZName());
						qi2.setRJ(bi.getQpList().get(i).getRJ());
						qi2.setBH(bi.getQpList().get(i).getBH());
						qi2.setBTSL(bi.getQpList().get(i).getBTSL());
						qi2.setTLKXL(bi.getQpList().get(i).getTLKXL());
						qi2.setJCZQ(bi.getQpList().get(i).getJCZQ());
						qi2.setKg(bi.getQpList().get(i).getKg());
						qi2.setQPPZ(bi.getQpList().get(i).getQPPZ());
						qi2.setSYQX(bi.getQpList().get(i).getSYQX());
						qi2.setWaterMpa(bi.getQpList().get(i).getWaterMpa());
						qi2.setWorkMpa(bi.getQpList().get(i).getWorkMpa());
						mInfo.get(j).getList().add(qi2);
					}

					break;
				}
			}
			if(!hasQP){
				BasicQPInfo qi = new BasicQPInfo();
				qi.setId(bi.getQpList().get(i).getId());
				qi.setName(bi.getQpList().get(i).getName());
				qi.setList(new ArrayList<BasicQPInfo2>());

				BasicQPInfo2 qi2 = new BasicQPInfo2();
				qi2.setId(bi.getQpList().get(i).getJZId());
				qi2.setName(bi.getQpList().get(i).getJZName());
				qi2.setRJ(bi.getQpList().get(i).getRJ());
				qi2.setBH(bi.getQpList().get(i).getBH());
				qi2.setBTSL(bi.getQpList().get(i).getBTSL());
				qi2.setTLKXL(bi.getQpList().get(i).getTLKXL());
				qi2.setJCZQ(bi.getQpList().get(i).getJCZQ());
				qi2.setKg(bi.getQpList().get(i).getKg());
				qi2.setQPPZ(bi.getQpList().get(i).getQPPZ());
				qi2.setSYQX(bi.getQpList().get(i).getSYQX());
				qi2.setWaterMpa(bi.getQpList().get(i).getWaterMpa());
				qi2.setWorkMpa(bi.getQpList().get(i).getWorkMpa());

				qi.getList().add(qi2);

				mInfo.add(qi);
			}
		}
		//
		ArrayAdapter<BasicUnit> CQDWAdapter = new ArrayAdapter<BasicUnit>(getContext(),android.R.layout.simple_spinner_item, bi.getCQDW());
		CQDWAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mView.qpinfo3_1.setAdapter(CQDWAdapter);
		for (int k = 0; k < bi.getCQDW().size(); k++) {
			if(bi.getCQDW().get(k).getId().equals("1702")){
				mView.qpinfo3_1.setSelection(k);
				break;
			}
		}

		ArrayAdapter<BasicUnit> JYDWAdapter = new ArrayAdapter<BasicUnit>(getContext(),android.R.layout.simple_spinner_item, bi.getJYDW());
		JYDWAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mView.qpinfo3_3.setAdapter(JYDWAdapter);

		ArrayAdapter<BasicQPInfo> QPGGAdapter = new ArrayAdapter<BasicQPInfo>(getContext(),android.R.layout.simple_spinner_item, mInfo);
		QPGGAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mView.qpinfo3_2.setAdapter(QPGGAdapter);
		for (int j = 0; j < mInfo.size(); j++) {
			if(mInfo.get(j).getId().equals("0202")){
				mView.qpinfo3_2.setSelection(j);
				break;
			}
		}
	}

	Rfid LastRfid = null;
	public void getRfid(String txt) {
		// TODO Auto-generated method stub
		Rfid rfid = null;

		try {
			rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(txt, Rfid.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ToastUtil.showToast(getContext(), "标签数据错误");
			LastRfid = null;
			mView.qpinfo3_tag.setText("");
			return ;
		}
		if(rfid==null ||rfid.getCQDW()==null || rfid.getLabelNo()==null){

			LastRfid = null;
			mView.qpinfo3_tag.setText("");
			ToastUtil.showToast(getContext(), "未读到标签");
			return ;
		}
		rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());

		if(!rfid.getVersion().equals("0101")){

			LastRfid = null;
			mView.qpinfo3_tag.setText("");
			ToastUtil.showToast(getContext(), "版本号错误");
			return ;
		}

		LastRfid = rfid;
		mView.qpinfo3_tag.setText(rfid.getQPDJCode());

		util.SoundUtil.play();

		CanRead = false;
		mView.qpinfo3_read.setEnabled(false);

		ConfirmDialog dialog = new ConfirmDialog(getContext());
		dialog.setCancelable(false);
		dialog.setTitle("绑定");
		dialog.setMessage("标签号: " + rfid.getQPDJCode());
		dialog.setConfirmButton("绑定", new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				UiUtil.CloseKey(ActivityQpInfo3.this);

				new RfidTask(getContext(), LastRfid, JG, mView.qpinfo3_jgid.getText().toString().trim()).execute(mReader);
			}

		});

		dialog.setCancelButton(new android.content.DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				CanRead = true;
				mView.qpinfo3_read.setEnabled(true);
			}

		});

		dialog.show();
	}

	private class RfidTask  extends AsyncTask<RFIDWithUHF, Void, ResponseData> {
		Context mContext;
		Rfid mRfid;
		JgInfo mSubmitQP;
		String mJgid;
		ProgressDialog dialog;

		public RfidTask(Context context, Rfid rfid, JgInfo qp, String jgid){
			mContext = context;
			mRfid = rfid;
			mSubmitQP = qp;
			mJgid = jgid;
		}
		@Override
		protected ResponseData doInBackground(RFIDWithUHF... params) {
			// TODO Auto-generated method stub
			ResponseData msg = new ResponseData();
			String DeviceID = mContext.getSharedPreferences("DeviceSetting", 0).getString("DeviceID", "");
			String TagID;
			//读TagID
			TagID = mReader.readData("00000000",
					BankEnum.valueOf("UII"), 4*8, 96, mRfid.getEPC(),
					BankEnum.valueOf("TID"), 0, 6);
			if(TagID==null){
				msg.setRespCode(1);
				msg.setRespMsg("未读取到TagID");
				return msg;
			}

			//ws 查询TagID是否已使用
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> m1 = new HashMap<String, Object>();
			m1.put("propertyName", "DeviceID");
			m1.put("propertyValue", DeviceID);
			list.add(m1);

			HashMap<String, Object> m2 = new HashMap<String, Object>();
			m2.put("propertyName", "TagID");
			m2.put("propertyValue", TagID);
			list.add(m2);
			msg = WsUtils.CallWs(mContext, "getTagIDInfo", list);
			if(msg.getRespCode()!=0) return msg;

			//写User区

			//User区写内容组装
			byte[] user = new byte[26];
			//标签类别 + 规范版本
			String p1 = "0101";
			//标签绑定日期
			SimpleDateFormat sdf=new SimpleDateFormat("yyMMdd");
			String p2 = RfidUtils.LeftAddString(Integer.toBinaryString(Integer.valueOf(sdf.format(new Date()))), 20, "0");
			BigInteger b1_2 = new BigInteger(p1 + p2, 2);
			byte[] byte0_2 = RfidUtils.hexStringToBytes(b1_2.toString(16));
			System.arraycopy(byte0_2, 0, user, 0, byte0_2.length);

			//气瓶制造日期
			String makeDate = mSubmitQP.getMakeDate();
			makeDate = makeDate.replaceAll("-", "").substring(2,8);

			//Standno
			String Standno = mSubmitQP.getStandNo();
			String p3_4 = RfidUtils.LeftAddString(Integer.toBinaryString(Integer.valueOf(makeDate)), 20, "0")
					+ RfidUtils.LeftAddString(Integer.toBinaryString(Integer.valueOf(Standno)), 12, "0");

			BigInteger b3_6 = new BigInteger(p3_4, 2);
			byte[] byte3_6 = RfidUtils.hexStringToBytes(b3_6.toString(16));
			//3-6
			System.arraycopy(byte3_6, 0, user, 3, byte3_6.length);

			//集格号
			String qp = mJgid;
			qp = RfidUtils.LeftAddString(qp, 12, " ");

			try {
				byte[] tmp = qp.getBytes("UTF-8");
				//7-18
				System.arraycopy(tmp, 0, user, 7, tmp.length);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			//4位单位代码+8位追溯码 19-23
			String QPDJCODE = mRfid.getQPDJCode();
			//5 byte
			BigInteger big = new BigInteger(QPDJCODE, 10);

			byte[] b = RfidUtils.hexStringToBytes( RfidUtils.LeftAddString(big.toString(16), 10, "0") );

			//19-23
			System.arraycopy(b, 0, user, 19, b.length);

			//充装介质 24-25
			BigInteger jz = new BigInteger(mSubmitQP.getMediumCode(), 10);
			byte[] c = RfidUtils.hexStringToBytes( RfidUtils.LeftAddString(jz.toString(16), 4, "0") );
			//24-25
			System.arraycopy(c, 0, user, 24, c.length);
			//xor 0x34
			for (int i = 0; i < user.length; i++) {
				user[i] = (byte)(user[i] ^ 0x34);
			}

			boolean wuser = mReader.writeData("00000000",
					BankEnum.valueOf("TID"), 0, 96, TagID,
					BankEnum.valueOf("USER"),0, 13,
					RfidUtils.bytesToHexString(user));

			if(wuser) {
				msg.setRespCode(0);
				msg.setRespMsg("写标签USER区成功");
			}else{
				msg.setRespCode(1);
				msg.setRespMsg("写标签USER区失败");
				return msg;
			}

			//写EPC
			//epc 后12位
			byte[] write_epc = new byte[6];

			//0-1 充装介质
			System.arraycopy(c, 0, write_epc, 0, c.length);

			//2-3 下检周期+钢瓶状态
			String NextCheckDate = mSubmitQP.getNextCheckDate();
			NextCheckDate = NextCheckDate.replaceAll("-", "").substring(2,6);
			BigInteger next = new BigInteger(NextCheckDate, 10);
			BigInteger w2 = new BigInteger(RfidUtils.LeftAddString(next.toString(2), 14, "0") + "00", 2);//00合格01报废10停用2
			byte[] write2 = RfidUtils.hexStringToBytes(RfidUtils.LeftAddString(w2.toString(16), 4, "0"));
			System.arraycopy(write2, 0, write_epc, 2, write2.length);

			//4-5 气瓶种类+签发校验区
			String tmp = "01";

			BigInteger w3 = new BigInteger( tmp + "11111111111111", 2);//起始2位二进制：0:00散瓶, 1:01集格, 2:10集格内瓶
			byte[] write3  = RfidUtils.hexStringToBytes(RfidUtils.LeftAddString(w3.toString(16), 4, "0"));//
			System.arraycopy(write3, 0, write_epc, 4, write3.length);

			for (int i = 0; i < write_epc.length; i++) {
				write_epc[i] = (byte) (write_epc[i] ^ 0x34);
			}

			boolean wepc = mReader.writeData("31064434",
					BankEnum.valueOf("TID"), 0, 96, TagID,
					BankEnum.valueOf("UII"),5, 3,
					RfidUtils.bytesToHexString(write_epc));

			if(wepc) {
				msg.setRespCode(0);
				msg.setRespMsg("写标签EPC区成功");
			}else{
				msg.setRespCode(1);
				msg.setRespMsg("写标签EPC区失败");
				return msg;
			}

			//ws
			QPDJCode info = new QPDJCode();
			info.setBottleKindCode(mSubmitQP.getStandNo().substring(0,2));
			info.setPropertyUnitCode(mRfid.getCQDW());
			info.setUseRegCode(mRfid.getLabelNo());
			info.setMediumCode(mSubmitQP.getMediumCode());
			info.setMakeDate(mSubmitQP.getMakeDate());
			info.setCheckDate(mSubmitQP.getCheckDate());
			info.setNextCheckDate(mSubmitQP.getNextCheckDate());
			info.setGPNO(mJgid);
			info.setTagID(TagID);

			List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("propertyName", "DeviceID");
			map1.put("propertyValue", DeviceID);
			propertyList.add(map1);

			ResponseData rd = new ResponseData();
			rd.setRespMsg(util.json.JSONUtils.toJsonWithGson(info));
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("propertyName", "RequestData");
			map2.put("propertyValue", util.json.JSONUtils.toJsonWithGson(rd));
			propertyList.add(map2);
			msg = WsUtils.CallWs(mContext, "QPXXCJ", propertyList);

			return msg;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			dialog = new ProgressDialog(mContext);
			dialog.setMessage("请勿移动，正在绑定标签...");
			dialog.setCancelable(false);
			dialog.show();

			mView.qpinfo3_read.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(ResponseData result) {
			// TODO Auto-generated method stub
			dialog.setCancelable(true);

			if(result.getRespCode()==0){
				dialog.dismiss();
				ToastUtil.showToast(getContext(), "绑定标签成功");
				setViewsEnabled(true);

				mView.qpinfo3_jgid.setText("");
				mView.qpinfo3_tag.setText("");

				CanRead = false;
				mView.qpinfo3_read.setEnabled(false);
				mView.qpinfo3_5.setText("");
				CleanList();
			}else{
				LastRfid = null;
				mView.qpinfo3_tag.setText("");

				dialog.setMessage("绑定标签失败："+result.getRespMsg());
				UiUtil.CloseDiag(dialog);

				CanRead = true;
				mView.qpinfo3_read.setEnabled(true);
			}

			super.onPostExecute(result);
		}

	}


	@Override
	public void ScanRfid(){
		if(hasRfid && CanRead){
			mView.qpinfo3_tag.setText("");

			String uii = mReader.inventorySingleTag();
			if (!TextUtils.isEmpty(uii)){
				String epc = mReader.convertUiiToEPC(uii);

				getRfid(RfidUtils.getDataFromEPC(epc));
			}
		}
	}

	static class InfoView{

		LinearLayout qpinfo3_ll1;//集格信息
		EditText qpinfo3_tag;
		EditText qpinfo3_jgid;
		Spinner qpinfo3_jgnum;

		Spinner qpinfo3_1;//产权单位
		Spinner qpinfo3_2;//气瓶规格
		Spinner qpinfo3_0;//介质
		Spinner qpinfo3_3;//定检机构
		EditDate qpinfo3_5;//下次检验日期

		hsic.ui.MyListView qpinfo3_list;//气瓶列表

		Button qpinfo3_clean;//清空
		Button qpinfo3_add;//添加集格气瓶
		Button qpinfo3_save;//提交登记
		Button qpinfo3_read;//绑定集格
		//======
		LinearLayout qpinfo3_ll2;//气瓶信息
		EditText info3_item_1;//瓶号
		EditText info3_item_2;//自编号
		EditText info3_item_3;//单位代码
		EditText info3_item_3s;//单位名称
		hsic.ui.EditDate info3_item_4;//制造日期
		EditText info3_item_5;//容积
		EditText info3_item_7;//壁厚
		EditText info3_item_8;//皮重
		EditText info3_item_9;//公称压力
		EditText info3_item_10;//水压
		EditText info3_item_11;//材质
		EditText info3_item_12;//孔隙
		EditText info3_item_13;//周期
		EditText info3_item_14;//年限
		EditText info3_item_6;//检验次数

		Button info3_item_back;//返回
		Button info3_item_save;//添加气瓶
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
				mView.qpinfo3_add.setEnabled(false);
				mView.qpinfo3_read.setEnabled(false);
				mView.qpinfo3_save.setEnabled(false);
				mView.qpinfo3_clean.setEnabled(false);
			}else{
				ToastUtil.showToast(getContext(), "RFID设备开启");
				hasRfid = true;
				mView.qpinfo3_read.setEnabled(false);
				mView.qpinfo3_add.setEnabled(true);
				mView.qpinfo3_save.setEnabled(true);
				mView.qpinfo3_clean.setEnabled(true);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if(Part==2){
					sqAdapter.notifyDataSetChanged();
					mView.qpinfo3_ll1.setVisibility(View.VISIBLE);
					mView.qpinfo3_ll2.setVisibility(View.GONE);
					Part = 1;
					CleanItem();
				}else{
					finish();
				}
				break;
		}
		return true;
	}
}
