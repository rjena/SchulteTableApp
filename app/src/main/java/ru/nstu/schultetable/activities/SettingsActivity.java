package ru.nstu.schultetable.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import ru.nstu.schultetable.R;

import static ru.nstu.schultetable.activities.MainActivity.getSettings;
import static ru.nstu.schultetable.activities.MainActivity.setSettings;

public class SettingsActivity extends AppCompatActivity {
    Switch theme;
    Switch timer;
    Switch counter;
    Switch sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        theme = findViewById(R.id.themeS);
        timer = findViewById(R.id.timerS);
        counter = findViewById(R.id.counterS);
        sound = findViewById(R.id.soundS);

        Switch[] switches = {theme, timer, counter, sound};
        for (int i=0; i<switches.length; i++)
            switches[i].setChecked(getSettings()[i]);
        theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setSettings(theme.isChecked(), timer.isChecked(), counter.isChecked(), sound.isChecked());
            }
        });
    }

    @Override
    public void onBackPressed() {
        setSettings(theme.isChecked(), timer.isChecked(), counter.isChecked(), sound.isChecked());
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