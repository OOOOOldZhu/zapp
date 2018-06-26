package com.z.xwclient.bean;

import java.io.Serializable;
import java.util.List;

public class NewBean {
	public NewItem data;
	public String retcode;
	
	public class NewItem{
		public String countcommenturl;
		public String more;
		public String title;
		public List<News> news;
		public List<TopPic> topic;
		public List<TopNews> topnews;
	}
	public static class News implements Serializable{
		
		public boolean comment;
		public String commentlist;
		public String commenturl;
		//����Ψһ�Ա�ʾ,��¼�ڱ��أ��ж������Ѷ�������
		public String id;
		//ͼƬurl
		public String listimage;
		//ͼƬurl1
		public String listimage1;
		//ͼƬurl2
		public String listimage2;
		
		//ʱ��
		public String pubdate;
		//������������
		public String title;
		public String type;
		
		//url��������ҳ�����ַ  html------>WebView(����HTML��ҳ)
		public String url;
		
		//��ʾ��ǰ��Ŀ�����Ƿ��Ѷ�����
		public boolean isRead;
	}
	public class TopPic{
		public String description;
		public int id;
		public String listimage;
		public String sort;
		public String title;
		public String url;
	}
	public class TopNews{
		public boolean comment;
		public String commentlist;
		public String commenturl;
		public int id;
		public String pubdate;
		//����
		public String title;
		//ͼƬ
		public String topimage;
		public String type;
		//����ҳurl��ַ
		public String url;
	}
	
}
