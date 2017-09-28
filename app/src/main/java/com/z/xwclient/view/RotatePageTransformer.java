package com.z.xwclient.view;

import android.view.View;
/**
 * 自定义的动画效果
 * 
 */
public class RotatePageTransformer implements android.support.v4.view.ViewPager.PageTransformer{

	/**最大角度**/
	private static final float MAXRUDIOS = 25f;
	
	//界面切换的时候调用的方法
	//参数1：界面的view对象
	//参数2：界面的切换的状态，当前界面的position是0，预加载的界面的position1，如果当前界面切换出去了，当前的界面的position是-1，预加载界面切换进来position是0,有几个界面显示几个界面的状态
	@Override
	public void transformPage(View page, float position) {
		System.out.println("ViewPager的切换动画的position:"+position);
		
		int width = page.getWidth();
		
		//本质就是实现旋转动画
		//因为是平面效果，关于z轴的旋转
		//参数：旋转的角度
		//page.setRotation(rotation);
		//因为只是当前界面和预加载界面实现旋转效果，跟其他界面没关系，当前界面和预加载界面的position是[0,-1]和[1,0]
		//判断是否是当前界面和预加载界面，执行动画
		if (position < -1) {
			//小于-1，不在[0,-1]范围中，不执行动画
			page.setRotation(0);
		}else if(position > 1){
			//大于1，不在[1,0]范围中，不执行动画
			page.setRotation(0);
		}else if(position <=0 ){
			//范围是[0,-1],是当前界面，执行动画
			page.setRotation(position * MAXRUDIOS);
			//设置选中中心的x的坐标
			page.setPivotX(width / 2);
			//设置选中中心的y的坐标
			page.setPivotY(page.getHeight());
			
		}else if(position <= 1){
			//范围是[1,0],是预加载界面，执行动画
			page.setRotation(position * MAXRUDIOS);
			//设置选中中心的x的坐标
			page.setPivotX(width / 2);
			//设置选中中心的y的坐标
			page.setPivotY(page.getHeight());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
