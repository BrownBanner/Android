package banner.brown.ui;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import banner.brown.api.BannerAPI;
import banner.brown.models.Course;
import banner.brown.ui.R;

public class CourseDetail extends BannerBaseLogoutTimerActivity {

    public static String CRN_EXTRA = "crn_extra";
    public static String COURSE_NAME_EXTRA = "course_name_extra";

    private String CRN = "";

    private Course mCourse;

    private TextView mTitleText;
    private TextView mScheduleText;
    private TextView mAvailableSeatsText;
    private TextView mTotalSeatsText;
    private TextView mInstructorText;
    private TextView mLocationText;
    private TextView mDescriptionText;
    private TextView mCRNText;
    private TextView mExamInfoText;
    private TextView mPrereqText;


    private RelativeLayout mBookListButton;
    private RelativeLayout mCriticalReviewButton;
    private RelativeLayout mCoursePreviewButton;
    private String mBookList;
    private String mCriticalReview;
    private String mCoursePreview;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        CRN = b.getString(CRN_EXTRA);
        String title = b.getString(COURSE_NAME_EXTRA);

        getSupportActionBar().setTitle(title);

        mTitleText = (TextView) findViewById(R.id.title_text);
        mScheduleText = (TextView) findViewById(R.id.schedule);
        mAvailableSeatsText = (TextView) findViewById(R.id.seats_available);
        mTotalSeatsText = (TextView) findViewById(R.id.seats_total);
        mInstructorText = (TextView) findViewById(R.id.detail_instructor_teaching);
        mLocationText = (TextView) findViewById(R.id.detail_location_taught);
        mDescriptionText = (TextView) findViewById(R.id.detail_description_course);
        mCRNText = (TextView) findViewById(R.id.detail_CRN);
        mExamInfoText = (TextView) findViewById(R.id.detail_exam);
        mBookListButton = (RelativeLayout) findViewById(R.id.book_list_button);
        mCoursePreviewButton = (RelativeLayout) findViewById(R.id.course_preview_button);
        mCriticalReviewButton = (RelativeLayout) findViewById(R.id.critical_review_button);
        mPrereqText = (TextView) findViewById(R.id.detail_prereq);

        mBookListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CourseDetail.this, BannerWebActivity.class);
                i.putExtra(BannerWebActivity.WEB_ACTIVITY_NAME,"Book List");
                i.putExtra(BannerWebActivity.WEB_URL_EXTRA, mBookList);
                startActivity(i);
            }
        });

        mCoursePreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CourseDetail.this, BannerWebActivity.class);
                i.putExtra(BannerWebActivity.WEB_ACTIVITY_NAME,"Course Preview");
                i.putExtra(BannerWebActivity.WEB_URL_EXTRA, mCoursePreview);
                startActivity(i);
            }
        });

        mCriticalReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mCriticalReview));
                startActivity(i);
            }
        });
        updateCourse();


    }

    private void updateCourse() {
        BannerAPI.getCourseByCRN(CRN, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray courseList = response.getJSONArray("items");
                    if (courseList.length() > 0) {
                        mCourse = new Course(courseList.getJSONObject(0));
                    }
                    updateUIwithCourse();
                } catch (JSONException e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyError x = error;
            }
        });
    }

    private void updateUIwithCourse() {
        mCRNText.setText( mCourse.getCRN());
        mTitleText.setText(mCourse.getTitle());
        mInstructorText.setText(mCourse.getInstructor());
        mScheduleText.setText(mCourse.getMeetingTime());
        mAvailableSeatsText.setText(""+mCourse.getSeatsAvailable());
        mTotalSeatsText.setText(" / "+mCourse.getSeatsTotal());
        mLocationText.setText(mCourse.getMeetingLocation());
        mDescriptionText.setText(mCourse.getDescription());
        mExamInfoText.setText(mCourse.getExamInfo());
        mPrereqText.setText(mCourse.getPrereq());

        mBookList = mCourse.getBookList();
        mCriticalReview = mCourse.getCriticialReview();
        mCoursePreview = mCourse.getCoursePreview();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateCourse();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu_course_detail, menu);
            return true;

    }


}
