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

public class pWebView extends AppCompatActivity {
    private static final String TAG = "MyTAG";
    private WebView mWebView;
    SQLiteDatabase db;
    String tableName = "Information";
    MyAPI mMyAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String myUrl = intent.getStringExtra("webview_addr");     // 접속 URL (내장HTML의 경우 왼쪽과 같이 쓰고 아니면 걍 URL)

        Log.d(myUrl,"테스트1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotcheck_view);


        // 웹뷰 셋팅
        mWebView = (WebView) findViewById(webView);//xml 자바코드 연결
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용
        Log.d(myUrl,"테스트2");
        mWebView.loadUrl(myUrl);//웹뷰 실행
        Log.d(myUrl,"테스트3");
        mWebView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
        mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사



    }




    @Override
    public void onBackPressed() {

        if(mWebView.getUrl().equalsIgnoreCase("http://emoclew.pythonanywhere.com/new/")){
            putInformation();
            Log.d("TAG", "namemajor3" );
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

    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
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
//
//                    Log.d("TAG", "namemajor0" + name + major + phone);
//                    PutInformation putInformation = new PutInformation(name,major,phone,"se");
//
//                    Log.d("TAG", "putIn " + putInformation);
//                    Call<PutInformation> putCall = mMyAPI.putPost(phone, putInformation);
//
//                    Log.d("TAG", "namemajor1" + putCall);
//                    putCall.enqueue(new Callback<PutInformation>() {
//                        @Override
//                        public void onResponse(Call<PutInformation> call, Response<PutInformation> response) {
//                            if (!response.isSuccessful()) {
//
//                                Log.d("TAG", "namemajor2");
//                                return;
//                            }
//                            String content = "";
//                            content += "code: " + response.code() + "\n";
//                            content += "정상적으로 삽입되었습니다..";
//                        }
//                        @Override
//                        public void onFailure(Call<PutInformation> call, Throwable t) {
//                        }
//                    });


                    Log.d("TAG", "phone: " + phone);
                }
            }
        }

        db.close();
        databaseHelper.close();
    }

}
