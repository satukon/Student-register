package com.satumaarit;

/**
 *
 * Field of Study class to manage the Field of Study objects.
 * The attributes, getters, setters and methods to manage the information of fields of studies.
 * @author satu
 */
public class FieldOfStudy {
    /**
     * Attributes
     */
    private int fieldOfStudyId;
    private String name;

    /**
     * Constructor
     */
    public FieldOfStudy() {
    }

    /**
    * Constructor
    * @param name
     */
    public FieldOfStudy(int id, String name) {
        this.fieldOfStudyId = id;
        this.name = name;
    }

    /**
     * @return id
     */
    public int getFieldOfStudyId() {
        return fieldOfStudyId;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param fieldOfStudyId_int student id to set
     */
    public void setFieldOfStudyId(int fieldOfStudyId_int) { this.fieldOfStudyId = fieldOfStudyId_int; }

    /**
     * @param name_str student's first name to set
     */
    public void setName(String name_str) {
        this.name = name_str;
    }

    /**
     * @return the name of a field of study
     */
    public String toString() {
        return this.getName();
    }
}