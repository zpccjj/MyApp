package util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import bean.ResponseData;
import bean.WsInfo;
import bll.GetData;

public class WsUtils {
	
	public static ResponseData CallWs(Context context, String Url, List<Map<String, Object>> propertyList){
		ResponseData msg = new ResponseData();
		
		msg = NetWorkUtils.isNetWork(context);
		if(msg.getRespCode()!=0) return msg;
		
		GetData orderdeclData = new GetData(context);
        
        try {//getData WebService�ӿڵķ�����
        	WsInfo res = orderdeclData.recevieData(Url, propertyList, null, null, true);   
            Log.e("object", res.getObj().toString());           
            if(res.isOk())
            	msg = (ResponseData) util.json.JSONUtils.toObjectWithGson(res.getObj().toString(), ResponseData.class);
            else{
            	msg.setRespCode(1);
                msg.setRespMsg("���ýӿ�ʧ��");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setRespCode(1);
            msg.setRespMsg("�ӿ��쳣");
        }
        return msg;
	}
}
