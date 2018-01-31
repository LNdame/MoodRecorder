package cct.ansteph.moodrecorder.api;

import android.net.Uri;

/**
 * Created by loicstephan on 2018/01/20.
 */

public class ContentTypes {

    public static final Uri ACTIVITY_CONTENT_URI = Uri.parse("content://cct.ansteph.moodrecorder.contentprovider.activitycontentprovider/moodrecorder");
    public static final Uri DIARYUSER_CONTENT_URI = Uri.parse("content://cct.ansteph.moodrecorder.contentprovider.diaryusercontentprovider/moodrecorder");

    public static final Uri EMOJI_CONTENT_URI = Uri.parse("content://cct.ansteph.moodrecorder.contentprovider.emojicontentprovider/moodrecorder");
    public static final Uri ENTRY_CONTENT_URI = Uri.parse("content://cct.ansteph.moodrecorder.contentprovider.entrycontentprovider/moodrecorder");
    public static final Uri RECORDEDACTIVITY_CONTENT_URI = Uri.parse("content://cct.ansteph.moodrecorder.contentprovider.recordedactivitycontentprovider/moodrecorder");


}
