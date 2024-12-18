package com.course.manage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.course.manage.App;
import com.course.manage.activity.admin.AdminActivity;
import com.course.manage.activity.student.StudentActivity;
import com.course.manage.activity.teacher.TeacherActivity;
import com.course.manage.databinding.ActivityRegisterBinding;
import com.course.manage.model.User;
import com.course.manage.utils.HttpUtils;
import com.course.manage.utils.SPUtils;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etUsername = binding.etUsername;
                EditText etEmail = binding.etEmail;
                EditText etMobile = binding.etMobile;
                EditText etPassword = binding.etPassword;
                EditText etPasswordAgain = binding.etPasswordAgain;
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String mobile = etMobile.getText().toString();
                String password = etPassword.getText().toString();
                String passwordAgain = etPasswordAgain.getText().toString();

                if (TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this,"please input username",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this,"please input email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mobile)){
                    Toast.makeText(RegisterActivity.this,"please input mobile",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this,"please input password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordAgain)){
                    Toast.makeText(RegisterActivity.this,"please input password again",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.equals(password,passwordAgain)){
                    Toast.makeText(RegisterActivity.this,"passwords entered twice are inconsistent",Toast.LENGTH_SHORT).show();
                    return;
                }
                String type = binding.rbStudent.isChecked()?"student":"teacher";

                RequestParams requestParams = new RequestParams(HttpUtils.register);
                requestParams.addParameter("username",username);
                requestParams.addParameter("password",password);
                requestParams.addParameter("email",email);
                requestParams.addParameter("mobile",mobile);
                requestParams.addParameter("type",type);
                HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(RegisterActivity.this,"register success",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }
                });
            }
        });
    }
}