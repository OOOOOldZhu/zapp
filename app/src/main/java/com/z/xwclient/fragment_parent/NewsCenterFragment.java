package com.z.xwclient.fragment_parent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.z.xwclient.base.BaseMenuPager;
import com.z.xwclient.base.BasePagerFragment;
import com.z.xwclient.bean.EventBusInfo;
import com.z.xwclient.utils.NetUrl;
import com.z.xwclient.bean.NewsCenterInfo;
import com.z.xwclient.pager.MenuActionPager;
import com.z.xwclient.pager.MenuNewsCenterPager;
import com.z.xwclient.pager.MenuPhotosPager;
import com.z.xwclient.pager.MenuSplecialPager;
import com.z.xwclient.utils.Constants;
import com.z.xwclient.utils.SharedPreferencesTool;

/**
 * 新闻中心的Fragment
 * 因为每个界面都要显示界面加载数据，相同操作抽取父类
 */
public class NewsCenterFragment extends BasePagerFragment {

	private List<BaseMenuPager> pagers;
	
	/**菜单页条目文本数组**/
	private String[] TITLES = new String[] { "新闻", "专题", "组图", "互动" };
	
	@Override
	public View initView() {
		// TODO Auto-generated method stub
		return super.initView();
	}
	
	@Override
	public void initData() {
		
		//加载数据，通过父类控件展示
		/*TextView textView = new TextView(activity);
		textView.setText("新闻");
		textView.setTextSize(23);
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);*/
		//设置父类内容显示区域的数据
		//mContent.addView(textView);
		//设置标题
		//mTitle.setText("新闻");
		//隐藏按钮
		mMenu.setVisibility(View.VISIBLE);
		
		//请求服务器，获取新闻中心的数据
		//2.再次请求服务器，判断是否有缓存数据，有，加载显示缓存数据
		String sp_msg = SharedPreferencesTool.getString(activity, Constants.NEWSCENTERMSG, "");
		if (!TextUtils.isEmpty(sp_msg)) {
			//解析加载缓存数据
			processJSON(sp_msg);
		}
		getData();
		
		super.initData();
	}

	/**
	 * 请求服务器获取数据
	 *
	 */
	private void getData() {
		//联网耗时操作，联网权限，使用第三方的工具实现请求服务器的操作，okhttp3
		//获取okhttp请求端
		OkHttpClient okHttpClient = new OkHttpClient();
		//设置请求路径，默认就是get请求
		Request request = new Request.Builder().url(NetUrl.NEWSCENTERURL).build();
		//创建一个请求
		//参数:请求的配置信息
		Call newCall = okHttpClient.newCall(request);
		//请求服务器
		newCall.enqueue(new Callback() {
			//请求成功调用的方法
			//response : 服务返回的数据，封装了服务返回的数据
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				//获取服务器返回的数据
				final String json = response.body().string();
				//System.out.println("服务返回的数据："+json);
				//1.获取服务器的数据，保存服务器的数据
				SharedPreferencesTool.saveString(activity, Constants.NEWSCENTERMSG, json);
				//3.获取最新的数据之后，还要将缓存数据覆盖显示
				//细节：回调是在子线程中执行的
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						processJSON(json);
					}
				});
			
			}
			//请求失败调用的方法
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	

	/**
	 * 解析json串的操作
	 *
	 */
	private void processJSON(String json) {
		Gson gson = new Gson();
		//fromJson : 将json串封装成对象
		//toJson:将对象转化成json串
		NewsCenterInfo newsCenterInfo = gson.fromJson(json, NewsCenterInfo.class);
		
		System.out.println(newsCenterInfo.data.get(0).title);
		
		showMenuPager(newsCenterInfo);
	}

	/**
	 * 显示切换侧拉菜单条目对应的界面
	 *
	 * 2016年12月13日   上午10:42:58
	 */
	private void showMenuPager(NewsCenterInfo newsCenterInfo) {
		
		//1.因为侧拉菜单条目的对应的界面，都是在新闻中心Fragment界面中进行切换的，所以必须先将界面添加到新闻中心的Fragment中，方便切换操作
		pagers = new ArrayList<BaseMenuPager>();
		pagers.clear();
		pagers.add(new MenuNewsCenterPager(activity,newsCenterInfo.data.get(0)));
		pagers.add(new MenuSplecialPager(activity));
		pagers.add(new MenuPhotosPager(activity,mPhoto));
		pagers.add(new MenuActionPager(activity));
		
		//2.切换界面
		switchPager(0);
	}
	
	/**
	 * 切换界面的操作，提供给MenuFragment的listView的条目点击事件调用的，方便点击菜单页的条目，实现切换新闻中心显示界面内容
	 * 但是条目点击事件在MenuFragment中，切换切换在NewsCenterFragment中，两个fragment是没有关系的，如果想实现点击菜单页条目切换新闻中心界面操作，需要EventBus
	 * position : 菜单页条目的索引，方便根据索引判断切换那个界面和标题
	 */
	public void switchPager(int position){
		//1.切换标题
		mTitle.setText(TITLES[position]);
		//2.切换界面
		//切换内容显示区域显示的界面
		//根据条目的索引获取对应的界面
		BaseMenuPager baseMenuPager = pagers.get(position);
		//将界面的View对象添加到内容显示区域进行显示
		//先删除之前的 界面
		mContent.removeAllViews();
		//再添加要显示的界面的view对象
		mContent.addView(baseMenuPager.view);
		//界面显示完成，还要填充数据
		baseMenuPager.initData();
		
		//判断是否切换到组图界面，如果切换到了，显示组图按钮，没有切换，不显示
		//判断position
		//判断界面
		//instanceof : 判断前面的对象是否是后面的类型
		if (baseMenuPager instanceof MenuPhotosPager) {
			mPhoto.setVisibility(View.VISIBLE);
		}else{
			mPhoto.setVisibility(View.GONE);
		}
	}
	
	
	
	@Override
	public void onStart() {
		super.onStart();
		//注册EventBus操作
		//判断EventBus是否注册
		//isRegistered : 判断Eventbus是否在当前Fragment注册
		if (!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);//注册EventBus
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);//注销EventBus
		}
	}
	
	
	//设置接受EventBus消息的方法
	//ThreadMode.POSTING : 默认的类型，发送操作和接受操作在一个线程中进行操作
	//ThreadMode.MAIN : 不管在哪个线程中发送，都是在主线程中接受
	//ThreadMode.BACKGROUND : 如果是在子线程发送，接受操作也是在该子线程，如果是在主线程发送，会启动一个子线程进行接受
	//ThreadMode.ASYNC ： 不管接受和发送都是在子线程中执行的
	@Subscribe(threadMode = ThreadMode.MAIN)  
	public void getEventBusMsg(EventBusInfo eventBusInfo){
		switchPager(eventBusInfo.position);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
