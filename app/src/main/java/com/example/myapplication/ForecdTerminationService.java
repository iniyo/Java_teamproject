package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;

//앱 종료시 클래스
public class ForecdTerminationService extends Service {

    private DatabaseReference myRef;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) { //핸들링 하는 부분
        myRef.removeValue();
        Toast.makeText(this, "onTaskRemoved ", Toast.LENGTH_SHORT).show();
        stopSelf(); //서비스 종료
    }
}