package banner.brown;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import banner.brown.api.BannerAPI;
import banner.brown.models.Cart;
import banner.brown.models.Semester;
import banner.brown.ui.BannerBaseLogoutTimerActivity;
import banner.brown.ui.LoginActivity;
import banner.brown.ui.R;

/**
 * Created by Andy on 2/22/15.
 */
public class BannerApplication extends Application {
    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    private static BannerApplication sInstance;

    public static Semester curSelectedSemester;

    public static String SHARED_PREF_NAME = "banner.brown.prefs";

    public static String SHARED_PREF_SEMESTER = "banner.brown.prefs.semester";

    public static String SHARED_PREF_TIME_LAST_ACTIVE = "banner.brown.prefs.time.last.active";

    public static String SHARED_PREF_COOKIE = "banner.brown.prefs.cookie";

    public static Cart mCurrentCart;

    public static HashMap<String, String []> mNamedCarts;

    public static String curCookie = "";

    public static String mostRecentNamedCart = "Example";
    static ProgressDialog progress;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;
        disableSSLCertificateChecking();
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String prefSemester = prefs.getString(SHARED_PREF_SEMESTER, null);
        if (prefSemester == null) {
            curSelectedSemester = Semester.getCurrentSemester();
        } else {
            curSelectedSemester = new Semester(prefSemester);
        }
        mCurrentCart = new Cart();
        mNamedCarts = new HashMap<String, String[]>();
        curCookie = prefs.getString(SHARED_PREF_COOKIE,"");


    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized BannerApplication getInstance() {
        return sInstance;
    }

    public static void removeUserCookie() {
        BannerAPI.logOut();
        curCookie = "";
        SharedPreferences prefs = getInstance().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(BannerApplication.SHARED_PREF_TIME_LAST_ACTIVE);
        editor.remove(SHARED_PREF_COOKIE);
        editor.commit();
    }

    public static void updateUserCookie(String cookie) {
        curCookie = cookie;
        SharedPreferences prefs = getInstance().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SHARED_PREF_COOKIE,cookie);
        editor.commit();
        updateLastActive();
    }

    public static boolean getShouldLogOut(){
        long curTime = System.currentTimeMillis();
        SharedPreferences prefs = getInstance().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        if (!prefs.contains(SHARED_PREF_TIME_LAST_ACTIVE)) {
            return false;
        }
        long lastTime = prefs.getLong(SHARED_PREF_TIME_LAST_ACTIVE, 0);
        if (curTime - lastTime > BannerBaseLogoutTimerActivity.startTime) {
            return true;
        }
        return false;
    }

    public static void updateLastActive() {
        SharedPreferences prefs = getInstance().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(BannerApplication.SHARED_PREF_TIME_LAST_ACTIVE, System.currentTimeMillis());
        editor.commit();
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setCurSelectedSemester(Semester semester) {
        curSelectedSemester = semester;
        SharedPreferences pref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BannerApplication.SHARED_PREF_SEMESTER, semester.getSemesterCode());
        editor.commit();
    }
    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                }
        };

        try {

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }

            });
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void updateNamedCarts(JSONObject carts) {
        mNamedCarts.clear();
        try {
            JSONArray cartList = carts.getJSONArray("items");
            for (int i = 0; i < cartList.length(); i++){
                JSONObject curCart = cartList.getJSONObject(i);
                String[] crnList = curCart.getString("crn_list").split(",");
                mNamedCarts.put(curCart.getString("cart_name"), crnList);
            }
        } catch (JSONException e){

        }
    }

    public static void showToast(Activity activity, String text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();

    }

    public static void showLoadingIcon(Activity activity){
        if (progress != null) {
            hideLoadingIcon();
        }
        progress = new ProgressDialog(activity, R.style.CustomDialogTheme);
        progress.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progress.setCancelable(false);
        progress.show();
    }

    public static void hideLoadingIcon(){
        progress.dismiss();
    }
}
