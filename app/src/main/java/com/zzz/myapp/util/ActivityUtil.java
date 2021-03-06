package com.zzz.myapp.util;

import android.content.Context;
import android.content.Intent;

import com.zzz.myapp.HttpTestActivity;
import com.zzz.myapp.MainActivity;
import com.zzz.myapp.PicassoListActivity;
import com.zzz.myapp.RegisterActivity;

public class ActivityUtil {
    public static void JumpToMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void JumpToRegisterActivity(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void JumpToHttpTestActivity(Context context){
        Intent intent = new Intent(context, HttpTestActivity.class);
        context.startActivity(intent);
    }

    public static void JumpToPicassoListActivity(Context context){
        Intent intent = new Intent(context, PicassoListActivity.class);
        context.startActivity(intent);
    }
}
