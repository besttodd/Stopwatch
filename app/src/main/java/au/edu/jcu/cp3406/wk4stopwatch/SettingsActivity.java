package au.edu.jcu.cp3406.wk4stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    public static int SETTINGS_REQUEST = 222;
    public static int speed = 0;
    public TextView selectedSpeed;
    public SeekBar speedBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        speed = getIntent().getExtras().getInt("speed");

        selectedSpeed = (TextView) findViewById(R.id.selectedSpeed);
        selectedSpeed.setText(Integer.toString(speed));

        speedBar = findViewById(R.id.speedBar);
        speedBar.setProgress(speed);

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedSpeed.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void doneClicked(View view) {
        EditText input = findViewById(R.id.speedInput);
        try {
            speed = Integer.parseInt(input.getText().toString());
        } catch (NumberFormatException e) {
            speed = speedBar.getProgress();
        }

        Intent intent = new Intent();
        intent.putExtra("speed", speed);
        setResult(RESULT_OK, intent);
        finish();
    }
}
