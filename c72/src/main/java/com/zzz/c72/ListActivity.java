package com.zzz.c72;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import com.zzz.c72.task.ScanTask;
import com.zzz.c72.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.bean.RfidData;
import app.ui.HsicActivity;
import app.ui.SwipeListLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends HsicActivity {
    private Set<SwipeListLayout> sets = new HashSet();
    private List<RfidData> mList = new ArrayList<RfidData>();
    private SwipeListAdapter mAdapter;

    private RFIDWithUHF mReader;
    ScanTask rfidTask;
    boolean hasPow = false;
    boolean isStart = false;

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

        setContentView(root);
        initListView();

        //初始化状态栏
        initTopBar();
        new InitTask(getContext()).execute();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if(mReader!=null && rfidTask!=null && rfidTask.getStatus()==AsyncTask.Status.RUNNING){
            rfidTask.cancel(true);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mReader != null) {
            mReader.free();
            hasPow = false;
            ToastUtil.showToast(getContext(), "设备下电");
        }

        super.onDestroy();
    }

    private void initListView(){
        //初始化数据

        mAdapter = new SwipeListAdapter(getContext(), mList, sets, true);

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

        mTopBar.addRightTextButton("清空", R.id.topbar_right_about_button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
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



    /**
     * 设备上电异步类
     */
    private class InitTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog mypDialog;
        Context mContext;

        public InitTask(Context context) {
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
            if (!init) return 2;

            boolean pow = mReader.setPower(30);//5-30
            if (!pow) return 3;

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (result != 0) {
                String txt = "设备打开失败：";
                switch (result) {
                    case 1:
                        txt += "初始化失败";
                        break;
                    case 2:
                        txt += "上电失败";
                        break;
                    case 3:
                        txt += "设置频率失败";
                        break;
                }
                ToastUtil.showToast(mContext, txt);
            } else {
                ToastUtil.showToast(mContext, "RFID设备开启");
                hasPow = true;
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

    @Override
    public void ScanRfid(){
        if(hasPow) {
            if (isStart) {
                if (mReader != null && rfidTask != null && rfidTask.getStatus() == AsyncTask.Status.RUNNING) {
                    rfidTask.cancel(true);
                }
                rfidTask = null;
                isStart = false;
                ToastUtil.showToast(getContext(), "暂停扫描");
            } else {
                if (mReader != null && rfidTask == null) {
                    isStart = true;
                    rfidTask = new ScanTask(rfidHandler);
                    rfidTask.execute(mReader);
                    ToastUtil.showToast(getContext(), "开始扫描");
                }
            }
        }
    }

    @Override
    public void closeRFID(){
        super.closeRFID();
        rfidTask = null;
        isStart = false;
    }

    @Override
    public void getRFID(String txt) {
        // TODO Auto-generated method stub
        super.getRFID(txt);
        boolean hasRfid = false;
        for (int i=0; i<mList.size(); i++){
            if(mList.get(i).getEPC().equals(txt)){
                int num = mList.get(i).getNUM() + 1;
                mList.get(i).setNUM(num);
                hasRfid = true;
                break;
            }
        }
        if(!hasRfid){
            RfidData rfid = new RfidData();
            rfid.setEPC(txt);
            mList.add(rfid);
        }

        mAdapter.notifyDataSetChanged();
    }
}
