package com.hsic.qp.sz.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.hsic.qp.sz.listener.WsListener;
import com.jcraft.jsch.ChannelSftp;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bean.ResponseData;
import bean.UploadFile;
import util.MySFTP;
import util.PathFileUtils;
import util.ToastUtil;
import util.WsUtils;

public class UploadTask extends AsyncTask<List<String>, Void, ResponseData> {

    private Context mContext;
    private String mUser;
    private WsListener mListener;
    private boolean hasSale;
    private boolean hasPhoto;
    ProgressDialog dialog;

    public UploadTask(Context context, String user, WsListener listener, boolean hassale, boolean hasphoto){
        this.mContext = context;
        this.mUser = user;
        this.mListener = listener;
        this.hasSale = hassale;
        this.hasPhoto = hasphoto;
    }

    @Override
    protected ResponseData doInBackground(List<String>... params) {
        // TODO Auto-generated method stub
        List<String> salesList = params[0];
        List<String> failedList = new ArrayList<String>();
//		String path1 = PathFileUtils.getPath();
//		String path2 = PathFileUtils.getSalePath();

        String DeviceID = mContext.getSharedPreferences("DeviceSetting", 0).getString("DeviceID", "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = sdf.format(new Date());//当日文件目录
        ResponseData result = new ResponseData();
        String root = "/";
        MySFTP mySFTP  = new MySFTP(mContext);
        ChannelSftp channelSftp = mySFTP.connect();

        ResponseData res = new ResponseData();
        if (channelSftp == null) {
            res.setRespCode(1);
            res.setRespMsg("SFTP连接失败");
            return res;
        }else{
            mySFTP.mkdir(root, dir, channelSftp);
        }
        for (int i = 0; i < salesList.size(); i++) {
            List<UploadFile> filesList = new ArrayList<UploadFile>();
            if(hasSale){
                List<String> filesList1 = PathFileUtils.getFileList(salesList.get(i), PathFileUtils.getSalePath());
                Log.e("filesList1", util.json.JSONUtils.toJsonWithGson(filesList1));
                for (int j = 0; j < filesList1.size(); j++) {
                    UploadFile uf = new UploadFile();
                    uf.setFileName(filesList1.get(j));
                    uf.setFilePath(PathFileUtils.getSalePath());
                    filesList.add(uf);
                }
            }
            if(hasPhoto){
                List<String> filesList2 = PathFileUtils.getFileList(salesList.get(i), PathFileUtils.getPath());
                for (int j = 0; j < filesList2.size(); j++) {
                    UploadFile uf = new UploadFile();
                    uf.setFileName(filesList2.get(j));
                    uf.setFilePath(PathFileUtils.getPath());
                    filesList.add(uf);
                }
            }

            Log.e("===SaleID===", salesList.get(i));
            for (int j = 0; j < filesList.size(); j++) {
                Log.e("===File===", filesList.get(j).getFileName());

                //sftp上传文件
                Log.e("upload to SFTP", "stat");
                if (mySFTP.upload(root+dir, filesList.get(j).getFilePath() + filesList.get(j).getFileName(), channelSftp)) {
                    Log.e("upload to SFTP", "success");
                    //ws上传记录
                    List<Map<String, Object>> propertyList = new ArrayList<Map<String, Object>>();
                    HashMap<String, Object> map0 = new HashMap<String, Object>();
                    map0.put("propertyName", "DeviceID");
                    map0.put("propertyValue", DeviceID);
                    propertyList.add(map0);

                    HashMap<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("propertyName", "UserID");
                    map1.put("propertyValue", mUser);
                    propertyList.add(map1);

                    HashMap<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("propertyName", "SaleID");
                    map2.put("propertyValue", salesList.get(i));
                    propertyList.add(map2);

                    HashMap<String, Object> map3 = new HashMap<String, Object>();
                    map3.put("propertyName", "Filename");
                    map3.put("propertyValue", root+dir+ "/" +filesList.get(j));
                    propertyList.add(map3);

                    res = WsUtils.CallWs(mContext, "uploadImage", propertyList);

                    if(res.getRespCode()==0){
                        try {
                            File del = new File(filesList.get(j).getFilePath() + filesList.get(j).getFileName());
                            Log.e("=delete file", filesList.get(j).getFilePath() + filesList.get(j).getFileName() + ":" + String.valueOf(del.delete()));
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }

                    }else{
                        failedList.add(filesList.get(j).getFilePath() + filesList.get(j).getFileName());
                        Log.e("ws upload data failed", res.getRespMsg());
                    }
                }else{
                    Log.e("upload to SFTP", "fail:" + filesList.get(j).getFilePath() + filesList.get(j).getFileName() + " to " + root+dir);
                    failedList.add(filesList.get(j).getFilePath() + filesList.get(j).getFileName());
                }
            }
        }

        if(failedList.size()==0){
            result.setRespCode(0);
            result.setRespMsg("上传成功");
        }else{
            result.setRespCode(1);
            result.setRespMsg("上传失败数量："+String.valueOf(failedList.size()));
        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        dialog = new ProgressDialog(mContext);
        if(hasSale && hasPhoto) dialog.setMessage("正在上传电子销售单和照片...");
        else if(hasSale && !hasPhoto) dialog.setMessage("正在上传电子销售单...");
        else if(!hasSale && hasPhoto) dialog.setMessage("正在上传照片...");
        dialog.setCancelable(false);
        dialog.show();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ResponseData result) {
        // TODO Auto-generated method stub
        dialog.setCancelable(true);
        boolean isOk;
        if(result.getRespCode()==0){
            dialog.dismiss();
            ToastUtil.showToast(mContext, result.getRespMsg());
            isOk = true;
        }else{
            dialog.setMessage(result.getRespMsg());
            isOk = false;
        }

        if(mListener!=null) mListener.WsFinish(isOk, result.getRespCode(), result.getRespMsg());

        super.onPostExecute(result);
    }
}
