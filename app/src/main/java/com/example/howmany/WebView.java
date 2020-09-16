package com.example.howmany;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

public class WebView extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String addr = intent.getStringExtra("webview_addr");
    }
}
