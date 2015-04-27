package banner.brown.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import banner.brown.models.Course;
import banner.brown.models.Semester;
import banner.brown.ui.R;



/**
 * Created by Andy on 3/18/15.
 */
public class SemesterSpinnerAdapter extends ArrayAdapter<Semester> {

    /**
     * Created by kappi on 3/4/15.
     */

    Context context;
    int layoutResourceId;
    ArrayList<Semester> data = null;

    public SemesterSpinnerAdapter(Context context, int layoutResourceId, ArrayList<Semester> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);



        Semester item = data.get(position);
        TextView toUpdate = (TextView)row.findViewById(android.R.id.text1);
        toUpdate.setText(item.toString());
        return row;
    }


}


