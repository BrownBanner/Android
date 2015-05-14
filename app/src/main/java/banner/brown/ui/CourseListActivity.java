package banner.brown.ui;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import banner.brown.BannerApplication;
import banner.brown.api.BannerAPI;
import banner.brown.models.Course;
import banner.brown.models.listHeader;
import banner.brown.ui.R;

public class CourseListActivity extends BannerBaseLogoutTimerActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener{

    public static String DEPT_ABBREV_EXTRA = "dept_extra";
    public static String DEPT_FULL_EXTRA = "dept_full";

    SearchView mSearchView;

    ListView mCourseListView;

    CourseListAdapter mAdapter;

    ArrayList<Course> mCourseData;
    ArrayList<Course> mOriginalCourseData;


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

        BannerApplication.showLoadingIcon(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(full);

        mCourseListView = (ListView) findViewById(R.id.course_list);
        mCourseData = new ArrayList<>();
        mAdapter = new CourseListAdapter(this,  R.layout.course_list_view_row, mCourseData);
        mCourseListView.setAdapter(mAdapter);

        mCourseListView.setOnItemClickListener(this);

        BannerAPI.getCoursesByDept( dept, 0, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    BannerApplication.hideLoadingIcon();
                    JSONArray classes = response.getJSONArray("items");
                    processClasses(classes);


                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BannerApplication.hideLoadingIcon();
                VolleyError x = error;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        MenuItem item = menu.findItem(R.id.search_courses_in_activity);
        item.expandActionView();


        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Do whatever you want
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //Do whatever you want
                finish();
                return true;
            }
        });
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.requestFocusFromTouch();
        mSearchView.setQueryHint("Filter results by time etc");
        mSearchView.setOnQueryTextListener(this);

        return true;
    }



    private void processClasses(JSONArray classes) {
        mAdapter.clear();
        try {
            for (int i = 0; i < classes.length(); i++) {
                JSONObject course = classes.getJSONObject(i);
                mCourseData.add(new Course(course));
            }
            Collections.sort(mCourseData);
            mOriginalCourseData = new ArrayList<>();
            mOriginalCourseData.addAll(mCourseData);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {

            ArrayList<Course> matching = new ArrayList<>();
            for (Course course : mOriginalCourseData){
                if (course.getCRN().toLowerCase().contains(newText.toLowerCase()) ||
                        course.getMeetingTime().toLowerCase().contains(newText.toLowerCase())||
                        course.getDepartment().toLowerCase().contains(newText.toLowerCase())||
                        course.getTitle().toLowerCase().contains(newText.toLowerCase())||
                        course.getSubjectCode().toLowerCase().contains(newText.toLowerCase())
                        ){
                    matching.add(course);
                }
            }
            mOriginalCourseData.size();
            mCourseData.clear();
            mCourseData.addAll(matching);
            Collections.sort(mCourseData);

            mAdapter.notifyDataSetChanged();


        }
        else{
            mCourseData.clear();
            mCourseData.addAll(mOriginalCourseData);
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }
}
