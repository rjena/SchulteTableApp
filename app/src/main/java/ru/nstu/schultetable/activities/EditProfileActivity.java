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
import android.view.Menu;
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

import static ru.nstu.schultetable.activities.MainActivity.changeMATheme;
import static ru.nstu.schultetable.activities.MainActivity.getCurrentUserID;
import static ru.nstu.schultetable.activities.MainActivity.getSettings;
import static ru.nstu.schultetable.activities.MainActivity.resetCurrentUserID;

public class EditProfileActivity extends AppCompatActivity {
    Activity activity;
    Button editB;
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
    String uID= "", uName, uLogin, uEmail, uBirth, uPassword, uEmailToken, uPasswordToken,
            fuName, fuLogin, fuEmail, fuBirth, fuPassword, fuEmailToken, fuPasswordToken;
    String newName, newLogin, newEmail, newBirth, newPassword, newEmailToken, newPasswordToken;
    ArrayList<String> usersIDs, usersLogins, usersEmails, usersPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = this;
        background = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.windowBackground, background, true);

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

        editB = findViewById(R.id.editB);
        emailET = findViewById(R.id.emailET);
        loginET = findViewById(R.id.usernameET);
        nameET = findViewById(R.id.nameET);
        birthET = findViewById(R.id.birthET);
        okDPB.callOnClick();
        pass1ET = findViewById(R.id.passET);
        pass2ET = findViewById(R.id.pass2ET);
        editB.setEnabled(false);

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    newLogin = loginET.getText().toString();
                    newEmail = emailET.getText().toString();
                    newName = nameET.getText().toString();
                    newBirth = birthET.getText().toString();
                    newPassword = uPassword;
                    newEmailToken = uEmailToken;
                    newPasswordToken = uPasswordToken;
                    if (!uEmail.equals(emailET.getText().toString()))
                        newEmailToken = random();
                    if (!pass1ET.getText().toString().isEmpty()) {
                        newPassword = pass1ET.getText().toString();
                        newPasswordToken = random();
                    }
                    progressFL.setVisibility(View.VISIBLE);
                    tryCall(uID, newLogin, newEmail, newName, newBirth, newPassword,
                            newEmailToken, newPasswordToken);
                }
            }
        });

        retryIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noConnectionLL.setVisibility(View.GONE);
                progressFL.setVisibility(View.VISIBLE);
                if (checkData()) {
                    newLogin = loginET.getText().toString();
                    newEmail = emailET.getText().toString();
                    newName = nameET.getText().toString();
                    newBirth = birthET.getText().toString();
                    newPassword = uPassword;
                    newEmailToken = uEmailToken;
                    newPasswordToken = uPasswordToken;
                    if (!uEmail.equals(emailET.getText().toString()))
                        newEmailToken = random();
                    if (!pass1ET.getText().toString().isEmpty()) {
                        newPassword = pass1ET.getText().toString();
                        newPasswordToken = random();
                    }
                    progressFL.setVisibility(View.VISIBLE);
                    tryCall(uID, newLogin, newEmail, newName, newBirth, newPassword,
                            newEmailToken, newPasswordToken);
                } else if (uID.equals("")) {
                    tryGetUser();
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

        progressFL.setVisibility(View.VISIBLE);
        tryGetUser();
    }

    private void checkFieldsFill() {
        if (!emailET.getText().toString().isEmpty() &&
                !loginET.getText().toString().isEmpty() &&
                !nameET.getText().toString().isEmpty() &&
                !birthET.getText().toString().isEmpty()) {
            if ((!pass1ET.getText().toString().isEmpty() &&
                    !pass2ET.getText().toString().isEmpty()) ||
                    (pass1ET.getText().toString().isEmpty() &&
                            pass2ET.getText().toString().isEmpty()))
                editB.setEnabled(true);
        }
        else editB.setEnabled(false);
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
        if (pass1.length() > 0 && pass1.length() < 8) {
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

    public void tryCall(final String id, final String name, final String login, final String email,
                        final String birth, final String pass,
                        final String tokenToConfirmEmail, final String tokenToResetPassword) {
        STInterface api = ApiUtils.getAPIService();
        Call<UserModel> call = api.changeUsersData(id, id, login, email, name, birth, pass,
                tokenToConfirmEmail, tokenToResetPassword);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            R.string.acc_edit_success, Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else if (fuEmail.equals(emailET.getText().toString()) &&
                        fuName.equals(nameET.getText().toString()) &&
                        fuBirth.equals(birthET.getText().toString()) &&
                        fuLogin.equals(loginET.getText().toString()) &&
                        pass1ET.getText().toString().isEmpty() &&
                        pass2ET.getText().toString().isEmpty())
                    onBackPressed();
                else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    progressFL.setVisibility(View.GONE);
                    if (usersEmails.contains(email)) {
                        if (!getCurrentUserID().equals(usersIDs.get(usersEmails.indexOf(email))))
                            emailET.setError(getString(R.string.error_email_taken));
                    }
                    if (usersLogins.contains(login)) {
                        if (!getCurrentUserID().equals(usersIDs.get(usersLogins.indexOf(login))))
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

    public void tryGetUser() {
        STInterface api = ApiUtils.getAPIService();
        Call<UserModel> call = api.getUserByID(getCurrentUserID(), "json");
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel usersM = response.body();
                    uID = usersM.getUserId();
                    uLogin = usersM.getLogin();
                    fuLogin = uLogin;
                    uEmail = usersM.getEmail();
                    fuEmail = uEmail;
                    uName = usersM.getName();
                    fuName = uName;
                    uBirth = usersM.getBday();
                    fuBirth = uBirth;
                    uPassword = usersM.getPassword();
                    fuPassword = uPassword;
                    uEmailToken = usersM.getTokenToConfirmEmail();
                    fuEmailToken = uEmailToken;
                    uPasswordToken = usersM.getTokenToResetPassword();
                    fuPasswordToken = uPasswordToken;

                    nameET.setText(uName);
                    emailET.setText(uEmail);
                    loginET.setText(uLogin);
                    birthET.setText(uBirth);
                    tryGetUsers();
                } else {
                    resetCurrentUserID();
                    onBackPressed();
                };
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                progressFL.setVisibility(View.GONE);
                noConnectionLL.setVisibility(View.VISIBLE);
            }
        });
    }
    public void tryGetUsers() {
        STInterface api = ApiUtils.getAPIService();
        Call<UserModel[]> call = api.getUsers("json");
        call.enqueue(new Callback<UserModel[]>() {
            @Override
            public void onResponse(Call<UserModel[]> call, Response<UserModel[]> response) {
                if (response.isSuccessful()) {
                    UserModel[] usersM = response.body();
                    usersIDs = new ArrayList<>();
                    usersLogins = new ArrayList<>();
                    usersEmails = new ArrayList<>();
                    usersPasswords = new ArrayList<>();
                    for (int i=0; i < usersM.length; i++) {
                        usersIDs.add(usersM[i].getUserId());
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
        MenuItem settings = menu.findItem(R.id.settings);
        settings.setVisible(false);
        settings.setEnabled(false);
        MenuItem logOut = menu.findItem(R.id.logOut);
        logOut.setVisible(true);
        logOut.setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.logOut:
                resetCurrentUserID();
                changeMATheme();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
