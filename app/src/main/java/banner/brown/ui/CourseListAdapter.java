package banner.brown.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
            View row = convertView;
            InfoHolder holder = null;

            if(row == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new InfoHolder();
                holder.abbrev = (TextView)row.findViewById(R.id.listViewAbbreviation);
                holder.title = (TextView)row.findViewById(R.id.listViewTitle);

                row.setTag(holder);
            }
            else
            {
                holder = (InfoHolder)row.getTag();
            }

            Course item = data.get(position);
            holder.title.setText(item.getTitle());
            holder.abbrev.setText(item.getSubjectCode());

            return row;
        }

        static class InfoHolder
        {
            TextView abbrev;
            TextView title;
        }

    }


