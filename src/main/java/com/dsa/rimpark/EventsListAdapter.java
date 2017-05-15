package com.dsa.rimpark;

/**
 * Created by amalroshand on 09/05/17.
 */
import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dsa.rimpark.model.Attendee;
import com.dsa.rimpark.model.EventModel;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.rgb;

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
        TextView titleTV=(TextView)view.findViewById(R.id.titleTV);
        titleTV.setText("");
        TextView attendeesTV = (TextView)view.findViewById(R.id.attendeesTV);
        attendeesTV.setText("");
        TextView attendedTV = (TextView)view.findViewById(R.id.attendedTV);
        attendedTV.setText("");
        TextView unAttendedTV = (TextView)view.findViewById(R.id.unAttendedTV);
        unAttendedTV.setText("");

        TextView pendingTV = (TextView)view.findViewById(R.id.pendingTV);
        pendingTV.setText("");



        EventModel event = items.get(position).getValue(EventModel.class);
        titleTV.setText(items.get(position).getValue(EventModel.class).getTitle());

        CardView cardViewList = (CardView) view.findViewById(R.id.eventItemCard);
        switch (event.getStatus())
        {
            case "COMPLETED" :
                cardViewList.setCardBackgroundColor(Color.parseColor("#A5D6A7"));

                break;
            case "ONGOING" :
                cardViewList.setCardBackgroundColor(Color.parseColor("#FFCCBC"));

                break;
            case "UPCOMING" :
                cardViewList.setCardBackgroundColor(Color.parseColor("#81D4FA"));

                break;
        }

        TextView eventDate = (TextView)view.findViewById(R.id.eventDate);
        eventDate.setText(event.getDateTime());



        HashMap<String, Attendee> attendees = event.getAttendees();
        if(attendees != null) {
            attendeesTV.setText(String.valueOf(attendees.size()));
            int attended = 0, unAttended = 0, pending = 0;
            for (Map.Entry<String, Attendee> entry : attendees.entrySet()) {
                String key = entry.getKey();
                Attendee attendee = entry.getValue();
                switch (attendee.getStatus())
                {
                    case "PENDING" :
                        pending++;
                        break;
                    case "ATTENDED" :
                        attended++;
                        break;
                    case "UNATTENDED" :
                        unAttended++;
                        break;
                }
            }
            attendedTV.setText(String.valueOf(attended));
            unAttendedTV.setText(String.valueOf(unAttended));
            pendingTV.setText(String.valueOf(pending));
        }
        return view;

    }

    public String getKey(int position)
    {
        return items.get(position).getKey();
    }
}
