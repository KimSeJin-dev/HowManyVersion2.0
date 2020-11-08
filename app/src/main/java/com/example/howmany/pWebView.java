package com.example.howmany;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static com.example.howmany.R.id.webView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class pWebView extends AppCompatActivity {
    private static final String TAG = "MyTAG";
    private WebView mWebView;
    SQLiteDatabase db;
    String tableName = "Information";
    private final String BASE_URL = "http://emoclew.pythonanywhere.com";
    private MyAPI mMyAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String myUrl = intent.getStringExtra("webview_addr");     // 접속 URL (내장HTML의 경우 왼쪽과 같이 쓰고 아니면 걍 URL)

        Log.d(myUrl,"테스트1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotcheck_view);
        initMyAPI(BASE_URL);

        // 웹뷰 셋팅
        mWebView = (WebView) findViewById(webView);//xml 자바코드 연결
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용
        Log.d(myUrl,"테스트2");
        mWebView.loadUrl(myUrl);//웹뷰 실행
        Log.d(myUrl,"테스트3");
        mWebView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사



    }

    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }




    @Override
    public void onBackPressed() {

        if(mWebView.getUrl().equalsIgnoreCase("http://emoclew.pythonanywhere.com/new/")){
            putInformation();
            super.onBackPressed();
        }

        if (mWebView.getUrl().equalsIgnoreCase(" http://emoclew.pythonanywhere.com")
                || mWebView.getUrl().equalsIgnoreCase("http://emoclew.pythonanywhere.com/new2/")
        ) {
            super.onBackPressed();
        }else if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }

    private void putInformation(){
        Log.d("TAG", "namemajor4");
        DatabaseHelper databaseHelper = new DatabaseHelper(pWebView.this, "DB", null, 1);
        // 쓰기 가능한 SQLiteDatabase 인스턴스 구함
        db = databaseHelper.getWritableDatabase();


        Log.d("TAG", "namemajor5");
        try (Cursor cursor = db.query(tableName, null, null, null, null, null, null)) {
            if (cursor != null) {

                Log.d("TAG", "namemajor6");
                while (cursor.moveToNext()) {

                    String name = cursor.getString(cursor.getColumnIndex("NAME"));
                    String major = cursor.getString(cursor.getColumnIndex("MAJOR"));
                    String phone = cursor.getString(cursor.getColumnIndex("PHONE"));

                    Log.d( "TAG", "NAME= " + name + " "+ "MAJOR= " + major + " " + "PHONE= "  + phone);
                    Post post = new Post();
                    post.setName(name);
                    post.setMajor(major);
                    post.setPhone_num(phone);

                    Call<Post> call = mMyAPI.putData(phone, post);

                    Log.d("TAG", "namemajor10: ");
                    call.enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            if (!response.isSuccessful()){
                                Log.d("TAG", "namemajor7: " + response.message());
                                return;
                            }

                            Log.d("TAG", "namemajor9: ");
                            Post postResponse = response.body();

                            String content = "";
                            content += "Code : " + response.code() + "\n";
                            content += "Id : " + postResponse.getName() + "\n";
                            content += "User Id : " + postResponse.getMajor() + "\n";
                            content += "Title : " + postResponse.getPhone_num() + "\n";

                            Log.d("TAG", "content : " + content );

                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {
                            Log.d("TAG", "namemajor8: ");
                        }
                    });



                    Log.d("TAG", "phone: " + phone);
                }
            }
        }

        db.close();
        databaseHelper.close();
    }


    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.equals("http://emoclew.pythonanywhere.com/new2/"))
            {
                Toast.makeText(pWebView.this, "일일 그래프는 어플 재실행시 최신화됩니다." , Toast.LENGTH_SHORT).show();

            }
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }



}
