package com.satumaarit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * database CRUD operations for student records
 */
public class StudentRecordDAO {

    /**
     * CREATE a new student record to db
     * @param c connection
     * @param studentRecord object to create
     * @throws SQLException
     */
    public static void addStudentRecord(Connection c, StudentRecord studentRecord) throws SQLException {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date utilDate = inputFormat.parse(studentRecord.getEvaluationdate());
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO StudentRecord (StudentId, CourseId, Grading, EvaluationDate) "
                            + "VALUES(?, ?, ?, ?)");

            ps.setInt(1, studentRecord.getStudent().getStudentId());
            ps.setInt(2, studentRecord.getCourse().getCourseId());
            ps.setInt(3, studentRecord.getGrading());
            ps.setString(4, sqlDate.toString());

            ps.execute();
            System.out.println("\t>> A new grading was added for student " + studentRecord.getStudent().getFirstname() + " " + studentRecord.getStudent().getLastname() + "."
                    + " Completed course: " + studentRecord.getCourse().getName() + ", grading: " + studentRecord.getGrading() + " evaluation date: " + studentRecord.getEvaluationdate());
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    /**
     * UPDATE a student record in db
     * @param c connection
     * @param studentRecord object to update
     * @throws SQLException
     */
    public static void updateStudentRecord(Connection c, StudentRecord studentRecord) throws SQLException {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date utilDate = inputFormat.parse(studentRecord.getEvaluationdate());
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            PreparedStatement ps = c.prepareStatement(
                    "UPDATE  StudentRecord SET Grading = ?, EvaluationDate = ? WHERE RecordId = " + studentRecord.getRecordId());

            ps.setInt(1, studentRecord.getGrading());
            ps.setString(2, sqlDate.toString());

            ps.executeUpdate();
            System.out.println("\t>> Student " + studentRecord.getStudent().getFirstname() + " " + studentRecord.getStudent().getLastname()
                    + "'s grading for course " + studentRecord.getCourse().getName() + " was updated to db.");
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    /**
     * DELETE a student record from db
     * @param c connection
     * @param studentRecord object to delete
     * @throws SQLException
     */
    public static void deleteStudentRecord(Connection c, StudentRecord studentRecord) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "DELETE FROM StudentRecord WHERE RecordId = " + studentRecord.getRecordId() + ";");

        ps.execute();
        System.out.println("\t>> Student " + studentRecord.getStudent().getFirstname() + " " + studentRecord.getStudent().getLastname()
                + "'s grading for course " + studentRecord.getCourse().getName() + " was deleted from db.");
    }

    /**
     * READ student records from db
     * @param c connection
     * @return list of student records
     * @throws SQLException
     */
    public static ObservableList<StudentRecord> readAllStudentRecords(Connection c) throws SQLException {
        ObservableList<StudentRecord> studentRecords = FXCollections.observableArrayList();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT g.RecordId, g.StudentId, g.CourseId, g.Grading, g.EvaluationDate, " +
                        "s.Firstname AS Firstname, s.Lastname AS Lastname, c.Name AS Course, c.Credits AS Credits " +
                        "FROM StudentRecord g " +
                        "JOIN Student s ON g.StudentId = s.StudentId " +
                        "JOIN Course c ON g.CourseId = c.CourseId"
        );

        while (rs.next()) {
            StudentRecord studentRecord = new StudentRecord();
            Student student = new Student();
            Course course = new Course();

            int id = rs.getInt("RecordId");
            studentRecord.setRecordId(id);

            int studentId = rs.getInt("StudentId");
            student.setStudentId(studentId);

            int courseId = rs.getInt("CourseId");
            course.setCourseId(courseId);

            int grading = rs.getInt("Grading");
            studentRecord.setGrading(grading);

            String evaluationdate = rs.getString("EvaluationDate");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate localDate = LocalDate.parse(evaluationdate);
            String formattedDate = localDate.format(formatter);
            studentRecord.setEvaluationdate(formattedDate);


            String firstname = rs.getString("Firstname");
            student.setFirstname(firstname);

            String lastname = rs.getString("Lastname");
            student.setLastname(lastname);

            String coursename = rs.getString("Course");
            course.setName(coursename);

            int credits = rs.getInt("Credits");
            course.setCredits(credits);

            studentRecord.setStudent(student);
            studentRecord.setCourse(course);
            studentRecords.add(studentRecord);
        }
        System.out.println("\t>> Student records table was read from db.");
        return studentRecords;
        }
}
