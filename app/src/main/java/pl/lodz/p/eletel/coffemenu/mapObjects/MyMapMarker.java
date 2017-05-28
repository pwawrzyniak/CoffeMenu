package pl.lodz.p.eletel.coffemenu.mapObjects;

import com.google.android.gms.maps.model.Marker;

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
}
