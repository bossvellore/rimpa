package com.dsa.rimpark;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

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
    List<DataSnapshot> subAttendeesDataSnapShotList;
    AttendeeListAdapter attendeeListAdapter;
    AttendeeFBDB attendeeFBDB;
    int allAttendeeCount;
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
            allAttendeeCount++;
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            for (int index=0; index < attendeesDataSnapShotList.size(); index++) {
                if(attendeesDataSnapShotList.get(index).getKey().equals(dataSnapshot.getKey())){
                    attendeesDataSnapShotList.set(index, dataSnapshot);
                }
            }
            attendeeListAdapter.notifyDataSetChanged();
        }
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            allAttendeeCount--;
            for (int index=0; index < attendeesDataSnapShotList.size(); index++) {
                if(attendeesDataSnapShotList.get(index).getKey().equals(dataSnapshot.getKey())){
                    attendeesDataSnapShotList.remove(index);
                    attendeeListAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    public EventAttendeeActivityFragment() {
        allAttendeeCount=0;
    }

    public int getAllAttendeeCount(){return allAttendeeCount;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_event_attendee, container, false);
        attendeeFBDB=new AttendeeFBDB(EventAttendeeActivity.eventKey);
        attendeesDataSnapShotList=new ArrayList<DataSnapshot>();
        subAttendeesDataSnapShotList=new ArrayList<DataSnapshot>();
        attendeeListView =  (ListView) view.findViewById(R.id.attendeeListView);
        attendeeListAdapter=new AttendeeListAdapter(getActivity(), attendeesDataSnapShotList);
        attendeeListView.setAdapter(attendeeListAdapter);
        attendeeFBDB.getReference().orderByChild("name").addChildEventListener(attendeeChildEventListner);
        SearchView searchView = (SearchView)view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
        return view;
    }
    private void search(String query)
    {
        if(!query.equals("")) {
            subAttendeesDataSnapShotList.clear();
            for (DataSnapshot dataSnapshot : attendeesDataSnapShotList) {
                Attendee attendee = dataSnapshot.getValue(Attendee.class);
                if (attendee.getName().contains(query)) {
                    subAttendeesDataSnapShotList.add(dataSnapshot);
                    attendeeListAdapter.setItems(subAttendeesDataSnapShotList);
                    attendeeListAdapter.notifyDataSetChanged();
                }
            }
        }
        else{
            attendeeListAdapter.setItems(attendeesDataSnapShotList);
            attendeeListAdapter.notifyDataSetChanged();
        }
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
