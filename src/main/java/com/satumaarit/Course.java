package com.satumaarit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Course class to manage the course objects.
 * The attributes, getters, setters for course objects, and methods to manage the information of courses.
 * @author satu
 */
public class Course {

    /**
     * Attributes
    */
    private int courseId;
    private String name;
    private FieldOfStudy fieldOfStudy;
    private int credits;

    /**
     * Class constructor
     */
    public Course() {
    }

    /**
     * Class constructor
     * @param name
     * @param fieldOfStudy
     * @param credits
    */
    public Course(int id, String name, FieldOfStudy fieldOfStudy, int credits) {
        this.courseId = id;
        this.name = name;
        this.fieldOfStudy = fieldOfStudy;
        this.credits = credits;
    }

    /**
     * @return course's id
    */
    public int getCourseId() {
        return courseId;
    }

    /**
     * @return course's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return course's field of study
     */
    public FieldOfStudy getFieldOfStudy() {
        return fieldOfStudy;
    }

    /**
     * @return course's credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * @param courseId_int course's new id to set
    */
    public void setCourseId(int courseId_int) {
        this.courseId = courseId_int;
    }

    /**
     * @param name_str course's new name to set
     */
    public void setName(String name_str) {
        this.name = name_str;
    }

    /**
     * @param fieldOfStudy course's new field of study to set
     */
    public void setFieldOfStudy(FieldOfStudy fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    /**
     * @param credits_int course's new credits to set
     */
    public void setCredits(int credits_int) {
        this.credits = credits_int;
    }

    /**
     * Add a new course to the system.
     * @param c database connection
     * @param name course's name
     * @param fieldOfStudy course's field of study
     * @param credits course's credits
     * @param courses list of all courses
     * @return the updated list of all courses
     */
    public static ObservableList<Course> addCourse(Connection c, String name, FieldOfStudy fieldOfStudy, int credits, ObservableList<Course> courses) {
        // Generate id for new course
        int id;
        if (courses.size() == 0) {
            id = 1;
        }
        else {
            id = courses.get(courses.size() - 1).getCourseId() + 1;
        }

        Course course = new Course();
        course.setCourseId(id);
        course.setName(name);
        course.setFieldOfStudy(fieldOfStudy);
        course.setCredits(credits);

        // add to courses list and db
        try {
            CourseDAO.addCourse(c, course);
            courses.add(course);
            return courses;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Update a course's information.
     * @param c connection
     * @param courseToUpdate course object to update
     * @param courses list of all courses
     * @return the updated list of all courses
     */
    public static ObservableList<Course> updateCourse(Connection c, Course courseToUpdate, ObservableList<Course> courses) {
        try {
            CourseDAO.updateCourse(c, courseToUpdate);

            for (Course course : courses) {
                if (course.getCourseId() == courseToUpdate.getCourseId()) {
                    course.setName(courseToUpdate.getName());
                    course.setFieldOfStudy(courseToUpdate.getFieldOfStudy());
                    course.setCredits(courseToUpdate.getCredits());
                    break;
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courses;
    }

    /**
     * Delete a course's information.
     * @param c connection
     * @param courseToDelete course object to delete
     * @param courses list of all courses
     * @return the updated list of all courses
     */
    public static ObservableList<Course> deleteCourse(Connection c, Course courseToDelete, ObservableList<Course> courses) {
        try {
            CourseDAO.deleteCourse(c, courseToDelete);

            Iterator<Course> iterator = courses.iterator();
            while (iterator.hasNext()) {
                Course course = iterator.next();
                if (course.getCourseId() == courseToDelete.getCourseId()) {
                    iterator.remove();
                    break;
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courses;
    }

    /**
     * Search a course by id.
     * @param courses list of all courses
     * @param keyword the keyword (= id to search results for)
     * @return the filtered list of courses
     */
    public static ObservableList<Course> searchByCourseId(ObservableList<Course> courses, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return courses;
        }
        ObservableList<Course> filteredData = FXCollections.observableArrayList();
        try {
            int id = Integer.parseInt(keyword);
            for (Course course : courses) {
                if (course.getCourseId() == id) {
                    filteredData.add(course);
                }
            }
        } catch (NumberFormatException e) {
        }
        return filteredData;
    }

    /**
     * Search courses by name.
     * @param courses list of all courses
     * @param keyword the keyword (= name to search results for)
     * @return the filtered list of courses
     */
    public static ObservableList<Course> searchByCourseName(ObservableList<Course> courses, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return courses;
        }
        ObservableList<Course> filteredData = FXCollections.observableArrayList();
        String lowerCaseKeyword = keyword.toLowerCase();
        for (Course course : courses) {
            if (course.getName().toLowerCase().contains(lowerCaseKeyword)) {
                filteredData.add(course);
            }
        }
        return filteredData;
    }

    /**
     * @return the name of a field of study
     */
    public String toString() {
        return this.getName();
    }
}
