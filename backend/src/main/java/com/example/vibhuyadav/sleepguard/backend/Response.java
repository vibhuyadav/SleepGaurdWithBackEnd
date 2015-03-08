package com.example.vibhuyadav.sleepguard.backend;

/**
 * Created by zohaibakmal on 3/4/2015.
 */

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Response {
    @Id Long id;
    String mDeviceId;
    Long mTimeStamp;
    @Parent Key<Request> parentRequest;

    public Key<Request> getParentRequest() {
        return parentRequest;
    }

    public void setParentRequest(Key<Request> parentRequest) {
        this.parentRequest = parentRequest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getmDeviceId() {
        return mDeviceId;
    }

    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public Long getmTimeStamp() {
        return mTimeStamp;
    }

    public void setmTimeStamp(Long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }
}

