package ru.nstu.schultetable.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import ru.nstu.schultetable.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*DatePicker dp = findViewById(R.id.datePicker2);

        dp.setMaxDate(System.currentTimeMillis());
        dp.setMinDate(0); // 01.01.1970*/

        final Button signUp = findViewById(R.id.signUpB);
        EditText login = findViewById(R.id.loginET);
        EditText birth = findViewById(R.id.birthET);
        EditText pass1 = findViewById(R.id.passET);
        EditText pass2 = findViewById(R.id.pass2ET);

        // todo разделители в дате
        // todo проверка на совпадение паролей

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo проверка на заполненность полей
                // todo тост о том, что аккаунт создан и нужно подтвердить почту в письме
                onBackPressed();
            }
        });

        // todo при непустых полях делать кнопку регистрации доступной
        signUp.setEnabled(true);
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