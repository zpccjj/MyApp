package com.zzz.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzz.myapp.Http.HttpHelper;
import com.zzz.myapp.ui.HttpActivity;
import com.zzz.myapp.util.MD5Utils;
import com.zzz.myapp.util.ToolUtils;
import com.zzz.myapp.util.json.JSONUtils;

import java.util.HashMap;
import java.util.Map;

import bean.HttpData;
import bean.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HttpTestActivity extends HttpActivity {
    @BindView(R.id.topbar) QMUITopBar mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);

        View root = LayoutInflater.from(this).inflate(R.layout.activity_httptest, null);
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

        mTopBar.setTitle("OkHttp");
    }



    @OnClick(R.id.button_http_1 )
    public void getHttp1(){
        HttpHelper.getHttp("https://restapi.amap.com/v3/weather/weatherInfo?key=e3d1cb07307faee07eb6d74dd8565c07&city=上海", "getWeather",
                myHandler);
    }

    @OnClick(R.id.button_http_2 )
    public void postHttp2(){
        User user = new User();
        user.setUseroneid("BA111111");
        user.setUseronepassword(MD5Utils.MD5("111111"));
        user.setUsertwoid("BA111112");
        user.setUsertwopassword(MD5Utils.MD5("111111"));
        user.setSystemtype("0");
        user.setDeptcode("9999");
        String json = JSONUtils.toJsonWithGson(user);
        HttpHelper.postHttp(
                HttpHelper.setBosHsicUrl("http://192.168.1.75:8380/restful_bankofshanghai/services/user/jjlogin",
                        "BA111111,BA111112", ToolUtils.getWifiMac(getContext())),
                "login",
                myHandler, json);
    }

    @OnClick(R.id.button_http_3 )
    public void postHttp3(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("id","0");
        map.put("tagid","N20160226005287");

        HttpHelper.postHttp("http://47.90.58.48:8080/importtrace/post/GetTagInfo",
                "GetTagInfo",
                myHandler, map);
    }

    @OnClick(R.id.button_http_4 )
    public void postHttp4(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("id","0");
        map.put("tagid","N20160226005287");

        HttpHelper.postHttp("http://47.90.58.48:8080/importtrace/post/GetOtherInfo",
                "GetOtherInfo",
                myHandler, map);
    }

    @Override
    public void FinishHttp(HttpData data){
        if(data.isSucc()){
            Toast.makeText(this, data.getFun() + ": " + data.getMsg(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, data.getErr(), Toast.LENGTH_SHORT).show();
        }
    }

}
