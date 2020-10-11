package com.example.howmany;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import at.grabner.circleprogress.CircleProgressView;
import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.widget.ImageView;
import android.graphics.drawable.Drawable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    ArrayList<PeopleList> arrayList = new ArrayList<>();
    private final String BASE_URL = "http://emoclew.pythonanywhere.com";
    private MyAPI mMyAPI;
    public static WebView mWebView;
    private Button buttonScan;
    private Button mStopWatch;


    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 2;
    private CircleIndicator3 mIndicator;

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

        fragfirst = new FragFirst();

        //ViewPager2
        mPager = findViewById(R.id.viewpager);
        //Adapter
        pagerAdapter = new MyAdapter(this, num_page);
        mPager.setAdapter(pagerAdapter);
        //Indicator
        mIndicator = findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0);
        //ViewPager Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(1000);
        mPager.setOffscreenPageLimit(3);



        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                Log.d(TAG, "Progress Changed: " + value);
            }
        });

        mCircleView.setMaxValue(100);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
            }

        });

        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (mPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(mPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });


        mWebView = findViewById(R.id.webView);

        mStopWatch = findViewById(R.id.stopwatch);
        mStopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        StopWatch.class);
                startActivity(intent);
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
                        int num_1 = 1;
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
                            num_1++;

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
                                int num = 1;
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
                                    num++;
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

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.detach(fragfirst).attach(fragfirst).commit();

                    swipeRefreshLayout.setRefreshing(false);


                }
            });


        buttonScan = (Button) findViewById(R.id.buttonScan_In);
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

        	public static String utcToLocaltime(String datetime) throws Exception {
        	    String locTime = null;
        	    //TimeZone tz = TimeZone.getTimeZone("GMT+08:00"); 해당 국가 일시 확인 할 때, 한쿸은 +9
        	    TimeZone tz = TimeZone.getDefault();
        	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        	    try {
        	        Date parseDate = sdf.parse(datetime);
        	        long milliseconds = parseDate.getTime();
        	        int offset = tz.getOffset(milliseconds);
        	        locTime = sdf.format(milliseconds + offset);
        	        locTime = locTime.replace("+0000", "");
        	    } catch(Exception e) {
        	        e.printStackTrace();
        	              }

        	    return locTime;
        	}




    @Override
    public void onClick(View v) {
        if( v == buttonScan) { //qr코드 버튼 클릭시
            //scan option
            qrScan = new IntentIntegrator(this);
            qrScan.setPrompt("Scanning...");
            qrScan.setOrientationLocked(false);
            qrScan.initiateScan();
        }

        else if( v == mStopWatch){

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
//                Log.d(TAG,"테스트0");
//                String address = "http://emoclew.pythonanywhere.com/"; //obj.getString("address");// 주소 받아오기
//                Intent intent_1 = new Intent(MainActivity.this, pWebView.class);
//                intent_1.putExtra("webview_addr",address);
//                startActivity(intent_1);
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            } else {
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "Scan Perfect", Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환
                     JSONObject obj = new JSONObject(result.getContents());
                    String address_1 = "m.naver.com"; //obj.getString("address");// 주소 받아오기
                    Intent intent = new Intent(MainActivity.this, pWebView.class);
                    intent.putExtra("webview_addr",address_1);

                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        int imageId = (int)(Math.random() * images.length);// 리사이클러뷰


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