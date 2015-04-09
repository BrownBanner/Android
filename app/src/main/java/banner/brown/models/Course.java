package banner.brown.models;

import org.json.JSONException;
import org.json.JSONObject;

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
        } catch(JSONException e) {
            JSONException z = e;
        }

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

}
