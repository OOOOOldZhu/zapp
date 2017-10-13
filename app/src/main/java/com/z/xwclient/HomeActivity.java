package com.z.xwclient;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jaeger.library.StatusBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomeActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        StatusBarUtil.setColor(this, Color.RED);

        initSlidingMenu();
        initFragment();
    }

    /**
     * 添加SlidingMenu操作
     *
     */
    private void initSlidingMenu() {
        // 1.获取SlidingMenu的对象
        SlidingMenu slidingMenu = getSlidingMenu();
        // 2.设置显示那种样式的菜单
        slidingMenu.setMode(SlidingMenu.LEFT);
        // 3.设置侧拉菜单的触摸形式
        // TOUCHMODE_FULLSCREEN : 全屏触摸
        // TOUCHMODE_MARGIN : 边缘触摸
        // TOUCHMODE_NONE : 不能触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        // 4.设置侧拉菜单的布局文件
        setBehindContentView(R.layout.menu_layout);
        // 5.设置侧拉菜单的宽度
        // slidingMenu.setBehindWidth(300);//单位px
        // 6.设置显示内容的宽度

        //获取屏幕的宽度
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        //Width * 200 / 320
        slidingMenu.setBehindOffset(width * 200 / 320);// 单位px,代码中所有标示宽高，距离等值都是px值
        // 7.设置分割线的样式
        slidingMenu.setShadowDrawable(R.drawable.shape_home_sldingmenu_shaow);
        // 8.设置分割线的宽度
        slidingMenu.setShadowWidth(5);
		/*
		 * //9.设置第二个菜单的布局样式 slidingMenu.setSecondaryMenu(R.layout.menu_right);
		 * //10.设置第二个菜单的分割线,可以共享第一个菜单的分割线的宽度
		 * slidingMenu.setSecondaryShadowDrawable
		 * (R.drawable.shape_home_sldingmenu_shaow);
		 */
    }

    /**
     * 添加fragment操作
     *
     */
    private void initFragment() {
        // 1.获取Fragment的管理者
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        // 2.获取添加的事务
        FragmentTransaction beginTransaction = supportFragmentManager
                .beginTransaction();
        // 3.添加Fragment的操作
        // 参数1：fragment添加到容器的id
        // 参数2：添加的Fragment
        // 参数3：添加的Fragment的标示，方便后期根据标示找到添加的Fragment
       // beginTransaction.replace(R.id.home_root, new ContentFragment(),"CONTENT");
       // beginTransaction.replace(R.id.menu_root, new MenuFragment(), "MENU");
        // 4.提供事务，执行完添加操作
        beginTransaction.commit();

        // 根据标示找到添加的fragment
        // supportFragmentManager.findFragmentByTag(tag);
    }


    public void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
       // MobclickAgent.onPause(this);
    }

}
