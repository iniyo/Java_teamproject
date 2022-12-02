package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PlaceActivity extends AppCompatActivity {

    private Button[] place_btnID = new Button[16]; //버튼 배열
    private int getID; //ID를 받을 integer
    private String place_name[] = new String[16]; //버튼의 text를 받을 문자열 선언
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        for(int i=0; i<place_btnID.length;i++){
            getID = getResources().getIdentifier("place_btn"+(i+1),"id", getPackageName());
            place_btnID[i] = findViewById(getID);
            // 두번째 장소게시판에 대한 화면전환
            Intent intent = new Intent(PlaceActivity.this, PlaceActivity2.class); // activity에 대한 화면전환 객체
            place_name[i] = place_btnID[i].getText().toString();
            intent.putExtra("place_name",place_name[i]); //넘겨줄 이름을 정의하는 name과 넘겨줄 이름이 있는 String
            place_btnID[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    startActivity(intent); //액티비티 이동.
                }
            });
        }

        // 뒤로가기버튼 툴바 생성
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }
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
}