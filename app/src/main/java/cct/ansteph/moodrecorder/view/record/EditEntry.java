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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.api.ContentTypes;
import cct.ansteph.moodrecorder.api.columns.ActivityColumns;
import cct.ansteph.moodrecorder.api.columns.EmojiColumns;
import cct.ansteph.moodrecorder.api.columns.EntryColumns;
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
    Emoji mEditedEmoji;
    TextView  txtStartDate, txtStartTime;

    EditText edtAddedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        txtStartDate= (TextView) findViewById(R.id.txtstartdateday);
        txtStartTime= (TextView) findViewById(R.id.txtstartdatetime);
        edtAddedNote =(EditText) findViewById(R.id.edtAddedNote);



        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            mEditedEntry = (Entry)bundle.getSerializable("entry");
            mClickedActivies = mEditedEntry.getActivityList();
            mEditedEmoji  = mEditedEntry.getEmoji();

            //highlight the emoji and the activities selected
            prepareEmoji(mEditedEmoji);
            preparedActivity(mClickedActivies);

            //report the time
            txtStartDate.setText(mEditedEntry.getRecordDate());
            txtStartTime.setText(mEditedEntry.getRecordTime());
            edtAddedNote.setText(mEditedEntry.getAddedNote());

        }else{
            mEditedEntry = new Entry();
        }

        mActivityArrayList = retrieveActivities();
        mEmojiArrayList = retrieveEmojis();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int i= updateEntry(mEditedEntry.getId());
               if(i==1)
                   startActivity(new Intent(getApplicationContext(),Entries.class));

            }
        });



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





    public void recordActivity(ArrayList<Activity> clickedActivity, int entryID)throws SQLException {

        String entry_id = String.valueOf( entryID);


        for(Activity activity:clickedActivity){


            ContentValues values = new ContentValues();

            values.put(RecordedActivtyColumns.ENTRY_ID,entry_id) ;
            values.put(RecordedActivtyColumns.ACTIVITY_ID ,activity.getId()) ;

            getContentResolver().insert(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, values);



        }



    }

    //TO DO:
    //check the status of clicked activities
    //check that deletion occurs

    public void deleteActivities(int entryID)throws SQLException {

        String entry_id = String.valueOf( entryID);

        getContentResolver().delete(ContentTypes.RECORDEDACTIVITY_CONTENT_URI, RecordedActivtyColumns.ENTRY_ID+" =?", new String[]{entry_id});

    }


    public int updateEntry(int entryID)
    {
        String entry_id = String.valueOf( entryID);
        String emoji_id =String.valueOf( mEditedEmoji.getId());
        String recordtime = txtStartTime.getText().toString();
        String recorddate = txtStartDate.getText().toString();
        String note = edtAddedNote.getText().toString();


        try{
             deleteActivities(entryID);

            recordActivity(mClickedActivies,entryID);


            ContentValues values = new ContentValues();

            values.put(EntryColumns.ADDED_NOTE, note );
            values.put(EntryColumns.EMOJI_ID,emoji_id);
            values.put(EntryColumns.RECORDTIME_ID ,recordtime);
            values.put(EntryColumns.RECORDDATE_ID,recorddate) ;

            getContentResolver().update(ContentTypes.ENTRY_CONTENT_URI,values, EntryColumns._ID+" =?", new String[]{entry_id});

            return 1;


        }catch (SQLException se)
        {
            se.printStackTrace();
            return  0;
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
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

        clearEmojiSelection();
        int id  = view.getId();

        Emoji clickedEmoji = new Emoji();

        Button btnClicked = (Button) view;

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

        //Update the edited emoji with the newly selected one even if it is the same
        mEditedEmoji = clickedEmoji;

        //highlight the selected emoji
        btnClicked.setSelected(true);
        btnClicked.setPressed(false);


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



public void prepareEmoji(Emoji emo)
{
    Button btn =new Button(this) ;

    if(emo.getMoodName().equals(EmojiName.POWERFUL)){

        btn =(Button) findViewById(R.id.btnpowerful);
    }else if(emo.getMoodName().equals(EmojiName.HAPPYARTIST)){
        btn =(Button) findViewById(R.id.btnhappy);
    }else if(emo.getMoodName().equals(EmojiName.EXCITED)){
        btn =(Button) findViewById(R.id.btnexcited);

    }else if(emo.getMoodName().equals(EmojiName.TIRED)){
        btn =(Button) findViewById(R.id.btntired);

    }else if(emo.getMoodName().equals(EmojiName.LONELY)){
        btn =(Button) findViewById(R.id.btnlonely);

    }else if(emo.getMoodName().equals(EmojiName.NUMB)){
        btn =(Button) findViewById(R.id.btnnumb);

    }else if(emo.getMoodName().equals(EmojiName.IRRITABLE)){
        btn =(Button) findViewById(R.id.btnirritable);

    }else if(emo.getMoodName().equals(EmojiName.SAD)){
        btn =(Button) findViewById(R.id.btnsad);

    }else if(emo.getMoodName().equals(EmojiName.FRUSTRATED)){
        btn =(Button) findViewById(R.id.btnfrustrated);

    }else if(emo.getMoodName().equals(EmojiName.WORRIED)){
        btn =(Button) findViewById(R.id.btnworried);

    }else if(emo.getMoodName().equals(EmojiName.FEARFUL)){
        btn =(Button) findViewById(R.id.btnfearful);

    }else if(emo.getMoodName().equals(EmojiName.ANGRY)){
        btn =(Button) findViewById(R.id.btnangry);

    }

    btn.setSelected(true);
    btn.setPressed(false);
}


void clearEmojiSelection(){
    ((Button) findViewById(R.id.btnpowerful)).setSelected(false);
    ((Button) findViewById(R.id.btnpowerful)).setPressed(false);

    ((Button) findViewById(R.id.btnhappy)).setSelected(false);
    ((Button) findViewById(R.id.btnhappy)).setPressed(false);


    ((Button) findViewById(R.id.btnexcited)).setSelected(false);
    ((Button) findViewById(R.id.btnexcited)).setPressed(false);

    ((Button) findViewById(R.id.btntired)).setSelected(false);
    ((Button) findViewById(R.id.btntired)).setPressed(false);

    ((Button) findViewById(R.id.btnlonely)).setSelected(false);
    ((Button) findViewById(R.id.btnlonely)).setPressed(false);

    ((Button) findViewById(R.id.btnnumb)).setSelected(false);
    ((Button) findViewById(R.id.btnnumb)).setPressed(false);

    ((Button) findViewById(R.id.btnirritable)).setSelected(false);
    ((Button) findViewById(R.id.btnirritable)).setPressed(false);

    ((Button) findViewById(R.id.btnsad)).setSelected(false);
    ((Button) findViewById(R.id.btnsad)).setPressed(false);

    ((Button) findViewById(R.id.btnfrustrated)).setSelected(false);
    ((Button) findViewById(R.id.btnfrustrated)).setPressed(false);

    ((Button) findViewById(R.id.btnworried)).setSelected(false);
    ((Button) findViewById(R.id.btnworried)).setPressed(false);

    ((Button) findViewById(R.id.btnfearful)).setSelected(false);
    ((Button) findViewById(R.id.btnfearful)).setPressed(false);

    ((Button) findViewById(R.id.btnangry)).setSelected(false);
    ((Button) findViewById(R.id.btnangry)).setPressed(false);
}






}
