package ru.nstu.schultetable.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ru.nstu.schultetable.R;

import static ru.nstu.schultetable.activities.MainActivity.getSettings;

public class ReadyActivity extends AppCompatActivity {
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_ready);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;
        Button start = findViewById(R.id.startB);
        Button stat = findViewById(R.id.statB);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                intent.putExtra("table", 1);
                startActivity(intent);
                finish();
            }
        });

        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public static void changeRATheme() {
        activity.recreate();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getSettings()[0])
            getMenuInflater().inflate(R.menu.menu_dark, menu);
        else
            getMenuInflater().inflate(R.menu.menu_light, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("activity", "READY");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
