package com.z.xwclient.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 从网络获取图片的操作
 * 
 */
public class MyNetBitmapTool {

	private MyCacheBitmapTool mCacheBitmapTool;
	private MyLocalBitMapTool mLocalBitMapTool;
	public MyNetBitmapTool(MyCacheBitmapTool cacheBitmapTool,MyLocalBitMapTool localBitMapTool){
		this.mCacheBitmapTool = cacheBitmapTool;
		this.mLocalBitMapTool = localBitMapTool;
	}
	
	/**
	 * 请求网络获取图片
	 *
	 */
	public void getBitmap(ImageView imageView,String url){
		//参数：在子线程中（doInBackground中）执行的操作所需的参数
		new MyAsyncTask().execute(imageView,url);
	}
	//类型1：子线程执行所需的参数
	//类型2：更新进入的参数
	//类型3：子线程的返回值
	private class MyAsyncTask extends AsyncTask<Object, Integer, Bitmap>{

		private ImageView iamgeview;
		private String url;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected Bitmap doInBackground(Object... params) {
			
			//以为图片有大有小，如果同时下载两张图片，先下载完第二张图片，这个时候第二张图片就有可能会赋给第一个imageview展示，造成图片错乱
			iamgeview = (ImageView) params[0];
			url = (String) params[1];
			
			//将ImageView和imageView对应的url地址绑定，这样获取到图片之后，就可以根据url去判断到底应用将图片设置给那个imageView
			iamgeview.setTag(url);
			
			//获取图片
			Bitmap bitmap = dowloadImage(url);
		
			
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			System.out.println("从网络获取图片");
			//判断图片要设置给那个iamgeView，
			//首先获取跟imageView绑定的url地址
			String mUrl = (String) iamgeview.getTag();
			if (mUrl.equals(url)) {
				if (result!=null) {
					iamgeview.setImageBitmap(result);
					
					//将图片保存到本地和缓存到内存中
					mLocalBitMapTool.saveBitMap(url, result);
					mCacheBitmapTool.saveBitmap(url, result);
				}
			}
			
			super.onPostExecute(result);
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}
	/**
	 * 请求服务器，下载图片
	 *
	 */
	public Bitmap dowloadImage(String url) {
		try {
			URL uri = new URL(url);
			HttpURLConnection openConnection = (HttpURLConnection) uri.openConnection();
			openConnection.setConnectTimeout(5000);//链接超时时间
			openConnection.setReadTimeout(5000);//读取超时时间
			openConnection.connect();//链接服务器
			int responseCode = openConnection.getResponseCode();//获取响应码
			if (responseCode == 200) {
				InputStream stream = openConnection.getInputStream();
				//将流转化成bitmap
				Bitmap bitmap = BitmapFactory.decodeStream(stream);
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
