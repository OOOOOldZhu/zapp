package com.z.xwclient.bean;

import java.util.List;

/**
 * 新闻中心信息的bean类
 * 
 * 必须跟json串格式字段保持一致
 * { ： jsonObject,相当于一个bean类
 * [] : 数组或者是集合
 * 单个字段:bean类中保存信息的变量
 * 
 */
public class NewsCenterInfo {


	public List<NewsCenterDataInfo> data;
	public List<String> extend;
	public String retcode;


	public class NewsCenterDataInfo{
		public List<child> children;
		public String id;
		public String title;
		public String type;
		
		public String url;
		public String url1;
		
		public String dayurl;
		public String excurl;
		public String weekurl;
	}
	
	public class child{
		public String id;
		public String title;
		public String type;
		
		public String url;
	}
	
}
