package com.satumaarit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * database CRUD operations for courses
 */
public class CourseDAO {

    /**
     * CREATE a new course to db
     * @param c connection
     * @param course object to create
     * @throws SQLException
     */
    public static void addCourse(Connection c, Course course) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO Course (Name, FieldOfStudyId, Credits) "
                        + "VALUES(?, ?, ?)");

        ps.setString(1, course.getName());
        ps.setInt(2, course.getFieldOfStudy().getFieldOfStudyId());
        ps.setInt(3, course.getCredits());
        ps.execute();
        System.out.println("\t>> A new course was added to db: " + course.getName());
    }

    /**
     *
     * UPDATE a course in db
     * @param c connection
     * @param course object to update
     * @throws SQLException
     */
    public static void updateCourse(Connection c, Course course) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "UPDATE  Course SET Name = ?, FieldOfStudyId = ?, Credits = ? WHERE CourseId = " + course.getCourseId());

        ps.setString(1, course.getName());
        ps.setInt(2, course.getFieldOfStudy().getFieldOfStudyId());
        ps.setInt(3, course.getCredits());
        ps.executeUpdate();
        System.out.println("\t>> Information of course " + course.getName() + " was updated in db.");
    }

    /**
     *
     * DELETE a course from db
     * @param c connection
     * @param course object to delete
     * @throws SQLException
     */
    public static void deleteCourse(Connection c, Course course) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "DELETE FROM Course WHERE CourseId = " + course.getCourseId() + ";");

        ps.execute();
        System.out.println("\t>> Information of course " + course.getName() + " was deleted from db.");
    }

    /**
     *
     * READ courses from db
     * @param c connection
     * @return list of courses
     * @throws SQLException
     */
    public static ObservableList<Course> readAllCourses(Connection c) throws SQLException {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT c.CourseId, c.Name AS CourseName, c.Credits, f.FieldOfStudyId, f.Name AS FieldOfStudyName " +
                        "FROM Course c " +
                        "INNER JOIN FieldOfStudy f ON c.FieldOfStudyId = f.FieldOfStudyId"
        );

        while (rs.next()) {
            Course course = new Course();
            FieldOfStudy fieldOfStudy = new FieldOfStudy();

            int id = rs.getInt("CourseId");
            course.setCourseId(id);

            String nimi = rs.getString("CourseName");
            course.setName(nimi);

            int tieteenalaId = rs.getInt("FieldOfStudyId");
            fieldOfStudy.setFieldOfStudyId(tieteenalaId);

            String tieteenalaNimi = rs.getString("FieldOfStudyName");
            fieldOfStudy.setName(tieteenalaNimi);

            int op = rs.getInt("Credits");
            course.setCredits(op);

            course.setFieldOfStudy(fieldOfStudy);
            courses.add(course);
        }

        System.out.println("\t>> Courses table was read from db.");
        return courses;
    }
}
