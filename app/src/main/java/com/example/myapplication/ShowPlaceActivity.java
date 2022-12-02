package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ShowPlaceActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어store 인스턴스 선언
    private FirebaseAuth firebaseAuth;     //파이어베이스 인증처리
    private TextView title_txt, solo_together;
    private Context shcontext;
    private PlaceAdapter placeAdapter; //어댑터
    private MainAdapter mainAdapter; // 메인 어댑터
    private final ArrayList<Board> ShowBoardList = new ArrayList<>();
    private Button delevery_to_btn, delevery_cancel_btn, chat_btn;
    private String id, place_name, showtitle, showpeople, showinfo, dateview, solo, together, now_people, userid ="";
    private int count, peopleNum, count2;
    private boolean to_so;
    private UserAccount check_count = new UserAccount();
    private Board check_uid = new Board(); ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_place);

        //파이어베이스
        firebaseAuth = FirebaseAuth.getInstance();


        // 사용하는 모든 텍스트들
        TextView show_title = findViewById(R.id.show_title); // 제목
        TextView show_people = findViewById(R.id.show_people); //총인원 수
        TextView show_info = findViewById(R.id.show_info); //보여줄 내용
        TextView show_placename = findViewById(R.id.show_placename); //장소이름
        TextView date_view1 = findViewById(R.id.date_view1); //시간
        solo_together = findViewById(R.id.solo_together); // 같이 먹는지 따로 먹는지
        delevery_to_btn = findViewById(R.id.delevery_to_btn);
        delevery_cancel_btn = findViewById(R.id.delevery_cancel_btn);
        title_txt = findViewById(R.id.title_txt);
        chat_btn = findViewById(R.id.chat_btn);

        shcontext = this;

        //인텐트시 정보 받아옴.
        Intent intent = getIntent();
        place_name = intent.getStringExtra("place_name"); // 장소
        showtitle = intent.getStringExtra("title_info"); // 제목
        showpeople = intent.getStringExtra("people_number"); // 총인원
        showinfo = intent.getStringExtra("delevery_info"); // 상세 정보
        dateview = intent.getStringExtra("time"); // 시키는 시간
        solo = intent.getStringExtra("solo");// 혼자
        together = intent.getStringExtra("together");// 같이
        if(solo!=null){
            solo_together.setText(solo);
        }else if(together!=null){
            solo_together.setText(together);
        }
        id = intent.getStringExtra("id"); // id 값
        now_people = intent.getStringExtra("count"); // 현재 인원

        // 유저에게 보여지는 정보 셋팅
        show_title.setText(showtitle); // 제목
        show_placename.setText("<"+place_name+">"); // 장소
        show_people.setText(now_people+"/"+showpeople); // 총인원
        //show_info.setText(); // 상세정보
        date_view1.setText(dateview); // 시간



        //채팅 클릭 이벤트
        chat_btn.setOnClickListener(view -> {
            Intent ChatIntent = new Intent(ShowPlaceActivity.this, ChatActivity.class); //장소게시판 액티비티에 전달
            startActivity(ChatIntent);//화면전환
        });

        //같이먹기 버튼 이벤트
        delevery_to_btn.setOnClickListener(view -> {
            count = Integer.parseInt(now_people); //현재 등록된 인원 저장
            peopleNum = Integer.parseInt(showpeople.split(" ")[0]); // 총인원 저장
            to_so = true;
            if(peopleNum != count) {
                count_update();
            }
            else{
                Toast.makeText(shcontext, " 인원이 다 찼습니다. ", Toast.LENGTH_SHORT).show();
            }
        });

        //취소 버튼 이벤트
        delevery_cancel_btn.setOnClickListener(view -> {
            count = Integer.parseInt(now_people); //현재 등록된 인원 저장
            peopleNum = Integer.parseInt(showpeople.split(" ")[0]); // 총인원 저장
            to_so = false;
            count_update(); // update함수에서 db 업데이트 이미 신청한 적이 있는 경우 불가
        });


        // 뒤로가기버튼 툴바
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }
    //툴바 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//옵션메뉴
        if (item.getItemId() == android.R.id.home) {//toolbar의 back키 눌렀을 때 동작
            // 액티비티 이동
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);//옵션메뉴 리턴*/
    }

    // 저장되어 있는 유저 정보를 가져오고 카운트 update
    public void count_update(){
        db.collection("UserInfo") //컬렉션 유저정보 선택
                .get() // 데이터 가져옴
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserAccount User = document.toObject(UserAccount.class); // 오브젝트 형식으로 변환.
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(User.getIdToken().equals(user.getUid())){
                                userid = document.getId();
                                count2 = User.getCheck_count(); // 저장되어있는 유저 카운트를 가지고 온다.
                                if(to_so) {
                                    if (count2 == 0) {
                                        count2 = 1; // 신청 카운트를 1로
                                        count += 1; // +1
                                        now_people = String.valueOf(count); // 증가된 값 저장(총인원)
                                        updateinfo(); // 정보 업데이트
                                    } else if (count2 == 1) {
                                        // 정보 업데이트 안됨.
                                        Toast.makeText(ShowPlaceActivity.this, "이미 신청한 적이 있습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ShowPlaceActivity.this, "에러 발생 관리자에게 문의하세요", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(!to_so){
                                    if (count2 == 1) {
                                        count2 = 0; // 신청 카운트를 1로
                                        count -= 1; // -1
                                        now_people = String.valueOf(count); // 감소된 값 저장(총인원)
                                        updateinfo(); // 정보 업데이트
                                    } else if (count2 == 0) {
                                        // 정보 업데이트 안됨.
                                        Toast.makeText(ShowPlaceActivity.this, "음식 신청을 해주세요", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ShowPlaceActivity.this, "에러 발생 관리자에게 문의하세요", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                });
    }
    
    // 파이어베이스 db에 데이터 업데이트 함.
    public void updateinfo(){

        // db 업데이트
        db.collection("UserInfo")
                .document(userid)//doc id로 해당문서 수정
                .update("check_count", count2);

        db.collection("post")
                .document(id)//doc id로 해당문서 수정
                .update("count", now_people);

        // 리사이클러뷰 업데이트
        db.collection("post") //컬렉션 게시물 선택
                .get() // 데이터 가져옴
                .addOnCompleteListener(task -> {
                    ShowBoardList.clear(); // 리스트 공간 초기화
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Board Board = document.toObject(Board.class); // 오브젝트 형식으로 변환.
                            Board.setId(document.getId());
                            if (place_name.equals(Board.getBoard_name())) { // 게시판이름이 동일한 경우에만.
                                ShowBoardList.add(Board);
                            }
                        }
                        placeAdapter = new PlaceAdapter(ShowBoardList, shcontext);
                        placeAdapter.notifyDataSetChanged();
                        mainAdapter = new MainAdapter(ShowBoardList, shcontext);
                        mainAdapter.notifyDataSetChanged();
                    }
                });
        if(to_so)
            Toast.makeText(ShowPlaceActivity.this, "성공적으로 신청되었습니다.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(ShowPlaceActivity.this, "성공적으로 취소되었습니다.", Toast.LENGTH_SHORT).show();
        // 종료시 값 전달
        Intent resultintent = new Intent();
        setResult(RESULT_OK, resultintent);
        finish();
    }

    // 수정하기 버튼 이벤트
    public void update_btn(View view){
        db.collection("post") //컬렉션 게시물 선택
                .get() // 데이터 가져옴
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Board Board = document.toObject(Board.class); // 오브젝트 형식으로 변환.
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            if(user.getUid().equals(Board.getUid())){ // 로그인 되어있는 회원의 uid와 게시판에 저장된 uid와 일치할 경우 실행
                                if(id.equals(document.getId())) // 선택한 id와 document id가 일치하는 경우 이동
                                    updateUI();
                            }else if(!user.getUid().equals(Board.getUid())){
                                Toast.makeText(shcontext, " 수정할 수 있는 권한이 없습니다. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    //수정하기 이동 이벤트
    public void updateUI(){
        Intent reintent = new Intent(ShowPlaceActivity.this, RecruitActivity.class); //장소게시판 액티비티에 전달
        reintent.putExtra("place_name", place_name); //장소이름
        reintent.putExtra("title_info", showtitle); //제목
        reintent.putExtra("people_number", showpeople); //총인원
        reintent.putExtra("delevery_info", showinfo); //상세정보
        reintent.putExtra("time",  dateview);//등록시간
        reintent.putExtra("id",id);
        if(solo != null){
            reintent.putExtra("solo",solo);
        }else if(together != null){
            reintent.putExtra("together",together);
        }
        startActivity(reintent);//화면전환
        finish();//등록게시판 종료
    }
    // 삭제하기 버튼 이벤트
    public void delete_btn(View view){
        db.collection("post") //컬렉션 게시물 선택
                .get() // 데이터 가져옴
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Board Board = document.toObject(Board.class); // 오브젝트 형식으로 변환.
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            assert user != null;
                            if(user.getUid().equals(Board.getUid())){ // 로그인 되어있는 회원의 uid와 게시판에 저장된 uid와 일치할 경우 삭제
                                db.collection("post")
                                        .document(id)//doc id와 일치할 경우 삭제
                                        .delete()
                                        .addOnSuccessListener(aVoid -> Toast.makeText(shcontext, " 정상적으로 게시글이 삭제되었습니다. ", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(shcontext, " 게시글을 찾지 못했습니다. ", Toast.LENGTH_SHORT).show());
                                finish();
                            }else if(!user.getUid().equals(Board.getUid())){
                                Toast.makeText(shcontext, " 삭제할 수 있는 권한이 없습니다. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}