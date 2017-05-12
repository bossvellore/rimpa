package com.dsa.rimpark.FireBaseSvr;

import com.dsa.rimpark.model.Attendee;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by amalroshand on 11/05/17.
 */

public class AttendeeFBDB {
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public AttendeeFBDB(String eventKey)
    {
        database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        reference = database.getReference("events").child(eventKey).child("attendees");
    }

    public void save(Attendee attendee)
    {
        reference.push().setValue(attendee);
    }

    public void save(Attendee attendee, String key)
    {
        reference.child(key).setValue(attendee);
    }

    public void delete(String childReference)
    {
        reference.child(childReference).removeValue();
    }

    public DatabaseReference getReference() {
        return reference;
    }
}
