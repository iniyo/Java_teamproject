package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public  RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
    private String nick = "";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어store 인스턴스 선언
    private FirebaseAuth firebaseAuth;
    private EditText EditText_chat;
    private Button Button_send;
    private DatabaseReference myRef;
    private Context chContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 기본셋팅
        Button_send = findViewById(R.id.Button_send); // 보내기 버튼
        EditText_chat = findViewById(R.id.EditText_chat); // chat Edit
        chContext = this; // 현재 컨텍스트

        //파이어베이스 문서에서 데이터 가져오기 위해 셋팅
        firebaseAuth = FirebaseAuth.getInstance();
        getUserInfo();
        // 보내기 버튼 이벤트
        Button_send.setOnClickListener(v -> {
          String msg = EditText_chat.getText().toString(); //msg
            // 메시지가 들어오면
            if(msg != null) {
                ChatData chat = new ChatData();
                chat.setNickname(nick); // 유저 닉네임 저장
                chat.setMsg(msg); // 유저 메시지 저장
                myRef.push().setValue(chat); // 파이어 베이스의 realtime database에 저장.
            }
        });

        // 리사이클러뷰 셋팅
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true); // 리사이클러뷰 성능 향상
        mLayoutManager = new LinearLayoutManager(this); //linearlayout을 사용해서 리사이클러뷰에 셋팅하기 때문에 linearlayoutmanager 사용
        mRecyclerView.setLayoutManager(mLayoutManager); //리사이클러뷰에 매니저 셋팅

        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, ChatActivity.this, nick);

        mRecyclerView.setAdapter(mAdapter);

        // database에 메시지 넣기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // 파이어베이스 데이터 이벤트 리스너
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatData chat = dataSnapshot.getValue(ChatData.class); //데이터 베이스에서 메시지 가져오기
                ((ChatAdapter) mAdapter).addChat(chat); // 어댑터에 셋팅.
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //1. recyclerView - 반복
        //2. 디비 내용을 넣는다
        //3. 상대방폰에 채팅 내용이 보임 - get

        //1-1. recyclerview - chat data
        //1. message, nickname - Data Transfer Object

    }
    ForecdTerminationService exit = new ForecdTerminationService();
    // 뒤로가기 이벤트 (앱 내부 아님)
    public boolean onKeyDown(int Keycode, KeyEvent event){
        if(Keycode == KeyEvent.KEYCODE_BACK){
            myRef.removeValue();
            finish();
            return true;
        }
        return false;
    }
    // 파이어베이스 문서에서 user UID와 일치하는 문서를 찾으면 해당 문서의 nick을 가져옴.
    private void getUserInfo(){
        // 유저 정보 읽어옴
        db.collection("UserInfo") //컬렉션 게시물 선택
                .get() // 데이터 가져옴
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserAccount userinfo = document.toObject(UserAccount.class); // 오브젝트 형식으로 변환.
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // 유저 UID랑 일치하면
                            if(userinfo.equals(user.getUid())){
                                nick = userinfo.getNickname();
                                Toast.makeText(chContext, userinfo.getNickname(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Toast.makeText(chContext, "유저 UID가 존재하지 않습니다. 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
