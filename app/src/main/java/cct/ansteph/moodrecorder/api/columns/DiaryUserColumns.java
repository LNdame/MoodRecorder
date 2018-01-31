package cct.ansteph.moodrecorder.api.columns;

/**
 * Created by loicstephan on 2018/01/20.
 */

public interface DiaryUserColumns extends DataColumns {

    String FIRSTNAME  = "firstname";
    String SURNAME  = "surname";
    String AGE = "age";
    String MOTIVE = "motive";


    String[]PROJECTION = new String[]{_ID,FIRSTNAME,SURNAME,AGE,MOTIVE};
}
