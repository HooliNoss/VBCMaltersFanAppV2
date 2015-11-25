package com.magmail.stefan.bachmann.vbcmaltersfanappv3.DTOs;

/**
 * Created by stefan.bachmann on 16.11.2015.
 */
public class Team {

    String m_Name;
    String m_League;
    String m_ImgSrc;
    int m_GroupId;
    int m_TeamId;

    public int getM_GroupId() {
        return m_GroupId;
    }

    public void setM_GroupId(int m_GroupId) {
        this.m_GroupId = m_GroupId;
    }

    public int getM_TeamId() {
        return m_TeamId;
    }

    public void setM_TeamId(int m_TeamId) {
        this.m_TeamId = m_TeamId;
    }

    public String getM_Name() {
        return m_Name;
    }

    public void setM_Name(String m_Name) {
        this.m_Name = m_Name;
    }

    public String getM_League() {
        return m_League;
    }

    public void setM_League(String m_League) {
        this.m_League = m_League;
    }

    public String getM_ImgSrc() {
        return m_ImgSrc;
    }

    public void setM_ImgSrc(String m_ImgSrc) {
        this.m_ImgSrc = m_ImgSrc;
    }


}
