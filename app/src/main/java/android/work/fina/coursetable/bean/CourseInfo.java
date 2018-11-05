package android.work.fina.coursetable.bean;

public class CourseInfo {

    private String coursname;
    private String courseinfo;
    private String place;

    public CourseInfo(String coursname, String courseinfo,String place) {
        this.coursname = coursname;
        this.courseinfo = courseinfo;
        this.place = place;
    }

    public String getCoursname() {
        return coursname;
    }

    public String getCourseinfo() {
        return courseinfo;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCoursname(String coursname) {
        this.coursname = coursname;
    }

    public void setCourseinfo(String courseinfo) {
        this.courseinfo = courseinfo;
    }
}
