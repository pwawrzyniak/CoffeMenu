package pl.lodz.p.eletel.coffemenu;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends Activity {


    WebDownloader webDownloader;
    MyImageRepository imageRepository;
    MyMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webDownloader = new WebDownloader();
        imageRepository = new MyImageRepository(this);
        mapFragment = new MyMapFragment();


        TabHost host = (TabHost) findViewById(R.id.tabhost);
        host.setup();

        createTab("Mapa", R.id.tab1, host);
        createTab("Lista", R.id.tab2, host);
        createTab("Ustawienia", R.id.tab3, host);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.tab3, new MyPreferenceFragment(), FragmentTags.SETTINGS)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.tab1, mapFragment, FragmentTags.MAP)
                .commit();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.i("MAIN", "Button clicked");
//                    webDownloader.beginFileDownload(
//                            "http://kopernik.alpha.pl/upload/image/aaa.jpg",//"http://www.niedzica.pl/userfiles/image/majowka_w_gorach10.png",
//                            new WebDownloader.OnDownloadedListener() {
//                                @Override
//                                public void onDownloadFinished(int status, final byte[] item, URL url) {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Log.i("MAIN", "Image downloaded");
//                                            ImageView iv = (ImageView)findViewById(R.id.imageView);
//                                            iv.setImageBitmap(BitmapFactory.decodeByteArray(
//                                                    item,0,item.length
//                                            ));
//
//                                        }
//                                    });
//                                }
//                            }
//                    );



                    byte[] item = imageRepository.getImage(
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Radziejow_ratusz.jpg/1200px-Radziejow_ratusz.jpg");
//                                                       "http://static.panoramio.com/photos/original/36586641.jpg");
  //                          "http://kopernik.alpha.pl/upload/image/aaa.jpg");
                    Log.i("MAIN", "Image downloaded");

                    ImageView iv = (ImageView) findViewById(R.id.imageView);
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(
                            item, 0, item.length
                    ));


                    boolean a = true;
                    a = !a;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createTab(String text, int viewId, TabHost host) {
        TabHost.TabSpec tab1 = host.newTabSpec(text);
        tab1.setContent(viewId);
        tab1.setIndicator(text);
        host.addTab(tab1);
    }


}
