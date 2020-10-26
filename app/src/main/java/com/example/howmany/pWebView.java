package com.example.howmany;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import static com.example.howmany.R.id.webView;

import androidx.appcompat.app.AppCompatActivity;

public class pWebView extends AppCompatActivity {
    private static final String TAG = "MyTAG";
    private WebView mWebView;

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
        mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용

    }

    @Override
    public void onBackPressed() {
        if (mWebView.getUrl().equalsIgnoreCase(" http://emoclew.pythonanywhere.com")
                || mWebView.getUrl().equalsIgnoreCase("http://emoclew.pythonanywhere.com/new/")
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

}
