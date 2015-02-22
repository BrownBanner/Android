package banner.brown.ui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import banner.brown.api.BannerAPI;
import banner.brown.models.Course;
import banner.brown.ui.R;

public class CourseDetail extends ActionBarActivity {

    public static String CRN_EXTRA = "crn_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        if (savedInstanceState == null) {
            CourseDetailFragment fragment = new CourseDetailFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CourseDetailFragment extends Fragment {

        TextView mTitleText;
        Course mCourse;

        public CourseDetailFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_course_detail, container, false);
            mTitleText = (TextView) rootView.findViewById(R.id.title_text);
            String crn = getArguments().getString(CRN_EXTRA);
            mCourse = BannerAPI.getCourse(crn);
            mTitleText.setText(mCourse.getTitle());
            return rootView;
        }
    }
}
