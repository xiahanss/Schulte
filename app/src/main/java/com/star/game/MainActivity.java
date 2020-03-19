package com.star.game;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.star.schulte.bean.SchulteGame;
import com.star.schulte.listener.SchulteListener;
import com.star.schulte.view.SchulteView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SchulteView schulteView = findViewById(R.id.schulteView);
        SchulteGame game = new SchulteGame();
        game.setRow(3);
        game.setColumn(3);
        game.setListener(new SchulteListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onTapError(int index, int currentIndex) {

            }

            @Override
            public void onProgress(int index, int maxIndex) {
                Toast.makeText(MainActivity.this, index + "", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFinish(int totalTap, int correctTap) {
                Toast.makeText(MainActivity.this, "Finished", Toast.LENGTH_SHORT).show();

            }
        });
        schulteView.setGame(game);
        schulteView.start();
    }
}
