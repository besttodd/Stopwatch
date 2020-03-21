package au.edu.jcu.cp3406.wk4stopwatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity {
    boolean isRunning = false;
    Stopwatch stopwatch = new Stopwatch();
    private int speed = 1000;
    final Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRunning = false;
        if (savedInstanceState == null) {
            stopwatch = new Stopwatch();
        } else {
            stopwatch = new Stopwatch(savedInstanceState.getString("stopwatch"));
            boolean running = savedInstanceState.getBoolean("running");
            int currentSpeed = savedInstanceState.getInt("speed");
            if (running) {
                enableStopwatch();
            }
        }
        updateDisplay();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("stopwatch", stopwatch.toString());
        outState.putBoolean("running", isRunning);
        outState.putInt("speed", speed);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SettingsActivity.SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    speed = data.getIntExtra("speed", 1000);
                    System.out.println("SPEED = " + speed);
                }
            }
        }
    }

    public void startClicked (View view) {
        enableStopwatch();
    }

    public void stopClicked(View view) {
        disableStopwatch();
    }

    public void resetClicked(View view) {
        disableStopwatch();
        stopwatch.reset();
        updateDisplay();
        handler.removeCallbacks(runnable);
    }

    public void settingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
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
                handler.postDelayed(this, speed);
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
