package banner.brown.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import banner.brown.api.BannerAPI;
import banner.brown.models.Course;
import banner.brown.ui.R;

public class SearchActivity extends BannerBaseLogoutTimerActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {


    ListView mListView;

    CourseListAdapter mListAdapter;

    ArrayList<Course> mSearchResults;

    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mListView = (ListView) findViewById(R.id.search_list);
        mSearchResults = new ArrayList<>();
        mListAdapter = new CourseListAdapter(this,  R.layout.general_list_view_row, mSearchResults);
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(this);


    }



    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        MenuItem item = menu.findItem(R.id.search_courses_in_activity);
        item.expandActionView();

        MenuItemCompat.setOnActionExpandListener(item,new MenuItemCompat.OnActionExpandListener() {

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
        mSearchView.setQueryHint("CSCI 1900 John Smith");
        mSearchView.setOnQueryTextListener(this);


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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 1) {

            BannerAPI.searchCourses(newText, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray result = (JSONArray) response.getJSONArray("items");
                        mListAdapter.clear();
                        try {
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject course = result.getJSONObject(i);
                                mSearchResults.add(new Course(course));
                            }
                            mListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {

                        }
                    } catch (JSONException e) {

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyError e = error;
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, CourseDetail.class);
        String CRN = mSearchResults.get(position).getCRN();
        String name = mSearchResults.get(position).getSubjectCode();
        i.putExtra(CourseDetail.CRN_EXTRA, CRN);
        i.putExtra(CourseDetail.COURSE_NAME_EXTRA, name );
        startActivity(i);
    }
}
