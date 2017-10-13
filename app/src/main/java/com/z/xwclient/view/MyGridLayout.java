package com.z.xwclient.view;

import java.util.List;


import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.z.xwclient.R;

public class MyGridLayout extends GridLayout {

	public MyGridLayout(Context context) {
		//super(context);
		this(context,null);
	}

	public MyGridLayout(Context context, AttributeSet attrs) {
		//super(context, attrs);
		this(context,attrs,-1);
	}

	public MyGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		//设置平移的过渡动画效果
		this.setLayoutTransition(new LayoutTransition());
	}
	
	/**
	 * 提供给activity调用的，负责将activity中的数据，传递给Gridlayout进行展示操作
	 */
	public void setItemDatas(List<String> datas){
		//因为每个集合中的元素都对应有一个textview来进行展示，所以需要根据集合的长度创建相应个数的textview来展示数据
		for (String string : datas) {
			addTextView(string);
		}
	}

	/**
	 * 根据集合的个数创建相应的textview，并添加到gridlayout中展示
	 */
	public void addTextView(String string) {
		final TextView textView = new TextView(getContext());
		textView.setText(string);
		textView.setTextColor(Color.BLACK);
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundResource(R.drawable.select_gridlayout_textview_bg);
		textView.setPadding(15, 5, 15, 5);//设置textview中文本距离textview内边框的上下左右的距离
		
		LayoutParams layoutParams = new LayoutParams();
		layoutParams.width = LayoutParams.WRAP_CONTENT;
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		layoutParams.setMargins(5, 5, 5, 5);//设置textview距离父控件的上下左右的位置
		
		//将layoutparams设置给TextView，使距离父控件上下左右的距离的操作生效
		textView.setLayoutParams(layoutParams);
		
		
		//设置TextView的点击事件，实现两个Gridlayout数据的添加和删除操作
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//因为实在自定义Gridlayout中实现的textview的点击事件，但是数据的操作是跟两个Gridlayout有关系的，只有activity中有两个GridLayout
				//因为点击textView之后，两个GridLayout就要实现添加和删除点击的textview的操作，但是两个GridLayout实在activity中的，所以需要将被点击的TextView传递到activity才能实现两个Gridlayout之间的数据操作
				if (listener != null) {
					listener.itemClick(textView);
				}
			}
		});
		
		//设置TextView的长按事件，实现拖拽效果
		//根据是否可以拖拽的标示设置textview是否能够进行拖拽操作
		if (isDrag) {
			textView.setOnLongClickListener(longClickListener);
		}else{
			textView.setOnLongClickListener(null);
		}
		
		//监听GridLayout的拖拽监听，实现条目位置改变的操作
		this.setOnDragListener(dragListener);
		
		//将textview设置给GridLayout进行显示操作
		this.addView(textView);
	}
	
	/**
	 * GridLayout的拖拽的监听
	 */
	private OnDragListener dragListener = new OnDragListener() {
		
		//当Gridlayout有拖拽的操作的时候，调用此方法
		//参数1：拖拽监听设置的控件
		//参数2：拖拽的事件
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED://开始拖拽了
				System.out.println("拖拽开始");
				//以TextView为蓝本创建矩形
				createRect();
				break;
			case DragEvent.ACTION_DRAG_ENTERED://控件开始拖拽后，进入控件拖拽范围的操作
				System.out.println("进入拖拽范围");
				break;
			case DragEvent.ACTION_DRAG_EXITED://控件开始拖拽后，退出控件拖拽范围的操作
				System.out.println("退出拖拽范围");
				break;
			case DragEvent.ACTION_DRAG_LOCATION://控件开始拖拽后，在拖拽范围内移动的操作
				System.out.println("拖拽控件移动");
				//根据移动的坐标，去判断移动的坐标是否已经完全移动相应的textview上了
				int index = getIndex(event);
				//判断如果有移动的位置，并且移动的textview不等于空，并且移动的textview不在原来的 位置，就可以进行移动操作
				if (index > -1 && currentView != null && currentView != MyGridLayout.this.getChildAt(index)) {
					MyGridLayout.this.removeView(currentView);
					//将view对象，添加到那个位置
					//index : view对象添加显示的位置
					MyGridLayout.this.addView(currentView, index);
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED://结束拖拽
				System.out.println("拖拽结束");
				//当textview拖拽结束，设置textview重新可以使用
				if (currentView != null) {
					currentView.setEnabled(true);
				}
				break;
			case DragEvent.ACTION_DROP://结束拖拽，手指松开（在拖拽范围内执行的操作）
				System.out.println("拖拽结束，手指松开");
				break;
			}
			//返回true:拖拽的操作可以实现，返回false:不能实现
			return true;
		}
	};
	
	/**
	 * 判断移动的坐标，是否在textview相对应的矩形的中
	 * 返回矩形所代表的textview的位置
	 */
	protected int getIndex(DragEvent event) {
		for (int i = 0; i < rects.length; i++) {
			//判断移动的坐标是否在textview对应的矩形中
			if (rects[i].contains((int)event.getX(), (int)event.getY())) {
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * 创建矩形
	 */
	protected void createRect() {
		//创建包含左上角和右下角的坐标的矩形的数组
		//this.getChildCount() : 获取自定义控件的孩子的个数
		rects = new Rect[this.getChildCount()];
		for (int i = 0; i < this.getChildCount(); i++) {
			//根据索引获取子控件的对象
			View view = this.getChildAt(i);
			//矩形的坐标是根据实际的textview的坐标来定的
			//参数1，2：左上角的x和y的坐标
			//参数3,4:右下角的x和y的坐标
			Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
			
			rects[i] = rect;
		}
	}

	/**设置保存拖拽的textview**/
	private View currentView;
	
	/**
	 * textview的长按监听
	 */
	private OnLongClickListener longClickListener = new OnLongClickListener() {
		
		//v：被长按的控件的view对象
		@Override
		public boolean onLongClick(View v) {
			
			//实现textview的拖拽的效果
			//开始拖拽
			//参数1：拖拽所需的数据，一般null
			//参数2：拖拽的阴影的效果,DragShadowBuilder(v):根据实际的控件的效果实现阴影效果
			//参数3：设置拖拽的控件的状态，一般null
			//参数4：设置拖拽的控件的执行的事件，一般0
			v.startDrag(null, new DragShadowBuilder(v), null, 0);
			
			//设置textview在拖拽的时候不可用
			v.setEnabled(false);
			
			//当textView拖拽的时候，保存TextView方便在Gridlayout改为条目位置的进行背景，控制的位置的设置
			currentView = v;
			
			//返回true执行长按事件，返回false：不执行
			return true;
		}
	};
	
	private boolean isDrag;
	/**
	 * 提供给activity调用的，方便在activity中设置两个Gridlayout中的textview是否可以进行拖拽效果
	 */
	public void isDrag(boolean isDrag){
		this.isDrag = isDrag;
		//根据是否可以拖拽的标示，设置两个GridLayout是否可以进行条目位置移动的操作
		if (isDrag) {
			this.setOnDragListener(dragListener);
		}else{
			this.setOnDragListener(null);
		}
	}

	/**保存activity传递过来的接口实现对象**/
	private OnItemClickListener listener;

	private Rect[] rects;
	/**
	 * 提供给activity调用，负责activity将接口的实现对象传递到自定义控件中
	 */
	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener = listener;
	}
	
	public interface OnItemClickListener{
		/**
		 * 条目点击的回调方法
		 * TextView ： 被点击的textview
		 */
		public void itemClick(TextView view);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
