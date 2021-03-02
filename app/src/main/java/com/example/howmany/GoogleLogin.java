package com.example.howmany;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import im.dacer.androidcharts.BarView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//구글 로그인 및 등록된 회원인지 확인 기능
public class GoogleLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SQLiteDatabase db;
    private SignInButton btn_google;
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;
    private static final int REQ_SIGN_GOOGLE = 100;
    private Animation anim;
    private TextView textHello, textKorea, textExample;

    private final String BASE_URL = "http://163.152.223.34:8000/";
    private MyAPI mMyAPI;

    private String name,major,phone_num,student_num,email,reserve_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        textHello = (TextView) findViewById(R.id.textHello);
        textKorea = (TextView) findViewById(R.id.textKorea);
        textExample = (TextView) findViewById(R.id.textExample);

        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myanim);

        textHello.startAnimation(anim);
        textKorea.startAnimation(anim);
        textExample.startAnimation(anim);

        //구글로그인시 필
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance();


        //등록된 회원일 시 이 화면 패스 기능
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }


        btn_google = findViewById(R.id.signInButton);
        btn_google.startAnimation(anim);
        btn_google.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplication(), MainActivity.class);
//                startActivity(intent);
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient); // 서버연결됐을씨 주석해제
                startActivityForResult(intent,REQ_SIGN_GOOGLE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){ // 인증결과가 성공적이
                GoogleSignInAccount account = result.getSignInAccount(); // account 라는 데이터는 구글 로그인 정보를 담고있습니다.(닉네임,프로필,,,,이메일주서,,, 등등)
                resultLogin(account); // 로그인 결과 값 출력 수행하라는 메소드
            }

        }

    }
    private void resultLogin(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){// 로그인 성공시
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                            Log.d("TAG","getEmail : " + account.getEmail());
                            String getEmail = account.getEmail();
                            //sejin2740@korea.ac.kr

                            String real_email = getEmail.substring(0,getEmail.length()-6);
                            Log.d("TAG","real_email : "+ real_email);


                            initMyAPI(BASE_URL);

                            mMyAPI.get_excelDb(real_email).enqueue(new Callback<ExcelDB>() {
                                @Override
                                public void onResponse(Call<ExcelDB> call, Response<ExcelDB> response) {
                                    Log.d("TAG","response: " + response.code());
                                    if(response.isSuccessful()){
                                        ExcelDB body = response.body();
                                        if(body != null){

                                            Log.d("TAG", "이름 : " + body.getName());
                                            Log.d("TAG", "학과 : " + body.getMajor());
                                            Log.d("TAG", "핸드폰번호 : " + body.getPhone_num());
                                            Log.d("TAG", "학번 : " + body.getStudent_num());
                                            Log.d("TAG", "이메일 : " + body.getEmail());
                                            Log.d("TAG","이용월 : " + body.getReserve_product());

                                            name = body.getName();
                                            major = body.getMajor();
                                            phone_num = body.getPhone_num();
                                            student_num = body.getStudent_num();
                                            email = body.getEmail();
                                            reserve_product = body.getReserve_product();


                                            DatabaseHelper databaseHelper = new DatabaseHelper(GoogleLogin.this,"DB",null,1);

                                            db = databaseHelper.getWritableDatabase();
                                            dbInsert("Information", name,major,phone_num,student_num,email);

                                            db.close();
                                            databaseHelper.close();
                                            Toast.makeText(getApplicationContext(),"로그인 성공!", Toast.LENGTH_LONG).show();

                                            startActivity(intent);
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"등록된 회원이 아닙니다.", Toast.LENGTH_LONG).show();
                                        signOut();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ExcelDB> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(),"통신에러", Toast.LENGTH_LONG).show();

                                }
                            });

                        } else { // 로그인이 실패
                            Toast.makeText(getApplicationContext(),"로그인실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"로그인실패", Toast.LENGTH_LONG).show();
    }

    private void initMyAPI(String baseUrl){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }


    //정보 DB에 집어넣기
    void dbInsert(String tableName, String name,String major,String phone_num,String student_num,String email){
        ContentValues contentValues = new ContentValues();

        contentValues.put("NAME", name);
        contentValues.put("MAJOR", major);
        contentValues.put("PHONENUM", phone_num);
        contentValues.put("STUDENTNUM", student_num);
        contentValues.put("EMAIL", email);

        // 리턴값: 생성된 데이터의 id
        long id = db.insert(tableName, null, contentValues);

    }

    //로그아웃
    private void signOut(){

        googleApiClient.connect();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                auth.signOut();
                if(googleApiClient.isConnected()){
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if(status.isSuccess()){
                                Log.d("TAG","User Logged Out");
                                setResult(1);
                            }
                            else {
                                setResult(0);
                            }

                            finish();
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("TAG","Google API Client Connection Suspended");
                setResult(-1);
                finish();
            }
        });
    }
}