package ru.nstu.schultetable.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ru.nstu.schultetable.R;

public class MainActivity extends AppCompatActivity {
    static SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sign = findViewById(R.id.signB);
        Button start = findViewById(R.id.startB);
        Button help = findViewById(R.id.helpB);
        settings = getPreferences(MODE_PRIVATE);

        // ширина кнопок одинаковая
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ConstraintLayout.LayoutParams signParams = (ConstraintLayout.LayoutParams) sign.getLayoutParams();
        signParams.width = (int) (size.x * 0.6);
        sign.setLayoutParams(signParams);
        ConstraintLayout.LayoutParams startParams = (ConstraintLayout.LayoutParams) start.getLayoutParams();
        startParams.width = (int) (size.x * 0.6);
        start.setLayoutParams(startParams);
        ConstraintLayout.LayoutParams helpParams = (ConstraintLayout.LayoutParams) help.getLayoutParams();
        helpParams.width = (int) (size.x * 0.6);
        help.setLayoutParams(helpParams);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReadyActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                intent.putExtra("changeTV",false);
                startActivity(intent);
            }
        });
    }

    public static boolean[] getSettings() {
        /*
        Theme: light-false, dark-true
        Timer: on-false, off-true
        Counter: on-false, off-true
        Sound: on-false, off-true
        */
        boolean[] set = {
                settings.getBoolean("theme", false),
                settings.getBoolean("timer", true),
                settings.getBoolean("counter", true),
                settings.getBoolean("sound", true)
        };
        return set;
    }

    public static void setSettings(boolean theme, boolean timer, boolean counter, boolean sound) {
        SharedPreferences.Editor ed = settings.edit();
        ed.putBoolean("theme", false);
        //ed.putBoolean("theme", theme);
        ed.putBoolean("timer", timer);
        ed.putBoolean("counter", counter);
        ed.putBoolean("sound", sound);
        ed.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSettings()[0])
            getMenuInflater().inflate(R.menu.menu_dark, menu);
        else
            getMenuInflater().inflate(R.menu.menu_light, menu);
        MenuItem info = menu.findItem(R.id.info);
        info.setVisible(true);
        info.setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.info:
                intent = new Intent(getApplicationContext(), HelpActivity.class);
                intent.putExtra("changeTV",true);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
