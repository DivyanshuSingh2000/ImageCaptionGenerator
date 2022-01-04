package com.example.captiongenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {
    Button cButton;
    Button gButton;
    ImageView img;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 100;
    int count = 0;

    //this method is to make user, press twice to exit
    @Override
    public void onBackPressed() {
        count++;
        if (count == 1)
            Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT).show();
        if (count == 2)
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        cButton = findViewById(R.id.camera);
        gButton = findViewById(R.id.gallery);
        img = (ImageView) findViewById(R.id.displayImg);

        cButton.setOnClickListener(v -> dispatchTakePictureIntent());

        gButton.setOnClickListener(v -> openGallery());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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

    // function to call system's camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    //Function to call system's (Gallery)
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        try {
            startActivityForResult(gallery, PICK_IMAGE);
        } catch (ActivityNotFoundException e) {
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //for camera result
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle b = data.getExtras();
            Bitmap imageBitmap = (Bitmap) b.get("data");

            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("BitmapImage", imageBitmap);
            intent.putExtra("flag", 0);
            startActivity(intent);
        }

        //for gallery result
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            Uri imageUri = data.getData();

            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("imageUri", imageUri.toString());
            intent.putExtra("flag", 1);
            startActivity(intent);
        }
    }

}