package com.dsa.rimpark.FireBaseSvr;

import android.support.annotation.NonNull;

import com.dsa.rimpark.EventCounts;
import com.dsa.rimpark.model.EventModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by amalroshand on 09/05/17.
 */

public class EventFBDB {
    // Write a message to the database

    private FirebaseDatabase database;
    private DatabaseReference reference;

    public EventFBDB()
    {
        database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        reference = database.getReference("events");
    }

    public void save(EventModel event)
    {
        reference.push().setValue(event);
    }

    public void save(EventModel event, String key)
    {
        reference.child(key).setValue(event);
    }

    public void getEvents()
    {
        final List<Map<String, EventModel>> events = new ArrayList<Map<String,EventModel>>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    HashMap<String,EventModel> event = new HashMap<String, EventModel>();
                    event.put(eventSnapshot.getKey(), eventSnapshot.getValue(EventModel.class));
                    events.add(event);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCount(final String status, final EventCounts count)
    {
        Query query=reference.orderByChild("status").startAt(status);
        switch (status) {
            case "COMPLETED" :
                query.endAt("ONGOING");
                break;
            case "ONGOING" :
                query.endAt("UPCOMING");
                break;
            case "UPCOMING" :

                break;
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switch (status) {
                    case "COMPLETED" :
                        count.completedCount=dataSnapshot.getChildrenCount();
                        break;
                    case "ONGOING" :
                        count.ongoingCount=dataSnapshot.getChildrenCount();
                        break;
                    case "UPCOMING" :
                        count.upcomingCount=dataSnapshot.getChildrenCount();
                        break;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public DatabaseReference getReference() {
        return reference;
    }
}
