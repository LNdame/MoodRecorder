package cct.ansteph.moodrecorder.api.columns;

/**
 * Created by loicstephan on 2018/01/20.
 */

public interface RecordedActivtyColumns extends DataColumns {

String ENTRY_ID = "entry_id";
    String ACTIVITY_ID = "activity_id";
String TIME_CREATED = "time_created";

    String[]PROJECTION = new String[]{_ID,ENTRY_ID,ACTIVITY_ID};
}
