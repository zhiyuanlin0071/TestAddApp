package com.mktech.testaddapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by stefan on 2017/3/27.
 */

public class AppSql extends SQLiteOpenHelper {
    private static final String TAG = "DBSQLiteHelper";
    private static final String Name = "APP";
    private static final int VERSION = 1;
    public AppSql(Context context) {
        super(context, Name, null, VERSION);
}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE APPINFO("+"id integer primary key,"+"name varchar UNIQUE,"+"packageName varchar"+")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS APPINFO");
    }
}
