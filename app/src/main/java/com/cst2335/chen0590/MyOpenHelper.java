package com.cst2335.chen0590;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class MyOpenHelper extends SQLiteOpenHelper {
        public static final String filename="MyDatabase";
        public static final int version=1;
        public static final String TABLE_NAME="MyData";
        public static final String COL_ID="_id";
        public static final String COL_MESSAGE="Message";
        public static final String COL_SEND_RECEIVE="SendOrReceive";

        public MyOpenHelper(Context context){
            super(context, filename,null,version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String result="CREATE TABLE " + TABLE_NAME + " ("
                    +COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +COL_MESSAGE + " TEXT, "
                    +COL_SEND_RECEIVE +" TEXT )";

            db.execSQL(result);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table if exists "+TABLE_NAME);
            this.onCreate(db);
        }


    }

