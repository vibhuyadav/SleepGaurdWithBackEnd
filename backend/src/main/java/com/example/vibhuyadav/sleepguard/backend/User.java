package com.example.vibhuyadav.sleepguard.backend;

/**
 * Created by zohaibakmal on 3/4/2015.
 */

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User {
    @Id String mDeviceId;
    Double mLongitude;
    Double mLatitude;
    Boolean mStatus;
    String mGroupId;

    public User(){

    }

    public String getMyId(){
        return mDeviceId;
    }

    public void setDeviceId(String id){
        this.mDeviceId = id;
    }

    public String getMyGroupId(){
        return mGroupId;
    }

    public void setGroupId(String id){
        this.mGroupId = id;
    }

    public Double getLongitude(){
        return mLongitude;
    }

    public void setLongitude(Double longitude){
        this.mLongitude = longitude;
    }

    public Double getLatitude(){
        return this.mLatitude;
    }

    public void setLatitude(Double latitude){
        this.mLatitude = latitude;
    }

    public Boolean getStatus(){
        return this.mStatus;
    }

    public void setStatus(Boolean status){
        this.mStatus = status;
    }

}

