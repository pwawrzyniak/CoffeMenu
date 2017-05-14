package pl.lodz.p.eletel.coffemenu;

import android.app.Fragment;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.lodz.p.eletel.coffemenu.mapObjects.TramApiObject;

/**
 * Created by student_pl on 14.05.17.
 */

public class DataStore extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        webDownloader = new WebDownloader();
    }

    @Override
    public void onResume() {
        super.onResume();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String json = new String(webDownloader.downloadFileSync(UM_URL));
                TramApiObject[] apiObjects = gson.fromJson(json, TramApiObject[].class);
                for (DataStoreObserver observer: observers) {

                    observer.onNewDataAvailable(Arrays.asList(apiObjects));

                }

            }
        });
    }

    private static final String UM_URL = "https://api.um.warszawa.pl/api/action/wsstore_get/?id=c7238cfe-8b1f-4c38-bb4a-de386db7e776&apikey=API_KEY";


    WebDownloader webDownloader;
    Thread thread;
    Gson gson;
    List<DataStoreObserver> observers;

    public void registerObserver(DataStoreObserver observer){

    }

    public void unregisterObserver(DataStoreObserver observer){

    }


    public static interface DataStoreObserver{

        void onNewDataAvailable(List<TramApiObject> objects);
    }
}
