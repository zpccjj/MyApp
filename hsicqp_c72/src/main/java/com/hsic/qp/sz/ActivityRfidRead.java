package com.hsic.qp.sz;

import java.util.List;
import util.RfidUtils;
import util.ToastUtil;
import bean.GasBaseInfo;
import bean.ResponseData;
import bean.Rfid;
import com.google.gson.reflect.TypeToken;
import com.hsic.qp.sz.adapter.ItemReadAdapter;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;
import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.RFIDWithUHF.BankEnum;
import data.ConfigData;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import hsic.ui.HsicActivity;
import hsic.ui.MyListView;


public class ActivityRfidRead extends HsicActivity implements WsListener{
    private final static String MenuHOME = "返回";
    private RFIDWithUHF mReader;
    private boolean CanRfid = false;
    private boolean IsReading = false;
    private Context getContext(){
        return ActivityRfidRead.this;
    }

    static class mView{
        TextView read_1;
        TextView read_2;
        TextView read_3;
        TextView read_4;
        TextView read_5t;
        TextView read_5;
        TextView read_6;
        TableRow read_6tr;
        MyListView read_listview;

        Button btn_clean;
        Button btn_read;
    }
    mView mV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfidread);

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(MenuHOME);

        initViews();

        new InitTask(getContext()).execute();
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


    private void initViews(){
        mV = new mView();
        mV.read_1 = (TextView) findViewById(R.id.read_1);
        mV.read_2 = (TextView) findViewById(R.id.read_2);
        mV.read_3 = (TextView) findViewById(R.id.read_3);
        mV.read_4 = (TextView) findViewById(R.id.read_4);
        mV.read_5 = (TextView) findViewById(R.id.read_5);
        mV.read_5t = (TextView) findViewById(R.id.read_5t);
        mV.read_6 = (TextView) findViewById(R.id.read_6);
        mV.read_6tr = (TableRow) findViewById(R.id.read_6tr);
        mV.read_listview = (MyListView) findViewById(R.id.read_list);

        mV.btn_clean = (Button) findViewById(R.id.read_btn_clean);
        mV.btn_read = (Button) findViewById(R.id.read_btn_read);

        mV.read_5t.setText("气瓶编号:");
        mV.read_6tr.setVisibility(View.GONE);

        mV.btn_clean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Clean();
            }
        });

        mV.btn_read.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Clean();
                ScanRfid();
            }
        });

    }

    private void Clean(){
        mV.read_1.setText("");
        mV.read_3.setText("");
        mV.read_4.setText("");
        mV.read_5.setText("");
        mV.read_5t.setText("");
        mV.read_6.setText("");
        mV.read_listview.setAdapter(null);
    }

//	public void getRfid(String txt) {
//		// TODO Auto-generated method stub
//		Rfid rfid = null;
//
//		try {
//			rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(txt, Rfid.class);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			ToastUtil.showToast(getContext(), "标签数据错误");
//			return ;
//		}
//		if(rfid==null ||rfid.getCQDW()==null || rfid.getLabelNo()==null){
//			ToastUtil.showToast(getContext(), "未读到标签");
//			return ;
//		}
//		rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());
//
//		if(!rfid.getVersion().equals("0101")){
//			ToastUtil.showToast(getContext(), "版本号错误");
//			return ;
//		}
//
//		mV.read_1.setText(rfid.getQPDJCode()==null ? "" : rfid.getQPDJCode());
//
//		if(RfidUtils.isBound(rfid.getEPC())){
//			mV.read_5.setText("");
//			mV.read_3.setText(rfid.getNextCheckDate()==null ? "" : rfid.getNextCheckDate());
//			if(rfid.getCZJZCode()==null || rfid.getCZJZCode().length()==0) mV.read_4.setText("");
//			else{
//				String Media = ConfigData.getMediaName(rfid.getCZJZCode(), getApp().getMediaInfo());
//				if(Media.length()==0) mV.read_4.setText("未知介质代码:"+rfid.getCZJZCode());
//				else mV.read_4.setText(Media);
//			}
//		}else{
//			mV.read_3.setText("");
//			mV.read_4.setText("");
//			mV.read_5.setText("标签未绑定");
//		}
//
//		util.SoundUtil.play();
//	}

    @Override
    public void ScanRfid(){
        if(CanRfid && !IsReading){

//		String uii = mReader.inventorySingleTag();
//		if (!TextUtils.isEmpty(uii)){
//			String epc = mReader.convertUiiToEPC(uii);
//			Log.e("inventorySingleTag", uii);
//			Log.e("convertUiiToEPC", epc);
//
//			getRfid(RfidUtils.getDataFromEPC(epc));
//		}
            new ReadUserTask(getContext()).execute(mReader);
        }
    }

    private class ReadUserTask  extends AsyncTask<RFIDWithUHF, Void, ResponseData> {
        Context mContext;
        ProgressDialog dialog;

        public ReadUserTask(Context context){
            mContext = context;
        }

        @Override
        protected ResponseData doInBackground(RFIDWithUHF... params) {
            // TODO Auto-generated method stub
            ResponseData ret = new ResponseData();
            String uii = params[0].inventorySingleTag();
            if (!TextUtils.isEmpty(uii)){
                String epc = params[0].convertUiiToEPC(uii);
                Rfid rfid = null;

                try {
                    rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(RfidUtils.getDataFromEPC(epc), Rfid.class);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    ret.setRespCode(1);
                    ret.setRespMsg("标签数据错误");
                    return ret;
                }
                if(rfid==null ||rfid.getCQDW()==null || rfid.getLabelNo()==null){
                    ret.setRespCode(1);
                    ret.setRespMsg("未读到标签");
                    return ret;
                }
                rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());

                if(!rfid.getVersion().equals("0101")){
                    ret.setRespCode(1);
                    ret.setRespMsg("版本号错误");
                    return ret;
                }

                if(!RfidUtils.isBound(rfid.getEPC())){
                    ret.setRespCode(1);
                    ret.setRespMsg("标签未绑定");
                    return ret;
                }

                //user
                String User = params[0].readData("00000000",
                        BankEnum.valueOf("UII"), 4*8, 96, rfid.getEPC(),
                        BankEnum.valueOf("USER"), 0, 13);
                if(User==null || User.length()!=52){
                    ret.setRespCode(1);
                    ret.setRespMsg("USER区读取错误");
                    return ret;
                }
                RfidUtils.getDataFromUser(rfid, User);

                ret.setRespCode(0);
                ret.setRespMsg(util.json.JSONUtils.toJsonWithGson(rfid));
            }else{
                ret.setRespCode(1);
                ret.setRespMsg("未读到标签");
            }

            return ret;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            dialog = new ProgressDialog(mContext);
            dialog.setMessage("正在读取标签...");
            dialog.setCancelable(false);
            dialog.show();
            IsReading = true;

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResponseData result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            IsReading = false;
            if(result.getRespCode()==0){
                Rfid rfid = null;

                try {
                    rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(result.getRespMsg(), Rfid.class);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    ToastUtil.showToast(getContext(), "标签数据错误");
                    return ;
                }
                mV.read_1.setText(rfid.getQPDJCode()==null ? "" : rfid.getQPDJCode());
                mV.read_2.setText(rfid.getCheckDate()==null ? "" : setDate(rfid.getCheckDate()));
                mV.read_3.setText(rfid.getNextCheckDate()==null ? "" : setDate(rfid.getNextCheckDate()));
                if(rfid.getCZJZCode()==null || rfid.getCZJZCode().length()==0) mV.read_4.setText("");
                else{
                    String Media = ConfigData.getMediaName(rfid.getCZJZCode(), getApp().getMediaInfo());
                    if(Media.length()==0) mV.read_4.setText("未知介质代码:"+rfid.getCZJZCode());
                    else mV.read_4.setText(Media);
                }

                mV.read_5.setText(rfid.getQPNO()==null ? "" : rfid.getQPNO());
                if(rfid.getIsJG()==1){
                    mV.read_5t.setText("集格编号:");
                    mV.read_6.setText("");
                    if(rfid.getQPNO()!=null && rfid.getQPNO().length()>4){
                        try {
                            mV.read_6.setText(String.valueOf(Integer.valueOf(rfid.getQPNO().substring(2, 4))));
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                    mV.read_6tr.setVisibility(View.VISIBLE);

                    if(rfid.getQPDJCode()!=null && rfid.getQPDJCode().length()>0)
                        new CallRfidWsTask(getContext(), ActivityRfidRead.this, 13).execute(rfid.getQPDJCode());
                }else{
                    mV.read_listview.setAdapter(null);
                    mV.read_5t.setText("气瓶编号:");
                    mV.read_6.setText("");
                    mV.read_6tr.setVisibility(View.GONE);
                }

                util.SoundUtil.play();
            }else{
                ToastUtil.showToast(getContext(), result.getRespMsg());
            }
        }

//		int yy = Integer.valueOf(MadeDate.substring(0,2));
//		String YY = "20";
//		if(yy>=80) YY = "19";
//		MadeDate = YY+ MadeDate.substring(0,2) + "-" + MadeDate.substring(2,4) + "-" + MadeDate.substring(4,6);
    }

    private String setDate(String MadeDate){
        int yy = Integer.valueOf(MadeDate.substring(0,2));
        String YY = "20";
        if(yy>=80) YY = "19";
        String ret = YY+ MadeDate.substring(0,2) + "-" + MadeDate.substring(2,4);
        if(MadeDate.length()==6)
            ret += "-" + MadeDate.substring(4,6);
        return ret;
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
                mV.btn_read.setEnabled(true);
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            //mV.btn1.setEnabled(false);
            mV.btn_read.setEnabled(false);

            mypDialog = new ProgressDialog(mContext);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("正在打开RFID设备...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }

    @Override
    public void WsFinish(boolean isSuccess, int code, String retData) {
        // TODO Auto-generated method stub
        if(isSuccess){
            if(code==13){
                try {
                    List<GasBaseInfo> list = util.json.JSONUtils.toListWithGson(retData,  new TypeToken<List<GasBaseInfo>>(){}.getType());

                    mV.read_listview.setAdapter(new ItemReadAdapter(getContext(), list));
                } catch (Exception e) {
                    // TODO: handle exception
                    ToastUtil.showToast(getContext(), "集格内气瓶信息错误");
                    mV.read_listview.setAdapter(null);
                }
            }
        }else{
            ToastUtil.showToast(getContext(), retData);
        }
    }
}
