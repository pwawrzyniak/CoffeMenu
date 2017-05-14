package pl.lodz.p.eletel.coffemenu;

import android.os.Looper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Created by student_pl on 07.05.17.
 */

public class WebDownloader {


    public interface OnDownloadedListener {
        void onDownloadFinished(int status, byte[] item, URL url);
    }

    public void beginFileDownload(final String uri, final OnDownloadedListener listener) throws IOException {
        if (uri != null) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("WEB", "Downloading resource: " + uri);
                        Log.i("THREAD", Thread.currentThread().getName());
                        URI u = URI.create(uri);
                        URL url = null;

                        url = u.toURL();
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        connection.connect();

                        int responseCode = connection.getResponseCode();

                        if (responseCode == 200) {

                            InputStream is = connection.getInputStream();
                            int contentLength = connection.getContentLength();
                            byte[] ret;
                            Log.i("WEB", "Resource conten-length: " + contentLength);
                            if (contentLength != -1) {
                                // znamy długość odpowiedzi
                                ret = new byte[contentLength];
                                BufferedInputStream bis = new BufferedInputStream(is);
//                                int rd = bis.read(ret);

                                int count = 0;
                                byte[] buffer = new byte[2048];
                                int progress = 0;
                                while ((count = bis.read(buffer, 0, 2048)) != -1) {
                                    System.arraycopy(buffer, 0, ret, progress, count);
                                    progress += count;
                                }
                            } else {
                                //długość odpowiedzi chunked (nieznana)
                                byte[] buffer = new byte[1024];
                                BufferedInputStream bis = new BufferedInputStream(is);
                                int count = 0;
                                ret = new byte[2048];
                                int progress = 0;
                                while ((count = bis.read(buffer, 0, 1024)) != -1) {
                                    System.arraycopy(buffer, 0, ret, progress, count);
                                    ret = Arrays.copyOf(ret, ret.length + 1024);
                                    progress += count;

                                }

                            }
                            if (ret != null) {
                                listener.onDownloadFinished(
                                        responseCode,
                                        ret,
                                        url
                                );
                            } else {
                                listener.onDownloadFinished(
                                        204,
                                        null,
                                        url
                                );
                            }

                        } else {
                            listener.onDownloadFinished(
                                    responseCode,
                                    null,
                                    url
                            );
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            );
            thread.start();
        }
    }

    Object lock;

    public byte[] downloadFileSync(final String uri){

        lock = new Object();

        final byte[][] mybytes = {new byte[1]};


        synchronized (lock){
            try {
                beginFileDownload(uri, new OnDownloadedListener() {
                    @Override
                    public void onDownloadFinished(int status, byte[] item, URL url) {
                        synchronized (lock){
                            Log.i("WEB", "Sync download, status=" + status);
                            mybytes[0] = item;
                            lock.notifyAll();
                            Log.i("THREAD", Thread.currentThread().getName());
                        }
                    }
                });

                Log.i("THREAD", Thread.currentThread().getName());
                lock.wait();
                Log.i("THREAD", Thread.currentThread().getName());
                return mybytes[0];
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
