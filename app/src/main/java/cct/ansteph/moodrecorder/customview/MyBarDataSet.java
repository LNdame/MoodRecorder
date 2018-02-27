package cct.ansteph.moodrecorder.customview;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * Created by loicstephan on 2017/07/22.
 */

public class MyBarDataSet extends BarDataSet {


    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {

        if(getEntryForIndex(index).getY()<=49)
        {
            return mColors.get(0);
        }else if(getEntryForIndex(index).getY()>=50 && getEntryForIndex(index).getY()<=74)
        {
            return mColors.get(1);
        }else if(getEntryForIndex(index).getY()>=75) {
            return mColors.get(2);
        }
        else {return mColors.get(0);}





    }
}
