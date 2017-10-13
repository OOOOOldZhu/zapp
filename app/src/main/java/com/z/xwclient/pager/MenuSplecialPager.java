package com.z.xwclient.pager;

import com.z.xwclient.base.BaseMenuPager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 菜单页新闻中心条目对象的界面,专题界面
 * 
 * 不能使用fragment，因为Fragment嵌套太多，容易内存溢出
 * 不能使用Activity，因为Activity不能添加到Fragment中、
 * 可以使用一个普通java,因为显示界面的最终操作，起始就是View.inflate加载一个布局的view对象，展示布局的view对象就可以了
 * 
 * 因为每个界面都可以显示界面加载数据，相同操作，抽取到父类
 * 
 */
public class MenuSplecialPager extends BaseMenuPager{

	public MenuSplecialPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(activity);
		textView.setText("菜单详情页-专题");
		textView.setTextColor(Color.RED);
		textView.setTextSize(22);
		textView.setGravity(Gravity.CENTER);
		return textView;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
}
