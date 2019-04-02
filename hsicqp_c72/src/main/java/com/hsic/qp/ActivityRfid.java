package com.hsic.qp;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hsic.qp.listener.WsListener;
import com.hsic.qp.task.CallRfidWsTask;
import com.hsic.qp.task.ScanTask;
import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.RFIDWithUHF.BankEnum;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.GasBaseInfo;
import bean.QPDJCode;
import bean.ResponseData;
import bean.Rfid;
import data.ConfigData;
import hsic.ui.HsicActivity;
import util.RfidUtils;
import util.ToastUtil;
import util.UiUtil;
import util.WsUtils;

public class ActivityRfid extends HsicActivity implements WsListener{
	private final static String MenuHOME = "返回";
	private RFIDWithUHF mReader;
	ScanTask rfidTask;
	static class mView{
		EditText rfid_1;
		TextView rfid_2;
		TextView rfid_3;
		TextView rfid_4;
		TextView rfid_5;


		Button btn1;
		Button btn2;
		Button btn3;
	}
	mView mV;
	String DeviceID;

	private Context getContext(){
		return ActivityRfid.this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rfid);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(MenuHOME);

		initViews();

		DeviceID = getSharedPreferences("DeviceSetting", 0).getString("DeviceID", "");

		new InitTask(getContext()).execute();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mReader != null) {
			boolean free = mReader.free();
			Log.e("===========free", String.valueOf(free));
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
		mV.rfid_1 = (EditText) findViewById(R.id.rfid_1);
		mV.rfid_2 = (TextView) findViewById(R.id.rfid_2);
		mV.rfid_3 = (TextView) findViewById(R.id.rfid_3);
		mV.rfid_4 = (TextView) findViewById(R.id.rfid_4);
		mV.rfid_5 = (TextView) findViewById(R.id.rfid_5);


		mV.btn1 = (Button) findViewById(R.id.rfid_btn1);
		mV.btn2 = (Button) findViewById(R.id.rfid_btn2);
		mV.btn3 = (Button) findViewById(R.id.rfid_btn3);

		mV.btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mV.rfid_1.getText().toString().trim().length()==0){
					ToastUtil.showToast(getContext(), "请输入钢瓶号");
					return;
				}
				setQpInfo(null);
				new CallRfidWsTask(getContext(), ActivityRfid.this, 7).execute(mV.rfid_1.getText().toString().trim());
			}
		});
		mV.btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScanRfid();
			}
		});
		mV.btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(GBI==null){
					ToastUtil.showToast(getContext(), "请先获取气瓶信息");
					return;
				}
				if(LastRfid==null){
					ToastUtil.showToast(getContext(), "请先读取标签");
					return;
				}

				if(GBI.getCZDW().equals(LastRfid.getCQDW())){
					new RfidTask(DeviceID, getContext(), LastRfid, GBI, UII).execute(mReader);
				}else{
					ToastUtil.showToast(getContext(), "标签产权单位与气瓶产权单位不相符");
				}
			}
		});
	}

	@Override
	public void closeRFID(){
		//	Log.e("HsicActivity closeRFID", txt);
		//	ToastUtil.showToast(getContext(), "finish scan");
		rfidTask = null;
	}

	Rfid LastRfid = null;
	String UII = "";

	public void getRfid(String txt) {
		// TODO Auto-generated method stub
		Rfid rfid = null;

		try {
			rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(txt, Rfid.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ;
		}
		if(rfid==null ||rfid.getCQDW()==null || rfid.getLabelNo()==null){

			LastRfid = null;
			mV.rfid_5.setText("");

			return ;
		}
		rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());

		if(!rfid.getVersion().equals("0101")){

			LastRfid = null;
			mV.rfid_5.setText("");

			return ;
		}

		if(!rfid.getCQDW().equals(getApp().getLogin().getStation())){
			ToastUtil.showToast(getContext(), "非产权单位标签");

			LastRfid = null;
			mV.rfid_5.setText("");

			return;
		}

		LastRfid = rfid;
		mV.rfid_5.setText(rfid.getQPDJCode());
		util.SoundUtil.play();
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
				ToastUtil.showToast(getContext(), "RFID设备开启");
				mV.btn1.setEnabled(true);
				mV.btn2.setEnabled(true);
				mV.btn3.setEnabled(true);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			mV.btn1.setEnabled(false);
			mV.btn2.setEnabled(false);
			mV.btn3.setEnabled(false);

			mypDialog = new ProgressDialog(mContext);
			mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mypDialog.setMessage("正在打开RFID设备...");
			mypDialog.setCanceledOnTouchOutside(false);
			mypDialog.show();
		}
	}



	public class RfidTask  extends AsyncTask<RFIDWithUHF, Void, ResponseData> {
		String TagID;

		String mID;
		Context mContext;
		Rfid mRfid;
		String mUII;
		GasBaseInfo mGbi;

		ProgressDialog dialog;

		public RfidTask(String id, Context context, Rfid rfid, GasBaseInfo gbi, String uii){
			mID = id;
			mContext = context;
			mRfid = rfid;
			mGbi = gbi;
			mUII = uii;
		}
		@Override
		protected ResponseData doInBackground(RFIDWithUHF... params) {
			// TODO Auto-generated method stub
			ResponseData msg = new ResponseData();

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
			m1.put("propertyValue", mID);
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
			String makeDate = mGbi.getMakeDate();
			makeDate = makeDate.replaceAll("-", "").substring(2,8);

			//Standno
			String Standno = mGbi.getStandNo();
			String p3_4 = RfidUtils.LeftAddString(Integer.toBinaryString(Integer.valueOf(makeDate)), 20, "0")
					+ RfidUtils.LeftAddString(Integer.toBinaryString(Integer.valueOf(Standno)), 12, "0");

			BigInteger b3_6 = new BigInteger(p3_4, 2);

			byte[] byte3_6 = RfidUtils.hexStringToBytes(b3_6.toString(16));
			//3-6
			System.arraycopy(byte3_6, 0, user, 3, byte3_6.length);

			//气瓶钢号
			String qp = mGbi.getGPNO();
			qp = RfidUtils.LeftAddString(qp, 12, " ");

			try {
				byte[] tmp = qp.getBytes("UTF-8");
				//7-18
				System.arraycopy(tmp, 0, user, 7, tmp.length);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			//4位单位代码+7位追溯码 19-23
			String QPDJCODE = mRfid.getQPDJCode();
			//5 byte
			BigInteger big = new BigInteger(QPDJCODE, 10);

			byte[] b = RfidUtils.hexStringToBytes( RfidUtils.LeftAddString(big.toString(16), 10, "0") );

			//19-23
			System.arraycopy(b, 0, user, 19, b.length);

			//充装介质 24-25
			BigInteger jz = new BigInteger(mGbi.getMediumCode(), 10);
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
			String NextCheckDate = mGbi.getNextCheckDate();
			NextCheckDate = NextCheckDate.replaceAll("-", "").substring(2,6);
			BigInteger next = new BigInteger(NextCheckDate, 10);
			BigInteger w2 = new BigInteger(RfidUtils.LeftAddString(next.toString(2), 14, "0") + "00", 2);//00合格01报废10停用
			byte[] write2 = RfidUtils.hexStringToBytes(RfidUtils.LeftAddString(w2.toString(16), 4, "0"));
			System.arraycopy(write2, 0, write_epc, 2, write2.length);

			//4-5 气瓶种类+签发校验区
			BigInteger w3 = new BigInteger("0011111111111111", 2);//起始2位二进制：00散瓶01集格02集格内瓶
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
			info.setBottleKindCode(mGbi.getStandNo().substring(0,2));
			info.setPropertyUnitCode(mGbi.getCZDW());
			info.setUseRegCode(mRfid.getLabelNo());
			info.setMediumCode(mGbi.getMediumCode());
			info.setMakeDate(mGbi.getMakeDate());
			info.setCheckDate(mGbi.getCheckDate());
			info.setNextCheckDate(mGbi.getNextCheckDate());
			info.setGPNO(mGbi.getGPNO());
			info.setTagID(TagID);

			List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("propertyName", "DeviceID");
			map1.put("propertyValue", mID);
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

			mV.btn1.setEnabled(false);
			mV.btn2.setEnabled(false);
			mV.btn3.setEnabled(false);
			TagID = "";
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(ResponseData result) {
			// TODO Auto-generated method stub
			mV.btn1.setEnabled(true);
			mV.btn2.setEnabled(true);
			mV.btn3.setEnabled(true);
			dialog.setCancelable(true);

			if(result.getRespCode()==0){
				dialog.dismiss();
				ToastUtil.showToast(getContext(), "绑定标签成功");
				setQpInfo(null);
				mV.rfid_1.setText("");
				LastRfid = null;
				mV.rfid_5.setText("");
			}else{
				dialog.setMessage("绑定标签失败："+result.getRespMsg());
				UiUtil.CloseDiag(dialog);
			}

			super.onPostExecute(result);
		}

	}
	@Override
	public void WsFinish(boolean isSuccess, int code, String retData) {
		// TODO Auto-generated method stub
		if(isSuccess){
			if(code==7){
				List<GasBaseInfo> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<GasBaseInfo>>(){}.getType());
				if(list!=null && list.size()>0){
					if(list.size()==1){
						setQpInfo(list.get(0));
					}else{
						DialogChoice(getContext(), list);
					}
				}else{
					ToastUtil.showToast(getContext(), "无气瓶瓶信息");
				}
			}
		}
	}

	GasBaseInfo GBI = null;
	private void setQpInfo(GasBaseInfo gbi){
		GBI = gbi;
		if(GBI!=null){
			mV.rfid_1.setText(GBI.getGPNO());
			mV.rfid_2.setText(GBI.getMakeDate());
			mV.rfid_3.setText(GBI.getNextCheckDate());
			mV.rfid_4.setText(ConfigData.getMediaName(GBI.getMediumCode()));
		}else{
			mV.rfid_2.setText("");
			mV.rfid_3.setText("");
			mV.rfid_4.setText("");
		}
	}

	AlertDialog mDialogChoice;
	private void DialogChoice(final Context context, final List<GasBaseInfo> list) {
		final int[] ChoiceID = {-1};
		final String items[] = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			items[i] = list.get(i).getGPNO();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context,3);
		builder.setTitle("选择气瓶");
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
				if(ChoiceID[0]==-1){
					ToastUtil.showToast(getContext(),  "请选择气瓶号");
					UiUtil.setDiagBtn(dialog, false);
				}else{
					setQpInfo(list.get(ChoiceID[0]));
					UiUtil.setDiagBtn(dialog, true);
				}
			}
		});
		builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UiUtil.setDiagBtn(dialog, true);
			}
		});

		mDialogChoice = builder.create();
		mDialogChoice.show();
	}

	@Override
	public void ScanRfid(){
		LastRfid = null;
		UII = "";

		String uii = mReader.inventorySingleTag();
		if (!TextUtils.isEmpty(uii)){
			String epc = mReader.convertUiiToEPC(uii);
			Log.e("inventorySingleTag", uii);
			Log.e("convertUiiToEPC", epc);

			getRfid(RfidUtils.getDataFromEPC(epc));
		}
	}
}
