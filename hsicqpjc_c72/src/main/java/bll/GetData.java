package bll;

import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import bean.WsInfo;

import com.hsic.qp.szjc.R;


import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

/*
 * WebService 接口调用的方法
 */
public class GetData {
    private static String nameSpaceDefaultString = "http://tempuri.org/";


    private String nameSpace; // 命名空间
    private String endPoint; // EndPoint
    private int overTime;//接口超时时间

    public GetData(Context context) {
        super();
        this.nameSpace = nameSpaceDefaultString;

        this.endPoint = PreferenceManager.getDefaultSharedPreferences(context).getString("webServer", context.getResources().getString(R.string.config_ws));

        try{
            String oTime = PreferenceManager.getDefaultSharedPreferences(context).getString("overTime", context.getResources().getString(R.string.config_time));
            this.overTime = Integer.parseInt(oTime);
        }catch(Exception e){
            this.overTime = 30;
        }

//		Log.e("nameSpace", nameSpace);
//		Log.e("endPoint", endPoint);
//		Log.e("overTime", String.valueOf(overTime));
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    @SuppressWarnings("rawtypes")
    public WsInfo recevieData(String methodName,
                              List<Map<String, Object>> propertyList, List<String> mappingName,
                              List<Map<String, Class>> mappingList, Boolean isSimpleRet) {
        // 返回的查询结果
        // SOAP Action
        WsInfo resInfo = new WsInfo();
        String soapAction = nameSpace + methodName;
//		Log.e("soapAction", soapAction);
        // 指定WebService的命名空间和调用的方法名
        SoapObject request = new SoapObject(this.nameSpace, methodName);
        // 设置需要返回请求对象的参数
        if(propertyList!=null){
            for (Map<String, Object> map : propertyList) {
                request.addProperty(map.get("propertyName").toString(),
                        map.get("propertyValue"));
            }
        }
        // 设置soap的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        // 添加类映射
        if (mappingName != null) {
            if (mappingName.size() == mappingList.size()) {
                for (int i = 0; i < mappingName.size(); i++) {
                    envelope.addMapping(this.nameSpace, mappingName.get(i),
                            mappingList.get(i).get("mappingClass"));
                }
            }
        }
        // 设置是否调用的是dotNet开发的
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(request);

        HttpTransportSE hts = new HttpTransportSE(endPoint,  overTime * 1000);//90000
        // web service请求

        try {
            // 调用WebService
            hts.call(soapAction, envelope);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error ", e.getMessage());
            resInfo.setOk(false);
            resInfo.setObj(e.getMessage());
            return resInfo;
        }
        // 得到返回结果
        try {
            if (!isSimpleRet) {
                SoapObject o = (SoapObject) envelope.bodyIn;
                if (o != null) {
                    resInfo.setOk(true);
                    resInfo.setObj(o);
                    return resInfo;
                }else{
                    resInfo.setOk(false);
                    resInfo.setObj("WebService return null!");
                    return resInfo;
                }
            } else {
                resInfo.setOk(true);
                resInfo.setObj(envelope.getResponse());
                return resInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("WebService", "WebService return error!");
            resInfo.setOk(false);
            resInfo.setObj("WebService return error!");
            return resInfo;
        }
    }

    @SuppressWarnings("rawtypes")
    public KvmSerializable parseToObject(SoapObject soapObject,
                                         Class objectClass) throws InstantiationException,
            IllegalAccessException {

        KvmSerializable result = (KvmSerializable) objectClass.newInstance();

        int numOfAttr = result.getPropertyCount();

        for (int i = 0; i < numOfAttr; i++) {
            PropertyInfo info = new PropertyInfo();
            result.getPropertyInfo(i, null, info);

            // 处理property不存在的情况
            try {
                Object object = soapObject.getProperty(info.name);
                result.setProperty(i, object);
            } catch (Exception e) {
                continue;
            }
        }
        return result;
    }

}
