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
import banner.brown.ui.R;

/**
 * Created by Andy on 5/14/15.
 */
public class ConflictListAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<Course> courses;

    public ConflictListAdapter(Context context, int resource, ArrayList<Course> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.courses = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);


        }


        Course course = courses.get(position);

        TextView courseName = (TextView) row.findViewById(R.id.conflict_name);
        TextView courseTime = (TextView) row.findViewById(R.id.conflict_time);
        courseName.setText(course.getTitle());
        courseTime.setText(course.getFormattedTime());
        return row;
    }
}
