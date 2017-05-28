package pl.lodz.p.eletel.coffemenu.mapObjects;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;

/**
 * Created by student_pl on 28.05.17.
 */

public class MyMapMarker {

    private Marker marker;
    private TramApiObject tram;

    public MyMapMarker(Marker marker, TramApiObject tram) {
        this.marker = marker;
        this.tram = tram;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public TramApiObject getTram() {
        return tram;
    }

    public void setTram(TramApiObject tram) {
        this.tram = tram;
    }


    public void update(TramApiObject tram){

        this.tram = tram;
        marker.setPosition(new LatLng(tram.getLat(), tram.getLon()));
        marker.setTitle(getMarkerTittle(tram));

    }


    public static String getMarkerTittle(TramApiObject tram){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String firstLine =  tram.getFirstLine().trim();
        String lines = tram.getLines().trim();
        String ret = sdf.format(tram.getTime())
                + " "
                + (firstLine.equals(lines) ? firstLine : lines);


        return ret;
    }

}
