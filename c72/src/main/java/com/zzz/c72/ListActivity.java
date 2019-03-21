package com.zzz.c72;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.rscja.deviceapi.RFIDWithUHF;
import com.zzz.c72.adaptor.SwipeListAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.ui.HsicActivity;
import app.ui.SwipeListLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends HsicActivity {
    private RFIDWithUHF mReader;
    private Set<SwipeListLayout> sets = new HashSet();
    private List<String> mList = new ArrayList<String>();
    private SwipeListAdapter mAdapter;

    @BindView(R.id.topbar) QMUITopBar mTopBar;
    @BindView(R.id.listview) ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);

        View root = LayoutInflater.from(this).inflate(R.layout.activity_list, null);
        //绑定初始化ButterKnife
        ButterKnife.bind(this, root);
        //初始化状态栏
        initTopBar();
        setContentView(root);
        initListView();
    }

    private void initListView(){
        //初始化数据
        mList.add("No.000001"); mList.add("No.000002"); mList.add("No.000003");
        mList.add("No.000004"); mList.add("No.000005"); mList.add("No.000006");
        mList.add("No.000007"); mList.add("No.000008"); mList.add("No.000009");
        mList.add("No.000010"); mList.add("No.000011"); mList.add("No.000012");
        mList.add("No.000013"); mList.add("No.000014"); mList.add("No.000015");
        mList.add("No.000016"); mList.add("No.000017"); mList.add("No.000018");
        mList.add("No.000019"); mList.add("No.000020"); mList.add("No.000021");

        mAdapter = new SwipeListAdapter(getContext(), mList, sets);

        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                            //    sets.remove(s);
                            }
                            sets.clear();
                        }
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initTopBar() {

        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessagePositiveDialog();
            }
        });

        mTopBar.addRightTextButton("设置", R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });

        mTopBar.setTitle("扫描");
    }

    private void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setTitle("退出扫描")
                .setMessage("确定退出扫描？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
    }



}
