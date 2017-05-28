package pl.lodz.p.eletel.coffemenu;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.lodz.p.eletel.coffemenu.mapObjects.TramApiObject;
import pl.lodz.p.eletel.coffemenu.mapObjects.TramApiResult;

/**
 * Created by student_pl on 14.05.17.
 */

public class DataStore extends Fragment {

    public DataStore() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(logTag, "onCreate called");
        GsonBuilder builder = new GsonBuilder();
        gson = builder.setDateFormat("YYYY-MM-dd'T'HH:mm:ss").create();
        observers = new ArrayList<>();
        webDownloader = new WebDownloader();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;
                while (running) {
                    try {
                        byte[] byteArray = webDownloader.downloadFileSync(UM_URL);
                        Log.e(logTag, "Tablica: " + byteArray.length);
                        String json = new String(byteArray);
                        Log.e(logTag, "String: " + json.length());
                        Log.e(logTag, "Downloaded: " + json);
                        Log.e(logTag, "Starting JSON parser");
                        JsonReader rdr = new JsonReader(
                                new StringReader(json));
                        rdr.setLenient(true);
                        TramApiResult apiObjects = gson.fromJson(rdr, TramApiResult.class);
                        synchronized (observers) {
                            for (DataStoreObserver observer : observers) {
                                observer.onNewDataAvailable(Arrays.asList(apiObjects.result));
                            }
                        }
                    } catch(Exception e){
                        Log.e(logTag, e.getMessage());
                    }
                    try {
                        Thread.sleep(period);
                    } catch (InterruptedException e) {
                        Log.e(logTag, e.getMessage());
//                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private long period;
    private boolean running;

    public void start(long period) {
        if (!running) {
            this.period = period;
            Log.i(logTag, "Starting data download thread");
            thread.start();
        }
    }

    public void stop() {
        // will stop after maximum time of 1 period
        running = false;
    }


    private static final String UM_URL = "https://api.um.warszawa.pl/api/action/wsstore_get/?id=c7238cfe-8b1f-4c38-bb4a-de386db7e776&apikey=";

    private static final String logTag = "DATA-STORE";
    WebDownloader webDownloader;
    Thread thread;
    Gson gson;
    List<DataStoreObserver> observers;

    public void registerObserver(DataStoreObserver observer) {
        synchronized (observers) {
            if (!observers.contains(observer)) {
                observers.add(observer);
            }
        }
    }

    public void unregisterObserver(DataStoreObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }


    public static interface DataStoreObserver {

        void onNewDataAvailable(List<TramApiObject> objects);
    }
}
