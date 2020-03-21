package au.edu.jcu.cp3406.wk4stopwatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.*;


import java.util.Objects;

public class StopwatchActivity extends AppCompatActivity {
    boolean isRunning = false;
    Stopwatch stopwatch = new Stopwatch();
    final Handler handler = new Handler();
    Runnable runnable;
    Button controlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRunning = false;
        if (savedInstanceState == null) {
            stopwatch = new Stopwatch();
        } else {
            stopwatch = new Stopwatch(Objects.requireNonNull(savedInstanceState.getString("stopwatch")));
            boolean running = savedInstanceState.getBoolean("running");
            stopwatch.setSpeed(savedInstanceState.getInt("speed"));
            if (running) {
                enableStopwatch();
            }
        }
        updateDisplay();

        controlButton = findViewById(R.id.startButton);
        controlButton.setTag(1);
        controlButton.setText("START");
        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                final int status =(Integer) v.getTag();
                if(status == 1) {
                    enableStopwatch();
                    controlButton.setText("STOP");
                    v.setTag(0);
                } else {
                    disableStopwatch();
                    controlButton.setText("START");
                    v.setTag(1);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("stopwatch", stopwatch.toString());
        outState.putBoolean("running", isRunning);
        outState.putInt("speed", stopwatch.getSpeed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SettingsActivity.SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    stopwatch.setSpeed(data.getIntExtra("speed", 1000));
                }
            }
        }
    }

    public void resetClicked(View view) {
        disableStopwatch();
        stopwatch.reset();
        updateDisplay();
        handler.removeCallbacks(runnable);

        controlButton.setText("START");
        controlButton.setTag(1);
    }

    public void settingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("speed", stopwatch.getSpeed());
        startActivityForResult(intent, SettingsActivity.SETTINGS_REQUEST);
    }

    private void enableStopwatch() {
        isRunning = true;
        //handler.removeCallbacks();
        handler.post(runnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    stopwatch.tick();
                    System.out.println("TICK TICK TICK TICK");
                    updateDisplay();
                }
                handler.postDelayed(this, stopwatch.getSpeed());
            }
        });
    }

    private void disableStopwatch() {
        isRunning = false;
        handler.removeCallbacks(runnable);
    }

    private void updateDisplay() {
        final TextView timer = findViewById(R.id.timer);
        timer.setText(stopwatch.toString());
    }
}
