package com.z.xwclient.utils;

import com.z.xwclient.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 图片加载的工具类
 * 
 * 2016年12月17日   下午3:56:09
 */
public class MyBitMapTool {

	private MyCacheBitmapTool cacheBitmapTool;
	private MyLocalBitMapTool localBitMapTool;
	private MyNetBitmapTool myNetBitmapTool;

	public MyBitMapTool(){
		cacheBitmapTool = new MyCacheBitmapTool();
		localBitMapTool = new MyLocalBitMapTool();
		myNetBitmapTool = new MyNetBitmapTool(cacheBitmapTool, localBitMapTool);
	}
	
	/**
	 * 根据图片的路径，加载图片给ImageView展示
	 *
	 * 2016年12月17日   下午3:56:29
	 */
	public void getBitmap(ImageView imageView,String imageurl){
		//首先，设置imageView默认展示图片
		imageView.setImageResource(R.drawable.pic_item_list_default);
		//1.从内存中获取图片
		Bitmap bitmap = cacheBitmapTool.getBitmap(imageurl);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			//如果内存中已经获取到图片，就不需要再从本地文件中获取图片，所以直接结束方法即可
			return;
		}
		//2.内存中没有，从本地文件获取图片，展示，并缓存到内存中
		bitmap = localBitMapTool.getBitmap(imageurl);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			//重新将获取的图片缓存到内存中
			cacheBitmapTool.saveBitmap(imageurl, bitmap);
			return;
		}
		//3.本地没有获取到图片，从网络下载图片，展示，并保存到本地和缓存到内存中
		myNetBitmapTool.getBitmap(imageView, imageurl);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
