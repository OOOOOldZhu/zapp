package com.z.xwclient;

//import cn.jpush.android.api.JPushInterface;
import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 极光推送的初始化操作
		//JPushInterface.setDebugMode(true);
		//JPushInterface.init(this);
	}
	
}
