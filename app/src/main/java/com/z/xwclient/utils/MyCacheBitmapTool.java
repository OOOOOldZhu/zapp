package com.z.xwclient.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**
 * 从内存获取图片
 * 
 */
public class MyCacheBitmapTool {

	
	
	private LruCache<String, Bitmap> lrucache;

	//private HashMap<String, SoftReference<Bitmap>> map;

	/**
	 * 1.软引用
	 *   强引用：new TextView(this)，在内存不会被系统及时回收
	 *   软引用:SoftReference<Bitmap>，当内存不足的时候，系统会及时回收
	 *   弱引用：WeakReference<Bitmap>,当内存不足的时候，系统会及时回收，但是如果软引用和弱引用同时存在，先回收的时候弱引用
	 *   虚引用：PhantomReference<Bitmap>,级别最低，当内存不足的时候，系统会及时回收，最先被回收的
	 * 2.LruCache,通过图片的使用次数和使用时间判定图片是否需要从内存中清除的
	 */
	
	
	public MyCacheBitmapTool(){
		//map = new HashMap<String, SoftReference<Bitmap>>();
		//maxSize : 设置缓存空间的大小，一般都是总内存的八分之一
		int maxSize = (int) (Runtime.getRuntime().totalMemory()/8);
		lrucache = new LruCache<String, Bitmap>(maxSize){
			//获取缓存的图片的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				//getRowBytes() : 获取图片一行，占用的字节数
				return value.getRowBytes() * value.getHeight();
			}
		};
	}
	
	
	/**
	 * 保存图片
	 *
	 */
	public void saveBitmap(String url,Bitmap bitmap){
		/*SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
		map.put(url, softReference);*/
		
		lrucache.put(url, bitmap);
	}
	
	/**
	 * 获取图片
	 *
	 */
	public Bitmap getBitmap(String url){
		
		System.out.println("从内存获取图片");
		/*SoftReference<Bitmap> softReference = map.get(url);
		if (softReference != null) {
			Bitmap bitmap = softReference.get();
			return bitmap;
		}*/
		
		Bitmap bitmap = lrucache.get(url);
		
		return bitmap;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
