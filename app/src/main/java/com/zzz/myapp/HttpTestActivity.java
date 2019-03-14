package com.zzz.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzz.myapp.Http.HttpHelper;
import com.zzz.myapp.ui.HttpActivity;
import com.zzz.myapp.util.FileUtils;
import com.zzz.myapp.util.MD5Utils;
import com.zzz.myapp.util.ToolUtils;
import com.zzz.myapp.util.json.JSONUtils;

import java.net.URISyntaxException;
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

        dialog = new ProgressDialog(getContext());
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
        showDialog("getWeather");
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

        showDialog("login");
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
        showDialog("GetTagInfo");
        HttpHelper.postHttp("http://47.90.58.48:8080/importtrace/post/GetTagInfo",
                "GetTagInfo",
                myHandler, map);
    }

    @OnClick(R.id.button_http_4 )
    public void showFileChooser(){
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("id","0");
//        map.put("tagid","N20160226005287");
//
//        HttpHelper.postHttp("http://47.90.58.48:8080/importtrace/post/GetOtherInfo",
//                "GetOtherInfo",
//                myHandler, map);

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                        FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException ex) {
                // Potentially direct the user to the Market with a Dialog
                Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
                        .show();
        }
    }
    String filepath = "";
    String filename = "";
    private static final int FILE_SELECT_CODE = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("showFileChooser", "File Uri: " + uri.toString());

                    // Get the path
                    String path = null;
                    try {
                        path = FileUtils.getPath(this, uri);

                        filename = path.substring(path.lastIndexOf("/") + 1);
                    } catch (URISyntaxException e) {
                        Log.e("TAG", e.toString());
                        //e.printStackTrace();
                        path = "";
                    }
                    filepath = path;
                    Log.e("filename="+filename, "filepath=" + filepath);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                    showDialog("Uploading...");
                    HttpHelper.updateFile("http://192.168.1.84:8080/eciqTransServer/post/jcjf/Upload", "UpdateFile", myHandler, filename,filepath);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private ProgressDialog dialog;

    private void showDialog(String Title){
        dialog.setMessage(Title);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void FinishHttp(HttpData data){
        dialog.setCancelable(true);
        if(data.isSucc()){
            dialog.dismiss();
            Toast.makeText(this, data.getFun() + ": " + data.getMsg(), Toast.LENGTH_SHORT).show();
        }else{
            dialog.setMessage(data.getErr());
            Toast.makeText(this, data.getErr(), Toast.LENGTH_SHORT).show();
        }
    }

}
