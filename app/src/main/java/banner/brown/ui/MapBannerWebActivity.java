package banner.brown.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by kappi on 5/14/15.
 */
public class MapBannerWebActivity extends BannerWebActivity {

    private boolean loadedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {


                if (url.equals("http://www.brown.edu/Facilities/Facilities_Management/m/index.php")){
                    mWebView.loadUrl("javascript:(function(){"+
                            "l=document.getElementById('searchbtn');"+
                            "e=document.createEvent('HTMLEvents');"+
                            "e.initEvent('click',true,true);"+
                            "l.dispatchEvent(e);"+
                            "})()");
                    mWebView.loadUrl("javascript:(function(){"+
                            "l=document.getElementById('btnsearch');"+
                            "e=document.createEvent('HTMLEvents');"+
                            "e.initEvent('click',true,true);"+
                            "l.dispatchEvent(e);"+
                            "})()");
                    loadedOnce = true;
                }

                else if (loadedOnce) {
                    String loc = getIntent().getExtras().getString(LOCATION_STRING);
                    mWebView.loadUrl("javascript:document.getElementById('txtsearch').value = '"+ loc +"';");
                    mWebView.loadUrl("javascript:window.searchlist();");
                    loadedOnce = false;
                }


            }
        });


    }

}
