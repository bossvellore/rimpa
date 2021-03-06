package com.dsa.rimpark.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amalroshand on 09/05/17.
 */

public class EventModel {
    private String title;
    private String description;
    private String dateTime;
    private String status;
    private HashMap<String, Attendee> attendees;
    private HashMap<String, String> users;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String,Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(HashMap<String, Attendee> attendees) {
        this.attendees = attendees;
    }

    public HashMap<String, String> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, String> users) {
        this.users = users;
    }
}
