package com.example.howmany;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import at.grabner.circleprogress.CircleProgressView;
import im.dacer.androidcharts.BarView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.widget.ImageView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;


public class MainActivity extends AppCompatActivity {

    Button btnLogout;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    public SharedPreferences prefs;

    BarChart chart2;

    int Mon, Tue, Wed, Thu, Fri, Sat, Sun = 0;
    private String Six = "0", Seven = "0", Eight = "0", THIRTeen = "0",
    FourTeen = "0",FifTeen = "0",SixTeen = "0",SevenTeen = "0",EIGHTeen = "0",NineTeen = "0";
    private String Twenty = "0",TwentyOne = "0";
    private Float six = 0.0f,seven = 0.0f,eight = 0.0f,thirteen = 0.0f,fourteen = 0.0f,
            fifteen = 0.0f,sixteen = 0.0f,seventeen = 0.0f,eighteen = 0.0f,
            nineteen = 0.0f,twenty = 0.0f,twentyOne = 0.0f;
    int count = 0;
    private final String TAG = getClass().getSimpleName();
    ArrayList<PeopleList> arrayList = new ArrayList<>();
    private final String BASE_URL = "http://163.152.223.34:8000";
    private MyAPI mMyAPI;
    public static WebView mWebView;

    int[] images =  new int[] {R.drawable.health1, R.drawable.health2,
    R.drawable.health3, R.drawable.health4, R.drawable.health45,R.drawable.health6,
    R.drawable.health7, R.drawable.health8, R.drawable.health9};

    CircleProgressView mCircleView; // livePercent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initMyAPI(BASE_URL);
        btnLogout = (Button) findViewById(R.id.logOut);

        chart2 = (BarChart) findViewById(R.id.barchart);
        chart2.clearChart();

        randomSet();



        // GoogleSignInOptions 개체 구성
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "Login fail");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(v.getContext());
                alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(false)
                        .setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        signOut();
                                    }
                                }).setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = alt_bld.create();
                alert.setTitle("로그아웃");                // 대화창 아이콘 설정

                alert.setIcon(R.drawable.common_google_signin_btn_icon_dark);

                // 대화창 배경 색 설정

                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,255,255)));
                alert.show();
                }
        });

        prefs = getSharedPreferences("Pref", MODE_PRIVATE);

        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                Log.d(TAG, "Progress Changed: " + value);
            }
        });


        mCircleView.setMaxValue(100);
        mCircleView.setOnTouchListener((v, event) -> true);

        mWebView = findViewById(R.id.webView);

        Button mStopWatch = findViewById(R.id.stopwatch);
        mStopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        StopWatch.class);
                startActivity(intent);
            }
        });


        Button buttonScan_In = (Button) findViewById(R.id.buttonScan_In);
        buttonScan_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(MainActivity.this, InCreateQR.class);
                startActivity(newIntent);
            }
        });

        Button buttonScan_Out = (Button) findViewById(R.id.buttonScan_Out);
        buttonScan_Out.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, OutCreateQR.class);
                startActivity(newIntent);
            }
        });


        initMyAPI(BASE_URL);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CustomAdapter customAdapter = new CustomAdapter();
        recyclerView.setAdapter(customAdapter);


            Call<List<PostItem>> getCall = mMyAPI.get_posts();
            getCall.enqueue(new Callback<List<PostItem>>() {
                @Override
                public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                    if (response.isSuccessful()) {

                        List<PostItem> mList = response.body();
                        arrayList.clear();

                        for (PostItem item : mList) {

                            int imageId = (int)(Math.random() * images.length);
                            PeopleList peopleList = new PeopleList();

                            peopleList.setId(images[imageId]);
                            String realName = item.getName();
                            String realRealName = realName.substring(0,1) + "**";//이름 변경 ex)강민규 >> 강**
                            peopleList.setName(realRealName);
                            peopleList.setMajor(item.getMajor());
                            Log.d(TAG, "Test_ex" + item.getEnter_time());

                            peopleList.setEnter_time(
                                    item.getEnter_time().substring(11,13) + "시 " +
                                    item.getEnter_time().substring(14,16) + "분 " ); //2020-09-29T13:18:33 2021/02/15 21:28:28
                            arrayList.add(peopleList);


                        }
                          customAdapter.notifyDataSetChanged();
                          mCircleView.setValue(mList.size());
                        } else {
                        Log.d(TAG, "Status Code : " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<PostItem>> call, Throwable t) {

                }
            });

            final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {


                    Call<List<PostItem>> getCall = mMyAPI.get_posts();
                    getCall.enqueue(new Callback<List<PostItem>>() {
                        @Override
                        public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                            if (response.isSuccessful()) {
                                List<PostItem> mList = response.body();
                                arrayList.clear();

                                for (PostItem item : mList) {

                                    int imageId = (int)(Math.random() * images.length);
                                    PeopleList peopleList = new PeopleList();

                                    peopleList.setId(images[imageId]);
                                    String realName = item.getName();
                                    String realRealName = realName.substring(0,1) + "**";//이름 변
                                    peopleList.setName(realRealName);
                                    peopleList.setMajor(item.getMajor());


                                    Log.d("TAG","item.getEnter_time" + item.getEnter_time());
                                    peopleList.setEnter_time(
                                            item.getEnter_time().substring(11,13) + "시 " +
                                            item.getEnter_time().substring(14,16) + "분 "); //2020-09-29T13:18:33

                                    arrayList.add(peopleList);

                            }

                                customAdapter.notifyDataSetChanged();

                                mCircleView.setValue(mList.size());
                                Toast.makeText(MainActivity.this, "인원이 최신화되었습니다." , Toast.LENGTH_SHORT).show();


                            } else {
                                Log.d(TAG, "Status Code : " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<PostItem>> call, Throwable t) {

                        }
                    });

                    swipeRefreshLayout.setRefreshing(false);

                }
            });


    }

    //로그아웃
    private void signOut(){

        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                mAuth.signOut();
                if(mGoogleApiClient.isConnected()){
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if(status.isSuccess()){
                                Log.d("TAG","User Logged Out");
                                setResult(1);
                            }
                            else {
                                setResult(0);
                            }

                            finish();
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("TAG","Google API Client Connection Suspended");
                setResult(-1);
                finish();
            }
        });
    }

    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }

    private void randomSet() {

        Call<List<PostItem>> getCall = mMyAPI.get_graph_posts();
        getCall.enqueue(new Callback<List<PostItem>>() {
            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                if (response.isSuccessful()) {
                    List<PostItem> mList = response.body();

                    for (PostItem item : mList) {
                        count += 1;
                        Log.d("TAG","count : " + count);
                        Log.d("TAG","EnterTime : " + item.getEnter_time());
                        String day = item.getEnter_time().substring(11,13);
                        int dayToInt = Integer.parseInt(day);
                        Log.d("TAG","dayToInt : " + dayToInt);
                        switch(dayToInt){
                            case 6:
                                Six += 1;
                                break;
                            case 7:
                                Seven += 1;
                                break;
                            case 8:
                                Eight += 1;
                                break;
                            case 13:
                                THIRTeen += 1;
                                Log.d("TAG","thirteen " + THIRTeen);

                                break;
                            case 14:
                                FourTeen += 1;
                                break;
                            case 15:
                                FifTeen += 1;
                                break;
                            case 16:
                                SixTeen += 1;
                                break;
                            case 17:
                                SevenTeen += 1;
                                break;
                            case 18:
                                EIGHTeen += 1;
                                break;
                            case 19:
                                NineTeen += 1;
                                break;
                            case 20:
                                Twenty += 1;
                                break;
                            case 21:
                                TwentyOne += 1;
                                break;
                        }

                    }


                    six = Float.parseFloat(Six);
                    seven = Float.parseFloat(Seven);
                    eight = Float.parseFloat(Eight);
                    thirteen = Float.parseFloat(THIRTeen);
                    fourteen = Float.parseFloat(FourTeen);
                    fifteen = Float.parseFloat(FifTeen);
                    sixteen = Float.parseFloat(SixTeen);
                    seventeen = Float.parseFloat(SevenTeen);
                    eighteen = Float.parseFloat(EIGHTeen);
                    nineteen = Float.parseFloat(NineTeen);
                    twenty = Float.parseFloat(Twenty);
                    twentyOne = Float.parseFloat(TwentyOne);

                    chart2.addBar(new BarModel("6시", six, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("7시", seven, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("8시", eight, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("13시", thirteen, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("14시", fourteen, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("15시", fifteen, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("16시", sixteen, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("17시", seventeen, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("18시", eighteen, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("19시", nineteen, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("20시", twenty, 0xFFFFB6C1));
                    chart2.addBar(new BarModel("21시", twentyOne, 0xFFFFB6C1));

                    chart2.startAnimation();

                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }

            private String toString(Calendar cal) {
                final String[] arrWeek = {"", "일", "월", "화", "수", "목", "금", "토"};
                int year = cal.get(Calendar.YEAR);
                int mon = cal.get(Calendar.MONTH)+1;
                int date = cal.get(Calendar.DATE);
                String weekDay = arrWeek[cal.get(Calendar.DAY_OF_WEEK)];

                return ( year + " 년 " + mon + "월" + date + "일 (" + weekDay + "요일)");

            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {

            }
        });
    }

    private String getDateDay(String day){
        String day_x ="";

        String day_num[] = day.split("-");
        String day_final = day_num[0] + day_num[1] + day_num[2];

        java.util.Date wantDate = null;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");

        try {
            wantDate = sdf.parse(day_final);
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(wantDate);
        int final_date = calendar.get(Calendar.DAY_OF_WEEK);

        switch(final_date){
            case 1:
                day_x = "일";
                break ;
            case 2:
                day_x = "월";
                break ;
            case 3:
                day_x = "화";
                break ;
            case 4:
                day_x = "수";
                break ;
            case 5:
                day_x = "목";
                break ;
            case 6:
                day_x = "금";
                break ;
            case 7:
                day_x = "토";
                break ;
        }

        return day_x;

    }




    public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_recyclerview_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            ((CustomViewHolder) holder).person_id.setImageResource(arrayList.get(position).getId());//Drawable(arrayList.get(position).getId());
            ((CustomViewHolder) holder).person_name.setText(arrayList.get(position).getName());
            ((CustomViewHolder) holder).person_major.setText(arrayList.get(position).getMajor());
            ((CustomViewHolder) holder).person_enter_time.setText(arrayList.get(position).getEnter_time());

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView person_id;
            TextView person_name;
            TextView person_major;
            TextView person_enter_time;

            public CustomViewHolder(View view) {
                super(view);
                person_id = (ImageView) view.findViewById(R.id.person_id);
                person_name = (TextView) view.findViewById(R.id.person_name);
                person_major = (TextView) view.findViewById(R.id.person_major);
                person_enter_time = (TextView) view.findViewById(R.id.person_enter_time);


            }
        }
    }

}