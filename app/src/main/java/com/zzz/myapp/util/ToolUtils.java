package com.zzz.myapp.util;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ToolUtils {
    public static void CloseKey(Activity activity){
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen=imm.isActive();

            if(isOpen){
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static String getWifiMac(Context ctx) {
        try {
            WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String str = info.getMacAddress();
            if (str == null) str = "";
            return str;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "";
        }
    }

    public static List<String> getBluetoothMacList(Set<BluetoothDevice> devices){
        List<String> bList = new ArrayList<String>();

        if (devices!=null || devices.size()>0) {
            for(Iterator<BluetoothDevice> iterator = devices.iterator(); iterator.hasNext();){
                BluetoothDevice bluetoothDevice=(BluetoothDevice)iterator.next();
                bList.add(bluetoothDevice.getAddress());
            }
        }
        return bList;
    }
}
