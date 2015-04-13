package banner.brown.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import banner.brown.ui.R;

public class LoginActivity extends ActionBarActivity {

    public static String PPROD_LOGIN = "https://bannersso.cis-qas.brown.edu/SSB_PPRD";
    public static String PPROD_MAIN_PAGE = "https://selfservice-qas.brown.edu/ssPPRD/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu";


    private String curLoginAPI = PPROD_LOGIN;
    private String curLoginMain = PPROD_MAIN_PAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webview = new WebView(this);
        CookieManager.getInstance().setAcceptCookie(true);

        setContentView(webview);
        webview.loadUrl(curLoginAPI);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(curLoginMain)) {
                    finish();
                    return true;
                }
                view.loadUrl(url);
                return false;
            }
        });

        String cookie = CookieManager.getInstance().getCookie("https://bannersso.cis-qas.brown.edu/SSB_PPRD");
        CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {

            }
        });
//        CookieManager.getInstance().setCookie("https://bannersso.cis-qas.brown.edu/SSB_PPRD", cookie);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
