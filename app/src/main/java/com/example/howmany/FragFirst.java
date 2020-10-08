package com.example.howmany;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    private static final int SHOW_POPUPS_ALL = 1;
    private String TAG = "TAG";
    private MyAPI mMyAPI;
    private final String BASE_URL = "http://emoclew.pythonanywhere.com";

    int Mon, Tue, Wed, Thu, Fri, Sat, Sun = 0;

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
        lineViewFloat.setShowPopup(LineView.SHOW_POPUPS_All);




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

    private void randomSet(final LineView lineViewFloat) {

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


                    ArrayList<Integer> dataListF = new ArrayList<>();
                    dataListF.add(Mon);
                    dataListF.add(Tue);
                    dataListF.add(Wed);
                    dataListF.add(Thu);
                    dataListF.add(Fri);
                    dataListF.add(Sat);
                    dataListF.add(Sun);



                    ArrayList<ArrayList<Integer>> dataListFs = new ArrayList<>();
                    dataListFs.add(dataListF);

                    lineViewFloat.setDataList(dataListFs);

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
