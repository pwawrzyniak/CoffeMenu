package pl.lodz.p.eletel.coffemenu.mapObjects;

import java.util.Date;

/**
 * Created by student_pl on 14.05.17.
 */

public class TramApiObject {

    private String Status, Lines, FirstLine, Brigade;
    private boolean lowFloor;
    private double Lat, Lon;
    private Date Time;

    public TramApiObject(){
    }

    public TramApiObject(String brigade, String firstLine, double lat, String lines, double lon, boolean lowFloor, String status, Date time) {
        Brigade = brigade;
        FirstLine = firstLine;
        Lat = lat;
        Lines = lines;
        Lon = lon;
        this.lowFloor = lowFloor;
        Status = status;
        Time = time;
    }

    public String getBrigade() {
        return Brigade;
    }

    public void setBrigade(String brigade) {
        Brigade = brigade;
    }

    public String getFirstLine() {
        return FirstLine;
    }

    public void setFirstLine(String firstLine) {
        FirstLine = firstLine;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public String getLines() {
        return Lines;
    }

    public void setLines(String lines) {
        Lines = lines;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public boolean isLowFloor() {
        return lowFloor;
    }

    public void setLowFloor(boolean lowFloor) {
        this.lowFloor = lowFloor;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
