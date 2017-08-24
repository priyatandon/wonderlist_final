package com.ptandon.wonderlist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "list.db";


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_LIST_TABLE = "CREATE TABLE " + ListContract.ListEntry.TABLE_NAME + "(" +
                ListContract.ListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ListContract.ListEntry.COLUMN_TODOTEXT + " TEXT," +
                ListContract.ListEntry.COLUMN_DUE_DATE + " INTEGER," +
                ListContract.ListEntry.COLUMN_PRIORITY + " TEXT," +
                ListContract.ListEntry.COLUMN_STATUS + " TEXT," +
                ListContract.ListEntry.COLUMN_TASKNOTE + " TEXT )";
        db.execSQL(SQL_CREATE_LIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ ListContract.ListEntry.TABLE_NAME);
        onCreate(db);
    }


    public ArrayList<List> getData(int query_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<List> lst= new ArrayList<List>();
        String status;
        if(query_id==1)
            status = "TODO";
        else
            status ="DONE";
        Cursor result = db.rawQuery("select * from "+ ListContract.ListEntry.TABLE_NAME +" where "+ ListContract.ListEntry.COLUMN_STATUS +" = '" + status + "'" + "order by " + ListContract.ListEntry.COLUMN_DUE_DATE, null);

        while(result.moveToNext()){
            lst.add( new List(result.getInt(result.getColumnIndex(ListContract.ListEntry._ID)),
                    result.getString(result.getColumnIndex(ListContract.ListEntry.COLUMN_TODOTEXT)),
                    result.getInt(result.getColumnIndex(ListContract.ListEntry.COLUMN_DUE_DATE)),
                    result.getString(result.getColumnIndex(ListContract.ListEntry.COLUMN_PRIORITY)),
                    result.getString(result.getColumnIndex(ListContract.ListEntry.COLUMN_STATUS)),
                    result.getString(result.getColumnIndex(ListContract.ListEntry.COLUMN_TASKNOTE))
            ));
        }
        result.close();
        return lst;
    }
}


