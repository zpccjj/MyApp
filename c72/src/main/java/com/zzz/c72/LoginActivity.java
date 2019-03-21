package com.zzz.c72;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zzz.c72.utils.ActivityUtil;

import java.util.List;

import app.ui.HttpActivity;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class LoginActivity extends HttpActivity {
    @BindView(R.id.topbar) QMUITopBar mTopBar;

    @BindViews({ R.id.edittext_user, R.id.edittext_password})
    public List<EditText> editTexts;

    @OnClick(R.id.button_login )   //给 button 设置一个点击事件
    public void login(){
        if(editTexts.get(0).getText().toString().trim().length()==0 || editTexts.get(1).getText().toString().trim().length()==0)
            Toast.makeText(this, "请输入账号密码", Toast.LENGTH_SHORT).show();
        else{
           //login
            ActivityUtil.JumpToMainActivity(getContext());
        }
    }

    @OnClick(R.id.button_quit )   //给 button 设置一个点击事件
    public void quit(){
        finish();
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

        editTexts.get(0).setText("123");
        editTexts.get(1).setText("111111");

        requestPermissions();

        Log.e("getExternalFilesDir",getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);

//        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Permission>() {
//            @Override
//            public void accept(Permission permission) throws Exception {
//                if (permission.granted) {
//                    // 用户已经同意该权限
//                    Log.e("用户已经同意该权限", permission.name + " is granted.");
//                    Toast.makeText(getContext(), "用户已经同意该权限 "+permission.name, Toast.LENGTH_SHORT).show();
//                } else if (permission.shouldShowRequestPermissionRationale) {
//                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                    Log.e("用户拒绝了该权限，没有选中『不再询问』", permission.name + " is denied. More info should be provided.");
//                    Toast.makeText(getContext(), "用户拒绝了该权限，没有选中『不再询问』"+permission.name, Toast.LENGTH_SHORT).show();
//                } else {
//                    // 用户拒绝了该权限，并且选中『不再询问』
//                    Log.e("用户拒绝了该权限，并且选中『不再询问』", permission.name + " is denied.");
//                    Toast.makeText(getContext(), "用户拒绝了该权限，并且选中『不再询问』"+permission.name, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        rxPermission.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {

            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //Toast.makeText(getContext(), "同意权限", Toast.LENGTH_SHORT).show();
                    Log.e("同意权限", "READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE");
                } else {
                    Toast.makeText(getContext(), "拒绝 READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE 权限", Toast.LENGTH_SHORT).show();
                    Log.e("拒绝权限", "READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE");
                }
            }
        });
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
