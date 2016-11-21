package com.example.DAL;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.Project.R;

import java.util.ArrayList;
import java.util.List;

public class CRUD extends Activity {

    // Database fields
    private SQLiteDatabase database;
    private DBConnection dbHelper;
    private static String[] allColumns = {DBConnection.COLUMN_ID,
            DBConnection.COLUMN_FIRSTNAME, DBConnection.COLUMN_LASTNAME, DBConnection.COLUMN_PHONENUMBER};
    private static final String PREFS_NAME = "MyPrefsFile";

    public CRUD(Context context) {
        dbHelper = new DBConnection(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public GetSetters createTask(String firstnameCreateString, String lastnameCreateString, String phoneNumberCreateString) {
        ContentValues values = new ContentValues();
        values.put(DBConnection.COLUMN_FIRSTNAME, firstnameCreateString);
        values.put(DBConnection.COLUMN_LASTNAME, lastnameCreateString);
        values.put(DBConnection.COLUMN_PHONENUMBER, phoneNumberCreateString);
        long insertId = database.insert(DBConnection.TASKS_TABLE_NAME, null, values);
        Cursor cursor = database.query(DBConnection.TASKS_TABLE_NAME,
                allColumns, DBConnection.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        GetSetters newTask = cursorToTask(cursor);
        cursor.close();
        return newTask;
    }

    public void deleteTask(GetSetters task) {
        long id = task.getId();


        database.delete(DBConnection.TASKS_TABLE_NAME, DBConnection.COLUMN_ID
                + " = " + id, null);

    }

    public GetSetters getTask(int taskId) {
        String restrict = DBConnection.COLUMN_ID + "=" + taskId;
        Cursor cursor = database.query(true, DBConnection.TASKS_TABLE_NAME, allColumns, restrict,
                null, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            GetSetters task = cursorToTask(cursor);
            return task;
        }
        // Make sure to close the cursor
        cursor.close();
        return null;
    }

    public boolean updateTask(GetSetters task, String firstname, String lastname, String phonenumber) {
        long taskId = task.getId();
        ContentValues args = new ContentValues();
        args.put(DBConnection.COLUMN_FIRSTNAME, firstname);
        args.put(DBConnection.COLUMN_LASTNAME, lastname);
        args.put(DBConnection.COLUMN_PHONENUMBER, phonenumber);

        String restrict = DBConnection.COLUMN_ID + "=" + taskId;
        return database.update(DBConnection.TASKS_TABLE_NAME, args, restrict, null) > 0;
    }

    public List<GetSetters> getAllTasks() {
        List<GetSetters> users = new ArrayList<GetSetters>();


        Cursor cursor = database.query(DBConnection.TASKS_TABLE_NAME,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GetSetters user = cursorToTask(cursor);

            users.add(user);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return users;
    }

    private GetSetters cursorToTask(Cursor cursor) {
        GetSetters user = new GetSetters();
        user.setId(cursor.getLong(0));
        user.setFirstname(cursor.getString(1));
        user.setLastname(cursor.getString(2));
        user.setPhonenumber(cursor.getString(3));
        return user;
    }

    //Takes care of returning the Asc / Desc result
    public List<GetSetters> printAscDesc(Cursor c) {
        List<GetSetters> tasks = new ArrayList<GetSetters>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            GetSetters task = cursorToTask(c);
            tasks.add(task);
            c.moveToNext();
        }
        // Make sure to close the cursor
        c.close();
        return tasks;
    }

    public List<GetSetters> sortFirstAndLastname(int i) {
        List<GetSetters> tasks = new ArrayList<GetSetters>();

        switch (i) {
            case R.id.ASCfirstname:
                Cursor firstnameAsc = database.rawQuery("SELECT * FROM " + DBConnection.TASKS_TABLE_NAME + " ORDER BY " + DBConnection.COLUMN_FIRSTNAME + " ASC", new String[]{});
                tasks = printAscDesc(firstnameAsc);
                return tasks;
            case R.id.DESCfirstname:
                Cursor firstnameDesc = database.rawQuery("SELECT * FROM " + DBConnection.TASKS_TABLE_NAME + " ORDER BY " + DBConnection.COLUMN_FIRSTNAME + " DESC", new String[]{});
                tasks = printAscDesc(firstnameDesc);
                return tasks;
            case R.id.ASCLastname:
                Cursor lastnameAsc = database.rawQuery("SELECT * FROM " + DBConnection.TASKS_TABLE_NAME + " ORDER BY " + DBConnection.COLUMN_LASTNAME + " ASC", new String[]{});
                tasks = printAscDesc(lastnameAsc);
                return tasks;
            case R.id.DESCLastname:
                Cursor lastnameDesc = database.rawQuery("SELECT * FROM " + DBConnection.TASKS_TABLE_NAME + " ORDER BY " + DBConnection.COLUMN_LASTNAME + " DESC", new String[]{});
                tasks = printAscDesc(lastnameDesc);
                return tasks;
        }
        return null;
    }
}