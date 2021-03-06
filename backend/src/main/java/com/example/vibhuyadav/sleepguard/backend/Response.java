package com.example.vibhuyadav.sleepguard.backend;

/**
 * Created by zohaibakmal on 3/4/2015.
 */

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Response {
    @Id Long id;
    String mDeviceId;
    Long mTimeStamp;

    public Double getAverage() {
        return this.average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    Double average;

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    String requestId;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getmDeviceId() {
        return this.mDeviceId;
    }

    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public Long getmTimeStamp() {
        return this.mTimeStamp;
    }

    public void setmTimeStamp(Long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }
}

