package com.z.xwclient;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jaeger.library.StatusBarUtil;
import com.z.xwclient.utils.Constants;
import com.z.xwclient.utils.DensityUtil;
import com.z.xwclient.utils.SharedPreferencesTool;
import com.z.xwclient.view.RotatePageTransformer;

/**
 * 引导界面
 * 
 */
public class GuideActivity extends Activity {

	private ViewPager mViewPager;
	
	private int[] mImageIds = new int[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
	
	private List<ImageView> imageViews;

	private Button mStart;

	private LinearLayout mLLDot;

	private ImageView mRedDot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//代码去除标题栏
		setContentView(R.layout.activity_guide);
		//通过第三方框架实现了沉浸式状态栏操作，必须在setContentView之后执行
		StatusBarUtil.setColor(this, getResources().getColor(R.color.yellow));
		
		initView();
	}

	/**
	 * 初始化控件
	 *
	 */
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.guide_vp_viewpager);
		mStart = (Button) findViewById(R.id.guide_btn_start);
		mLLDot = (LinearLayout) findViewById(R.id.guide_ll_dot);
		mRedDot = (ImageView) findViewById(R.id.guide_iv_reddot);
		
		imageViews = new ArrayList<ImageView>();
		imageViews.clear();
		//通过viewpager展示的三张图片
		//先将图片，放到imageView中，然后再将ImageView放到viewpager中展示
		//1.根据图片的张数，创建相应个数的imageView,并存放，方便viewpager展示
		for (int i = 0; i < mImageIds.length; i++) {
			//创建相应的imageView
			createImageView(i);
			//根据图片的张数，创建点的个数
			createDot();
		}
		//2.通过viewpager展示imageView
		mViewPager.setAdapter(new Myadapter());
		
		
		//4.实现viewpager的界面切换动画
		//mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		//设置viewpager切换动画的，
		//参数1：如果是第一个到最后一个：true,如果是最后一个到第一个：false
		//参数2：自定义的动画效果
		//mViewPager.setPageTransformer(true, new DepthPageTransformer());
		mViewPager.setPageTransformer(true, new RotatePageTransformer());
		
		//3.实现viewpager的界面切换监听，监听当切换到第三个界面的时候，显示按钮，否则隐藏按钮
		//跟setOnPageChangeListener效果是一样
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			//当界面切换完成调用的方法
			//position : 切换到的界面的索引
			@Override
			public void onPageSelected(int position) {
				//判断如果已经切换到第三个界面了，显示按钮，否则隐藏按钮
				if (position == imageViews.size()-1) {
					mStart.setVisibility(View.VISIBLE);
				}else{
					mStart.setVisibility(View.GONE);
				}
			}
			
			//viewpager切换界面的时候调用的方法
			//position : 条目的索引
			//positionOffset : 界面移动的百分比
			//positionOffsetPixels : 界面移动的像素
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				//当viewpager切换界面的时候，实现移动红色的点
				//translationX : 控件平移的距离,从右往左滑动从0开始慢慢变大，如果切换完成，百分比瞬间归0,重新从0开始，进行下一次的百分比计算
				
				System.out.println(positionOffset+"");
				//红色点的移动距离 = 移动的百分比*20
				// 0 * 20 = 0    1*20=20
				//mRedDot.setTranslationX(positionOffset * 20 + position * 20);
				mRedDot.setTranslationX((positionOffset+ position) * DensityUtil.dip2px(GuideActivity.this, 20));
			}
			
			//当切换状态改变的时候调用的方法
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	/**
	 * 创建图片对应的imageView
	 * i:表示创建第几个图片对应的imageView
	 */
	private void createImageView(int i) {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(mImageIds[i]);
		imageViews.add(imageView);
	}
	
	/**
	 * 根据图片的张数创建点
	 *
	 */
	private void createDot() {
		View view = new View(this);
		view.setBackgroundResource(R.drawable.shape_guide_dot);
		
		LayoutParams params = new LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
		params.rightMargin=DensityUtil.dip2px(this, 10);
		
		view.setLayoutParams(params);//将属性设置给view对象，使用属性生效
		//添加linearlayout中展示
		mLLDot.addView(view);
	}
	
	/**ViewPager的adapter**/
	private class Myadapter extends PagerAdapter{

		//设置条目个数
		@Override
		public int getCount() {
			return imageViews.size();
		}

		//view:viewpager的页面对象
		//object：instantiateItem返回对象
		//判断是否可以加载显示界面
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		//添加显示viewpager的界面
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//根据条目的索引，获取条目对应的imageview
			ImageView imageView = imageViews.get(position);
			//将imageView添加到viewpager中显示
			container.addView(imageView);
			//添加什么View对象，返回什么view对象
			return imageView;
		}
		
		//删除条目
		//object : instantiateItem的返回值
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//super.destroyItem(container, position, object);抛异常的
			container.removeView((View) object);
		}
		
	}
	
	/**
	 * 开始体验按钮的点击事件
	 *
	 */
	public void start(View view){
		//保存是否是第一次进入的标示
		SharedPreferencesTool.saveBoolean(this, Constants.ISFIRSTENTER, false);
		//跳转的首页
		startActivity(new Intent(this,HomeActivity.class));
		//移除引导界面，避免在首页点击返回键，再回退到引导界面
		finish();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
