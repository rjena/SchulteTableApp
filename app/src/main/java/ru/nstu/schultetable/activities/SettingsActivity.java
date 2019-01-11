package ru.nstu.schultetable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import ru.nstu.schultetable.R;

import static ru.nstu.schultetable.activities.MainActivity.changeMATheme;
import static ru.nstu.schultetable.activities.MainActivity.getSettings;
import static ru.nstu.schultetable.activities.MainActivity.setSettings;
import static ru.nstu.schultetable.activities.ReadyActivity.changeRATheme;
import static ru.nstu.schultetable.activities.ResultActivity.changeRESATheme;
import static ru.nstu.schultetable.activities.TestActivity.changeTATheme;

public class SettingsActivity extends AppCompatActivity {
    Switch theme;
    Switch timer;
    Switch counter;
    Switch sound;
    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        theme = findViewById(R.id.themeS);
        timer = findViewById(R.id.timerS);
        counter = findViewById(R.id.counterS);
        sound = findViewById(R.id.soundS);

        Intent intent = getIntent();
        activity = intent.getStringExtra("activity");
        Switch[] switches = {theme, timer, counter, sound};
        for (int i=0; i<switches.length; i++)
            switches[i].setChecked(getSettings()[i]);
        theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setSettings(theme.isChecked(), timer.isChecked(), counter.isChecked(), sound.isChecked());
                recreate();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setSettings(theme.isChecked(), timer.isChecked(), counter.isChecked(), sound.isChecked());
        switch (activity) {
            case "MAIN":
                changeMATheme();
                break;
            case "READY":
                changeMATheme();
                changeRATheme();
                break;
            case "TEST":
                changeMATheme();
                changeTATheme();
                break;
            case "RESULT":
                changeMATheme();
                changeRESATheme();
                break;
            default:
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}