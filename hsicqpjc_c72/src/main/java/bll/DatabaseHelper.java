package bll;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance = null;

    /** 数据库名称 **/
    public static final String DATABASE_NAME = "com_hsic_qp_szjc.db";

    /** 数据库版本号 **/
    private static final int DATABASE_VERSION = 1;

    /** 数据库SQL语句 添加一个表 **/
    /**用户基本信息 **/
    private static final String UserInfo_CREATE = "create table if not exists UserInfo (" +
            "UserLoginID TEXT," +
            "UserName TEXT, " +
            "UserPWD TEXT," +
            "UserDepart TEXT," +
            "DepartName TEXT," +
            "RoleID TEXT," +
            "RoleName TEXT" +
            ")";
    /**钢质无缝气瓶检验信息 **/
    private static final String NewGZWFQPCheck_CREATE = "create table if not exists NewGZWFQPCheck (" +
            "State TEXT," +//0未上传 1上传成功 2上传失败
            //气瓶基本信息
            "QPDJCODE TEXT, " +//气瓶使用登记代码(EPC)
            "KEYGPNO TEXT," +//气瓶唯一标识码-不使用
            "GPNO TEXT," +//气瓶编号/集格编号 (USER)
            "YHNO TEXT," +//企业自编号-不使用
            "MadeNo TEXT," +//气瓶制造单位代码-不使用
            "MadeDate TEXT," +////气瓶制造日期 (USER)
            "CZDW TEXT," +//气瓶充装单位代码 (同产权单位EPC)
            "QPGG TEXT,"+//气瓶类型编号
            "QPGGName TEXT,"+//气瓶类型名中文描述
            "BottleKindCode TEXT,"+//气瓶类型代码
            "BoroughCode TEXT,"+//行政区划代码
            "PropertyUnitCode TEXT,"+//产权单位代码(EPC)
            "IssYear TEXT,"+//使用登记入编年份 (19000001) =19
            "UseRegCode TEXT,"+//气瓶使用登记代码序列号 =000001
            "MediumCode TEXT,"+//气瓶充装气体介质代码 (EPC)
            "TagID TEXT,"+//电子标签识别符 (EPC)
            "SenBaoDate TEXT,"+//气瓶使用代码申报时间
            "BanFaDate TEXT,"+//气瓶使用登记证颁发时间
            "SHFLAG TEXT,"+//申报状态
            "QPSYDJZBM TEXT,"+//气瓶使用登记证编码
            "JGType TEXT,"+//集格类型编号
            "LABELType TEXT,"+//数据库记录对应电子标签类型标识
            "JG TEXT,"+//气瓶所属集格编号
            //检验基本信息
            "CheckTime TEXT,"+//气瓶检验时间 yyyy-MM-dd HH:mm:ss
            "JCRQ TEXT,"+//气瓶检验日期 yyyy-MM-dd
            "XCJCRQ TEXT,"+//气瓶下次检验日期 yyyy-MM-dd
            "C_ZL TEXT,"+//检验结果(0:合格;1:报废)
            "JCX0 TEXT,"+//外观检查结果(1:合格;0:报废)
            "JCX1 TEXT,"+//音响检查结果(1:合格;0:报废)
            "JCX2 TEXT,"+//瓶口螺纹检查结果(1:合格;0:报废)
            "JCX3 TEXT,"+//内部检查结果(1:合格;0:报废)
            "JCX4 TEXT,"+//重量与容积测定结果(1:合格;0:报废)
            "JCX5 TEXT,"+//水压试验结果(1:合格;0:报废)
            "JCX6 TEXT,"+//气密性试验结果(1:合格;0:报废)
            "BFYY TEXT,"+//气瓶报废原因描述
            "BFYYDM TEXT,"+//气瓶报废原因标记
            //检验检测数据
            "KSHSSYBH TEXT,"+//0磕伤划伤凹坑处剩余壁厚
            "SJBH TEXT,"+//1设计壁厚
            "AXSD TEXT,"+//2凹陷深度
            "AXDJ TEXT,"+//3凹陷短径
            "KSHSSD TEXT,"+//4磕伤划伤长度
            "QXXMSYBH TEXT,"+//5缺陷修磨后剩余壁厚
            "FSCSYBH TEXT,"+//6腐蚀处剩余壁厚
            "JIANJU TEXT,"+//7底座支撑面与瓶底中心的间距
            "TTJMWJC TEXT,"+//8筒体截面最大与最小外径差
            "TTJMPJWJ TEXT,"+//9筒体截面平均外径
            "TTZXD TEXT,"+//10筒体直线度
            "PTZXCD TEXT,"+//11瓶体直线长度
            "WQSD TEXT,"+//12弯曲深度
            "PTCZD TEXT,"+//13瓶体垂直度
            "SCZL TEXT,"+//14实测重量
            "GYBJZL TEXT,"+//15钢印标记重量
            "MINBH TEXT,"+//16最小壁厚
            "SCRJ TEXT,"+//17实测容积
            "GYBJRJ TEXT,"+//18钢印标记容积
            "RJCYBXL TEXT,"+//19容积残余变形率
            //检验操作人员
            "C_WGJC TEXT,"+//外观检验员
            "C_WGSH TEXT,"+//外观审核员
            "C_YSJC TEXT,"+//音响检验员
            "C_YSSH TEXT,"+//音响审核员
            "C_PKLWJC TEXT,"+//瓶口螺纹检验员
            "C_PKLWSH TEXT,"+//瓶口螺纹审核员
            "C_NBJC TEXT,"+//内部检验员
            "C_NBSH TEXT,"+//内部审核员
            "C_RJJC TEXT,"+//重量与容积检验员
            "C_RJSH TEXT,"+//重量与容积审核员
            "C_SYJC TEXT,"+//水压检验员
            "C_SYSH TEXT,"+//水压审核员
            "C_QMJC TEXT,"+//气密检验员
            "C_QMSH TEXT,"+//气密审核员
            //操作信息
            "PosID TEXT,"+//设备编号
            "LogTime TEXT"+//上传时间;
            ")";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** 单例模式 **/
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        creatTables(db);
    }

    private void creatTables(SQLiteDatabase db) {
        /** 向数据中添加表 **/
        db.execSQL(UserInfo_CREATE);
        db.execSQL(NewGZWFQPCheck_CREATE);
    }

    private void deleteTables(SQLiteDatabase db){
        db.execSQL("delete from UserInfo_CREATE");
        db.execSQL("delete from NewGZWFQPCheck_CREATE");

        db.execSQL("DROP table UserInfo_CREATE");
        db.execSQL("DROP table NewGZWFQPCheck_CREATE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /** 可以拿到当前数据库的版本信息 与之前数据库的版本信息 用来更新数据库 **/
        Log.i("old="+oldVersion, "new="+newVersion);
        if (newVersion > oldVersion) {
            deleteTables(db);

            creatTables(db);
        }
    }

    /**
     * 删除数据库
     *
     * @param context
     * @return
     */
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }
}
