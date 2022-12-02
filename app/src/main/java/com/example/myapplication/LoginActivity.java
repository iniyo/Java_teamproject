package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;     //파이어베이스 인증처리
    private EditText  et_email, et_pw;     //로그인 입력필드
    private String email = "";
    private String password = "";
    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 파이어auth 인스턴스
        firebaseAuth = FirebaseAuth.getInstance();
        //기입항목
        et_email = findViewById(R.id.et_signEmail);
        et_pw = findViewById(R.id.et_signPW);


        // 성공적으로 로그인한 경우 가져올 수 있는 여러 메소드들
        /*if (user != null) { // 사용자가 등록되어 있는 경우
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            else {
                 // No user is signed in
            }
        }*/

        // 뒤로가기버튼 툴바 생성
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }


    // 툴바 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//옵션메뉴
        if (item.getItemId() == android.R.id.home) {//toolbar의 back키 눌렀을 때 동작
            // 액티비티 이동
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);//옵션메뉴 리턴
    }
    //회원가입 화면으로 이동
    public void stratSingup(View view ){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    // 로그인 성공시 액티비티 이동 메소드
    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(MainIntent); // 메인 화면으로 이동
            finish(); // 로그인창 닫음.
        }
    }
    // 현재 로그인 중인지 확인
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
    // 로그인 이벤트
    public void signIn(View view) {
        email = et_email.getText().toString();
        password = et_pw.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        }
    }
    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(LoginActivity.this, "이메일이 공백입니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(LoginActivity.this, "이메일 형식이 불일치 합니다.", Toast.LENGTH_SHORT).show();
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(LoginActivity.this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 로그인
    private void loginUser(String email, String password)
    {
        // 기존 사용자 등록되어있는지 조회함
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "성공적으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // 조회 시 일치하지 않을 경우
                        Toast.makeText(LoginActivity.this, "가입하지 않은 계정입니다.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }
}