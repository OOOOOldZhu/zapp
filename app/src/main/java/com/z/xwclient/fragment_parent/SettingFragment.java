package com.z.xwclient.fragment_parent;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.z.xwclient.CollectionActivity;
import com.z.xwclient.R;
import com.z.xwclient.base.BasePagerFragment;

/**
 * 首页的Fragment
 * 因为每个界面都要显示界面加载数据，相同操作抽取父类
 */
public class SettingFragment extends BasePagerFragment {

	@Override
	public View initView() {
		// TODO Auto-generated method stub
		return super.initView();
	}
	
	@Override
	public void initData() {
		
		//加载数据，通过父类控件展示
		/*TextView textView = new TextView(activity);
		textView.setText("设置");
		textView.setTextSize(23);
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);*/
		view = View.inflate(activity, R.layout.settingfragment, null);
		Button mShouCang = (Button) view.findViewById(R.id.setting_btn_shoucang);
		
		mShouCang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转到收藏界面
				startActivity(new Intent(activity,CollectionActivity.class));
			}
		});
		
		//设置父类内容显示区域的数据
		mContent.addView(view);
		//设置标题
		mTitle.setText("设置");
		//隐藏按钮
		mMenu.setVisibility(View.GONE);
		
		super.initData();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
