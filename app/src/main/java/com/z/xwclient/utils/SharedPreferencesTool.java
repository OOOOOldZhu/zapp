package com.z.xwclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences的工具类
 * 
 */
public class SharedPreferencesTool {

	private static SharedPreferences sp;

	/**
	 * 保存boolean值信息
	 *
	 */
	public static void saveBoolean(Context context,String key,boolean value){
		if (sp==null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
		
	}
	
	/**
	 * 获取boolean值信息
	 *
	 */
	public static boolean getBoolean(Context context,String key,boolean defvalue){
		if (sp==null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defvalue);
	}
	
	
	/**
	 * 保存String值信息
	 *
	 */
	public static void saveString(Context context,String key,String value){
		if (sp==null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	
	/**
	 * 获取String值信息
	 *
	 */
	public static String getString(Context context,String key,String defvalue){
		if (sp==null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sp.getString(key, defvalue);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
