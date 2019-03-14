package com.hsic.qp;

import hsic.ui.ConfirmDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

public class QueryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void onBackPressed() {
        ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定退出？");
        dialog.setConfirmButton(new android.content.DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                finish();
            }

        });
        dialog.show();
    }
}
