package com.danxx.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import danxx.library.widget.DXPullRefreshMoreView;
import danxx.library.widget.RefreshMoreLisenter;

public class RefreshListViewActivity extends AppCompatActivity implements RefreshMoreLisenter {
    ListView listView;
    ArrayList<Integer> data = new ArrayList<>();
    private DXPullRefreshMoreView mRefreshableView;

    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if(message.what == 1){
                mRefreshableView.onHeaderRefreshFinish();
            }else if(message.what == 2){
                mRefreshableView.onFootrRefreshFinish();
            }


            Toast.makeText(RefreshListViewActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list_view);
        listView = (ListView) findViewById(R.id.listview);
        mRefreshableView = (DXPullRefreshMoreView) findViewById(R.id.refresh_root2);
        mRefreshableView.setRefreshListener(this);
        for(int i=0;i<17;i++){
            data.add(i);
        }

        MyAdapter myAdapter = new MyAdapter();

        listView.setAdapter(myAdapter);

        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    @Override
    public void onLoadMore() {
        handler.sendEmptyMessageDelayed(2, 2000);
    }

    /**
     * 重度上拉，一般是做跳转到新页面
     **/
    @Override
    public void onLongPullUp() {

    }


    class MyAdapter extends BaseAdapter{

        /**
         * How many items are in the data set represented by this Adapter.
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return data.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         * @param position Position of the item whose data we want within the adapter's
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return null;
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView tv = new TextView(RefreshListViewActivity.this);
            tv.setHeight(120);
            tv.setTextSize(18);
            tv.setGravity(Gravity.CENTER);
            tv.setText("数据:"+data.get(position));

            return tv;
        }
    }

}
