package com.dsa.rimpark;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dsa.rimpark.model.Attendee;
import com.dsa.rimpark.model.EventModel;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

/**
 * Created by amalroshand on 11/05/17.
 */

public class AttendeeListAdapter extends BaseAdapter {

    List<DataSnapshot> items;
    Activity context;
    public AttendeeListAdapter(Activity context, List<DataSnapshot> items) {
        super();
        this.context = context;
        this.items = items;
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
            view = context.getLayoutInflater().inflate(R.layout.adapter_attendee_item, null);
        Attendee attendee = items.get(position).getValue(Attendee.class);
        TextView titleText=(TextView)view.findViewById(R.id.attendeeNameTV);
        titleText.setText(attendee.getName());
        return view;
    }
}