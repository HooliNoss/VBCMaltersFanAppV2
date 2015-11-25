package com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs;

import org.ksoap2.serialization.SoapObject;

import java.util.Objects;

/**
 * Created by stefan.bachmann on 17.11.2015.
 */
public class Result {

    public String getmRank() {
        return mRank;
    }

    public void setmRank(String mRank) {
        this.mRank = mRank;
    }

    public String getmTeam() {
        return mTeam;
    }

    public void setmTeam(String mTeam) {
        this.mTeam = mTeam;
    }

    public String getmNumberOfGames() {
        return mNumberOfGames;
    }

    public void setmNumberOfGames(String mNumberOfGames) {
        this.mNumberOfGames = mNumberOfGames;
    }

    public String getmPoints() {
        return mPoints;
    }

    public void setmPoints(String mPoints) {
        this.mPoints = mPoints;
    }

    public SoapObject getmSoapObject() {
        return mSoapObject;
    }

    public void setmSoapObject(SoapObject mSoapObject) {
        this.mSoapObject = mSoapObject;
    }

    public String mRank;
    public String mTeam;
    public String mNumberOfGames;
    public String mPoints;
    public SoapObject mSoapObject;

}
