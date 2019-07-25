package com.hsic.qp.sz;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.rscja.deviceapi.RFIDWithUHF;

import bean.Rfid;
import data.ConfigData;
import hsic.ui.HsicActivity;
import util.RfidUtils;
import util.ToastUtil;


public class ActivityRfidRead extends HsicActivity{
    private final static String MenuHOME = "返回";
    private RFIDWithUHF mReader;

    private Context getContext(){
        return ActivityRfidRead.this;
    }

    static class mView{
        TextView read_1;
        TextView read_3;
        TextView read_4;
        TextView read_5;

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
        mV.read_3 = (TextView) findViewById(R.id.read_3);
        mV.read_4 = (TextView) findViewById(R.id.read_4);
        mV.read_5 = (TextView) findViewById(R.id.read_5);

        mV.btn_clean = (Button) findViewById(R.id.read_btn_clean);
        mV.btn_read = (Button) findViewById(R.id.read_btn_read);

        mV.btn_clean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mV.read_1.setText("");
                mV.read_3.setText("");
                mV.read_4.setText("");
                mV.read_5.setText("");
            }
        });

        mV.btn_read.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mV.read_1.setText("");
                mV.read_3.setText("");
                mV.read_4.setText("");
                mV.read_5.setText("");
                ScanRfid();
            }
        });

    }

    public void getRfid(String txt) {
        // TODO Auto-generated method stub
        Rfid rfid = null;

        try {
            rfid = (Rfid) util.json.JSONUtils.toObjectWithGson(txt, Rfid.class);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            ToastUtil.showToast(getContext(), "标签数据错误");
            return ;
        }
        if(rfid==null ||rfid.getCQDW()==null || rfid.getLabelNo()==null){
            ToastUtil.showToast(getContext(), "未读到标签");
            return ;
        }
        rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());

        if(!rfid.getVersion().equals("0101")){
            ToastUtil.showToast(getContext(), "版本号错误");
            return ;
        }

        mV.read_1.setText(rfid.getQPDJCode()==null ? "" : rfid.getQPDJCode());

        if(RfidUtils.isBound(rfid.getEPC())){
            mV.read_3.setText(rfid.getNextCheckDate()==null ? "" : rfid.getNextCheckDate());
            if(rfid.getCZJZCode()==null || rfid.getCZJZCode().length()==0) mV.read_4.setText("");
            else{
                String Media = ConfigData.getMediaName(rfid.getCZJZCode(), getApp().getMediaInfo());
                if(Media.length()==0) mV.read_4.setText("未知介质代码:"+rfid.getCZJZCode());
                else mV.read_4.setText(Media);
            }
        }else{
            mV.read_5.setText("标签未绑定");
        }

        util.SoundUtil.play();
    }

    @Override
    public void ScanRfid(){

        String uii = mReader.inventorySingleTag();
        if (!TextUtils.isEmpty(uii)){
            String epc = mReader.convertUiiToEPC(uii);
            Log.e("inventorySingleTag", uii);
            Log.e("convertUiiToEPC", epc);

            getRfid(RfidUtils.getDataFromEPC(epc));
        }
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
}
