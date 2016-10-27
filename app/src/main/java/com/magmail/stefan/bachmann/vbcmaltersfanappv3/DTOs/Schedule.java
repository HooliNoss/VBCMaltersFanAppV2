package com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by stefan.bachmann on 25.11.2015.
 */
public class Schedule {
    public SoapObject mSoapObject;

    String date;
    String time;
    String teamHome;
    String teamAway;
    String setPointsHome;
    String setPointsAway;
    String hall;
    int[] pointsTeamHome = new int[5];
    int[] pointsTeamAway = new int[5];

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

    public String getSetPointsHome() {
        return setPointsHome;
    }

    public void setSetPointsHome(String setPointsHome) {
        this.setPointsHome = setPointsHome;
    }

    public String getSetPointsAway() {
        return setPointsAway;
    }

    public void setSetPointsAway(String setPointsAway) {
        this.setPointsAway = setPointsAway;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public int[] getPointsTeamHome() {
        return pointsTeamHome;
    }

    public void setPointsTeamHome(int[] pointsTeamHome) {
        this.pointsTeamHome = pointsTeamHome;
    }

    public int[] getPointsTeamAway() {
        return pointsTeamAway;
    }

    public void setPointsTeamAway(int[] pointsTeamAway) {
        this.pointsTeamAway = pointsTeamAway;
    }
}
