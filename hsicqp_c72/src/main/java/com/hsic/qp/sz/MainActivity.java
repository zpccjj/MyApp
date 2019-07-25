package com.hsic.qp.sz;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainActivity extends TabActivity{
    private static final String TAG = "TestTabActivity";

    private TabHost tab_host;
    private RadioGroup rg;
    private RadioButton rb_1, rb_2, rb_3, rb_4;
    private String FIRST_TAG = "first";
    private String SECOND_TAG = "second";
    private String THIRD_TAG = "third";
    private String FORTH_TAG = "forth";

    private TabHost.TabSpec getNewTab(String spec, int label, Intent intent) {
        return tab_host
                .newTabSpec(spec)
                .setIndicator(getString(label))
                .setContent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab_host = getTabHost();
        tab_host.addTab(getNewTab(FIRST_TAG, R.string.title_activity_home, new Intent(this, HomeActivity.class)));
        tab_host.addTab(getNewTab(SECOND_TAG, R.string.title_activity_query,new Intent(this, QueryActivity.class)));
        tab_host.addTab(getNewTab(THIRD_TAG, R.string.title_activity_count,new Intent(this, CountActivity.class)));
        tab_host.addTab(getNewTab(FORTH_TAG, R.string.title_activity_my,   new Intent(this, MyActivity.class)));

        rg = (RadioGroup) findViewById(R.id.llyt_tab_group);
        rg.setOnCheckedChangeListener(RadioGroupCheckedChangeListener);

        rb_1 = ((RadioButton) findViewById(R.id.rbtn_tab_1));
        rb_2 = ((RadioButton) findViewById(R.id.rbtn_tab_2));
        rb_3 = ((RadioButton) findViewById(R.id.rbtn_tab_3));
        rb_4 = ((RadioButton) findViewById(R.id.rbtn_tab_4));


        rb_1.setChecked(true);

    }

    OnCheckedChangeListener RadioGroupCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            if(checkedId==rb_1.getId()){
                tab_host.setCurrentTabByTag(FIRST_TAG);
            }else if(checkedId==rb_2.getId()){
                tab_host.setCurrentTabByTag(SECOND_TAG);
            }else if(checkedId==rb_3.getId()){
                tab_host.setCurrentTabByTag(THIRD_TAG);
            }else if(checkedId==rb_4.getId()){
                tab_host.setCurrentTabByTag(FORTH_TAG);
            }

        }
    };
    
    
    
    
/*    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == true) {
            setButtonCheck(buttonView);
            if (buttonView.equals(quanpeis_button)) {
                tab_host.setCurrentTabByTag(FIRST_TAG);
            } else if (buttonView.equals(scan_button)) {
                tab_host.setCurrentTabByTag(SECOND_TAG);
            } else if (buttonView.equals(suy_button)) {
                tab_host.setCurrentTabByTag(THIRD_TAG);
            }else if (buttonView.equals(count_button)) {
                tab_host.setCurrentTabByTag(FORTH_TAG);
            }
//            else if (buttonView.equals(news_button)) {
//                tab_host.setCurrentTabByTag(FIFTH_TAG);
//            }
        }
    }*/

}
