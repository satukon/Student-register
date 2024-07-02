package com.satumaarit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The database connection, table creations and a method to insert some sample data into the db.
 * @author satu
 */
public class DatabaseManager {
    
    /**
     * Open connection
     * @return connection
     * @throws SQLException
     */
    public static Connection openConnection() throws SQLException {
        String url = "jdbc:sqlite:student-register-database.db";
        Connection connection = DriverManager.getConnection(url);
        System.out.println("\t>> Successfully connected to db.");
        return connection;
    }

    /**
     * 
     * Create tables "Student", "Field of Study", "Course" and "StudentRecord"
     * @param c connection
     * @throws SQLException
     */    
    public static void createTables(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
        "CREATE TABLE IF NOT EXISTS Student ("
                + "StudentId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + "Firstname VARCHAR(40) NOT NULL,"
                + "Lastname VARCHAR(40) NOT NULL,"
                + "Homeaddress VARCHAR(40) NOT NULL,"
                + "Postalcode INT NOT NULL,"
                + "City VARCHAR(40) NOT NULL,"
                + "Mail VARCHAR(40) NOT NULL,"
                + "Phone VARCHAR(15) NOT NULL)"
            );
        ps.execute();
        System.out.println("\t>> Table created: Student");

        ps = c.prepareStatement(
                "CREATE TABLE IF NOT EXISTS FieldOfStudy ("
                        + "FieldOfStudyId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                        + "Name VARCHAR(50) NOT NULL)"
        );
        ps.execute();
        System.out.println("\t>> Table created: Field of study");

        ps = c.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Course ("
                        + "CourseId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                        + "Name VARCHAR(50) NOT NULL,"
                        + "FieldOfStudyId INT,"
                        + "Credits INT NOT NULL,"
                        + "FOREIGN KEY (FieldOfStudyId) REFERENCES FieldOfStudy(FieldOfStudyId));"
        );
        ps.execute();
        System.out.println("\t>> Table created:  Course");

        ps = c.prepareStatement(
                "CREATE TABLE IF NOT EXISTS StudentRecord ("
                        + "RecordId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                        + "StudentId INT NOT NULL,"
                        + "CourseId INT NOT NULL,"
                        + "Grading INT NOT NULL,"
                        + "EvaluationDate DATE NOT NULL)"
        );
        ps.execute();
        System.out.println("\t>> Table created: StudentRecord");
    }

    /**
     * Add sample data to the database
     * @param c connection
     * @throws SQLException
     */
    public static void addSampleData(Connection c) throws SQLException {
        // Add sample students
        Student spongebob = new Student(1, "SpongeBob", "SquarePants", "Pineapple house", "12345", "Bikini Bottom", "spongebob.squarepants@gmail.com", "040 1234567");
        Student patrick = new Student(2, "Patrick", "Star", "Under a rock", "12345", "Bikini Bottom", "patrick.star@gmail.com", "045 76543221");
        Student squidward = new Student(3, "Squidward", "Tentacles", "Easter Island head", "12345", "Bikini Bottom", "squidward.tentacles@gmail.com", "050 1234567");
        Student mrcrabs = new Student(4, "Eugene", "Crabs", "Anchor house", "12345", "Bikini Bottom", "mr.crabs@gmail.com", "040 3216547");
        Student sandy = new Student(5, "Sandy", "Cheecks", "Treedome", "12345", "Bikini Bottom", "sandy.cheeks@gmail.com", "045 5672341");
        Student gary = new Student(5, "Gary", "Snail", "Pineapple house", "12345", "Bikini Bottom", "gray.snail@gmail.com", "045 1234123");
        Student plankton = new Student(5, "Sheldon", "Plankton", "Chum bucket", "12345", "Bikini Bottom", "sheldon.j.plankton@gmail.com", "045 1212121");
        StudentDAO.addStudent(c, spongebob);
        StudentDAO.addStudent(c, patrick);
        StudentDAO.addStudent(c, squidward);
        StudentDAO.addStudent(c, mrcrabs);
        StudentDAO.addStudent(c, sandy);
        StudentDAO.addStudent(c, gary);
        StudentDAO.addStudent(c, plankton);

        // Add sample fields of studies
        FieldOfStudy fos1 = new FieldOfStudy(1, "Medicine");
        FieldOfStudy fos2 = new FieldOfStudy(2, "Physics");
        FieldOfStudy fos3 = new FieldOfStudy(3,"History");
        FieldOfStudy fos4 = new FieldOfStudy(4, "Philosophy");
        FieldOfStudy fos5 = new FieldOfStudy(5, "Mathematics");
        FieldOfStudyDAO.addFieldOfStudy(c, fos1);
        FieldOfStudyDAO.addFieldOfStudy(c, fos2);
        FieldOfStudyDAO.addFieldOfStudy(c, fos3);
        FieldOfStudyDAO.addFieldOfStudy(c, fos4);
        FieldOfStudyDAO.addFieldOfStudy(c, fos5);

        // Add sample courses
        Course course1 = new Course(1, "Brain surgery: a practical training course", fos1, 5);
        Course course2 = new Course(2, "Rocket science: basic course", fos2, 5);
        Course course3 = new Course(3, "Rocket science: advanced course", fos2, 10);
        Course course4 = new Course(4, "Medieval torture methods", fos3, 5);
        Course course5 = new Course(5, "Essentials of hedonism", fos4, 10);
        Course course6 = new Course(6, "Astrophysics for dummies", fos2, 5);
        Course course7 = new Course(7, "Math for dummies", fos5, 5);
        CourseDAO.addCourse(c, course1);
        CourseDAO.addCourse(c, course2);
        CourseDAO.addCourse(c, course3);
        CourseDAO.addCourse(c, course4);
        CourseDAO.addCourse(c, course5);
        CourseDAO.addCourse(c, course6);
        CourseDAO.addCourse(c, course7);

        // Add sample records
        StudentRecord studentRecord1 = new StudentRecord(spongebob, course1, 2, "25.11.2024");
        StudentRecord studentRecord2 = new StudentRecord(patrick, course1, 1, "25.08.2024");
        StudentRecord studentRecord3 = new StudentRecord(squidward, course1, 3, "25.08.2024");
        StudentRecord studentRecord4 = new StudentRecord(mrcrabs, course1, 3, "25.08.2024");
        StudentRecord studentRecord5 = new StudentRecord(sandy, course1, 5, "25.08.2024");
        StudentRecord studentRecord6 = new StudentRecord(spongebob, course2, 1, "12.05.2024");
        StudentRecord studentRecord7 = new StudentRecord(spongebob, course3, 3, "05.06.2024");
        StudentRecord studentRecord8 = new StudentRecord(spongebob, course4, 2, "11.09.2024");
        StudentRecord studentRecord9 = new StudentRecord(spongebob, course5, 1, "01.10.2024");
        StudentRecord studentRecord10 = new StudentRecord(spongebob, course6, 2, "30.09.2024");
        StudentRecord studentRecord11 = new StudentRecord(spongebob, course7, 1, "13.11.2024");
        StudentRecord studentRecord12 = new StudentRecord(patrick, course5, 5, "13.11.2024");
        StudentRecord studentRecord13 = new StudentRecord(patrick, course7, 2, "14.11.2024");
        StudentRecordDAO.addStudentRecord(c, studentRecord1);
        StudentRecordDAO.addStudentRecord(c, studentRecord2);
        StudentRecordDAO.addStudentRecord(c, studentRecord3);
        StudentRecordDAO.addStudentRecord(c, studentRecord4);
        StudentRecordDAO.addStudentRecord(c, studentRecord5);
        StudentRecordDAO.addStudentRecord(c, studentRecord6);
        StudentRecordDAO.addStudentRecord(c, studentRecord7);
        StudentRecordDAO.addStudentRecord(c, studentRecord8);
        StudentRecordDAO.addStudentRecord(c, studentRecord9);
        StudentRecordDAO.addStudentRecord(c, studentRecord10);
        StudentRecordDAO.addStudentRecord(c, studentRecord11);
        StudentRecordDAO.addStudentRecord(c, studentRecord12);
        StudentRecordDAO.addStudentRecord(c, studentRecord13);


        System.out.println("\t>> Successfully added sample data to db.");
    }
}
