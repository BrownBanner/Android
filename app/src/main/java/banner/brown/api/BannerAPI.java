package banner.brown.api;

import java.util.List;

import banner.brown.models.Course;

/**
 * Created by Andy on 2/16/15.
 */
public class BannerAPI {
    public static Course getCourse(String CRN) {
        return new Course("Test Course", 2014, 1, "Test Description", "123", "DEPT");
    }
}
