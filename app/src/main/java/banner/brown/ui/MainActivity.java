package banner.brown.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import banner.brown.BannerApplication;
import banner.brown.api.BannerAPI;
import banner.brown.models.Cart;
import banner.brown.models.Course;


public class MainActivity extends BannerBaseLogoutTimerActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener {


    private NavigationDrawerFragment mNavigationDrawerFragment;
    private WeekView mWeekView;

    private ArrayList<Integer> mEventColors;
    private int currColIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mWeekView = (WeekView)findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setMonthChangeListener(this);

        if (BannerApplication.mCurrentCart != null){
            BannerApplication.mCurrentCart = new Cart();
        }

        setUpColors();

    }

    private void setUpColors() {


        mEventColors = new ArrayList<>();

        mEventColors.add(Color.rgb(127,255,212));
        mEventColors.add(Color.rgb(210, 77, 87));
        mEventColors.add(Color.rgb(217, 30, 24));
        mEventColors.add(Color.rgb(239, 72, 54));
        mEventColors.add(Color.rgb(192, 57, 43));
        mEventColors.add(Color.rgb(231, 76, 60));
        mEventColors.add(Color.rgb(246, 71, 71));
        mEventColors.add(Color.rgb(210, 82, 127));
        mEventColors.add(Color.rgb(246, 36, 89));
        mEventColors.add(Color.rgb(220, 198, 224));
        mEventColors.add(Color.rgb(103, 65, 114));
        mEventColors.add(Color.rgb(145, 61, 136));
        mEventColors.add(Color.rgb(191, 85, 236));
        mEventColors.add(Color.rgb(142, 68, 173));
        mEventColors.add(Color.rgb(155, 89, 18));
        mEventColors.add(Color.rgb(268,108,179));
        mEventColors.add(Color.rgb(65, 131, 215));
        mEventColors.add(Color.rgb(129, 207, 224));
        mEventColors.add(Color.rgb(197, 239, 247));
        mEventColors.add(Color.rgb(52, 152, 219));
        mEventColors.add(Color.rgb(25, 181, 254));
        mEventColors.add(Color.rgb(34, 49, 63));
        mEventColors.add(Color.rgb(30, 139, 195));
        mEventColors.add(Color.rgb(52, 73, 94));
        mEventColors.add(Color.rgb(37, 116, 169));
        mEventColors.add(Color.rgb(137, 196, 244));
        mEventColors.add(Color.rgb(92, 151, 191));
        mEventColors.add(Color.rgb(162, 222, 208));
        mEventColors.add(Color.rgb(144, 198, 149));
        mEventColors.add(Color.rgb(3, 201, 169));
        mEventColors.add(Color.rgb(101, 198, 187));
        mEventColors.add(Color.rgb(27, 163, 156));
        mEventColors.add(Color.rgb(54, 215, 183));
        mEventColors.add(Color.rgb(134, 226, 213));
        mEventColors.add(Color.rgb(22, 160, 133));
        mEventColors.add(Color.rgb(81, 152, 117));
        mEventColors.add(Color.rgb(77, 175, 124));
        mEventColors.add(Color.rgb(50, 177, 106));
        mEventColors.add(Color.rgb(4, 147, 114));
        mEventColors.add(Color.rgb(253, 227, 167));
        mEventColors.add(Color.rgb(35, 149, 50));
        mEventColors.add(Color.rgb(44, 179, 80));
        mEventColors.add(Color.rgb(235, 151, 78));
        mEventColors.add(Color.rgb(211, 84, 2));
        mEventColors.add(Color.rgb(249, 105, 14));
        mEventColors.add(Color.rgb(242, 121, 53));
        mEventColors.add(Color.rgb(236,236,236));
        mEventColors.add(Color.rgb(210, 215, 211));
        mEventColors.add(Color.rgb(189, 195, 199));
        mEventColors.add(Color.rgb(149, 165, 166));
        mEventColors.add(Color.rgb(171, 183, 183));
        mEventColors.add(Color.rgb(191, 191, 191));

        Collections.shuffle(mEventColors);


    }

    @Override
    public void onResume() {
        super.onResume();
        BannerAPI.getCurrentCourses(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject x = response;
                try {
                    processClasses(x.getJSONArray("items"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {

            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.course_detail) {

            mWeekView.notifyDatasetChanged();

            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(MainActivity.this, "Clicked " + event.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(MainActivity.this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        if (newMonth == 1) {
            return BannerApplication.mCurrentCart.getEventsOfCourses();
        }
        return new ArrayList<>();
    }

    private void processClasses(JSONArray classes) {
        final Cart tempCart = new Cart();
        try {
            for (int i = 0; i < classes.length(); i++) {
                JSONObject course = classes.getJSONObject(i);

                BannerAPI.getCourseByCRN(course.getString("crn"),new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Course currentCourse = null;
                        try {
                            currentCourse = new Course(response.getJSONArray("items").getJSONObject(0));

                            if (!BannerApplication.mCurrentCart.hasClass(currentCourse.getCRN())){
                                currentCourse.setColor(getNewColor());
                                tempCart.addClass(currentCourse);
                                mWeekView.notifyDatasetChanged();
                            }
                            else {
                                tempCart.addClass(BannerApplication.mCurrentCart.getCourse(currentCourse.getCRN()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyError x = error;
                    }
                });
            }

        } catch (JSONException e) {

        }
        BannerApplication.mCurrentCart = tempCart;
        mWeekView.notifyDatasetChanged();
    }

    private int getNewColor() {
        currColIndex ++;
        if (currColIndex >= mEventColors.size()) {currColIndex = 0;}
        return mEventColors.get(currColIndex);
    }


}
