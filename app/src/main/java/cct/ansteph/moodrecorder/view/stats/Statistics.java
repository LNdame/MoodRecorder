package cct.ansteph.moodrecorder.view.stats;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.api.ContentTypes;
import cct.ansteph.moodrecorder.api.columns.DiaryUserColumns;
import cct.ansteph.moodrecorder.customview.PdfCustFooter;
import cct.ansteph.moodrecorder.customview.RadarMarkerView;
import cct.ansteph.moodrecorder.model.DiaryUser;
import cct.ansteph.moodrecorder.model.Entry;
import cct.ansteph.moodrecorder.view.export.About;
import cct.ansteph.moodrecorder.view.export.ExportData;
import cct.ansteph.moodrecorder.view.record.RecordActivity;
import cct.ansteph.moodrecorder.view.record.RecordMood;

public class Statistics extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static  String TAG  = Statistics.class.getSimpleName();

    private RadarChart mActiMonthRadarChart,mActiAllRadarChart;
    protected BarChart mMoodMonthChart,mMoodAllChart ;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    DiaryUser dUser;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    String mPDFFilePath;
    public static final String KEY_FILE_PATH = "filepath";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermission();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPreview();
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

        dUser = retrieveDiaryUser();

    }



    private DiaryUser retrieveDiaryUser (){
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(ContentTypes.DIARYUSER_CONTENT_URI, DiaryUserColumns.PROJECTION,null,null,null);
        DiaryUser user = new DiaryUser ();

        if(cursor.moveToFirst()){
            do{

                user.setId(((cursor.getString(0))!=null ? Integer.parseInt(cursor.getString(0)):0) );
                user.setFirstname( (cursor.getString(cursor.getColumnIndex(DiaryUserColumns.FIRSTNAME)))  );
                user.setSurname( (cursor.getString(cursor.getColumnIndex(DiaryUserColumns.SURNAME))) );
                // user.setAge( (cursor.getString(cursor.getColumnIndex(DiaryUserColumns.AGE)))  );
                user.setMotive( (cursor.getString(cursor.getColumnIndex(DiaryUserColumns.MOTIVE))) );

            }while (cursor.moveToNext());

        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }


        return user;
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
        setMonthChartData(12, 10);

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
        setAllChartData(12, 20);

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


    private void requestPermission()
    {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
        }
    }


    /**
     * Listener for response to user permission request
     *
     * @param requestCode  Check that permission request code matches
     * @param permissions  Permissions that requested
     * @param grantResults Whether permissions granted
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }



       /* if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission " +permissions[0]+ " was " +grantResults[0]);
        }*/
    }


    /***************************************-----------Statistics Report-------------------------*****************/

    private void viewPreview()
    {
        try {
            createPDF();

            Intent i = new Intent(getApplicationContext(), ExportData.class);
            i.putExtra(KEY_FILE_PATH, mPDFFilePath);
            startActivity(i);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public byte [] bitmaptoByte (Bitmap b)
    {
        // int bytes = b.getByteCount();
        //ByteBuffer buffer = ByteBuffer.allocate(bytes);

        //  b.copyPixelsToBuffer(buffer);
        //byte[] array = buffer.array();

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG,0,blob);


        return  blob.toByteArray();
    }


    public void createPDF () throws FileNotFoundException, DocumentException
    {



        ///step 1 create the file
        File pdfStatRepfolder = new File (Environment.getExternalStorageDirectory(), "CUMStatReport");

        if(!pdfStatRepfolder.exists())   //this what should be used
        {
            pdfStatRepfolder.mkdir();
            Log.i(TAG, "PDF 2 directory created");
        }

        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File newdoc = new File(pdfStatRepfolder +File.separator+"StatReport_"+ timeStamp+".pdf");

        try {
            newdoc.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OutputStream output = new FileOutputStream(newdoc);

        Document document = new Document(PageSize.A4);

        ///step 2 retrieve the document
        PdfWriter writer= PdfWriter.getInstance(document, output);


        PdfCustFooter reportFooter =  new PdfCustFooter();
        reportFooter.setFooterMsg("");
        reportFooter.setHeaderMsg("");
        writer.setPageEvent(reportFooter);


        ///step 3 open the document
        document.open();

        ///step 4 add content

        //the fonts
        Font fontTitle= new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font fontSubtitle= new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

        //end of fonts


        //******************************************User Summary header goes here *****************************/
        BaseColor blue_grey_color = new BaseColor(207,216,220);

        PdfPTable tableUserSum = new PdfPTable(5);


        PdfPCell cellSeparator = new PdfPCell(new Phrase(" " ));
        cellSeparator.setRowspan(2);
        cellSeparator.setColspan(5);
        //cellSeparator.setBackgroundColor(blue_grey_color);
        cellSeparator.setMinimumHeight(30);

        cellSeparator.setBorder(Rectangle.NO_BORDER);
        cellSeparator.setHorizontalAlignment(Element.ALIGN_CENTER);
       // table.addCell(cellSeparator);



        PdfPCell cellRepTitle = new PdfPCell(new Phrase("Diary Statistics",fontTitle));
        cellRepTitle.setRowspan(2);
        cellRepTitle.setColspan(5);
        cellRepTitle.setBackgroundColor(blue_grey_color);
        cellRepTitle.setMinimumHeight(25);
        cellRepTitle.setBorder(Rectangle.NO_BORDER);
        cellRepTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellRepTitle.setVerticalAlignment(Element.ALIGN_CENTER);
        tableUserSum.addCell(cellRepTitle);


        PdfPCell cellLeft = new PdfPCell(new Phrase("First Name: " ));
        cellLeft.setRowspan(2);
        cellLeft.setColspan(1);
        cellLeft.setMinimumHeight(20);
        cellLeft.setBackgroundColor(blue_grey_color);
        //cellSite.setBorder(Rectangle.NO_BORDER);
        cellLeft.setHorizontalAlignment(Element.ALIGN_LEFT);
        tableUserSum.addCell(cellLeft);


        PdfPCell cellRight = new PdfPCell(new Phrase(dUser.getFirstname() ));
        cellRight.setRowspan(2);
        cellRight.setColspan(4);
        cellRight.setMinimumHeight(20);
        // cellSummaryinfo.setBorder(Rectangle.NO_BORDER);
        cellRight.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableUserSum.addCell(cellRight);


        cellLeft.setPhrase(new Phrase("Surname: " ));
        cellRight.setPhrase(new Phrase(dUser.getSurname() ));
        tableUserSum.addCell(cellLeft);
        tableUserSum.addCell(cellRight);


        tableUserSum.addCell(cellSeparator);

        //******************************************End User Summary header goes here *****************************/


        PdfPTable tableStatChart = new PdfPTable(2);
        tableStatChart.setWidthPercentage(100f);


        //empty cell just to create space
        PdfPCell smallemptycell;
        smallemptycell = new PdfPCell(new Phrase(" "));
        smallemptycell.setColspan(3);
        smallemptycell.setMinimumHeight(10);
        //cellst.setBackgroundColor(blue_grey_color);
        smallemptycell.setBorder(Rectangle.NO_BORDER);
        smallemptycell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //end empty cell


        PdfPCell cellTitle;
        cellTitle = new PdfPCell(new Phrase("Moods",fontTitle));
        cellTitle.setColspan(3);
        cellTitle.setMinimumHeight(25);
        //cellst.setBackgroundColor(blue_grey_color);
        cellTitle.setBorder(Rectangle.NO_BORDER);
        cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);


        PdfPCell cellSubTitle;

        cellSubTitle = new PdfPCell(new Phrase("This Month",fontSubtitle));
        cellSubTitle.setColspan(3);
        cellSubTitle.setMinimumHeight(25);
        //cellst.setBackgroundColor(blue_grey_color);
        cellSubTitle.setBorder(Rectangle.NO_BORDER);
        cellSubTitle.setHorizontalAlignment(Element.ALIGN_CENTER);

        tableStatChart.addCell(cellTitle);
        tableStatChart.addCell(cellSubTitle);

        tableStatChart .addCell(smallemptycell);

        //adding the first month bar chart
        try{
            mMoodMonthChart.setDrawingCacheEnabled(true);
            Bitmap b = mMoodMonthChart.getDrawingCache();

            Image monthChartImg = Image.getInstance(bitmaptoByte(b));
            monthChartImg.scalePercent(30f);

            PdfPCell cellChart = new PdfPCell(monthChartImg);
            cellChart.setColspan(2);
            cellChart.setRowspan(2);

            cellChart.setMinimumHeight(60);
            // cellstatus.setBackgroundColor(hard_grey_color);
            cellChart.setBorder(Rectangle.NO_BORDER);
            cellChart.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellChart.setVerticalAlignment(Element.ALIGN_CENTER);
            tableStatChart.addCell(cellChart);

        }catch (IOException e)
        {
            e.printStackTrace();
        }

      //  tableStatChart .addCell(smallemptycell);

        //adding the second all time bar chart
        cellSubTitle.setPhrase(new Phrase("All time",fontSubtitle));
        tableStatChart.addCell(cellSubTitle);


        try{
            mMoodAllChart.setDrawingCacheEnabled(true);
            Bitmap b = mMoodAllChart.getDrawingCache();

            Image monthChartImg = Image.getInstance(bitmaptoByte(b));
            monthChartImg.scalePercent(30f);

            PdfPCell cellChart = new PdfPCell(monthChartImg);
            cellChart.setColspan(2);
            cellChart.setRowspan(2);

            cellChart.setMinimumHeight(60);
            // cellstatus.setBackgroundColor(hard_grey_color);
            cellChart.setBorder(Rectangle.NO_BORDER);
            cellChart.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellChart.setVerticalAlignment(Element.ALIGN_CENTER);
            tableStatChart.addCell(cellChart);

        }catch (IOException e)
        {
            e.printStackTrace();
        }

       // tableStatChart .addCell(smallemptycell);

        //adding the first month radar chart

        cellTitle.setPhrase(new Phrase("Activities",fontTitle));
        cellSubTitle.setPhrase(new Phrase("This Month",fontSubtitle));
        tableStatChart.addCell(cellTitle);
        tableStatChart.addCell(cellSubTitle);

        try{
            mActiMonthRadarChart.setDrawingCacheEnabled(true);
            Bitmap b = mActiMonthRadarChart.getDrawingCache();

            Image monthChartImg = Image.getInstance(bitmaptoByte(b));
            monthChartImg.scalePercent(30f);

            PdfPCell cellChart = new PdfPCell(monthChartImg);
            cellChart.setColspan(2);
            cellChart.setRowspan(2);

            cellChart.setMinimumHeight(60);
            // cellstatus.setBackgroundColor(hard_grey_color);
            cellChart.setBorder(Rectangle.NO_BORDER);
            cellChart.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellChart.setVerticalAlignment(Element.ALIGN_CENTER);
            tableStatChart.addCell(cellChart);

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        tableStatChart .addCell(smallemptycell);

        /****adding the second all time radar chart*****/
        cellSubTitle.setPhrase(new Phrase("This Month",fontSubtitle));
        tableStatChart.addCell(cellSubTitle);
        try{
            mActiAllRadarChart.setDrawingCacheEnabled(true);
            Bitmap b = mActiAllRadarChart.getDrawingCache();

            Image monthChartImg = Image.getInstance(bitmaptoByte(b));
            monthChartImg.scalePercent(30f);

            PdfPCell cellChart = new PdfPCell(monthChartImg);
            cellChart.setColspan(2);
            cellChart.setRowspan(2);

            cellChart.setMinimumHeight(60);
            // cellstatus.setBackgroundColor(hard_grey_color);
            cellChart.setBorder(Rectangle.NO_BORDER);
            cellChart.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellChart.setVerticalAlignment(Element.ALIGN_CENTER);
            tableStatChart.addCell(cellChart);

        }catch (IOException e)
        {
            e.printStackTrace();
        }



        /****adding the table to the document*****/
        document.add( tableUserSum);
        document.add(tableStatChart);

        ///step 5 close the document
        document.close();

        ///step 6 extract the file path
        mPDFFilePath =newdoc.getPath();

    }






}
