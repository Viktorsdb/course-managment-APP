package com.course.manage.utils;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.course.manage.App;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class HttpUtils {
//    public static String baseUrl = "http://192.168.0.16:32168/index.php/api";
    public static String baseUrl = "http://103.146.51.24/api";

    public static String login = baseUrl+"/user/login";
    public static String register = baseUrl+"/user/register";
    public static String studentList = baseUrl+"/admin/student_list";
    public static String teacherList = baseUrl+"/admin/teacher_list";
    public static String courselist = baseUrl+"/admin/course_list";
    public static String courseAdd = baseUrl+"/admin/course_add";
    public static String courseEdit = baseUrl+"/admin/course_edit";
    public static String courseDelete = baseUrl+"/admin/course_delete";
    public static String singinList = baseUrl+"/admin/signin_list";
    public static String courseDetail = baseUrl+"/admin/course_detail";

    public static String uploadFirebaseToken = baseUrl+"/admin/firebase_token";
    public static String studentSigninDetail = baseUrl+"/admin/student_signin_detail";

    public static String teacherCourselist = baseUrl+"/teacher/course_list";
    public static String teacherStudentlist = baseUrl+"/teacher/student_list";
    public static String teacherSigninlist = baseUrl+"/teacher/signin_list";
    public static String teacherHasCourse = baseUrl+"/teacher/has_course";

    public static String studentCourseMylist = baseUrl+"/student/course_my";
    public static String studentCourseAlllist = baseUrl+"/student/course_all";
    public static String studentCourseDelete = baseUrl+"/student/course_delete";
    public static String studentSignInList = baseUrl+"/student/signin_list";
    public static String studentSignIn = baseUrl+"/student/signin";
    public static String studentSelectCourse = baseUrl+"/student/select_course";
    public static String studentHasCourse = baseUrl+"/student/has_course";
    public static String profile = baseUrl+"/user/profile";
    public static void post(RequestParams requestParams,HttpCallack callack) {
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code==1){
                        if (!TextUtils.isEmpty(jsonObject.getString("msg"))){
                            Toast.makeText(App.app,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        }
                        if (callack!=null)
                            callack.onSuccess(result);
                    }else if (code==0){
                        Toast.makeText(App.app,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                    }else{
                        if (callack!=null)
                            callack.onError(new Exception(),false);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e(ex.getMessage());
                if (callack!=null)
                    callack.onError(ex,isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public interface HttpCallack {
        public void onSuccess(String result) throws JSONException;
        public void onError(Throwable ex, boolean isOnCallback);
    }
}
