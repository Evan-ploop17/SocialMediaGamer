package com.example.socialmediagamer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    TextView mTextViewRegister;
    TextInputEditText mTextinputEmail, mTextinputPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewRegister = findViewById(R.id.textViewRegister);
        mTextinputEmail = findViewById(R.id.textInputMail);
        mTextinputPassword = findViewById(R.id.textInputPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this , RegisterActivity.class);
                 startActivity(intent);
            }
        });
    }

    private void login(){
        String mail = mTextinputEmail.getText().toString();
        String pass = mTextinputPassword.getText().toString();
        Log.d("Prueba", "Mail " + mail);
        Log.d("Prueba", "Pass " + pass);
    }

}