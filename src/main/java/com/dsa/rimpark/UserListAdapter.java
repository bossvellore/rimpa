package com.dsa.rimpark;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dsa.rimpark.FireBaseSvr.EventFBDB;
import com.dsa.rimpark.model.UserModel;

import java.util.List;
import java.util.Map;

/**
 * Created by amalroshand on 16/05/17.
 */

public class UserListAdapter extends BaseAdapter {

    Activity context;
    List<UserModel> items;
    String eventKey;
    Map<String,String> eventUsers;

    public UserListAdapter(Activity context, List<UserModel> items, String eventKey, Map<String,String> eventUsers)
    {
        this.context=context;
        this.items=items;
        this.eventKey = eventKey;
        this.eventUsers = eventUsers;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView; // re-use an existing view, if one is available
        if (view == null) // otherwise create a new one
            view = context.getLayoutInflater().inflate(R.layout.adapter_user_item, null);
        UserModel userModel = items.get(position);
        TextView displayNameTV = (TextView) view.findViewById(R.id.displayNameTV);
        displayNameTV.setText(userModel.getDisplayName());
        TextView userNameTV = (TextView) view.findViewById(R.id.userNameTV);
        userNameTV.setText(userModel.getEmail());
        Button addUserBtn = (Button)view.findViewById(R.id.addUserBtn);
        addUserBtn.setOnClickListener(new AddRemoveUserListener(eventKey, userModel.getUid(), "ADD", addUserBtn));
        Button removeUserBtn = (Button)view.findViewById(R.id.removeUserBtn);
        removeUserBtn.setOnClickListener(new AddRemoveUserListener(eventKey, userModel.getUid(), "REMOVE", addUserBtn));
        boolean isAdded = false;
        for(Map.Entry<String,String> userEntry : eventUsers.entrySet())
        {
            if(userModel.getUid().equals(userEntry.getKey()))
            {
                isAdded=true;
                break;
            }
        }
        if(isAdded)
            addUserBtn.setBackgroundColor(Color.parseColor("#26A69A"));
        return view;
    }
}

class AddRemoveUserListener implements View.OnClickListener{
    private String eventKey;
    private String uid;
    private String type;
    private EventFBDB eventFBDB;
    private Button addUserBtn;
    public AddRemoveUserListener(String eventKey, String uid, String type, Button addUserBtn)
    {
        this.eventKey = eventKey;
        this.uid=uid;
        this.type=type;
        this.addUserBtn=addUserBtn;
        eventFBDB = new EventFBDB();
    }
    @Override
    public void onClick(View v) {
        if(type == "ADD")
        {
            //eventFBDB.getReference().child(eventKey).child("users").child(uid).setValue("true");
            eventFBDB.addUser(uid, eventKey);
            v.findViewById(R.id.addUserBtn).setBackgroundColor(Color.parseColor("#26A69A"));
        }
        else if(type == "REMOVE")
        {
            eventFBDB.removeUser(uid, eventKey);
            addUserBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }
}
