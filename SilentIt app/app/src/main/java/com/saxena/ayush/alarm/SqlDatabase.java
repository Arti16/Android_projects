package com.saxena.ayush.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ayush on 1/28/2017.
 */
/*
public class Database{
    SQLiteDatabase db;
    public Database(int t,int c1,int c2,int c3,int c4,int c5,int c6,int c7)
    {
        String s="insert into ";
    }

}*/
public class SqlDatabase {

    public static final String TIME = "time";
    public static final String MONDAY = "monday";
    public static final String TUESDAY = "tuesday";
    public static final String WEDNESDAY = "wednesday";
    public static final String THURSDAY = "thursday";
    public static final String FRIDAY = "friday";
    public static final String SATURDAY = "saturday";
    public static final String SUNDAY = "sunday";

    private static final String DATABASE_NAME = "TimeTable";

    private static final String TABLE = "days";
    private static final String TABLE2 = "TimeTable";

    private static final int DATABASE_VERSION = 20141;

    private DbHelper mHelper;

    private final Context mContext;

    private SQLiteDatabase mDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        //Set up database here
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                            //Column name     Type of variable
                            TIME + " NUMBER, " +
                            MONDAY + " NUMBER, " +
                            TUESDAY + " NUMBER, " +
                            WEDNESDAY + " NUMBER, " +
                            THURSDAY + " NUMBER, " +
                            FRIDAY + " NUMBER, " +
                            SATURDAY + " NUMBER, " +
                            SUNDAY + " NUMBER);");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE2 + " (" +
                    //Column name     Type of variable
                    TIME + " NUMBER, " +
                    MONDAY + " VARCHAR(20), " +
                    TUESDAY + " VARCHAR(20), " +
                    WEDNESDAY + " VARCHAR(20), " +
                    THURSDAY + " VARCHAR(20), " +
                    FRIDAY + " VARCHAR(20), " +
                    SATURDAY + " VARCHAR(20), " +
                    SUNDAY + " VARCHAR(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE2);

            onCreate(db);
        }

    }

    public SqlDatabase (Context c) {
        mContext = c;
    }

    public SqlDatabase open() throws SQLException {
        //Set up the helper with the context
        mHelper = new DbHelper (mContext);
        //Open the database with our helper
        mDatabase = mHelper.getWritableDatabase();
        return this;
    }
    public void trunc()
    {
        mDatabase.execSQL("delete from "+TABLE2);
        updateEntry();
    }
    public void createEntry(String[] days) {
            ContentValues cv = new ContentValues();
            cv.put(TIME, Integer.parseInt(days[0]));
            cv.put(MONDAY, days[1]);
            cv.put(TUESDAY, days[2]);
            cv.put(WEDNESDAY, days[3]);
            cv.put(THURSDAY, days[4]);
            cv.put(FRIDAY, days[5]);
            cv.put(SATURDAY, days[6]);
            cv.put(SUNDAY, days[7]);
            mDatabase.insert(TABLE2, null, cv);

    }
    public void createEntry() {
        for(int i=7;i<=19;i++) {
            ContentValues cv = new ContentValues();
            cv.put(TIME,i);
            cv.put(MONDAY,0);
            cv.put(TUESDAY, 0);
            cv.put(WEDNESDAY,0);
            cv.put(THURSDAY, 0);
            cv.put(FRIDAY, 0);
            cv.put(SATURDAY, 0);
            cv.put(SUNDAY,0);
            mDatabase.insert(TABLE, null, cv);
        }

    }
    public void updateEntry() {
        for(int i=7;i<=19;i++) {
            ContentValues cv = new ContentValues();
            cv.put(TIME,i);
            cv.put(MONDAY,0);
            cv.put(TUESDAY, 0);
            cv.put(WEDNESDAY,0);
            cv.put(THURSDAY, 0);
            cv.put(FRIDAY, 0);
            cv.put(SATURDAY, 0);
            cv.put(SUNDAY,0);
            mDatabase.update(TABLE,cv,"time="+i,null);
        }

    }
    public void updateEntry(int time,String day,int val) {
        ContentValues cv = new ContentValues();
        cv.put(day,val);
        mDatabase.update(TABLE,cv,"time="+time,null);

    }
    public void createEntry(int days[])
    {
        ContentValues cv=new ContentValues();
        //cv.put(TIME, days[0]);
        cv.put(MONDAY, days[1]);
        cv.put(TUESDAY, days[2]);
        cv.put(WEDNESDAY, days[3]);
        cv.put(THURSDAY, days[4]);
        cv.put(FRIDAY, days[5]);
        cv.put(SATURDAY, days[6]);
        cv.put(SUNDAY, days[7]);
        mDatabase.update(TABLE,cv,"time="+days[0],null);
    }
    public void closeTable()
    {
        if(mDatabase!=null && mHelper!=null) {
            mHelper.close();
            mDatabase.close();
        }
    }
    public Cursor getCursor(String day)
    {String sql;
        if(day==null)
            sql="select * from "+TABLE;
        else
            sql="select time,"+day+" from "+TABLE;
        Cursor c=mDatabase.rawQuery(sql,null);
            return c;
    }
}
