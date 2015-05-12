package banner.brown.models;

import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class representing a course
 */

//https://ords-qa.services.brown.edu:8443/pprd/banner/mobile/cartbyid?term=201420&in_id=100445912
    //getting a cart

public class Course {

    private String mTitle;
    private int mYear;
    private int mSemester; //1 for spring, 2 for fall
    private String mDescription;
    private String mCRN;
    private String mDepartment;
    private String mSubjectCode;
    private String mInstructor;
    private int mSeatsAvailable;
    private int mSeatsTotal;
    private String mMeetingTime;
    private String mMeetingLocation;
    private String mBookList;
    private String mCriticalReview;
    private String mCoursePreview;
    private String mExamInfo;
    private String mPrereq;
    private int mColor = 0;
    private boolean mRegistered;

    public Course (JSONObject json) {
        try {
            mTitle = json.getString("title");
            mDepartment = json.getString("dept");
            mCRN = json.getString("crn");
            mSubjectCode = json.getString("subjectc");
            mInstructor = json.getString("instructor");
            int takenSeats = json.getInt("actualreg");
            mSeatsTotal = json.getInt("maxregallowed");
            mSeatsAvailable = mSeatsTotal - takenSeats;
            mMeetingTime = json.getString("meetingtime");
            mMeetingLocation = json.getString("meetinglocation");
            mBookList = json.getString("booklist");
            mDescription = json.getString("description");
            mCriticalReview = json.getString("critical_review");
            mCoursePreview = json.getString("course_preview");
            mPrereq = json.optString("prereq", "No prerequisites");
            mRegistered = json.getString("reg_indicator").equals("Y");

        } catch(JSONException e) {
            JSONException z = e;
        }

    }

    public Course(String title, int year, int semester, String description, String CRN, String department, String meettime) {
        mTitle = title;
        mYear = year;
        mSemester = semester;
        mDescription = description;
        mCRN = CRN;
        mDepartment = department;
        mMeetingTime = meettime;
    }

    public void setAsRegistered(){
        mRegistered = true;
        setColor(mColor);

    }

    @Override
    public boolean equals(Object o) {
        if (((Course) o).getCRN().equals(getCRN())) {
            return true;
        } else {
            return false;
        }
    }

    public void setAsUnregistered(){
        mRegistered = false;
        setColor(mColor);

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

        //Format: TR 0900-1020

        String meetingTimeString = this.getMeetingTime();
        String[] split = meetingTimeString.split("\\s+");

        char[] days = split[split.length-2].toCharArray();
        String time = split[split.length-1];

        ArrayList<WeekViewEvent> toRet = new ArrayList<WeekViewEvent>();

        for (int i = 0; i < days.length;i++){

            int OFFSET = 8;

            int day = 0;

            switch (days[i]){
                case 'M': day = 2;
                    break;
                case 'T': day = 3;
                    break;
                case 'W': day = 4;
                    break;
                case 'R': day = 5;
                    break;
                case 'F': day = 6;
                    break;
            }

            Calendar startTime = Calendar.getInstance();
            String[] times = time.split("-");

            startTime.set(Calendar.DAY_OF_MONTH, day);

            startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0].substring(0,2)) - OFFSET);
            startTime.set(Calendar.MINUTE, Integer.parseInt(times[0].substring(2,4)));

            startTime.set(Calendar.MONTH, 1);
            startTime.set(Calendar.YEAR, 2015);

            Calendar endTime = (Calendar) startTime.clone();

            endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[1].substring(0,2)) - OFFSET);
            endTime.set(Calendar.MINUTE, Integer.parseInt(times[1].substring(2,4)));
            endTime.set(Calendar.MONTH, 1);
            WeekViewEvent event = new WeekViewEvent(getCRN(), getTitle(), startTime, endTime);
            event.setColor(mColor);

            toRet.add(event);
        }

        return toRet;
    }

    public String getCRN() {
        return mCRN;
    }

    public String getSubjectCode() {
        return mSubjectCode;
    }

    public String getMeetingTime() {
        return mMeetingTime;
    }

    public String getMeetingLocation() {
        return mMeetingLocation;
    }

    public int getSeatsAvailable() {
        return mSeatsAvailable;
    }

    public int getSeatsTotal() {
        return mSeatsTotal;
    }

    public String getInstructor() {
        return mInstructor;
    }

    public String getCriticialReview() {
        return mCriticalReview;
    }

    public String getCoursePreview() {
        return mCoursePreview;
    }

    public String getExamInfo() {
        return mExamInfo;
    }

    public String getBookList() {return mBookList;}

    public String getPrereq() {return mPrereq;}

    public int getColor() {
        return mColor;
    }

    public void setColor(int col){
        if (mRegistered) {
            mColor = saturate(col);
        }
        else{
            mColor = desaturate(col);
        }
    }

    private int desaturate(int col){
        float[] hsv = new float[3];
        Color.colorToHSV(col,hsv);
        hsv[1] = (float)0.2;
        return Color.HSVToColor(hsv);
    }

    private int saturate(int col){
        float[] hsv = new float[3];
        Color.colorToHSV(col,hsv);
        hsv[1] = (float)1;
        return Color.HSVToColor(hsv);
    }
}
