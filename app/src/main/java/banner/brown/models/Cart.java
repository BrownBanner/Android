package banner.brown.models;

import java.util.ArrayList;

/**
 * Created by kappi on 4/1/15.
 */
public class Cart {

    public final String name;
    public final String term;
    private ArrayList<Course> courses;

    public Cart(String cartName, String cartTerm){
        name = cartName;
        term = cartTerm;
        courses = new ArrayList<Course>();
    }

    public void addClass(Course course){
        courses.add(course);
    }

    public void removeClass(Course course){
        courses.remove(course);
    }

    public ArrayList<Course> getCourses(){
        return courses;
    }



}
