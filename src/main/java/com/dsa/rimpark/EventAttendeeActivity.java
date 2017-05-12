package com.dsa.rimpark;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dsa.rimpark.FireBaseSvr.AttendeeFBDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventAttendeeActivity extends AppCompatActivity {
    Bundle bundle;
    DataSnapshot eventDataSnapShot;
    int STATE_EVENT_POSITION;
    static String eventKey="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle=getIntent().getExtras();
        if(bundle != null) {
            STATE_EVENT_POSITION = bundle.getInt("data_snapshot_position");
        }else if(savedInstanceState != null)
        {
            STATE_EVENT_POSITION = savedInstanceState.getInt("data_snapshot_position");
        }
        eventDataSnapShot = MainActivity.dataSnapshotList.get(STATE_EVENT_POSITION);
        eventKey=eventDataSnapShot.getKey();

        Fragment newFragment = new EventAttendeeActivityFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.attendeeListFragContainer, newFragment);
        //transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                Intent attendeeAddIntent= new Intent(getApplicationContext(), AttendeeAddActivity.class);
                attendeeAddIntent.putExtra("event_key", eventDataSnapShot.getKey());
                startActivity(attendeeAddIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("data_snapshot_position", STATE_EVENT_POSITION);
        super.onSaveInstanceState(outState);
    }
}
