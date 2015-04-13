package banner.brown.models;

import com.alamkanak.weekview.WeekViewEvent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class representing a course
 */
public class Course {

    private String mTitle;
    private int mYear;
    private int mSemester; //1 for spring, 2 for fall
    private String mDescription;
    private String mCRN;
    private String mDepartment;


    public Course (JSONObject json) {

    }

    public Course(String title, int year, int semester, String description, String CRN, String department) {
        mTitle = title;
        mYear = year;
        mSemester = semester;
        mDescription = description;
        mCRN = CRN;
        mDepartment = department;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public ArrayList<WeekViewEvent> getWeekViewEvent(){

        ArrayList<WeekViewEvent> toRet = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, 1);
        startTime.set(Calendar.YEAR, 2015);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        //endTime.set(Calendar.MONTH, newMonth-1);
        WeekViewEvent event = new WeekViewEvent(1, "hi", startTime, endTime);
        //event.setColor(getResources().getColor(R.color.event_color_01));

        toRet.add(event);

        return toRet;
    }





}
