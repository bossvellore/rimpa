package com.dsa.rimpark;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;

public class EventAttendeeActivity extends AppCompatActivity {
    Bundle bundle;
    DataSnapshot eventDataSnapShot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        bundle=getIntent().getExtras();
        eventDataSnapShot = MainActivity.dataSnapshotList.get(bundle.getInt("data_snapshot_position"));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                Intent eventAddIntent= new Intent(getApplicationContext(), AttendeeAddActivity.class);
                startActivity(eventAddIntent);
            }
        });
    }

}
