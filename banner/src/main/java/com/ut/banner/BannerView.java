package com.ut.banner;

import java.util.List;

import com.ut.banner.adapter.CBPageAdapter;
import com.ut.banner.adapter.CBPageAdapter.Holder;
import com.ut.banner.pager.AutoScrollViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class BannerView<T> extends LinearLayout {

	private AutoScrollViewPager viewPager;
	private LinearLayout ll_dots;

	CBPageAdapter<T> adapter;
	private List<T> mDatas;

	public BannerView(Context context) {
		super(context);
		init();
	}

	public BannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		View.inflate(getContext(), R.layout.layout_slideshow, this);
		viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
		ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
	}

	public void setInterval(int interval) {
		viewPager.setInterval(interval);
	}

	public void startAutoScroll() {
		viewPager.startAutoScroll();
	}

	public void stopAutoScroll() {
		viewPager.stopAutoScroll();
	}

	// 触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_OUTSIDE) {
			// 开始翻页
			viewPager.startAutoScroll();
		} else if (action == MotionEvent.ACTION_DOWN) {
			// 停止翻页
			viewPager.stopAutoScroll();
		}
		return super.dispatchTouchEvent(ev);
	}

	public BannerView<T> setPages(CBViewHolderCreator<Holder<T>> holderCreator, List<T> datas) {
		this.mDatas = datas;
		adapter = new CBPageAdapter<T>(holderCreator, datas);
		adapter.setDataChangeListener(new CBPageAdapter.DataChangeListener() {

			@Override
			public void onDataChanged() {
				viewPager.setCurrentItem(CBPageAdapter.CYCLE_COUNT / 2 - CBPageAdapter.CYCLE_COUNT / 2 % mDatas.size());
				ll_dots.removeAllViews();
				buidIndicator();
			}
		});
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new PageChangeListener());

		buidIndicator();
		viewPager.setCurrentItem(CBPageAdapter.CYCLE_COUNT / 2 - CBPageAdapter.CYCLE_COUNT / 2 % mDatas.size());
		viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
		return this;
	}

	private void buidIndicator() {
		int size = (int) (getContext().getResources().getDisplayMetrics().density * 8 + 0.5);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
		lp.setMargins(size, 0, 0, 0);
		for (int i = 0; i < mDatas.size(); i++) {
			View v = new View(getContext());
			v.setLayoutParams(lp);
			ll_dots.addView(v);
		}

	}

	class PageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {

			int count = ll_dots.getChildCount();
			for (int i = 0; i < count; i++) {
				if (i == position % mDatas.size()) {
					ll_dots.getChildAt(i).setBackgroundResource(R.drawable.cycle_on);
				} else {
					ll_dots.getChildAt(i).setBackgroundResource(R.drawable.cycle_off);
				}
			}

			// int currentPosition = position;
			// if (position == 0) {
			// currentPosition = mDatas.size() - 1;
			// } else if (position == mDatas.size()) {
			// currentPosition = 1;
			// }
			// viewPager.setCurrentItem(currentPosition, false);
		}

	}
}
