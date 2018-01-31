package cct.ansteph.moodrecorder.view.record.datetimepicker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import cct.ansteph.moodrecorder.R;

/**
 * Created by loicstephan on 2018/01/25.
 */

public class RecordTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }



    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        TextView txtStartTime = (TextView) getActivity().findViewById(R.id.txtstartdatetime);

        String min ="";
        if (minute<10)
        {
            min = "0"+String.valueOf(minute);
        }else{
            min =String.valueOf(minute);
        }

        String hour = "";
        if(hourOfDay<10)
        {
            hour="0"+String.valueOf(hourOfDay);
        }else{
            hour=String.valueOf(hourOfDay);
        }

        txtStartTime.setText( hour+ ":"+ min+":00");
    }
}
