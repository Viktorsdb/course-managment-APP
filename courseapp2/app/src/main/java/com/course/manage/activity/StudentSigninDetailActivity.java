package com.course.manage.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.course.manage.App;
import com.course.manage.R;
import com.course.manage.activity.teacher.CourseListActivity;
import com.course.manage.activity.teacher.SigninLogActivity;
import com.course.manage.activity.teacher.StudentListActivity;
import com.course.manage.databinding.ActivityStudentSigninDetailBinding;
import com.course.manage.model.Course;
import com.course.manage.utils.HttpUtils;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

public class StudentSigninDetailActivity extends AppCompatActivity {
    private ActivityStudentSigninDetailBinding binding;
    private  int courseId;
    private int studentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.gray)
                .init();
        binding = ActivityStudentSigninDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        courseId = intent.getIntExtra("course_id", 0);
        studentId = intent.getIntExtra("student_id", 0);
        getSigninDetail();
    }

    public void getSigninDetail(){
        RequestParams requestParams = new RequestParams(HttpUtils.studentSigninDetail+"?token="+ App.token);
        requestParams.addParameter("student_id",studentId);
        requestParams.addParameter("course_id",courseId);
        HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject data = jsonObject.optJSONObject("data");
                String student = data.optString("student");
                String course = data.optString("course");
                JSONArray signin = data.optJSONArray("signin");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(student).append("\r\n")
                                .append(course).append("\r\n");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < signin.length(); i++) {
                    JSONObject jb = signin.optJSONObject(i);
                    stringBuilder.append(sdf.format(new Date(jb.optLong("createtime")*1000))).append("\r\n");
                }
                binding.tvDetail.setText(stringBuilder);
                binding.tvCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", binding.tvDetail.getText());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(StudentSigninDetailActivity.this,"copy success",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }
}