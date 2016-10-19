package app.caro.notification_progress_bar;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    int id = 1;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyManager;
    private String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testDownload2();

    }

    private void testDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count;
                try {
                    URL url = new URL("http://cdn01.ib.infobae.com/adjuntos/162/imagenes/014/014/0014014674.jpg");
                    URLConnection conection = url.openConnection();
                    conection.connect();
// getting file length
                    int lenghtOfFile = conection.getContentLength();

// input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

// Output stream to write file
                    OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
// publishing the progress....
// After this onProgressUpdate will be called
                        int progress = (int) ((total * 100) / lenghtOfFile);

// writing data to file
                        output.write(data, 0, count);
                    }

// flushing output
                    output.flush();

// closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    /*Log.e("Error: ", e.getMessage());*/
                }
            }
        }).start();


    }

    private void testDownload2() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int count;
                try {
                    URL url = new URL("https://www.donquijote.org/spanishlanguage/literature/library/quijote/quijote1.pdf");
                    URLConnection conection = url.openConnection();
                    conection.connect();


// getting file length
                    int lenghtOfFile = conection.getContentLength();


// input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    File file = new File(Environment.getExternalStorageDirectory(), "downloadfile2.pdf");
// Output stream to write file
                    OutputStream output = new FileOutputStream(file);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
// publishing the progress....
// After this onProgressUpdate will be called
                        int progress = (int) ((total * 100) / lenghtOfFile);
                        System.out.println("progress = " + progress);
                        //testNotification2(progress, lenghtOfFile);
                        testNotification2(progress, lenghtOfFile);

// writing data to file
                        output.write(data, 0, count);
                    }

// flushing output
                    output.flush();

// closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        ;

    }

    private void testNotification() {
        //original
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr += 5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(id, mBuilder.build()); // cambiando el id, se ccrea una nueva notificacion
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        mNotifyManager.notify(id, mBuilder.build());
                    }
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();

    }

    private void testNotification2(int prog, int size) {
        final int incr = prog;
        final int tam = size;
        final int id = 1;
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher);

        mBuilder.setProgress(tam, incr, false);
        mNotifyManager.notify(id, mBuilder.build());
        mBuilder.setContentText("Download complete")
                .setProgress(0, 0, false);
        mNotifyManager.notify(id, mBuilder.build());
    }
}

