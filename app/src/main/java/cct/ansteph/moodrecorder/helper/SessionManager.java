package cct.ansteph.moodrecorder.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import cct.ansteph.moodrecorder.view.record.RecordMood;
import cct.ansteph.moodrecorder.view.welcome.Registration;

/**
 * Created by loicstephan on 2018/01/22.
 */

public class SessionManager {

    SharedPreferences preferences;
    //Editor for shared preferences
    SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "MoodRecordedPref";
    //Context
    Context _context;

    //shared pref mode
    int PRIVATE_MODE = 0;

    private static final String KEY_HAS_REGISTERED= "hasRegistered";

    public SessionManager(Context context) {
        this._context = context;

        preferences=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);

        editor = preferences.edit();

    }

    public void recordRegistration()
    {
        editor.putBoolean(KEY_HAS_REGISTERED, true);

        editor.commit();
    }


    public void checkReg(){

        if(!this.hasRegistered())
        {
           Intent i= new Intent(_context, Registration.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            _context.startActivity(i);
        }else
        {

                Intent i = new Intent(_context, RecordMood.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //Starting Login Activity
                _context.startActivity(i);

        }

    }

    // Get Login State
    public boolean hasRegistered() {
        return preferences.getBoolean(KEY_HAS_REGISTERED,false);
    }

}
