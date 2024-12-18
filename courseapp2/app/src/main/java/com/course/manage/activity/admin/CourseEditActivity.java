package com.course.manage.activity.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.course.manage.App;
import com.course.manage.R;
import com.course.manage.activity.BaseActivity;
import com.course.manage.databinding.ActivityCourseEditBinding;
import com.course.manage.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CourseEditActivity extends BaseActivity {
    private int teacherId = -1;
    private ActivityCourseEditBinding binding;
    private int courseId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        courseId = getIntent().getIntExtra("course_id", -1);
        initSelectDate(binding.tvStartDate);
        initSelectDate(binding.tvEndDate);
        initSelectDateTime(binding.tvSelectStart);
        initSelectDateTime(binding.tvSelectEnd);
        initSelectTime(binding.tvDay1Start);
        initSelectTime(binding.tvDay1End);
        initSelectTime(binding.tvDay2Start);
        initSelectTime(binding.tvDay2End);
        initSelectTime(binding.tvDay3Start);
        initSelectTime(binding.tvDay3End);
        initSelectTime(binding.tvDay4Start);
        initSelectTime(binding.tvDay4End);
        initSelectTime(binding.tvDay5Start);
        initSelectTime(binding.tvDay5End);
        initSelectTime(binding.tvDay6Start);
        initSelectTime(binding.tvDay6End);
        initSelectTime(binding.tvDay7Start);
        initSelectTime(binding.tvDay7End);

        binding.tvTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CourseEditActivity.this,TeacherListActivity.class).putExtra("type","select"),10000);
            }
        });
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                String courseName = binding.etCourseName.getText().toString();
                String maxStudent = binding.etMaxStudent.getText().toString();
                String location = binding.etLocation.getText().toString();
                String semester = binding.etSemester.getText().toString();
                String startDate = binding.tvStartDate.getText().toString();
                String endDate = binding.tvEndDate.getText().toString();
                String selectStart = binding.tvSelectStart.getText().toString();
                String selectEnd = binding.tvSelectEnd.getText().toString();
                String day1Start = binding.tvDay1Start.getText().toString();
                String day1End = binding.tvDay1End.getText().toString();
                String day2Start = binding.tvDay2Start.getText().toString();
                String day2End = binding.tvDay2End.getText().toString();
                String day3Start = binding.tvDay3Start.getText().toString();
                String day3End = binding.tvDay3End.getText().toString();
                String day4Start = binding.tvDay4Start.getText().toString();
                String day4End = binding.tvDay4End.getText().toString();
                String day5Start = binding.tvDay5Start.getText().toString();
                String day5End = binding.tvDay5End.getText().toString();
                String day6Start = binding.tvDay6Start.getText().toString();
                String day6End = binding.tvDay6End.getText().toString();
                String day7Start = binding.tvDay7Start.getText().toString();
                String day7End = binding.tvDay7End.getText().toString();
                if (TextUtils.isEmpty(courseName)){
                    Toast.makeText(CourseEditActivity.this,"course name is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (teacherId==-1){
                    Toast.makeText(CourseEditActivity.this,"teacher name is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(maxStudent)){
                    Toast.makeText(CourseEditActivity.this,"max student is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(location)){
                    Toast.makeText(CourseEditActivity.this,"location is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(semester)){
                    Toast.makeText(CourseEditActivity.this,"semester is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)){
                    Toast.makeText(CourseEditActivity.this,"start date or end date is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(selectStart) || TextUtils.isEmpty(selectEnd)){
                    Toast.makeText(CourseEditActivity.this,"select start or select end is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    RequestParams requestParams = new RequestParams(HttpUtils.courseAdd+"?token="+ App.token);
                    if (courseId>-1){
                        requestParams.addParameter("course_id",courseId);
                    }else{
                        requestParams.addParameter("status","select start");
                    }
                    requestParams.addParameter("name",courseName);
                    requestParams.addParameter("startselecttime",selectStart);
                    requestParams.addParameter("endselecttime",selectEnd);
                    requestParams.addParameter("startdate",startDate);
                    requestParams.addParameter("enddate",endDate);
                    requestParams.addParameter("max_student",Integer.parseInt(maxStudent));
                    requestParams.addParameter("location",location);
                    requestParams.addParameter("semester",semester);
                    requestParams.addParameter("teacher_id",teacherId);
                    if (!TextUtils.isEmpty(day1Start) && !TextUtils.isEmpty(day1End)){
                        requestParams.addParameter("day1",sdftime.parse(day1Start).getTime()+","+sdftime.parse(day1End).getTime());
                    }
                    if (!TextUtils.isEmpty(day2Start) && !TextUtils.isEmpty(day2End)){
                        requestParams.addParameter("day2",sdftime.parse(day2Start).getTime()+","+sdftime.parse(day2End).getTime());
                    }
                    if (!TextUtils.isEmpty(day3Start) && !TextUtils.isEmpty(day3End)){
                        requestParams.addParameter("day3",sdftime.parse(day3Start).getTime()+","+sdftime.parse(day3End).getTime());
                    }
                    if (!TextUtils.isEmpty(day4Start) && !TextUtils.isEmpty(day4End)){
                        requestParams.addParameter("day4",sdftime.parse(day4Start).getTime()+","+sdftime.parse(day4End).getTime());
                    }
                    if (!TextUtils.isEmpty(day5Start) && !TextUtils.isEmpty(day5End)){
                        requestParams.addParameter("day5",sdftime.parse(day5Start).getTime()+","+sdftime.parse(day5End).getTime());
                    }
                    if (!TextUtils.isEmpty(day6Start) && !TextUtils.isEmpty(day6End)){
                        requestParams.addParameter("day6",sdftime.parse(day6Start).getTime()+","+sdftime.parse(day6End).getTime());
                    }
                    if (!TextUtils.isEmpty(day7Start) && !TextUtils.isEmpty(day7End)){
                        requestParams.addParameter("day7",sdftime.parse(day7Start).getTime()+","+sdftime.parse(day7End).getTime());
                    }
                    HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
                        @Override
                        public void onSuccess(String result) throws JSONException {
                            finish();
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {

                        }
                    });
                }catch (Exception e){
                }
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (courseId>-1){
            getCourse();
        }
    }
    public void initSelectDate(TextView textView){
        textView.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = null;
            datePickerDialog = new DatePickerDialog(CourseEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month+1;
                    textView.setText( ""+(year>=10?year:("0"+year))+"-"+(month>=10?month:("0"+month))+"-"+(dayOfMonth>=10?dayOfMonth:("0"+dayOfMonth)));
                }
            }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    public void getCourse(){
        RequestParams requestParams = new RequestParams(HttpUtils.courseDetail+"?token="+App.token);
        requestParams.addParameter("course_id",courseId);
        HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject js = new JSONObject(result);
                JSONObject data = js.optJSONObject("data");
                binding.etCourseName.setText(data.optString("name"));
                binding.etMaxStudent.setText(data.optString("max_student"));
                binding.etLocation.setText(data.optString("location"));
                binding.etSemester.setText(data.optString("semester"));
                binding.tvTeacher.setText(data.optString("teacher"));
                teacherId = data.optInt("teacher_id");
                binding.tvSelectStart.setText(data.optString("startselecttime"));
                binding.tvSelectEnd.setText(data.optString("endselecttime"));
                binding.tvStartDate.setText(data.optString("startdate"));
                binding.tvEndDate.setText(data.optString("enddate"));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss");
                if (!TextUtils.isEmpty(data.optString("day1"))&&!TextUtils.equals("null",data.optString("day1"))){
                    String day1 = data.optString("day1");
                    String[] split = day1.split(",");
                    binding.tvDay1Start.setText(sdf.format(new Date(Long.parseLong(split[0]))));
                    binding.tvDay1End.setText(sdf.format(new Date(Long.parseLong(split[1]))));
                }
                if (!TextUtils.isEmpty(data.optString("day2"))&&!TextUtils.equals("null",data.optString("day2"))){
                    String day2 = data.optString("day2");
                    String[] split = day2.split(",");
                    binding.tvDay2Start.setText(sdf.format(new Date(Long.parseLong(split[0]))));
                    binding.tvDay2End.setText(sdf.format(new Date(Long.parseLong(split[1]))));
                }
                if (!TextUtils.isEmpty(data.optString("day3"))&&!TextUtils.equals("null",data.optString("day3"))){
                    String day3 = data.optString("day3");
                    String[] split = day3.split(",");
                    binding.tvDay3Start.setText(sdf.format(new Date(Long.parseLong(split[0]))));
                    binding.tvDay3End.setText(sdf.format(new Date(Long.parseLong(split[1]))));
                }
                if (!TextUtils.isEmpty(data.optString("day4"))&&!TextUtils.equals("null",data.optString("day4"))){
                    String day4 = data.optString("day4");
                    String[] split = day4.split(",");
                    binding.tvDay4Start.setText(sdf.format(new Date(Long.parseLong(split[0]))));
                    binding.tvDay4End.setText(sdf.format(new Date(Long.parseLong(split[1]))));
                }
                if (!TextUtils.isEmpty(data.optString("day5"))&&!TextUtils.equals("null",data.optString("day5"))){
                    String day5 = data.optString("day5");
                    String[] split = day5.split(",");
                    binding.tvDay5Start.setText(sdf.format(new Date(Long.parseLong(split[0]))));
                    binding.tvDay5End.setText(sdf.format(new Date(Long.parseLong(split[1]))));
                }
                if (!TextUtils.isEmpty(data.optString("day6"))&&!TextUtils.equals("null",data.optString("day6"))){
                    String day6 = data.optString("day6");
                    String[] split = day6.split(",");
                    binding.tvDay6Start.setText(sdf.format(new Date(Long.parseLong(split[0]))));
                    binding.tvDay6End.setText(sdf.format(new Date(Long.parseLong(split[1]))));
                }
                if (!TextUtils.isEmpty(data.optString("day7"))&&!TextUtils.equals("null",data.optString("day7"))){
                    String day7 = data.optString("day7");
                    String[] split = day7.split(",");
                    binding.tvDay7Start.setText(sdf.format(new Date(Long.parseLong(split[0]))));
                    binding.tvDay7End.setText(sdf.format(new Date(Long.parseLong(split[1]))));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    public void initSelectDateTime(TextView textView){
        textView.setOnClickListener(v -> {
            final String[] datetime = {""};
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = null;
            TimePickerDialog timePickerDialog = null;
            timePickerDialog = new TimePickerDialog(CourseEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    textView.setText(datetime[0] + " " +(i>=10?i:("0"+i))+":"+(i1>=10?i1:("0"+i1))+":00");
                }
            },0,0,true);
            TimePickerDialog finalTimePickerDialog = timePickerDialog;
            datePickerDialog = new DatePickerDialog(CourseEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    finalTimePickerDialog.show();
                    month = month+1;
                    datetime[0] = ""+(year>=10?year:("0"+year))+"-"+(month>=10?month:("0"+month))+"-"+(dayOfMonth>=10?dayOfMonth:("0"+dayOfMonth));
                }
            }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    public void initSelectTime(TextView textView){
        textView.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = null;
            timePickerDialog = new TimePickerDialog(CourseEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    textView.setText((i>=10?i:("0"+i))+":"+(i1>=10?i1:("0"+i1))+":00");
                }
            },0,0,true);
            timePickerDialog.show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10000 && resultCode ==RESULT_OK){
            teacherId = data.getIntExtra("teacher_id", 0);
            String teacherName = data.getStringExtra("teacher_name");
            binding.tvTeacher.setText(teacherName);
        }
    }
}