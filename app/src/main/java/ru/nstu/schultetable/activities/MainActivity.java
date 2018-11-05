package ru.nstu.schultetable.activities;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sign = findViewById(R.id.signB);
        Button start = findViewById(R.id.startB);
        Button help = findViewById(R.id.helpB);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
