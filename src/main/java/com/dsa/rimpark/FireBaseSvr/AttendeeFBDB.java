package com.dsa.rimpark.FireBaseSvr;

import com.dsa.rimpark.model.Attendee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by amalroshand on 11/05/17.
 */

public class AttendeeFBDB {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth fbAuth;
    private FirebaseUser user;

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
    public void updateStatus(String childReference, String newStatus)
    {
        reference.child(childReference).child("status").setValue(newStatus);
    }

    public DatabaseReference getReference() {
        return reference;
    }
}
