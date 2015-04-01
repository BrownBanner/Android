package banner.brown.ui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import banner.brown.api.BannerAPI;
import banner.brown.models.Course;
import banner.brown.ui.R;

public class CourseDetail extends ActionBarActivity {

    public static String CRN_EXTRA = "crn_extra";
    public static String COURSE_NAME_EXTRA = "course_name_extra";

    private String CRN = "";

    private Course mCourse;

    private TextView mTitleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        CRN = b.getString(CRN_EXTRA);
        String title = b.getString(COURSE_NAME_EXTRA);

        getSupportActionBar().setTitle(title);

        mTitleText = (TextView) findViewById(R.id.title_text);

        BannerAPI.getCourseByCRN("201420", CRN, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray courseList = response.getJSONArray("items");
                    if (courseList.length() > 0) {
                        mCourse = new Course(courseList.getJSONObject(0));
                    }
                } catch (JSONException e) {

                }

                mTitleText.setText(mCourse.getTitle());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyError x = error;
            }
        });
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
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
