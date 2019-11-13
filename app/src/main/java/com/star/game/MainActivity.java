package com.star.game;

import android.os.Bundle;
import android.util.Log;

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
        schulteView.setGame(game);
        schulteView.start();
    }
}
