package ru.nstu.schultetable.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nstu.schultetable.R;
import ru.nstu.schultetable.models.TestModel;
import ru.nstu.schultetable.models.UserModel;
import ru.nstu.schultetable.rest.ApiUtils;
import ru.nstu.schultetable.rest.STInterface;

import static ru.nstu.schultetable.activities.MainActivity.getCurrentUserBday;
import static ru.nstu.schultetable.activities.MainActivity.getCurrentUserID;
import static ru.nstu.schultetable.activities.MainActivity.getSettings;

public class ResultActivity extends AppCompatActivity {
    boolean firstBack;
    static Activity activity;
    LinearLayout partLL;
    LinearLayout totalLL;
    FrameLayout progressFL;
    LinearLayout noConnectionLL;
    ImageButton retryIB;
    double[] tablesTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;
        firstBack = false;
        partLL = findViewById(R.id.partLL);
        totalLL = findViewById(R.id.totalLL);

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

        Intent intent = getIntent();
        final int table = intent.getIntExtra("table", 0);
        tablesTime = new double[table];
        switch (table) {
            case 1:
                tablesTime[0] = intent.getDoubleExtra("table1",0);
                break;
            case 2:
                tablesTime[0] = intent.getDoubleExtra("table1",0);
                tablesTime[1] = intent.getDoubleExtra("table2",0);
                break;
            case 3:
                tablesTime[0] = intent.getDoubleExtra("table1",0);
                tablesTime[1] = intent.getDoubleExtra("table2",0);
                tablesTime[2] = intent.getDoubleExtra("table3",0);
                break;
            case 4:
                tablesTime[0] = intent.getDoubleExtra("table1",0);
                tablesTime[1] = intent.getDoubleExtra("table2",0);
                tablesTime[2] = intent.getDoubleExtra("table3",0);
                tablesTime[3] = intent.getDoubleExtra("table4",0);
                break;
            case 5:
                tablesTime[0] = intent.getDoubleExtra("table1",0);
                tablesTime[1] = intent.getDoubleExtra("table2",0);
                tablesTime[2] = intent.getDoubleExtra("table3",0);
                tablesTime[3] = intent.getDoubleExtra("table4",0);
                tablesTime[4] = intent.getDoubleExtra("table5",0);
                break;
        }

        if (table == 5) {
            partLL.setVisibility(View.GONE);
            totalLL.setVisibility(View.VISIBLE);
            TextView weTV = findViewById(R.id.WETV);
            TextView wuTV = findViewById(R.id.WUTV);
            TextView psTV = findViewById(R.id.PSTV);
            double WE = 0;
            for (double t: tablesTime) WE += t;
            WE /= 5;
            double WU = tablesTime[0] / WE;
            double PS = tablesTime[3] / WE;
            DecimalFormat df = new DecimalFormat("####0.00");
            weTV.setText(df.format(WE));
            wuTV.setText(df.format(WU));
            psTV.setText(df.format(PS));
            tryCall(getDate(), getCurrentUserID(), getAge(getCurrentUserBday()),
                    df.format(WE).replace(",","."),
                    df.format(WU).replace(",","."),
                    df.format(PS).replace(",","."));

        } else {
            TextView timeTV = findViewById(R.id.timeTV);
            TextView nextTV = findViewById(R.id.nextTV);
            Button continueB = findViewById(R.id.continueB);
            timeTV.setText(new DecimalFormat("####0.00").format(tablesTime[table-1]));
            Spannable sqm = new SpannableString(String.valueOf(table+1));
            sqm.setSpan(new ForegroundColorSpan(ContextCompat
                            .getColor(getApplicationContext(), R.color.colorAccent)),
                    0, sqm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nextTV.append(sqm);
            continueB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                    intent.putExtra("table", table + 1);
                    switch (table) {
                        case 1:
                            intent.putExtra("table1", tablesTime[0]);
                            break;
                        case 2:
                            intent.putExtra("table1", tablesTime[0]);
                            intent.putExtra("table2", tablesTime[1]);
                            break;
                        case 3:
                            intent.putExtra("table1", tablesTime[0]);
                            intent.putExtra("table2", tablesTime[1]);
                            intent.putExtra("table3", tablesTime[2]);
                            break;
                        case 4:
                            intent.putExtra("table1", tablesTime[0]);
                            intent.putExtra("table2", tablesTime[1]);
                            intent.putExtra("table3", tablesTime[2]);
                            intent.putExtra("table4", tablesTime[3]);
                    }
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private int getAge(String bday){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        String[] bdate = bday.split("-");
        dob.set(Integer.valueOf(bdate[0]), Integer.valueOf(bdate[1])-1, Integer.valueOf(bdate[2]));
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
            age--;
        return age;
    }

    private String getDate(){
        Calendar today = Calendar.getInstance();
        int dayToday = today.get(Calendar.DAY_OF_MONTH);
        int monthToday = today.get(Calendar.MONTH) + 1;
        int yearToday = today.get(Calendar.YEAR);
        String date = yearToday+"-";
        if (monthToday <10) date+="0";
        date += monthToday+"-";
        if (dayToday<10)date+="0";
        date+=dayToday;
        return date;
    }

    public void tryCall(final String date, final String userId, final int age,
                        final String stWE, final String stWU, final String stPS) {
        STInterface api = ApiUtils.getAPIService();
        Call<TestModel> call = api.addTest(date, userId, age, stWE, stWU, stPS);
        call.enqueue(new Callback<TestModel>() {
            @Override
            public void onResponse(Call<TestModel> call, Response<TestModel> response) {
                if (response.isSuccessful()) {
                    progressFL.setVisibility(View.GONE);
                } else Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<TestModel> call, Throwable t) {
                progressFL.setVisibility(View.GONE);
                noConnectionLL.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void changeRESATheme() {
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
                intent.putExtra("changeTV", false);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("activity", "RESULT");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
