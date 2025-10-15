package com.kyl.zflix;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 앱 시작 시 Firebase 초기화
        FirebaseApp.initializeApp(this);
    }
}
