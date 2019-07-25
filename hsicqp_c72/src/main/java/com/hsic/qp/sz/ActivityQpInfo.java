package com.hsic.qp.sz;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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

import com.hsic.qp.sz.listener.WsListener;
import com.hsic.qp.sz.task.CallRfidWsTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.BasicInfo;
import bean.BasicQPInfo;
import bean.BasicQPInfo2;
import bean.BasicUnit;
import bean.SubmitQP;
import hsic.ui.EditDate;
import hsic.ui.HsicActivity;
import util.AllCapTransformationMethod;
import util.ToastUtil;
import util.UiUtil;

public class ActivityQpInfo extends HsicActivity implements WsListener{
    private final static String MenuHOME = "返回";
    InfoView mView;


    List<BasicUnit> ZZDW;
    private Context getContext(){
        return ActivityQpInfo.this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qpinfo);

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(MenuHOME);

        initView();
        setListener();


        new CallRfidWsTask(getContext(), ActivityQpInfo.this, 8).execute("");
        //    setData("");
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
                setCheckTimes();
            }
        });

        mView.info_17.addTextChangedListener(new TextWatcher() {

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
                setCheckTimes();
            }
        });

        mView.btn_clean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UiUtil.CloseKey(ActivityQpInfo.this);

                clean();

            }
        });

        mView.btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UiUtil.CloseKey(ActivityQpInfo.this);

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
//				if(mView.info_11.getText().toString().trim().length()==0){
//					ToastUtil.showToast(getContext(), "请输入实际重量");
//					return ;
//				}
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

                SubmitQP qp = new SubmitQP();
                qp.setCQDW(cqdw.getId()); qp.setCZDW(cqdw.getId());
                qp.setStandNo(gplx.getId());
                qp.setGPNO(mView.info_3.getText().toString().trim().toUpperCase());
                qp.setYHNO(mView.info_4.getText().toString().trim());
                qp.setMadeName(mView.info_5s.getText().toString().trim());
                qp.setJCDW(jcdw.getId());
                qp.setMakeDate(mView.info_7.getText().toString().trim());
                qp.setNextCheckDate(mView.info_20.getText().toString().trim());
                qp.setMediumCode(czjz.getId());
                qp.setL(mView.info_9.getText().toString().trim());
                qp.setMM(mView.info_10.getText().toString().trim());
                qp.setKg(mView.info_12.getText().toString().trim());
                qp.setQPPZ(mView.info_12.getText().toString().trim());
                qp.setWorkMpa(mView.info_13.getText().toString().trim());
                qp.setWaterMpa(mView.info_14.getText().toString().trim());
                qp.setBTSL(mView.info_15.getText().toString().trim());
                qp.setTLKXL(mView.info_16.getText().toString().trim());
                qp.setJCZQ(mView.info_17.getText().toString().trim());
                qp.setSYQX(mView.info_18.getText().toString().trim());
                qp.setJCCS(mView.info_19.getText().toString().trim());

                int zq = Integer.valueOf(mView.info_17.getText().toString().trim());
                int next = Integer.valueOf(mView.info_20.getText().toString().substring(0, 4));
                int check = next - zq;

                qp.setCheckDate(String.valueOf(check) + mView.info_20.getText().toString().substring(4, mView.info_20.getText().toString().length()));

                qp.setOPID(getApp().getLogin().getUserID());
                //
                Log.e("QP===", util.json.JSONUtils.toJsonWithGson(qp));

                new CallRfidWsTask(getContext(), ActivityQpInfo.this, 9).execute(util.json.JSONUtils.toJsonWithGson(qp));
            }
        });
    }

    private void clean(){
        mView.info_3.setText("");
        mView.info_4.setText("");
        mView.info_5.setText("");
        mView.info_7.setText("");
        mView.info_20.setText("");
    }

    private void setCheckTimes(){
        if(mView.info_7.getText().toString().length()>0 && mView.info_17.getText().toString().length()>0){
            try {
                int start = Integer.valueOf(mView.info_7.getText().toString().substring(0, 4));
                int zq = Integer.valueOf(mView.info_17.getText().toString());
                int now = Integer.valueOf(new SimpleDateFormat("yyyy").format(new Date()));

                mView.info_19.setText(String.valueOf( ((now-start) / zq )) );
            } catch (Exception e) {
                // TODO: handle exception
                mView.info_19.setText("");
            }
        }else{
            mView.info_19.setText("");
        }
    }

    private void initView(){
        mView = new InfoView();
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
        mView.btn_submit = (Button) findViewById(R.id.qpinfo_submit);

        mView.info_5s.setEnabled(false);
        mView.info_17.setEnabled(false);
        mView.info_18.setEnabled(false);
        mView.info_19.setEnabled(false);

    }

    static class InfoView{
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
        Button btn_submit;
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

    @Override
    public void WsFinish(boolean isSuccess, int code, String retData) {
        // TODO Auto-generated method stub
        if(isSuccess){
            if(code==9){
                ToastUtil.showToast(getContext(), "提交基本信息成功");
                clean();

                mView.info_3.setFocusable(true);
                mView.info_3.setFocusableInTouchMode(true);
                mView.info_3.requestFocus();
                mView.info_3.requestFocusFromTouch();
            }else if(code==8){
                setData(retData);
            }
        }
    }
}
