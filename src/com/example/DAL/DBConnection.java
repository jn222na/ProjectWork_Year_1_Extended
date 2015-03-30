package com.example.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBConnection extends SQLiteOpenHelper {

	public static final String TASKS_TABLE_NAME = "members";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FIRSTNAME = "firstname";
	public static final String COLUMN_LASTNAME = "lastname";
	public static final String COLUMN_PHONENUMBER = "phonenumber";
	public static final String DATABASE_NAME = "tasks.db";
	private static final int DATABASE_VERSION = 1;
	

    private static final String DATABASE_CREATE = "create table "
    		+ TASKS_TABLE_NAME 
    		+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FIRSTNAME + " TEXT NOT NULL, "
            + COLUMN_LASTNAME  + " TEXT NOT NULL, "
            + COLUMN_PHONENUMBER  + " TEXT NOT NULL);";

	
	public DBConnection(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(DBConnection.class.getName(), "Upgrading database from version " 
	    		+ oldVersion + " to " + newVersion 
	    		+ ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE_NAME);
        onCreate(db);
	}

}
