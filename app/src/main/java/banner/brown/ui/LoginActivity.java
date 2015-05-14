package banner.brown.ui;

import android.content.Intent;
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


    public static String curLoginAPI = PPROD_LOGIN;
    public static String curLoginMain = PPROD_MAIN_PAGE;
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
                if (url.equals(curLoginMain)) {
                    String cookie = getIDMSESSID();
                    BannerApplication.updateUserCookie(cookie);
                    Intent toStart = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(toStart);
                    finish();

                }

            }


        }
        );




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
