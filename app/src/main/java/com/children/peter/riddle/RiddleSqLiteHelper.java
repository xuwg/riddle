package com.children.peter.riddle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/5.
 */

public class RiddleSqLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "RiddleSqLiteHelper";
    private static final String CREATE_RIDDLES = "create table riddles("
            + "id integer primary key autoincrement,"
            + "category integer,"
            + "type integer,"
            + "content text,"
            + "key text)";

    public RiddleSqLiteHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_RIDDLES);
        Log.d(TAG, "onCreate: " + CREATE_RIDDLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
