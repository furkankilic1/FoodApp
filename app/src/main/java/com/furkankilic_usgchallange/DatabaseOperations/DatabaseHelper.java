package com.furkankilic_usgchallange.DatabaseOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME="FavoriteDB";
    public static int DATABASE_VERSION = 1;
    SQLiteDatabase db;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );

        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(FavoriteDB.CREATE_TABLE_SQL);

        }catch (SQLException e){
            Log.d("DATABASE OPERATIONS", "Error");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(FavoriteDB.DROP_TABLE_SQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
        onCreate(db);
    }

    public Cursor getAllRecords(String tableName, String[] colums){
        Cursor cursor = db.query(tableName, colums, null, null, null, null,null);
        return cursor;
    }
    public Cursor getSomeRecords( String tableName, String[] columns,String whereCondition ){
        Cursor cursor = db.query(tableName, columns, whereCondition, null, null, null, null);
        return cursor;
    }

    public boolean insert(String tableName, ContentValues contentValues) {
        return db.insert(tableName, null, contentValues)>0;
    }

    public boolean delete(String tableName, String whereCondition) {
        return db.delete(tableName, whereCondition, null)>0;
    }

}
