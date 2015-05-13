package banner.brown.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        setUpColors();

    }

    private void setUpColors() {


        mEventColors = new ArrayList<>();

        mEventColors.add(Color.rgb(26, 188, 156));
        mEventColors.add(Color.rgb(46, 204, 113));
        mEventColors.add(Color.rgb(52, 152, 219));
        mEventColors.add(Color.rgb(155, 89, 182));
        mEventColors.add(Color.rgb(52, 73, 94));
        mEventColors.add(Color.rgb(241, 196, 15));
        mEventColors.add(Color.rgb(230, 126, 34));
        mEventColors.add(Color.rgb(231, 76, 60));
        mEventColors.add(Color.rgb(118, 47, 0));
        mEventColors.add(Color.rgb(149, 165, 166));
        mEventColors.add(Color.rgb(22, 160, 133));
        mEventColors.add(Color.rgb(39, 174, 96));
        mEventColors.add(Color.rgb(41, 128, 185));
        mEventColors.add(Color.rgb(142, 68, 173));
        mEventColors.add(Color.rgb(44, 62, 80));
        mEventColors.add(Color.rgb(243, 156, 18));
        mEventColors.add(Color.rgb(211, 84, 0));
        mEventColors.add(Color.rgb(192, 57, 43));
        mEventColors.add(Color.rgb(189, 195, 199));
        mEventColors.add(Color.rgb(127, 140, 141));
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(BannerApplication.curSelectedSemester.toString());
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

        BannerAPI.getNamedCarts(new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                BannerApplication.updateNamedCarts(response);
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

        if (id == R.id.search_courses) {
            Intent search = new Intent(this, SearchActivity.class);
            startActivity(search);
        }

//        if (id == R.id.course_detail) {
//
//            mWeekView.notifyDatasetChanged();
//
//            Intent login = new Intent(this, LoginActivity.class);
//            startActivity(login);
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        if (event.getBuds() == null){
            Intent i = new Intent(this, CourseDetail.class);
            String CRN = event.getId();
            String name = event.getName();
            i.putExtra(CourseDetail.CRN_EXTRA, CRN);
            i.putExtra(CourseDetail.COURSE_NAME_EXTRA, name );
            startActivity(i);
        }
        else{
            WeekView.CollisionBuddies buds = event.getBuds();
            conflictAlert(buds.events);
        }

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(MainActivity.this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
        BannerApplication.mCurrentCart.getCourse(event.getId()).setAsUnregistered();
        mWeekView.notifyDatasetChanged();
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        if (newMonth == 1) {
            if (BannerApplication.mCurrentCart != null){
                return BannerApplication.mCurrentCart.getEventsOfCourses();
            }
        }
        return new ArrayList<>();
    }


    private void processClasses(JSONArray classes) {
        final Cart tempCart = new Cart();
        try {
            for (int i = 0; i < classes.length(); i++) {
                JSONObject course = classes.getJSONObject(i);

                Course currentCourse = new Course(course);

                if (!tempCart.hasClass(currentCourse)) {

                    if (!BannerApplication.mCurrentCart.hasClass(currentCourse)) {
                        boolean colorSet = false;
                        for (Course comparingCourse: BannerApplication.mCurrentCart.getCourses()){
                            if (comparingCourse.getCRN().equals(currentCourse.getCRN())){
                                currentCourse.setColor(comparingCourse.getColor());
                                colorSet = true;
                            }
                        }
                        if (!colorSet) currentCourse.setColor(getNewColor());
                        //currentCourse.setAsUnregistered();
                        tempCart.addClass(currentCourse);
                        mWeekView.notifyDatasetChanged();
                    } else {
                        tempCart.addClass(BannerApplication.mCurrentCart.getCourse(currentCourse.getCRN()));
                    }
                }
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

    private void conflictAlert(ArrayList<WeekViewEvent> events) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.BannerDrawerSection);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        alertDialogBuilder.setTitle("Choose a class");

        final CharSequence[] classes = new CharSequence[events.size()-1];
        final String[] crns = new String[events.size()-1];

        for (int i = 0; i < events.size()-1; i++) classes[i] = events.get(i).getName();
        for (int i = 0; i < events.size()-1; i++) crns[i] = events.get(i).getId();

        alertDialogBuilder.setItems(classes,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                Intent i = new Intent(MainActivity.this, CourseDetail.class);
                String CRN = crns[id];
                String name = classes[id].toString();
                i.putExtra(CourseDetail.CRN_EXTRA, CRN);
                i.putExtra(CourseDetail.COURSE_NAME_EXTRA, name );
                startActivity(i);
            }

        });



        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }



}
