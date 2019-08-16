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
import bean.BasicInfo;
import bean.BasicQPInfo;
import bean.BasicQPInfo2;
import bean.BasicUnit;
import bean.QPDJCode;
import bean.ResponseData;
import bean.Rfid;
import bean.SubmitQP;
import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;
import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.RFIDWithUHF.BankEnum;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ActivityQpInfo2 extends HsicActivity implements WsListener{
    private final static String MenuHOME = "返回";

    InfoView mView;
    private RFIDWithUHF mReader;
    SubmitQP mQp=null;

    List<BasicUnit> ZZDW;
    private Context getContext(){
        return ActivityQpInfo2.this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qpinfo2);

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(MenuHOME);

        initView();
        setListener();

        new InitTask(getContext()).execute();
        new CallRfidWsTask(getContext(), ActivityQpInfo2.this, 8).execute("");
        //    setData("");

    }

    @Override
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

    private void setListener(){
        mView.info_2.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                BasicQPInfo selectItem = (BasicQPInfo) parent.getSelectedItem();
                if(selectItem!=null && selectItem.getList()!=null){
                    ArrayAdapter<BasicQPInfo2> list2Adapter = new ArrayAdapter<BasicQPInfo2>(getContext(),android.R.layout.simple_spinner_item,
                            ((BasicQPInfo) parent.getSelectedItem()).getList());
                    list2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mView.info_8.setAdapter(list2Adapter);
                }else{
                    mView.info_8.setAdapter(null);
                    mView.info_9.setText("");
                    mView.info_10.setText("");
//					mView.info_11.setText("");
                    mView.info_12.setText("");
                    mView.info_13.setText("");
                    mView.info_14.setText("");
                    mView.info_15.setText("");
                    mView.info_16.setText("");
                    mView.info_17.setText("");
                    mView.info_18.setText("");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        mView.info_8.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                BasicQPInfo2 bqInfo2 = (BasicQPInfo2) parent.getSelectedItem();
                if(bqInfo2!=null){
                    mView.info_9.setText(bqInfo2.getRJ()!=null ? bqInfo2.getRJ() : "");
                    mView.info_10.setText(bqInfo2.getBH()!=null ? bqInfo2.getBH() : "");
//					mView.info_11.setText(bqInfo2.getKg()!=null ? bqInfo2.getKg() : "");
                    mView.info_12.setText(bqInfo2.getQPPZ()!=null ? bqInfo2.getQPPZ() : "");
                    mView.info_13.setText(bqInfo2.getWorkMpa()!=null ? bqInfo2.getWorkMpa() : "");
                    mView.info_14.setText(bqInfo2.getWaterMpa()!=null ? bqInfo2.getWaterMpa() : "");
                    mView.info_15.setText(bqInfo2.getBTSL()!=null ? bqInfo2.getBTSL() : "");
                    mView.info_16.setText(bqInfo2.getTLKXL()!=null ? bqInfo2.getTLKXL() : "");
                    mView.info_17.setText(bqInfo2.getJCZQ()!=null ? bqInfo2.getJCZQ() : "");
                    mView.info_18.setText(bqInfo2.getSYQX()!=null ? bqInfo2.getSYQX() : "");
                }else{
                    mView.info_9.setText("");
                    mView.info_10.setText("");
//					mView.info_11.setText("");
                    mView.info_12.setText("");
                    mView.info_13.setText("");
                    mView.info_14.setText("");
                    mView.info_15.setText("");
                    mView.info_16.setText("");
                    mView.info_17.setText("");
                    mView.info_18.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });


        mView.info_3.setTransformationMethod(new AllCapTransformationMethod ());
        mView.info_5.setTransformationMethod(new AllCapTransformationMethod ());

        mView.info_5.addTextChangedListener(new TextWatcher() {

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
                        if(ZZDW.get(i).getId().equals(mView.info_5.getText().toString().trim().toUpperCase())){
                            mView.info_5s.setText(ZZDW.get(i).getName());
                            break;
                        }else{
                            mView.info_5s.setText("");
                        }
                    }
                }
            }
        });

        mView.info_7.addTextChangedListener(new TextWatcher() {

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
                if(s.toString().length()>0 && mView.info_17.getText().toString().length()>0){
                    try {
                        int start = Integer.valueOf(s.toString().substring(0, 4));
                        int zq = Integer.valueOf(mView.info_17.getText().toString());
                        int now = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));

                        mView.info_19.setText(String.valueOf( ((now-start) / zq )) );
                    } catch (Exception e) {
                        // TODO: handle exception
                        mView.info_19.setText("0");
                    }
                }else{
                    mView.info_19.setText("0");
                }
            }
        });

        mView.btn_clean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UiUtil.CloseKey(ActivityQpInfo2.this);

                clean();
            }
        });

        mView.btn_read.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UiUtil.CloseKey(ActivityQpInfo2.this);

                ScanRfid();
            }
        });

        mView.btn_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UiUtil.CloseKey(ActivityQpInfo2.this);
                BasicUnit cqdw = (BasicUnit) mView.info_1.getSelectedItem();
                if(cqdw==null){
                    ToastUtil.showToast(getContext(), "请选择产权单位");
                    return ;
                }


                BasicQPInfo gplx = (BasicQPInfo) mView.info_2.getSelectedItem();
                if(gplx==null || gplx.getId()==null || gplx.getId().length()==0){
                    ToastUtil.showToast(getContext(), "请选择气瓶类型");
                    return ;
                }

                if(mView.info_3.getText().toString().trim().toUpperCase().length()==0){
                    ToastUtil.showToast(getContext(), "请输入气瓶编号");
                    return ;
                }
                if(mView.info_5.getText().toString().trim().toUpperCase().length()==0){
                    ToastUtil.showToast(getContext(), "请输入气瓶制造单位代码");
                    return ;
                }
                if(mView.info_5s.getText().toString().trim().length()==0){
                    ToastUtil.showToast(getContext(), "气瓶制造单位代码错误");
                    return ;
                }
                BasicUnit jcdw = (BasicUnit) mView.info_6.getSelectedItem();
                if(jcdw==null){
                    ToastUtil.showToast(getContext(), "请选择检验单位");
                    return ;
                }
                if(mView.info_7.getText().toString().trim().length()==0){
                    ToastUtil.showToast(getContext(), "请输入气瓶制造日期");
                    return ;
                }
                if(mView.info_20.getText().toString().trim().length()==0){
                    ToastUtil.showToast(getContext(), "请输入下次检验日期");
                    return ;
                }
                BasicQPInfo2 czjz = (BasicQPInfo2) mView.info_8.getSelectedItem();
                if(czjz==null){
                    ToastUtil.showToast(getContext(), "请选择充装介质");
                    return ;
                }
                if(mView.info_9.getText().toString().trim().length()==0){
                    ToastUtil.showToast(getContext(), "请输入实际容积");
                    return ;
                }
                if(mView.info_10.getText().toString().trim().length()==0){
                    ToastUtil.showToast(getContext(), "请输入瓶体壁厚");
                    return ;
                }
                if(mView.info_12.getText().toString().trim().length()==0){
                    ToastUtil.showToast(getContext(), "请输入皮重");
                    return ;
                }
                if(mView.info_13.getText().toString().trim().length()==0){
                    ToastUtil.showToast(getContext(), "请输入公称工作压力");
                    return ;
                }
                if(mView.info_14.getText().toString().trim().length()==0){
                    ToastUtil.showToast(getContext(), "请输入水压测试压力");
                    return ;
                }
                //

                mQp = new SubmitQP();
                mQp.setCQDW(cqdw.getId()); mQp.setCZDW(cqdw.getId());
                mQp.setStandNo(gplx.getId());
                mQp.setGPNO(mView.info_3.getText().toString().trim().toUpperCase());
                mQp.setYHNO(mView.info_4.getText().toString().trim());
                mQp.setMadeName(mView.info_5s.getText().toString().trim());
                mQp.setJCDW(jcdw.getId());
                mQp.setMakeDate(mView.info_7.getText().toString().trim());
                mQp.setNextCheckDate(mView.info_20.getText().toString().trim());
                mQp.setMediumCode(czjz.getId());
                mQp.setL(mView.info_9.getText().toString().trim());
                mQp.setMM(mView.info_10.getText().toString().trim());
                mQp.setKg(mView.info_12.getText().toString().trim());
                mQp.setQPPZ(mView.info_12.getText().toString().trim());
                mQp.setWorkMpa(mView.info_13.getText().toString().trim());
                mQp.setWaterMpa(mView.info_14.getText().toString().trim());
                mQp.setBTSL(mView.info_15.getText().toString().trim());
                mQp.setTLKXL(mView.info_16.getText().toString().trim());
                mQp.setJCZQ(mView.info_17.getText().toString().trim());
                mQp.setSYQX(mView.info_18.getText().toString().trim());
                mQp.setJCCS(mView.info_19.getText().toString().trim());

                int zq = Integer.valueOf(mView.info_17.getText().toString().trim());
                int next = Integer.valueOf(mView.info_20.getText().toString().substring(0, 4));
                int check = next - zq;

                mQp.setCheckDate(String.valueOf(check) + mView.info_20.getText().toString().substring(4, mView.info_20.getText().toString().length()));

                mQp.setOPID(getApp().getLogin().getUserID());
                //
                Log.e("QP===", util.json.JSONUtils.toJsonWithGson(mQp));

                new CallRfidWsTask(getContext(), ActivityQpInfo2.this, 9).execute(util.json.JSONUtils.toJsonWithGson(mQp));
            }
        });

    }

    private class RfidTask  extends AsyncTask<RFIDWithUHF, Void, ResponseData> {
        Context mContext;
        Rfid mRfid;
        SubmitQP mSubmitQP;
        ProgressDialog dialog;

        public RfidTask(Context context, Rfid rfid, SubmitQP qp){
            mContext = context;
            mRfid = rfid;
            mSubmitQP = qp;
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

            //气瓶钢号
            String qp = mSubmitQP.getGPNO();
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
            //String jgtype = mSubmitQP.getJGType();
            String tmp = "00";
//			if(jgtype!=null){
//				if(jgtype.equals("0")) tmp = "00";
//				else if(jgtype.equals("1")) tmp = "01";
//				else if(jgtype.equals("2")) tmp = "10";
//			}

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
            info.setGPNO(mSubmitQP.getGPNO());
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

            mView.btn_read.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ResponseData result) {
            // TODO Auto-generated method stub
            mView.btn_read.setEnabled(false);
            dialog.setCancelable(true);

            if(result.getRespCode()==0){
                dialog.dismiss();
                ToastUtil.showToast(getContext(), "绑定标签成功");
                setViewsEnabled(true);
                clean();
                mView.info_3.setFocusable(true);
                mView.info_3.setFocusableInTouchMode(true);
                mView.info_3.requestFocus();
                mView.info_3.requestFocusFromTouch();

                CanRead = false;
                mView.btn_read.setEnabled(false);
            }else{
                LastRfid = null;
                mView.info_tag.setText("");

                dialog.setMessage("绑定标签失败："+result.getRespMsg());
                UiUtil.CloseDiag(dialog);

                CanRead = true;
                mView.btn_read.setEnabled(true);
            }

            super.onPostExecute(result);
        }

    }

    private void clean(){
        LastRfid = null;
        mQp = null;
        mView.info_tag.setText("");
        mView.info_3.setText("");
        mView.info_4.setText("");
        mView.info_5.setText("");
        mView.info_7.setText("");
        mView.info_20.setText("");
    }


    private void initView(){
        mView = new InfoView();
        mView.info_tag = (EditText) findViewById(R.id.info_tag);
        mView.info_1 = (Spinner) findViewById(R.id.info_1);
        mView.info_2 = (Spinner) findViewById(R.id.info_2);
        mView.info_3 = (EditText) findViewById(R.id.info_3);
        mView.info_4 = (EditText) findViewById(R.id.info_4);
        mView.info_5 = (EditText) findViewById(R.id.info_5);
        mView.info_5s = (EditText) findViewById(R.id.info_5s);
        mView.info_6 = (Spinner) findViewById(R.id.info_6);
        mView.info_7 = (EditDate) findViewById(R.id.info_7);
        mView.info_8 = (Spinner) findViewById(R.id.info_8);
        mView.info_9 = (EditText) findViewById(R.id.info_9);
        mView.info_10 = (EditText) findViewById(R.id.info_10);
//		mView.info_11 = (EditText) findViewById(R.id.info_11);
        mView.info_12 = (EditText) findViewById(R.id.info_12);
        mView.info_13 = (EditText) findViewById(R.id.info_13);
        mView.info_14 = (EditText) findViewById(R.id.info_14);
        mView.info_15 = (EditText) findViewById(R.id.info_15);
        mView.info_16 = (EditText) findViewById(R.id.info_16);
        mView.info_17 = (EditText) findViewById(R.id.info_17);
        mView.info_18 = (EditText) findViewById(R.id.info_18);
        mView.info_19 = (EditText) findViewById(R.id.info_19);
        mView.info_20 = (EditDate) findViewById(R.id.info_20);

        mView.btn_clean = (Button) findViewById(R.id.qpinfo_clean);
        mView.btn_read = (Button) findViewById(R.id.qpinfo_read);
        mView.btn_save = (Button) findViewById(R.id.qpinfo_save);

        mView.info_5s.setEnabled(false);
        mView.info_17.setEnabled(false);
        mView.info_18.setEnabled(false);
        mView.info_19.setEnabled(false);

    }

    static class InfoView{
        EditText info_tag;
        Spinner info_1;
        Spinner info_2;
        EditText info_3;
        EditText info_4;
        EditText info_5;
        EditText info_5s;
        Spinner info_6;
        EditDate info_7;
        Spinner info_8;
        EditText info_9;
        EditText info_10;
        //		EditText info_11;
        EditText info_12;
        EditText info_13;
        EditText info_14;
        EditText info_15;
        EditText info_16;
        EditText info_17;
        EditText info_18;
        EditText info_19;
        EditDate info_20;

        Button btn_clean;
        Button btn_save;
        Button btn_read;
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
        mView.info_1.setAdapter(CQDWAdapter);
        for (int k = 0; k < bi.getCQDW().size(); k++) {
            if(bi.getCQDW().get(k).getId().equals("1702")){
                mView.info_1.setSelection(k);
                break;
            }
        }

        ArrayAdapter<BasicUnit> JYDWAdapter = new ArrayAdapter<BasicUnit>(getContext(),android.R.layout.simple_spinner_item, bi.getJYDW());
        JYDWAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mView.info_6.setAdapter(JYDWAdapter);

        Log.e("mView.info_2", util.json.JSONUtils.toJsonWithGson(mInfo));
        ArrayAdapter<BasicQPInfo> listAdapter = new ArrayAdapter<BasicQPInfo>(getContext(),android.R.layout.simple_spinner_item, mInfo);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mView.info_2.setAdapter(listAdapter);



        for (int j = 0; j < mInfo.size(); j++) {
            if(mInfo.get(j).getId().equals("0202")){
                mView.info_2.setSelection(j);
                break;
            }
        }
    }

    private void setViewsEnabled(boolean ret){
        mView.info_3.setEnabled(ret);
        mView.info_1.setEnabled(ret);
        mView.info_2.setEnabled(ret);
        mView.info_3.setEnabled(ret);
        mView.info_4.setEnabled(ret);
        mView.info_5.setEnabled(ret);
        mView.info_6.setEnabled(ret);
        mView.info_7.setEnabled(ret);
        mView.info_8.setEnabled(ret);
        mView.info_9.setEnabled(ret);
        mView.info_10.setEnabled(ret);
        mView.info_12.setEnabled(ret);
        mView.info_13.setEnabled(ret);
        mView.info_14.setEnabled(ret);
        mView.info_15.setEnabled(ret);
        mView.info_16.setEnabled(ret);
        mView.info_20.setEnabled(ret);

        mView.btn_save.setEnabled(ret);
        mView.btn_clean.setEnabled(ret);
    }

    @Override
    public void WsFinish(boolean isSuccess, int code, String retData) {
        // TODO Auto-generated method stub
        if(isSuccess){
            if(code==9){
                ToastUtil.showToast(getContext(), "登记气瓶基本信息成功");

                setViewsEnabled(false);
                CanRead = true;
                if(hasRfid)
                    mView.btn_read.setEnabled(true);
            }else if(code==8){
                setData(retData);
            }
        }else{
            if(code==9){
                mQp = null;
            }
        }
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
            ToastUtil.showToast(getContext(), "标签数据错误");
            LastRfid = null;
            mView.info_tag.setText("");
            return ;
        }
        if(rfid==null ||rfid.getCQDW()==null || rfid.getLabelNo()==null){

            LastRfid = null;
            mView.info_tag.setText("");
            ToastUtil.showToast(getContext(), "未读到标签");
            return ;
        }
        rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());

        if(!rfid.getVersion().equals("0101")){

            LastRfid = null;
            mView.info_tag.setText("");
            ToastUtil.showToast(getContext(), "版本号错误");
            return ;
        }

        LastRfid = rfid;
        mView.info_tag.setText(rfid.getQPDJCode());

        util.SoundUtil.play();

        CanRead = false;
        mView.btn_read.setEnabled(false);
        ConfirmDialog dialog = new ConfirmDialog(getContext());
        dialog.setCancelable(false);
        dialog.setTitle("绑定");
        dialog.setMessage("标签号: " + rfid.getQPDJCode());
        dialog.setConfirmButton("绑定", new android.content.DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                UiUtil.CloseKey(ActivityQpInfo2.this);

                if(mQp==null){
                    ToastUtil.showToast(getContext(), "请先登记气瓶基本信息");
                    return ;
                }

                if(LastRfid==null){
                    ToastUtil.showToast(getContext(), "请先读取标签号");
                    return ;
                }
                if(mView.info_tag.getText().toString().trim().length()==0){
                    ToastUtil.showToast(getContext(), "请先读取标签号");
                    return ;
                }

                new RfidTask(getContext(), LastRfid, mQp).execute(mReader);
            }

        });

        dialog.setCancelButton(new android.content.DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                CanRead = true;
                mView.btn_read.setEnabled(true);
            }

        });

        dialog.show();
    }
    boolean CanRead = false;

    @Override
    public void ScanRfid(){
        if(hasRfid && CanRead){
            LastRfid = null;
            mView.info_tag.setText("");

            String uii = mReader.inventorySingleTag();
            if (!TextUtils.isEmpty(uii)){
                String epc = mReader.convertUiiToEPC(uii);
                Log.e("inventorySingleTag", uii);
                Log.e("convertUiiToEPC", epc);

                getRfid(RfidUtils.getDataFromEPC(epc));
            }
        }
    }
    /**
     * 设备上电异步类
     */
    private boolean hasRfid = false;
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
                mView.btn_read.setEnabled(false);
                mView.btn_save.setEnabled(false);
                mView.btn_clean.setEnabled(false);
            }else{
                ToastUtil.showToast(getContext(), "RFID设备开启");
                hasRfid = true;
                mView.btn_read.setEnabled(false);
                mView.btn_save.setEnabled(true);
                mView.btn_clean.setEnabled(true);
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

}
