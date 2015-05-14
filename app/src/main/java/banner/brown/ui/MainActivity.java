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


    }



    public void updateCalendar(final boolean shouldShowLoading) {
        getSupportActionBar().setTitle(BannerApplication.curSelectedSemester.toString());
        if (shouldShowLoading) {
            BannerApplication.showLoadingIcon(this);
        }
        BannerAPI.getCurrentCourses(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (shouldShowLoading) {
                    BannerApplication.hideLoadingIcon();
                }
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
                BannerApplication.hideLoadingIcon();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        updateCalendar(true);
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
                        for (Course comparingCourse: tempCart.getCourses()){
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
                        tempCart.addClass(BannerApplication.mCurrentCart.getCourse(currentCourse));
                    }
                }
            }

        } catch (JSONException e) {

        }
        BannerApplication.mCurrentCart = tempCart;
        mWeekView.notifyDatasetChanged();
    }

    private int getNewColor() {
        BannerApplication.currColIndex ++;
        if (BannerApplication.currColIndex >= BannerApplication.mEventColors.size()) {BannerApplication.currColIndex = 0;}
        return BannerApplication.mEventColors.get(BannerApplication.currColIndex);
    }

    private void conflictAlert(ArrayList<WeekViewEvent> events) {
        //ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.BannerDrawerSection);s
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

    public NavigationDrawerFragment getNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }



}
