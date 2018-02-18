package cct.ansteph.moodrecorder.model;

import java.io.Serializable;

/**
 * Created by loicstephan on 2018/01/20.
 */

public class RecordedActivity implements Serializable {

    int id, entryId , activityId ;

    Entry entry;

    Activity activity;




    public RecordedActivity() {
    }



    public RecordedActivity(int id, int entryId, Entry entry, Activity activity) {
        this.id = id;
        this.entryId = entryId;
        this.entry = entry;
        this.activity = activity;

    }


    public RecordedActivity(int id, int entryId, int activityId) {
        this.id = id;
        this.entryId = entryId;
        this.activityId = activityId;
    }


    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


}
