package com.zzz.c72;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.rscja.deviceapi.RFIDWithUHF;

import app.bean.HttpData;
import app.ui.HsicActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends HsicActivity {
    private RFIDWithUHF mReader;

    @BindView(R.id.topbar) QMUITopBar mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);

        View root = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        //绑定初始化ButterKnife
        ButterKnife.bind(this, root);
        //初始化状态栏
        initTopBar();
        setContentView(root);
    }

    private void initTopBar() {

        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTopBar.setTitle("菜单");
    }

    @OnClick(R.id.button_main_1 )
    public void JumpToMenu1(){

    }

    @OnClick(R.id.button_main_2 )
    public void JumpToMenu2(){

    }

    @Override
    public void FinishHttp(HttpData data){

    }

    @Override
    public void getRFID(String txt){
        //	Log.e("HsicActivity getRFID", txt);
    }

    @Override
    public void closeRFID(){
        //	Log.e("HsicActivity closeRFID", txt);
    }

    @Override
    public void ScanRfid(){

    }
}
