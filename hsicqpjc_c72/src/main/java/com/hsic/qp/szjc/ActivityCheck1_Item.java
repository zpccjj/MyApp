package com.hsic.qp.szjc;

import java.util.List;

import util.ToastUtil;

import com.google.gson.reflect.TypeToken;
import com.hsic.qp.szjc.adapter.ItemCheckAdapter;

import bean.ItemCheck;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import hsic.ui.HsicActivity;

public class ActivityCheck1_Item extends HsicActivity{
    String Title = "外观检查";
    String Part;
    List<ItemCheck> mList;

    static class mView{
        ListView lv;
        Button btn_no;
        Button btn_yes;
    }
    mView mV;

    private Context getContext(){
        return ActivityCheck1_Item.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check1_item);

        Part = getIntent().getExtras().getString("part");
        mList = util.json.JSONUtils.toListWithGson(getIntent().getExtras().getString("data"), new TypeToken<List<ItemCheck>>(){}.getType());

        if(Part==null || mList==null && Part.length()==0 || mList.size()==0){
            ToastUtil.showToast(getContext(), "检验项目错误");
            finish();
        }
        if(Part.equals("1-0")) Title = "外观检查";
        else if(Part.equals("1-1")) Title = "音响检查";
        else if(Part.equals("1-2")) Title = "瓶口螺纹检查";
        else if(Part.equals("1-3")) Title = "内部检查";
        else if(Part.equals("1-4")) Title = "重量与容积测定";
        else if(Part.equals("1-5")) Title = "水压试验";
        else if(Part.equals("1-6")) Title = "气密性试验";

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(Title);

        initViews();
        setListener();
        initData();
    }

    private void initViews(){
        mV = new mView();
        mV.lv = (ListView) findViewById(R.id.check1_item_lv);
        mV.btn_yes = (Button) findViewById(R.id.check1_item_yes);
        mV.btn_no = (Button) findViewById(R.id.check1_item_no);
    }

    private void setListener(){
        mV.btn_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                callReturn(true);
            }
        });

        mV.btn_no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                callReturn(false);
            }
        });
    }

    private void callReturn(boolean isSave){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("part", Part);
        resultIntent.putExtra("data", util.json.JSONUtils.toJsonWithGson(mList));

        this.setResult(isSave? RESULT_OK : RESULT_CANCELED, resultIntent);
        finish();
    }

    private void initData(){
        mV.lv.setAdapter(new ItemCheckAdapter(getContext(), mList));
    }
}
