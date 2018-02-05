package cct.ansteph.moodrecorder.view.stats;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.customview.RadarMarkerView;
import cct.ansteph.moodrecorder.model.Entry;
import cct.ansteph.moodrecorder.view.export.About;
import cct.ansteph.moodrecorder.view.record.RecordActivity;
import cct.ansteph.moodrecorder.view.record.RecordMood;

public class Statistics extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RadarChart mActiMonthRadarChart,mActiAllRadarChart;
    protected BarChart mMoodMonthChart,mMoodAllChart ;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

         mActiMonthRadarChart = (RadarChart) findViewById(R.id.activityMonthChart);
         mActiAllRadarChart = (RadarChart) findViewById(R.id.activityAllChart);
         mMoodMonthChart = (BarChart) findViewById(R.id.moodMonthyBar);
         mMoodAllChart = (BarChart) findViewById(R.id.moodAllBar);


        generateMonthRadarChart();
        generateMonthBarChart();

        generateAllRadarChart();
        generateAllBarChart();

    }


    /***************************************-----------bar chart-------------------------*****************/

    public void generateMonthBarChart()
    {
        mMoodMonthChart.setDrawBarShadow(false);
        mMoodMonthChart.setDrawValueAboveBar(true);
        mMoodMonthChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mMoodMonthChart.setMaxVisibleValueCount(60);

        //scaling can now only be done on x- and y-axis separately
        mMoodMonthChart.setPinchZoom(false);

        final ArrayList<String> xLabelCat = new ArrayList<>();
        xLabelCat.add("powerful");
        xLabelCat.add("happy");
        xLabelCat.add("excited");
        xLabelCat.add("tired");
        xLabelCat.add("lonely");
        xLabelCat.add("numb");
        xLabelCat.add("irritable ");
        xLabelCat.add("sad");
        xLabelCat.add("frustrated");
        xLabelCat.add("worried");
        xLabelCat.add("fearful");
        xLabelCat.add("angry");

        mMoodMonthChart.setDrawGridBackground(false);


      //  IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);
/*
        XAxis xAxis = mMoodMonthChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7); */
        //xAxis.setValueFormatter(xAxisFormatter);

     //   IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mMoodMonthChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
     //   leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

      /*  YAxis rightAxis = mMoodMonthChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
      //  rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
*/
       Legend l = mMoodMonthChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

     //   XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
       // mv.setChartView(mChart); // For bounds control
        //mChart.setMarker(mv); // Set the marker to the chart
        setMonthChartData(12, 30);

    }



    private void setMonthChartData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        Drawable[] icons = new Drawable[]{
                getResources().getDrawable(R.drawable.mood_powerful),
                getResources().getDrawable(R.drawable.mood_happy),
                getResources().getDrawable(R.drawable.mood_excited),
                getResources().getDrawable(R.drawable.mood_tired),
                getResources().getDrawable(R.drawable.mood_lonely),
                getResources().getDrawable(R.drawable.mood_numb),
                getResources().getDrawable(R.drawable.mood_irritable),
                getResources().getDrawable(R.drawable.mood_sadness),
                getResources().getDrawable(R.drawable.mood_frustrated),
                getResources().getDrawable(R.drawable.mood_worried),
                getResources().getDrawable(R.drawable.mood_fearfull),
                getResources().getDrawable(R.drawable.mood_angry)

        };


        for (int i = 0; i <  count ; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);

            yVals1.add(new BarEntry(i, val, icons[i] ));

          /*  yVals1.add(new BarEntry(i, val, icons[i] getResources().getDrawable(R.mipmap.ic_mood_angry)));
           if (Math.random() * 100 < 25) {
              ;
            } else {
                yVals1.add(new BarEntry(i, val));
            }*/
        }

        BarDataSet set1;

        if (mMoodMonthChart.getData() != null && mMoodMonthChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mMoodMonthChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mMoodMonthChart.getData().notifyDataChanged();
            mMoodMonthChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");

            set1.setDrawIcons(true);

            set1.setColors(ColorTemplate.LIBERTY_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mMoodMonthChart.setData(data);
        }
    }


    public void generateAllBarChart()
    {
        mMoodAllChart.setDrawBarShadow(false);
        mMoodAllChart.setDrawValueAboveBar(true);
        mMoodAllChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mMoodAllChart.setMaxVisibleValueCount(60);

        //scaling can now only be done on x- and y-axis separately
        mMoodAllChart.setPinchZoom(false);

        final ArrayList<String> xLabelCat = new ArrayList<>();
        xLabelCat.add("powerful");
        xLabelCat.add("happy");
        xLabelCat.add("excited");
        xLabelCat.add("tired");
        xLabelCat.add("lonely");
        xLabelCat.add("numb");
        xLabelCat.add("irritable ");
        xLabelCat.add("sad");
        xLabelCat.add("frustrated");
        xLabelCat.add("worried");
        xLabelCat.add("fearful");
        xLabelCat.add("angry");

        mMoodAllChart.setDrawGridBackground(false);

        //  IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);
/*
        XAxis xAxis = mMoodMonthChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7); */
        //xAxis.setValueFormatter(xAxisFormatter);

        //   IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mMoodAllChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        //   leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

      /*  YAxis rightAxis = mMoodMonthChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
      //  rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
*/
        Legend l = mMoodAllChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        //   XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        // mv.setChartView(mChart); // For bounds control
        //mChart.setMarker(mv); // Set the marker to the chart
        setAllChartData(12, 30);

    }


    private void setAllChartData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        Drawable[] icons = new Drawable[]{
                getResources().getDrawable(R.drawable.mood_powerful),
                getResources().getDrawable(R.drawable.mood_happy),
                getResources().getDrawable(R.drawable.mood_excited),
                getResources().getDrawable(R.drawable.mood_tired),
                getResources().getDrawable(R.drawable.mood_lonely),
                getResources().getDrawable(R.drawable.mood_numb),
                getResources().getDrawable(R.drawable.mood_irritable),
                getResources().getDrawable(R.drawable.mood_sadness),
                getResources().getDrawable(R.drawable.mood_frustrated),
                getResources().getDrawable(R.drawable.mood_worried),
                getResources().getDrawable(R.drawable.mood_fearfull),
                getResources().getDrawable(R.drawable.mood_angry)

        };


        for (int i = 0; i <  count ; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);

            yVals1.add(new BarEntry(i, val, icons[i] ));

          /*  yVals1.add(new BarEntry(i, val, icons[i] getResources().getDrawable(R.mipmap.ic_mood_angry)));
           if (Math.random() * 100 < 25) {
              ;
            } else {
                yVals1.add(new BarEntry(i, val));
            }*/
        }

        BarDataSet set1;

        if (mMoodAllChart.getData() != null && mMoodAllChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mMoodAllChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mMoodAllChart.getData().notifyDataChanged();
            mMoodAllChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");

            set1.setDrawIcons(true);

            set1.setColors(ColorTemplate.LIBERTY_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mMoodAllChart.setData(data);
        }
    }


    /***************************************-----------End bar chart-------------------------*****************/


    /***************************************-----------Radar chart-------------------------*****************/



    public void generateMonthRadarChart()
    {

        mActiMonthRadarChart.setBackgroundColor(Color.rgb(241, 245, 248));
        mActiMonthRadarChart.getDescription().setEnabled(false);

        mActiMonthRadarChart.setWebLineWidth(1f);
        mActiMonthRadarChart.setWebColor(Color.LTGRAY);
        mActiMonthRadarChart.setWebLineWidthInner(0.5f);
        mActiMonthRadarChart.setWebColorInner(Color.LTGRAY);
        mActiMonthRadarChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv  = new RadarMarkerView (this, R.layout.radar_markerview);
        mv.setChartView(mActiMonthRadarChart);// For bounds control
        mActiMonthRadarChart.setMarker(mv); //set teh marker chart


        //setradarData
        setRadarMonthData( );

        mActiMonthRadarChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);


        XAxis xAxis = mActiMonthRadarChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"Work", "Relax", "Friends", "Date",
                    "Sport","Party" , "Movies" , "Reading",
                    "Shopping","Travel","Food","Cleaning"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = mActiMonthRadarChart.getYAxis();
        yAxis.setTypeface(mTfLight);
        yAxis.setLabelCount(8, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = mActiMonthRadarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTypeface(mTfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);

    }



    private void setRadarMonthData( ) {
        float mult = 80;
        float min = 20;
        int cnt = 12;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

//this will the actual value to be displayed
        float [] percentage = new float[]{
        };

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) 1;
            entries1.add(new RadarEntry(val1));

            float val2 =  (float) (Math.random() * mult) + min; //percentage[i];//
            entries2.add(new RadarEntry(val2));
        }


        RadarDataSet set1 = new RadarDataSet(entries1, "Last month");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "This month");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mActiMonthRadarChart.setData(data);
        mActiMonthRadarChart.invalidate();

    }





    public void generateAllRadarChart()
    {

        mActiAllRadarChart.setBackgroundColor(Color.rgb(241, 245, 248));
        mActiAllRadarChart.getDescription().setEnabled(false);

        mActiAllRadarChart.setWebLineWidth(1f);
        mActiAllRadarChart.setWebColor(Color.LTGRAY);
        mActiAllRadarChart.setWebLineWidthInner(0.5f);
        mActiAllRadarChart.setWebColorInner(Color.LTGRAY);
        mActiAllRadarChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv  = new RadarMarkerView (this, R.layout.radar_markerview);
        mv.setChartView(mActiAllRadarChart);// For bounds control
        mActiAllRadarChart.setMarker(mv); //set teh marker chart


        //setradarData
        setRadarAllData( );

        mActiAllRadarChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);


        XAxis xAxis = mActiAllRadarChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"Work", "Relax", "Friends", "Date",
                    "Sport","Party" , "Movies" , "Reading",
                    "Shopping","Travel","Food","Cleaning"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = mActiAllRadarChart.getYAxis();
        yAxis.setTypeface(mTfLight);
        yAxis.setLabelCount(8, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = mActiAllRadarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTypeface(mTfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);

    }



    private void setRadarAllData( ) {
        float mult = 80;
        float min = 20;
        int cnt = 12;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

//this will the actual value to be displayed
        float [] percentage = new float[]{
        };

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) 1;
            entries1.add(new RadarEntry(val1));

            float val2 =  (float) (Math.random() * mult) + min; //percentage[i];//
            entries2.add(new RadarEntry(val2));
        }


        RadarDataSet set1 = new RadarDataSet(entries1, "Last month");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "This month");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mActiAllRadarChart.setData(data);
        mActiAllRadarChart.invalidate();

    }







    /***************************************-----------End Radar chart-------------------------*****************/








    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Splash/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_record_mood) {
            startActivity(new Intent(getApplicationContext(), RecordMood.class));
        } else if (id == R.id.nav_record_activity) {
            startActivity(new Intent(getApplicationContext(), RecordActivity.class));
        } else if (id == R.id.nav_entry) {
            startActivity(new Intent(getApplicationContext(), Entry.class));
        } else if (id == R.id.nav_statistic) {
          //  startActivity(new Intent(getApplicationContext(), Statistics.class));
        } else if (id == R.id.nav_calendar) {
            startActivity(new Intent(getApplicationContext(), CalendarView.class));
        } else if (id == R.id.nav_export) {

        }else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
