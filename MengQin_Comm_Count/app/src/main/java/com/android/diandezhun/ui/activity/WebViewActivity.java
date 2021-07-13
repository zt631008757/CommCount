package com.android.diandezhun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.baselibrary.tool.CommToast;
import com.android.baselibrary.tool.Log;
import com.android.baselibrary.view.Comm_HeadView;
import com.android.diandezhun.R;

import butterknife.BindView;


/**
 * Created by Administrator on 2018/6/20.
 */
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.comm_title)
    Comm_HeadView comm_title;
    @BindView(R.id.myProgressBar)
    ProgressBar myProgressBar;
    @BindView(R.id.webview)
    WebView webview;

    public String title = "";
    public String url = "";

    public static void startActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        comm_title.setRightText("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.reload();
            }
        });
        initWebView();
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        Log.i("url:" + url);
        if (!TextUtils.isEmpty(url)) {
            webview.loadUrl(url);
        } else {
            CommToast.showToast(mContext, "URL地址为空");
            finish();
        }
        comm_title.setTitle(title);
    }

    private void initWebView() {
        WebSettings mWebSettings = webview.getSettings();
        String userAgent = webview.getSettings().getUserAgentString();
        if (!TextUtils.isEmpty(userAgent)) {
            webview.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 7.1.1; 1801-A01 Build/NMF26X; wv)");
        }
        mWebSettings.setJavaScriptEnabled(true);                    //  支持Javascript 与js交互
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//  支持通过JS打开新窗口
        mWebSettings.setAllowFileAccess(true);                      //  设置可以访问文件
        mWebSettings.setSupportZoom(true);                          //  支持缩放
        mWebSettings.setBuiltInZoomControls(true);                  //  设置内置的缩放控件
        mWebSettings.setUseWideViewPort(true);                      //  自适应屏幕

        //自适应屏幕
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setLoadWithOverviewMode(true);

        mWebSettings.setSupportMultipleWindows(true);               //  多窗口
        mWebSettings.setDefaultTextEncodingName("utf-8");           //  设置编码格式
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);       //  缓存模式
        //设置不用系统浏览器打开,直接显示在当前WebView
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                Log.i("url:" + url);
//                return true;
                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                    return true;
                } catch (Exception e){
                    return false;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        //设置WebChromeClient类
        webview.setWebChromeClient(new MyWebClient());
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                // TODO: 2017-5-6 处理下载事件
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        webview.addJavascriptInterface(new AndroidtoJs(), "Android");//AndroidtoJS类对象映射到js的Android对象
    }

    class MyWebClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            myProgressBar.setProgress(newProgress);
            if (myProgressBar != null) {
                if (newProgress == 100) {
                    myProgressBar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == myProgressBar.getVisibility()) {
                        myProgressBar.setVisibility(View.VISIBLE);
                    }
                    myProgressBar.setProgress(newProgress);
                }
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String tit) {
            super.onReceivedTitle(view, tit);
            Log.i("tit:"+ tit);
            if(TextUtils.isEmpty(title)) {
                comm_title.setTitle(tit);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();
            webview.clearCache(true);
            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public class AndroidtoJs extends Object {
        @JavascriptInterface
        public void openActivity(String msg) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            Message message = new Message();
            message.obj = msg;
            message.what = MSG_OPEN_ACTIVITY;
            handler.sendMessage(message);
        }
    }

    private static final int MSG_OPEN_ACTIVITY = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_OPEN_ACTIVITY:
                    String msg = (String) message.obj;
                    break;
            }
        }
    };

}
