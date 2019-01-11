package ru.nstu.schultetable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.nstu.schultetable.R;

import static ru.nstu.schultetable.activities.MainActivity.getSettings;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tv = findViewById(R.id.textView);
        Intent intent = getIntent();
        boolean mode = intent.getBooleanExtra("changeTV", false);
        if (mode)
            tv.setText(R.string.about);
        else
            tv.setText(R.string.help_info);
    }

    @Override
    public void onBackPressed() {
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