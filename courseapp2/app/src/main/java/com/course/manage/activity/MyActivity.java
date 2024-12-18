package com.course.manage.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.course.manage.App;
import com.course.manage.R;
import com.course.manage.databinding.ActivityMyBinding;
import com.course.manage.utils.Base64ImageUtils;
import com.course.manage.utils.HttpUtils;
import com.course.manage.utils.SPUtils;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONException;
import org.xutils.http.RequestParams;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MyActivity extends AppCompatActivity {
    private ActivityMyBinding binding;

    public static final int PRC_PHOTO_PICKER = 10010;
    public static final int RC_CHOOSE_PHOTO = 10011;
    public ActivityResultLauncher<Intent> intentActivityResultLauncher;
    public ActivityResultLauncher<Intent> takePhotoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();
        binding = ActivityMyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                builder.setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.token = null;
                                App.user = null;
                                SPUtils.setInt("isAuto",0);
                                startActivity(new Intent(MyActivity.this,LoginActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
        if (!App.user.getAvatar().contains("data:image/svg+xml")){
            Glide.with(this).load(Base64ImageUtils.base64ToBitmap(App.user.getAvatar())).into(binding.ivAvatar);
        }
        binding.tvUsername.setText(App.user.getUsername());
        initPhoto();
    }


    private void initPhoto(){
        takePhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResultCallback<ActivityResult>) result -> {
            if (result.getResultCode() == RESULT_OK){
                Intent data = result.getData();
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                Glide.with(MyActivity.this).asBitmap().load(bitmap).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        binding.ivAvatar.setImageBitmap(resource);
                        String s = Base64ImageUtils.bitmapToBase64(resource).replaceAll("\n", "");
                        updateAvatar(s);
                    }
                });

            }
        });

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResultCallback<ActivityResult>) result -> {
            if (result.getResultCode() == RESULT_OK){
                Intent data = result.getData();
                Uri uri = data.getData();
                Glide.with(MyActivity.this).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        binding.ivAvatar.setImageBitmap(resource);
                        String s = Base64ImageUtils.bitmapToBase64(resource).replaceAll("\n", "");
                        updateAvatar(s);
                    }
                });

            }
        });
        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog();
            }
        });
    }


    public void updateAvatar(String img){
        RequestParams requestParams = new RequestParams(HttpUtils.profile+"?token="+App.user.getToken());
        requestParams.addParameter("avatar",img);
        HttpUtils.post(requestParams, new HttpUtils.HttpCallack() {
            @Override
            public void onSuccess(String result) throws JSONException {
                App.user.setAvatar(img);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent gallery =new Intent(Intent.ACTION_PICK);
            gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intentActivityResultLauncher.launch(gallery);
        } else {
            EasyPermissions.requestPermissions(this, "image selection requires the following permissions::\n\n1. Access photos on your device\n\n2. Take pictures", PRC_PHOTO_PICKER, perms);
        }
    }

    @AfterPermissionGranted(RC_CHOOSE_PHOTO)
    private void takePhotoWrapper() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoLauncher.launch(intent);
        } else {
            EasyPermissions.requestPermissions(this, "image selection requires the following permissions::\n\n1. Access photos on your device\n\n2. Take pictures", PRC_PHOTO_PICKER, perms);
        }
    }

    private void showListDialog(){
        final String[] items = {"take photo","pick picture"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(MyActivity.this);
        listDialog.setTitle("select image");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    takePhotoWrapper();
                }else if(which==1){
                    choicePhotoWrapper();
                }
            }
        });
        listDialog.show();
    }
}