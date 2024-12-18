package com.course.manage.activity.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.course.manage.App;
import com.course.manage.R;
import com.course.manage.activity.MyActivity;
import com.course.manage.activity.teacher.TeacherActivity;
import com.course.manage.databinding.ActivityStudentBinding;
import com.course.manage.utils.HttpUtils;
import com.course.manage.utils.LogUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONException;
import org.xutils.http.RequestParams;

public class StudentActivity extends AppCompatActivity {
    private ActivityStudentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();
        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.llCourseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this,SCourseListActivity.class));
            }
        });
        binding.llMyCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this,SMyCourseListActivity.class));
            }
        });
        binding.llSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, SSigninLogActivity.class));
            }
        });
        binding.llMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, MyActivity.class));
            }
        });
        binding.llCourseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentActivity.this, SCourseTimeActivity.class));
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("notification")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                    }
                });
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        LogUtils.e(token);
                        App.firebaseToken = token;
                        uploadFireBaseToken();
                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void uploadFireBaseToken(){
        RequestParams requestParams = new RequestParams(HttpUtils.uploadFirebaseToken+"?token="+ App.token);
        requestParams.addParameter("firebase_token",App.firebaseToken);
        HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
            @Override
            public void onSuccess(String result) throws JSONException {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }
}