package banner.brown.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import banner.brown.BannerApplication;
import banner.brown.api.BannerAPI;
import banner.brown.models.Course;

public class CourseDetail extends BannerBaseLogoutTimerActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static String CRN_EXTRA = "crn_extra";
    public static String COURSE_NAME_EXTRA = "course_name_extra";

    private String mCrn = "";

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
    private ImageView mInCart;


    private RelativeLayout mBookListButton;
    private RelativeLayout mCriticalReviewButton;
    private RelativeLayout mCoursePreviewButton;
    private String mBookList;
    private String mCriticalReview;
    private String mCoursePreview;

    private SwipeRefreshLayout mSwipeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course_detail);

        BannerApplication.showLoadingIcon(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        mCrn = b.getString(CRN_EXTRA);
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
        mInCart = (ImageView) findViewById(R.id.in_cart);

        if (BannerApplication.mCurrentCart.getCourse(mCrn) != null) {
            mInCart.setVisibility(View.VISIBLE);
        }

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

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);


    }

    private void updateCourse() {
        BannerAPI.getCourseByCRN(mCrn, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mSwipeLayout.setRefreshing(false);
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
                BannerApplication.hideLoadingIcon();
                VolleyError x = error;
            }
        });
    }

    private void updateUIwithCourse() {
        mCRNText.setText( mCourse.getCRN());
        mTitleText.setText(mCourse.getTitle());
        mInstructorText.setText(mCourse.getInstructor());
        mScheduleText.setText(mCourse.getFormattedTime());
        mAvailableSeatsText.setText(""+mCourse.getSeatsAvailable());
        mTotalSeatsText.setText(" / "+mCourse.getSeatsTotal());
        mLocationText.setText(mCourse.getMeetingLocation());
        mDescriptionText.setText(mCourse.getDescription());
        mExamInfoText.setText(mCourse.getExamInfo());
        mPrereqText.setText(mCourse.getPrereq());

        mBookList = mCourse.getBookList();
        mCriticalReview = mCourse.getCriticialReview();
        mCoursePreview = mCourse.getCoursePreview();

        BannerApplication.hideLoadingIcon();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_to_cart) {
            BannerAPI.addToCart(mCrn, new Response.Listener<String>(){

                @Override
                public void onResponse(String response) {
                    if (response.toLowerCase().contains("success")) {
                        Intent main = new Intent(CourseDetail.this, MainActivity.class);
                        startActivity(main);

                    } else {
                        Toast.makeText(CourseDetail.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyError e = error;
                }
            });
            return true;
        } else if (id == R.id.remove_from_cart) {
            Course cartCourse = BannerApplication.mCurrentCart.getCourse(mCrn);
            if (!cartCourse.getRegistered()) {
                BannerAPI.removeFromCart(mCrn, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (response.toLowerCase().contains("success")) {
                            Intent main = new Intent(CourseDetail.this, MainActivity.class);
                            startActivity(main);

                        } else {
                            Toast.makeText(CourseDetail.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyError e = error;
                    }
                });
            } else {
                Toast.makeText(CourseDetail.this, "You can't remove a course you're registered for", Toast.LENGTH_SHORT).show();
            }
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.registered_check);
        if (item != null) {
            item.setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            Course cartCourse = BannerApplication.mCurrentCart.getCourse(mCrn);
            if (cartCourse != null) {
                if (cartCourse.getRegistered()) {
                    getMenuInflater().inflate(R.menu.menu_course_detail_registered, menu);

                } else {
                    getMenuInflater().inflate(R.menu.menu_course_detail_remove, menu);
                }


            } else {
                getMenuInflater().inflate(R.menu.menu_course_detail, menu);
            }
            return true;

    }


    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(true);
        updateCourse();
    }
}
