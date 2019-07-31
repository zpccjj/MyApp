package hsic.ui;

import android.view.KeyEvent;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import bll.MyApplication;

public class HsicActivity extends SherlockActivity{
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;  
	}
	
	public MyApplication getApp(){
		return (MyApplication) super.getApplication();
	}
	
	public void onBackPressed() {
		
	}
	
//	public Handler myHandler = new Handler(){
//		@Override
//        public void handleMessage(Message msg){
//            super.handleMessage(msg);
//            if(msg.what==1){
//            	getRFID(msg.obj.toString());
//            }else if(msg.what==2){
//            	closeRFID();
//            }
//        }
//	};
//	
//	public void getRFID(String txt){
//    }
//	public void closeRFID(){
//	}
	public void ScanRfid(){
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==280) {

            if (event.getRepeatCount() == 0) {
            	ScanRfid();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
