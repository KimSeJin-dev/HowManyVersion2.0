package com.example.howmany;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class OutCreateQR extends AppCompatActivity {

    private ImageView iv;

    private String name,major,phoneNum,studentNum,email;
    SQLiteDatabase db;
    String tableName = "Information";
    private String resultString;
    private JSONObject result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_out_create_qr);

        iv = (ImageView)findViewById(R.id.qrcodeOut);

        putInformation();

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(resultString, BarcodeFormat.QR_CODE,330,330);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);
        }catch (Exception e){}

    }


    private void putInformation(){
        DatabaseHelper databaseHelper = new DatabaseHelper(OutCreateQR.this, "DB", null, 1);
        // 쓰기 가능한 SQLiteDatabase 인스턴스 구함
        db = databaseHelper.getWritableDatabase();
        try (Cursor cursor = db.query(tableName, null, null, null, null, null, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {

                    name = cursor.getString(cursor.getColumnIndex("NAME"));
                    major = cursor.getString(cursor.getColumnIndex("MAJOR"));
                    phoneNum = cursor.getString(cursor.getColumnIndex("PHONENUM"));
                    studentNum = cursor.getString(cursor.getColumnIndex("STUDENTNUM"));
                    email = cursor.getString(cursor.getColumnIndex("EMAIL"));

                    result = new JSONObject();
                    try {
                        result.put("구분","퇴장");
                        result.put("이름", name);
                        result.put("학과",major);
                        result.put("핸드폰번호", phoneNum);
                        result.put("학번",studentNum);
                        result.put("이메일", email);

                        resultString = result.toString();
                        resultString = URLEncoder.encode(resultString,"UTF-8");


                        Log.d("TAG","resultString :" + resultString);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
            else{
                Toast.makeText(OutCreateQR.this, "정보가 입력되지 않았습니다. 탈퇴 후 재 로그인해주세요." , Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e){
            Toast.makeText(OutCreateQR.this, "QR code 생성 실패." , Toast.LENGTH_SHORT).show();
        }

        db.close();
        databaseHelper.close();
    }


}