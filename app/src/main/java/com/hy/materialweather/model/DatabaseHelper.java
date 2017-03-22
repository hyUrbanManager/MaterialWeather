package com.hy.materialweather.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite 数据库助手
 * 推荐使用原生SQL语句，以提高SQL语句熟练程度
 *
 * @author hy
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public final String CREATE_BOOK = "create tablet Book (" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real" +
            "page integer" +
            "name text) ";

    private Context context;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
