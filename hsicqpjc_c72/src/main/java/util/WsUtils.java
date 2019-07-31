package util;

import android.content.Context;

import java.util.List;
import java.util.Map;

import bean.ResponseData;
import bean.WsInfo;
import bll.GetData;

public class WsUtils {

    public static ResponseData CallWs(Context context, String Url, List<Map<String, Object>> propertyList){
        ResponseData msg = new ResponseData();

        msg = NetWorkUtils.isNetWork(context);
        if(msg.getRespCode()!=0) return msg;

        GetData orderdeclData = new GetData(context);

        try {//getData WebService接口的方法名
            WsInfo res = orderdeclData.recevieData(Url, propertyList, null, null, true);
            if(res.isOk())
                msg = (ResponseData) util.json.JSONUtils.toObjectWithGson(res.getObj().toString(), ResponseData.class);
            else{
                msg.setRespCode(1);
                msg.setRespMsg("调用接口失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setRespCode(1);
            msg.setRespMsg("接口异常");
        }
        return msg;
    }
}
