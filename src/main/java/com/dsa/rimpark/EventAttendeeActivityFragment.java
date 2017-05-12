package com.dsa.rimpark;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dsa.rimpark.FireBaseSvr.AttendeeFBDB;
import com.dsa.rimpark.model.Attendee;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventAttendeeActivityFragment extends Fragment {

    ListView attendeeListView;
    List<DataSnapshot> attendeesDataSnapShotList;
    AttendeeListAdapter attendeeListAdapter;
    AttendeeFBDB attendeeFBDB;
    ValueEventListener attendeeValueEventLister = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            attendeesDataSnapShotList.clear();
            for (DataSnapshot attendeeDataSnapShot : dataSnapshot.getChildren()) {
                attendeesDataSnapShotList.add(attendeeDataSnapShot);
            }
            attendeeListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ChildEventListener attendeeChildEventListner = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            attendeesDataSnapShotList.add(dataSnapshot);
            attendeeListAdapter.notifyDataSetChanged();
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
    public EventAttendeeActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_event_attendee, container, false);
        attendeeFBDB=new AttendeeFBDB(EventAttendeeActivity.eventKey);
        attendeesDataSnapShotList=new ArrayList<DataSnapshot>();
        attendeeListView =  (ListView) view.findViewById(R.id.attendeeListView);
        attendeeListAdapter=new AttendeeListAdapter(getActivity(), attendeesDataSnapShotList);
        attendeeListView.setAdapter(attendeeListAdapter);
        attendeeFBDB.getReference().addChildEventListener(attendeeChildEventListner);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //attendeeFBDB.getReference().addChildEventListener(attendeeChildEventListner);
    }

    @Override
    public void onPause() {
        super.onPause();
        //attendeeFBDB.getReference().removeEventListener(attendeeChildEventListner);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
