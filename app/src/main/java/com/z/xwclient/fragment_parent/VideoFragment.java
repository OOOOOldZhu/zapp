package com.z.xwclient.fragment_parent;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.z.xwclient.R;
import com.z.xwclient.base.BasePagerFragment;
import com.z.xwclient.view.ListItemView;
import com.z.xwclient.view.MyMediaPlay;

/**
 * 视频的Fragment
 * 因为每个界面都要显示界面加载数据，相同操作抽取父类
 */
public class VideoFragment extends BasePagerFragment {

	private ListView mListView;
	
	private String url = "http://flv2.bn.netease.com/videolib3/1604/28/fVobI0704/SD/fVobI0704-mobile.mp4";
	
	/**保存上一个条目的操作**/
	private ListItemView currentView;

	@Override
	public View initView() {
		// TODO Auto-generated method stub
		return super.initView();
	}
	
	@Override
	public void initData() {
		
		//加载数据，通过父类控件展示
		/*TextView textView = new TextView(activity);
		textView.setText("视频");
		textView.setTextSize(23);
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);*/
		view = View.inflate(activity, R.layout.videofragment, null);
		mListView = (ListView) view.findViewById(R.id.videofragment_lv_listview);
		
		//设置父类内容显示区域的数据
		mContent.addView(view);
		//设置标题
		mTitle.setText("视频");
		//隐藏按钮
		mMenu.setVisibility(View.VISIBLE);
		
		setListViewAdapter();
		
		super.initData();
	}

	/**
	 * 设置listview的adapter
	 *
	 */
	private void setListViewAdapter() {
		mListView.setAdapter(new Myadapter());
	}
	
	/**ListView的adapter**/
	private class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//加载条目样式
			if (convertView == null) {
				//加载listview条目的样式
				final ListItemView itemView = new ListItemView(activity);
				//获取播放按钮，实现按钮的点击事件
				itemView.getPlayButton().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						//判断是否存在上一个条目，存放则实现删除界面停止播放操作
						if (currentView != null) {
							currentView.removeVideoView();
						}
						
						//实现播放界面的切换操作
						MyMediaPlay mediaPlay = new MyMediaPlay(activity);
						
						//调用播放操作，实现播放视频
						mediaPlay.play(url);
						
						itemView.addVideoView(mediaPlay);
						
						
						//当点击下一个条目的播放按钮的时候，需要将之前条目的播放界面删除，播放操作停止，同时设置下一个条目进行播放操作
						//问题:因为Listview的条目都会调用getview方法，如何在getview中区别是上一个条目还是当前条目
						currentView = itemView;
						
					}
				});
				convertView = itemView;
			}else{
				//判断之前的播放界面是否被复用缓存使用
				if (currentView != null && convertView == currentView) {
					currentView.removeVideoView();
				}
			}
			return convertView;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
