package ru.nstu.schultetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final ImageButton back = findViewById(R.id.backIB);
        final Button signIn = findViewById(R.id.signInB);
        TextView signUp = findViewById(R.id.signUpTV);
        EditText login = findViewById(R.id.loginET);
        EditText password = findViewById(R.id.passET);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo проверка на существование логина -> пароль верный?
                // todo завершить предыдущие активити
                Intent intent = new Intent(getApplicationContext(), ReadyActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        // todo при непустых полях делать кнопку входа доступной
        signIn.setEnabled(true);
    }
}