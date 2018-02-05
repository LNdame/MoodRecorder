package cct.ansteph.moodrecorder.api.columns;

/**
 * Created by loicstephan on 2018/01/20.
 */

public interface EntryColumns extends DataColumns {

String EMOJI_ID ="emoji_id";
    String RECORDTIME_ID ="recordtime";
    String RECORDDATE_ID ="recorddate";
    String DIARYUSER_ID ="diaryuser_id";
    String ADDED_NOTE ="added_note";


    String[]PROJECTION = new String[]{_ID,EMOJI_ID, RECORDTIME_ID,RECORDDATE_ID,ADDED_NOTE,DIARYUSER_ID};
}
