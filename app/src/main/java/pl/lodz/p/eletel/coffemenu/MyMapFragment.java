package pl.lodz.p.eletel.coffemenu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by student_pl on 14.05.17.
 */

public class MyMapFragment extends MapFragment {

    GoogleMap map;

    @Override
    public void onResume() {
        super.onResume();
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
            }
        });


    }






}



