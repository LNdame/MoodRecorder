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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.api.ContentTypes;
import cct.ansteph.moodrecorder.api.columns.ActivityColumns;
import cct.ansteph.moodrecorder.api.columns.EmojiColumns;
import cct.ansteph.moodrecorder.api.columns.RecordedActivtyColumns;
import cct.ansteph.moodrecorder.app.ActivityName;
import cct.ansteph.moodrecorder.app.EmojiName;
import cct.ansteph.moodrecorder.model.Activity;
import cct.ansteph.moodrecorder.model.Emoji;
import cct.ansteph.moodrecorder.model.Entry;
import cct.ansteph.moodrecorder.view.record.datetimepicker.RecordDatePickerFragment;
import cct.ansteph.moodrecorder.view.record.datetimepicker.RecordTimePickerFragment;

public class EditEntry extends AppCompatActivity {

    ArrayList<Activity> mActivityArrayList;

    ArrayList<Activity> mClickedActivies;
    ArrayList<Emoji> mEmojiArrayList;

    Entry mEditedEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);
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

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            mEditedEntry = (Entry)bundle.getSerializable("entry");
            mClickedActivies = mEditedEntry.getActivityList();

            preparedActivity(mClickedActivies);
        }else{
            mEditedEntry = new Entry();
        }

        mActivityArrayList = retrieveActivities();
        mEmojiArrayList = retrieveEmojis();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void onStartDateClicked(View view)
    {
        DialogFragment nf = new RecordDatePickerFragment();
        nf.show(getSupportFragmentManager(), "Entry Date");
    }

    public void onStartTimeClicked(View view)
    {
        DialogFragment nf = new RecordTimePickerFragment();
        nf.show(getSupportFragmentManager(),"Entry Time");
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





    public void recordActivity(ArrayList<Activity> clickedActivity)throws SQLException {


        int id =0;

        String entry_id = String.valueOf( id);


        for(Activity activity:clickedActivity){


            ContentValues values = new ContentValues();

            values.put(RecordedActivtyColumns.ENTRY_ID,id) ;
            values.put(RecordedActivtyColumns.ACTIVITY_ID ,activity.getId()) ;

            getContentResolver().insert(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, values);



        }



    }


    public void getActivitiesClickedEdt(View view)
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


    public void getEmojiClickedEdt(View view)
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



       /* if(recordMoodEntry(clickedEmoji) ==1){

            int entryID = getLastID();
            Intent i = new Intent(getApplicationContext(), RecordActivity.class);
            i.putExtra("entryID",entryID );
            startActivity(i);
        }*/


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


    public void preparedActivity(ArrayList<Activity> activities)
    {

        for(Activity act: activities)
        {
            Button btn =new Button(this) ;

            if(act.getActivityName().equals(ActivityName.WORK)){

               btn =(Button) findViewById(R.id.btnActiWork);
            }else if(act.getActivityName().equals(ActivityName.RELAX)){
                btn =(Button) findViewById(R.id.btnActiRelax);
            }else if(act.getActivityName().equals(ActivityName.FRIENDS)){
                btn =(Button) findViewById(R.id.btnActiFriends);

            }else if(act.getActivityName().equals(ActivityName.DATE)){
                btn =(Button) findViewById(R.id.btnActiDate);

            }else if(act.getActivityName().equals(ActivityName.SPORT)){
                btn =(Button) findViewById(R.id.btnActiSport);

            }else if(act.getActivityName().equals(ActivityName.PARTY)){
                btn =(Button) findViewById(R.id.btnActiParty);

            }else if(act.getActivityName().equals(ActivityName.MOVIES)){
                btn =(Button) findViewById(R.id.btnActiMovies);

            }else if(act.getActivityName().equals(ActivityName.READING)){
                btn =(Button) findViewById(R.id.btnActiReading);

            }else if(act.getActivityName().equals(ActivityName.SHOPPING)){
                btn =(Button) findViewById(R.id.btnActiShopping);

            }else if(act.getActivityName().equals(ActivityName.TRAVEL)){
                btn =(Button) findViewById(R.id.btnActiTravel);

            }else if(act.getActivityName().equals(ActivityName.FOOD)){
                btn =(Button) findViewById(R.id.btnActiFood);

            }else if(act.getActivityName().equals(ActivityName.CLEANING)){
                btn =(Button) findViewById(R.id.btnActiCleaning);

            }

            btn.setSelected(true);
            btn.setPressed(false);
        }


    }










}
