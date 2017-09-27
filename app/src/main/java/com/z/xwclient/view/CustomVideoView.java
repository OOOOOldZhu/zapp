package com.z.xwclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;
/**
 * 自定义VideoView
 * 
 */
public class CustomVideoView extends VideoView {

	//代码中使用的时候调用
	public CustomVideoView(Context context) {
		//super(context);
		this(context,null);
	}

	//布局文件中使用的时候调用的
	public CustomVideoView(Context context, AttributeSet attrs) {
		//super(context, attrs);
		this(context,attrs,-1);
	}

	//不对外调用，只是内容让两个和一个参数的构造函数调用
	public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	//测量的控件
	//widthMeasureSpec  : 期望的宽（理解：布局文件的宽）
	//heightMeasureSpec : 期望的高（理解：布局文件的高）
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//调用系统帮助我们测量
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//获取控件被父控件约束的宽高
		//参数1：控件默认的宽高
		//参数2：父控件约束的宽高
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		//自己测量的控件的宽高;this:测量哪个控件
		this.setMeasuredDimension(width, height);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
