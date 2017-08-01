package com.danxx.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import danxx.library.widget.DXPullRefreshMoreView;
import danxx.library.widget.DXPullRefreshView;
import danxx.library.widget.RefreshMoreLisenter;


/**
 * 
 * @author Nono
 *
 */
public class RefreshScrollActivity extends Activity implements RefreshMoreLisenter {
    /** Called when the activity is first created. */
	private DXPullRefreshMoreView mRefreshableView;
	private Context mContext;
	
	
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			if(message.what == 1){
				mRefreshableView.onHeaderRefreshFinish();
			}else if(message.what == 2){
				mRefreshableView.onFootrRefreshFinish();
			}


			Toast.makeText(mContext, "刷新完成", Toast.LENGTH_SHORT).show();
		};
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        mContext = this;
        init();
    }
	private void init() {
		// TODO Auto-generated method stub
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		mRefreshableView = (DXPullRefreshMoreView) findViewById(R.id.refresh_root);
		initData();
	}
	private void initData() {
		mRefreshableView.setRefreshListener(this);
		
	}

	@Override
	public void onRefresh() {
		handler.sendEmptyMessageDelayed(1, 2000);
	}

	@Override
	public void onLoadMore() {
		handler.sendEmptyMessageDelayed(2, 2000);
	}

	@Override
	public void onLongPullUp() {
		Intent intent = new Intent(RefreshScrollActivity.this,CircleImageViewActivity.class);
		startActivity(intent);
		mRefreshableView.onFootrRefreshFinish();
	}
}