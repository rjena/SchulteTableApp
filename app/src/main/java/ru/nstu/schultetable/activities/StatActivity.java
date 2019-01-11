package ru.nstu.schultetable.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nstu.schultetable.R;
import ru.nstu.schultetable.models.TestModel;
import ru.nstu.schultetable.rest.ApiUtils;
import ru.nstu.schultetable.rest.STInterface;

import static ru.nstu.schultetable.activities.MainActivity.getCurrentUserBday;
import static ru.nstu.schultetable.activities.MainActivity.getCurrentUserID;
import static ru.nstu.schultetable.activities.MainActivity.getSettings;

public class StatActivity extends AppCompatActivity {
    GraphView graph;
    FrameLayout progressFL;
    LinearLayout noConnectionLL;
    ImageButton retryIB;
    ArrayList<String> testsIDs, testsUsersIDs, testsDates;
    ArrayList<Double> testsWEs;
    ArrayList<Integer> testsAges;
    TextView bestResTV, meanResTV, ageResTV, allResTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSettings()[0] ? R.style.AppThemeDark : R.style.AppThemeLight);
        setContentView(R.layout.activity_stat);
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

        graph= findViewById(R.id.graph);
        bestResTV = findViewById(R.id.bestResTV);
        meanResTV = findViewById(R.id.meanResTV);
        ageResTV = findViewById(R.id.ageResTV);
        allResTV = findViewById(R.id.allResTV);

        progressFL.setVisibility(View.VISIBLE);
        tryCall();
    }

    public void buildGraph(ArrayList<Integer> indexes) {
        ArrayList<Double> usersRes = new ArrayList<>();
        ArrayList<String> usersDates = new ArrayList<>();
        for (int i = indexes.size(); i > 0; i--) {
            usersRes.add(testsWEs.get(indexes.get(i - 1)));
            usersDates.add(testsDates.get(indexes.get(i - 1)));
        }
        DataPoint[] points;
        if (usersRes.size() > 5)
             points = new DataPoint[5];
        else
            points = new DataPoint[usersRes.size()];

        for (int i = 0; i<points.length; i++) {
            points[i] = new DataPoint(getDays(usersDates.get(i)), usersRes.get(i));
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        graph.addSeries(series);
    }

    public void makeStat() {
        ArrayList<Integer> curUsersTests = indexOfAll(getCurrentUserID(), testsUsersIDs);
        ArrayList<Integer> curAgeTests = indexOfAll(getAge(getCurrentUserBday()), testsAges);
        double bestUsersRes = testsWEs.get(curUsersTests.get(0));
        double meanUsersRes = 0.0;
        double meanAgeRes = 0.0;
        double meanAllRes = 0.0;
        for (int i = 0; i < curUsersTests.size(); i++) {
            meanUsersRes += testsWEs.get(curUsersTests.get(i));
            if (bestUsersRes > testsWEs.get(curUsersTests.get(i)))
                bestUsersRes = testsWEs.get(curUsersTests.get(i));
        }
        meanUsersRes /= curUsersTests.size();
        for (int i = 0; i < curAgeTests.size(); i++)
            meanAgeRes += testsWEs.get(curAgeTests.get(i));
        meanAgeRes /= curAgeTests.size();
        for (int i = 0; i < testsWEs.size(); i++)
            meanAllRes += testsWEs.get(i);
        meanAllRes /= testsWEs.size();
        bestResTV.setText(new DecimalFormat("####0.00").format(bestUsersRes));
        meanResTV.setText(new DecimalFormat("####0.00").format(meanUsersRes));
        ageResTV.setText(new DecimalFormat("####0.00").format(meanAgeRes));
        allResTV.setText(new DecimalFormat("####0.00").format(meanAllRes));
        buildGraph(curUsersTests);
    }

    private long getDays(String bday){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        String[] bdate = bday.split("-");
        dob.set(Integer.valueOf(bdate[0]), Integer.valueOf(bdate[1])-1, Integer.valueOf(bdate[2]));
        long daysBetween = 0;
        while (dob.before(today)) {
            dob.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
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

    private ArrayList<Integer> indexOfAll(Object id, ArrayList list){
        ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            if(id.equals(list.get(i)))
                indexList.add(i);
        return indexList;
    }

    public void tryCall() {
        STInterface api = ApiUtils.getAPIService();
        Call<TestModel[]> call = api.getTests("json");
        call.enqueue(new Callback<TestModel[]>() {
            @Override
            public void onResponse(Call<TestModel[]> call, Response<TestModel[]> response) {
                if (response.isSuccessful()) {
                    TestModel[] testsM = response.body();
                    testsIDs = new ArrayList<>();
                    testsUsersIDs = new ArrayList<>();
                    testsWEs = new ArrayList<>();
                    testsAges = new ArrayList<>();
                    testsDates = new ArrayList<>();
                    for (int i=0; i < testsM.length; i++) {
                        testsIDs.add(testsM[i].getId());
                        testsUsersIDs.add(testsM[i].getUserId());
                        testsWEs.add(testsM[i].getStWE());
                        testsAges.add(testsM[i].getAge());
                        testsDates.add(testsM[i].getDate());
                    }
                    progressFL.setVisibility(View.GONE);
                    makeStat();
                }
            }
            @Override
            public void onFailure(Call<TestModel[]> call, Throwable t) {
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
