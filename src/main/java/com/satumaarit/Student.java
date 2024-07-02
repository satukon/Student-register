package com.satumaarit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Student class
 * Contains the attributes, getters, setters for student objects, and methods to manage the "students" list.
 * @author satu
 */
public class Student {
    /**
     * attributes
     */
    private int studentId;
    private String firstname;
    private String lastname;
    private String homeaddress;
    private String postalcode;
    private String city;
    private String mail;
    private String phone;

    /**
     * Constructor
     */
    public Student() {
    }

    /**
     * Constructor
     * @param firstname
     * @param lastname
     * @param homeaddress
     * @param city
     * @param mail
     * @param phone
     */
    public Student(int id, String firstname, String lastname, String homeaddress, String postalcode, String city, String mail, String phone) {
        this.studentId = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.homeaddress = homeaddress;
        this.postalcode = postalcode;
        this.city = city;
        this.mail = mail;
        this.phone = phone;
    }

    /**
     * @return student's id
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * @return student's first name
     */
    public String getFirstname() { return firstname; }

    /**
     * @return student's last name
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @return student's address
     */
    public String getHomeaddress() {
        return homeaddress;
    }

    /**
     * @return student's city
     */
    public String getCity() {
        return city;
    }

    /**
     * @return student's postal code
     */
    public String getPostalcode() {
        return postalcode;
    }

    /**
     * @return student's e-mail address
     */
    public String getMail() {
        return mail;
    }

    /**
     * @return student's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param studentId_int student id to set
     */
    public void setStudentId(int studentId_int) { this.studentId = studentId_int; }

    /**
     * @param firstname_str student's first name to set
     */
    public void setFirstname(String firstname_str) {
        this.firstname = firstname_str;
    }

    /**
     * @param lastname_str student's last name to set
     */
    public void setLastname(String lastname_str) {
        this.lastname = lastname_str;
    }

    /**
     * @param homeddress_str student's home address to set
     */
    public void setHomeaddress(String homeddress_str) {
        this.homeaddress = homeddress_str;
    }

    /**
     * @param postalcode_str student's postal code to set
     */
    public void setPostalcode(String postalcode_str) {
        this.postalcode = postalcode_str;
    }

    /**
     * @param city_str student's city to set
     */
    public void setCity(String city_str) {
        this.city = city_str;
    }

    /**
     * @param mail_str student's e-mail address to set
     */
    public void setMail(String mail_str) {
        this.mail = mail_str;
    }

    /**
     * @param phone_str student's phone number to set
     */
    public void setPhone(String phone_str) {
        this.phone = phone_str;
    }

    /**
     * Add a new student: Creates a new student object and adds it to the system.
     * @param c database connection
     * @param firstname student's first name
     * @param lastname student's last name
     * @param homeaddress student's home address
     * @param postalcode student's postal code
     * @param city student's city
     * @param mail student's e-mail address
     * @param phone student's phone number
     * @param students list of all students
     * @return the updated list of all students
     */
    public static ObservableList<Student> addStudent(Connection c, String firstname, String lastname, String homeaddress, String postalcode, String city, String mail, String phone, ObservableList<Student> students) {
        // Generate id
        int id;
        if (students.size() == 0) {
            id = 1;
        }
        else {
            id = students.get(students.size() - 1).getStudentId() + 1;
        }

        // generate new Student object
        Student student = new Student();
        student.setStudentId(id);
        student.setFirstname(firstname);
        student.setLastname(lastname);
        student.setHomeaddress(homeaddress);
        student.setPostalcode(postalcode);
        student.setCity(city);
        student.setMail(mail);
        student.setPhone(phone);

        // add new information to students list and db
        try {
            StudentDAO.addStudent(c, student);
            students.add(student);
            return students;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Update a student's information
     * @param c connection
     * @param studentToUpdate student object to update
     * @param students list of all students
     * @return the updated list of all students
     */
    public static ObservableList<Student> updateStudent(Connection c, Student studentToUpdate, ObservableList<Student> students) {
        try {
            StudentDAO.updateStudent(c, studentToUpdate);

            for (Student student : students) {
                if (student.getStudentId() == studentToUpdate.getStudentId()) {
                    student.setFirstname(studentToUpdate.getFirstname());
                    student.setLastname(studentToUpdate.getLastname());
                    student.setHomeaddress(studentToUpdate.getHomeaddress());
                    student.setPostalcode(studentToUpdate.getPostalcode());
                    student.setCity(studentToUpdate.getCity());
                    student.setMail(studentToUpdate.getMail());
                    student.setPhone(studentToUpdate.getPhone());
                    break;
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return students;
    }

    /**
     * Delete student's information
     * @param c connection
     * @param studentToDelete student object to delete
     * @param students list of all students
     * @return the updated list of all students
     */
    public static ObservableList<Student> deleteStudent(Connection c, Student studentToDelete, ObservableList<Student> students) {
        try {
            StudentDAO.deleteStudent(c, studentToDelete);

            Iterator<Student> iterator = students.iterator();
            while (iterator.hasNext()) {
                Student student = iterator.next();
                if (student.getStudentId() == studentToDelete.getStudentId()) {
                    iterator.remove();
                    break;
                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return students;
    }

    /**
     * Search student by id
     * @param students list of students
     * @param keyword the keyword (= id to search results for)
     * @return filteredData the filtered list of students
     */
    public static ObservableList<Student> searchByStudentId(ObservableList<Student> students, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return students;
        }
        ObservableList<Student> filteredData = FXCollections.observableArrayList();
        try {
            int id = Integer.parseInt(keyword);
            for (Student student : students) {
                if (student.getStudentId() == id) {
                    filteredData.add(student);
                }
            }
        } catch (NumberFormatException e) {
        }
        return filteredData;
    }

    /**
     *
     * Search student by name
     * @param students list of students
     * @param keyword the keyword (= name to search results for)
     * @return filteredData the filtered list of students
     */
    public static ObservableList<Student> searchByStudentName(ObservableList<Student> students, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return students;
        }
        ObservableList<Student> filteredData = FXCollections.observableArrayList();
        String lowerCaseKeyword = keyword.toLowerCase();
        for (Student student : students) {
            String fullname = (student.getFirstname() + " " + student.getLastname()).toLowerCase();
            if (fullname.contains(lowerCaseKeyword)) {
                filteredData.add(student);
            }
        }
        return filteredData;
    }

    /**
     * @return the full name of a student
     */
    public String toString() {
        return this.getFirstname() + " " + this.getLastname();
    }

}
