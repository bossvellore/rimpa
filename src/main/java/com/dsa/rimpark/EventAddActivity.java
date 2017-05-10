package com.dsa.rimpark;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dsa.rimpark.FireBaseSvr.EventFBDB;
import com.dsa.rimpark.model.EventModel;

public class EventAddActivity extends AppCompatActivity {

    EditText titleTxt;
    EditText descriptionTxt;
    EditText dateTxt;
    EditText timeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_add);

        titleTxt = (EditText)findViewById(R.id.titleTxt);
        descriptionTxt = (EditText)findViewById(R.id.descriptionText);
        dateTxt = (EditText)findViewById(R.id.dateTxt);
        timeTxt = (EditText)findViewById(R.id.timeTxt);

        final Button eventCreate=(Button)findViewById(R.id.eventCreateBtn);
        eventCreate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EventModel eventModel=new EventModel();
                eventModel.setTitle(titleTxt.getText().toString());
                eventModel.setDescription(descriptionTxt.getText().toString());
                eventModel.setStatus("UPCOMING");
                EventFBDB eventDB=new EventFBDB();
                eventDB.save(eventModel);
            }
        });
    }
}


