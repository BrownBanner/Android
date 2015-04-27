package banner.brown.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import banner.brown.api.BannerAPI;
import banner.brown.api.DepartmentList;
import banner.brown.models.listHeader;

/**
 * Created by kappi on 2/23/15.
 */
public class DepartmentsActivity extends BannerBaseLogoutTimerActivity {

    private ListView mainListView ;
    private ArrayList<listHeader> departmentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_list_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.mainListView );

        departmentData = new ArrayList<listHeader>();

        for (int i = 0; i < DepartmentList.titles.size();i++){
            departmentData.add(new listHeader(DepartmentList.abbreviations.get(i),DepartmentList.titles.get(i)));
        }

        final CourseCubListAdapter adapter = new CourseCubListAdapter(this,
                R.layout.general_list_view_row, departmentData);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DepartmentsActivity.this, CourseListActivity.class);
                String dept = departmentData.get(position).abbrev;
                String full = departmentData.get(position).title;
                i.putExtra(CourseListActivity.DEPT_ABBREV_EXTRA, dept);
                i.putExtra(CourseListActivity.DEPT_FULL_EXTRA, full);
                startActivity(i);
//                String dept = departmentData.get(position).abbrev;
//                BannerAPI.getCoursesByDept("201420", dept,0, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        JSONObject x = response;
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyError x = error;
//                    }
//                });
            }
        });


        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( adapter );
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
