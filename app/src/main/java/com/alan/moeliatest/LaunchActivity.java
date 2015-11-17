package com.alan.moeliatest;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * This is the launch activity, here we'll download the image and display it, and we'll launch the main activity
 * after a 5 second delay.
 */
public class LaunchActivity extends AppCompatActivity {

    private ImageView launchImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch);

        //We first load our image
        initializeScreen();

        //And start our timer so that the main activity will be launched after a 5 second delay
        startTimer();

    }

    private void initializeScreen(){

        launchImage = (ImageView)findViewById(R.id.launch_image);

        //Image handling is easier with Picasso!
        Picasso.with(this).load(getString(R.string.launchImageUrl)).into(launchImage);

    }

    /**
     * This is a simple method, it creates a handler and after a 5 second delay it will launch the main activity and close this activity
     * that way we make sure that if the user taps the back button it doesn't return to this screen.
     */
    private void startTimer(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainListIntent = new Intent(getApplicationContext(),MainListActivity.class);

                startActivity(mainListIntent);

                finish();

            }
        }, 5000);

    }
}
