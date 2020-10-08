package com.example.howmany;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import im.dacer.androidcharts.LineView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragFirst extends Fragment {

    private String TAG = "TAG";
    private MyAPI mMyAPI;
    private final String BASE_URL = "http://emoclew.pythonanywhere.com";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_1p, container, false
        );

        final LineView lineViewFloat = (LineView) rootView.findViewById(R.id.line_view_float);
        initMyAPI(BASE_URL);
       initLineView(lineViewFloat);
       randomSet(lineViewFloat);
        return rootView;
    }


    private void initLineView(LineView lineView) {
        ArrayList<String> test = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            test.add("월");
            test.add("화");
            test.add("수");
            test.add("목");
            test.add("금");
            test.add("토");
            test.add("일");
        }
        lineView.setBottomTextList(test);
        lineView.setColorArray(new int[] {
                Color.parseColor("#F44336"), Color.parseColor("#9C27B0"),
                Color.parseColor("#2196F3"), Color.parseColor("#009688")
        });
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
    }

    private void randomSet(LineView lineViewFloat) {

        Call<List<PostItem>> getCall = mMyAPI.get_posts();
        getCall.enqueue(new Callback<List<PostItem>>() {
            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                if (response.isSuccessful()) {
                    List<PostItem> mList = response.body();

                    for (PostItem item : mList) {
                        String day = item.getEnter_time().substring(0,10);
                        String Date  = getDateDay(day);


                    }

                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {

            }
        });



        ArrayList<Integer> dataListF = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 9; i++) {
            dataListF.add(r.nextInt(10) + 1);
        }

        ArrayList<ArrayList<Integer>> dataListFs = new ArrayList<>();
        dataListFs.add(dataListF);

        lineViewFloat.setDataList(dataListFs);
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


    private void initMyAPI(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }

}
