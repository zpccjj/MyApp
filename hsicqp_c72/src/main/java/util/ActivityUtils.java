package util;

import com.hsic.qp.ActivityConfig;
import com.hsic.qp.ActivityPrint;
import com.hsic.qp.ActivityQPReceive;
import com.hsic.qp.ActivityReprint;
import com.hsic.qp.ActivityRfid;
import com.hsic.qp.ActivitySendReceive;
import com.hsic.qp.ActivityTask;
import com.hsic.qp.ActivityTaskList;
import com.hsic.qp.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
	
	public static void JumpToReceive(Context context, Activity activity, String rlist){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
        bundle.putString("rList", rlist);
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
	
	public static void JumpToReprint(Context context){
		Intent intent = new Intent(context, ActivityReprint.class);
		context.startActivity(intent);
	}
	
	public static void JumpToRfid(Context context){
		Intent intent = new Intent(context, ActivityRfid.class);
		context.startActivity(intent);
	}
}
