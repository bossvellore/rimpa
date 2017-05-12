package com.dsa.rimpark;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dsa.rimpark.FireBaseSvr.EventFBDB;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView eventListView;
    TextView completedTV;
    TextView ongoingTV;
    TextView upcomingTV;
    EventsListAdapter eventsListAdaper;
    EventFBDB eventDB = new EventFBDB();
    public static List<DataSnapshot> dataSnapshotList = new ArrayList<DataSnapshot>();
    ValueEventListener eventsValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dataSnapshotList.clear();
            for (DataSnapshot eventDataSnapShot : dataSnapshot.getChildren()) {
                dataSnapshotList.add(eventDataSnapShot);
            }
            eventsListAdaper.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ChildEventListener eventsChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot eventDataSnapShot, String s) {
            dataSnapshotList.add(eventDataSnapShot);
            eventsListAdaper.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    public MainActivity() {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //completedTV=(TextView) findViewById(R.id.completedTV);

        //ongoingTV=(TextView) findViewById(R.id.ongoingTV);
        //upcomingTV=(TextView) findViewById(R.id.upcomingTV);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventAddIntent= new Intent(getApplicationContext(), EventAddActivity.class);
                startActivity(eventAddIntent);
            }
        });

        eventListView=(ListView) findViewById(R.id.eventsListView);
        eventsListAdaper = new EventsListAdapter(this, dataSnapshotList);
        eventListView.setAdapter(eventsListAdaper);
        eventDB.getReference().addChildEventListener(eventsChildEventListener);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent eventAttendeeIntent= new Intent(getApplicationContext(),EventAttendeeActivity.class);
                eventAttendeeIntent.putExtra("data_snapshot_position", position);
                eventAttendeeIntent.putExtra("is_manage_attendee", true);
                startActivity(eventAttendeeIntent);
            }
        });

        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                Intent eventAddIntent= new Intent(getApplicationContext(), EventAddActivity.class);
                eventAddIntent.putExtra("data_snapshot", position);
                eventAddIntent.putExtra("is_edit", true);
                startActivity(eventAddIntent);
                */
                return false;
            }
        });
        completedTV=(TextView) findViewById(R.id.completedTV);
        ongoingTV=(TextView) findViewById(R.id.ongoingTV);
        upcomingTV=(TextView) findViewById(R.id.upcomingTV);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //final Activity context=this;
        //eventDB.getReference().addListenerForSingleValueEvent(eventsValueEventListener);

        getCount("COMPLETED", completedTV);
        getCount("ONGOING", ongoingTV);
        getCount("UPCOMING", upcomingTV);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //eventDB.getReference().removeEventListener(eventsValueEventListener);
    }
    public void getCount(final String status, final TextView countTV)
    {
        Query query=eventDB.getReference().orderByChild("status").equalTo(status);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                switch (status) {
                    case "COMPLETED" :
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                countTV.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                            }
                        });
                        break;
                    case "ONGOING" :
                        ongoingTV.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        break;
                    case "UPCOMING" :
                        upcomingTV.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        break;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

