package com.zzz.c72.utils;

import android.content.Context;
import android.content.Intent;

import com.zzz.c72.MainActivity;
import com.zzz.c72.ListActivity;

public class ActivityUtil {
    public static void JumpToMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void JumpToListActivity(Context context){
        Intent intent = new Intent(context, ListActivity.class);
        context.startActivity(intent);
    }
}
