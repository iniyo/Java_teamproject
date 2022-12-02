package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    private Button login_btn; //로그인 버튼 ID
    private Button sign_btn; //회원가입 버튼 ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //로그인 버튼에 대한 화면전환
        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class); //로그인 activity에 대한 화면전환 객체
                startActivity(intent); //액티비티 이동.
            }
        });
        //회원가입 버튼에 대한 화면전환
        sign_btn = findViewById(R.id.logout_btn);
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, SignUpActivity.class); //회원가입 activity에 대한 화면전환 객체
                startActivity(intent); //액티비티 이동.
            }
        });
    }
}