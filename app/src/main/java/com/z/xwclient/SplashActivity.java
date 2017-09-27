package com.z.xwclient;

import com.z.xwclient.utils.Constants;
import com.z.xwclient.utils.SharedPreferencesTool;
import com.z.xwclient.view.CustomVideoView;

import android.app.Activity;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.InputStream;

public class SplashActivity extends Activity {
    String TAG="program";
    private CustomVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化ShareSDK
        //ShareSDK.initSDK(this);

        mVideoView = (CustomVideoView) findViewById(R.id.splash_vv_videoview);

        // 播放视频
        initData();
    }

    /**
     * 播放视频
     *
     */
    private void initData() {
        // 1.获取播放资源
        // android.resource:// : 获取工程中的res->raw中的资源
        // android.resource://工程的包名/R.raw.kr36
        
        try{
            AssetManager assetManager = this.getAssets();
            String [] files = assetManager.list("");//遍历assets根目录
            Log.d(TAG, "initData: "+files.toString());
            String s="file:///android_asset/kr36.mp4";
            mVideoView.setVideoURI(Uri.parse(s));
        }catch (Exception e){
            Log.d(TAG, "initData: "+e.toString());
        }
        /*mVideoView.setVideoURI(Uri.parse("android.resource://"
                + this.getPackageName() + "/" + R.raw.kr36));*/

        //3.监听视频是否播放完毕，如果播放完毕，重新开始播放
        //监听视频是否播放完毕的操作
        mVideoView.setOnCompletionListener(new OnCompletionListener() {

            //当视频播放完毕调用的方法
            @Override
            public void onCompletion(MediaPlayer mp) {
                //重新播放
                mVideoView.start();
            }
        });

        // 2.播放视频资源
        mVideoView.start();
    }

    /**
     * 进入智慧北京按钮的点击事件
     *
     */
    public void enter(View view){
        //判断用户是否是第一个进入应用，如果是：跳转引导界面，如果不是：跳转首页
        //问题：如何判断用户是否是第一次进入应用
        //获取用户是否是第一次进入的标示
        //还没有保存数据，保存数据需要到引导界面的开始体验按钮中去做,true:标示第一次进入，false:不是
        boolean isfirstenter = SharedPreferencesTool.getBoolean(this, Constants.ISFIRSTENTER, true);
        if (isfirstenter) {
            //第一次进入跳转到引导界面
            //startActivity(new Intent(this,GuideActivity.class));
        }else{
            //不是第一次进入跳转到首页
            //startActivity(new Intent(this,HomeActivity.class));
        }

        finish();
    }


























}
