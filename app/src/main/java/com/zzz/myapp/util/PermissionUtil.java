package com.zzz.myapp.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class PermissionUtil {
    public static void  requestPermissions(Activity activity, final Context context, final String... permissions) {
        RxPermissions rxPermission = new RxPermissions(activity);

//        rxPermission.request(permissions).subscribe(new Consumer<Boolean>() {
//
//            @Override
//            public void accept(Boolean aBoolean) throws Exception {
//                if (aBoolean) {
//                    Toast.makeText(context, "同意权限", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "拒绝权限", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        rxPermission.requestEach(permissions).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    // 用户已经同意该权限
                    Log.e("用户已经同意该权限", permission.name + " is granted.");
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                    Log.e("用户拒绝了该权限，没有选中『不再询问』", permission.name + " is denied. More info should be provided.");
                } else {
                    // 用户拒绝了该权限，并且选中『不再询问』
                    Log.e("用户拒绝了该权限，并且选中『不再询问』", permission.name + " is denied.");
                }
            }
        });

    }
}
