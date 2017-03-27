package com.mktech.testaddapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by stefan on 2017/3/24.
 */

public class Myadapter extends RecyclerView.Adapter<Myadapter.Viewholder> {
	private List<Drawable>				mData;
	private Context						mContext;
	private View.OnFocusChangeListener	mOnFocusChangeListener;
	public Myadapter(List<Drawable> data, Context context) {
		mData = data;
		mContext = context;
	}
	
	@Override
	public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test, parent, false);
		view.setOnFocusChangeListener(mOnFocusChangeListener);
		Viewholder viewholder = new Viewholder(view);

		return viewholder;
	}
	
	@Override
	public void onBindViewHolder(Viewholder holder, int position) {
		
		holder.mImageView.setImageDrawable(mData.get(position));
		holder.mImageView.setFocusable(true);
	}
	
	@Override
	public int getItemCount() {
		return mData.size();
	}
	
	public static class Viewholder extends RecyclerView.ViewHolder {
		private ImageView mImageView;
		
		public Viewholder(View itemView) {
			super(itemView);
			mImageView = (ImageView) itemView.findViewById(R.id.image);
		}
	}
	
	public void setOnFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
		mOnFocusChangeListener = onFocusChangeListener;
	}
}
