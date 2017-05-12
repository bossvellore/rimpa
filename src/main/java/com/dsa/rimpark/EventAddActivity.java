package com.dsa.rimpark;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dsa.rimpark.FireBaseSvr.EventFBDB;
import com.dsa.rimpark.model.EventModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class EventAddActivity extends AppCompatActivity {

    EditText titleTxt;
    EditText descriptionTxt;
    EditText dateTxt;
    EditText timeTxt;
    Bundle bundle;
    SimpleDateFormat dateF = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);

        titleTxt = (EditText)findViewById(R.id.titleTxt);
        descriptionTxt = (EditText)findViewById(R.id.descriptionText);
        dateTxt = (EditText)findViewById(R.id.dateTxt);
        timeTxt = (EditText)findViewById(R.id.timeTxt);


        String date = dateF.format(Calendar.getInstance().getTime());
        String time = timeF.format(Calendar.getInstance().getTime());


        dateTxt.setText(date);
        timeTxt.setText(time);

        dateTxt.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar myCurrentDate = Calendar.getInstance();
                int mYear = myCurrentDate.get(Calendar.YEAR);
                int mMonth = myCurrentDate.get(Calendar.MONTH);
                int mDay = myCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;

                mDatePicker = new DatePickerDialog(EventAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        Date date = new GregorianCalendar(selectedyear, selectedmonth, selectedday).getTime();
                        dateTxt.setText(dateF.format(date));
                    }
                }, mYear, mMonth, mDay);

                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });
        timeTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EventAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeTxt.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        bundle=getIntent().getExtras();
        if (bundle != null) {
            Boolean isEdit = bundle.getBoolean("is_edit");
            if( isEdit ) {
                String key=MainActivity.dataSnapshotList.get(bundle.getInt("data_snapshot")).getKey();
                EventModel event=MainActivity.dataSnapshotList.get(bundle.getInt("data_snapshot")).getValue(EventModel.class);

            }
        }

        final Button eventCreate=(Button)findViewById(R.id.eventCreateBtn);
        eventCreate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EventModel eventModel=new EventModel();
                eventModel.setTitle(titleTxt.getText().toString());
                eventModel.setDescription(descriptionTxt.getText().toString());
                String datetime = dateTxt.getText().toString() +" "+ timeTxt.getText().toString();
                eventModel.setDateTime(datetime);
                eventModel.setStatus("UPCOMING");
                EventFBDB eventDB=new EventFBDB();
                eventDB.save(eventModel);
                onBackPressed();
            }
        });
    }


}