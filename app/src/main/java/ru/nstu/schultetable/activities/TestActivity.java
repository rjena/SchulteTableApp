package ru.nstu.schultetable.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ru.nstu.schultetable.R;

import static ru.nstu.schultetable.activities.MainActivity.getCurrentUserBday;
import static ru.nstu.schultetable.activities.MainActivity.getSettings;

public class TestActivity extends AppCompatActivity {
    static Activity activity;
    int counter;
    ArrayList<Button> buttons;
    TextView counterTV;
    TextView timerTV;
    long startTime = 0;
    TypedValue background;
    int table;
    double[] tablesTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;
        counterTV = findViewById(R.id.counterTV);
        timerTV = findViewById(R.id.timerTV);
        GridLayout gl = findViewById(R.id.gl);

        if (!getSettings()[1])
            timerTV.setVisibility(View.INVISIBLE);
        if (!getSettings()[2])
            counterTV.setVisibility(View.INVISIBLE);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int x = size.x - 50;
        background = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.windowBackground, background, true);

        Intent intent = getIntent();
        table = intent.getIntExtra("table", 0);
        tablesTime = new double[table-1];
        switch (table) {
            case 2:
                tablesTime[0] = intent.getDoubleExtra("table1", 0);
                break;
            case 3:
                tablesTime[0] = intent.getDoubleExtra("table1", 0);
                tablesTime[1] = intent.getDoubleExtra("table2", 0);
                break;
            case 4:
                tablesTime[0] = intent.getDoubleExtra("table1", 0);
                tablesTime[1] = intent.getDoubleExtra("table2", 0);
                tablesTime[2] = intent.getDoubleExtra("table3", 0);
                break;
            case 5:
                tablesTime[0] = intent.getDoubleExtra("table1", 0);
                tablesTime[1] = intent.getDoubleExtra("table2", 0);
                tablesTime[2] = intent.getDoubleExtra("table3", 0);
                tablesTime[3] = intent.getDoubleExtra("table4", 0);
                break;
        }

        buttons = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            final Button bt = new Button(this);
            bt.setBackgroundColor(background.data);
            gl.addView(bt);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) bt.getLayoutParams();
            params.width = x / 5;
            params.height = params.width;
            params.topMargin = 1;
            params.bottomMargin = 1;
            params.leftMargin = 1;
            params.rightMargin = 1;
            bt.setLayoutParams(params);
            bt.setTextSize(x / 20);
            buttons.add(bt);
        }
        newTest();
    }

    public void newTest() {
        Timer stopwatchTimer = new Timer();
        startTime = System.currentTimeMillis();
        stopwatchTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerTV.setText(new SimpleDateFormat("mm:ss.S")
                                .format(new Date(System.currentTimeMillis() - startTime)));
                    }
                });

            }
        }, 0, 10);

        counter = 1;
        counterTV.setText(String.valueOf(counter));
        final ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < 26; i++)
            numbers.add(i);
        Collections.shuffle(numbers);

        for (int i = 0; i < 25; i++) {
            buttons.get(i).setText(String.valueOf(numbers.get(i)));
            final int ii = i;
            buttons.get(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (counter == Integer.valueOf(buttons.get(ii).getText().toString())) {
                                buttons.get(ii).setBackgroundColor(Color.parseColor("#59ba61"));
                                counter++;
                                if (counter > 25) {
                                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                                    intent.putExtra("table", table);
                                    switch (table) {
                                        case 1:
                                            intent.putExtra("table1", (double)Math.round((System.currentTimeMillis() - startTime)/10)/100);
                                            break;
                                        case 2:
                                            intent.putExtra("table1", tablesTime[0]);
                                            intent.putExtra("table2", (double)Math.round((System.currentTimeMillis() - startTime)/10)/100);
                                            break;
                                        case 3:
                                            intent.putExtra("table1", tablesTime[0]);
                                            intent.putExtra("table2", tablesTime[1]);
                                            intent.putExtra("table3", (double)Math.round((System.currentTimeMillis() - startTime)/10)/100);
                                            break;
                                        case 4:
                                            intent.putExtra("table1", tablesTime[0]);
                                            intent.putExtra("table2", tablesTime[1]);
                                            intent.putExtra("table3", tablesTime[2]);
                                            intent.putExtra("table4", (double)Math.round((System.currentTimeMillis() - startTime)/10)/100);
                                            break;
                                        case 5:
                                            intent.putExtra("table1", tablesTime[0]);
                                            intent.putExtra("table2", tablesTime[1]);
                                            intent.putExtra("table3", tablesTime[2]);
                                            intent.putExtra("table4", tablesTime[3]);
                                            intent.putExtra("table5", (double)Math.round((System.currentTimeMillis() - startTime)/10)/100);
                                            break;
                                    }
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    counterTV.setText(String.valueOf(counter));
                            } else
                                buttons.get(ii).setBackgroundColor(Color.parseColor("#d13636"));
                            buttons.get(ii).invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            buttons.get(ii).setBackgroundColor(background.data);
                            buttons.get(ii).invalidate();
                            break;
                        }
                    }
                    return false;
                }
            });
        }
    }

    public static void changeTATheme() {
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
        MenuItem refresh = menu.findItem(R.id.refresh);
        refresh.setVisible(true);
        refresh.setEnabled(true);
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
            case R.id.refresh:
                newTest();
                return true;
            case R.id.info:
                intent = new Intent(getApplicationContext(), HelpActivity.class);
                intent.putExtra("changeTV", false);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("activity", "TEST");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}