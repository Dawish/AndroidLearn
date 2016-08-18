package com.danxx.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import danxx.library.adapter.BaseRecyclerViewAdapter;
import danxx.library.adapter.BaseRecyclerViewHolder;
import danxx.library.focusview.FocusLinearLayout;

public class ActivityTest extends Activity {
		
	private FocusLinearLayout focusLinearLayout;


	private ArrayList<Integer> data = new ArrayList<>();

	private MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_test);
		
		focusLinearLayout = (FocusLinearLayout) findViewById(R.id.focusLinearLayout);
		for(int i=0;i<17;i++){
			data.add(i);
		}


//		focusLinearLayout.setFocusMovingDuration(0);
//		focusLinearLayout.setFocusHighlightDrawable(R.drawable.default_focus);
//		focusLinearLayout.setFocusScale(1.4f,1.4f);

//		View view = LayoutInflater.from(this).inflate(R.layout.focus_item_view,null);
//
//		focusLinearLayout.addChildView(view, 10, 30, 0, 0);
//
//		View view1 = LayoutInflater.from(this).inflate(R.layout.focus_item_view,null);
//		focusLinearLayout.addChildView(view1, 10, 0, 0, 0);
	}

	class MyAdapter extends BaseRecyclerViewAdapter<Integer>{

		/**
		 * 创建item view
		 *
		 * @param parent
		 * @param viewType
		 * @return
		 */
		@Override
		protected BaseRecyclerViewHolder createItem(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(ActivityTest.this).inflate(R.layout.focus_item_view, null);
			return  new MyViewHolder(view);
		}

		/**
		 * 绑定数据
		 *
		 * @param holder
		 * @param position
		 */
		@Override
		protected void bindData(BaseRecyclerViewHolder holder, int position) {

		}

		class MyViewHolder extends BaseRecyclerViewHolder {

			public MyViewHolder(View itemView) {
				super(itemView);
			}

			@Override
			protected View getView() {
				return null;
			}
		}

	}



}
