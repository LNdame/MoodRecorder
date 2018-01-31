package cct.ansteph.moodrecorder.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import cct.ansteph.moodrecorder.api.Tables;
import cct.ansteph.moodrecorder.api.columns.ActivityColumns;
import cct.ansteph.moodrecorder.api.columns.DiaryUserColumns;
import cct.ansteph.moodrecorder.helper.DbHelper;


public class DiaryUserContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://cct.ansteph.moodrecorder.contentprovider.diaryusercontentprovider/moodrecorder");
    private SQLiteDatabase db;

    public DbHelper databhelper ;


    public DiaryUserContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int retVal = db.delete(Tables.DIARYUSER_TABLE, selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return retVal;
    }

    @Override
    public String getType(Uri uri) {

        // at the given URI.
        return null;

    }

    @Override
    public Uri insert(Uri uri, ContentValues inValues) {
        ContentValues values = new ContentValues(inValues);

        long rowId = db.insert(Tables.DIARYUSER_TABLE, DiaryUserColumns.SURNAME, values);

        if(rowId>0){
            Uri insertUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(insertUri, null);

            return insertUri;
        }else{
            throw new SQLException("Failed to insert row into "+ uri);
        }

    }

    @Override
    public boolean onCreate() {
        try {
            databhelper= new DbHelper(getContext());
            databhelper.createDatabase();
        } catch (Exception e) {

            e.printStackTrace();
        }

        db = databhelper.getReadDatabase();

        return (db==null) ? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String orderBy;
        if(TextUtils.isEmpty(sortOrder))
        {
            orderBy = DiaryUserColumns.SURNAME;
        }else {
            orderBy =sortOrder;
        }

        Cursor c = db.query(Tables.DIARYUSER_TABLE, projection,selection,selectionArgs,null,null,orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int retVal = db.update(Tables.DIARYUSER_TABLE, values, selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return retVal;
    }
}
