package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hsic.qp.szjc.ActivityCheck1_Item;
import com.hsic.qp.szjc.ActivityConfig;
import com.hsic.qp.szjc.ActivityMenu;
import com.hsic.qp.szjc.ActivityNewGZWFQPCheck;
import com.hsic.qp.szjc.ActivityRead;
import com.hsic.qp.szjc.ActivityRfid;
import com.hsic.qp.szjc.ActivityStatistics;

public class ActivityUtils {
	public static void JumpToMenu(Context context){
		Intent intent = new Intent(context, ActivityMenu.class);
		context.startActivity(intent);
	}
	
	public static void JumpToConfig(Context context){
		Intent intent = new Intent(context, ActivityConfig.class);
		context.startActivity(intent);
	}
	
	public static void JumpToCheck1(Context context){
		Intent intent = new Intent(context, ActivityNewGZWFQPCheck.class);
		context.startActivity(intent);
	}
	
	public static void JumpToCheck1_Item(Context context, String part, String data, int requestCode, Activity activity){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
        bundle.putString("part", part);
        bundle.putString("data", data);
        intent.putExtras(bundle);
		intent.setClass(context, ActivityCheck1_Item.class);
		
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void JumpToRfid(Context context, String Json_NewGZWFQPCheck, Activity activity){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("json", Json_NewGZWFQPCheck);
		intent.putExtras(bundle);
		intent.setClass(context, ActivityRfid.class);
		
		activity.startActivityForResult(intent, 99);
	}
	
	public static void JumpToRead(Context context){
		Intent intent = new Intent(context, ActivityRead.class);
		context.startActivity(intent);
	}
	
	public static void JumpToStatistics(Context context){
		Intent intent = new Intent(context, ActivityStatistics.class);
		context.startActivity(intent);
	}
	
	
}
