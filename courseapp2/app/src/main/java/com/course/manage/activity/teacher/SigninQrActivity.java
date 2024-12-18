package com.course.manage.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.course.manage.R;
import com.course.manage.databinding.ActivitySigninQrBinding;
import com.gyf.immersionbar.ImmersionBar;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class SigninQrActivity extends AppCompatActivity {
    private ActivitySigninQrBinding binding;
    private int courseId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.gray)
                .init();
        binding = ActivitySigninQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        courseId = getIntent().getIntExtra("course_id",-1);
        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(String.valueOf(courseId), 800);
        binding.ivQrcode.setImageBitmap(bitmap);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}