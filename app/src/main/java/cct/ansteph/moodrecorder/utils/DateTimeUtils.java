package cct.ansteph.moodrecorder.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by loicstephan on 2018/02/01.
 */

public class DateTimeUtils {


    //Date constants
    public static final String SHORTWEEKDATEFORMAT = "EEEE, MMM dd, yyyy";

    public static final String LONGDATEDASH = "yyyy-MM-dd";

    public static final String SHORTTIMEFORMAT = "HH:mm";
    public static final String LONGTIMESFORMAT = "HH:mm:ss";



    public static String parseDateTime(String dateString, String originalFormat, String outputFromat){

        SimpleDateFormat formatter = new SimpleDateFormat(originalFormat, Locale.US);
        Date date = null;
        try {
            date = formatter.parse(dateString);

            SimpleDateFormat dateFormat=new SimpleDateFormat(outputFromat, new Locale("US"));

            return dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getRelativeTimeSpan(String dateString, String originalFormat){

        SimpleDateFormat formatter = new SimpleDateFormat(originalFormat, Locale.US);
        Date date = null;
        try {
            date = formatter.parse(dateString);

            return DateUtils.getRelativeTimeSpanString(date.getTime()).toString();

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
