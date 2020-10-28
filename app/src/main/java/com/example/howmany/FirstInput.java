package com.example.howmany;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.database.FirebaseDatabase;

public class FirstInput extends AppCompatActivity {

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String major = editTextMajor.getText().toString();
                String phone = editTextPhone.getText().toString();

                dbInsert("Information", name, major , phone);
                Toast.makeText(FirstInput.this, "Name : "+ name +"\nMajor : " + major +"\nPhone : " + phone , Toast.LENGTH_SHORT).show();


                DatabaseHelper databaseHelper = new DatabaseHelper(FirstInput.this, "DB", null, 1);
                // 쓰기 가능한 SQLiteDatabase 인스턴스 구함
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                db.close();
                databaseHelper.close();

                Intent intent = new Intent(FirstInput.this, MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.show();

    }




    void dbInsert(String tableName, String name, String major, String phone) {
        Log.d("TAG", "Insert Data " + name);

        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("MAJOR", major);
        contentValues.put("PHONE", phone);

        Log.d("TAG","Table : " + contentValues);

    }


}