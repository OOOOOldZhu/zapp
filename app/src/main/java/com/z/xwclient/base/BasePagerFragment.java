package com.z.xwclient.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.z.xwclient.HomeActivity;
import com.z.xwclient.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 首页新闻等界面的父类
 */
public class BasePagerFragment extends Fragment {

	public View view;
	public Activity activity;
	public TextView mTitle;
	public ImageButton mMenu;
	public FrameLayout mContent;
	public SlidingMenu slidingMenu;
	public ImageButton mPhoto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = getActivity();
		slidingMenu = ((HomeActivity)activity).getSlidingMenu();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView();
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		super.onActivityCreated(savedInstanceState);
	}
	
	//父类不知道子类要显示什么界面，加载什么数据，所以创建方法，子类根据自己的情况实现方法，进行操作
	
	
	//因为首页等页面都是标题栏+内容显示区域的布局，所以可以抽取到父类，但是父类没有加载布局的操作，所以将initView和initData方法设置为不是抽象的方法，
	//这样在父类中调用initView和initData方法的是就会直接调用父类中方法进行操作
	/**
	 * 显示 界面
	 */
	public View initView(){
		//加载父类抽取首页、新闻等界面的布局
		view = View.inflate(activity, R.layout.basepagerfragment, null);
		//初始化父类中的控件，用来填充显示子类的数据
		mTitle = (TextView) view.findViewById(R.id.titlebar_tv_title);
		mMenu = (ImageButton) view.findViewById(R.id.titlebar_btn_menu);
		mContent = (FrameLayout) view.findViewById(R.id.basefragment_fram_content);
		//初始化组图的按钮
		mPhoto = (ImageButton) view.findViewById(R.id.titlebar_btn_photo);
		
		//设置菜单页按钮的点击事件，实现打开关闭侧拉菜单
		mMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});
		
		return view;
	};
	
	/**
	 * 加载数据
	 *
	 */
	public void initData(){
		
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
