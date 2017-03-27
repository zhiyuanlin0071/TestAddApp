package com.mktech.testaddapp.dao;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;

        import com.mktech.testaddapp.entity.HotAppInfo;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by stefan on 2017/3/27.
 */

public class AppinfoHandle {
    private Context	mContext;
    private AppSql	mAppSql;

    public AppinfoHandle(Context context) {
        mContext = context;
        mAppSql = new AppSql(context);
    }
    public List<HotAppInfo> getHotAppinfo() {
        List<HotAppInfo> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mAppSql.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from APPINFO", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                HotAppInfo hotAppInfo = new HotAppInfo();
                hotAppInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
                hotAppInfo.setPackageName(cursor.getString(cursor.getColumnIndex("packageName")));
                list.add(hotAppInfo);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public void addHotAppinfo(HotAppInfo info){
        SQLiteDatabase database=mAppSql.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",info.getName());
        contentValues.put("packageName",info.getPackageName());
        database.replace("APPINFO",null,contentValues);
        database.close();
    }

}
