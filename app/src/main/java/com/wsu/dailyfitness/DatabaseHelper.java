package com.wsu.dailyfitness;

import java.util.ArrayList;
import java.util.Vector;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Fitness.db";
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_USERNAME = "username";
    public static final String USER_FULL_NAME = "fullname";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String DAILY_STATS_TABLE_NAME = "daily_stats";
    public static final String DAILY_DATE = "date";
    public static final String DAILY_STEPS = "steps";
    static final String ID = "id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE if exists " + USER_TABLE_NAME);

        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + " ( "//
                + USER_USERNAME + " VARCHAR PRIMARY KEY, "//
                + USER_FULL_NAME + " VARCHAR, "//
                + USER_EMAIL + " VARCHAR, "//
                + USER_PASSWORD + " VARCHAR) ");

        db.execSQL("DROP TABLE if exists " + DAILY_STATS_TABLE_NAME);

        db.execSQL("CREATE TABLE " + DAILY_STATS_TABLE_NAME + " ( "//
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "//
                + USER_USERNAME + " VARCHAR, "//
                + DAILY_DATE + " VARCHAR, "//
                + DAILY_STEPS + " INTEGER) ");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertDailyStats(String username, String date, int steps ) {

        int id = isTodayFound(username, date);
        if(id == -1){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(USER_USERNAME, username);
            cv.put(DAILY_DATE, date);
            cv.put(DAILY_STEPS, steps);
            long row_id = db.insert(DAILY_STATS_TABLE_NAME, null, cv);
            db.close();

            return (row_id != -1);
        }else{

            return updateSteps(id, steps);
        }
    }

    private int isTodayFound(String username, String date){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from daily_stats where username = '" + username + "' AND date = '" + date + "'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            int id = res.getInt(res.getColumnIndex(ID));
            return id;
        }

        return -1;
    }

    public boolean updateSteps(int id, int steps) {
        SQLiteDatabase db = this.getReadableDatabase();
        String strFilter = ID + "=" + id;
        ContentValues args = new ContentValues();
        args.put(DAILY_STEPS, steps);
        int row_id = db.update(DAILY_STATS_TABLE_NAME, args, strFilter, null);
        db.close();
        return (row_id != -1);

    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_USERNAME, user.getUserName());
        cv.put(USER_FULL_NAME, user.getFullName());
        cv.put(USER_EMAIL, user.getEmail());
        cv.put(USER_PASSWORD, user.getPassword());

        long row_id = db.insert(USER_TABLE_NAME, null, cv);
        db.close();

        return (row_id != -1);
    }

    public boolean isuserFound(String username, String password){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users where username = '" + username + "' AND password = '" + password + "'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            return true;
        }
         return false;
    }

    public ArrayList<User> getAlluser() {
        ArrayList<User> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new User(res.getString(res.getColumnIndex(USER_FULL_NAME)), res.getString(res.getColumnIndex(USER_USERNAME)), res.getString(res.getColumnIndex(USER_EMAIL)), res.getString(res.getColumnIndex(USER_PASSWORD))));
            res.moveToNext();
        }
        return array_list;
    }

    public Vector<Statistics> getSteps(String username) {
        Vector<Statistics> list = new Vector<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from daily_stats where username = '" + username +  "'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            list.add(new Statistics(res.getString(res.getColumnIndex(DAILY_DATE)), res.getInt(res.getColumnIndex(DAILY_STEPS))));
            res.moveToNext();
        }
        return list;
    }
}
