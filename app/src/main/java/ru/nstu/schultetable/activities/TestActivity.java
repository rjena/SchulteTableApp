package ru.nstu.schultetable.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ru.nstu.schultetable.R;

public class TestActivity extends AppCompatActivity {
    int counter;
    ArrayList<Button> buttons;
    TextView counterTV;
    TextView timerTV;
    long startTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        counterTV = findViewById(R.id.counterTV);
        timerTV = findViewById(R.id.timerTV);
        GridLayout gl = findViewById(R.id.gl);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int x = size.x - 40;

        buttons = new ArrayList<>();
        for (int i=0; i<25; i++) {
            final Button bt = new Button(this);
            bt.setBackgroundColor(Color.WHITE);
            gl.addView(bt);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) bt.getLayoutParams();
            params.width = x / 5;
            params.height = params.width;
            params.topMargin=1;
            params.bottomMargin=1;
            params.leftMargin=1;
            params.rightMargin=1;
            bt.setLayoutParams(params);
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
        for (int i=1; i<26; i++)
            numbers.add(i);
        Collections.shuffle(numbers);

        for (int i=0; i<25; i++) {
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
                                if (counter > 25)
                                    newTest();
                                else
                                    counterTV.setText(String.valueOf(counter));
                            } else
                                buttons.get(ii).setBackgroundColor(Color.parseColor("#d13636"));
                            buttons.get(ii).invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            buttons.get(ii).setBackgroundColor(Color.WHITE);
                            buttons.get(ii).invalidate();
                            break;
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
                intent.putExtra("changeTV",false);
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