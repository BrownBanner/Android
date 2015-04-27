package banner.brown.models;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Andy on 4/13/15.
 */
public class Semester {

    private static int totalSemestersToShow = 4;

    private int year;
    private int semester;

    //According to the API, "10" means fall and "20" means spring
    //so fall 2014 is 201410 and spring 2015 is 201420

    public Semester(int year, int semester) {
        this.year = year;
        this.semester = semester;
    }

    public Semester(Calendar c) {
        this.year = c.get(Calendar.YEAR);
        this.semester = getSemester(c.get(Calendar.MONTH));
    }

    private static int getSemester(int month) {
        if (month >= 0 && month < 6) {
            //Spring 2015 is represented as "201420"
            return 2;
        } else {
            return 1;
        }
    }

    public Semester(String semesterCode) {
        String semester = semesterCode.substring(4);
        if (semester.equals("20")) {
            this.semester = 2;
            this.year = Integer.valueOf(semesterCode.substring(0,4))+1;
        } else {
            this.semester = 1;
            this.year = Integer.valueOf(semesterCode.substring(0,4));
        }
    }

    public static Semester getCurrentSemester() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        return new Semester(year, getSemester(month));
    }

    public String getSemesterCode() {
        if (semester == 2) {
            //Spring 2015 is represented as "201420"
            return (year - 1) + "20";
        } else {
            return year + "10";
        }
    }

    public String toString() {
        if (semester == 2) {
            return "Spring " + year;
        } else {
            return "Fall " + year;
        }
    }

    @Override
    public boolean equals(Object o) {
        return ((Semester) o).getSemesterCode().equals(getSemesterCode());
    }

    public static ArrayList<Semester> getSemestersToChooseFrom() {
        ArrayList <Semester> toReturn = new ArrayList<Semester>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -18);
        //start 2 semesters ago up until next semester
        for (int i = 0; i < totalSemestersToShow; i++) {
            c.add(Calendar.MONTH, 6);
            toReturn.add(new Semester(c));
        }
        return toReturn;
    }
}
