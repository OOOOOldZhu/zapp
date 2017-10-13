package com.z.xwclient.view;

import java.io.IOException;

import com.z.xwclient.R;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * 视频播放的类
 */
public class MyMediaPlay implements OnClickListener{

	private ImageButton mPlay;
	private ProgressBar mLoading;
	private TextureView mTexture;
	private View view;

	private Surface mSurfce;
	private MediaPlayer mediaPlayer;
	private Context mContext;
	
	//但是视频播放时需要界面的，普通的java类是无法显示界面
	public MyMediaPlay(Context context){
		this.mContext = context;
		view = View.inflate(context, R.layout.videofragment_mediaplay, null);
		mTexture = (TextureView) view.findViewById(R.id.mediaplay_tv_texture);
		mLoading = (ProgressBar) view.findViewById(R.id.mediaplay_pb_loading);
		mPlay = (ImageButton) view.findViewById(R.id.mediaplay_ibtn_play);
		
		//在播放视频的时候，需要等TextureView界面加载好，才能获取到播放界面才能进行播放
		//播放界面加载视频播放界面的监听
		mTexture.setSurfaceTextureListener(new SurfaceTextureListener() {
			

			//当界面加载好的时候调用的方法
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
					int height) {
				//获取播放界面
				//根据TextureView提供SurfaceTexture获取真正的播放界面
				mSurfce = new Surface(surface);
				//获取Mediaplay
				mediaPlayer = new MediaPlayer();
			}
			
			//播放界面更新的时候 调用
			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture surface) {
				// TODO Auto-generated method stub
				
			}
			
			//当播放界面的缓存区域大小改变的时候调用的方法
			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
			
			//当播放界面销毁的时候调用的方法
			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		
		//设置按钮点击事件，实现播放和暂停操作
		mPlay.setOnClickListener(this);
	}
	
	
	//因为播放界面也是要放到自定义Framlayout中展示的
	/**
	 * 提供给ListItemView调用的方法，方便ListItemView加载播放界面
	 */
	public View getRooView(){
		return view;
	}
	
	/**
	 * 播放视频的操作
	 * url:视频播放地址
	 */
	public void play(final String url){
		//进度条展示
		mLoading.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//播放视频的操作
				playView(url);
			}
		}, 1000);
	}

	/**
	 * 播放视频具体操作
	 *
	 * 2016年12月18日   上午10:27:42
	 */
	protected void playView(String url) {
		try {
			mediaPlayer.setDataSource(mContext, Uri.parse(url));//设置视频播放源
			mediaPlayer.setSurface(mSurfce);//设置播放的界面
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放的类型
			mediaPlayer.setLooping(true);//设置循环播放
			mediaPlayer.prepareAsync();//异步准备播放操作
			//设置监听是否准备完毕
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				//当准备完毕的时候调用的方法
				@Override
				public void onPrepared(MediaPlayer mp) {
					mLoading.setVisibility(View.GONE);
					mediaPlayer.start();//开始播放
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(View v) {
		//实现播放和暂定播放功能
		//isPlaying（）：是否播放
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			//正在播放，点击暂定，设置暂定的图标
			mediaPlayer.pause();//暂停播放
			mPlay.setBackgroundResource(R.drawable.play_icon);
		}else{
			//暂停播放，点击播放，设置播放图标
			mediaPlayer.start();
			mPlay.setBackgroundResource(R.drawable.pause_video);
		}
	}
	
	/**
	 * 停止视频播放
	 */
	public void release(){
		mediaPlayer.release();//停止播放
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
