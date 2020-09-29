package com.example.howmany;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    ArrayList<PeopleList> arrayList = new ArrayList<>();
    private final String BASE_URL = "http://emoclew.pythonanywhere.com";
    private MyAPI mMyAPI;
    private TextView mListTv;
    public static WebView mWebView;
    private Button buttonScan;
    private Button mliveCount;
    private TextView textViewName, textViewAddress, textViewResult;
    private IntentIntegrator qrScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webView);

        mliveCount = findViewById(R.id.livecount);
        mliveCount.setOnClickListener(this);


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

                            PeopleList peopleList = new PeopleList();
                            peopleList.setId(item.getId());
                            peopleList.setName(item.getName());
                            peopleList.setMajor(item.getMajor());
                          //  peopleList.setEnter_time(getDateTimeAddLocalTimezone(item.getEnter_time()));
                            arrayList.add(peopleList);
                        }
                        customAdapter.notifyDataSetChanged();
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
                                    PeopleList peopleList = new PeopleList();
                                    peopleList.setId(item.getId());
                                    peopleList.setName(item.getName());
                                    peopleList.setMajor(item.getMajor());
                                   // peopleList.setEnter_time(getDateTimeAddLocalTimezone(item.getEnter_time()));
                                    arrayList.add(peopleList);
                            }
                                customAdapter.notifyDataSetChanged();
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


        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(this);


    }

    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }

    private static String format(String format, Date date)
    {
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(date);
    }

//    public static String getDateTimeAddLocalTimezone(String date){
//        Date d;
//        try {
//            d = new SimpleDateFormat(item.).parse(date);
//            Time t = new Time();
//            Long l = t.normalize(t.isDst==0); // -32400000밀리초
//            Long between = l/1000/60/60; // -9시간
//            d.setHours(d.getHours() - Long.valueOf(between).intValue());
//            return format(formatDateTime, d);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//
//    }




    @Override
    public void onClick(View v) {
        if( v == buttonScan) { //qr코드 버튼 클릭시
            //scan option
            qrScan = new IntentIntegrator(this);
            qrScan.setPrompt("Scanning...");
            qrScan.setOrientationLocked(false);
            qrScan.initiateScan();
        }

        else if( v == mliveCount){

        }
    }


    //Getting the scan results
    //qr코드 승인 , qr코드 없을시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

           //qrcode 가 없으면
           /*String address = "www.naver.com"; //obj.getString("address");// 주소 받아오기
            Intent intent_1 = new Intent(MainActivity.this, pWebView.class);
            intent_1.putExtra("webview_addr",address);
            startActivity(intent_1);
            */
            if (result.getContents() == null) {
                Log.d(TAG,"테스트0");
                String address = "http://emoclew.pythonanywhere.com/"; //obj.getString("address");// 주소 받아오기
                Intent intent_1 = new Intent(MainActivity.this, pWebView.class);
                intent_1.putExtra("webview_addr",address);
                startActivity(intent_1);
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            } else {
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "Scan Perfect", Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환
                     JSONObject obj = new JSONObject(result.getContents());
                    textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));
                    String address_1 = "m.naver.com"; //obj.getString("address");// 주소 받아오기
                    Intent intent = new Intent(MainActivity.this, pWebView.class);
                    intent.putExtra("webview_addr",address_1);

                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    textViewResult.setText(result.getContents());
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> { // 리사이클러뷰


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_recyclerview_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            ((CustomViewHolder) holder).person_id.setText(arrayList.get(position).getId());
            ((CustomViewHolder) holder).person_name.setText(arrayList.get(position).getName());
            ((CustomViewHolder) holder).person_major.setText(arrayList.get(position).getMajor());
            ((CustomViewHolder) holder).person_enter_time.setText(arrayList.get(position).getEnter_time());



        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView person_id;
            TextView person_name;
            TextView person_major;
            TextView person_enter_time;

            public CustomViewHolder(View view) {
                super(view);
                person_id = (TextView) view.findViewById(R.id.person_id);
                person_name = (TextView) view.findViewById(R.id.person_name);
                person_major = (TextView) view.findViewById(R.id.person_major);
                person_enter_time = (TextView) view.findViewById(R.id.person_enter_time);


            }
        }
    }

}