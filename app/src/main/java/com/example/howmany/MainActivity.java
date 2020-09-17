package com.example.howmany;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import im.dacer.androidcharts.LineView;
import im.dacer.androidcharts.BarView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyTag";

  //  private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
   // private DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference().child("information");



    private LineView lineView;
    //view Objects
    private Button buttonScan;
    private TextView textViewName, textViewAddress, textViewResult;

    //qr code scanner object
    private IntentIntegrator qrScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);











        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            list.add(String.format("번호 %d ", i)) ;
            list.add(String.format("김세진 %d ", i)) ;
            list.add(String.format("1시간 32분 %d ", i)) ;

        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.

        RecyclerView recyclerView = findViewById(R.id.recyclerView) ;
        //구분선
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        PeopleInformationAdapter adapter = new PeopleInformationAdapter(list) ;
        recyclerView.setAdapter(adapter) ;


        /*
        리사이클러뷰 객체만들기
        */

        //View Objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //button onClick
        // qr코드 스캐너 버튼시
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
                qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });





        /*
        lineView = (LineView) findViewById(R.id.line_view);

        //list data
        List<AirQualityData> data = db.todayAirQualityData();

        //lable
        ArrayList<String> hour = new ArrayList<String>();
        //3 data sets

        ArrayList<Integer> dataList_10 = new ArrayList<>();
        ArrayList<Integer> dataList_2_5 = new ArrayList<>();
        ArrayList<Integer> dataList_1_0 = new ArrayList<>();

        //put db data into arrays
        for(AirQualityData datum : data) {
            hour.add(String.valueOf(datum.getHour()));
            dataList_10.add(datum.getPm10());
            dataList_2_5.add(datum.getPm2_5());
            dataList_1_0.add(datum.getPm1_0());
        }


        // put data sets into datalist
        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
        dataLists.add(dataList_10);
        dataLists.add(dataList_2_5);
        dataLists.add(dataList_1_0);


        //put data sets into datalist
        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();

        //draw line graph
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.ShOW_POPUPS_NONE);
        lineView.setColorArray(new int[]{
                Color.parseColor("e74c3c") , Color.parseColor("#2980b9"), Color.parseColor(("1abc9c"))
        });

        lineView.setBottomTextList(hour);
        lineView.setDataList(dataLists);
        */


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
                String address = "http://www.naver.com"; //obj.getString("address");// 주소 받아오기
                Intent intent_1 = new Intent(MainActivity.this, pWebView.class);
                intent_1.putExtra("webview_addr",address);
                startActivity(intent_1);
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            } else {
                //qrcode 결과가 있으면
                Log.d(TAG,"테스트1");
                Toast.makeText(MainActivity.this, "Scan Perfect", Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환
                    Log.d(TAG,"테스트2");
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
}