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

    private EditText editTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_input);

        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.login_dialog, null);

        editTextPhone = v.findViewById(R.id.editTextPhone);

        editTextPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String phone = editTextPhone.getText().toString();


                DatabaseHelper databaseHelper = new DatabaseHelper(FirstInput.this, "DB", null, 1);
                // 쓰기 가능한 SQLiteDatabase 인스턴스 구함
                db = databaseHelper.getWritableDatabase();
                db.close();
                databaseHelper.close();

                Intent intent = new Intent(FirstInput.this, MainActivity.class);
                Toast.makeText(FirstInput.this, "Phone : " + phone , Toast.LENGTH_SHORT).show();

                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.show();

    }





    }