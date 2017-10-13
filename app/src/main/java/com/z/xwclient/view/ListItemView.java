package com.z.xwclient.view;

import com.z.xwclient.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
/**
 * 自定义视频播放的界面的listview的条目的样式
 */
public class ListItemView extends FrameLayout {

	private ImageButton mPlay;

	public ListItemView(Context context) {
		//super(context);
		this(context,null);
	}

	public ListItemView(Context context, AttributeSet attrs) {
		//super(context, attrs);
		this(context,attrs,-1);
	}
	
	public ListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//加载默认的样式
		View view = View.inflate(context, R.layout.videofragment_listview_normal, null);
		mPlay = (ImageButton) view.findViewById(R.id.normal_ibtn_play);
		this.addView(view);
	}
	
	//因为点击播放按钮要实现，播放界面的切换操作，但是按钮是在ListitemView中的，点击操作是在VideoFragment的adapter中实现的
	/**
	 * 提供给VideoFragment中的adapter的getView方法调用，用来在getview方法中实现点击操作
	 */
	public ImageButton getPlayButton(){
		return mPlay;
	}
	
	private MyMediaPlay mMediaPlay;
	/**
	 * 提供给VideoFragment中的adapter的getView方法调用,添加播放界面
	 */
	public void addVideoView(MyMediaPlay mediaPlay){
		this.mMediaPlay = mediaPlay;
		//将播放界面获取，添加自定义控件中展示
		this.addView(mediaPlay.getRooView());
	}
	
	/**
	 * 删除播放界面，停止播放
	 */
	public void removeVideoView(){
		//停止视频播放
		this.mMediaPlay.release();
		//删除界面
		this.removeView(this.mMediaPlay.getRooView());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
