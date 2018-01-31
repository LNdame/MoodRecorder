package cct.ansteph.moodrecorder.view.record;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.adapter.EntryRecyclerAdapter;
import cct.ansteph.moodrecorder.api.ContentTypes;
import cct.ansteph.moodrecorder.api.columns.ActivityColumns;
import cct.ansteph.moodrecorder.api.columns.EmojiColumns;
import cct.ansteph.moodrecorder.api.columns.EntryColumns;
import cct.ansteph.moodrecorder.api.columns.RecordedActivtyColumns;
import cct.ansteph.moodrecorder.model.Activity;
import cct.ansteph.moodrecorder.model.Emoji;
import cct.ansteph.moodrecorder.model.Entry;
import cct.ansteph.moodrecorder.view.export.About;
import cct.ansteph.moodrecorder.view.stats.CalendarView;
import cct.ansteph.moodrecorder.view.stats.Statistics;

public class Entries extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView EntryrecyclerView;

    ArrayList<Entry> mEntryList;
    EntryRecyclerAdapter mEntryAdapter;

    //just temporarily holding in memory
    ArrayList<Emoji> mEmojiArrayList;
    ArrayList<Activity> mActiviesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);
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
        mActiviesArrayList = retrieveActivities();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mEntryList = retrieveEntries();//setupList();// new ArrayList<>();

        mEntryAdapter  = new EntryRecyclerAdapter(mEntryList, this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mEntryAdapter);



    }



    ArrayList<Entry> setupList()
    {
        ArrayList<Entry>  entryArrayList = new ArrayList<>();

        entryArrayList.add(new Entry ("Bad","2:00 pm", "Thursday, 23 Jan 2017"));
        entryArrayList.add(new Entry ("Good","2:00 pm", "Friday, 24 Jan 2017"));
        entryArrayList.add(new Entry ("Happy Artist","2:00 pm", "Saturday, 25 Jan 2017"));



        // String duration, String task_date, String start, String end, String project, String description, String realduration, String task_break) {
        return  entryArrayList;
    }




    private ArrayList<Entry> retrieveEntries()
    {
        ContentResolver resolver = getContentResolver();

        Cursor  cursor = resolver.query(ContentTypes.ENTRY_CONTENT_URI, EntryColumns.PROJECTION, null, null,null);
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
                entry.setActivityList(retrieveRecordedActivity(entry.getId()));


                entries.add(entry);

            }while(cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return entries;
    }




    private ArrayList<Activity> retrieveRecordedActivity(int entry_id)
    {
        ContentResolver resolver = getContentResolver();

        /*Cursor cursor = resolver.query(ContentTypes.ORGANISATION_CONTENT_URI, OrganisationColumns.PROJECTION,
                OrganisationColumns.WORK_AREA_ID + "=?",new String[]{String.valueOf(cat.getId())},null);
        */

        Cursor  cursor = resolver.query(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, RecordedActivtyColumns.PROJECTION,
                RecordedActivtyColumns.ENTRY_ID + "=?", new String[]{String.valueOf(entry_id)},null);
        ArrayList<Activity> activities = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                Activity activity = new Activity();

                int activity_id =(cursor.getString(cursor.getColumnIndex(RecordedActivtyColumns.ACTIVITY_ID)))!=null ? Integer.parseInt(cursor.getString(cursor.getColumnIndex(RecordedActivtyColumns.ACTIVITY_ID))):0;
                activity= retrieveActivityById(activity_id);

                activities.add(activity);

            }while(cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return activities;
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
        getMenuInflater().inflate(R.menu.entries, menu);
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
         //   startActivity(new Intent(getApplicationContext(), Entry.class));
        } else if (id == R.id.nav_statistic) {
            startActivity(new Intent(getApplicationContext(), Statistics.class));
        } else if (id == R.id.nav_calendar) {
            startActivity(new Intent(getApplicationContext(), CalendarView.class));
        } else if (id == R.id.nav_export) {

        }else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private ArrayList<Activity> retrieveActivities()
    {
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(ContentTypes.ACTIVITY_CONTENT_URI, ActivityColumns.PROJECTION, null, null,null);
        ArrayList<Activity> activities = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                Activity activity = new Activity(
                        ((cursor.getString(0))!=null ? Integer.parseInt(cursor.getString(0)):0),
                        (cursor.getString(cursor.getColumnIndex(ActivityColumns.NAME))),
                        (cursor.getBlob(cursor.getColumnIndex(ActivityColumns.IMAGEBYTE)))
                );

                activities.add(activity);

            }while(cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return activities;
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


    private Activity retrieveActivityById(int id)
    {
        for(Activity acti: mActiviesArrayList)
        {
            if(id== acti.getId()){
                return acti;
            }
        }
        return null;
    }


}
