package cct.ansteph.moodrecorder.view.welcome;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import cct.ansteph.moodrecorder.R;
import cct.ansteph.moodrecorder.api.ContentTypes;
import cct.ansteph.moodrecorder.api.columns.DiaryUserColumns;
import cct.ansteph.moodrecorder.helper.SessionManager;
import cct.ansteph.moodrecorder.view.record.RecordMood;

public class Registration extends AppCompatActivity {

    EditText edtFirstname, edtSurname, edtAge, edtMotive;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (registerDiaryUser() ==1)
                startActivity(new Intent(getApplicationContext(), RecordMood.class));

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        edtFirstname = (EditText) findViewById(R.id.edtfirstname) ;
        edtSurname = (EditText) findViewById(R.id.edtlastname) ;
        edtAge = (EditText) findViewById(R.id.edtAge) ;
        edtMotive = (EditText) findViewById(R.id.edtMotive) ;

        sessionManager = new SessionManager(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    public boolean isFormCancelled() {
        //Reset the errors
        edtFirstname.setError(null);
        edtSurname.setError(null);


        //flash storing
        String f_name = edtFirstname.getText().toString();
        String surname = edtSurname.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(f_name))
        {
            edtFirstname.setError(getString(R.string.error_field_required));
            focusView = edtFirstname;
            cancel = true;

        }else if(TextUtils.isEmpty(surname))
        {
            edtSurname.setError(getString(R.string.error_field_required));
            focusView = edtSurname;
            cancel = true;

        }

        if(cancel)
        {
            focusView.requestFocus();
        }

        return cancel;

    }


    public int registerDiaryUser(){

        if(isFormCancelled())
                return 0;

        String f_name = edtFirstname.getText().toString();
        String surname = edtSurname.getText().toString();
        String age = edtSurname.getText().toString();
        String motive = edtSurname.getText().toString();


        try {
            ContentValues values = new ContentValues();

            values.put(DiaryUserColumns.FIRSTNAME,f_name) ;
            values.put(DiaryUserColumns.SURNAME ,surname) ;
            values.put(DiaryUserColumns.AGE,age) ;
            values.put(DiaryUserColumns.MOTIVE,motive) ;

            getContentResolver().insert(ContentTypes.DIARYUSER_CONTENT_URI, values);

            sessionManager.recordRegistration();
            return 1;


        }catch (SQLException e)
        {
            e.printStackTrace();

            return 0;
        }

    }


}
