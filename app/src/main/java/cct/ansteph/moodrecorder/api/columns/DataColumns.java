package cct.ansteph.moodrecorder.api.columns;

import android.provider.BaseColumns;

/**
 * Created by loicstephan on 2018/01/20.
 */

public interface DataColumns  extends BaseColumns{

    /** UUID of data. */
    String ID = "uuid";

    /** Sync columns. */
    String USER = "user";
    String DELETED = "deleted";
    String UPDATED = "updated";
}
