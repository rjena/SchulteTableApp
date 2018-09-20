package ru.nstu.schultetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final ImageButton back = findViewById(R.id.backIB);
        TextView tv = findViewById(R.id.textView);

        Intent intent = getIntent();
        boolean mode = intent.getBooleanExtra("changeTV", false);

        if (mode)
            tv.setText(R.string.about);
        else
            tv.setText(R.string.help_info);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}