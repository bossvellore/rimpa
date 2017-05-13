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
import android.widget.TextView;

import com.dsa.rimpark.FireBaseSvr.AttendeeFBDB;
import com.google.firebase.database.ChildEventListener;
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
    Fragment eventAttendeeActivityFragment;

    TextView attendedCountTV;
    TextView unAttendedCountTV;
    TextView pendingCountTV;


    int attendedCount;
    ChildEventListener attendedStatusListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            attendedCount++;
            setStatusText();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setStatusText();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            attendedCount--;
            setStatusText();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    int unAttendedCount;
    ChildEventListener unAttendedStatusListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            unAttendedCount++;
            setStatusText();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setStatusText();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            unAttendedCount--;
            setStatusText();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    int pendingCount;
    ChildEventListener pendingStatusListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            pendingCount++;
            setStatusText();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setStatusText();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            pendingCount--;
            setStatusText();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public EventAttendeeActivity()
    {


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        attendedCountTV = (TextView) findViewById(R.id.attendedCountTV);
        unAttendedCountTV = (TextView) findViewById(R.id.unAttendedCountTV);
        pendingCountTV = (TextView) findViewById(R.id.pendingCountTV);

        bundle=getIntent().getExtras();
        if(bundle != null) {
            STATE_EVENT_POSITION = bundle.getInt("data_snapshot_position");
        }else if(savedInstanceState != null)
        {
            STATE_EVENT_POSITION = savedInstanceState.getInt("data_snapshot_position");
        }
        eventDataSnapShot = MainActivity.dataSnapshotList.get(STATE_EVENT_POSITION);
        eventKey=eventDataSnapShot.getKey();
        attendedCount=0;
        unAttendedCount=0;
        pendingCount=0;

        AttendeeFBDB attendeeFBDB = new AttendeeFBDB(eventKey);
        attendeeFBDB.getReference().orderByChild("status").equalTo("PENDING").addChildEventListener(pendingStatusListener);
        attendeeFBDB.getReference().orderByChild("status").equalTo("ATTENDED").addChildEventListener(attendedStatusListener);
        attendeeFBDB.getReference().orderByChild("status").equalTo("UNATTENDED").addChildEventListener(unAttendedStatusListener);

        eventAttendeeActivityFragment = new EventAttendeeActivityFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.attendeeListFragContainer, eventAttendeeActivityFragment);
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

    private void setStatusText()
    {

        if(attendedCountTV!=null)
        {
            attendedCountTV.setText(String.valueOf(attendedCount));
        }
        if(unAttendedCountTV!=null)
        {
            unAttendedCountTV.setText(String.valueOf(unAttendedCount));
        }
        if(pendingCountTV!=null)
        {
            pendingCountTV.setText(String.valueOf(pendingCount));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setStatusText();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("data_snapshot_position", STATE_EVENT_POSITION);
        super.onSaveInstanceState(outState);
    }
}
