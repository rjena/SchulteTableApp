package ru.nstu.schultetable.activities;

import android.app.Activity;
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
    static Activity activity;
    static SharedPreferences curUserID;
    final static String CURRENTUSERID = "current_user_ID";
    final static String CURRENTUSERBDAY = "current_user_bday";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getPreferences(MODE_PRIVATE);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_main);

        curUserID = getPreferences(MODE_PRIVATE);
        activity = this;
        Button sign = findViewById(R.id.signB);
        Button start = findViewById(R.id.startB);
        Button help = findViewById(R.id.helpB);

        // ширина кнопок одинаковая
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ConstraintLayout.LayoutParams signParams =
                (ConstraintLayout.LayoutParams) sign.getLayoutParams();
        signParams.width = (int) (size.x * 0.6);
        sign.setLayoutParams(signParams);
        if (getCurrentUserID().equals("0"))
            sign.setText(getString(R.string.sign));
        else
            sign.setText(getString(R.string.edit_profile));
        ConstraintLayout.LayoutParams startParams =
                (ConstraintLayout.LayoutParams) start.getLayoutParams();
        startParams.width = (int) (size.x * 0.6);
        start.setLayoutParams(startParams);
        ConstraintLayout.LayoutParams helpParams =
                (ConstraintLayout.LayoutParams) help.getLayoutParams();
        helpParams.width = (int) (size.x * 0.6);
        help.setLayoutParams(helpParams);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (getCurrentUserID().equals("0"))
                    intent = new Intent(getApplicationContext(), SignInActivity.class);
                else
                    intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        if (getCurrentUserID().equals("0"))
            start.setEnabled(false);
        else
            start.setEnabled(true);
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
                intent.putExtra("changeTV", false);
                startActivity(intent);
            }
        });
    }

    public static String getCurrentUserID() {
        return curUserID.getString(CURRENTUSERID, "0");
    }
    public static String getCurrentUserBday() {
        return curUserID.getString(CURRENTUSERBDAY, "0");
    }

    public static void setCurrentUserID(String userId) {
        SharedPreferences.Editor ed = curUserID.edit();
        ed.putString(CURRENTUSERID, userId);
        ed.apply();
    }
    public static void setCurrentUserBday(String bday) {
        SharedPreferences.Editor ed = curUserID.edit();
        ed.putString(CURRENTUSERBDAY, bday);
        ed.apply();
    }

    public static void resetCurrentUserID() {
        SharedPreferences.Editor ed = curUserID.edit();
        ed.clear();
        ed.apply();
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
        ed.putBoolean("theme", theme);
        ed.putBoolean("timer", timer);
        ed.putBoolean("counter", counter);
        ed.putBoolean("sound", sound);
        ed.apply();
    }

    public static void changeMATheme() {
        activity.recreate();
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
                intent.putExtra("changeTV", true);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("activity", "MAIN");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}