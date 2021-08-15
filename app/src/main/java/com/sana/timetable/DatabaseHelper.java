package com.sana.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="WorkVerba.db";
    public static  final String TABLE_NAME1="Timetable";
    public static  final String COL_1="ID";
    public static  final String COL_2="SUBJECT";
    public static  final String COL_3="DAY";
    public static  final String COL_4="START_TIME";
    public static  final String COL_5="END_TIME";
    public static  final String COL_6="ROOM";
    int id=1;
    Context context;
    private static final String TABLE_NAME2 = "mynotes";
    private static final String ColumnId = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnDescription = "description";

    public DatabaseHelper( Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1="CREATE TABLE "+TABLE_NAME1+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,SUBJECT TEXT, DAY TEXT, START_TIME TEXT,END_TIME TEXT,ROOM TEXT)";
        String query2 = "CREATE TABLE " +TABLE_NAME2+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,description TEXT)";

        db.execSQL(query1);
        db.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME2);
        onCreate(db);

    }
    public boolean insertData(String subject, String day,String start_time, String end_time,String room)
    {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,subject);
        contentValues.put(COL_3,day);
        contentValues.put(COL_4,start_time);
        contentValues.put(COL_5,end_time);
        contentValues.put(COL_6,room);

        result=db.insert(TABLE_NAME1,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;

    }

    public Cursor readData(String day){
        SQLiteDatabase db=this.getWritableDatabase();
        String qry="SELECT * FROM "+ TABLE_NAME1 + " WHERE DAY='"+day+"'";
        Cursor cursor=db.rawQuery(qry,null);
        return cursor;


    }
    public boolean  deleteItem(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TABLE_NAME1, "id=?", new String[]{id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    boolean addNotes(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);

        long resultValue = db.insert(TABLE_NAME2, null, cv);
        if(resultValue == -1){
            return false;
        }
        else {
            return true;
        }
    }
    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME2;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }
    void deleteAllNotes() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM " +TABLE_NAME2;
        database.execSQL(query);
    }
    boolean updateNotes(String title, String description, String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);

        long result = database.update(TABLE_NAME2, cv, "id=?", new String[]{id});
        if(result == -1){
            return false;
        }
        else {
            return true;
        }

    }
    public boolean deleteSingleItem(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TABLE_NAME2, "id=?", new String[]{id});
        if (result==-1){
            return false;
        }
        else {
            return true;
        }
//
    }

}


