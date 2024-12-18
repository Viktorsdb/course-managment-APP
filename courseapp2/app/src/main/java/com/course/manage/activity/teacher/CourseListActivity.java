package com.course.manage.activity.teacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.course.manage.App;
import com.course.manage.R;
import com.course.manage.activity.student.SCourseListActivity;
import com.course.manage.databinding.ActivityCourseListBinding;
import com.course.manage.databinding.ActivityTeacherCourseListBinding;
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

public class CourseListActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ActivityTeacherCourseListBinding binding;
    private RecyclerView recyclerView;
    private BGARefreshLayout mRefreshLayout;
    private BGARecyclerViewAdapter bgaRefreshLayoutAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.gray)
                .init();
        binding = ActivityTeacherCourseListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView = binding.recycler;
        mRefreshLayout = binding.rlModulenameRefresh;
        initRefreshLayout();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bgaRefreshLayoutAdapter = new BGARecyclerViewAdapter<Course>(recyclerView, R.layout.item_teacher_course) {
            @Override
            protected void fillData(BGAViewHolderHelper helper, int position, Course model) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                helper.getTextView(R.id.tv_name).setText(model.getNanme());
                helper.getTextView(R.id.tv_location).setText("location:"+model.getLocation());
                helper.getTextView(R.id.tv_semester).setText("semester:"+model.getSemester());
                helper.getTextView(R.id.tv_status).setText("status:"+model.getStatus());
                if (TextUtils.equals("select start",model.getStatus())){
                    helper.getTextView(R.id.tv_select).setVisibility(View.VISIBLE);
                    helper.getTextView(R.id.tv_select).setText("select time:\r\n"+model.getStartselecttime()+"-"+model.getEndselecttime());
                }else{
                    helper.getTextView(R.id.tv_select).setVisibility(View.GONE);
                }
                helper.getTextView(R.id.tv_start).setText("begin time:\r\n"+model.getStartdate()+"-"+model.getEnddate());
                helper.getTextView(R.id.tv_student_list).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CourseListActivity.this,StudentListActivity.class).putExtra("course_id",model.getId()));
                    }
                });
                helper.getTextView(R.id.tv_sign_list).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CourseListActivity.this, SigninLogActivity.class).putExtra("course_id",model.getId()));
                    }
                });

                if (model.isShowTime()){
                    helper.getView(R.id.ll_cours_time_root).setVisibility(View.VISIBLE);
                    Glide.with(CourseListActivity.this).load(R.drawable.baseline_keyboard_arrow_up_24).into(helper.getImageView(R.id.iv_arrow_open));
                }else{
                    helper.getView(R.id.ll_cours_time_root).setVisibility(View.GONE);
                    Glide.with(CourseListActivity.this).load(R.drawable.baseline_keyboard_arrow_down_24).into(helper.getImageView(R.id.iv_arrow_open));
                }
                helper.getView(R.id.iv_arrow_open).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.setShowTime(!model.isShowTime());
                        if (model.isShowTime()){
                            helper.getView(R.id.ll_cours_time_root).setVisibility(View.VISIBLE);
                            Glide.with(CourseListActivity.this).load(R.drawable.baseline_keyboard_arrow_up_24).into(helper.getImageView(R.id.iv_arrow_open));
                        }else{
                            helper.getView(R.id.ll_cours_time_root).setVisibility(View.GONE);
                            Glide.with(CourseListActivity.this).load(R.drawable.baseline_keyboard_arrow_down_24).into(helper.getImageView(R.id.iv_arrow_open));
                        }
                    }
                });
                setTimeToTv(helper.getTextView(R.id.tv_day1),model.getDay1(),"Monday");
                setTimeToTv(helper.getTextView(R.id.tv_day2),model.getDay2(),"Tuesday");
                setTimeToTv(helper.getTextView(R.id.tv_day3),model.getDay3(),"Wednesday");
                setTimeToTv(helper.getTextView(R.id.tv_day4),model.getDay4(),"Thursday");
                setTimeToTv(helper.getTextView(R.id.tv_day5),model.getDay5(),"Friday");
                setTimeToTv(helper.getTextView(R.id.tv_day6),model.getDay6(),"Saturday");
                setTimeToTv(helper.getTextView(R.id.tv_day7),model.getDay7(),"Sunday");
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

    public void setTimeToTv(TextView textView, String time, String prefix){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss");
        if (TextUtils.isEmpty(time)||!time.contains(",")){
            textView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
            String[] times = time.split(",");
            textView.setText(prefix+":"+sdf.format(new Date(Long.parseLong(times[0])))+" To "+sdf.format(new Date(Long.parseLong(times[1]))));
        }
    }

    private void initRefreshLayout() {
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGAStickinessRefreshViewHolder refreshViewHolder = new BGAStickinessRefreshViewHolder(this,true);
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
        RequestParams requestParams = new RequestParams(HttpUtils.teacherCourselist+"?token="+ App.token);
        HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray data = jsonObject.optJSONArray("data");
                if (data!=null&&data.length()>0){
                    ArrayList<Course> courseArray = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.optJSONObject(i);
                        Course course = new Course();
                        course.setId(item.optInt("id"));
                        course.setNanme(item.optString("name"));
                        course.setCreatetime(item.optLong("createtime"));
                        course.setStudent(item.optInt("student"));
                        course.setTeacherName(item.optString("teacher_name"));
                        course.setLocation(item.optString("location"));
                        course.setSemester(item.optString("semester"));
                        course.setMaxStudent(item.optInt("max_student"));
                        course.setStartselecttime(item.optString("startselecttime"));
                        course.setEndselecttime(item.optString("endselecttime"));
                        course.setStartdate(item.optString("startdate"));
                        course.setEnddate(item.optString("enddate"));
                        course.setStatus(item.optString("status"));
                        course.setDay1(item.optString("day1"));
                        course.setDay2(item.optString("day2"));
                        course.setDay3(item.optString("day3"));
                        course.setDay4(item.optString("day4"));
                        course.setDay5(item.optString("day5"));
                        course.setDay6(item.optString("day6"));
                        course.setDay7(item.optString("day7"));
                        courseArray.add(course);
                    }
                    bgaRefreshLayoutAdapter.setData(courseArray);
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