package com.z.xwclient;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.jaeger.library.StatusBarUtil;
import com.z.xwclient.view.MyGridLayout;
import java.util.ArrayList;
import java.util.List;

public class SelectItemActivity extends AppCompatActivity {

    private ImageView back;
    private List<String> titles1;

    private String[] TITLES = new String[]{"娱乐","服饰","音乐","视频","段子","搞笑","科学","房产","名站"};

    private List<String> titles2;

    private MyGridLayout mGrid2;

    private MyGridLayout mGrid1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_item);
        StatusBarUtil.setColor(this, Color.WHITE);

        getValue();
        initView();

    }
    /**
     * 获取拖拽界面所需的数据
     */
    private void getValue() {
        titles1 = (List<String>) getIntent().getSerializableExtra("titles");
        //获取底部所需数据
        titles2 = new ArrayList<>();
        titles2.clear();
        for (int i = 0; i < TITLES.length; i++) {
            titles2.add(TITLES[i]);
        }
    }
    /**
     * 初始化控件
     *
     */
    private void initView() {
        back = (ImageView)findViewById(R.id.select_activity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mGrid1 = (MyGridLayout) findViewById(R.id.selectitem_mgl_gridlayout1);
        mGrid2 = (MyGridLayout) findViewById(R.id.selectitem_mgl_gridlayout2);

        //调用设置是否可以拖拽的方法，设置两个GridLayout中的textview是否可以进行拖拽的效果
        mGrid1.isDrag(true);
        mGrid2.isDrag(false);

        //将数据传递给gridlayout进行显示
        mGrid1.setItemDatas(titles1);
        mGrid2.setItemDatas(titles2);

        //调用回调，获取被点击textview,实现数据的操作
        mGrid1.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {

            @Override
            public void itemClick(TextView view) {
                //mGrid1删除点击的textview
                //mGrid2添加点击的textview
                mGrid1.removeView(view);
                mGrid2.addTextView(view.getText().toString());
            }
        });
        mGrid2.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {

            @Override
            public void itemClick(TextView view) {
                //mGrid2删除点击的textview
                //mGrid1添加点击的textview
                mGrid2.removeView(view);
                mGrid1.addTextView(view.getText().toString());
            }
        });
    }
    /**
     * 返回按钮的点击事件
     *
     */
    public void back(View view){
        finish();
    }
}
