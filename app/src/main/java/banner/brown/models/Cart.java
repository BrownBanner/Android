package banner.brown.models;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;

/**
 * Created by kappi on 4/1/15.
 */
public class Cart {

//    public final String name;
//    public final String term;
    private ArrayList<Course> courses;

    public Cart(){
//        name = cartName;
//        term = cartTerm;
        courses = new ArrayList<Course>();
    }

    public void addClass(Course course){
        courses.add(course);
    }

    public void removeClass(Course course){
        courses.remove(course);
    }

    public boolean hasClass(Course course) {return courses.contains(course);}


    public ArrayList<Course> getCourses(){
        return courses;
    }

    public Course getCourse(String crn) {
        for (Course course : courses){
            if (course.getCRN().equals(crn)) return course;
        }
        return null;
    }

    public Course getCourse(Course course) {
        int index = courses.indexOf(course);
        if (index == -1) {
            return null;
        } else {
            return courses.get(index);
        }
    }

    // Asks each course in cart for its corresponding calendar events and returns the whole cart
    public ArrayList<WeekViewEvent> getEventsOfCourses(){
        ArrayList<WeekViewEvent> toRet = new ArrayList<WeekViewEvent>();
        for (Course course : courses){
            ArrayList<WeekViewEvent> temp = course.getWeekViewEvent();
            for (WeekViewEvent each : temp){
                toRet.add(each);
            }
        }
        return toRet;
    }

}
