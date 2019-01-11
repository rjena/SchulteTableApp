package ru.nstu.schultetable.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nstu.schultetable.R;
import ru.nstu.schultetable.models.UserModel;
import ru.nstu.schultetable.rest.ApiUtils;
import ru.nstu.schultetable.rest.STInterface;

import static ru.nstu.schultetable.activities.MainActivity.changeMATheme;
import static ru.nstu.schultetable.activities.MainActivity.getSettings;
import static ru.nstu.schultetable.activities.MainActivity.setCurrentUserBday;
import static ru.nstu.schultetable.activities.MainActivity.setCurrentUserID;

public class SignInActivity extends AppCompatActivity {
    Button signInB;
    EditText loginET;
    EditText passwordET;
    FrameLayout progressFL;
    LinearLayout noConnectionLL;
    ImageButton retryIB;
    ArrayList<String> usersIDs, usersBdays, usersLogins, usersEmails, usersPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        retryIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noConnectionLL.setVisibility(View.GONE);
                progressFL.setVisibility(View.VISIBLE);
                tryCall();
            }
        });
        signInB = findViewById(R.id.signInB);
        TextView signUp = findViewById(R.id.signUpTV);
        loginET = findViewById(R.id.loginET);
        passwordET = findViewById(R.id.passET);

        signInB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginET.getText().toString();
                String password = passwordET.getText().toString();
                int ii;
                if ((usersEmails.contains(login) &&
                        password.equals(usersPasswords.get(ii = usersEmails.indexOf(login)))) ||
                        (usersLogins.contains(login) &&
                                password.equals(usersPasswords.get(ii = usersLogins.indexOf(login))))) {
                    setCurrentUserID(usersIDs.get(ii));
                    setCurrentUserBday(usersBdays.get(ii));
                    Toast.makeText(getApplicationContext(),
                            "Вы вошли в аккаунт " + usersLogins.get(ii),
                            Toast.LENGTH_LONG).show();
                    changeMATheme();
                    onBackPressed();
                } else {
                    if (!usersEmails.contains(login) && !usersLogins.contains(login)) {
                        loginET.setError(getString(R.string.error_no_user));
                    } else if ((usersEmails.contains(login) &&
                            !password.equals(usersPasswords.get(usersEmails.indexOf(login)))) ||
                            (usersLogins.contains(login) &&
                                    !password.equals(usersPasswords.get(usersLogins.indexOf(login))))) {
                        passwordET.setError(getString(R.string.error_wrong_password));
                    }
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundleL = new Bundle();
                bundleL.putSerializable("usersLogins", usersLogins);
                Bundle bundleE = new Bundle();
                bundleE.putSerializable("usersEmails", usersEmails);
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtras(bundleE);
                intent.putExtras(bundleL);
                startActivity(intent);
                onBackPressed();
            }
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
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkFieldsFill();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        progressFL.setVisibility(View.VISIBLE);
        tryCall();
        Intent intent = getIntent();
        if (intent.getBooleanExtra("isNewUser", false)) {
            loginET.setText(intent.getStringExtra("email"));
            passwordET.setText(intent.getStringExtra("password"));
            signInB.performClick();
        }
    }

    public void tryCall() {
        STInterface api = ApiUtils.getAPIService();
        Call<UserModel[]> call = api.getUsers("json");
        call.enqueue(new Callback<UserModel[]>() {
            @Override
            public void onResponse(Call<UserModel[]> call, Response<UserModel[]> response) {
                if (response.isSuccessful()) {
                    UserModel[] usersM = response.body();
                    usersIDs = new ArrayList<>();
                    usersBdays = new ArrayList<>();
                    usersLogins = new ArrayList<>();
                    usersEmails = new ArrayList<>();
                    usersPasswords = new ArrayList<>();
                    for (int i=0; i < usersM.length; i++) {
                        usersIDs.add(usersM[i].getUserId());
                        usersBdays.add(usersM[i].getBday());
                        usersLogins.add(usersM[i].getLogin());
                        usersEmails.add(usersM[i].getEmail());
                        usersPasswords.add(usersM[i].getPassword());
                    }
                    progressFL.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<UserModel[]> call, Throwable t) {
                progressFL.setVisibility(View.GONE);
                noConnectionLL.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkFieldsFill() {
        if (!loginET.getText().toString().isEmpty() &&
                !passwordET.getText().toString().isEmpty())
            signInB.setEnabled(true);
        else signInB.setEnabled(false);
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