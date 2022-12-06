package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button recruit_btn; // 모집하기 버튼
    private Button place_btn; // 장소게시판 버튼
    private Button freeboard_btn; // 자유게시판 버튼
    private Button refrash_btn; // 새로고침 버튼
    private ArrayList<Board> mBoardList = new ArrayList<Board>();
    private MainAdapter mainAdapter; // 메인 어댑터
    private RecyclerView mRecycler; // 메인 리사이클러 뷰
    private LinearLayoutManager linearLayoutManager; // 리니어 레이아웃 매니저
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어store 인스턴스 선언
    private Context context;
    private FirebaseAuth firebaseAuth;
    private UserAccount User; // 유저 UID 받아옴.
    private Board count= new Board();
    private DatabaseReference myRef; //realtime 데이터 베이스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, ForecdTerminationService.class));
        //리사이클러뷰 셋팅
        mRecycler = findViewById(R.id.main_RecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        //현재 컨텍스트
        context = this;
        // 인스턴스
        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어 스토어 데이터 읽어옴
        dbCollection();

        //새로고침 버튼 이벤트
        refrash_btn = findViewById(R.id.refrash_btn);
        refrash_btn.setOnClickListener(view -> dbCollection());

        //모집하기 버튼
        recruit_btn = findViewById(R.id.recruit_btn);
        recruit_btn.setOnClickListener(view -> {
            Intent recruitIntent = new Intent(MainActivity.this, RecruitActivity.class);
            startActivity(recruitIntent);
        });
        //장소게시판 버튼에 대한 화면전환
        place_btn = findViewById(R.id.place_btn);
        place_btn.setOnClickListener((View view) -> {
            Intent placeintent = new Intent(MainActivity.this, PlaceActivity.class); //장소게시판 activity에 대한 화면전환 객체
            startActivity(placeintent); //액티비티 이동.
        });
        //자유게시판 버튼에 대한 화면전환
        freeboard_btn = findViewById(R.id.freeboard_btn);
        freeboard_btn.setOnClickListener(view -> {
            Intent freeintent = new Intent(MainActivity.this, BoardActivity.class); //자유게시판 activity에 대한 화면전환 객체
            startActivity(freeintent); //액티비티 이동.
        });
    }
    //// 함수 선언

    //파이어 스토어 DB 데이터값 읽어와서 리사이클러뷰에 부착
    @SuppressLint("NotifyDataSetChanged")
    public void dbCollection(){
        db.collection("post")
                .get()
                .addOnCompleteListener(task -> {
                    mBoardList.clear(); // 리스트 공간 초기화
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Board Board = document.toObject(Board.class); // 오브젝트 형식으로 변환
                            Board.setId(document.getId());
                            mBoardList.add(Board);
                        }
                        mainAdapter = new MainAdapter(mBoardList,context);
                        mainAdapter.notifyDataSetChanged();
                        mRecycler.setAdapter(mainAdapter);
                        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        mRecycler.setLayoutManager(mLinearLayoutManager);
                    } else {
                        // toast 메시지 넣을 공간
                        Toast.makeText(context, " DB를 불러오지 못했습니다. ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void SignOut(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("정말로 로그아웃하시겠습니까?");
        builder.setTitle("로그아웃 알림창")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        signOut(); // 로그아웃 메소드
                        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
                        startActivity(startIntent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("로그아웃 알림창");
        alert.show();
    }

    public void Exit(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("정말로 종료하시겠습니까?");
        builder.setTitle("종료 알림창")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("종료 알림창");
        alert.show();
    }

    // 로그아웃
    private void signOut() {
        firebaseAuth.signOut();
    }
    
    // 회원탈퇴
    public void revokeAccess(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("정말로 회원탈퇴 하시겠습니까?");
        builder.setTitle("회원탈퇴 알림창")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, i) -> {
                    db.collection("UserInfo") //컬렉션 유저정보 선택
                            .get() // 데이터 가져옴
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        UserAccount User = document.toObject(UserAccount.class); // 오브젝트 형식으로 변환.
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if(User.getIdToken().equals(user.getUid())){
                                            firebaseAuth.getCurrentUser().delete(); // 회원탈퇴 메소드
                                            builder.setTitle(" 성공적으로 회원탈퇴 되었습니다. ");
                                        }
                                    }
                                }else{
                                    builder.setTitle(" 목록을 불러오기 실패했습니다.");
                                }
                            });
                    Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
                    startActivity(startIntent);
                    finish();
                })
                .setNegativeButton("No", (dialog, i) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.setTitle("회원탈퇴 알림창");
        alert.show();

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
    // 뒤로가기 이벤트 (앱 내부 아님)
    public boolean onKeyDown(int Keycode, KeyEvent event){
        if(Keycode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("정말로 로그아웃하시겠습니까?");
            builder.setTitle("로그아웃 알림창")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, i) -> {
                        signOut();
                        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
                        startActivity(startIntent);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, i) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.setTitle("로그아웃 알림창");
            alert.show();
            return true;
        }
        return false;
    }
}
