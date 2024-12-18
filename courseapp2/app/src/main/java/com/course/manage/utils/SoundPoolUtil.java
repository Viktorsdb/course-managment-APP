package com.course.manage.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.course.manage.R;

public class SoundPoolUtil {

    @SuppressLint("StaticFieldLeak")
    private volatile static SoundPoolUtil mInstance;
    private SoundPool mSoundPool;
    private Context mContext;
    public static int soundPlayId = 0;

    private SoundPoolUtil() {
    }

    public static SoundPoolUtil getInstance() {
        if (mInstance == null) {
            synchronized (SoundPoolUtil.class) {
                mInstance = new SoundPoolUtil();
            }
        }
        return mInstance;
    }

    /**
     * 需要先初始化再调用playSound
     */
    public void init(Context context) {
        mContext = context;
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            // 传入最多播放音频数量,
            builder.setMaxStreams(1);
            // AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            // 设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            // 加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            mSoundPool = builder.build();
        } else {
            /**
             * 第一个参数：SoundPool对象的最大并发流数
             * 第二个参数：AudioManager中描述的音频流类型
             * 第三个参数：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
             */
            mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
    }

    /**
     * 播放音频
     */
    public void playSound() {

        //加载音频，获取音频load的ID
        int resSoundId = mSoundPool.load(mContext, R.raw.ring, 1);
        // 异步需要等待加载完成，音频才能播放成功
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPlayId = mSoundPool.play(resSoundId,
                            1,  // 左声道音量取值范围0-1
                            1,  // 右声道音量取值范围0-1
                            1,    // 优先级
                            -1,  // 循环播放次数 设为-1代表循环播放
                            1);     // 回放速度，该值在0.5-2.0之间 1为正常速度
                }
            }
        });
        // 当loop值为“0”时，播放模式为单次模式；当loop值为一个正整数时，loop值意味着可以播放的次数。
        // mSoundPool.setLoop(soundPlayId, -1);//这个也可以设置无限循环
    }

    /**
     * 停止播放声音
     */
    public void stopSound() {
        if (mSoundPool != null) {
            if (soundPlayId != 0) {
                mSoundPool.stop(soundPlayId);
                soundPlayId = 0;
            }
            if (soundPlayId != 0) {
                mSoundPool.unload(soundPlayId);
                onDestroy();
                soundPlayId = 0;
            }
        }
    }

    /**
     * 释放资源
     */
    public void onDestroy() {
        if (mSoundPool != null) {
            mSoundPool.autoPause();
            mSoundPool.unload(R.raw.ring);
            mSoundPool.release();
            mSoundPool = null;
        }
    }

}