package au.edu.jcu.cp3406.wk4stopwatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity {
    boolean isRunning = false;
    Stopwatch stopwatch = new Stopwatch();
    //private int speed = 1000;
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
            stopwatch.setSpeed(savedInstanceState.getInt("speed"));
            System.out.println("onCREATE() CALLED------------------------------------------------");
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
        outState.putInt("speed", stopwatch.getSpeed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SettingsActivity.SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    stopwatch.setSpeed(data.getIntExtra("speed", 1000));
                    System.out.println("SPEED = " + stopwatch.getSpeed()+"-----------------------");
                }
            }
        }
    }

    public void startClicked (View view) {
        enableStopwatch();
        Button start = findViewById(R.id.startButton);
        start.setEnabled(false);
    }

    public void stopClicked(View view) {
        disableStopwatch();
        Button start = findViewById(R.id.startButton);
        start.setEnabled(true);
    }

    public void resetClicked(View view) {
        disableStopwatch();
        stopwatch.reset();
        updateDisplay();
        handler.removeCallbacks(runnable);
        Button start = findViewById(R.id.startButton);
        start.setEnabled(true);
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
