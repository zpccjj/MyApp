package com.zzz.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.zzz.myapp.ui.HttpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicassoActivity extends HttpActivity {
    @BindView(R.id.topbar) QMUITopBar mTopBar;
    @BindView(R.id.picasso_imageview) ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);

        View root = LayoutInflater.from(this).inflate(R.layout.activity_picasso, null);
        //绑定初始化ButterKnife
        ButterKnife.bind(this, root);
        //初始化状态栏
        initTopBar();
        setContentView(root);

        Picasso.get()
                .load("http://ww3.sinaimg.cn/large/610dc034jw1fasakfvqe1j20u00mhgn2.jpg")
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(mImageView);
    }
    private void initTopBar() {

        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTopBar.setTitle("Picasso");
    }
}
