package com.egci428.a10074.fortunecookies;

import java.util.Date;

/**
 * Created by 6272user on 10/21/2016 AD.
 */
public class Comment {
    private int CookieID;
    private String date;
    private String PositionID;

    public int getCookieID() {return CookieID;}
    public String getDate() {return date;}
    public String getPositionID() {return PositionID;}

    public void setCookieID (int CookieID) { this.CookieID = CookieID;}
    public void setDate(String date) { this.date = date;}
    public void setPositionID(String PositionID) {this.PositionID = PositionID;}


    // Will be used by the ArrayAdapter in the ListView
    /*
    @Override
    public String toString() {
        return CookieID;
    }
    */
}