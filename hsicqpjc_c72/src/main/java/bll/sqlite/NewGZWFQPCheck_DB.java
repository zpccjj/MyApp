package bll.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import bean.NewGZWFQPCheck;
import bean.StatisticsItem;
import bll.DatabaseHelper;

public class NewGZWFQPCheck_DB {
    private static final String TAG = "NewGZWFQPCheck_DB";

    private static final String TABLE_NAME = "NewGZWFQPCheck";

    private SQLiteDatabase mDatabase = null;

    public NewGZWFQPCheck_DB(Context context){
        this.mDatabase = DatabaseHelper.getInstance(context).getReadableDatabase();
    }

    //表是否存在数据
    public boolean isExist() {
        boolean result = false;
        try {
            String[] columns = new String[] { "QPDJCODE","State"};
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

    //表是否存在未上传/上传成功的数据 //0未上传 1上传成功
    public boolean isExist(String State) {
        boolean result = false;
        try {
            String whereClause = "State=?";
            String[] whereArgs = new String[] { State };
            String[] columns = new String[] { "QPDJCODE","State"};
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

    //isAll true:全部记录 false:未上传记录
    public List<NewGZWFQPCheck> getUploadCheck(boolean isAll){
        List<NewGZWFQPCheck> list = new ArrayList<NewGZWFQPCheck>();

        try {
            String whereClause = "State=? ";
            String[] whereArgs = new String[] { "0" };
            if(isAll){
                whereClause = null;
                whereArgs = null;
            }
            String[] columns = null;

            Cursor cursor = mDatabase.query(TABLE_NAME, columns, whereClause,
                    whereArgs, null, null, null);
            if (cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<String, String>();
                    for (int i = 0, j = cursor.getColumnCount(); i < j; i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
//                    Log.e("NewGZWFQPCheck map",util.json.JSONUtils.toJsonWithGson(map));
                    NewGZWFQPCheck info = (NewGZWFQPCheck)util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(map), NewGZWFQPCheck.class);
                    list.add(info);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return list;
    }


    //表是否存在QPDJCODE + JCRQ重复的记录 //
    public NewGZWFQPCheck isExist(String QPDJCODE, String JCRQ) {
        NewGZWFQPCheck result = null;
        try {
            String whereClause = "QPDJCODE=? and JCRQ=?";
            String[] whereArgs = new String[] {QPDJCODE, JCRQ};
            String[] columns = new String[] { "QPDJCODE","JCRQ", "State"};
            Cursor cursor = mDatabase.query(TABLE_NAME, columns, whereClause,
                    whereArgs, null, null, null);
            if (cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<String, String>();
                    for (int i = 0, j = cursor.getColumnCount(); i < j; i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    result = (NewGZWFQPCheck)util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(map), NewGZWFQPCheck.class);
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //QPDJCODE + JCRQ 无重复做插入操作
    public boolean insertCheck(NewGZWFQPCheck QP){
        boolean result = false;

        try {
            result = true;
            Map<String, String> map = new HashMap<String, String>();
            map = (Map) util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(QP), Map.class);

            ContentValues cValues = new ContentValues();

            for(Map.Entry<String, String> entry : map.entrySet()){
                cValues.put(entry.getKey(), entry.getValue());
            }

            long ret = mDatabase.insert(TABLE_NAME, null, cValues);

            if (ret == -1) {
                result = false;
                Log.i(TAG, "insertCheck -1!");
            } else {
                result = true;
                Log.e(TAG + " insertCheck OK ="+ret, util.json.JSONUtils.toJsonWithGson(map));
            }


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    //QPDJCODE + JCRQ 有重复做更新操作
    public boolean updateCheck(NewGZWFQPCheck QP){
        boolean result = false;

        try {
            String whereClause = "QPDJCODE=? and JCRQ=?";
            String[] whereArgs = new String[] {QP.getQPDJCODE(), QP.getJCRQ() };

            Map<String, String> map = new HashMap<String, String>();
            map = (Map) util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(QP), Map.class);

            ContentValues cValues = new ContentValues();

            for(Map.Entry<String, String> entry : map.entrySet()){
                cValues.put(entry.getKey(), entry.getValue());
            }

            long res = mDatabase.update(TABLE_NAME, cValues, whereClause, whereArgs);
            if (res == -1) {
                result = false;
                Log.e(TAG, "updateCheck -1!");
            } else {
                result = true;
                Log.e("QPDJCODE=" + QP.getQPDJCODE(), "JCRQ= " + QP.getJCRQ());
                Log.e("TAG", "updateCheck = " + res);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    //上传成功后，将未上传的QPDJCODE + JCRQ的State标志位由0更新为1
    public boolean updateCheckState(String QPDJCODE, String JCRQ){

        boolean result = false;
        try {
            String whereClause = "State=? and QPDJCODE=? and JCRQ=?";
            String[] whereArgs = new String[] { "0", QPDJCODE, JCRQ };
            ContentValues cValues = new ContentValues();
            cValues.put("State", "1");
            long res = mDatabase.update(TABLE_NAME, cValues, whereClause, whereArgs);
            if (res == -1) {
                result = false;
                Log.e(TAG, "updateCheckState -1!");
            } else {
                result = true;
                Log.e("QPDJCODE=" + QPDJCODE, "JCRQ= " + JCRQ);
                Log.e("TAG", "updateCheckState = " + res);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean deleteAllCheck(String State){
        boolean result = false;
        if(isExist()){
            String whereClause = "State=?";
            String[] whereArgs = new String[] { State };
            if(mDatabase.delete(TABLE_NAME, null, null)>0) result = true;
            else result = false;
        }else{
            result = true;
        }

        return result;
    }

    public List<StatisticsItem> getStatistics(){
        List<StatisticsItem> list = new ArrayList<StatisticsItem>();

        if(isExist()){
            try {
                String sql = "SELECT COUNT(A.QPDJCODE) AS Num, COUNT(B.QPDJCODE) AS Uploaded, COUNT(C.QPDJCODE) AS NotUploaded, A.JCRQ AS CheckDate"
                        + " FROM NewGZWFQPCheck A "
                        + " LEFT OUTER JOIN NewGZWFQPCheck B ON A.QPDJCODE = B.QPDJCODE and A.JCRQ = B.JCRQ and A.State = B.State and B.State='1' "
                        + " LEFT OUTER JOIN NewGZWFQPCheck C ON A.QPDJCODE = C.QPDJCODE and A.JCRQ = C.JCRQ and A.State = C.State and C.State='0' "
                        + " GROUP BY CheckDate;";
                Cursor cursor = mDatabase.rawQuery(sql, null);
                if (cursor.getCount() >0) {
                    while (cursor.moveToNext()) {
                        Map<String, String> map = new HashMap<String, String>();
                        for (int i = 0, j = cursor.getColumnCount(); i < j; i++) {
                            map.put(cursor.getColumnName(i), cursor.getString(i));
                        }
                        StatisticsItem si = (StatisticsItem) util.json.JSONUtils.toObjectWithGson(util.json.JSONUtils.toJsonWithGson(map), StatisticsItem.class);
                        list.add(si);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }

        return list;
    }
}
