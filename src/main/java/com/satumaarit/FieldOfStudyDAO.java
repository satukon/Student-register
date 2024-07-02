package com.satumaarit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * database CRUD operations for Fields of Studies
 */
public class FieldOfStudyDAO {

    /**
     *
     * CREATE a new Field of Study to db
     * @param c connection
     * @param fos object to create
     * @throws SQLException
     */
    public static void addFieldOfStudy(Connection c, FieldOfStudy fos) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO FieldOfStudy (Name) "
                        + "VALUES(?)");

        ps.setString(1, fos.getName());
        ps.execute();
        System.out.println("\t>> A new field of study was added to db: " + fos.getName());
    }

    /**
     *
     * READ Fields of Studies from db
     * @param c connection
     * @return list of Fields of Studies
     * @throws SQLException
     */
    public static ObservableList<FieldOfStudy> readAllFieldsOfStudies(Connection c) throws SQLException {
        ObservableList<FieldOfStudy> fieldsOfStudies = FXCollections.observableArrayList();

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT FieldOfStudyId, Name FROM FieldOfStudy"
        );

        while (rs.next()) {
            FieldOfStudy fieldofstudy = new FieldOfStudy();

            int id = rs.getInt("FieldOfStudyId");
            fieldofstudy.setFieldOfStudyId(id);

            String name = rs.getString("Name");
            fieldofstudy.setName(name);

            fieldsOfStudies.add(fieldofstudy);
        }
        System.out.println("\t>> Fields of Study table was read from db.");
        return fieldsOfStudies;
    }
}
