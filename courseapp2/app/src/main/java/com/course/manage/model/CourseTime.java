package com.course.manage.model;


import java.util.ArrayList;
import java.util.List;

public class CourseTime {
    private List<Course> courseList = new ArrayList<>();
    private String day;

    public CourseTime(List<Course> courseList, String day) {
        this.courseList = courseList;
        this.day = day;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}