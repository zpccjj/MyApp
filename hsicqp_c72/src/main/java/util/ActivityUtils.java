package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.hsic.qp.sz.ActivityConfig;
import com.hsic.qp.sz.ActivityPrint;
import com.hsic.qp.sz.ActivityQP;
import com.hsic.qp.sz.ActivityQPReceive;
import com.hsic.qp.sz.ActivityQpInfo;
import com.hsic.qp.sz.ActivityReprint;
import com.hsic.qp.sz.ActivityRfid;
import com.hsic.qp.sz.ActivitySendReceive;
import com.hsic.qp.sz.ActivitySign;
import com.hsic.qp.sz.ActivityTask;
import com.hsic.qp.sz.ActivityTaskList;
import com.hsic.qp.sz.MainActivity;

public class ActivityUtils {
	public static void JumpToMain(Context context){
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}
	
	public static void JumpToConfig(Context context){
		Intent intent = new Intent(context, ActivityConfig.class);
		context.startActivity(intent);
	}
	
	public static void JumpToTaskList(Context context){
		Intent intent = new Intent(context, ActivityTaskList.class);
		context.startActivity(intent);
	}
	
	public static void JumpToTask(Context context, String TaskInfo, String Truck, String Persons){
		Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("TaskInfo", TaskInfo);
        bundle.putString("Truck", Truck);
        bundle.putString("Persons", Persons);
        intent.putExtras(bundle);
        intent.setClass(context, ActivityTask.class);
		context.startActivity(intent);
	}
	
	public static void JumpToSendReceive(Context context, Activity activity, int key, String rlist, String slist, String Detail){//key 1=Receive,2=Send
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("key", key);
        bundle.putString("rList", rlist);
        bundle.putString("sList", slist);
        bundle.putString("Detail", Detail);
        intent.putExtras(bundle);
		intent.setClass(context, ActivitySendReceive.class);
		
		activity.startActivityForResult(intent, key);
	}
	
	public static void JumpToReceive(Context context, Activity activity, String mlist, String rlist, String slist){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
        bundle.putString("mList", mlist);
        bundle.putString("rList", rlist);
        bundle.putString("sList", slist);
        intent.putExtras(bundle);
		intent.setClass(context, ActivityQPReceive.class);
		
		activity.startActivityForResult(intent, 1);
	}
	
	public static void JumpToPrint(Context context, String PrintListInfo, String SaleID){
		Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("PrintListInfo", PrintListInfo);
        bundle.putString("SaleID", SaleID);
        intent.putExtras(bundle);
        intent.setClass(context, ActivityPrint.class);
		context.startActivity(intent);
	}
	
	public static void JumpToSign(Context context, String SaleID){
		Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("SaleID", SaleID);
        intent.putExtras(bundle);
        intent.setClass(context, ActivitySign.class);
		context.startActivity(intent);
	}
	
	public static void JumpToReprint(Context context){
		Intent intent = new Intent(context, ActivityReprint.class);
		context.startActivity(intent);
	}
	
	public static void JumpToRfid(Context context){
		Intent intent = new Intent(context, ActivityRfid.class);
		context.startActivity(intent);
	}
	
	public static void JumpToQP(Context context){
		Intent intent = new Intent(context, ActivityQP.class);
		context.startActivity(intent);
	}
	
	public static void JumpToQpInfo(Context context){
		Intent intent = new Intent(context, ActivityQpInfo.class);
		context.startActivity(intent);
	}
}
