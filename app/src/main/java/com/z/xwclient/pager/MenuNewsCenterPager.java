package com.z.xwclient.pager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.z.xwclient.R;
import com.z.xwclient.SelectItemActivity;
import com.z.xwclient.base.BaseMenuPager;
import com.z.xwclient.bean.NewsCenterInfo;
import com.z.xwclient.pager.item.MenuNewsCenterItemPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 菜单页新闻中心条目对象的界面,新闻界面
 * 
 * 不能使用fragment，因为Fragment嵌套太多，容易内存溢出
 * 不能使用Activity，因为Activity不能添加到Fragment中、
 * 可以使用一个普通java,因为显示界面的最终操作，起始就是View.inflate加载一个布局的view对象，展示布局的view对象就可以了
 * 
 * 因为每个界面都可以显示界面加载数据，相同操作，抽取到父类
 * 
 */
public class MenuNewsCenterPager extends BaseMenuPager{

	private NewsCenterInfo.NewsCenterDataInfo mCenterDataInfo;
	private ViewPager mViewPager;
	private TabPageIndicator mIndicator;
	
	private List<MenuNewsCenterItemPager> itemPagers;
	private Myadapter myadapter;
	private ImageButton mSelectItem;
	
	/**保存滑动标签的文本标题**/
	private List<String> titles;
	
	public MenuNewsCenterPager(Activity activity,NewsCenterInfo.NewsCenterDataInfo centerDataInfo) {
		super(activity);
		this.mCenterDataInfo = centerDataInfo;
	}

	@Override
	public View initView() {
		/*TextView textView = new TextView(activity);
		textView.setText("菜单详情页-新闻");
		textView.setTextColor(Color.RED);
		textView.setTextSize(22);
		textView.setGravity(Gravity.CENTER);*/
		view = View.inflate(activity, R.layout.menunewscenterpager, null);
		return view;
	}

	@Override
	public void initData() {
		mViewPager = (ViewPager) view.findViewById(R.id.menunewscenter_vp_viewpager);
		mIndicator = (TabPageIndicator) view.findViewById(R.id.menunewscenter_tpi_indicator);
		mSelectItem = (ImageButton) view.findViewById(R.id.menunewscenter_ibtn_selectitem);
		
		//设置给viewpager显示数据
		//因为viewpager中包含的都是界面，所以先把viewpager要填充的界面创建出来
		itemPagers = new ArrayList<MenuNewsCenterItemPager>();
		titles = new ArrayList<String>();
		itemPagers.clear();
		titles.clear();
		
		//根据标签的长度，创建相应个数的界面，标示的数据保存在NewsCenterInfo -> data集合的第一个元素的childern集合中
		for (int i = 0; i < mCenterDataInfo.children.size(); i++) {
			itemPagers.add(new MenuNewsCenterItemPager(activity,mCenterDataInfo.children.get(i).url));
			titles.add(mCenterDataInfo.children.get(i).title);
		}
		
		//通过viewpager展示界面
		if (myadapter == null) {
			myadapter = new Myadapter();
			mViewPager.setAdapter(myadapter);
		}else{
			myadapter.notifyDataSetChanged();
		}
		
		//将viewpager和标签关联起来的操作
		mIndicator.setViewPager(mViewPager);
		
		//设置viewpager的界面切换监听，实现只有切换到北京界面的是，才可以打开侧拉菜单
		/*mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//判断是否切换到北京界面，切换到可以打开侧拉菜单，没有切换不可以打开侧拉菜单
				if (position == 0) {
					//打开侧拉菜单
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}else{
					//不可以打开侧拉菜单
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
				//因为viewpager和标签是联动的，如果滑动了viewpager，也要让标签进行切换界面才可以
				mIndicator.setCurrentItem(position);//设置当前显示的标签的
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});*/
		
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//判断是否切换到北京界面，切换到可以打开侧拉菜单，没有切换不可以打开侧拉菜单
				if (position == 0) {
					//打开侧拉菜单
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}else{
					//不可以打开侧拉菜单
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
				//因为viewpager和标签是联动的，如果滑动了viewpager，也要让标签进行切换界面才可以,使用addOnPageChangeListener不用考虑此问题
				//mIndicator.setCurrentItem(position);//设置当前显示的标签的
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		
		//设置加号的点击事件，实现跳转拖拽的界面
		mSelectItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转到拖拽界面
				Intent intent = new Intent(activity,SelectItemActivity.class);
				//需要将滑动标签的文本，全部传递给拖拽界面，进行显示
				intent.putExtra("titles", (Serializable)titles);
				activity.startActivity(intent);
			}
		});
	}
	
	/**viewpager的adapter**/
	private class Myadapter extends PagerAdapter{

		//设置展示标签的文本
		@Override
		public CharSequence getPageTitle(int position) {
			return mCenterDataInfo.children.get(position).title;
		}
		
		@Override
		public int getCount() {
			return itemPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			MenuNewsCenterItemPager menuNewsCenterItemPager = itemPagers.get(position);
			View rootView = menuNewsCenterItemPager.view;
			container.addView(rootView);
			//展示界面完成，加载数据，显示数据
			menuNewsCenterItemPager.initData();//加载显示界面对应的数据
			return rootView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
