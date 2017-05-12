package com.dsa.rimpark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dsa.rimpark.FireBaseSvr.AttendeeFBDB;
import com.dsa.rimpark.model.Attendee;

public class AttendeeAddActivity extends AppCompatActivity {

    EditText nameTxt;
    EditText emailTxt;
    EditText mobileTxt;
    EditText notesTxt;
    EditText companyTxt;
    Attendee attendee;
    Bundle bundle;
    String eventKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_add);
        bundle=getIntent().getExtras();
        if(bundle !=null)
        {
            eventKey = bundle.getString("event_key");
        }

        attendee=new Attendee();
        nameTxt = (EditText) findViewById(R.id.nameTxt);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        mobileTxt = (EditText) findViewById(R.id.mobileTxt);
        notesTxt = (EditText) findViewById(R.id.notesTxt);
        companyTxt = (EditText) findViewById(R.id.companyTxt);

        Button addAttendeeBtn=(Button) findViewById(R.id.addAttendeeBtn);
        addAttendeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameTxt.getText().toString().equals("")) {
                    attendee.setName(nameTxt.getText().toString());
                    attendee.setEmail(emailTxt.getText().toString());
                    String mobile = mobileTxt.getText().toString().trim();
                    if (!mobile.equals("")) {
                        attendee.setMobile(Long.valueOf(mobileTxt.getText().toString()));
                    }
                    attendee.setNotes(notesTxt.getText().toString());
                    attendee.setCompany(companyTxt.getText().toString());
                    attendee.setStatus("PENDING");
                    AttendeeFBDB attendeeFBDB = new AttendeeFBDB(eventKey);
                    attendeeFBDB.save(attendee);
                    onBackPressed();

                }
            }
        });
    }
}
