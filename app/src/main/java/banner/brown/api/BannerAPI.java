package banner.brown.api;

import android.webkit.CookieManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URLEncoder;
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
import banner.brown.ui.LoginActivity;

/**
 * Created by Andy on 2/16/15.
 */
public class BannerAPI {

    private static String DEV = "https://ords-qa.services.brown.edu:8443/pprd/banner/mobile";

    private static String TEST = "https://blooming-bastion-7117.herokuapp.com";

    public static String HOST = DEV;

    public static String SEARCH_ENDPOINT = "http://blooming-bastion-7117.herokuapp.com/search?";

    private static int NUM_SEARCH_RESULTS = 20;

    public static Course getCourse(String CRN) {
        return new Course("Test Course", 2014, 1, "Test Description", "123", "DEPT","TR 0900-1020");
    }

    public static void testVolleyCall(Response.Listener responseListener,
                                      Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest("http://api.ipify.org/?format=json", null,  responseListener, errorListener);
        BannerApplication.getInstance().addToRequestQueue(request);
    }

    public static void getCoursesByDept(String dept, int page,
                                         Response.Listener responseListener, Response.ErrorListener errorListener) {
        String semester = BannerApplication.getInstance().curSelectedSemester.getSemesterCode();
        getCoursesByDept(semester, dept, page, responseListener, errorListener);
    }

    private static void getCoursesByDept(String term, String dept, int page,
                                        Response.Listener responseListener, Response.ErrorListener errorListener) {
        String urlRequest = HOST + "/courses?term=" + term +"&dept=" + dept;

        if (page > 0) {
            urlRequest += "&page=" + page;
        }
        JsonObjectRequest request = new JsonObjectRequest(urlRequest, null,  responseListener, errorListener);
        BannerApplication.getInstance().addToRequestQueue(request);
    }

    public static void getCourseByCRN( String CRN,
                                      Response.Listener responseListener, Response.ErrorListener errorListener) {
        String semester = BannerApplication.getInstance().curSelectedSemester.getSemesterCode();

        getCourseByCRN(semester, CRN, responseListener, errorListener);
    }

    private static void getCourseByCRN(String term, String CRN,
                                        Response.Listener responseListener, Response.ErrorListener errorListener) {
        String urlRequest = HOST + "/courses?term=" + term +"&crn=" + CRN;

        JsonObjectRequest request = new JsonObjectRequest(urlRequest, null,  responseListener, errorListener);
        BannerApplication.getInstance().addToRequestQueue(request);
    }

    public static void getCurrentCourses(Response.Listener responseListener,
                                         Response.ErrorListener errorListener) {
        String semester = BannerApplication.getInstance().curSelectedSemester.getSemesterCode();

        if (BannerApplication.curCookie.isEmpty()) {
        } else {
            String urlRequest = HOST + "/cartbyid?term=" + semester + "&in_id=" + BannerApplication.curCookie;
            JsonObjectRequest request = new JsonObjectRequest(urlRequest, null,  responseListener, errorListener);
            BannerApplication.getInstance().addToRequestQueue(request);

        }

//        String cookies = CookieManager.getInstance().getCookie(LoginActivity.curLoginAPI);

//        String urlRequest = HOST + "/cartbyid?term=" + semester + "&in_id=" + 100445912"
    }

    public static void searchCourses(String query,
                                     Response.Listener responseListener, Response.ErrorListener errorListener) {
        String semester = BannerApplication.getInstance().curSelectedSemester.getSemesterCode();

        searchCourses(semester, query, NUM_SEARCH_RESULTS, responseListener, errorListener);
    }

    private static void searchCourses(String term, String query, int num_results,
                                       Response.Listener responseListener, Response.ErrorListener errorListener) {
        try {
            String cleanedQuery = URLEncoder.encode(query, "UTF-8");

            String urlRequest = SEARCH_ENDPOINT + "term=" + term + "&num_results=" + num_results + "&search="
                    + cleanedQuery;
            JsonObjectRequest request = new JsonObjectRequest(urlRequest, null, responseListener, errorListener);
            BannerApplication.getInstance().addToRequestQueue(request);
        }
        catch (Exception e) {

        }
    }

}
