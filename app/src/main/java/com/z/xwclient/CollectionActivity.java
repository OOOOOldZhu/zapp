package com.z.xwclient;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.z.xwclient.bean.NewBean;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private ListView mListView;
    private ImageButton mBack;
    private ImageButton mMenu;
    private TextView mTitle;
    private List<NewBean.News> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collection);
        StatusBarUtil.setColor(this, Color.RED);

        initView();
    }

    /*
     * 初始化控件
     */

    private void initView() {
        mTitle = (TextView) findViewById(R.id.titlebar_tv_title);
        mMenu = (ImageButton) findViewById(R.id.titlebar_btn_menu);
        mBack = (ImageButton) findViewById(R.id.titlebar_btn_back);
        mListView = (ListView) findViewById(R.id.collection_lv_listview);

        mTitle.setText("收藏");
        mMenu.setVisibility(View.GONE);
        mBack.setVisibility(View.VISIBLE);

        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置ListView的条目点击事件，实现跳转到新闻详情页面，展示收藏的新闻
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //跳转到新闻详情页面
                Intent intent = new Intent(CollectionActivity.this,NewsDetailActivity.class);
                //因为要在新闻详情页面展示新闻的信息，所以需要将新闻的信息传递到新闻详情界面
                intent.putExtra("news", list.get(position));
                //startActivity(intent);
                startActivityForResult(intent, 0);
            }
        });


        //获取保存在数据库中的数据，并展示
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //重新刷新数据更新列表
        initData();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * 加载数据展示数据
     */

    private void initData() {
        DbUtils.DaoConfig daoConfig = new DbUtils.DaoConfig(this);
        daoConfig.setDbName("zhbj97");
        //如果数据库存在，直接使用，需要注意数据库的配置一定要一致
        DbUtils dbUtils = DbUtils.create(daoConfig);
        try {
            //从数据库中获取数据
            //参数：获取什么类型的数据，是类型.class
            list = dbUtils.findAll(NewBean.News.class);
            //通过listview展示数据
            if (list != null && list.size() > 0) {
                mListView.setAdapter(new MyListViewAdapter());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /* ListView的adapter */
    private class MyListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // 设置listview显示的条目的样式
        @Override
        public int getViewTypeCount() {
            return 2;
        }

        // 根据条目的索引，设置条目显示那种样式
        @Override
        public int getItemViewType(int position) {
            // 通过bean类中的type字段进行控制
            String type = list.get(position).type;
            // 根据type字段控制条目显示的样式类型
            if ("0".equals(type)) {
                // 单个图片的样式
                return 0;
            } else {
                // 多个图片的样式
                return 1;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

			 // TextView textView = new TextView(activity);
			 // textView.setText("1111111");

            // 根据条目的索引，获取条目的显示类型
            int itemViewType = getItemViewType(position);
            if (itemViewType == 0) {
                // 单个条目的样式
                if (convertView == null) {
                    convertView = View.inflate(CollectionActivity.this,
                            R.layout.menunewscenteritem_listview_item1, null);
                }

                // 初始化控件，显示数据
                ImageView mIcon = (ImageView) convertView
                        .findViewById(R.id.item_iv_icon);
                TextView mTitle = (TextView) convertView
                        .findViewById(R.id.item_tv_title);
                TextView mTime = (TextView) convertView
                        .findViewById(R.id.item_tv_time);

                NewBean.News news = list.get(position);
                mTitle.setText(news.title);
                mTime.setText(news.pubdate);

                Glide.with(CollectionActivity.this).load(news.listimage).into(mIcon);


            } else if (itemViewType == 1) {
                // 多个条目的样式
                // 单个条目的样式
                if (convertView == null) {
                    convertView = View.inflate(CollectionActivity.this,
                            R.layout.menunewscenteritem_listview_item2, null);
                }

                // 初始化控件，显示数据
                ImageView mIcon1 = (ImageView) convertView
                        .findViewById(R.id.item_iv_icon1);
                ImageView mIcon2 = (ImageView) convertView
                        .findViewById(R.id.item_iv_icon2);
                ImageView mIcon3 = (ImageView) convertView
                        .findViewById(R.id.item_iv_icon3);
                TextView mTime = (TextView) convertView
                        .findViewById(R.id.item_tv_time);

                NewBean.News news = list.get(position);
                mTime.setText(news.pubdate);

                Glide.with(CollectionActivity.this).load(news.listimage).into(mIcon1);
                Glide.with(CollectionActivity.this).load(news.listimage1).into(mIcon2);
                Glide.with(CollectionActivity.this).load(news.listimage2).into(mIcon3);


            }
            return convertView;
        }

    }
}
