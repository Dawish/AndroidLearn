package com.anno.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anno.R;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;

import danxx.library.tools.MyLog;

/**
 * dawish 2017.8.24
 */
public class WebViewActivity extends AppCompatActivity {
    public static final String URL_KEY = "url_key";
    private String url;

    /**
     * 跳转到Webview
     *
     * @param context Context
     * @param url     url地址
     */
    public static void webviewEntrance(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(URL_KEY, url);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initViews();
    }


    private void init() {
        Intent intent = getIntent();
        url = intent.getStringExtra(URL_KEY);
    }
    private AgentWeb mAgentWeb;
    private RelativeLayout webViewParent;
    private  Toolbar toolbar;
    private void initViews(){
        init();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(toolbar, "Dawish_大D", true);

        webViewParent = (RelativeLayout) findViewById(R.id.webViewParent);


        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(webViewParent, new RelativeLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(callback) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go(url);
    }

    ChromeClientCallbackManager.ReceivedTitleCallback callback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView webView, String s) {
            if(toolbar!=null){
                toolbar.setTitle(s);
            }
        }
    };
    public void initToolBar(Toolbar toolbar, String name, boolean showHomeAsUp) {
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setElevation(4);
            getSupportActionBar().setDisplayShowHomeEnabled(showHomeAsUp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }
    /**
     * @param str 弹出的文字
     */
    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb!=null && mAgentWeb.handleKeyEvent(keyCode, event)) {
            MyLog.i("danxx", "处理返回");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
