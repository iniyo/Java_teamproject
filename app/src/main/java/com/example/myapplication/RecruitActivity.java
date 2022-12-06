package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class RecruitActivity extends AppCompatActivity {

    private EditText delevey_et; // 배달내용 에디터텍스트
    private EditText title_et; // 제목 에디터텍스트
    private Spinner people_sp; // 총인원 스피너
    private Spinner place_sp; //place 스피너
    private TextView date_view; //날짜 텍스트
    private TextView time_et; //시간 텍스트
    private RadioButton radio_together; //같이 라디오버튼
    private RadioButton radio_solo; //따로 라디오버튼
    private final FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어 베이스 store
    private Context rContext; //context
    private FirebaseAuth firebaseAuth;     //파이어베이스 인증처리
    private final Board board = new Board(); // 게시판
    private String id, place_name, showtitle, showpeople, showinfo, time, solo, together;
    long mNow;
    Date mDate;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    private boolean checked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);
        // 게시판 uid 가져와서 저장
        firebaseAuth = FirebaseAuth.getInstance();
        rContext = getApplicationContext();
        // 사용할 컴포넌트
        place_sp = findViewById(R.id.place_sp);//장소스피너
        title_et = findViewById(R.id.title_et);//제목 에디터 텍스트
        delevey_et = findViewById(R.id.delevey_et);//배달내용 EditText
        radio_together = findViewById(R.id. radio_together);//같이 라디오버튼
        radio_solo = findViewById(R.id.radio_solo);//따로 라디오버튼
        date_view = findViewById(R.id.date_view1);//현재날짜
        date_view.setText(getTime());//날짜 지정
        time_et = findViewById(R.id.time_et);//시간 ID 가져오기
        people_sp = findViewById(R.id.people_sp);//총인원 스피너

        //다른 인텐트 받아올때 사용
        Intent intent = getIntent();
        place_name = intent.getStringExtra("place_name");
        showtitle = intent.getStringExtra("title_info");
        showpeople = intent.getStringExtra("people_number");
        showinfo = intent.getStringExtra("delevery_info");
        time = intent.getStringExtra("time");
        id = intent.getStringExtra("id");
        solo = intent.getStringExtra("solo");
        together = intent.getStringExtra("together");
        
        //ShowPlace에서 받아온 정보 셋팅
        title_et.setText(showtitle);
        time_et.setText(time);
        delevey_et.setText(showinfo);
        if(solo!=null){
            radio_solo.setChecked(true);
        }else if(together!=null){
            radio_together.setChecked(true);
        }
        for(int i=0;i<10;i++){
            if(people_sp.getItemAtPosition(i).toString().equals(solo)){
                people_sp.setSelection(i);
            }else if(people_sp.getItemAtPosition(i).toString().equals(together)){
                people_sp.setSelection(i);
            }
        }

        //시간 데이터 가져오기
        String data = intent.getStringExtra("time");
        time_et.setText(data);

        // 총인원 스피너
        people_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }

    // 삭제하기 버튼 이벤트
    public void deletebtn(){
        db.collection("post") //컬렉션 게시물 선택
                .get() // 데이터 가져옴
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Board Board = document.toObject(Board.class); // 오브젝트 형식으로 변환.
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            if(user.getUid().equals(Board.getUid())){ // 로그인 되어있는 회원의 uid와 게시판에 저장된 uid와 일치할 경우 삭제
                                if(id.equals(document.getId()))
                                    deletepost();
                            }else if(!user.getUid().equals(Board.getUid()) && checked == true){
                                checked = false;
                                Toast.makeText(rContext, " 삭제할 수 있는 권한이 없습니다. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        checked = true;
    }

    // 삭제하기 버튼 클릭시 가능한 경우
    public void deletepost(){
        db.collection("post")
                .document(id)//doc id와 일치할 경우 삭제
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(rContext, " 정상적으로 게시글이 삭제되었습니다. ", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(rContext, " 게시글을 찾지 못했습니다. ", Toast.LENGTH_SHORT).show());
        finish();
    }
    
    // 수정하기 버튼 이벤트
    public void retouchbtn(View view){
        db.collection("post") //컬렉션 게시물 선택
                .get() // 데이터 가져옴
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Board Board = document.toObject(Board.class); // 오브젝트 형식으로 변환.
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            if(user.getUid().equals(Board.getUid())){ // 로그인 되어있는 회원의 uid와 게시판에 저장된 uid와 일치할 경우 수정
                                if(id.equals(document.getId()))
                                    retouch();
                            }
                            else if(!user.getUid().equals(Board.getUid()) && checked == true){
                                Toast.makeText(rContext, " 수정할 수 있는 권한이 없습니다. ", Toast.LENGTH_SHORT).show();
                                checked = false;
                            }
                        }
                    }
                });
        checked = true;
    }
    //수정 이벤트
    public void retouch(){
        if (radio_together.isChecked()){
            board.setTogether(radio_together.getText().toString()); // 같이하면 같이 저장
        }else if(radio_solo.isChecked()){
            board.setSolo(radio_solo.getText().toString()); // 따로하면 따로 저장
        }
        db.collection("post")
                .document(id)//doc id와 일치할 경우 수정
                .update("board_name",place_sp.getSelectedItem().toString(), // 장소
                        "time",time_et.getText().toString(), // 배달 시간
                        "delevery_info",delevey_et.getText().toString(), // 상세 내용
                        "solo",radio_solo.getText().toString(), // 솔로
                        "together",radio_together.getText().toString(), // 같이
                        "people_number",people_sp.getSelectedItem().toString(),  // 총 인원
                        "title_info",title_et.getText().toString() // 제목
                )
                .addOnSuccessListener(aVoid -> Toast.makeText(rContext, " 정상적으로 게시글이 수정되었습니다. ", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(rContext, " 게시글을 찾지 못했습니다. ", Toast.LENGTH_SHORT).show());
        finish();
    }

    // 게시판 내용이 비어있는지 검사
    public boolean is_Empty(){
        if(TextUtils.isEmpty(title_et.getText().toString())){
            Toast.makeText(rContext, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(delevey_et.getText().toString())){
            Toast.makeText(rContext, "상세내용을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(time_et.getText().toString())){
            Toast.makeText(rContext, "시간을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    // 글자수 제한
    public boolean is_length(){
        if(title_et.length()>15){
            Toast.makeText(rContext, " 10글자 이내로 제목을 작성해주세요. ", Toast.LENGTH_SHORT).show();
            return false;
        }else if(delevey_et.length()>50){
            Toast.makeText(rContext, "50자 이내로 작성해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    // 등록하기 버튼 함수
    public void recruitbtn(View view){
        // 파이어베이스 store에 저장
        // 내용이 비어있는 경우 실행되지 않음
        if(is_Empty() && is_length()){
            save_boarddata(board); // 파이어 데이터베이스 저장 함수로 만들어 둠
            Intent placeBoardintent = new Intent(RecruitActivity.this, PlaceActivity2.class); //장소게시판 액티비티에 전달
            // 장소게시판에 넘겨줄 스트링 선언
            String place_name = place_sp.getSelectedItem().toString();
            placeBoardintent.putExtra("place_name", place_name); //화면 전환시 넘겨줄 장소이름
            startActivity(placeBoardintent);//화면전환
            finish();//등록게시판 종료
        }
    }

    // 시간선택 버튼 기능 = 타임피커
    public void rider_time(View view) {
        Intent intent = new Intent(RecruitActivity.this, TimePickerActivity.class); //타임피커 activity에 대한 화면전환 객체
        getRiderTimeActicity.launch(intent); //타임피커로 이동하면서 값 받아오는 함수
    }

    // 함수로 데이터 정보 받아옴
    private void save_boarddata(Board board){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        board.setBoard_name(place_sp.getSelectedItem().toString()); // 장소 스피너 텍스트 저장
        board.setPeople_number(people_sp.getSelectedItem().toString()); //인원수 스피너 텍스트 저장
        board.setDate(date_view.getText().toString()); //날짜 저장
        board.setTime(time_et.getText().toString()); //시간 저장
        board.setDelevery_info(delevey_et.getText().toString()); //배달내용 저장
        board.setTitle_info(title_et.getText().toString()); //제목 저장
        board.setCount("1"); // 카운트 될 값
        assert user != null;
        board.setUid(user.getUid()); // 유저 UID 저장
        // 같이 먹을건지 따로먹을건지 저장
        if (radio_together.isChecked()){
            board.setTogether(radio_together.getText().toString()); // 같이하면 같이 저장
        }else if(radio_solo.isChecked()){
            board.setSolo(radio_solo.getText().toString()); // 따로하면 따로 저장
        }
        db.collection("post") //post 데이터 베이스
                .add(board)
                .addOnSuccessListener(aVoid -> Toast.makeText(rContext, " 정상적으로 게시글이 작성되었습니다. ", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(rContext, " 게시글 내용을 확인해주세요. ",Toast.LENGTH_SHORT).show());
    }

    //현재날짜 출력 함수
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    //툴바 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//toolbar의 back키 눌렀을 때 동작
            // 액티비티 이동
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //화면전환 RegisterForActivityResult
    private final ActivityResultLauncher<Intent> getRiderTimeActicity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // 타임피커로부터 돌아올때 타임피커의 결괏값을 받아올 수 있는 통로.
                //성공적으로 값을 받아왔는지 확인
                 if(result.getResultCode()==RESULT_OK){
                     //타임피커의 액티비티 값을 텍스트뷰에 가져옴
                     assert result.getData() != null;
                     time_et.setText(result.getData().getStringExtra("time"));
                 }
            }
    );
}
