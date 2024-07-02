package com.satumaarit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * database CRUD operations for students
 */
public class StudentDAO
{

    /**
     * CREATE a new student to db
     * @param c connection
     * @param student object to create
     * @throws SQLException
     */
    public static void addStudent(Connection c, Student student) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO Student (Firstname, Lastname, Homeaddress, Postalcode, City, Mail, Phone) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?)");

        ps.setString(1, student.getFirstname());
        ps.setString(2, student.getLastname());
        ps.setString(3, student.getHomeaddress());
        ps.setInt(4, Integer.parseInt(student.getPostalcode()));
        ps.setString(5, student.getCity());
        ps.setString(6, student.getMail());
        ps.setString(7, student.getPhone());
        ps.execute();
        System.out.println("\t>> A new student was added to db: " + student.getFirstname() + " " + student.getLastname());
    }

    /**
     * UPDATE a student in db
     * @param c connection
     * @param student object to update
     * @throws SQLException
     */
    public static void updateStudent(Connection c, Student student) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "UPDATE  Student SET Firstname = ?, Lastname = ?, Homeaddress = ?, Postalcode = ?, City = ?, Mail = ?, Phone = ? WHERE StudentId = " + student.getStudentId());

        ps.setString(1, student.getFirstname());
        ps.setString(2, student.getLastname());
        ps.setString(3, student.getHomeaddress());
        ps.setInt(4, Integer.parseInt(student.getPostalcode()));
        ps.setString(5, student.getCity());
        ps.setString(6, student.getMail());
        ps.setString(7, student.getPhone());
        ps.executeUpdate();
        System.out.println("\t>> Information of student " + student.getFirstname() + " " + student.getLastname() + " was updated to db.");
    }

    /**
     * DELETE a student's information from db (also deletes their grade record from grades table)
     * @param c connection
     * @param student object to delete
     * @throws SQLException
     */
    public static void deleteStudent(Connection c, Student student) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "DELETE FROM Student WHERE StudentId = " + student.getStudentId() + ";");
        ps.execute();
        System.out.println("\t>> Information of student " + student.getFirstname() + " " + student.getLastname() + " was deleted from database.");
    }

    /**
     * READ students from db
     * @param c connection
     * @return list of students
     * @throws SQLException
     */
    public static ObservableList<Student> readAllStudents(Connection c) throws SQLException {
        ObservableList<Student> students = FXCollections.observableArrayList();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT StudentId, Firstname, Lastname, Homeaddress, Postalcode, City, Mail, Phone FROM Student"
        );

        while (rs.next()) {
            Student student = new Student();

            int id = rs.getInt("StudentId");
            student.setStudentId(id);

            String etunimi = rs.getString("Firstname");
            student.setFirstname(etunimi);

            String sukunimi = rs.getString("Lastname");
            student.setLastname(sukunimi);

            String katuosoite = rs.getString("Homeaddress");
            student.setHomeaddress(katuosoite);

            int pn = rs.getInt("Postalcode");
            String postinumero = String.valueOf(pn);
            student.setPostalcode(postinumero);

            String kaupunki = rs.getString("City");
            student.setCity(kaupunki);

            String mail = rs.getString("Mail");
            student.setMail(mail);

            String phone = rs.getString("Phone");
            student.setPhone(phone);

            students.add(student);
        }
        System.out.println("\t>> Students table was read from db.");
        return students;
    }

}
