package com.satumaarit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;

/**
 * StudentRecord class
 * Contains the attributes, getters, setters for student record objects, and methods to manage the "student records" list.
 * @author satu
 */
public class StudentRecord {

    /**
     * attributes
     */
    private int recordId;
    private Student student;
    private Course course;
    private int grading;
    private String evaluationdate;

    /**
     * Class constructor
     */
    public StudentRecord() {
    }

    /**
     * Class constructor
     * @param student
     * @param course
     * @param grading
     * @param evaluation_date
     */
    public StudentRecord(Student student, Course course, int grading, String evaluation_date) {
        this.student = student;
        this.course = course;
        this.grading = grading;
        this.evaluationdate = evaluation_date;
    }

    /**
     * @return Student record's id
     */
    public int getRecordId() {
        return recordId;
    }
    
    /**
     * @return associated student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @return associated course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * @return grading
     */
    public int getGrading() {
        return grading;
    }

    /**
     * @return evaluation date
     */
    public String getEvaluationdate() {
        return evaluationdate;
    }

    /**
     * @param recordId_int
     */
    public void setRecordId(int recordId_int) {
        this.recordId = recordId_int;
    }
    
    /**
     * @param student_object
     */
    public void setStudent(Student student_object) {
        this.student = student_object;
    }

    /**
     * @param course_object
     */
    public void setCourse(Course course_object) { this.course = course_object; }

    /**
     * @param grading_int
     */
    public void setGrading(int grading_int) {
        this.grading = grading_int;
    }

    /**
     * @param evaluationdate_string
     */
    public void setEvaluationdate(String evaluationdate_string) {
        this.evaluationdate = evaluationdate_string;
    }


    /**
     *
     * Add a new student record: Creates a new object and adds it to the system.
     * @param c database connection
     * @return the updated list of all courses
     */
    public static ObservableList<StudentRecord> addStudentRecord(Connection c, Student student, Course course, int grading, String evaluationdate, ObservableList<StudentRecord> student_records) {
        // Generate id
        int id;

        if (student_records.size() == 0) {
            id = 1;
        } else {
            id = student_records.get(student_records.size() - 1).getRecordId() + 1;
        }

        StudentRecord record = new StudentRecord();
        record.setRecordId(id);
        record.setStudent(student);
        record.setCourse(course);
        record.setGrading(grading);
        record.setEvaluationdate(evaluationdate);

        // add to records list and db
        try {
            student_records.add(record);
            StudentRecordDAO.addStudentRecord(c, record);
            return student_records;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Update grade's information
     * @param c connection
     * @param studentRecordToUpdate record object to update
     * @param studentRecords list of student records
     * @return the updated list of student records
     */
    public static ObservableList<StudentRecord> updateStudentRecord(Connection c, StudentRecord studentRecordToUpdate, ObservableList<StudentRecord> studentRecords) {
        try {
            StudentRecordDAO.updateStudentRecord(c, studentRecordToUpdate);

            for (StudentRecord studentRecord : studentRecords) {
                if (studentRecord.getRecordId() == studentRecordToUpdate.getRecordId()) {
                    studentRecord.setEvaluationdate(studentRecordToUpdate.getEvaluationdate());
                    studentRecord.setGrading(studentRecordToUpdate.getGrading());
                    break;
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return studentRecords;
    }


    /**
     * Delete grade's information
     * @param c connection
     * @param studentRecordToDelete grade object to delete
     * @param studentRecords list of studentRecords
     * @return the updated list of studentRecords
     */
    public static ObservableList<StudentRecord> deleteGrade(Connection c, StudentRecord studentRecordToDelete, ObservableList<StudentRecord> studentRecords) {
        try {
            StudentRecordDAO.deleteStudentRecord(c, studentRecordToDelete);

            Iterator<StudentRecord> iterator = studentRecords.iterator();
            while (iterator.hasNext()) {
                StudentRecord studentRecord = iterator.next();
                if (studentRecord.getRecordId() == studentRecordToDelete.getRecordId()) {
                    iterator.remove();
                    break;
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return studentRecords;
    }

    /**
     * Search a student record by student's id
     * @param studentRecords list of all studentRecords
     * @param keyword the keyword (= student id) to search results for
     * @return filteredData the filtered list of studentRecords
     */
    public static ObservableList<StudentRecord> searchByStudentId(ObservableList<StudentRecord> studentRecords, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return studentRecords;
        }
        ObservableList<StudentRecord> filteredData = FXCollections.observableArrayList();
        try {
            int id = Integer.parseInt(keyword);
            for (StudentRecord studentRecord : studentRecords) {
                if (studentRecord.getStudent().getStudentId() == id) {
                    filteredData.add(studentRecord);
                }
            }
        } catch (NumberFormatException e) {
        }
        return filteredData;
    }

    /**
     * Search a student record by name
     * @param studentRecords list of studentRecords
     * @param keyword the keyword (= name to search results for)
     * @return filteredData the filtered list of courses
     */
    public static ObservableList<StudentRecord> searchByStudentName(ObservableList<StudentRecord> studentRecords, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return studentRecords;
        }
        ObservableList<StudentRecord> filteredData = FXCollections.observableArrayList();
        String lowerCaseKeyword = keyword.toLowerCase();
        for (StudentRecord studentRecord : studentRecords) {
            String fullname = (studentRecord.getStudent().getFirstname() + " " + studentRecord.getStudent().getLastname()).toLowerCase();
            if (fullname.equals(lowerCaseKeyword)) {
                filteredData.add(studentRecord);
            }
        }
        return filteredData;
    }

    /**
     * Search a student record by selected row object
     * @param studentRecords list of all studentRecords
     * @param selectedStudentRecord the student on selected table row
     * @return filteredData the filtered list of courses
     */
    public static ObservableList<StudentRecord> searchRecordForStudent(StudentRecord selectedStudentRecord, ObservableList<StudentRecord> studentRecords) {
        ObservableList<StudentRecord> filteredData = FXCollections.observableArrayList();

        for (StudentRecord studentRecord : studentRecords) {
            if (studentRecord.getStudent().getStudentId() == selectedStudentRecord.getStudent().getStudentId()) {
                filteredData.add(studentRecord);
            }
        }
        return filteredData;
    }
}
