package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    private ArrayList<Board> mBoardList = new ArrayList<Board>(); //Board 배열 리스트
    private MainAdapter mainAdapter; // 메인 어댑터
    private RecyclerView mRecycler; // 메인 리사이클러 뷰
    private LinearLayoutManager linearLayoutManager; // 리니어 레이아웃 매니저
    private Context context; //현재 컨텍스트
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어store 인스턴스 선언
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        //리사이클러뷰 셋팅
        mRecycler = findViewById(R.id.free_Recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(linearLayoutManager);
        //현재 컨텍스트
        context = this;
        
        dbCollection(); // db셋팅

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
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
}