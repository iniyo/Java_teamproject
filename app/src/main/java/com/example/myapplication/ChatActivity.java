package com.example.myapplication;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public  RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
    private String nick = "nick2";
    
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private EditText EditText_chat;
    private Button Button_send;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Button_send = findViewById(R.id.Button_send); // 보내기 버튼
        EditText_chat = findViewById(R.id.EditText_chat); // chat Edit

        // 보내기 버튼 이벤트
        Button_send.setOnClickListener(v -> {
          String msg = EditText_chat.getText().toString(); //msg
            
            if(msg != null) {
                ChatData chat = new ChatData();
                chat.setNickname(nick); // 유저 닉네임 저장
                chat.setMsg(msg); // 유저 메시지 저장
                myRef.push().setValue(chat); // 파이어 베이스의 realtime database에 저장.
            }

        });

        // 리사이클러뷰 셋팅
        mRecyclerView = findViewById(R.id.my_recycler_view); 
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

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
}
