package cct.ansteph.moodrecorder.helper;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by loicstephan on 2018/01/20.
 */

public class DbHelper extends SQLiteOpenHelper implements BaseColumns {

    public static  String DATABASE_NAME = "moodrecorder.db";//moodrecorder.sqlite

    public static  String DB_PATH= "/data/data/cct.ansteph.moodrecorder/databases/";

    private final Context context;

    private static SQLiteDatabase moodrecorderdb;

    public DbHelper(Context context)
    {
        super(context,DATABASE_NAME, null,1 );
        this.context= context;
    }


    ///check that the database is there
    public boolean checkDatabase()
    {
        SQLiteDatabase db = null;
        try{
            String mypath = DB_PATH +DATABASE_NAME;
            db= SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e)
        {
            //too bad no db add a toast here
        }
        if(db!=null)
        {
            db.close();
        }

        return db!=null? true : false;
    }

    public void createDatabase() throws IOException
    {
        boolean dbExist = checkDatabase();

        if(!dbExist)
        {

            //empty db detected create new one and overwrite
            this.getReadableDatabase();
            try{
                copyDatabase();
            }catch(IOException e)
            {
                throw new Error ("Error copying the database");}
        }


    }

    private void copyDatabase()throws IOException
    {
        //open your local db input
        InputStream inputSt = context.getAssets().open(DATABASE_NAME);

        //path to create an empty db
        String outFileName = DB_PATH+DATABASE_NAME;

        OutputStream outputSt = new FileOutputStream(outFileName);

        //tranfers bytes  from inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while((length= inputSt.read(buffer))>0)
        {
            outputSt.write(buffer,0,length);

        }
        //close the stream
        outputSt.flush();
        outputSt.close();
        inputSt.close();
    }


    public static void openDatabase () throws SQLException
    {
        //open db
        String path = DB_PATH+DATABASE_NAME;
        moodrecorderdb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public SQLiteDatabase getReadDatabase()
    {
        //open db
        String path = DB_PATH+DATABASE_NAME;
        moodrecorderdb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return moodrecorderdb;
    }

    @Override
    public synchronized void close()
    {
        if(moodrecorderdb !=null)
            moodrecorderdb.close();

        super.close();
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /*********************** Escape string for single quotes (Insert,Update)************/

    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace("'", "''");
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }
}
