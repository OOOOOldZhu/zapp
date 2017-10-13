package com.z.xwclient.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.z.xwclient.HomeActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 菜单页和内容页的父类
 * 
 */
public abstract class BaseFragment extends Fragment {

	public Activity activity;
	public View view;
	public SlidingMenu slidingMenu;

	// 初始化数据
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//getActivity() : 获取fragment所在的Activity
		activity = getActivity();
		//因为SlidingMenu是添加在HomeActivity中，而Fragment中也是添加HomeActivity中的，在BaseFragment中已经通过getActivity获取到了HomeActivity
		slidingMenu = ((HomeActivity)activity).getSlidingMenu();
		super.onCreate(savedInstanceState);
	}

	// 加载布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//因为fragment加载界面和加载数据的操作，是在onCreateView和onActivityCreated方法中执行的，并不是在initView和initData方法中执行
		//所以onCreateView和onActivityCreated方法要调用initview和initdata方法，实现让fragment显示界面和加载数据
		view = initView();
		return view;
	}

	// 加载数据
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		super.onActivityCreated(savedInstanceState);
	}


	//2.因为父类，抽取到了显示界面和加载数据操作，但是父类并不知道子类要显示什么界面，加载什么数据，所以父类可以创建抽象方法，子类实现抽象方法，根据自己的特性进行相应的操作
	/**
	 * 显示界面
	 *
	 */
	public abstract View initView();

	/**
	 * 加载数据
	 *
	 */
	public abstract void initData();













}
