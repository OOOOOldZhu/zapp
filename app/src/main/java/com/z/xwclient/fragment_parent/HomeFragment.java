package com.z.xwclient.fragment_parent;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.z.xwclient.base.BasePagerFragment;

/**
 * 首页的Fragment
 * 因为每个界面都要显示界面加载数据，相同操作抽取父类
 */
public class HomeFragment extends BasePagerFragment {

	@Override
	public View initView() {
		// TODO Auto-generated method stub
		return super.initView();
	}
	
	@Override
	public void initData() {
		//加载数据，通过父类控件展示
		TextView textView = new TextView(activity);
		textView.setText("首页");
		textView.setTextSize(23);
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);
		//设置父类内容显示区域的数据
		mContent.addView(textView);
		//设置标题
		mTitle.setText("首页");
		//隐藏按钮
		mMenu.setVisibility(View.GONE);
		
		super.initData();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
