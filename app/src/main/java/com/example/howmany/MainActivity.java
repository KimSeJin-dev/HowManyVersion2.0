package com.example.howmany;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import im.dacer.androidcharts.LineView;
import im.dacer.androidcharts.BarView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyTag"; //로그 내용 보기
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private RecyclerView recyclerView;
    private PeopleInformationAdapter adapter; // 파이어베이스 어댑터 세팅
    List<Object> Array = new ArrayList<Object>();
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

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        final RecyclerView recyclerView = findViewById(R.id.recyclerView) ;

        initDatabase();

        ArrayList<String> list = new ArrayList<>();

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        final PeopleInformationAdapter adapter = new PeopleInformationAdapter(list) ;
        recyclerView.setAdapter(adapter) ;

        mReference = mDatabase.getReference("information");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot messageData : dataSnapshot.getChildren()){
                    String msg2 = messageData.getValue().toString();
                    Array.add(msg2);
            //        adapter.add(msg2);
                }
                adapter.notifyDataSetChanged();
               // recyclerView.setSelected(adapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        /*
        for (int i=0; i<100; i++) {
            list.add(String.format("번호 %d ", i)) ;
            list.add(String.format("김세진 %d ", i)) ;
            list.add(String.format("1시간 32분 %d ", i)) ;

        }
        */



        //구분선
        //recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;



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

    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("information");


        mChild = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
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





    public class PeopleInformationAdapter extends RecyclerView.Adapter<PeopleInformationAdapter.ViewHolder> {

        private ArrayList<String> mData = null ;


        // 아이템 뷰를 저장하는 뷰홀더 클래스.
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView_num;
            TextView textView_nick;
            TextView textView_time;

            ViewHolder(View itemView) {
                super(itemView) ;

                // 뷰 객체에 대한 참조. (hold strong reference)
                textView_num = itemView.findViewById(R.id.person_num) ;
                textView_nick = itemView.findViewById(R.id.person_nick) ;
                textView_time = itemView.findViewById(R.id.person_time) ;
            }
        }

        // 생성자에서 데이터 리스트 객체를 전달받음.
        PeopleInformationAdapter(ArrayList<String> list) {
            mData = list ;
        }

        // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
        @Override
        public PeopleInformationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext() ;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

            View view = inflater.inflate(R.layout.information_recyclerview_item, parent, false) ;
            PeopleInformationAdapter.ViewHolder vh = new PeopleInformationAdapter.ViewHolder(view) ;

            return vh ;
        }

        // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
        @Override
        public void onBindViewHolder(PeopleInformationAdapter.ViewHolder holder, int position) {
            String text = mData.get(position) ;

            holder.textView_num.setText(text) ;
            holder.textView_nick.setText(text) ;
            holder.textView_time.setText(text) ;
        }

        // getItemCount() - 전체 데이터 갯수 리턴.
        @Override
        public int getItemCount() {
            return mData.size() ;
        }
    }
}