package com.zzz.c72.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rscja.deviceapi.RFIDWithUHF;

public class ScanTask extends AsyncTask<RFIDWithUHF, Void, String> {
    Handler mHandler;
    public ScanTask(Handler handler){
        mHandler = handler;
    }

    @Override
    protected String doInBackground(RFIDWithUHF... params) {
        params[0].setEPCTIDMode(true);

        params[0].startInventoryTag((byte)0, (byte)0);

        while (true) {
            if (isCancelled()) {
                break;
            }
            String[] res = null;
            String strTid;
            String strEpc;
            res = params[0].readTagFromBuffer();
            if (res != null) {
                strTid = res[0];
                if (!strTid.equals("0000000000000000")&&!strTid.equals("000000000000000000000000")) {

                } else {
                    strTid = "";
                }

                strEpc = params[0].convertUiiToEPC(res[1]);
                Log.e("Tid="+strTid, "Epc="+strEpc);
                //
                String ret = strEpc;
                if(ret!=null){
                    Message msg = new Message() ;
                    msg.what = 1 ;
                    msg.obj = ret;
                    mHandler.sendMessage(msg);

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        params[0].stopInventory();
        Message msg = new Message() ;
        msg.what = 2 ;
        mHandler.sendMessage(msg);
        return "";
    }
}
