package com.example.razafaraz.newapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class dbController extends SQLiteOpenHelper {
    public static final String DB_NAME = "androidsqlite.db";
    public static final String COLUMN_NAME = "Customer_Name";
    public static final String COLUMN_ID = "Customer_ID";
    public static final String COLUMN_STATUS = "status";
    public static final String TABLE_NAME = "customer";
    public static final int UPTODATE = 1;
    public static final int OUTOFDATE = 0;

    public static final int DB_VERSION = 1;
    public dbController(Context application ) {
        super(application , DB_NAME , null , DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db_sql) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME +
                " VARCHAR, " + COLUMN_STATUS +
                " TINYINT);";
        db_sql.execSQL(sql);
    }

    public void deleteAll()
    {
        SQLiteDatabase database =  this.getWritableDatabase();

        database.execSQL("delete from "+ TABLE_NAME);

        //database.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME= " + TABLE_NAME);
        //database.execSQL("delete from sqlite_sequence where name=");

        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_Version, int new_Version) {
        String sql = "DROP TABLE IF EXISTS customer";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    //Insert insto user database
    public long insertCustomer(HashMap<String , String  > query_Values)
    {
        long sucess = 0 ;
        SQLiteDatabase database =  this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Customer_ID" , query_Values.get("Customer_ID"));
        values.put("Customer_Name" , query_Values.get("Customer_Name"));
        values.put("status" , OUTOFDATE);
        sucess = database.insert(TABLE_NAME , null ,  values  );
        database.close();

        return sucess;
    }

    public ArrayList<HashMap<String , String>> getAllUser(){
        ArrayList<HashMap<String , String>> arrayList_Customer = new  ArrayList<HashMap<String , String>>() ;
        String sql = "SELECT * FROM customer";
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(sql , null);
        if(cursor.moveToFirst())
        {
            do{
                HashMap<String , String> map = new HashMap<String, String>();
                map.put("Customer_ID" , cursor.getString(0));
                map.put("Customer_Name" , cursor.getString(1));
                arrayList_Customer.add(map);
            }
            while (cursor.moveToNext());
        }
        database.close();
        return  arrayList_Customer;
    }

    public ArrayList<HashMap<String , String>> getAllUnsyncUser(){
        ArrayList<HashMap<String , String>> arrayList_Customer = new  ArrayList<HashMap<String , String>>() ;
        String sql = "SELECT * FROM customer where status = "+ '0';
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(sql , null);
        if(cursor.moveToFirst())
        {
            do{
                HashMap<String , String> map = new HashMap<String, String>();
                map.put("Customer_ID" , cursor.getString(0));
                map.put("Customer_Name" , cursor.getString(1));
                arrayList_Customer.add(map);
            }
            while (cursor.moveToNext());
        }
        database.close();
        return  arrayList_Customer;
    }

  /*  public String composeJSONSQLITE(){
        ArrayList<HashMap<String , String>> arrayList_Customer = new  ArrayList<HashMap<String , String>>() ;
        String sql = "SELECT * FROM customer where status = "+ '0';

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(sql , null);
        if(cursor.moveToFirst())
        {
            do{
                HashMap<String , String> map = new HashMap<String, String>();
                map.put("Customer_ID" , cursor.getString(0));
                map.put("Customer_Name" , cursor.getString(1));
                arrayList_Customer.add(map);
            }
            while (cursor.moveToNext());
        }
        database.close();


        return arrayList_Customer.toString(); /*NEED TO CHANG THIS*/
    //}

    public int dbSyncCount()
    {
        int count = 0;
        String sql = "SELECT * FROM customer where status = "+ '0';
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    public void changeToUpdatedStatus(String id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update customer set status = '" + UPTODATE + "'where Customer_ID = '"+ id+"'";
        database.execSQL(updateQuery);
        database.close();
    }


}
