package com.zzz.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzz.myapp.adpater.PicassoListAdapter;
import com.zzz.myapp.ui.HttpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicassoListActivity extends HttpActivity {
    @BindView(R.id.topbar) QMUITopBar mTopBar;
    @BindView(R.id.picasso_listview) ListView mListView;

    public static String[] imageUrls = {
            "http://i.imgur.com/rFLNqWI.jpg",
            "http://i.imgur.com/C9pBVt7.jpg",
            "http://i.imgur.com/rT5vXE1.jpg",
            "http://i.imgur.com/aIy5R2k.jpg",
            "http://i.imgur.com/MoJs9pT.jpg",
            "http://i.imgur.com/S963yEM.jpg",
            "http://i.imgur.com/rLR2cyc.jpg",
            "http://i.imgur.com/SEPdUIx.jpg",
            "http://i.imgur.com/aC9OjaM.jpg",
            "http://i.imgur.com/76Jfv9b.jpg",
            "http://i.imgur.com/fUX7EIB.jpg",
            "http://i.imgur.com/syELajx.jpg",
            "http://i.imgur.com/COzBnru.jpg",
            "http://i.imgur.com/Z3QjilA.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);

        View root = LayoutInflater.from(this).inflate(R.layout.activity_picassolist, null);
        //绑定初始化ButterKnife
        ButterKnife.bind(this, root);
        //初始化状态栏
        initTopBar();
        setContentView(root);

        //Picasso.get().setIndicatorsEnabled(true);
//        Picasso.get().load("http://47.90.58.48:8080/ccicjpimptweb_file/lhs/20160323/5287%20(4).JPG")
//                .memoryPolicy(MemoryPolicy.NO_CACHE).into(mImageView);

        mListView.setAdapter(new PicassoListAdapter(this,imageUrls));
    }
    private void initTopBar() {

        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTopBar.setTitle("PicassoList");
    }
}
