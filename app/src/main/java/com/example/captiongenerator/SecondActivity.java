package com.example.captiongenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SecondActivity extends AppCompatActivity {
    ImageView imageView;
    TextView caption;

    int checker; //to check whether a camera button is clicked or a gallery

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //to enable a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.displayImg);
        caption = (TextView) findViewById(R.id.caption);

        Intent intent = getIntent(); //getting intent back from FirstActivity
        checker = (int) intent.getIntExtra("flag", 0);

        if (checker == 0) {
            Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");

            //it will create a file of image and direct it to server
            SyncHttpClient client = new SyncHttpClient();
            RequestParams params = new RequestParams();
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/Files");

            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
//                        return null;
                }
            }
            // Create a media file name
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
            File mediaFile;
            String mImageName="MI_"+ timeStamp +".jpg";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
            System.out.println(mediaFile.getPath());

            try {
                FileOutputStream fOut = new FileOutputStream(mediaFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                //LOG.i(null, "Save file error!");
                //return false;
            }


            //params.put("text", "some string");
            try {
                params.put("image", mediaFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            client.post("http://54.237.8.245:8080/predict", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    System.out.println(statusCode);
                    System.out.println(responseString);
                    caption.setText(responseString);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    Log.d("my",responseString);
                    System.out.println(statusCode);
                    System.out.println(responseString);
                    caption.setText(responseString);
                }
            });
            imageView.setImageBitmap(bitmap);
        }
        if (checker == 1) {

            Uri myUri = Uri.parse(intent.getStringExtra("imageUri"));
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SyncHttpClient client = new SyncHttpClient();
            RequestParams params = new RequestParams();
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/Files");

            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
//                        return null;
                }
            }
            // Create a media file name
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
            File mediaFile;
            String mImageName="MI_"+ timeStamp +".jpg";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
            System.out.println(mediaFile.getPath());

            try {
                FileOutputStream fOut = new FileOutputStream(mediaFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                //LOG.i(null, "Save file error!");
                //return false;
            }


            //params.put("text", "some string");
            try {
                params.put("image", mediaFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            client.post("http://54.237.8.245:8080/predict", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    System.out.println(statusCode);
                    System.out.println(responseString);
                    caption.setText(responseString);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    Log.d("my",responseString);
                    System.out.println(statusCode);
                    System.out.println(responseString);
                    caption.setText(responseString);
                }
            });
            imageView.setImageURI(myUri);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.help:
                Intent i1 = new Intent(this, HelpActivity.class);
                startActivity(i1);
                return true;
            case R.id.feedback:
                Intent i2 = new Intent(this, FeedbackActivity.class);
                startActivity(i2);
                return true;
            case R.id.aboutUs:
                Intent i = new Intent(this, AboutUsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}