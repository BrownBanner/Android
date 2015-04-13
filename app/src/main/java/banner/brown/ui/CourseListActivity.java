package banner.brown.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import banner.brown.api.BannerAPI;
import banner.brown.models.Course;
import banner.brown.models.listHeader;
import banner.brown.ui.R;

public class CourseListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    public static String DEPT_ABBREV_EXTRA = "dept_extra";
    public static String DEPT_FULL_EXTRA = "dept_full";


    ListView mCourseListView;

    CourseListAdapter mAdapter;

    ArrayList<Course> mCourseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        Bundle extras = getIntent().getExtras();
        String dept = "";
        String full = "";
        if(extras !=null) {
            dept = extras.getString(DEPT_ABBREV_EXTRA);
            full = extras.getString(DEPT_FULL_EXTRA);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(full);

        mCourseListView = (ListView) findViewById(R.id.course_list);
        mCourseData = new ArrayList<>();
        mAdapter = new CourseListAdapter(this,  R.layout.general_list_view_row, mCourseData);
        mCourseListView.setAdapter(mAdapter);

        mCourseListView.setOnItemClickListener(this);

        BannerAPI.getCoursesByDept("201420", dept, 0, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray classes = response.getJSONArray("items");
                    processClasses(classes);

                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyError x = error;
            }
        });
    }

    private void processClasses(JSONArray classes) {
        mAdapter.clear();
        try {
            for (int i = 0; i < classes.length(); i++) {
                JSONObject course = classes.getJSONObject(i);
                mCourseData.add(new Course(course));
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, CourseDetail.class);
        String CRN = mCourseData.get(position).getCRN();
        String name = mCourseData.get(position).getSubjectCode();
        i.putExtra(CourseDetail.CRN_EXTRA, CRN);
        i.putExtra(CourseDetail.COURSE_NAME_EXTRA, name );
        startActivity(i);
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
