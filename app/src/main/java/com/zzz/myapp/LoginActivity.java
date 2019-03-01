package com.zzz.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzz.myapp.util.ActivityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {
    @BindView(R.id.topbar) QMUITopBar mTopBar;

    @BindViews({ R.id.edittext_user, R.id.edittext_password})
    public List<EditText> editTexts;

    @OnClick(R.id.button_login )   //给 button 设置一个点击事件
    public void login(){
        if(editTexts.get(0).getText().toString().trim().length()==0 || editTexts.get(1).getText().toString().trim().length()==0)
            Toast.makeText(this, "请输入账号密码", Toast.LENGTH_SHORT).show();
        else{
           //login
            ActivityUtil.JumpToHttpTest(this);
        }
    }

    @OnClick(R.id.button_register )   //给 button 设置一个点击事件
    public void register(){
        ActivityUtil.JumpToRegister(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);

        View root = LayoutInflater.from(this).inflate(R.layout.activity_login, null);
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

        mTopBar.setTitle("登录");
    }
}
