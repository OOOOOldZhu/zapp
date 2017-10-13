package com.z.xwclient.fragment_grandfather;

import org.greenrobot.eventbus.EventBus;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.z.xwclient.R;
import com.z.xwclient.base.BaseFragment;
import com.z.xwclient.bean.EventBusInfo;

//1.因为菜单页和内容页都要显示界面加载数据，相同操作，抽取到父类
public class MenuFragment extends BaseFragment {

	private ListView mListView;
	/**条目文本数组**/
	private String[] TITLES = new String[] { "新闻", "专题", "组图", "互动" };
	private Myadapter myadapter;
	
	/**保存被点击的条目的索引**/
	private int currentPosition;

	//3.子类继承父类，实现父类的抽象方法，根据自己的特性进行相应的实现
	
	@Override
	public View initView() {
		/*TextView textView = new TextView(activity);
		textView.setText("菜单页的fragment");*/
		view = View.inflate(activity, R.layout.menufragment, null);
		return view;
	}

	@Override
	public void initData() {
		//初始化listview
		mListView = (ListView) view.findViewById(R.id.menu_lv_listview);
		
		//设置默认被点击的是0个条目，这样在第一次加载数据的时候，就可以在adapter的getview中进行判断设置样式了
		currentPosition = 0;
		
		//设置listview的adapter显示数据
		if (myadapter ==null) {
			myadapter = new Myadapter();
			mListView.setAdapter(myadapter);
		}else{
			myadapter.notifyDataSetChanged();
		}
		
		//设置listview的条目点击事件，实现更改条目样式的操作
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//更改条目的样式
				//将被点击的条目的索引，保存
				currentPosition = position;
				//更新界面，会调用getcount和getview
				myadapter.notifyDataSetChanged();
				//点击条目，除了更改条目样式，还要关闭侧拉菜单
				//如果侧拉菜单是关闭，执行打开，如果是打开，执行关闭
				slidingMenu.toggle();
				
				//发送切换界面的消息
				EventBus.getDefault().post(new EventBusInfo(position));
			}
		});
		
	}
	
	
	/**listview的adapter**/
	private class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Object getItem(int position) {
			return TITLES[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = View.inflate(activity, R.layout.menufragment_listview_item, null);
			
			//初始化控件，显示数据
			ImageView mIcon = (ImageView) itemView.findViewById(R.id.item_icon);
			TextView mTitle = (TextView) itemView.findViewById(R.id.item_title);
			//根据条目的索引获取文本展示
			mTitle.setText(TITLES[position]);
			
			//根据保存的被点击条目的索引实现条目样式的更改
			if (currentPosition == position) {
				mIcon.setImageResource(R.drawable.menu_arr_select);
				mTitle.setTextColor(Color.RED);
			}else{
				mIcon.setImageResource(R.drawable.menu_arr_normal);
				mTitle.setTextColor(Color.WHITE);
			}
			
			return itemView;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
