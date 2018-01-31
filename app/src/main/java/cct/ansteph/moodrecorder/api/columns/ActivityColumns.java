package cct.ansteph.moodrecorder.api.columns;

/**
 * Created by loicstephan on 2018/01/20.
 */

public interface ActivityColumns extends DataColumns {

    String NAME  = "name";
    String IMAGEBYTE  = "imagebyte";
    String STATUS = "status";

    String[]PROJECTION = new String[]{_ID, NAME,IMAGEBYTE, STATUS};
}
