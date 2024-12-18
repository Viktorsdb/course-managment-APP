package com.course.manage.activity.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.course.manage.App;
import com.course.manage.R;
import com.course.manage.activity.BaseActivity;
import com.course.manage.databinding.ActivityCourseTimeBinding;
import com.course.manage.databinding.ActivityScourseTimeBinding;
import com.course.manage.model.Course;
import com.course.manage.model.CourseTime;
import com.course.manage.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

public class SCourseTimeActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ActivityScourseTimeBinding binding;
    private RecyclerView recyclerView;
    private BGARefreshLayout mRefreshLayout;
    private BGARecyclerViewAdapter bgaRefreshLayoutAdapter;

    private List<CourseTime> courseTimeList = new ArrayList<>();

    private List<String> weekStr = Arrays.asList("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScourseTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView = binding.recycler;
        mRefreshLayout = binding.rlModulenameRefresh;
        initRefreshLayout();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bgaRefreshLayoutAdapter = new BGARecyclerViewAdapter<CourseTime>(recyclerView, R.layout.item_course_time) {
            @Override
            protected void fillData(BGAViewHolderHelper helper, int position, CourseTime model) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss");
                StringBuilder detail = new StringBuilder();
                detail.append(weekStr.get(position)).append("\r\n");
                List<Course> courseList = model.getCourseList();
                if (courseList.size()==0){
                    detail.append("No Course");
                }else{
                    for (int i = 0; i < courseList.size(); i++) {
                        Course course = courseList.get(i);
                        String times = "";
                        if (position==0){
                            times = course.getDay1();
                        }else if (position==1){
                            times = course.getDay2();
                        }else if (position==2){
                            times = course.getDay3();
                        }else if (position==3){
                            times = course.getDay4();
                        }else if (position==4){
                            times = course.getDay5();
                        }else if (position==5){
                            times = course.getDay6();
                        }else if (position==6){
                            times = course.getDay7();
                        }
                        String[] timeAr = times.split(",");
                        detail.append(course.getNanme()).append("\r\n");
                        detail.append(sdf.format(new Date(Long.parseLong(timeAr[0])))+" To "+sdf.format(new Date(Long.parseLong(timeAr[1]))));
                        detail.append("(").append(course.getLocation()).append("-").append(course.getTeacherName()).append(")");
                        detail.append("\r\n");
                    }
                }
                helper.getTextView(R.id.tv_detail).setText(detail);
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
        RequestParams requestParams = new RequestParams(HttpUtils.studentCourseMylist+"?token="+ App.token);
        HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray data = jsonObject.optJSONArray("data");
                if (data!=null&&data.length()>0){
                    CourseTime day1Ct = new CourseTime(new ArrayList<>(), "day1");
                    CourseTime day2Ct = new CourseTime(new ArrayList<>(), "day2");
                    CourseTime day3Ct = new CourseTime(new ArrayList<>(), "day3");
                    CourseTime day4Ct = new CourseTime(new ArrayList<>(), "day4");
                    CourseTime day5Ct = new CourseTime(new ArrayList<>(), "day5");
                    CourseTime day6Ct = new CourseTime(new ArrayList<>(), "day6");
                    CourseTime day7Ct = new CourseTime(new ArrayList<>(), "day7");
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

                        if (!TextUtils.equals(course.getStatus(),"start course")){
                            continue;
                        }

                        if (!TextUtils.isEmpty(item.optString("day1"))&&(!item.optString("day1").equals("null"))){
                            day1Ct.getCourseList().add(course);
                        }
                        if (!TextUtils.isEmpty(item.optString("day2"))&&(!item.optString("day2").equals("null"))){
                            day2Ct.getCourseList().add(course);
                        }
                        if (!TextUtils.isEmpty(item.optString("day3"))&&(!item.optString("day3").equals("null"))){
                            day3Ct.getCourseList().add(course);
                        }
                        if (!TextUtils.isEmpty(item.optString("day4"))&&(!item.optString("day4").equals("null"))){
                            day4Ct.getCourseList().add(course);
                        }
                        if (!TextUtils.isEmpty(item.optString("day5"))&&(!item.optString("day5").equals("null"))){
                            day5Ct.getCourseList().add(course);
                        }
                        if (!TextUtils.isEmpty(item.optString("day6"))&&(!item.optString("day6").equals("null"))){
                            day6Ct.getCourseList().add(course);
                        }
                        if (!TextUtils.isEmpty(item.optString("day7"))&&(!item.optString("day7").equals("null"))){
                            day7Ct.getCourseList().add(course);
                        }
                    }
                    courseTimeList.add(day1Ct);
                    courseTimeList.add(day2Ct);
                    courseTimeList.add(day3Ct);
                    courseTimeList.add(day4Ct);
                    courseTimeList.add(day5Ct);
                    courseTimeList.add(day6Ct);
                    courseTimeList.add(day7Ct);
                    bgaRefreshLayoutAdapter.setData(courseTimeList);
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