package com.example.howmany;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Random;

import im.dacer.androidcharts.LineView;

public class FragFirst extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_1p, container, false
        );

        final LineView lineViewFloat = (LineView) rootView.findViewById(R.id.line_view_float);


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

        ArrayList<Integer> dataListF = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 9; i++) {
            dataListF.add(r.nextInt(10) + 1);
        }

        ArrayList<ArrayList<Integer>> dataListFs = new ArrayList<>();
        dataListFs.add(dataListF);

        lineViewFloat.setDataList(dataListFs);
    }
}
