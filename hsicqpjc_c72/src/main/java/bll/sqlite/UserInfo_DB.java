package bll.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import bean.UserInfo;
import bll.DatabaseHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserInfo_DB {
    private static final String TAG = "UserInfo_DB";

    private static final String TABLE_NAME = "UserInfo";

    private SQLiteDatabase mDatabase = null;

    public UserInfo_DB(Context context){
        this.mDatabase = DatabaseHelper.getInstance(context).getReadableDatabase();
    }

    //RoleID角色用户是否存在
    public boolean isExist(String RoleID) {
        boolean result = false;

        try {
            String whereClause = "RoleID=?";
            String[] whereArgs = new String[] { RoleID };
            String[] columns = new String[] { "UserLoginID","UserName"};
            Cursor cursor = mDatabase.query(TABLE_NAME, columns, whereClause,
                    whereArgs, null, null, null);
            if (cursor.getCount() >0) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //从DB获取角色为RoleID的用户ID和Name
    public List<UserInfo> GetUserList(String RoleID) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (isExist(RoleID)) {
            String whereClause = "RoleID=?";
            String[] whereArgs = new String[] { RoleID };
            String[] columns = new String[] { "UserLoginID","UserName"};;

            Cursor cursor = mDatabase.query(TABLE_NAME, columns, whereClause,
                    whereArgs, null, null, null);
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                for (int i = 0, j = cursor.getColumnCount(); i < j; i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                list.add(map);
            }
        }
        List<UserInfo> ret = new ArrayList<UserInfo>();
        ret = util.json.JSONUtils.toListWithGson(util.json.JSONUtils.toJsonWithGson(list), new TypeToken<List<UserInfo>>(){}.getType());

        Log.e("=====" + RoleID, String.valueOf(ret.size()));

        return ret;
    }

    public UserInfo Login(String UserLoginID, String UserPWD){//MD5加密
        UserInfo ret = null;
        Log.e("Login", UserLoginID + ", " + UserPWD);
        try {
            String whereClause = "UserLoginID=? and UserPWD=? and RoleID=?";
            String[] whereArgs = new String[] { UserLoginID, UserPWD, "J9001"};
            String[] columns = null;
            Cursor cursor = mDatabase.query(TABLE_NAME, columns, whereClause,
                    whereArgs, null, null, null);
            if (cursor.getCount() == 1) {
                while (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<String, String>();
                    for (int i = 0, j = cursor.getColumnCount(); i < j; i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    Log.e("map",util.json.JSONUtils.toJsonWithGson(map));
                    ret = util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(map), UserInfo.class);
                }
            }else if (cursor.getCount() > 1) {
                Log.e("J9001用户："+UserLoginID,"重复数量 = " + cursor.getCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }


    //用户表是否存在数据
    public boolean isExist() {
        boolean result = false;
        try {
            String[] columns = new String[] { "UserLoginID","UserName"};
            Cursor cursor = mDatabase.query(TABLE_NAME, columns, null,
                    null, null, null, null);
            if (cursor.getCount() >0) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean deleteAll(){
        boolean result = false;
        try {
            if (isExist()) {
                int ret = mDatabase.delete(TABLE_NAME, null, null);
                if (ret > 0) {
                    result = true;
                } else {
                    result = false;
                }
            } else {
                result = true;
            }
            Log.i(TAG, "deleteAll = " + result);
        } catch (Exception e) {
            Log.i(TAG, "delete all error!");
        }
        return result;
    }

    public boolean Download(List<UserInfo> mList){
        boolean result = false;

        if(mList==null) return true;

        mDatabase.beginTransaction();  //手动设置开始事务
        try {
            for(int i=0; i<mList.size(); i++){
                result = true;
                Map<String, String> map = new HashMap<String, String>();
                map = (Map) util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(mList.get(i)), Map.class);
                try {
                    ContentValues cValues = new ContentValues();

                    for(Map.Entry<String, String> entry : map.entrySet()){
                        cValues.put(entry.getKey(), entry.getValue());
                    }

//    	            cValues.put("UserLoginID", mList.get(i).getUserLoginID());
//    	            cValues.put("UserName", mList.get(i).getUserName());
//    	            cValues.put("UserPWD", mList.get(i).getUserPWD());
//    	            cValues.put("UserDepart", mList.get(i).getUserDepart());
//    	            cValues.put("DepartName", mList.get(i).getDepartName());
//    	            cValues.put("RoleID", mList.get(i).getRoleID());
//    	            cValues.put("RoleName", mList.get(i).getRoleName());

                    long ret = mDatabase.insert(TABLE_NAME, null, cValues);

                    if (ret == -1) {
                        result = false;
                        Log.i(TAG, "insert -1!");
                        break;
                    } else {
                        result = true;
                    }
//    	            Log.e(TAG + " insert " + i,util.json.JSONUtils.toJsonWithGson(map));
                } catch (Exception e) {
                    Log.e(TAG, "insert error!");
                    result = false;
                    break;
                }
            }

            if(result) mDatabase.setTransactionSuccessful(); //设置事务处理成功，不设置会自动回滚不提交。
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        }finally{
            mDatabase.endTransaction(); //处理完成
        }

        return result;
    }

    public boolean UpdatePsw(String UserLoginID, String UserPWD){
        boolean ret = false;

        try {
            String whereClause = "UserLoginID=?";
            String[] whereArgs = new String[] { UserLoginID };

            ContentValues cValues = new ContentValues();
            cValues.put("UserPWD", UserPWD);

            long res = mDatabase.update(TABLE_NAME, cValues, whereClause, whereArgs);
            if (res == -1) {
                ret = false;
                Log.e(TAG, "UpdatePsw -1!");
            } else {
                ret = true;
                Log.e("UserLoginID=" + UserLoginID, "UserPWD= " + UserPWD);
                Log.e("UpdatePsw", " = " + res);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }
}
