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
    public final static String TABLE_NAMESTABLE = "EmployeeTable";
    public final static String COLUMN_EMPID = "empid";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_EMAIL = "email";
    public final static String COLUMN_GENDER = "gender";
    public final static String COLUMN_CODE = "accesscode";
    public final static String COLUMN_DEPARTMENT = "department";

    public final static String AUTHORITY = "edu.fsu.cs.hw5.provider";
    public final static Uri CONTENT_URI = Uri.parse(
            "content://edu.fsu.cs.hw5.provider/" + TABLE_NAMESTABLE);
    public final static int DBVERSION = 1;
    public final static String SQL_CREATE_MAIN =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAMESTABLE + " ( " +
                    "_ID INTEGER PRIMARY KEY, " +
                    COLUMN_EMPID + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_GENDER + " TEXT, " +
                    COLUMN_CODE + " TEXT, " +
                    COLUMN_DEPARTMENT + " TEXT)";
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
        // Checks that all column values are filled out
        String empid = values.getAsString(COLUMN_EMPID).trim();
        String name = values.getAsString(COLUMN_NAME).trim();
        String email = values.getAsString(COLUMN_EMAIL).trim();
        String gender = values.getAsString(COLUMN_GENDER).trim();
        String code = values.getAsString(COLUMN_CODE).trim();
        String department = values.getAsString(COLUMN_DEPARTMENT).trim();
        if(empid.equals("") || name.equals("") || email.equals("") || gender.equals("") ||
                code.equals("") || department.equals(""))
            return null;

        long id = helper.getWritableDatabase().insert(TABLE_NAMESTABLE, null, values);
        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return helper.getWritableDatabase().update(TABLE_NAMESTABLE, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return helper.getWritableDatabase().delete(TABLE_NAMESTABLE, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return helper.getReadableDatabase().query(TABLE_NAMESTABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
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
            db.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {}
    }
}
