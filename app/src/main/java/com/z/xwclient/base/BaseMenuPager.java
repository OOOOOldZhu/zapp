package com.z.xwclient.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.z.xwclient.HomeActivity;

import android.app.Activity;
import android.view.View;

/**
 * 菜单页的新闻，专题，组图，互动界面的父类
 * 
 */
public abstract class BaseMenuPager {
	
	public View view;
	public Activity activity;
	public SlidingMenu slidingMenu;
	public BaseMenuPager(Activity activity){
		this.activity = activity;
		slidingMenu = ((HomeActivity)activity).getSlidingMenu();
		
		//在刚创建界面的时候就加载界面的布局文件，等待显示
		view = initView();
	}

	//父类不知道子类，要显示什么界面和加载什么数据，所以创建抽象方法，子类根据自己的特性进行实现
	/**
	 * 显示界面的操作
	 *
	 */
	public abstract View initView();
	/**
	 * 初始化控件显示数据
	 *
	 */
	public abstract void initData();
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
