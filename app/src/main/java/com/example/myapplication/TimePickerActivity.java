package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class TimePickerActivity extends AppCompatActivity {

    private TimePicker time_picker; //타임피커 ID
    private Button select_btn; //선택버튼 ID
    private String time; //시간 스트링값 ID

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        //타임피커 시간 저장
        time_picker = findViewById(R.id.time_picker);
        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                if (hour > 12) {
                    hour -= 12;
                    time = "오후"+hour+"시"+minute+"분";
                } else {
                    time = "오전"+hour+"시"+minute+"분";
                }
            }
        });
        //배달시간 선택하기 버튼
        select_btn = findViewById(R.id.select_btn);
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //메인 액티비티로 다시 돌아갈떄 입력필드의 입력 값을 되돌려 줌.
                Intent intent = new Intent(TimePickerActivity.this, RecruitActivity.class);
                intent.putExtra("time",time);
                setResult(RESULT_OK, intent);
                //종료
                finish();
            }
        });
    }
}