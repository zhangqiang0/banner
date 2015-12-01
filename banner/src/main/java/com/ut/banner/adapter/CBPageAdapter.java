package com.ut.banner.adapter;

import java.util.List;

import com.ut.banner.CBViewHolderCreator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class CBPageAdapter<T> extends RecyclingPagerAdapter {
	
	public static final int CYCLE_COUNT = 1000;
	protected List<T> mDatas;
	protected CBViewHolderCreator<Holder<T>> holderCreator;
	
	protected DataChangeListener listener;

	public CBPageAdapter(CBViewHolderCreator<Holder<T>> holderCreator, List<T> datas) {
		this.holderCreator = holderCreator;
		this.mDatas = datas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View view, ViewGroup container) {
		Holder<T> holder = null;
		if (view == null) {
			holder = (Holder<T>) holderCreator.createHolder();
			view = holder.createView(container.getContext());
			view.setTag(holder);
		} else {
			holder = (Holder<T>) view.getTag();
		}
		if (mDatas != null && !mDatas.isEmpty())
			holder.UpdateUI(container.getContext(), position % mDatas.size(), mDatas.get(position % mDatas.size()));
		return view;
	}
	
	@Override
	public int getCount() {
		if (mDatas == null)
			return 0;
//		return mDatas.size();
//		return Integer.MAX_VALUE;
		return CYCLE_COUNT;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (listener != null){
			listener.onDataChanged();
		}
	}

	public void setDataChangeListener(DataChangeListener listener){
		this.listener = listener;
	}
	/**
	 * @param <T>
	 *            任何你指定的对象
	 */
	public interface Holder<T> {
		View createView(Context context);

		void UpdateUI(Context context, int position, T data);
	}
	
	public interface DataChangeListener{
		void onDataChanged();
	}
}
