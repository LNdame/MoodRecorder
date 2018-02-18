package cct.ansteph.moodrecorder.view.stats;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.badoualy.datepicker.MonthView;

import java.util.ArrayList;
import java.util.Calendar;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.adapter.EntryRecyclerAdapter;
import cct.ansteph.moodrecorder.api.ContentTypes;
import cct.ansteph.moodrecorder.api.columns.EmojiColumns;
import cct.ansteph.moodrecorder.api.columns.EntryColumns;
import cct.ansteph.moodrecorder.app.EmojiName;
import cct.ansteph.moodrecorder.model.Activity;
import cct.ansteph.moodrecorder.model.Emoji;
import cct.ansteph.moodrecorder.model.Entry;
import cct.ansteph.moodrecorder.view.export.About;
import cct.ansteph.moodrecorder.view.record.Entries;
import cct.ansteph.moodrecorder.view.record.RecordActivity;
import cct.ansteph.moodrecorder.view.record.RecordMood;

public class CalendarView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private OneCalendarView calendarView;

    private DatePickerTimeline moodTimeline;

    ArrayList<Entry> mEntryList;


    //just temporarily holding in memory
    ArrayList<Emoji> mEmojiArrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mEmojiArrayList = retrieveEmojis();

        moodTimeline = (DatePickerTimeline)findViewById(R.id.timeline);

        moodTimeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
            @Override
            public CharSequence getLabel(Calendar calendar, int index) {
                return Integer.toString(calendar.get(Calendar.MONTH)+1)+"/"+ (calendar.get(Calendar.YEAR)%2000);
            }
        });

        moodTimeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {

                String date=String.valueOf(year)+ " "+ String.valueOf(month)+" "+ String.valueOf(day);

                String monthcon = (month<10)? "0"+String.valueOf(month+1):String.valueOf(month+1);
                String daycon =  (day<10)? "0"+String.valueOf(day):String.valueOf(day);

                String startDate = String.valueOf(year)+ "-"+ monthcon+"-"+ daycon;
                String endDate = String.valueOf(year)+ "-"+ String.valueOf(month)+"-"+ 28;

                ArrayList<Entry>entryArrayList = retrieveEntries(startDate, endDate);
                moodCountAnalysis(entryArrayList);
               // Toast.makeText(getApplicationContext(),startDate , Toast.LENGTH_LONG).show();
            }
        });

        Calendar rightNow = Calendar.getInstance();

        moodTimeline.setFirstVisibleDate(rightNow.get(Calendar.YEAR)-1, rightNow.get(Calendar.MONTH)+0, rightNow.get(Calendar.DAY_OF_MONTH)+0);

        moodTimeline.setSelectedDate(rightNow.get(Calendar.YEAR)+0, rightNow.get(Calendar.MONTH)+0, rightNow.get(Calendar.DAY_OF_MONTH)+0);
        moodTimeline.setLastVisibleDate(rightNow.get(Calendar.YEAR)+2, rightNow.get(Calendar.MONTH)+1, rightNow.get(Calendar.DAY_OF_MONTH)+0);

        /* one calendarView related

        calendarView = (OneCalendarView) findViewById(R.id.oneCalendar);

        //los metodos son obigatorios
        //calendarView.setOnCalendarChangeListener(params)
        //setOneCalendarClickListener(params)

        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {
            @Override
            public void dateOnClick(Day day, int position) {

            }

            @Override
            public void dateOnLongClick(Day day, int position) {

            }
        });


        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {
            @Override
            public void prevMonth() {

            }

            @Override
            public void nextMonth() {

            }
        });
*/
    }


    private ArrayList<Entry> retrieveEntries(String startDate , String endDate)
    {
        ContentResolver resolver = getContentResolver();

        Cursor  cursor = resolver.query(ContentTypes.ENTRY_CONTENT_URI, EntryColumns.PROJECTION,
                EntryColumns.RECORDDATE_ID +">=? and " + EntryColumns.RECORDDATE_ID+"<=?", new String[]{startDate, endDate},null);
        ArrayList<Entry> entries = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                Entry entry = new Entry();

                entry.setId((cursor.getString(0))!=null ? Integer.parseInt(cursor.getString(0)):0);
                int emojiId =(cursor.getString(cursor.getColumnIndex(EntryColumns.EMOJI_ID)))!=null ? Integer.parseInt(cursor.getString(cursor.getColumnIndex(EntryColumns.EMOJI_ID))):0;
                entry.setEmoji(retrieveEmojiById(emojiId));
                entry.setRecordDate(cursor.getString(cursor.getColumnIndex(EntryColumns.RECORDDATE_ID)));
                entry.setRecordTime(cursor.getString(cursor.getColumnIndex(EntryColumns.RECORDTIME_ID)));


                //retrieve the activities
               // entry.setActivityList(retrieveRecordedActivity(entry.getId()));


                entries.add(entry);

            }while(cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return entries;
    }


    private void moodCountAnalysis(ArrayList<Entry> entries)
    {
        int powerCount = 0,happyCount=0, excitedCount =0;
        int lonelyCount= 0 , tiredCount= 0, numbCount= 0;
        int irritableCount= 0, sadCount= 0, frustratedCount= 0;
        int worriedCount= 0, fearfulCount= 0, angryCount= 0;

        for(Entry ent: entries)
        {
            if(ent.getEmoji().getMoodName().equals(EmojiName.ANGRY)){
                angryCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.EXCITED)){
                excitedCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.FEARFUL)){
                fearfulCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.FRUSTRATED)){
                frustratedCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.HAPPYARTIST)){
                happyCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.IRRITABLE)){
                irritableCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.LONELY)){
                lonelyCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.TIRED)){
                tiredCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.POWERFUL)){
                powerCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.NUMB)){
                numbCount++;

            }else if (ent.getEmoji().getMoodName().equals(EmojiName.WORRIED)){
                worriedCount++;
            }else if (ent.getEmoji().getMoodName().equals(EmojiName.SAD)){
                sadCount++;
            }
        }



        ((Button)findViewById(R.id.btnCalPowerful)).setText(String.valueOf(powerCount));
        ((Button)findViewById(R.id.btnCalHappy)).setText(String.valueOf(happyCount));
        ((Button)findViewById(R.id.btnCalExcited)).setText(String.valueOf(excitedCount));

        ((Button)findViewById(R.id.btnCalLonely)).setText(String.valueOf(lonelyCount));
        ((Button)findViewById(R.id.btnCalTired)).setText(String.valueOf(tiredCount));
        ((Button)findViewById(R.id.btnCalNumb)).setText(String.valueOf(numbCount));


        ((Button)findViewById(R.id.btnCalIrritable)).setText(String.valueOf(irritableCount));
        ((Button)findViewById(R.id.btnCalSad)).setText(String.valueOf(sadCount));
        ((Button)findViewById(R.id.btnCalFrustrated)).setText(String.valueOf(frustratedCount));

        ((Button)findViewById(R.id.btnCalWorried)).setText(String.valueOf(worriedCount));
        ((Button)findViewById(R.id.btnCalFearful)).setText(String.valueOf(fearfulCount));
        ((Button)findViewById(R.id.btnCalAngry)).setText(String.valueOf(angryCount));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_view, menu);
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
            startActivity(new Intent(getApplicationContext(), RecordMood.class));
        } else if (id == R.id.nav_record_activity) {
            startActivity(new Intent(getApplicationContext(), RecordActivity.class));
        } else if (id == R.id.nav_entry) {
            startActivity(new Intent(getApplicationContext(), Entry.class));
        } else if (id == R.id.nav_statistic) {
             startActivity(new Intent(getApplicationContext(), Statistics.class));
        } else if (id == R.id.nav_calendar) {
           // startActivity(new Intent(getApplicationContext(), cct.ansteph.moodrecorder.view.record.CalendarView.class));
        } else if (id == R.id.nav_export) {

        }else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


    private Emoji retrieveEmojiById(int id)
    {
        for(Emoji emoji: mEmojiArrayList)
        {
            if(id == emoji.getId()){
                return emoji;
            }
        }
        return null;
    }

}
