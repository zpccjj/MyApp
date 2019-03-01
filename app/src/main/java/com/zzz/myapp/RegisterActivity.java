package com.zzz.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends Activity {
    @BindView(R.id.topbar)//绑定View
    QMUITopBar mTopBar;
    @BindView(R.id.button_getverifycode) QMUIRoundButton mButton_getverifycode;
    @BindView(R.id.button_register) QMUIRoundButton mButton_register;

    @OnClick(R.id.button_getverifycode )   //给 button 设置一个点击事件
    public void showToast(){
        Toast.makeText(this, "获取验证码", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_register )
    public void showToast2(){
        Toast.makeText(this, "用户注册", Toast.LENGTH_SHORT).show();
    }

    final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);

        View root = LayoutInflater.from(this).inflate(R.layout.activity_register, null);

        // 绑定初始化ButterKnife
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

        mTopBar.setTitle("注册账号");
    }
}
