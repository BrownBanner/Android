package banner.brown.api;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 2/23/15.
 */
public class BannerRequest extends JsonObjectRequest{

        private String token = null;
        public static int LONG_TIMEOUT = 30000;

        public BannerRequest(int method, String endpoint, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, BannerAPI.HOST + "/api/apps/"+endpoint, jsonRequest, listener, errorListener);
        }
        public BannerRequest(String endpoint, JSONObject jsonRequest, String token, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(Method.GET, BannerAPI.HOST + "/api/apps/"+endpoint, jsonRequest, listener, errorListener);
        }

}
