package com.hsic.version;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadVersionApk extends AsyncTask<String, Integer, Integer> {
    public ProgressDialog dia=null;
    VersionHelper versionHelper=null;
    Context context;
    String APK_NAME = "sz.apk";
    public DownLoadVersionApk(Context context){
        this.context=context;
        versionHelper=new VersionHelper(context);
    }
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        dia = new ProgressDialog(context);
        dia.setTitle("提示信息");
        dia.setMessage("正在下载.....");
        dia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dia.setCancelable(false);
        dia.show();
    }
    @Override
    protected Integer doInBackground(String... params) {
        // TODO Auto-generated method stub
        int result=-1;
        try{
            String downLoadPath=params[0];
            URL url=new URL(downLoadPath);
            HttpURLConnection httpConnection=(HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(5000);
            httpConnection.setRequestMethod("GET");
            httpConnection.setReadTimeout(5000);
            int responseCode=httpConnection.getResponseCode();
            if(responseCode==200){
                File saveFile=new File(Environment.getExternalStorageDirectory()+"/downLoad");
                if(!saveFile.exists()){
                    saveFile.mkdirs();
                }
                APK_NAME = params[1];
                String path="";
                path=saveFile.getAbsolutePath()+"/"+params[1];
                File outFile=new File(path);
                if(outFile.exists()){
                    outFile.delete();
                }

                FileOutputStream fileOutputStream=new FileOutputStream(outFile);
                int fileSize=httpConnection.getContentLength();
                InputStream in = httpConnection.getInputStream();
                int len=0;
                byte[] bytes=new byte[1024*1024];
                int total=0;
                while((len=in.read(bytes))!=-1){
                    fileOutputStream.write(bytes, 0, len);
                    total+=len;
                    int values=(int)((total/(float)fileSize)*100);
                    publishProgress(values);
                }
                String fileMD5String = MD5Util.getFileMD5String(outFile);
                if(params[2].equals(fileMD5String)){
                    result=0;
                }else{
                    result=1;
                }

            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    @Override
    protected void onPostExecute(Integer result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if(result==0){
            dia.dismiss();
            versionHelper.installApk(APK_NAME);
        }else if(result==1){
            dia.dismiss();
            Toast.makeText( context, "文件校验失败", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText( context, "下载失败", Toast.LENGTH_SHORT).show();
            dia.dismiss();
        }
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
        dia.setProgress(values[0]);
    }

}
