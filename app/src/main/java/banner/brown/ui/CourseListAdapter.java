package banner.brown.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import banner.brown.BannerApplication;
import banner.brown.models.Course;
import banner.brown.models.listHeader;

/**
 * Created by Andy on 3/18/15.
 */
public class CourseListAdapter extends ArrayAdapter<Course>{

    /**
     * Created by kappi on 3/4/15.
     */

        Context context;
        int layoutResourceId;
        ArrayList<Course> data = null;

        public CourseListAdapter(Context context, int layoutResourceId, ArrayList<Course> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(layoutResourceId, null);
            }

            TextView abbrev = (TextView) view.findViewById(R.id.listViewAbbreviation);
            TextView title = (TextView)view.findViewById(R.id.listViewTitle);



            Course item = data.get(position);
            Course cartCourse = BannerApplication.mCurrentCart.getCourse(item.getCRN());


            if (cartCourse != null) {
                view.findViewById(R.id.course_list_row_background).setBackgroundColor(context.getResources().getColor(R.color.registeredRed));
            } else {
                view.findViewById(R.id.course_list_row_background).setBackgroundColor(context.getResources().getColor(android.R.color.white));
            }
            title.setText(item.getTitle());
            abbrev.setText(item.getSubjectCode());
            return view;
        }



    }


