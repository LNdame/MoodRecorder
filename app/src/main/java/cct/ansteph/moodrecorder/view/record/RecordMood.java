package cct.ansteph.moodrecorder.view.record;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.api.ContentTypes;
import cct.ansteph.moodrecorder.api.columns.DiaryUserColumns;
import cct.ansteph.moodrecorder.api.columns.EmojiColumns;
import cct.ansteph.moodrecorder.api.columns.EntryColumns;
import cct.ansteph.moodrecorder.app.EmojiName;
import cct.ansteph.moodrecorder.model.DiaryUser;
import cct.ansteph.moodrecorder.model.Emoji;
import cct.ansteph.moodrecorder.view.export.About;
import cct.ansteph.moodrecorder.view.export.ExportData;
import cct.ansteph.moodrecorder.view.record.datetimepicker.RecordDatePickerFragment;
import cct.ansteph.moodrecorder.view.record.datetimepicker.RecordTimePickerFragment;
import cct.ansteph.moodrecorder.view.stats.CalendarView;
import cct.ansteph.moodrecorder.view.stats.Statistics;

import static cct.ansteph.moodrecorder.app.Constants.LONGDATEDASH;
import static cct.ansteph.moodrecorder.app.Constants.LONGTIMESFORMAT;

public class RecordMood extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String TAG = RecordMood.class.getSimpleName();

    TextView txtGreetings;
    TextView  txtStartDate, txtStartTime;

    ArrayList<Emoji> mEmojiArrayList;

    DiaryUser mUser;

    int mLastInserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_mood);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        txtGreetings = (TextView) findViewById(R.id.txtGreetings);

        setupGreeting();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mEmojiArrayList = retrieveEmojis();

        txtStartDate= (TextView) findViewById(R.id.txtstartdateday);
        txtStartTime= (TextView) findViewById(R.id.txtstartdatetime);


        Date date = new Date();
        String currenttime = new SimpleDateFormat(LONGTIMESFORMAT).format(date);


        String curDate = new SimpleDateFormat(LONGDATEDASH).format(date);
        txtStartDate.setText(curDate);


        txtStartTime.setText(currenttime);

    }



    public void onStartDateClicked(View view)
    {
        DialogFragment nf = new RecordDatePickerFragment();
        nf.show(getSupportFragmentManager(), "Start Date");
    }

    public void onStartTimeClicked(View view)
    {
        DialogFragment nf = new RecordTimePickerFragment();
        nf.show(getSupportFragmentManager(),"Start Time");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private DiaryUser retrieveDiaryUser (){
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(ContentTypes.DIARYUSER_CONTENT_URI, DiaryUserColumns.PROJECTION,null,null,null);
        DiaryUser user = new DiaryUser ();

        if(cursor.moveToFirst()){
            do{

                user.setId(((cursor.getString(0))!=null ? Integer.parseInt(cursor.getString(0)):0) );
                user.setFirstname( (cursor.getString(cursor.getColumnIndex(DiaryUserColumns.FIRSTNAME)))  );
                user.setSurname( (cursor.getString(cursor.getColumnIndex(DiaryUserColumns.SURNAME))) );
               // user.setAge( (cursor.getString(cursor.getColumnIndex(DiaryUserColumns.AGE)))  );
                user.setMotive( (cursor.getString(cursor.getColumnIndex(DiaryUserColumns.MOTIVE))) );

            }while (cursor.moveToNext());

        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }


        return user;
    }


    private ArrayList<Emoji> retrieveEmojis()
    {
        ContentResolver resolver = getContentResolver();

        Cursor  cursor = resolver.query(ContentTypes.EMOJI_CONTENT_URI, EmojiColumns.PROJECTION, null, null,null);
        ArrayList<Emoji> emojis = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                Emoji emoji = new Emoji(
                        ((cursor.getString(0))!=null ? Integer.parseInt(cursor.getString(0)):0),
                        (cursor.getString(cursor.getColumnIndex(EmojiColumns.MOODNAME))),
                        (cursor.getBlob(cursor.getColumnIndex(EmojiColumns.IMAGEBYTE)))
                );

                emojis.add(emoji);

            }while(cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return emojis;
    }

    public void setupGreeting(){
         mUser = retrieveDiaryUser();

        txtGreetings.setText(getString(R.string.greeting ,mUser.getFirstname()));

    }


    public void getEmojiClicked(View view)
    {
        int id  = view.getId();

        Emoji clickedEmoji = new Emoji();

        switch (id)
        {
            case R.id.btnpowerful: clickedEmoji = retrieveEmojibyMoodName(EmojiName.POWERFUL);break;
            case R.id.btnhappy:clickedEmoji = retrieveEmojibyMoodName(EmojiName.HAPPYARTIST) ;break;
            case R.id.btnexcited:clickedEmoji = retrieveEmojibyMoodName(EmojiName.EXCITED) ;break;
            case R.id.btntired:clickedEmoji = retrieveEmojibyMoodName(EmojiName.TIRED) ;break;
            case R.id.btnlonely:clickedEmoji = retrieveEmojibyMoodName(EmojiName.LONELY) ;break;
            case R.id.btnnumb:clickedEmoji = retrieveEmojibyMoodName(EmojiName.NUMB) ;break;
            case R.id.btnirritable:clickedEmoji = retrieveEmojibyMoodName(EmojiName.IRRITABLE) ;break;
            case R.id.btnsad:clickedEmoji = retrieveEmojibyMoodName(EmojiName.SAD) ;break;
            case R.id.btnfrustrated:clickedEmoji = retrieveEmojibyMoodName(EmojiName.FRUSTRATED) ;break;
            case R.id.btnworried:clickedEmoji = retrieveEmojibyMoodName(EmojiName.WORRIED) ;break;
            case R.id.btnfearful:clickedEmoji = retrieveEmojibyMoodName(EmojiName.FEARFUL) ;break;
            case R.id.btnangry:clickedEmoji = retrieveEmojibyMoodName(EmojiName.ANGRY) ;break;

        }



        if(recordMoodEntry(clickedEmoji) ==1){

            int entryID = getLastID();
            Intent i = new Intent(getApplicationContext(), RecordActivity.class);
            i.putExtra("entryID",entryID );
            startActivity(i);
        }


    }



    private Emoji retrieveEmojibyMoodName(String moodname)
    {
        for(Emoji emoji: mEmojiArrayList)
        {
            if(moodname.equals( emoji.getMoodName())){
                return emoji;
            }
        }
        return null;
    }

    public int recordMoodEntry(Emoji clickedEmoji){



        String emoji_id =String.valueOf( clickedEmoji.getId());
        String recordtime = txtStartTime.getText().toString();
       String recorddate = txtStartDate.getText().toString();
        String diary_id = String.valueOf(mUser.getId()) ;


        try {
            ContentValues values = new ContentValues();

            values.put(EntryColumns.EMOJI_ID,emoji_id) ;
            values.put(EntryColumns.RECORDTIME_ID ,recordtime) ;
            values.put(EntryColumns.RECORDDATE_ID,recorddate) ;
            values.put(EntryColumns.DIARYUSER_ID,diary_id) ;

            getContentResolver().insert(ContentTypes.ENTRY_CONTENT_URI, values);


            return 1;


        }catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }

    }


    /**
     * get the last save Audit iD
     */

    public int getLastID(){

        String [] columns  = new String []{EntryColumns._ID};
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContentTypes.ENTRY_CONTENT_URI,columns,null,null, EntryColumns._ID+" DESC LIMIT 1");

        int lastId =0;

        if(cursor !=null && cursor.moveToFirst())
        {
            lastId =(cursor.getString(0))!=null ? Integer.parseInt(cursor.getString(0)):0;
            mLastInserted = lastId;
            //mGlobalRetainer.get_grCurrentAudit().set_id(lastId);
            Log.d(TAG, String.valueOf(lastId) );
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return lastId;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record_mood, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Splash/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_record_mood) {
            // Handle the camera action
        } else if (id == R.id.nav_record_activity) {
            startActivity(new Intent(getApplicationContext(), RecordActivity.class));
        } else if (id == R.id.nav_entry) {
            startActivity(new Intent(getApplicationContext(), Entries.class));
        } else if (id == R.id.nav_statistic) {
            startActivity(new Intent(getApplicationContext(), Statistics.class));
        } else if (id == R.id.nav_calendar) {
            startActivity(new Intent(getApplicationContext(), CalendarView.class));
        } else if (id == R.id.nav_export) {
            startActivity(new Intent(getApplicationContext(), ExportData.class));
        }else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
