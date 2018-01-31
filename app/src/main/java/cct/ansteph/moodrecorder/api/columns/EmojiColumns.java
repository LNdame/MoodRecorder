package cct.ansteph.moodrecorder.api.columns;

/**
 * Created by loicstephan on 2018/01/20.
 */

public interface EmojiColumns extends DataColumns {

String MOODNAME = "moodname";
    String IMAGEBYTE  = "imagebyte";
    String STATUS = "status";

    String[]PROJECTION = new String[]{_ID,MOODNAME,IMAGEBYTE,STATUS};
}
