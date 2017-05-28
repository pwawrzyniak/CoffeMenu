package pl.lodz.p.eletel.coffemenu;

import android.app.FragmentManager;
import android.provider.ContactsContract;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.lodz.p.eletel.coffemenu.DataStore.DataStoreObserver;
import pl.lodz.p.eletel.coffemenu.mapObjects.MyMapMarker;
import pl.lodz.p.eletel.coffemenu.mapObjects.TramApiObject;

/**
 * Created by student_pl on 14.05.17.
 */

public class MyMapFragment extends MapFragment implements DataStoreObserver {

    GoogleMap map;

    Map<String, MyMapMarker> markers;

    SimpleDateFormat sdf;

    @Override
    public void onResume() {
        super.onResume();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.229848, 21.011661),
                        (float) (map.getCameraPosition().zoom * 6.0)));
//                map.moveCamera(CameraUpdateFactory.zoomBy(4.0F));
            }
        });

        markers = new HashMap<>();
sdf = new SimpleDateFormat("HH:mm:ss");
        FragmentManager manager = getFragmentManager();
        DataStore dataStore = (DataStore) manager.findFragmentByTag(FragmentTags.DATASTORE);
        dataStore.registerObserver(this);

        dataStore.start(10000);
    }


    @Override
    public void onNewDataAvailable(final List<TramApiObject> objects) {

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (TramApiObject tram : objects) {

                    String key = tram.getBrigade() + "_" + tram.getFirstLine();

                    if (markers.containsKey(key)) {
                        markers.get(key)
                                .getMarker()
                                .setPosition(new LatLng(tram.getLat(), tram.getLon()));
                    } else {
                        MarkerOptions opts = new MarkerOptions()
                                .position(new LatLng(tram.getLat(), tram.getLon()))

                                .title( sdf.format(tram.getTime())
                                        + " "
                                        + tram.getFirstLine()
                                        + ","
                                        + tram.getLines());

                        Marker marker = map.addMarker(opts);

                        markers.put(key, new MyMapMarker(marker, tram));
                    }
                }
            }
        });


    }
}



