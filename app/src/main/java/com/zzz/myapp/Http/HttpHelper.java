package com.zzz.myapp.Http;

import android.os.Handler;
import android.os.Message;

import com.zzz.myapp.util.MD5Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import bean.HttpData;

public class HttpHelper {
    public static void getHttp(String url, final String fun, final Handler handler){
        NetUtils.getInstance().getDataAsynFromNet(url,
                new NetUtils.myNetCall(){

            @Override
            public void success(String dataJson) {
                HttpData data = new HttpData(true, fun, dataJson, null);
                Message msg = new Message() ;
                msg.what = 1 ; msg.obj = data;
                handler.sendMessage(msg);
            }

            @Override
            public void failed(String err) {
                HttpData data = new HttpData(false, fun, null, err);
                Message msg = new Message() ;
                msg.what = 1 ; msg.obj = data;
                handler.sendMessage(msg);
            }
        });
    }

    public static String setBosHsicUrl(String url, String loginer, String mac){
        String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        url += "?appid=bos&timestamp="+time+"&sign="+MD5Utils.MD5("bosHSIC1227"+time)+"&opeid="+loginer+"&minfo="+mac;

        return url;
    }

    public static void postHttp(String url, final String fun, final Handler handler, String json){
        NetUtils.getInstance().postDataAsynToNet(url, json,
            new NetUtils.myNetCall(){

                @Override
                public void success(String dataJson) {
                    HttpData data = new HttpData(true, fun, dataJson, null);
                    Message msg = new Message() ;
                    msg.what = 1 ; msg.obj = data;
                    handler.sendMessage(msg);
                }

                @Override
                public void failed(String err) {
                    HttpData data = new HttpData(false, fun, null, err);
                    Message msg = new Message() ;
                    msg.what = 1 ; msg.obj = data;
                    handler.sendMessage(msg);
                }
            });
    }

    public static void postHttp(String url, final String fun, final Handler handler, Map<String,String> map){
        NetUtils.getInstance().postDataAsynToNet(url, map,
                new NetUtils.myNetCall(){

                    @Override
                    public void success(String dataJson) {
                        HttpData data = new HttpData(true, fun, dataJson, null);
                        Message msg = new Message() ;
                        msg.what = 1 ; msg.obj = data;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void failed(String err) {
                        HttpData data = new HttpData(false, fun, null, err);
                        Message msg = new Message() ;
                        msg.what = 1 ; msg.obj = data;
                        handler.sendMessage(msg);
                    }
                });
    }

    public static void updateFile(String url, final String fun, final Handler handler, String filename, String filepath, String  json){
        NetUtils.getInstance().postUpdateFile(url, filename, filepath, json,
                new NetUtils.myNetCall(){

                    @Override
                    public void success(String dataJson) {
                        HttpData data = new HttpData(true, fun, dataJson, null);
                        Message msg = new Message() ;
                        msg.what = 1 ; msg.obj = data;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void failed(String err) {
                        HttpData data = new HttpData(false, fun, null, err);
                        Message msg = new Message() ;
                        msg.what = 1 ; msg.obj = data;
                        handler.sendMessage(msg);
                    }
                });
    }
}
