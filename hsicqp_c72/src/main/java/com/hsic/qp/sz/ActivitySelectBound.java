package com.hsic.qp.sz;

import java.util.ArrayList;
import java.util.List;
import util.RfidUtils;
import util.ToastUtil;
import bean.Rfid;
import com.hsic.qp.sz.task.ScanTask;
import com.rscja.deviceapi.RFIDWithUHF;
import hsic.ui.HsicActivity;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ActivitySelectBound extends HsicActivity{
    private final static String MenuHOME = "返回";

    List<String> unboundList = new ArrayList<String>();
    List<String> boundList = new ArrayList<String>();

    ArrayAdapter<String> unboundAdapter= null;
    ArrayAdapter<String> boundAdapter= null;

    private RFIDWithUHF mReader;
    ScanTask rfidTask;
    boolean isStart = false;

    static class mView{
        TextView num;
        RadioGroup rg;//bound_rb_1-未绑定 bound_rb_2-绑定
        RadioButton bound_rb_1;
        RadioButton bound_rb_2;
        ListView lv;

        Button btn1;
        Button btn2;

//		ArrayAdapter<String> boundAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, items);
//		mV.lv.setAdapter(boundAdapter);
    }
    mView mV;

    private Context getContext(){
        return ActivitySelectBound.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectbound);

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
        mV.num = (TextView) findViewById(R.id.bound_0);
        mV.rg = (RadioGroup) findViewById(R.id.bound_group);
        mV.bound_rb_1 = (RadioButton)findViewById(R.id.bound_rb_1);
        mV.bound_rb_2 = (RadioButton)findViewById(R.id.bound_rb_2);
        mV.lv = (ListView) findViewById(R.id.bound_list);

        mV.btn1 = (Button) findViewById(R.id.bound_btn1);
        mV.btn2 = (Button) findViewById(R.id.bound_btn2);

        unboundAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, unboundList);
        boundAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, boundList);

        mV.lv.setAdapter(unboundAdapter);

        cleanView();
        mV.bound_rb_1.setChecked(true);
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

        mV.rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId == R.id.bound_rb_1){
                    mV.lv.setAdapter(unboundAdapter);
                }else if(checkedId == R.id.bound_rb_2){
                    mV.lv.setAdapter(boundAdapter);
                }
            }
        });
    }

    private void cleanView(){
        unboundList.clear();
        boundList.clear();

        unboundAdapter.notifyDataSetChanged();
        boundAdapter.notifyDataSetChanged();

        mV.num.setText("总数:0");
        mV.bound_rb_1.setText("未绑定 (0)");
        mV.bound_rb_2.setText("已绑定 (0)");
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

        if(RfidUtils.isBound(rfid.getEPC())){
            if(!boundList.contains(rfid.getQPDJCode())){
                boundList.add(rfid.getQPDJCode());
                Log.e("isBound", "已绑定");

                boundAdapter.notifyDataSetChanged();

                mV.num.setText("未绑定:"+unboundList.size()+" ,已绑定:"+(boundList.size()));

                mV.bound_rb_2.setText("已绑定 ("+boundList.size()+")");
                util.SoundUtil.play();
            }else return ;
        }else{
            if(!unboundList.contains(rfid.getQPDJCode())){
                unboundList.add(rfid.getQPDJCode());
                Log.e("isBound", "未绑定");

                unboundAdapter.notifyDataSetChanged();

                mV.bound_rb_1.setText("未绑定 ("+unboundList.size()+")");
                util.SoundUtil.play();
            }else return ;
        }

        mV.num.setText("总数:" + (unboundList.size() + boundList.size()));

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

            boolean pow = mReader.setPower(power);//5-30
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


}
