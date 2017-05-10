package com.dsa.rimpark.model;

import java.util.Date;

/**
 * Created by amalroshand on 09/05/17.
 */

public class EventModel {
    private String title;
    private String description;
    private Date dateTime;

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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
