package com.star.game;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.star.schulte.model.SchulteListener;
import com.star.schulte.view.SchulteView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SchulteView schulteView = findViewById(R.id.schulteView);
        schulteView.setListener(new SchulteListener() {
            @Override
            public void onCountDown(long time) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onTapError(int index, int currentIndex) {

            }

            @Override
            public void onProgress(int index, int maxIndex) {

            }

            @Override
            public void onFinish(int totalTap, int correctTap) {
                Log.e("1", correctTap + "/" + totalTap);
            }
        });
        schulteView.start();
    }
}
