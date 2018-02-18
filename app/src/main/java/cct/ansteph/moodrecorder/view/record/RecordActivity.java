package cct.ansteph.moodrecorder.view.record;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.api.ContentTypes;
import cct.ansteph.moodrecorder.api.columns.ActivityColumns;
import cct.ansteph.moodrecorder.api.columns.EntryColumns;
import cct.ansteph.moodrecorder.api.columns.RecordedActivtyColumns;
import cct.ansteph.moodrecorder.app.ActivityName;
import cct.ansteph.moodrecorder.model.Activity;
import cct.ansteph.moodrecorder.view.export.About;
import cct.ansteph.moodrecorder.view.stats.*;

public class RecordActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Activity> mActivityArrayList;

    int mSentEntry_Id;
    ArrayList<Activity> mClickedActivies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mClickedActivies.size()>0){

                    try{
                        recordActivity(mClickedActivies);
                        String addedNote = ((EditText)findViewById(R.id.edtAddedNote) ).getText().toString();
                        recordAddedNote(addedNote);
                        Snackbar.make(view, "Recorded", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        startActivity(new Intent(getApplicationContext(),Entries.class));
                    }catch (SQLException e)
                    {
                        e.printStackTrace();
                    }

                }else{
                    Snackbar.make(view, "Please tell us at least one thing you did today.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            mSentEntry_Id = bundle.getInt("entryID");
           // Toast.makeText(this, String.valueOf(mSentEntry_Id), Toast.LENGTH_LONG).show();
        }

        mActivityArrayList = retrieveActivities();
        mClickedActivies = new ArrayList<>();
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






    public void getActivitiesClicked(View view)
    {
        int id  = view.getId();

        Button btnClicked = (Button) view;

        //Change the state

        String name="";

        Activity activity = new Activity();

        switch (id)
        {
            case R.id.btnActiWork: activity = retrieveActivitybyName(ActivityName.WORK)  ;break;
            case R.id.btnActiRelax:
                activity = retrieveActivitybyName(ActivityName.RELAX) ;break;
            case R.id.btnActiFriends:
                activity = retrieveActivitybyName(ActivityName.FRIENDS) ;break;
            case R.id.btnActiDate:
                activity = retrieveActivitybyName(ActivityName.DATE) ;break;
            case R.id.btnActiSport:
                activity = retrieveActivitybyName(ActivityName.SPORT) ;break;
            case R.id.btnActiParty:
                activity = retrieveActivitybyName(ActivityName.PARTY);break;
            case R.id.btnActiMovies:
                activity = retrieveActivitybyName(ActivityName.MOVIES) ;break;
            case R.id.btnActiReading:
                activity = retrieveActivitybyName(ActivityName.READING) ;break;
            case R.id.btnActiShopping:
                activity = retrieveActivitybyName(ActivityName.SHOPPING) ;break;
            case R.id.btnActiTravel:
                activity = retrieveActivitybyName(ActivityName.TRAVEL) ;break;
            case R.id.btnActiFood:
                activity = retrieveActivitybyName(ActivityName.FOOD) ;break;
            case R.id.btnActiCleaning:
                activity = retrieveActivitybyName(ActivityName.CLEANING);break;

        }


        if(!mClickedActivies.contains(activity))
        {
            mClickedActivies.add(activity);
            btnClicked.setSelected(true);
            btnClicked.setPressed(false);
           // Log.d("RecordActivity",String.valueOf(mClickedActivies.size()));
           // Toast.makeText(this, mClickedActivies.get(mClickedActivies.size()-1).getActivityName() +" added", Toast.LENGTH_SHORT).show();
        }else
        {
            try{
                mClickedActivies.remove(activity);
                btnClicked.setSelected(false);
                btnClicked.setPressed(false);
               // Toast.makeText(this, activity.getActivityName() +" removed", Toast.LENGTH_SHORT).show();
              //  Log.d("RecordActivity",String.valueOf(mClickedActivies.size()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }



     //Toast.makeText(this, mClickedActivies.get(mClickedActivies.size()-1).getActivityName(), Toast.LENGTH_SHORT).show();


    }

    private Activity retrieveActivitybyName(String actname)
    {
        for(Activity activity: mActivityArrayList)
        {
            if(actname.equals( activity.getActivityName())){
                return activity;
            }
        }
        return null;
    }


    public void recordActivity(ArrayList<Activity> clickedActivity)throws SQLException {



        String entry_id = String.valueOf( mSentEntry_Id);


        for(Activity activity:clickedActivity){


                ContentValues values = new ContentValues();

                values.put(RecordedActivtyColumns.ENTRY_ID,mSentEntry_Id) ;
                values.put(RecordedActivtyColumns.ACTIVITY_ID ,activity.getId()) ;

                getContentResolver().insert(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, values);



        }



    }



    public void recordAddedNote(String note){
        String entry_id = String.valueOf( mSentEntry_Id);

        try{
            ContentValues values = new ContentValues();
            values.put(EntryColumns.ADDED_NOTE, note );

            getContentResolver().update(ContentTypes.ENTRY_CONTENT_URI,values, EntryColumns._ID+" =?", new String[]{entry_id});


        }catch (SQLException se)
        {
            se.printStackTrace();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void changeButtonBackground(View view)
    {
        Button btnView =  (Button) view;
       // btnYes.setBackgroundResource(R.drawable.button_yes_select);
        btnView.setBackgroundResource(R.drawable.selected_background);
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
        getMenuInflater().inflate(R.menu.record, menu);
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

        } else if (id == R.id.nav_entry) {
            startActivity(new Intent(getApplicationContext(), Entries.class));
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
}
