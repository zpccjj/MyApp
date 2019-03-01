package com.zzz.myapp.util;

import android.content.Context;
import android.content.Intent;

import com.zzz.myapp.HttpTestActivity;
import com.zzz.myapp.RegisterActivity;

public class ActivityUtil {
    public static void JumpToRegister(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void JumpToHttpTest(Context context){
        Intent intent = new Intent(context, HttpTestActivity.class);
        context.startActivity(intent);
    }
}
