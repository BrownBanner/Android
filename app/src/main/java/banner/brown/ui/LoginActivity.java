package banner.brown.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import banner.brown.BannerApplication;
import banner.brown.ui.R;

public class LoginActivity extends ActionBarActivity {

    public static String PPROD_LOGIN = "https://bannersso.cis-qas.brown.edu/SSB_PPRD";
    public static String PPROD_MAIN_PAGE = "https://selfservice-qas.brown.edu/ssPPRD/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu";

    public static String DPROD_LOGIN = "https://dshibproxycit.services.brown.edu/SSB_DPRD";
    public static String DPROD_MAIN_PAGE = "https://selfservice-dev.brown.edu:9190/ssDPRD/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu";


    public static String curLoginAPI = DPROD_LOGIN;
    public static String curLoginMain = DPROD_MAIN_PAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webview = new WebView(this);
        CookieManager.getInstance().setAcceptCookie(true);
        if(android.os.Build.VERSION.SDK_INT >= 21){
            CookieManager.getInstance().removeAllCookies(null);
        }else{
            CookieManager.getInstance().removeAllCookie();
        }
        setContentView(webview);
        webview.loadUrl(curLoginAPI);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                 handler.proceed(); // Ignore SSL certificate errors
            }
            @Override
            public void onPageFinished(WebView webview, String url) {
                super.onPageFinished(webview, url);
                BannerApplication.hideLoadingIcon();
                if (url.equals(curLoginMain)) {
                    String cookie = getIDMSESSID();
                    BannerApplication.updateUserCookie(cookie);
                    Intent toStart = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(toStart);
                    finish();

                }

            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                BannerApplication.showLoadingIcon(LoginActivity.this);
            }


        }
        );




    }




    public String getIDMSESSID() {

        String cookieValue = "";

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(curLoginAPI);
        String[] temp=cookies.split(";");
        for (String ar1 : temp ){
            if(ar1.contains("IDMSESSID")){
                String[] temp1=ar1.split("=");
                cookieValue = temp1[1];
            }
        }
        return cookieValue;
    }

}
