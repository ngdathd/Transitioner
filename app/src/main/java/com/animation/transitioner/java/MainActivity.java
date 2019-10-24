package com.animation.transitioner.java;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.animation.transitioner.R;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout startingView;
    ConstraintLayout endingView;
    ConstraintLayout screen;
    AppCompatSeekBar seekBar;
    AppCompatButton button;

    Transitioner transitioner;
    float oldX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startingView = findViewById(R.id.starting_view);
        endingView = findViewById(R.id.ending_view);
        screen = findViewById(R.id.screen);
        seekBar = findViewById(R.id.seekBar);
        button = findViewById(R.id.button);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transitioner = new Transitioner(startingView, endingView, new Transitioner.Callback() {
                    @Override
                    public void onPercentChanged(float percent) {

                    }
                });

                oldX = 0f;
                Point size = new Point();
                WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getSize(size);
                final float screenSize = size.x;
                screen.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN: {
                                oldX = motionEvent.getX();
                                return true;
                            }
                            case MotionEvent.ACTION_MOVE: {
                                transitioner.setProgress((motionEvent.getX() - oldX) / screenSize);
                                return true;
                            }
                            default:
                                return true;
                        }
                    }
                });

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        transitioner.setProgress(i);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        transitioner.animateTo(0f, 500, new BounceInterpolator());
                        transitioner.setCallback(new Transitioner.Callback() {
                            @Override
                            public void onPercentChanged(float percent) {
                                seekBar.setProgress((int) percent * 100);
                            }
                        });
                    }
                });
            }
        }, 2000);
    }
}
