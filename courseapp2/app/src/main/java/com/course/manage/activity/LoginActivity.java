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
import com.course.manage.databinding.ActivityLoginBinding;
import com.course.manage.model.User;
import com.course.manage.utils.HttpUtils;
import com.course.manage.utils.SPUtils;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etAccount = binding.etAccount;
                EditText etPassword = binding.etPassword;

                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(account)){
                    Toast.makeText(LoginActivity.this,"please input account",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"please input password",Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestParams requestParams = new RequestParams(HttpUtils.login);
                requestParams.addParameter("account",account);
                requestParams.addParameter("password",password);
                HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
                    @Override
                    public void onSuccess(String result) throws JSONException {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject userinfo = data.getJSONObject("userinfo");
                        User user = new User();
                        user.setUserId(userinfo.optInt("id"));
                        user.setUsername(userinfo.optString("username"));
                        user.setPassword(password);
                        user.setAvatar(userinfo.optString("avatar"));
                        user.setNickname(userinfo.optString("nickname"));
                        user.setMobile(userinfo.optString("mobile"));
                        user.setToken(userinfo.optString("token"));
                        user.setCreatetime(userinfo.optLong("createtime"));
                        user.setType(userinfo.optString("type"));
                        App.user = user;
                        App.token = user.getToken();
                        SPUtils.setString("account",user.getUsername());
                        SPUtils.setString("password",user.getPassword());
                        if (binding.cbAuto.isChecked()){
                            SPUtils.setInt("isAuto",1);
                        }
                        if (TextUtils.equals(user.getType(),"admin")){
                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                            finish();
                        }else if (TextUtils.equals(user.getType(),"teacher")){
                            startActivity(new Intent(LoginActivity.this, TeacherActivity.class));
                            finish();
                        }else if (TextUtils.equals(user.getType(),"student")){
                            startActivity(new Intent(LoginActivity.this, StudentActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }
                });
            }
        });
        String account = SPUtils.getString("account");
        String password = SPUtils.getString("password");
        int isAuto = SPUtils.getInt("isAuto");
        binding.etAccount.setText(account);
        binding.etPassword.setText(password);
        if (isAuto>0){
            binding.cbAuto.setChecked(true);
        }
        if (!TextUtils.isEmpty(account)&&!TextUtils.isEmpty(password)&&isAuto>0){
            binding.btnLogin.performClick();
        }
    }
}