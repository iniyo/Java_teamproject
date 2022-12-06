package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; // 파이어베이스 인증 객체 생성
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어store 인스턴스 선언
    private EditText  et_email, et_pw, et_pw2, et_signName;      //회원가입 입력필드
    private Button pwcheck, submit, codecom, cert;      //비밀번호 같은지확인, 회원가입버튼, 인증코드버튼, 이메일 인증보내는 버튼
    private UserAccount User = new UserAccount();
    private Context sContext;

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    private ArrayList<UserAccount> userlist = new ArrayList<UserAccount>();
    private String email = "";
    private String password = "";
    private String signName = "";
    private String pwck = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();


        //기입항목
        et_email = findViewById(R.id.et_signEmail);
        et_pw = findViewById(R.id.et_signPW);
        et_pw2 = findViewById(R.id.et_signPW2);
        et_signName = findViewById(R.id.et_signName);
        //회원가입 버튼
        submit = findViewById(R.id.signupbutton);


        //비밀번호 확인 버튼
        pwcheck = findViewById(R.id.pwcheckbutton);
        pwcheck.setOnClickListener(view -> {
            if(et_pw.getText().toString().equals(et_pw2.getText().toString())){
                pwcheck.setText("일치");
                Toast.makeText(SignUpActivity.this, "비밀번호가 일치합니다.", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(SignUpActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }
        });

        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }
    // 뒤로가기 버튼 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//옵션메뉴
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);//옵션메뉴 리턴
    }

    // 회원가입 성공시 액티비티 이동 메소드
    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            //Intent UserInfoIntent = new Intent(SignUpActivity.this, UserInfoActivity.class);
            //startActivity(UserInfoIntent); // 유저 정보화면으로 인텐트 닉네임 등 설정.
            finish(); // 회원가입창 닫음.
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

    // 회원가입 메소드
    public void singUp(View view) {
        email = et_email.getText().toString(); // 이메일
        password = et_pw2.getText().toString(); // 패스워드
        signName = et_signName.getText().toString(); //이름
        pwck = pwcheck.getText().toString(); // 패스워드 체크 여부
        if(isValidEmail() && isValidPasswd() && isVaildName()) {
            createUser(email, password);
        }
    }
    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(SignUpActivity.this, "이메일이 공백입니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignUpActivity.this, "이메일 형식이 불일치 합니다.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SignUpActivity.this, "비밀번호가 공백입니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            Toast.makeText(SignUpActivity.this, "비밀번호 형식이 불일치 합니다.", Toast.LENGTH_SHORT).show();
            // 비밀번호 형식 불일치
            return false;
        } else if(pwck.isEmpty()){
            Toast.makeText(SignUpActivity.this, "비밀번호 확인을 해주세요!", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            return true;
        }
    }
    // 이름 넣었는지 검사
    private boolean isVaildName(){
        if (signName.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    // 회원가입
    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            User.setIdToken(task.getResult().getUser().getUid()); // 유저 UID 저장.
                            User.setEmailI(et_email.getText().toString()+"@office.deu.ac.kr"); // 유저 이메일 저장
                            User.setPassword(et_pw.getText().toString()); // 유저 pw저장
                            User.setCheck_count(0); //카운트 저장
                            save_User(User); // 유저 데이터 파이어스토어에 넣을 함수
                            Toast.makeText(SignUpActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            updateUI(user); // 회원가입 성공시 화면전환
                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignUpActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                            updateUI(null); // 회원가입 실패시 화면전환
                        }
                    }
                });
    }
    // 파이어 스토어 유저 데이터 넣는 함수.
    private void save_User(UserAccount User){
        db.collection("UserInfo") //UserInfo 데이터 베
                .document()
                .set(User, SetOptions.merge()); // UserInfo 데이터 넣음.
    }
}

