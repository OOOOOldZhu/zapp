package com.z.xwclient.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 本地缓存图片的操作
 * 
 */
public class MyLocalBitMapTool {

	/**缓存图片保存的目录**/
	private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj97_cache";
	
	/**
	 * 保存图片到本地
	 *
	 */
	public void saveBitMap(String url,Bitmap bitmap){
		File dir = new File(PATH);
		//判断目录是否存在，如果不存在创建
		if (!dir.exists() && !dir.isDirectory()) {
			dir.mkdirs();
		}
		
		try {
			File file = new File(dir, MD5Util.Md5(url).substring(0, 10));
			FileOutputStream stream = new FileOutputStream(file);
			//设置图片的类型及质量并保存到本地
			//参数1：图片的格式，类型
			//参数2：图片的质量
			//参数3：写入流
			bitmap.compress(CompressFormat.JPEG, 100, stream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取本地缓存的图片
	 *
	 */
	public Bitmap getBitmap(String url){
		
		System.out.println("从本地获取图片");
		//如果本地文件中没有这样一个目录，会抛出异常
		try{
			File file = new File(PATH, MD5Util.Md5(url).substring(0, 10));
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			return bitmap;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
