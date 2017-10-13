package com.z.xwclient.fragment_grandfather;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.z.xwclient.R;
import com.z.xwclient.base.BaseFragment;
import com.z.xwclient.fragment_parent.GovFragment;
import com.z.xwclient.fragment_parent.HomeFragment;
import com.z.xwclient.fragment_parent.NewsCenterFragment;
import com.z.xwclient.fragment_parent.SettingFragment;
import com.z.xwclient.fragment_parent.VideoFragment;

public class ContentFragment extends BaseFragment {

	private RadioGroup mButtons;

	@Override
	public View initView() {
		/*
		 * TextView textView = new TextView(activity);
		 * textView.setText("内容页的fragment");
		 */
		view = View.inflate(activity, R.layout.contentfragment, null);
		return view;
	}

	@Override
	public void initData() {

		mButtons = (RadioGroup) view
				.findViewById(R.id.contentfragment_rg_buttons);

		// 在内容展示界面，展示RadioButton按钮对应的Fragment界面

		// 打开界面，默认显示首页的Fragment，所在先加载首页的Fragment进行显示
		// 获取activity中的Fragment的子的Fragment的管理者，用来给Fragment中添加Fragment操作的
		getChildFragmentManager()
				.beginTransaction()
				.replace(R.id.contentfragment_fra_pager, new HomeFragment(),
						"SHOUYE").commit();

		// 设置首页的按钮默认选中
		mButtons.check(R.id.contentfragment_rbtn_home);// 设置选中那个RadioButton,id:选中的RadioButton的id

		// 点击相应的RadioButton切换相应的界面
		// 监听RadioGroup中的RadioButton的选中操作
		mButtons.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// 当RadioButton被选中的时候调用的方法
			// checkedId : 选中的RadioButton的id
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 通过选中的RadioButton的id，判断是哪个RadioButton选中了
				switch (checkedId) {
				case R.id.contentfragment_rbtn_home:
					// 首页
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fra_pager, new HomeFragment(),
							"SHOUYE").commit();
					
					//设置侧拉菜单是否可以侧拉
					isSlidingMenuToggle(false);
					
					break;
				case R.id.contentfragment_rbtn_newscenter:
					// 新闻
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fra_pager, new NewsCenterFragment(),
							"NEWSCENTER").commit();
					
					//设置侧拉菜单是否可以侧拉
					isSlidingMenuToggle(true);
					
					break;
				case R.id.contentfragment_rbtn_video:
					// 视频
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fra_pager, new VideoFragment(),
							"VIDEO").commit();
					
					//设置侧拉菜单是否可以侧拉
					isSlidingMenuToggle(true);
					
					break;
				case R.id.contentfragment_rbtn_gov:
					// 政务
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fra_pager, new GovFragment(),
							"GOV").commit();
					
					//设置侧拉菜单是否可以侧拉
					isSlidingMenuToggle(true);
					
					break;
				case R.id.contentfragment_rbtn_setting:
					// 设置
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fra_pager, new SettingFragment(),
							"SETTING").commit();
					
					//设置侧拉菜单是否可以侧拉
					isSlidingMenuToggle(false);
					
					break;
				}
			}
		});
	}
	
	/**
	 * 设置slidingmenu是否可以侧拉的操作
	 * istoggle:true：可以侧拉，false:不能进行侧拉
	 */
	public void isSlidingMenuToggle(boolean istoggle){
		if (istoggle) {
			//侧拉
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			//不能进行侧拉操作
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
