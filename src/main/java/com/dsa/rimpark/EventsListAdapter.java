package com.dsa.rimpark;

/**
 * Created by amalroshand on 09/05/17.
 */
import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dsa.rimpark.model.EventModel;
import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;
import java.util.List;
public class EventsListAdapter extends BaseAdapter {

    List<DataSnapshot> items;
    Activity context;
    public EventsListAdapter(Activity context, List<DataSnapshot> items) {
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
            view = context.getLayoutInflater().inflate(R.layout.adapter_event_item, null);
        TextView titleText=(TextView)view.findViewById(R.id.titleTV);
        titleText.setText(items.get(position).getValue(EventModel.class).getTitle());
        return view;

    }

    public String getKey(int position)
    {
        return items.get(position).getKey();
    }
}
