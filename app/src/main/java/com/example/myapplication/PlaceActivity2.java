package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.Objects;

public class PlaceActivity2 extends AppCompatActivity {

    private RecyclerView recyler_place; //리사이클러뷰 ID
    // 리스트들 동적 생성
    private final ArrayList<Board> pBoardLists = new ArrayList<>();
    //어댑터
    private PlaceAdapter placeAdapter; //어댑터
    public Context context;
    private String place_name;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어store 인스턴스 선언
    //테스트용 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place2);

        // 장소게시판 리사이클러뷰 접근을 위해 설정
        recyler_place = findViewById(R.id.recyler_place);
        //리사이클러뷰 어댑터를 도와줌
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyler_place.setLayoutManager(linearLayoutManager);
        //게시판 제목 받아올 ID
        TextView place2_view = findViewById(R.id.place2_view);
        // 인텐트 전환시 정보를 받아올 인텐트 객체
        Intent intent = getIntent();
        // String place_name 에 인텐트 전환시 받을 스트링 값 저장
        place_name = intent.getStringExtra("place_name");
        // place_name으로 텍스트 셋업
        place2_view.setText(place_name);
        context = this;

        setting_Recyclerview(); // 리사이클러뷰 셋팅



        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//옵션메뉴
        if (item.getItemId() == android.R.id.home) {//toolbar의 back키 눌렀을 때 동작
            // 액티비티 이동
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);//옵션메뉴 리턴*/
    }
    public void setting_Recyclerview(){
        //파이어베이스 db에서 모든 문서 가져오기
        //리스너를 통해 데이터 등록함.
        db.collection("post") //컬렉션 게시물 선택
                .get() // 데이터 가져옴
                .addOnCompleteListener(task -> {
                    pBoardLists.clear(); // 리스트 공간 초기화
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Board Board = document.toObject(Board.class); // 오브젝트 형식으로 변환.
                            Board.setId(document.getId());
                            if(place_name.equals(Board.getBoard_name())){ // 게시판이름이 동일한 경우에만.
                                pBoardLists.add(Board);
                            }
                        }
                        placeAdapter = new PlaceAdapter(pBoardLists,context);
                        placeAdapter.notifyDataSetChanged();
                        recyler_place.setAdapter(placeAdapter);
                        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyler_place.setLayoutManager(mLinearLayoutManager);
                    } else {
                        // toast 메시지 넣을 공간
                        Toast.makeText(PlaceActivity2.this, "목록을 불러오기 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}