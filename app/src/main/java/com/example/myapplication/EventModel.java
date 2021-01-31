package com.example.myapplication;

import android.provider.CalendarContract;

public class EventModel {

    private String eventName;
    private String eventDescription;
    private String eventLocation;
    private String eventDate;
    private String eventTime;

    public EventModel(String ena, String ede, String elo, String eda, String eti){
        this.eventName = ena;
        this.eventDescription = ede;
        this.eventLocation = elo;
        this.eventDate = eda;
        this.eventTime = eti;
    }

    public String getEventName() { return eventName; }

    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventDescription() { return eventDescription; }

    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }

    public String getEventLocation() { return eventLocation; }

    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }

    public String getEventDate() { return eventDate; }

    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getEventTime() { return eventTime; }

    public void setEventTime(String eventTime) { this.eventTime = eventTime; }
}
