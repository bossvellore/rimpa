package com.dsa.rimpark;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dsa.rimpark.FireBaseSvr.AttendeeFBDB;
import com.dsa.rimpark.model.Attendee;
import com.dsa.rimpark.model.EventModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

import static android.graphics.Color.rgb;

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
    public void setItems(List<DataSnapshot> items){this.items=items;}
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
        TextView mobileTV=(TextView)view.findViewById(R.id.mobileTV);
        String mobile=String.valueOf(attendee.getMobile());
        mobileTV.setText(mobile);
        TextView emailTv=(TextView)view.findViewById(R.id.emailTv);
        emailTv.setText(attendee.getEmail());
        TextView companyTv=(TextView)view.findViewById(R.id.companyTv);
        companyTv.setText(attendee.getCompany());
        TextView notesTv=(TextView)view.findViewById(R.id.notesTv);
        notesTv.setText(attendee.getNotes());
        CardView cardViewList = (CardView) view.findViewById(R.id.attendeItemCard);

        Button setUnAttendedBtn = (Button)view.findViewById((R.id.setUnAttendedBtn));
        Button setAttendedBtn = (Button)view.findViewById(R.id.setAttendedBtn);

        switch (attendee.getStatus().toString())
        {
            case "ATTENDED" :
                setAttendedBtn.setBackgroundColor(Color.parseColor("#26A69A"));
                setUnAttendedBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cardViewList.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case "UNATTENDED" :
                setUnAttendedBtn.setBackgroundColor(Color.parseColor("#FFA726"));
                setAttendedBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cardViewList.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case "PENDING" :
                cardViewList.setCardBackgroundColor(Color.parseColor("#90CAF9"));
                setUnAttendedBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                setAttendedBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
        }
        setAttendedBtn.setOnClickListener(new StatusChangeButtonClickListener(items.get(position).getKey(),
                "ATTENDED", setAttendedBtn, setUnAttendedBtn, attendee.getStatus()));
        setUnAttendedBtn.setOnClickListener(new StatusChangeButtonClickListener(items.get(position).getKey(),
                "UNATTENDED", setAttendedBtn, setUnAttendedBtn, attendee.getStatus()));
        return view;
    }
}

class StatusChangeButtonClickListener implements View.OnClickListener
{
    String attendeeKey, newStatus, previousStatus;
    Button setAttendedBtn,setUnAttendedBtn;

    public StatusChangeButtonClickListener(String attendeeKey, String newStatus, Button setAttendedBtn, Button setUnAttendedBtn, String previousStatus)
    {
        this.attendeeKey=attendeeKey;
        this.newStatus=newStatus;
        this.setAttendedBtn = setAttendedBtn;
        this.setUnAttendedBtn = setUnAttendedBtn;
        this.previousStatus = previousStatus;
    }
    @Override
    public void onClick(View v) {
        AttendeeFBDB attendeeFBDB=new AttendeeFBDB(EventAttendeeActivity.eventKey);
        final String finalStatus;
        if(newStatus.equals(previousStatus))
        {
            finalStatus = "PENDING";
        }
        else{
            finalStatus = newStatus;
        }

        attendeeFBDB.getReference().child(attendeeKey).child("status").setValue(finalStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                switch (finalStatus)
                {
                    case "ATTENDED" :
                        setAttendedBtn.setBackgroundColor(Color.parseColor("#26A69A"));
                        setUnAttendedBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "UNATTENDED" :
                        setUnAttendedBtn.setBackgroundColor(Color.parseColor("#FFA726"));
                        setAttendedBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "PENDING":
                        setUnAttendedBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        setAttendedBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        break;
                }
                previousStatus = finalStatus;
            }
        });

    }
}
