package com.example.vibhuyadav.sleepguard.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The Objectify object model for device registrations we are persisting
 */
@Entity
public class SleepReceiverRecord {

    @Id
    Long id;

    @Index
    private String timestamp;
    // you can add more fields...

    public SleepReceiverRecord() {
    }

    public String getTimestampId() {
        return timestamp;
    }

    public void setTimestampId(String timestamp) {
        this.timestamp = timestamp;
    }
}