package banner.brown.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import banner.brown.BannerApplication;
import banner.brown.models.Course;

/**
 * Created by Andy on 2/16/15.
 */
public class BannerAPI {

    private static String DEV = "https://ords-dev.brown.edu";

    public static String HOST = DEV;

    public static Course getCourse(String CRN) {
        return new Course("Test Course", 2014, 1, "Test Description", "123", "DEPT");
    }

    public static void testVolleyCall(Response.Listener responseListener,
                                      Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest("http://api.ipify.org/?format=json", null,  responseListener, errorListener);
        BannerApplication.getInstance().addToRequestQueue(request);
    }

    public static void getCoursesByDept(String term, String dept, Response.Listener responseListener, Response.ErrorListener errorListener) {
        //add departments when this works
        JsonObjectRequest request = new JsonObjectRequest(
                HOST + "/dprd/banner/mobile/courses?term=" + term, null,  responseListener, errorListener);
        BannerApplication.getInstance().addToRequestQueue(request);
    }


}
