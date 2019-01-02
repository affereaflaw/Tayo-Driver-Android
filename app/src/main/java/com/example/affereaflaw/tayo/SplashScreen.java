package com.example.affereaflaw.tayo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashScreen extends AppCompatActivity {

    //Splash Screen Timer
    private static int splash_timeout = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            //buat method SplashScreen

            @Override
            public void run() {

                //method berjalan sekali setelah timer selesai, lalu menuju LoginActivity
                Intent x = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(x);

                finish();
            }
        }, splash_timeout);
    }
}
