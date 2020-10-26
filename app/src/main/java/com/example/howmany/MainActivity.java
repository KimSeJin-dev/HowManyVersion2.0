package com.example.howmany;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import at.grabner.circleprogress.CircleProgressView;
import im.dacer.androidcharts.BarView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    int Mon, Tue, Wed, Thu, Fri, Sat, Sun = 0;
    private final String TAG = getClass().getSimpleName();
    ArrayList<PeopleList> arrayList = new ArrayList<>();
    private final String BASE_URL = "http://emoclew.pythonanywhere.com";
    private MyAPI mMyAPI;
    public static WebView mWebView;

    private IntentIntegrator qrScan;

    int[] images =  new int[] {R.drawable.health1, R.drawable.health2,
    R.drawable.health3, R.drawable.health4, R.drawable.health45,R.drawable.health6,
    R.drawable.health7, R.drawable.health8, R.drawable.health9};

    Fragment fragfirst;


    CircleProgressView mCircleView; // livePercent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        fragfirst = new FragFirst();

        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                Log.d(TAG, "Progress Changed: " + value);
            }
        });

        mCircleView.setMaxValue(100);
        mWebView = findViewById(R.id.webView);

        final BarView barView = (BarView) findViewById(R.id.line_view_float);

        initMyAPI(BASE_URL);
        initLineView(barView);
        randomSet(barView);
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



        qrScan = new IntentIntegrator(this);
        Button buttonScan_In = (Button) findViewById(R.id.buttonScan_In);
        buttonScan_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
                qrScan.setOrientationLocked(false);
                qrScan.setPrompt("스캐너에 QR코드를 위치시켜주세요");


            }
        });

        Button buttonScan_Out = (Button) findViewById(R.id.buttonScan_Out);
        buttonScan_Out.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
                qrScan.setOrientationLocked(false);
                qrScan.setPrompt("스캐너에 QR코드를 위치시켜주세요");

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
                            peopleList.setName(item.getName());
                            peopleList.setMajor(item.getMajor());
                            Log.d(TAG, "Test_ex" + item.getEnter_time());
                            peopleList.setEnter_time(
                                    item.getEnter_time().substring(11,13) + "시 " +
                                    item.getEnter_time().substring(14,16) + "분 " ); //2020-09-29T13:18:33
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
                                    peopleList.setName(item.getName());
                                    peopleList.setMajor(item.getMajor());

                                    peopleList.setEnter_time(
                                            item.getEnter_time().substring(11,13) + "시 " +
                                            item.getEnter_time().substring(14,16) + "분 "); //2020-09-29T13:18:33

                                    arrayList.add(peopleList);

                            }

                                customAdapter.notifyDataSetChanged();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.detach(fragfirst).attach(fragfirst).commit();

                                mCircleView.setValue(mList.size());

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


    private void initLineView(BarView barView) {
        ArrayList<String> test = new ArrayList<String>();

        test.add("월");
        test.add("화");
        test.add("수");
        test.add("목");
        test.add("금");
        test.add("토");
        test.add("일");

        barView.setBottomTextList(test);


    }


    private void randomSet(final BarView barViewFloat) {

        Call<List<PostItem>> getCall = mMyAPI.get_posts();
        getCall.enqueue(new Callback<List<PostItem>>() {
            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                if (response.isSuccessful()) {
                    List<PostItem> mList = response.body();

                    for (PostItem item : mList) {
                        String day = item.getEnter_time().substring(0,10);
                        String Date  = getDateDay(day);
                        Log.d(TAG, "example : " + Date);
                        if(Date == "월"){ Mon++; }
                        else if(Date == "화"){ Tue++; }
                        else if(Date == "수"){ Wed++; }
                        else if(Date == "목"){ Thu++; }
                        else if(Date == "금"){ Fri++; }
                        else if(Date == "토"){ Sat++; }
                        else if(Date == "일"){ Sun++; }
                        Log.d(TAG, "example2 : " + Fri);
                    }

                    TextView textView_mon = (TextView) findViewById(R.id.live_mon);
                    TextView textView_tue = (TextView) findViewById(R.id.live_tue);
                    TextView textView_wed = (TextView) findViewById(R.id.live_wen);
                    TextView textView_thur = (TextView) findViewById(R.id.live_thur);
                    TextView textView_fri = (TextView) findViewById(R.id.live_fri);
                    TextView textView_sat = (TextView) findViewById(R.id.live_sat);
                    TextView textView_sun = (TextView) findViewById(R.id.live_sun);

                    textView_mon.setText("월 : "+Mon + "명");
                    textView_tue.setText("화 : " + Tue + "명");
                    textView_wed.setText("수 : " + Wed +"명");
                    textView_thur.setText("목 : " + Thu + "명");
                    textView_fri.setText("금 : " + Fri + "명");
                    textView_sat.setText(" : " + Sat +"명");
                    textView_sun.setText("일 : " + Sun + "명");



                    ArrayList<Integer> dataListF = new ArrayList<>();
                    dataListF.add(Mon);
                    dataListF.add(Tue);
                    dataListF.add(Wed);
                    dataListF.add(Thu);
                    dataListF.add(Fri);
                    dataListF.add(Sat);
                    dataListF.add(Sun);

                    barViewFloat.setDataList(dataListF,100);
                    Mon = 0;
                    Tue = 0;
                    Wed = 0;
                    Thu = 0;
                    Fri = 0;
                    Sat = 0;
                    Sun = 0;

                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
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
                day_x = "";
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


    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }
    //Getting the scan results
    //qr코드 승인 , qr코드 없을시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            } else {
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "Scan Perfect", Toast.LENGTH_SHORT).show();
                //data를 json으로 변환
                String address_1 = result.getContents();// 주소 받아오기
                Intent intent = new Intent(MainActivity.this, pWebView.class);
                intent.putExtra("webview_addr",address_1);
                startActivity(intent);

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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