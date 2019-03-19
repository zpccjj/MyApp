package app.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.zzz.c72.QDApplication;

import app.bean.HttpData;

public class HttpActivity extends Activity {

    public Context getContext(){
        return this;
    }

    public QDApplication getApp(){
        return (QDApplication) super.getApplication();
    }

    public Handler httpHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==1){
                FinishHttp((HttpData) msg.obj);
            }else if(msg.what==2){
                ;
            }
        }
    };

    public void FinishHttp(HttpData data){

    }

}
