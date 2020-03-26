package com.brauma.withinyourmeans.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.brauma.withinyourmeans.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    Animation top, middle, bottom, image;
    ImageView logo;
    TextView within, your, means;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();

        setContentView(R.layout.activity_splash_screen);


        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        middle = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        image = AnimationUtils.loadAnimation(this, R.anim.image_animation);

        within = findViewById(R.id.textView1);
        your = findViewById(R.id.textView2);
        means = findViewById(R.id.textView3);
        logo = findViewById(R.id.logo);

        within.setAnimation(top);
        your.setAnimation(middle);
        means.setAnimation(bottom);
        logo.setAnimation(image);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },  SPLASH_SCREEN);
    }
}
