package com.hsic.qp.sz;

import hsic.tmj.gridview.MyGridAdapter;
import hsic.tmj.gridview.MyGridView;
import hsic.ui.HsicActivity;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityQP extends HsicActivity {
    private final static String MenuHOME = "返回";

    private MyGridView gridview;

    private int[] img_text = { R.string.txt_home_13, R.string.txt_home_6, R.string.txt_home_15, R.string.txt_home_19, R.string.txt_home_14, R.string.txt_home_16, R.string.txt_home_20};

    private int[] imgs = { R.drawable.icon_qpbd,  R.drawable.icon_qpbd, R.drawable.icon_qpbd, R.drawable.icon_qpbd, R.drawable.icon_qpbd, R.drawable.icon_qpbd, R.drawable.icon_qpbd};

    private Intent[] Intents = new Intent[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qp);

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(MenuHOME);

        InitIntents();
        gridview=(MyGridView) findViewById(R.id.gridview_qp);
        gridview.setAdapter(new MyGridAdapter(this, img_text, imgs));
        gridview.setOnItemClickListener(MyGridViewOnItemClickListener);
    }

    private void InitIntents(){
        for (int i = 0; i < Intents.length; i++) {

            switch (i) {
                case 0:
                    Intents[i] =  new Intent(ActivityQP.this, ActivityQpInfo.class);
                    break;
                case 1:
                    Intents[i] =  new Intent(ActivityQP.this, ActivityRfid.class);
                    break;
                case 2:
                    Intents[i] =  new Intent(ActivityQP.this, ActivityQpInfo2.class);
                    break;
                case 3:
                    Intents[i] =  new Intent(ActivityQP.this, ActivityQpInfo3.class);
                    break;
                case 4:
                    Intents[i] =  new Intent(ActivityQP.this, ActivitySelectBound.class);
                    break;
                case 5:
                    Intents[i] =  new Intent(ActivityQP.this, ActivityRfidRead.class);
                    break;
                case 6:
                    Intents[i] =  new Intent(ActivityQP.this, ActivitySearchQP.class);
                    break;
            }
        }

    }

    OnItemClickListener MyGridViewOnItemClickListener =  new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            startActivity(Intents[position]);

        }
    };


}
