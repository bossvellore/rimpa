package com.dsa.rimpark;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dsa.rimpark.FireBaseSvr.EventFBDB;
import com.dsa.rimpark.model.EventModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Menu menu;
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
            boolean exists = false;
            for (int index=0; index < dataSnapshotList.size(); index++) {
                if(dataSnapshotList.get(index).getKey().equals(eventDataSnapShot.getKey())){
                    exists = true;
                }
            }
            if(!exists) {
                EventModel eventModel=eventDataSnapShot.getValue(EventModel.class);
                if(checkEventForCurrentUser(eventModel.getUsers()))
                        dataSnapshotList.add(eventDataSnapShot);

                eventsListAdaper.notifyDataSetChanged();
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            EventModel eventModel=dataSnapshot.getValue(EventModel.class);
            if(checkEventForCurrentUser(eventModel.getUsers())) {
                boolean foundAndReplaced = false;
                for (int index = 0; index < dataSnapshotList.size(); index++) {
                    if (dataSnapshotList.get(index).getKey().equals(dataSnapshot.getKey())) {
                        dataSnapshotList.set(index, dataSnapshot);
                        foundAndReplaced=true;
                    }
                }
                if (!foundAndReplaced) {
                    dataSnapshotList.add(dataSnapshot);
                    resetCounts();
                }
            }
            eventsListAdaper.notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            for (int index=0; index < dataSnapshotList.size(); index++) {
                if(dataSnapshotList.get(index).getKey().equals(dataSnapshot.getKey())){
                    dataSnapshotList.remove(index);
                }
            }
            eventsListAdaper.notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private boolean checkEventForCurrentUser(HashMap<String,String> users)
    {
        if(users!=null)
        {
            for (Map.Entry<String,String> user : users.entrySet())
            {
                if(user.getKey().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()))
                    return true;

            }
        }
        return false;
    }
    int upComingCount;
    ChildEventListener upCommingStatusListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            EventModel eventModel=dataSnapshot.getValue(EventModel.class);
            if(checkEventForCurrentUser(eventModel.getUsers()))
                upComingCount++;
            setStatusText();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setStatusText();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            EventModel eventModel=dataSnapshot.getValue(EventModel.class);
            if(checkEventForCurrentUser(eventModel.getUsers()))
                upComingCount--;
            setStatusText();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    int onGoingCount;
    ChildEventListener onGoingStatusListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            EventModel eventModel=dataSnapshot.getValue(EventModel.class);
            if(checkEventForCurrentUser(eventModel.getUsers()))
                onGoingCount++;
            setStatusText();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setStatusText();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            EventModel eventModel=dataSnapshot.getValue(EventModel.class);
            if(checkEventForCurrentUser(eventModel.getUsers()))
                onGoingCount--;
            setStatusText();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    int completedCount;
    ChildEventListener completedStatusListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            EventModel eventModel=dataSnapshot.getValue(EventModel.class);
            if(checkEventForCurrentUser(eventModel.getUsers()))
                completedCount++;
            setStatusText();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setStatusText();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            EventModel eventModel=dataSnapshot.getValue(EventModel.class);
            if(checkEventForCurrentUser(eventModel.getUsers()))
                completedCount--;
            setStatusText();
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
        upComingCount =0;
        eventDB.getReference().orderByChild("status").equalTo("UPCOMING").addChildEventListener(upCommingStatusListener);
        onGoingCount=0;
        eventDB.getReference().orderByChild("status").equalTo("ONGOING").addChildEventListener(onGoingStatusListener);
        completedCount=0;
        eventDB.getReference().orderByChild("status").equalTo("COMPLETED").addChildEventListener(completedStatusListener);
        dataSnapshotList.clear();
        eventsListAdaper = new EventsListAdapter(this, dataSnapshotList);
        eventDB.getReference().addChildEventListener(eventsChildEventListener);

    }
    private void resetCounts()
    {
        eventDB.getReference().orderByChild("status").equalTo("UPCOMING").removeEventListener(upCommingStatusListener);
        upComingCount =0;
        eventDB.getReference().orderByChild("status").equalTo("UPCOMING").addChildEventListener(upCommingStatusListener);

        eventDB.getReference().orderByChild("status").equalTo("ONGOING").removeEventListener(onGoingStatusListener);
        onGoingCount=0;
        eventDB.getReference().orderByChild("status").equalTo("ONGOING").addChildEventListener(onGoingStatusListener);

        eventDB.getReference().orderByChild("status").equalTo("COMPLETED").removeEventListener(completedStatusListener);
        completedCount=0;
        eventDB.getReference().orderByChild("status").equalTo("COMPLETED").addChildEventListener(completedStatusListener);
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
        eventListView.setAdapter(eventsListAdaper);


        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent eventAttendeeIntent= new Intent(getApplicationContext(),EventAttendeeActivity.class);
                eventAttendeeIntent.putExtra("data_snapshot_position", position);
                eventAttendeeIntent.putExtra("is_manage_attendee", true);
                eventAttendeeIntent.putExtra("eventUsers", dataSnapshotList.get(position).getValue(EventModel.class).getUsers());
                startActivity(eventAttendeeIntent);
            }
        });
        registerForContextMenu(eventListView);
        completedTV=(TextView) findViewById(R.id.completedTV);
        ongoingTV=(TextView) findViewById(R.id.ongoingTV);
        upcomingTV=(TextView) findViewById(R.id.upcomingTV);


    }

    public void setStatusText()
    {
        if(completedTV!=null)
        {
            completedTV.setText(String.valueOf(completedCount));
        }
        if(ongoingTV!=null)
        {
            ongoingTV.setText(String.valueOf(onGoingCount));
        }
        if(upcomingTV!=null)
        {
            upcomingTV.setText(String.valueOf(upComingCount));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String eventKey = dataSnapshotList.get(info.position).getKey();
        switch (item.getItemId()) {
            case R.id.removeItem:
                eventDB.delete(eventKey);
                return true;
            case R.id.setCompleted:
                eventDB.updateStatus(eventKey, "COMPLETED");
                return  true;
            case  R.id.setOnGoing:
                eventDB.updateStatus(eventKey, "ONGOING");
                return  true;

        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.status_menu, menu);
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
        //if (id == R.id.logoutMenu) {
        //  return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusText();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu=menu;
        setLoggedInUser();
        return super.onPrepareOptionsMenu(menu);
    }

    private void setLoggedInUser()
    {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String name=user.getDisplayName();
        if(name.trim().equals(""))
        {
            name = user.getEmail();
        }
        MenuItem userGreetingMenu = (MenuItem) menu.findItem(R.id.userGreetingMenu);
        userGreetingMenu.setTitle(name);
    }

}

