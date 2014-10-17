package com.WhiteBoard.WhiteBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Adapter;

public class WelcomeActivity extends Activity {

	private ViewPager viewPager;
	private MyAdapter viewAdapter;
	private ScrollNotifier notifier;
	private List<View> list =null;
	private LayoutInflater inflater = null;
	private View view1,view2,view3,view4,view5,view6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function);
		viewPager = (ViewPager)this.findViewById(R.id.viewpager);
		viewAdapter = new MyAdapter();
		notifier = new ScrollNotifier();
		//加载布局
		inflater = LayoutInflater.from(WelcomeActivity.this);
		//第一次被加载的对象!!!!!!!
		view1 = inflater.inflate(R.layout.activity_main, null);
		view2 = inflater.inflate(R.layout.activity_main2, null);
		view3 = inflater.inflate(R.layout.activity_main3, null);
		view4 = inflater.inflate(R.layout.activity_main4, null);
		//view5 = inflater.inflate(R.layout.activity_main5, null);
		//view6 = inflater.inflate(R.layout.activity_main6, null);
		list = new ArrayList<View>();
		list.add(view1);
		list.add(view2);
		list.add(view3);
		list.add(view4);
	    //list.add(view5);
	    //list.add(view6);
		//初始化界面
		viewPager.setAdapter(viewAdapter);
		viewPager.setOnPageChangeListener(notifier);
		
		}
	
	public class MyAdapter extends PagerAdapter{
		
		private View mCurrentView;

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//初始化加载
			((ViewPager)container).addView(list.get(position));
			return list.get(position);
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//super.destroyItem(container, position, object);
			((ViewPager)container).removeView(list.get(position));
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			mCurrentView = (View)object;
		}
		
		public View getPrimaryItem() {
			return mCurrentView;
		}
		
	}
	
	public class ScrollNotifier implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			Log.d("WCM","state = " + arg0);
			if (viewAdapter.getPrimaryItem().equals((View)view4)) {
				//Log.d("WCM","" + viewAdapter.getPrimaryItem().equals((View)view5));
				finish();
			}
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageSelected(int arg0) {}

		
	}

}
