package com.z.xwclient.pager;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.z.xwclient.R;
import com.z.xwclient.base.BaseMenuPager;
import com.z.xwclient.bean.NetUrl;
import com.z.xwclient.bean.PhotosBean;
import com.z.xwclient.utils.Constants;
import com.z.xwclient.utils.MyBitMapTool;
import com.z.xwclient.utils.SharedPreferencesTool;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 菜单页新闻中心条目对象的界面,组图界面
 * 
 * 不能使用fragment，因为Fragment嵌套太多，容易内存溢出
 * 不能使用Activity，因为Activity不能添加到Fragment中、
 * 可以使用一个普通java,因为显示界面的最终操作，起始就是View.inflate加载一个布局的view对象，展示布局的view对象就可以了
 * 
 * 因为每个界面都可以显示界面加载数据，相同操作，抽取到父类
 * 
 * 2016年12月13日   上午10:25:34
 */
public class MenuPhotosPager extends BaseMenuPager implements OnClickListener{

	private ListView mListView;
	private GridView mGridView;
	private List<PhotosBean.PhotosItem> list;
	private ImageButton mPhoto;
	
	public MenuPhotosPager(Activity activity,ImageButton photo) {
		super(activity);
		this.mPhoto = photo;
		mPhoto.setOnClickListener(this);
	}

	@Override
	public View initView() {
		/*TextView textView = new TextView(activity);
		textView.setText("菜单详情页-组图");
		textView.setTextColor(Color.RED);
		textView.setTextSize(22);
		textView.setGravity(Gravity.CENTER);*/
		view = View.inflate(activity, R.layout.menuphotospager, null);
		mListView = (ListView) view.findViewById(R.id.menuphotospager_lv_listview);
		mGridView = (GridView) view.findViewById(R.id.menuphotospager_gv_gridview);
		return view;
	}

	@Override
	public void initData() {
		
		//2.再次请求服务器，先判断是否有缓存，有加载显示
		String sp_json = SharedPreferencesTool.getString(activity, Constants.PHOTOS, "");
		if (!TextUtils.isEmpty(sp_json)) {
			//加载解析缓存数据
			processJson(sp_json);
		}
		//请求服务器获取组图数据
		getData();
	}

	/**
	 * 请求服务器获取数据
	 *
	 * 2016年12月17日   下午2:52:16
	 */
	private void getData() {
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url(NetUrl.PHOTOS).build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				//请求成功，获取服务器的数据
				final String json = response.body().string();
				//1.请求数据成功，将数据缓存到本地（sp）
				SharedPreferencesTool.saveString(activity, Constants.PHOTOS, json);
				//3.再次请求服务器，获取到最新的数据，覆盖缓存数据展示
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						processJson(json);
					}
				});
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	/**
	 * 解析json数据
	 *
	 * 2016年12月17日   下午2:56:34
	 */
	private void processJson(String json) {
		//解析json数据操作
		Gson gson = new Gson();
		PhotosBean photosBean = gson.fromJson(json, PhotosBean.class);
		
		//获取数据
		list = photosBean.data.news;
		
		//展示数据
		mListView.setAdapter(new Myadapter());
		//因为listview和gridview都是继承absListView的，所以是可以共用一个adapter
		mGridView.setAdapter(new Myadapter());
	}
	
	private class Myadapter extends BaseAdapter{

		private MyBitMapTool bitMapTool;

		public Myadapter() {
			bitMapTool = new MyBitMapTool();
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rootView;
			ViewHolder viewHolder;
			if (convertView == null) {
				rootView = View.inflate(activity, R.layout.menuphotos_item, null);
				viewHolder = new ViewHolder();
				viewHolder.mIcon = (ImageView) rootView.findViewById(R.id.item_iv_icon);
				viewHolder.mTitle = (TextView) rootView.findViewById(R.id.item_tv_title);
				rootView.setTag(viewHolder);
			}else{
				rootView = convertView;
				viewHolder = (ViewHolder) rootView.getTag();
			}
			//获取数据展示数据
			PhotosBean.PhotosItem photosItem = list.get(position);
			viewHolder.mTitle.setText(photosItem.title);
			
			//获取图片
			bitMapTool.getBitmap(viewHolder.mIcon, photosItem.listimage);
			return rootView;
		}
		
	}
	
	static class ViewHolder{
		ImageView mIcon;
		TextView mTitle;
	}

	private boolean isShowListView = true;
	@Override
	public void onClick(View v) {
		//切换listview和gridview的隐藏和显示
		//如果listview显示，点击隐藏listview展示GridView,同时更改组图按钮图片
		//如果GridView显示，点击隐藏GridView展示ListView,同时更改组图按钮的图片
		if (isShowListView) {
			mListView.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
			mPhoto.setBackgroundResource(R.drawable.icon_pic_grid_type);
			isShowListView = false;
		}else{
			mGridView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mPhoto.setBackgroundResource(R.drawable.icon_pic_list_type);
			isShowListView = true;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
