package banner.brown.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import banner.brown.BannerApplication;
import banner.brown.ui.R;

public class BannerWebActivity extends BannerBaseLogoutTimerActivity {

    public static String WEB_URL_EXTRA = "WEB_URL_EXTRA";

    public static String WEB_ACTIVITY_NAME = "WEB_URL_NAME";

    public static String LOCATION_STRING = "LOCATION_STRING";

    protected WebView mWebView;


    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView = new WebView(this);
        CookieManager.getInstance().setAcceptCookie(true);

        mWebView.getSettings().setJavaScriptEnabled(true);

        String title = getIntent().getExtras().getString(WEB_ACTIVITY_NAME);

        if (!title.isEmpty()) {
            getSupportActionBar().setTitle(title);

        }

        setContentView(mWebView);

        url = getIntent().getExtras().getString(WEB_URL_EXTRA);
        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                BannerWebActivity.this.setSupportProgress(progress * 1000);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webview, String url) {
                super.onPageFinished(webview, url);
                BannerApplication.hideLoadingIcon();


            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                BannerApplication.showLoadingIcon(BannerWebActivity.this);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
