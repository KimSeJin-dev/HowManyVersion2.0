package com.example.howmany;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FirstInput extends AppCompatActivity {

    SQLiteDatabase db;
    private EditText editTextName;
    private EditText editTextMajor;
    private EditText editTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_input);

        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.login_dialog, null);

        editTextName = v.findViewById(R.id.editTextName);
        editTextMajor = v.findViewById(R.id.editTextMajor);
        editTextPhone = v.findViewById(R.id.editTextPhone);

        editTextPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = editTextName.getText().toString();
                String major = editTextMajor.getText().toString();
                String phone = editTextPhone.getText().toString();

                Log.d("TAG","msg1 " + phone);

                DatabaseHelper databaseHelper = new DatabaseHelper(FirstInput.this, "DB", null, 1);
                // 쓰기 가능한 SQLiteDatabase 인스턴스 구함
                db = databaseHelper.getWritableDatabase();
                dbInsert("Information", name,major,phone);
                db.close();
                databaseHelper.close();

                Intent intent = new Intent(FirstInput.this, MainActivity.class);
                Toast.makeText(FirstInput.this, "Phone : " + phone , Toast.LENGTH_SHORT).show();

                startActivity(intent);
                dialog.dismiss();
                //


            }
        });
        builder.show();


    }

    void dbInsert(String tableName, String name, String major, String phone) {
        Log.d("TAG", "Insert Data " + name + major + phone);

        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("MAJOR", major);
        contentValues.put("PHONE", phone);
        Log.d("TAG", "sejisejin : " + contentValues);

        // 리턴값: 생성된 데이터의 id
        long id = db.insert(tableName, null, contentValues);

        Log.d("TAG", "id: " + id);
    }



}