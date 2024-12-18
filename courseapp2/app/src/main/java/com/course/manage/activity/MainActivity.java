package com.course.manage.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.course.manage.R;
import com.gyf.immersionbar.ImmersionBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();
        setContentView(R.layout.activity_main);
    }
}