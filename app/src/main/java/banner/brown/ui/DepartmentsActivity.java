package banner.brown.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
public class DepartmentsActivity extends ActionBarActivity {

    private ListView mainListView ;
    private ArrayList<listHeader> departmentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_list_view);

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.mainListView );

        departmentData = new ArrayList<listHeader>();

        for (int i = 0; i < DepartmentList.titles.size();i++){
            departmentData.add(new listHeader(DepartmentList.abbreviations.get(i),DepartmentList.titles.get(i)));
        }

        final CourseCubListAdapter adapter = new CourseCubListAdapter(this,
                R.layout.general_list_view_row, departmentData);

//        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String dept = departmentData.get(position).abbrev;
//                BannerAPI.getCoursesByDept("201420", dept, new Response.Listener<JSONObject>() {
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
//            }
//        });


        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( adapter );
    }


}
