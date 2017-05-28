package pl.lodz.p.eletel.coffemenu;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pl.lodz.p.eletel.coffemenu.mapObjects.MyMapMarker;
import pl.lodz.p.eletel.coffemenu.mapObjects.TramApiObject;

public class MainActivity extends Activity {


    WebDownloader webDownloader;
    MyImageRepository imageRepository;
    MyMapFragment mapFragment;
    private DataStore dataStore;

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fragmentManager = getFragmentManager();
        ((DataStore) fragmentManager.findFragmentByTag(FragmentTags.DATASTORE))
                .registerObserver(new DataStore.DataStoreObserver() {
                    @Override
                    public void onNewDataAvailable(final List<TramApiObject> objects) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String[] listItems = new String[objects.size()];
                                int i = 0;
                                ListView listView = (ListView) findViewById(R.id.listView);
                                ((ArrayAdapter<String>) listView.getAdapter()).clear();
                                for (TramApiObject obj : objects) {
//                                    listItems[i++] = MyMapMarker.getMarkerTittle(obj);
                                    ((ArrayAdapter<String>) listView.getAdapter()).add(MyMapMarker.getMarkerTittle(obj));
                                }



//                                ((ArrayAdapter<String>) listView.getAdapter()).addAll(listItems);
                            }
                        });


                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webDownloader = new WebDownloader();
        imageRepository = new MyImageRepository(this);
        mapFragment = new MyMapFragment();
        dataStore = new DataStore();

        TabHost host = (TabHost) findViewById(R.id.tabhost);
        host.setup();

        createTab("Mapa", R.id.tab1, host);
        createTab("Lista", R.id.tab2, host);
        createTab("Ustawienia", R.id.tab3, host);

        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.tab3, new MyPreferenceFragment(), FragmentTags.SETTINGS)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.tab1, mapFragment, FragmentTags.MAP)
                .add(dataStore, FragmentTags.DATASTORE)
                .commit();

        List<String> listItems = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);


    }

    private void createTab(String text, int viewId, TabHost host) {
        TabHost.TabSpec tab1 = host.newTabSpec(text);
        tab1.setContent(viewId);
        tab1.setIndicator(text);
        host.addTab(tab1);
    }


}
