package ru.nstu.schultetable.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nstu.schultetable.R;
import ru.nstu.schultetable.models.UserModel;
import ru.nstu.schultetable.rest.ApiUtils;
import ru.nstu.schultetable.rest.STInterface;

import static ru.nstu.schultetable.activities.MainActivity.getSettings;

public class SignUpActivity extends AppCompatActivity {
    Activity activity;
    Button signUpB;
    EditText emailET;
    EditText loginET;
    EditText nameET;
    EditText birthET;
    EditText pass1ET;
    EditText pass2ET;
    FrameLayout datePickerFL;
    FrameLayout progressFL;
    LinearLayout noConnectionLL;
    ImageButton retryIB;
    TypedValue background;
    ArrayList<String> usersLogins, usersEmails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = this;
        background = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.windowBackground, background, true);

        Bundle extras = getIntent().getExtras();
        usersLogins = (ArrayList<String>) extras.getSerializable("usersLogins");
        usersEmails = (ArrayList<String>) extras.getSerializable("usersEmails");

        progressFL = findViewById(R.id.progressFL);
        if (getSettings()[0])
            progressFL.setBackgroundColor(Color.parseColor("#dd000000"));
        else
            progressFL.setBackgroundColor(Color.parseColor("#ccffffff"));
        noConnectionLL = findViewById(R.id.noConnectionLL);
        if (getSettings()[0])
            noConnectionLL.setBackgroundColor(Color.parseColor("#dd000000"));
        else
            noConnectionLL.setBackgroundColor(Color.parseColor("#ccffffff"));
        retryIB = findViewById(R.id.retryIB);
        if (getSettings()[0]) {
            retryIB.setBackgroundResource(R.drawable.round_black);
            retryIB.setImageResource(R.drawable.refresh_white);
        }
        else {
            retryIB.setBackgroundResource(R.drawable.round_white);
            retryIB.setImageResource(R.drawable.refresh_black);
        }
        datePickerFL = findViewById(R.id.datePickerFL);

        if (getSettings()[0])
            datePickerFL.setBackgroundColor(Color.parseColor("#99000000"));
        else
            datePickerFL.setBackgroundColor(Color.parseColor("#ccffffff"));
        findViewById(R.id.datePickerLL).setBackgroundColor(background.data);
        final DatePicker datePicker = findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        datePicker.setMinDate(0); // 01.01.1970
        datePicker.updateDate(1990,0,1);
        Button cancelDPB = findViewById(R.id.cancelB);
        Button okDPB = findViewById(R.id.okB);
        cancelDPB.setBackgroundColor(background.data);
        okDPB.setBackgroundColor(background.data);
        cancelDPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerFL.setVisibility(View.GONE);
            }
        });
        okDPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String d = String.valueOf(datePicker.getYear()) + "-";
                if (datePicker.getMonth() < 10) d += "0";
                d += String.valueOf(datePicker.getMonth() + 1) + "-";
                if (datePicker.getDayOfMonth() < 9) d += "0";
                d += String.valueOf(datePicker.getDayOfMonth());
                birthET.setText(d);
                datePickerFL.setVisibility(View.GONE);
            }
        });

        signUpB = findViewById(R.id.signUpB);
        emailET = findViewById(R.id.emailET);
        loginET = findViewById(R.id.usernameET);
        nameET = findViewById(R.id.nameET);
        birthET = findViewById(R.id.birthET);
        okDPB.callOnClick();
        pass1ET = findViewById(R.id.passET);
        pass2ET = findViewById(R.id.pass2ET);
        signUpB.setEnabled(false);

        signUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    progressFL.setVisibility(View.VISIBLE);
                    tryCall(nameET.getText().toString(),
                            loginET.getText().toString(),
                            emailET.getText().toString(),
                            birthET.getText().toString(),
                            pass1ET.getText().toString(),
                            random(),
                            random());
                }
            }
        });

        retryIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    progressFL.setVisibility(View.VISIBLE);
                    noConnectionLL.setVisibility(View.GONE);
                    tryCall(nameET.getText().toString(),
                            loginET.getText().toString(),
                            emailET.getText().toString(),
                            birthET.getText().toString(),
                            pass1ET.getText().toString(),
                            random(),
                            random());
                }
            }
        });

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFieldsFill();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        loginET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFieldsFill();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFieldsFill();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        birthET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFieldsFill();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        birthET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                datePickerFL.setVisibility(View.VISIBLE);
            }
        });
        pass1ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pass2ET.getError() != null)
                    if (pass2ET.getError().toString().equals(getString(R.string.error_pass_mismatch)))
                        pass2ET.setError(null);
                checkFieldsFill();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        pass2ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pass1ET.getError() != null)
                    if (pass1ET.getError().toString().equals(getString(R.string.error_pass_mismatch)))
                        pass1ET.setError(null);
                checkFieldsFill();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void checkFieldsFill() {
        if (!emailET.getText().toString().isEmpty() &&
                !loginET.getText().toString().isEmpty() &&
                !nameET.getText().toString().isEmpty() &&
                !birthET.getText().toString().isEmpty() &&
                !pass1ET.getText().toString().isEmpty() &&
                !pass2ET.getText().toString().isEmpty())
            signUpB.setEnabled(true);
        else signUpB.setEnabled(false);
    }

    private boolean checkData() {
        boolean dataOk = true;
        String email = emailET.getText().toString();
        String username = loginET.getText().toString();
        String pass1 = pass1ET.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError(getString(R.string.error_email_incorrect));
            dataOk = false;
        }
        if (email.length() > 50) {
            emailET.setError(getString(R.string.error_email_long));
            dataOk = false;
        }
        if (username.length() < 3) {
            loginET.setError(getString(R.string.error_username_short));
            dataOk = false;
        }
        else if (username.length() > 50) {
            loginET.setError(getString(R.string.error_username_long));
            dataOk = false;
        }
        if (nameET.getText().toString().length() > 100) {
            nameET.setError(getString(R.string.error_name_long));
            dataOk = false;
        }
        if (pass1.length() < 8) {
            pass1ET.setError(getString(R.string.error_pass_short));
            dataOk = false;
        }
        else if (pass1.length() > 100) {
            pass1ET.setError(getString(R.string.error_pass_long));
            dataOk = false;
        }
        else if (!pass1.equals(pass2ET.getText().toString())) {
            pass1ET.setError(getString(R.string.error_pass_mismatch));
            pass2ET.setError(getString(R.string.error_pass_mismatch));
            dataOk = false;
        }
        return dataOk;
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(200)+800;
        String[] chars = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
        String temp;
        for (int i = 0; i < randomLength; i++){
            temp = chars[generator.nextInt(chars.length)];
            randomStringBuilder.append(temp);
        }
        return randomStringBuilder.toString();
    }

    public void tryCall(final String name, final String login, final String email,
                        final String birth, final String pass,
                        final String tokenToConfirmEmail, final String tokenToResetPassword) {
        STInterface api = ApiUtils.getAPIService();
        Call<UserModel> call = api.signUp(
                login,
                email,
                name,
                birth,
                pass,
                tokenToConfirmEmail,
                tokenToResetPassword
        );
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            R.string.new_user, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    intent.putExtra("isNewUser", true);
                    intent.putExtra("email", email);
                    intent.putExtra("password", pass);
                    startActivity(intent);
                    onBackPressed();
                } else {
                    progressFL.setVisibility(View.GONE);
                    if (usersEmails.contains(email)) {
                        emailET.setError(getString(R.string.error_email_taken));
                    }
                    if (usersLogins.contains(login)) {
                        loginET.setError(getString(R.string.error_username_taken));
                    }
                }

            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                progressFL.setVisibility(View.GONE);
                noConnectionLL.setVisibility(View.VISIBLE);
            }
        });
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