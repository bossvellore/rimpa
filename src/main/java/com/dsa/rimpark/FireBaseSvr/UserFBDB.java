package com.dsa.rimpark.FireBaseSvr;

import com.dsa.rimpark.model.EventModel;
import com.dsa.rimpark.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by amalroshand on 16/05/17.
 */

public class UserFBDB {

    // Write a message to the database

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth fbAuth;
    private FirebaseUser user;

    public UserFBDB()
    {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
    }

    public void save(UserModel user)
    {
        reference.child(user.getUid()).setValue(user);

    }

    public void addEvent(String uid, String eventKey)
    {
        reference.child(uid).child("events").push().setValue(eventKey);
    }
    public void delete(String uid)
    {
        reference.child(uid).removeValue();
    }
    public DatabaseReference getReference() {
        return reference;
    }
}
