package me.everzc.webviewimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;



import java.io.File;

/**
 * 选择相册列表
 * Created by Zwj on 2019/09/05.
 */

public class Html5WebView extends WebView {

    private Context mContext;

    private OnProgressChangedListener mProgressChangedListener;

    public Html5WebView(Context context) {
        this(context, null);
    }

    public Html5WebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Html5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        WebSettings mWebSettings = getSettings();
//        mWebSettings.setDefaultFontSize(14); //设置的是默认字体大小

        mWebSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        mWebSettings.setBuiltInZoomControls(false);  //设置可以缩放
        mWebSettings.setDisplayZoomControls(false);  //隐藏原生的缩放控件
        //mWebSettings.setBlockNetworkImage(true);
        mWebSettings.setJavaScriptEnabled(true); //支持js
        mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWebSettings.setAppCacheEnabled(true);   //开启 Application Caches 功能
        mWebSettings.setDatabaseEnabled(true); //开启 database storage API 功能
        mWebSettings.setDomStorageEnabled(true); //// 开启 DOM storage API 功能
        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setLayoutAlgorithm
                (WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应内容大小
        //缓存数据
        saveData(mWebSettings);
        newWin(mWebSettings);
        setWebChromeClient(webChromeClient);
        setWebViewClient(webViewClient);
    }


    public void setProgressChangedListener(OnProgressChangedListener mProgressChangedListener) {
        this.mProgressChangedListener = mProgressChangedListener;
    }

    /**
     * 多窗口的问题
     */
    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /**
     * 判断网络是否连接
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public  boolean isConnected(Context context) {
        NetworkInfo info = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * HTML5数据存储
     */
    private void saveData(WebSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置

        if (isConnected(mContext)) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }
        File cacheDir = mContext.getCacheDir();
        if (cacheDir != null) {
            String appCachePath = cacheDir.getAbsolutePath();
            mWebSettings.setDomStorageEnabled(true);
            mWebSettings.setDatabaseEnabled(true);
            mWebSettings.setAppCacheEnabled(true);
            mWebSettings.setAppCachePath(appCachePath);
        }
    }

    public void setupBody(String body) {
        // loadData(createBody(body),"text/html","UTF-8");
        loadDataWithBaseURL("", createBody(body), "text/html", "UTF-8", "");
       // LogUtils.e("body : "+body);
    }

    private String createBody(String body) {
        return String.format(
                "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/articlenew.css\">" +
                       /* "<script type=\"text/javascript\" src=\"http://libs.baidu.com/jquery/1.11.1/jquery.min.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"http://www.w3cways.com/demo/LazyLoad/js/jquery.lazyload.js\"></script>"+
                       */
                        "<script type=\"text/javascript\" src=\"http://www.luzhouapp.cn/app_js/jquery.min.js\"></script>\n" +
                        "<script type=\"text/javascript\" src=\"http://www.luzhouapp.cn/app_js/jquery.lazyload.js\"></script>"+

                        "<script type=\"text/javascript\">\n" +
                                "$(function() {\n" +
                                "$(\"img.lazy\").lazyload({\n" +
                                "effect : \"fadeIn\",\n" +
                                "threshold: 200\n" +
                                "});\n" +
                                "});\n" +
                                "</script>"
                        + "</head>"
                        + "<body>"
                        + "%s"
                        + "</body>"
                        + "</html>"
                , body);
    }

    WebViewClient webViewClient = new WebViewClient() {
        /**
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.d("Url:", url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
           // imgReset(view);//重置webview中img标签的图片大小
            addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        private void addImageClickListener(WebView webView) {
            webView.loadUrl(
                    "javascript:(function(){" +
                            "var objs = document.getElementsByTagName(\"img\"); " +
                            "for(var i=0;i<objs.length;i++)  " +
                            "{"
                            + "    objs[i].onclick=function()  " +
                            "    {  "
                            + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                            "    }  " +
                            "}" +
                            "})()");
        }
        private void imgReset(WebView webView) {
            webView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName('img'); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "var img = objs[i];   " +
                    "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                    "}" +
                    "})()");
        }
    };

    WebChromeClient webChromeClient = new WebChromeClient() {

        //=========HTML5定位==========================================================
        //需要先加入权限
        //<uses-permission android:name="android.permission.INTERNET"/>
        //<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        //<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mProgressChangedListener != null) {
                mProgressChangedListener.onProgressChanged(newProgress);
            }
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
        //=========HTML5定位==========================================================


        //=========多窗口的问题==========================================================
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return true;
        }
        //=========多窗口的问题==========================================================
    };

    public static interface OnProgressChangedListener {
        void onProgressChanged(int progress);
    }
}
