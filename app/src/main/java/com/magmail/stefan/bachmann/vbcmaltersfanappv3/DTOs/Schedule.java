package com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by stefan.bachmann on 25.11.2015.
 */
public class Schedule {

    String date;
    String time;
    String teamHome;
    String teamAway;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeamHome() {
        return teamHome;
    }

    public void setTeamHome(String teamHome) {
        this.teamHome = teamHome;
    }

    public String getTeamAway() {
        return teamAway;
    }

    public void setTeamAway(String teamAway) {
        this.teamAway = teamAway;
    }

    public SoapObject getmSoapObject() {
        return mSoapObject;
    }

    public void setmSoapObject(SoapObject mSoapObject) {
        this.mSoapObject = mSoapObject;
    }

    public SoapObject mSoapObject;
}
