package banner.brown.models;

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





}
