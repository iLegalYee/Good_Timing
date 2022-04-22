package edu.fsu.cs.goodtiming;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public final static String DBNAME = "GOODTIMINGDB";
    public final static int DBVERSION = 1;
    public final static String AUTHORITY = "edu.fsu.cs.goodtiming.provider";

    // These are the names of the tables and table columns
    public final static String TABLE_EVENTS = "EventsTable";
    public final static String COLUMN_EVENTS_ID = "_ID";
    public final static String COLUMN_EVENTS_NAME = "Name";
    public final static String COLUMN_EVENTS_DESCRIPTION = "Description";
    public final static String COLUMN_EVENTS_TIME = "Time";
    public final static String COLUMN_EVENTS_DATE = "Date";
    public final static String COLUMN_EVENTS_REPEAT = "Repeatability";
    public final static String COLUMN_EVENTS_LOCATION = "Location";
    public final static String COLUMN_EVENTS_DURATION = "Duration";
    public final static String COLUMN_EVENTS_IS_SESSION = "Session";

    public final static Uri EVENTS_CONTENT_URI = Uri.parse(
            "content://edu.fsu.cs.goodtiming.provider/" + TABLE_EVENTS);
    public final static String EVENTS_SQL_CREATE_MAIN =
            "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + " ( " +
                    COLUMN_EVENTS_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_EVENTS_NAME + " TEXT, " +
                    COLUMN_EVENTS_DESCRIPTION + " TEXT, " +
                    COLUMN_EVENTS_TIME + " TEXT, " +
                    COLUMN_EVENTS_DATE + " TEXT, " +
                    COLUMN_EVENTS_REPEAT + " TEXT, " +
                    COLUMN_EVENTS_LOCATION + " TEXT, " +
                    COLUMN_EVENTS_DURATION + " TEXT, " +
                    COLUMN_EVENTS_IS_SESSION + " TEXT)";


    public final static String TABLE_TASKS = "TasksTable";
    public final static String COLUMN_TASKS_ID = "_ID";
    public final static String COLUMN_TASKS_NAME = "Name";
    public final static String COLUMN_TASKS_DESCRIPTION = "Description";
    public final static String COLUMN_TASKS_DEADLINE = "Deadline";

    public final static Uri TASKS_CONTENT_URI = Uri.parse(
            "content://edu.fsu.cs.goodtiming.provider/" + TABLE_TASKS);
    public final static String TASKS_SQL_CREATE_MAIN =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( " +
                    COLUMN_TASKS_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_TASKS_NAME + " TEXT, " +
                    COLUMN_TASKS_DESCRIPTION + " TEXT, " +
                    COLUMN_TASKS_DEADLINE + " TEXT)";


    public final static String TABLE_ANALYTICS = "AnalyticsTable";
    public final static String COLUMN_ANALYTICS_ID = "_ID";
    public final static String COLUMN_ANALYTICS_DATA = "Date";
    public final static String COLUMN_ANALYTICS_TIME = "Time";
    public final static String COLUMN_ANALYTICS_NAME = "Name";

    public final static Uri ANALYTICS_CONTENT_URI = Uri.parse(
            "content://edu.fsu.cs.goodtiming.provider/" + TABLE_ANALYTICS);
    public final static String ANALYTICS_SQL_CREATE_MAIN =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ANALYTICS + " ( " +
                    COLUMN_ANALYTICS_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_ANALYTICS_DATA + " TEXT, " +
                    COLUMN_ANALYTICS_TIME + " TEXT, " +
                    COLUMN_ANALYTICS_NAME + " TEXT)";

    private DatabaseHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        helper.getWritableDatabase();
        return true;
    }

    // Inserts table entry then returns a URI to it
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = helper.getWritableDatabase().insert(
                uri.toString().substring(uri.toString().lastIndexOf("/") + 1),
                null, values);
        return Uri.withAppendedPath(uri, "" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return helper.getWritableDatabase().update(
                uri.toString().substring(uri.toString().lastIndexOf("/") + 1),
                values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return helper.getWritableDatabase().delete(
                uri.toString().substring(uri.toString().lastIndexOf("/") + 1),
                selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return helper.getReadableDatabase().query(
                uri.toString().substring(uri.toString().lastIndexOf("/") + 1),
                projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    protected static final class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DBNAME, null, DBVERSION);
            getReadableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(EVENTS_SQL_CREATE_MAIN);
            db.execSQL(TASKS_SQL_CREATE_MAIN);
            db.execSQL(ANALYTICS_SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {}
    }
}
