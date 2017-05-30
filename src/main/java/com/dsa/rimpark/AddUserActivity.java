package com.dsa.rimpark;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.dsa.rimpark.FireBaseSvr.UserFBDB;
import com.dsa.rimpark.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    Bundle bundle;
    String eventKey;
    List<UserModel> userList=new ArrayList<UserModel>();
    UserListAdapter userListAdapter;
    ValueEventListener userValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot userDataSnapshot : dataSnapshot.getChildren())
            {
                UserModel userModel = userDataSnapshot.getValue(UserModel.class);
                userList.add(userModel);
                userListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bundle = this.getIntent().getExtras().getBundle("bundle");
        eventKey=bundle.getString("eventKey");
        Map<String,String> eventUsers = (Map<String,String>)bundle.get("eventUsers");

        userListAdapter=new UserListAdapter(this, userList, eventKey, eventUsers);
        ListView userListView=(ListView)findViewById(R.id.usersListView);
        userListView.setAdapter(userListAdapter);
        UserFBDB userFBDB=new UserFBDB();
        userFBDB.getReference().addListenerForSingleValueEvent(userValueEventListener);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
