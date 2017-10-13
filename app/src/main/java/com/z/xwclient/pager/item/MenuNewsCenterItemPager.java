package com.z.xwclient.pager.item;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.sax.StartElementListener;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.viewpagerindicator.CirclePageIndicator;
import com.z.xwclient.NewsDetailActivity;
import com.z.xwclient.R;
import com.z.xwclient.base.BaseMenuPager;
import com.z.xwclient.bean.NetUrl;
import com.z.xwclient.bean.NewBean;
import com.z.xwclient.utils.Constants;
import com.z.xwclient.utils.SharedPreferencesTool;
import com.z.xwclient.view.RoolViewPager;

/**
 * 菜单详情页-新闻页面中的viewpager包含的页面 用来处理小viewpager和listview操作的 2016年12月13日 下午3:14:17
 */
public class MenuNewsCenterItemPager extends BaseMenuPager {

	private String mUrl;
	private View mViewPagerView;
	private View mListViewPagerView;
	private RoolViewPager mViewPager;
	private TextView mTitle;
	private CirclePageIndicator mIndicator;
	private PullToRefreshListView mPullListView;

	/** 存放图片路径 **/
	private List<String> imagerUrls = new ArrayList<String>();
	/** 保存标题的操作 **/
	private List<String> titles = new ArrayList<String>();
	/** 存放listview数据的操作 **/
	private List<NewBean.News> mNews = new ArrayList<NewBean.News>();

	private Myadapter myadapter;
	private MyListViewAdapter listViewAdapter;
	private Handler handler;
	private ListView mListView;
	/** 加载更多 **/
	private String loadmore;

	/** 保存已读新闻id **/
	private List<String> mIds = new ArrayList<String>();

	public MenuNewsCenterItemPager(Activity activity, String url) {
		super(activity);
		this.mUrl = url;
	}

	@Override
	public View initView() {
		/*
		 * TextView textView = new TextView(activity);
		 * textView.setText("111111111"); textView.setTextColor(Color.RED);
		 */

		mViewPagerView = View.inflate(activity,
				R.layout.menunewscenter_viewpager, null);
		mListViewPagerView = View.inflate(activity,
				R.layout.menunewscenteritem_listview, null);

		// 初始化控件
		mViewPager = (RoolViewPager) mViewPagerView
				.findViewById(R.id.menunewscenteritem_vp_viewpager);
		mTitle = (TextView) mViewPagerView
				.findViewById(R.id.menunewscenteritem_tv_title);
		mIndicator = (CirclePageIndicator) mViewPagerView
				.findViewById(R.id.menunewscenteritem_cpi_indicator);

		mPullListView = (PullToRefreshListView) mListViewPagerView
				.findViewById(R.id.menunewscenteritem_lv_listview);

		// 首先获取PullToRefreshListView包含的listview
		mListView = mPullListView.getRefreshableView();
		// 将viewpager的布局设置给listview的头条目
		mListView.addHeaderView(mViewPagerView);

		return mListViewPagerView;
	}

	@Override
	public void initData() {

		// 实现listview的下拉刷新和上拉加载
		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新操作，加载出新的数据给用户查看
				getData(mUrl, false);
			}
		});

		mPullListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						// 上拉加载操作,加载新的数据，在原数据的后面增加新的加载出来的数据
						getData(loadmore, true);
					}
				});

		// 设置ListView的条目点击事件，实现新闻已读未读的操作
		mPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 新闻已读未读的操作
				if (mNews.get(position - 2).isRead == false) {
					mNews.get(position - 2).isRead = true;
					listViewAdapter.notifyDataSetChanged();

					// 当新闻变成已读之后，保存新闻的标示，方便再次进入app的时候可以展示出已读新闻的样式
					// #id#id#id
					String sp_read = SharedPreferencesTool.getString(activity,
							Constants.ISREAD, "");
					SharedPreferencesTool.saveString(activity,
							Constants.ISREAD,
							sp_read + "#" + mNews.get(position - 2).id);
				}
				// 点击新闻条目，跳转到新闻详情界面，展示新闻网页
				Intent intent = new Intent(activity, NewsDetailActivity.class);
				// 如果想要将一个bean类的对象或者是list集合，需要对对象和集合进行序列化
				// 序列化的方式：
				// 对象：News implements Serializable{}
				// 集合：intent.putExtra("news", (Serializable)mNews);
				intent.putExtra("news", mNews.get(position - 2));
				activity.startActivity(intent);
			}
		});

		// 获取已读新闻的id的数据
		String sp_read = SharedPreferencesTool.getString(activity,
				Constants.ISREAD, "");
		if (!TextUtils.isEmpty(sp_read)) {
			mIds.clear();
			// #id#id#id
			String[] split = sp_read.split("#");
			for (int i = 0; i < split.length; i++) {
				mIds.add(split[i]);
			}
		}

		// 请求服务器获取页面对应的数据
		// 2.再次请求服务器，判断是否有缓存数据，有加载解析缓存
		String sp_msg = SharedPreferencesTool.getString(activity,
				NetUrl.SERVERURL + mUrl, "");
		if (!TextUtils.isEmpty(sp_msg)) {
			processJSON(sp_msg, false);
		}
		getData(mUrl, false);
	}

	/**
	 * 请求服务器获取数据的操作
	 *
	 * 2016年12月13日 下午4:22:28
	 */
	private void getData(final String url, final boolean isloadmore) {

		// 判断路径是否为空
		if (!TextUtils.isEmpty(url)) {
			OkHttpClient okHttpClient = new OkHttpClient();
			Request request = new Request.Builder().url(NetUrl.SERVERURL + url)
					.build();
			Call call = okHttpClient.newCall(request);
			call.enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, Response response)
						throws IOException {
					// 获取服务器返回的数据
					final String json = response.body().string();
					// 1.将获取的最新数据，缓存sp中
					// 因为每个数据都是不一样，如果使用同一key，会出现数据的覆盖问题，所以可以采用请求页面的路径作为key，因为路径是不一样
					SharedPreferencesTool.saveString(activity, NetUrl.SERVERURL
							+ url, json);
					// 3.解析加载最新的数据，将缓存数据覆盖
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							processJSON(json, isloadmore);
						}
					});
				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {

				}
			});
		} else {
			Toast.makeText(activity, "没有更多数据了", 0).show();
			// 停止listview的加载数据的操作
			mPullListView.onRefreshComplete();
		}

	}

	/**
	 * 解析json数据的操作
	 *
	 * 2016年12月13日 下午4:30:05
	 */
	private void processJSON(String json, boolean isloadmore) {

		System.out.println(json);
		// 1.解析json数据
		Gson gson = new Gson();
		NewBean newBean = gson.fromJson(json, NewBean.class);

		showMsg(newBean, isloadmore);
	}

	/**
	 * 展示数据 isloadmore :是否是上拉加载的操作，true:上拉加载，false:下拉刷新 2016年12月13日 下午5:16:40
	 */
	private void showMsg(NewBean newBean, boolean isloadmore) {

		// 获取加载更多的路径
		loadmore = newBean.data.more;
		// 加载更多在原数据的基础上添加新的数据，并没有更改原来的viewpager中的数据，下拉刷新是直接覆盖原来的数据，会更新viewpager的数据，所以需要判断是否是上拉加载操作，来实现是否更新viewpager数据

		if (!isloadmore) {
			// 1.ViewPager的数据
			if (newBean.data.topnews.size() > 0) {
				// 1.1.获取ViewPager的数据，展示ViewPager的数据
				imagerUrls.clear();
				titles.clear();
				for (int i = 0; i < newBean.data.topnews.size(); i++) {
					imagerUrls.add(newBean.data.topnews.get(i).topimage);
					titles.add(newBean.data.topnews.get(i).title);
				}
				// 1.2.通过viewpager展示数据
				if (myadapter == null) {
					myadapter = new Myadapter();
					mViewPager.setAdapter(myadapter);
				} else {
					myadapter.notifyDataSetChanged();
				}
				// 1.3.将viewpager和点的indicator关联
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);// 快照，使用快照的方式显示点

				// 监听viewpager的界面切换，实现切换一个界面显示一个界面对应的文本
				mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						mTitle.setText(titles.get(position));
					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int state) {
						// TODO Auto-generated method stub

					}
				});

				// 1.4.设置默认显示第一张图片，第一个文本，第一个点
				mTitle.setText(titles.get(0));
				mIndicator.onPageSelected(0);
				mViewPager.setCurrentItem(0);// 设置viewpager当前显示的界面，item：条目的索引

				// 因为showMsg方法实在processjson方法中调用的，而processJson是在缓存和获取最新数据的时候都会调用，最终会造成发送两个延迟消息，但是只需要一个延迟消息就可以了
				if (handler == null) {
					handler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							// viewpager切换下一个界面的操作
							// 首先需要知道当前显示的界面
							int currentItem = mViewPager.getCurrentItem();// 获取当前显示界面的索引
							// 然后计算下一个界面的索引
							// 判断是否切换到最后一个界面，如果是最后一个界面了，切换回第一个界面
							if (currentItem == imagerUrls.size() - 1) {
								currentItem = 0;
							} else {
								currentItem++;
							}
							// 设置viewpager显示下一个界面
							mViewPager.setCurrentItem(currentItem);
							// 切换一次完成，还要紧接着切换第二次
							handler.sendEmptyMessageDelayed(0, 3000);
						};
					};
					handler.sendEmptyMessageDelayed(0, 3000);// 只有执行此方法，才会发送延迟消息，不执行就不发送
				}

				// 1.5.将ViewPager所在的布局，添加到listView中
				// 获取listview的头条目的个数
				/*
				 * if (mListView.getHeaderViewsCount()<1) {
				 * mListView.addHeaderView(mViewPagerView);//给listview添加头条目 }
				 */
			}
		}

		// 2.ListView的数据
		if (newBean.data.news.size() > 0) {

			// 判断是否是上拉加载操作，来实现是否更新原数据的操作
			if (isloadmore) {
				mNews.addAll(newBean.data.news);
			} else {
				mNews = newBean.data.news;
			}

			// 判断要展示的数据id，是否在已读新闻id的集合，如果在，已读标示改为true
			for (NewBean.News news : mNews) {
				if (mIds.contains(news.id)) {
					news.isRead = true;
				} else {
					news.isRead = false;
				}
			}

			// 设置listview的adapter展示数据
			if (listViewAdapter == null) {
				listViewAdapter = new MyListViewAdapter();
				mListView.setAdapter(listViewAdapter);
			} else {
				listViewAdapter.notifyDataSetChanged();
			}
		}

		// 加载完数据，取消listview的刷新操作
		mPullListView.onRefreshComplete();// 取消刷新操作

	}

	/** ViewPager的adapter **/
	private class Myadapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imagerUrls.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View rootView = View.inflate(activity,
					R.layout.menunewsceteritem_viewpager_item, null);
			ImageView mIcon = (ImageView) rootView
					.findViewById(R.id.item_iv_icon);

			// 因为图片是在服务器中，不在本地，所以需要从服务器获取图片，展示在imageView
			// 通过图片的路径请求服务器中的图片，存放到相应的imageView中
			Glide.with(activity.getApplicationContext())
					.load(imagerUrls.get(position)).into(mIcon);

			// 将图片所在的布局添加到ViewPager中展示
			container.addView(rootView);

			// 设置view的触摸事件事件，实现按下viewpager停止自动滑动，抬起，viewpager重新进行自动滑动操作
			rootView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// 按下viewpager停止滑动
						handler.removeCallbacksAndMessages(null);// 取消handler发送延迟消息，如果是null，全部handler都会被取消发送消息
						break;
					case MotionEvent.ACTION_UP:
						// 抬起viewpager重新滑动
						handler.sendEmptyMessageDelayed(0, 3000);
						break;
					case MotionEvent.ACTION_CANCEL:
						// view的事件取消执行的操作
						handler.sendEmptyMessageDelayed(0, 3000);
						break;
					}

					// 如果想要事件执行，返回true,返回事件不执行
					return true;
				}
			});

			return rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/** ListView的adapter **/
	private class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mNews.size();
		}

		@Override
		public Object getItem(int position) {
			return mNews.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// 设置listview显示的条目的样式
		@Override
		public int getViewTypeCount() {
			return 2;
		}

		// 根据条目的索引，设置条目显示那种样式
		@Override
		public int getItemViewType(int position) {
			// 通过bean类中的type字段进行控制
			String type = mNews.get(position).type;
			// 根据type字段控制条目显示的样式类型
			if ("0".equals(type)) {
				// 单个图片的样式
				return 0;
			} else {
				// 多个图片的样式
				return 1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/*
			 * TextView textView = new TextView(activity);
			 * textView.setText("1111111");
			 */
			// 根据条目的索引，获取条目的显示类型
			int itemViewType = getItemViewType(position);
			if (itemViewType == 0) {
				// 单个条目的样式
				if (convertView == null) {
					convertView = View.inflate(activity,
							R.layout.menunewscenteritem_listview_item1, null);
				}

				// 初始化控件，显示数据
				ImageView mIcon = (ImageView) convertView
						.findViewById(R.id.item_iv_icon);
				TextView mTitle = (TextView) convertView
						.findViewById(R.id.item_tv_title);
				TextView mTime = (TextView) convertView
						.findViewById(R.id.item_tv_time);

				NewBean.News news = mNews.get(position);
				mTitle.setText(news.title);
				mTime.setText(news.pubdate);

				Glide.with(activity).load(news.listimage).into(mIcon);

				// 根据标示设置条目文本的颜色
				if (news.isRead) {
					mTitle.setTextColor(Color.RED);
				} else {
					mTitle.setTextColor(Color.BLACK);
				}

			} else if (itemViewType == 1) {
				// 多个条目的样式
				// 单个条目的样式
				if (convertView == null) {
					convertView = View.inflate(activity,
							R.layout.menunewscenteritem_listview_item2, null);
				}

				// 初始化控件，显示数据
				ImageView mIcon1 = (ImageView) convertView
						.findViewById(R.id.item_iv_icon1);
				ImageView mIcon2 = (ImageView) convertView
						.findViewById(R.id.item_iv_icon2);
				ImageView mIcon3 = (ImageView) convertView
						.findViewById(R.id.item_iv_icon3);
				TextView mTime = (TextView) convertView
						.findViewById(R.id.item_tv_time);

				NewBean.News news = mNews.get(position);
				mTime.setText(news.pubdate);

				Glide.with(activity).load(news.listimage).into(mIcon1);
				Glide.with(activity).load(news.listimage1).into(mIcon2);
				Glide.with(activity).load(news.listimage2).into(mIcon3);

				// 根据标示设置条目文本的颜色
				if (news.isRead) {
					mTime.setTextColor(Color.RED);
				} else {
					mTime.setTextColor(Color.BLACK);
				}

			}
			return convertView;
		}

	}

}
