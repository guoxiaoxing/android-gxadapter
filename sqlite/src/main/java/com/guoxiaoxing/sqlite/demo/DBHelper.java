package com.guoxiaoxing.sqlite.demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.ref.PhantomReference;

/**
 * For more information, you can visit https://github.com/guoxiaoxing or contact me by
 * guoxiaoxingse@163.com.
 *
 * @author guoxiaoxing
 * @since 2017/7/13 下午5:48
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "crub.db";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_STUDENT = "CREATE_TABLE" + Student.TABLE
                + "("
                + Student.KEY_ID + "INTEGER PRIMARY KEY AUTOINCREMENT"
                + Student.KEY_NAME + "ITEXT"
                + Student.KEY_EMAIL + "TEXT"
                + Student.KEY_AGE + "INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + Student.TABLE);
        onCreate(db);
    }
}
