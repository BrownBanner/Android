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


    public Course (JSONObject json) {
        try {
            mTitle = json.getString("title");
            mDepartment = json.getString("dept");
            mCRN = json.getString("crn");
            mSubjectCode = json.getString("subjectc");
        } catch(JSONException e) {

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




}
