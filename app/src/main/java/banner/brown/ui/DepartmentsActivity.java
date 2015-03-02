package banner.brown.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import banner.brown.api.DepartmentList;

/**
 * Created by kappi on 2/23/15.
 */
public class DepartmentsActivity extends ActionBarActivity {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_list_view);

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.mainListView );

        // Create ArrayAdapter
        listAdapter = new ArrayAdapter<String>(this, R.layout.general_list_view_row, DepartmentList.titles);

        //listAdapter.add( "add this item" );


        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );
    }
}
