package com.course.manage.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.course.manage.App;
import com.course.manage.R;
import com.course.manage.activity.BaseActivity;
import com.course.manage.activity.StudentSigninDetailActivity;
import com.course.manage.databinding.ActivityScourseListBinding;
import com.course.manage.databinding.ActivityStudentListBinding;
import com.course.manage.model.User;
import com.course.manage.utils.Base64ImageUtils;
import com.course.manage.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

public class StudentListActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ActivityStudentListBinding binding;
    private RecyclerView recyclerView;
    private BGARefreshLayout mRefreshLayout;
    private BGARecyclerViewAdapter bgaRefreshLayoutAdapter;
    int courseId=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        courseId = getIntent().getIntExtra("course_id",-1);
        if (courseId==-1){
            finish();
            return;
        }
        recyclerView = binding.recycler;
        mRefreshLayout = binding.rlModulenameRefresh;
        initRefreshLayout();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bgaRefreshLayoutAdapter = new BGARecyclerViewAdapter<User>(recyclerView, R.layout.item_student) {
            @Override
            protected void fillData(BGAViewHolderHelper helper, int position, User model) {
                helper.getTextView(R.id.tv_username).setText(model.getUsername());
                helper.getTextView(R.id.tv_email).setText(model.getEmail());
                helper.getTextView(R.id.tv_mobile).setText(model.getMobile());
                helper.getTextView(R.id.tv_attendance).setText("attendance:"+model.getAttendance());
                helper.getTextView(R.id.tv_attendance).setVisibility(View.VISIBLE);
                helper.getTextView(R.id.tv_status).setText("status:"+model.getCsStatus());
                helper.getTextView(R.id.tv_status).setVisibility(View.VISIBLE);
                if (model.getAvatar().contains("data:image/svg+xml")) {
                    Glide.with(StudentListActivity.this).load(R.mipmap.ic_launcher).into(helper.getImageView(R.id.iv_avatar));
                } else {
                    Glide.with(StudentListActivity.this).load(Base64ImageUtils.base64ToBitmap(model.getAvatar())).into(helper.getImageView(R.id.iv_avatar));
                }
                helper.getTextView(R.id.tv_signin).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(StudentListActivity.this, StudentSigninDetailActivity.class).putExtra("course_id",courseId).putExtra("student_id",model.getUserId()));
                    }
                });
            }
        };
        recyclerView.setAdapter(bgaRefreshLayoutAdapter);
        mRefreshLayout.beginRefreshing();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRefreshLayout() {
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGAStickinessRefreshViewHolder refreshViewHolder = new BGAStickinessRefreshViewHolder(this, true);
        refreshViewHolder.setRotateImage(R.mipmap.ic_launcher);
        refreshViewHolder.setStickinessColor(R.color.purple_200);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("loading");
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        RequestParams requestParams = new RequestParams(HttpUtils.teacherStudentlist + "?token=" + App.token);
        requestParams.addParameter("course_id",courseId);
        HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray data = jsonObject.optJSONArray("data");
                if (data != null && data.length() > 0) {
                    ArrayList<User> studentArray = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.optJSONObject(i);
                        User user = new User();
                        user.setUserId(item.optInt("id"));
                        user.setUsername(item.optString("username"));
                        user.setNickname(item.optString("nickname"));
                        user.setEmail(item.optString("email"));
                        user.setMobile(item.optString("mobile"));
                        user.setAvatar(item.optString("avatar"));
                        user.setStatus(item.optString("status"));
                        user.setCsStatus(item.optString("sc_status"));
                        user.setAttendance(item.optString("attendance"));
                        studentArray.add(user);
                    }
                    bgaRefreshLayoutAdapter.setData(studentArray);
                }
                mRefreshLayout.endRefreshing();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mRefreshLayout.endRefreshing();
            }
        });
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}