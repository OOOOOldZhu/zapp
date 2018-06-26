package com.z.xwclient;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.z.xwclient.bean.NewBean;

import org.w3c.dom.Document;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private NewBean.News mNews;
    private ImageButton mSh;
    private ImageButton mShare;
    private ImageButton mTextSize;
    private LinearLayout mLLSizeOrShare;
    private ImageButton mBack;
    private ImageButton mMenu;
    private TextView mTitle;
    private WebView mWebView;
    private ProgressBar mLoding;
    private ImageButton mSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        StatusBarUtil.setColor(this, Color.RED);


        //SpeechUtility.createUtility(this, SpeechConstant.APPID +"=58523c33");
        //1.创建 SpeechSynthesizer 对象, 第二个参数： 本地合成时传 InitListener
       // mTts = SpeechSynthesizer.createSynthesizer(this, null);

        // 接受传递过来的序列化的对象
        mNews = (NewBean.News) getIntent().getSerializableExtra("news");

        initView();
    }

    /**
     * 初始化控件，显示数据
     *
     */
    private void initView() {
        mTitle = (TextView) findViewById(R.id.titlebar_tv_title);
        mMenu = (ImageButton) findViewById(R.id.titlebar_btn_menu);
        mBack = (ImageButton) findViewById(R.id.titlebar_btn_back);
        mLLSizeOrShare = (LinearLayout) findViewById(R.id.titlebar_ll_sizeorshare);
        mTextSize = (ImageButton) findViewById(R.id.titlebar_btn_textsize);
        mShare = (ImageButton) findViewById(R.id.titlebar_btn_share);
        mSh = (ImageButton) findViewById(R.id.titlebar_btn_sh);

        mWebView = (WebView) findViewById(R.id.detail_wv_webview);
        mLoding = (ProgressBar) findViewById(R.id.detail_pb_loading);
        mSpeak = (ImageButton) findViewById(R.id.detail_ib_speak);

        mTitle.setText("详情");
        mMenu.setVisibility(View.GONE);
        mBack.setVisibility(View.VISIBLE);
        mLLSizeOrShare.setVisibility(View.VISIBLE);

        mWebView.loadUrl(mNews.url);// 根据网页的地址，打开网页

        // 获取webview的设置
        settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);// 设置是否显示放大缩小网页的按钮(wap网页不支持)
        settings.setUseWideViewPort(true);// 设置是否支持双击放大(wap网页不支持)
        settings.setJavaScriptEnabled(true);// 设置是否支持android和网页中js代码的互调

        //如果想要webview支持网页中的alert，比如给weview设置WebChromeClient
        mWebView.setWebChromeClient(new WebChromeClient());

        // 加载网页的时候显示进度条
        // 监听网页的加载操作
        mWebView.setWebViewClient(new WebViewClient() {
            // 网页开始加载的时候调用的方法
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mLoding.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            // 网页加载完成的时候调用的方法
            @Override
            public void onPageFinished(WebView view, String url) {
                mLoding.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        mBack.setOnClickListener(this);

        mTextSize.setOnClickListener(this);

        mShare.setOnClickListener(this);

        mSh.setOnClickListener(this);

        mSpeak.setOnClickListener(this);

        //js调用android代码的操作
        //设置js可以调用的android的接口对象方法
        //参数1：js调用android的接口对象
        //参数2：js调用Android的接口对象的别名
        mWebView.addJavascriptInterface(new JsCallAndroid() {
            //Andorid4.2之后必须添加注解：@JavascriptInterface，提供安全性
            @Override
            @JavascriptInterface
            public void back() {
                Toast.makeText(NewsDetailActivity.this, "js调用了Android了", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, "Android");

    }

    /**
     * js调用android的接口
     *
     */
    public interface JsCallAndroid{
        public void back();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btn_back:
                finish();
                break;
            case R.id.titlebar_btn_textsize:
                // 设置字体大小
                showTextSizeDialog();
                break;
            case R.id.titlebar_btn_share:
                //share();
                break;
            case R.id.titlebar_btn_sh:
                save();
                break;
            case R.id.detail_ib_speak:
                //判断是否播放，如果播放，就停止播放，如果没有播放，播放
                /*if (mTts.isSpeaking()) {
                    mTts.stopSpeaking();
                }else{
                    speak();*/
                    //调用网页中js代码
                    mWebView.loadUrl("javascript:wave()");
               // }

                break;
        }
    }

    private String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
            "超小号字体" };

    /** 保存被选中的按钮的索引 **/
    public int textsize;
    private WebSettings settings;

    /** 保存设置好的字体大小，方便回显操作 **/
    private int currentTextSize = 2;
    //private SpeechSynthesizer mTts;

    /**
     * 显示设置字体大小的对话框
     *
     */
    private void showTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        // 设置单选按钮组
        // 参数1：单选按钮的文本数组
        // 参数2：默认选择的按钮
        builder.setSingleChoiceItems(items, currentTextSize,
                new DialogInterface.OnClickListener(){
                    // which : 被选中的按钮的索引
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textsize = which;
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 根据单选按钮的索引，设置文本的大小
                switch (textsize) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                // 保存字体大小，方便回显操作
                currentTextSize = textsize;
            }
        });
        builder.setNegativeButton("取消", null);// 如果只是取消对话框，并没有执行其他操作，可以设置为null
        builder.show();
    }

    /**
     * 分享功能
     *
     */
    /*private void share() {

        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // 分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }*/

    /**
     * 收藏新闻
     *
     */
    private void save() {
        //创建的数据库会保存在SD卡
        //将新闻相关信息保存到数据库中
        //创建数据库的配置操作
        DbUtils.DaoConfig daoConfig = new DbUtils.DaoConfig(this);
        //设置数据库的名称
        daoConfig.setDbName("zhbj");
        //创建数据库，获取到数据库的操作
        DbUtils dbUtils = DbUtils.create(daoConfig);
        //保存数据，可以直接保存bean对象，会自动识别bean类对象中保存的数据，并依次保存到数据库中
        try {
            dbUtils.save(mNews);
            Toast.makeText(this, "收藏成功", 0).show();
        } catch (DbException e) {
            //如果已经保存了，会抛出异常提醒用户，数据已经在数据库中保存，程序并不会崩溃
            //因为收藏和取消收藏点击的是一个按钮，调用的是一个方法，但是执行的是收藏和取消收藏的操作，那可以利用这个异常进行实现
            //e.printStackTrace();
            try {
                dbUtils.delete(mNews);
                Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
            } catch (DbException e1) {
                e1.printStackTrace();
            }
        }
    }



    /**
     * 阅读网页文本的操作
     *
     */
    private void speak() {
       /* //1.解析网页中的文本
        final StringBuilder sb = new StringBuilder();
        new Thread(){
            public void run() {
                try {
                    //注意:因为网页是在服务器端的，所以jsoup解析是联网解析网页操作，子线程中执行
                    //1.1.根据网页路径解析网页的内容,返回的就是解析出来的网页信息
                    //参数1：网页的路径
                    //参数2：超时时间
                    Document document = Jsoup.parse(new URL(mNews.url), 5000);
                    //1.2.获取网页中的某个标签
                    //参数：html网页中的标签的名称
                    Elements elements = document.getElementsByTag("p");

                    //1.3.解析p标签，返回所有p标签的数据，Iterator相当于集合操作，迭代器
                    Iterator<Element> iterator = elements.iterator();

                    //1.4.循环遍历Iterator，获取所有p标签的数据
                    //iterator.hasNext()：判断是否有下一个循环数据
                    while(iterator.hasNext()){
                        //获取这个元素，拿到其中的数据
                        Element element = iterator.next();
                        //获取元素中的文本数据
                        String text = element.text();
                        //将所有p标签的文本整合起来，方便阅读操作
                        //System.out.println(text);
                        sb.append(text);
                    }
                    //2.阅读操作
                    runOnUiThread(new Runnable() {
                        public void run() {
                            beginSpeak(sb.toString());
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();*/
    }


    /**
     * 阅读操作的实现
     *
     */
    private void beginSpeak(String msg) {
        /*//2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录13.2
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaomei"); //设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式， 如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成
        mTts.startSpeaking(msg, null);*/
    }


    @Override
    protected void onDestroy() {
        //判断语音是否播放，播放，停止播放
       /* if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }*/
        super.onDestroy();
    }


}
