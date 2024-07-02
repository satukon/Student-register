package com.satumaarit;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/**
 * Controller class for the user interface
 * @author satu
 */
public class OpiskelijaRekisteriFXMLController implements Initializable {
    // Database connection
    Connection connection;

    // Object lists
    ObservableList<Student> students;
    ObservableList<Course> courses;
    ObservableList<FieldOfStudy> fieldsOfStudies;
    ObservableList<StudentRecord> student_records;

    // Minimum and maximum course credits, from config.properties
    int min_credits = ConfigurationManager.getMinCredits();
    int max_credits = ConfigurationManager.getMaxCredits();

    // Minimum and maximum grade values, from config.properties
    int min_grade = ConfigurationManager.getMinGrade();
    int max_grade = ConfigurationManager.getMaxGrade();

    // UI elements of Tab 1: Students
    @FXML
    private Tab tabStudents;

    @FXML
    private TextField inputFieldSearchByStudentId;

    @FXML
    private TextField inputFieldSearchByStudentName;

    @FXML
    private Button btnShowAllStudents;

    @FXML
    private TextField txtFieldFirstname;

    @FXML
    private TextField txtFieldLastname;

    @FXML
    private TextField txtFieldAddress;

    @FXML
    private TextField txtFieldPostalcode;

    @FXML
    private TextField txtFieldCity;

    @FXML
    private TextField txtFieldMail;

    @FXML
    private TextField txtFieldPhone;

    @FXML
    private Button btnAddStudent;

    @FXML
    private TableView<Student> tableStudents;

    @FXML
    private TableColumn<Student, Integer> tableColumnStudentnumber;

    @FXML
    private TableColumn<Student, String> tableColumnStudentFirstname;

    @FXML
    private TableColumn<Student, String> tableColumnStudentLastname;

    @FXML
    private TableColumn<Student, String> tableColumnStudentHomeadress;

    @FXML
    private TableColumn<Student, String> tableColumnStudentPostalcode;

    @FXML
    private TableColumn<Student, String> tableColumnStudentCity;

    @FXML
    private TableColumn<Student, String> tableColumnStudentMail;

    @FXML
    private TableColumn<Student, String> tableColumnStudentPhone;

    @FXML
    private TableColumn<Student, Button> tableColumnDeleteStudent;

    // UI elements of Tab 2: Courses
    @FXML
    private Tab tabCourses;

    @FXML
    private TextField inputFieldSearchByCourseid;

    @FXML
    private TextField inputFieldSearchByCoursename;

    @FXML
    private Button btnShowAllCourses;

    @FXML
    private TextField txtFieldCoursename;

    @FXML
    private ComboBox<FieldOfStudy> comboboxFieldofstudy;

    @FXML
    private ComboBox<String> comboboxCredits;

    @FXML
    private Button btnAddCourse;

    @FXML
    private TableView<Course> tableCourses;

    @FXML
    private TableColumn<Course, Integer> tableColumnCourseid;

    @FXML
    private TableColumn<Course, String> tableColumnCourseName;

    @FXML
    private TableColumn<Course, FieldOfStudy> tableColumnCourseEducationprogram;

    @FXML
    private TableColumn<Course, String> tableColumnCourseCredits;

    @FXML
    private TableColumn<Course, Button> tableColumnDeleteCourse;


    // UI elements of Tab 3: Student records
    @FXML
    private Tab tabStudentRecords;

    @FXML
    private TextField inputFieldSearchStudentRecordByStudentId;

    @FXML
    private TextField inputFieldSearchStudentRecordByStudentName;

    @FXML
    private ComboBox<Student> comboboxStudent;

    @FXML
    private ComboBox<Course> comboboxCourse;

    @FXML
    private ComboBox<Integer> comboboxGrade;

    @FXML
    private DatePicker datePickerEvaluationDate;

    @FXML
    private Button btnShowStudentRecord;

    @FXML
    private TableView<StudentRecord> tableStudentRecords;

    @FXML
    private TableColumn<StudentRecord, Integer> tableColumnStudentGradesGradeId;

    @FXML
    private TableColumn<StudentRecord, String> tableColumnStudentGradesStudentFirstname;

    @FXML
    private TableColumn<StudentRecord, String> tableColumnStudentGradesStudentLastname;

    @FXML
    private TableColumn<StudentRecord, String> tableColumnStudentGradesCourseName;

    @FXML
    private TableColumn<StudentRecord, String> tableColumnStudentGradesCoursesCredits;

    @FXML
    private TableColumn<StudentRecord, String> tableColumnStudentGradesCourseEvaluationdate;

    @FXML
    private TableColumn<StudentRecord, Integer> tableColumnStudentGradesCourseGrade;

    @FXML
    private TableColumn<StudentRecord, Button> tableColumnStudentGradesDeleteGrade;

    /**
     * TAB: All tabs
     * Clicking an empty area on any of the tabs clears the selection models of the tables.
     */
    @FXML
    void tabAreaClicked(MouseEvent event) {
        tableStudents.getSelectionModel().clearSelection();
        tableCourses.getSelectionModel().clearSelection();
        tableStudentRecords.getSelectionModel().clearSelection();
    }

    /**
     * TAB: Students
     * Clicking tab "Students" open clears all input fields etc. shown on tab.
     */
    @FXML
    void tabStudentsSelected(Event event) {
        tableStudents.setItems(students);
        inputFieldSearchByStudentId.clear();
        inputFieldSearchByStudentName.clear();
        txtFieldFirstname.clear();
        txtFieldLastname.clear();
        txtFieldLastname.clear();
        txtFieldAddress.clear();
        txtFieldPostalcode.clear();
        txtFieldCity.clear();
        txtFieldMail.clear();
        txtFieldPhone.clear();
    }

    /**
     * TAB: Students
     * ID-search for students, filters the "Students" table.
     */
    @FXML
    void searchByStudentId(KeyEvent event) {
        inputFieldSearchByStudentName.clear();
        tableStudents.getSelectionModel().clearSelection();

        // Search by keyword and filter table, then re-populate last column with delete buttons
        String keyword = inputFieldSearchByStudentId.getText();
        tableStudents.setItems(Student.searchByStudentId(students, keyword));
        buttonDeleteStudent();
    }

    /**
     * TAB: Students
     * Name search for students, filters the "Students" table.
     */
    @FXML
    void searchByStudentName(KeyEvent event) {
        inputFieldSearchByStudentId.clear();
        tableStudents.getSelectionModel().clearSelection();

        // Search by keyword and filter table, then re-populate last column with delete buttons
        String keyword = inputFieldSearchByStudentName.getText();
        tableStudents.setItems(Student.searchByStudentName(students, keyword));
        buttonDeleteStudent();
    }

    /**
     * TAB: Students
     * "Show all students" button, shows all students on the "Students" table.
     */
    @FXML
    void btnShowAllStudentsClicked(ActionEvent event) {
        tableStudents.setItems(students);

        // Clear the search input fields
        inputFieldSearchByStudentId.clear();
        inputFieldSearchByStudentName.clear();
    }

    /**
     * TAB: Students
     * "Add a new student" button, clicking the button adds a new student to the system.
     */
    @FXML
    void btnAddStudentClicked(ActionEvent event) {
        try {
            Integer.parseInt(txtFieldPostalcode.getText());
            if (txtFieldPostalcode.getText().length() == 5) {
                Student.addStudent(connection, txtFieldFirstname.getText(), txtFieldLastname.getText(), txtFieldAddress.getText(), txtFieldPostalcode.getText(), txtFieldCity.getText(), txtFieldMail.getText(), txtFieldPhone.getText(), students);

                // Clear table selection and text fields after insertion
                tableStudents.getSelectionModel().clearSelection();
                txtFieldFirstname.clear();
                txtFieldLastname.clear();
                txtFieldLastname.clear();
                txtFieldAddress.clear();
                txtFieldPostalcode.clear();
                txtFieldCity.clear();
                txtFieldMail.clear();
                txtFieldPhone.clear();
            }
        }
        catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    /**
     * TAB: Students
     * "Delete" buttons for students. Adds event handlers and delete logic to the buttons,
     * then populates the last column of "Students" table with the buttons.
     */
    public void buttonDeleteStudent() {
        tableColumnDeleteStudent.setCellFactory(param -> new TableCell<>() {
            private final Button deleteStudentButton = new Button("Delete");

            {
                // Button's event handler and delete logic
                deleteStudentButton.setOnAction(event -> {
                    inputFieldSearchByStudentId.setText("");
                    inputFieldSearchByStudentName.setText("");

                    Student student = getTableView().getItems().get(getIndex());
                    Student.deleteStudent(connection, student, students);
                    tableStudents.setItems(students);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteStudentButton);
            }
        });
    }

    /**
     * TAB: Courses
     * Clicking tab "Courses" open clears all input fields etc. shown on tab.
     */
    @FXML
    void tabCoursesSelected(Event event) {
        tableCourses.setItems(courses);
        inputFieldSearchByCourseid.clear();
        inputFieldSearchByCoursename.clear();
        txtFieldCoursename.clear();
        comboboxFieldofstudy.getSelectionModel().clearSelection();
        comboboxCredits.getSelectionModel().clearSelection();
    }

    /**
     * TAB: Courses
     * ID-search for courses, filters the "Courses" table.
     */
    @FXML
    void searchByCourseId(KeyEvent event) {
        // Clear table selection and search fields
        inputFieldSearchByCoursename.clear();
        tableCourses.getSelectionModel().clearSelection();

        // Search by keyword and filter table, then re-populate last column with delete buttons
        String keyword = inputFieldSearchByCourseid.getText();
        tableCourses.setItems(Course.searchByCourseId(courses, keyword));
        buttonDeleteCourse();
    }

    /**
     * TAB: Courses
     * Name search for courses, filters the "Courses" table.
     */
    @FXML
    void searchByCourseName(KeyEvent event) {
        // Clear table selection and search fields
        inputFieldSearchByCourseid.clear();
        tableCourses.getSelectionModel().clearSelection();

        // Search by keyword and filter table, then re-populate last column with delete buttons
        String keyword = inputFieldSearchByCoursename.getText();
        tableCourses.setItems(Course.searchByCourseName(courses, keyword));
        buttonDeleteCourse();
    }

    /**
     * TAB: Courses
     * "Show all courses" button. Shows all courses on the "Courses" table.
     */
    @FXML
    void btnShowAllCoursesClicked(ActionEvent event) {
        tableCourses.setItems(courses);

        // Clear the search input fields
        inputFieldSearchByCourseid.clear();
        inputFieldSearchByCoursename.clear();

    }

    /**
     * TAB: Courses
     * "Add a new course" button, clicking the button adds a new course to the system.
     */
    @FXML
    void btnAddCourseClicked(ActionEvent event) {
        FieldOfStudy fieldOfStudy = comboboxFieldofstudy.getSelectionModel().getSelectedItem();
        String courseName = txtFieldCoursename.getText();
        String selectedCredits = comboboxCredits.getSelectionModel().getSelectedItem();

        if (fieldOfStudy != null && !courseName.isEmpty() && selectedCredits != null) {
            int credits = Integer.parseInt(selectedCredits);
            Course.addCourse(connection, courseName, fieldOfStudy, credits, courses);

            // Clear table selection and input fields
            tableCourses.getSelectionModel().clearSelection();
            txtFieldCoursename.clear();
            comboboxCredits.getSelectionModel().clearSelection();
            comboboxFieldofstudy.getSelectionModel().clearSelection();
        }
    }

    /**
     * TAB: Courses
     * "Delete" buttons for courses. Adds event handlers and delete logic to the buttons,
     * then populates the last column of "Courses" table with the buttons.
     */
    public void buttonDeleteCourse() {
        tableColumnDeleteCourse.setCellFactory(param -> new TableCell<>() {
            private final Button deleteCourseButton = new Button("Delete");

            {
                // Button's event handler and delete logic
                deleteCourseButton.setOnAction(event -> {
                    inputFieldSearchByCourseid.setText("");
                    inputFieldSearchByCoursename.setText("");

                    Course course = getTableView().getItems().get(getIndex());
                    Course.deleteCourse(connection, course, courses);
                    tableCourses.setItems(courses);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteCourseButton);
            }
        });
    }

    /**
     * TAB: Student records
     * Clicking tab "Student records" open clears all input fields etc. shown on tab.
     */
    @FXML
    void tabStudentRecordsSelected(Event event) {
        // Load student records from db (information has possibly changed)
        try {
            student_records = StudentRecordDAO.readAllStudentRecords(connection);
            tableStudentRecords.setItems(student_records);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Clear all input fields etc. shown on tab.
        inputFieldSearchStudentRecordByStudentId.clear();
        inputFieldSearchStudentRecordByStudentName.clear();
        datePickerEvaluationDate.setValue(null);
        comboboxCourse.getSelectionModel().clearSelection();
        comboboxGrade.getSelectionModel().clearSelection();
        btnShowStudentRecord.setVisible(false);
    }

    /**
     * TAB: Student records
     * ID-search for student records, filters the "Student records" table.
     */
    @FXML
    void searchStudentRecordByStudentId(KeyEvent event) {
        inputFieldSearchStudentRecordByStudentName.clear();

        // Search by keyword and filter table, then re-populate last column with delete buttons
        String keyword = inputFieldSearchStudentRecordByStudentId.getText();
        ObservableList<StudentRecord> filteredStudentRecords = StudentRecord.searchByStudentId(student_records, keyword);
        tableStudentRecords.setItems(filteredStudentRecords);
        buttonDeleteStudentRecord();

        // Dont show "Show student record" button if search gave no results.
        if (filteredStudentRecords.isEmpty()) {
            btnShowStudentRecord.setVisible(false);
            return;
        }

        // If search gave results, show information depending on if search resulted in one or all students.
        int firstGradeStudentId = filteredStudentRecords.get(0).getStudent().getStudentId();
        boolean allIdsAreSame = true;

        for (StudentRecord studentRecord : filteredStudentRecords) {
            if (studentRecord.getStudent().getStudentId() != firstGradeStudentId) {
                allIdsAreSame = false;
                tableStudentRecords.getSelectionModel().clearSelection();
                break;
            }
        }
        btnShowStudentRecord.setVisible(allIdsAreSame);
    }

    /**
     * TAB: Student records
     * Name search for a student record, filters the "Student records" table
     */
    @FXML
    void searchStudentRecordByStudentName(KeyEvent event) {
        inputFieldSearchStudentRecordByStudentId.clear();

        // Search by keyword and filter table, then re-populate last column with delete buttons
        String keyword = inputFieldSearchStudentRecordByStudentName.getText();
        ObservableList<StudentRecord> filteredStudentRecords = StudentRecord.searchByStudentName(student_records, keyword);
        tableStudentRecords.setItems(filteredStudentRecords);
        buttonDeleteStudentRecord();

        // Dont show "Show student record" button if search gave no results.
        if (filteredStudentRecords.isEmpty()) {
            btnShowStudentRecord.setVisible(false);
            return;
        }

        // If search gave results, show information depending on if search resulted in one or all students.
        int firstGradeStudentId = filteredStudentRecords.get(0).getStudent().getStudentId();
        boolean allIdsAreSame = true;

        for (StudentRecord studentRecord : filteredStudentRecords) {
            if (studentRecord.getStudent().getStudentId() != firstGradeStudentId) {
                allIdsAreSame = false;
                tableStudentRecords.getSelectionModel().clearSelection();
                break;
            }
        }
        btnShowStudentRecord.setVisible(allIdsAreSame);
    }

    /**
     * TAB: Student records
     * "Show all records" button. Clicking the button shows records for all students on the "Student records" table.
     */
    @FXML
    void btnShowAllStudentRecordsClicked(ActionEvent event) {
        tableStudentRecords.setItems(student_records);
        buttonDeleteStudentRecord();
        tableStudentRecords.getSelectionModel().clearSelection();

        // Clear the input fields etc.
        inputFieldSearchStudentRecordByStudentId.clear();
        inputFieldSearchStudentRecordByStudentName.clear();
        btnShowStudentRecord.setVisible(false);
    }

    /**
     * TAB: Student records
     * "Add a new record for student" button, clicking the button adds a new record to the system.
     */
    @FXML
    void btnAddStudentRecordClicked(ActionEvent event) {
        if (comboboxStudent.getSelectionModel().isEmpty()) {
            return;
        }

        if (comboboxCourse.getSelectionModel().isEmpty()) {
            return;
        }

        if (comboboxGrade.getSelectionModel().isEmpty()) {
            return;
        }

        if (datePickerEvaluationDate.getValue() == null) {
            return;
        }

        Student selectedStudent = comboboxStudent.getSelectionModel().getSelectedItem();
        Course selectedCourse = comboboxCourse.getSelectionModel().getSelectedItem();
        int grade = comboboxGrade.getSelectionModel().getSelectedItem();

        // Format date
        LocalDate date = datePickerEvaluationDate.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = date.format(formatter);

        // Add to system
        StudentRecord.addStudentRecord(connection, selectedStudent, selectedCourse, grade, formattedDate, student_records);

        //Clear input fields
        comboboxStudent.getSelectionModel().clearSelection();
        comboboxCourse.getSelectionModel().clearSelection();
        comboboxGrade.getSelectionModel().clearSelection();
        datePickerEvaluationDate.setValue(null);
    }

    /**
     * TAB: Student records
     * Button "Open pdf report". Clicking the button generates and then opens a pdf report of a student's records.
     */
    @FXML
    void btnShowStudentRecordClicked(ActionEvent event) {
        PdfGenerator pdfGenerator = new PdfGenerator();
        ObservableList<StudentRecord> studentrecord = tableStudentRecords.getItems();
        String outputFilePath = "studentrecord.pdf";
        pdfGenerator.generatePdf(studentrecord, outputFilePath);
        pdfGenerator.openPdf(outputFilePath);
    }

    /**
     * TAB: Student records
     * "Delete" buttons for student records. Adds event handlers and delete logic to the buttons,
     * then populates the last column of "Student records" table with the buttons.
     */
    public void buttonDeleteStudentRecord() {
        tableColumnStudentGradesDeleteGrade.setCellFactory(param -> new TableCell<>() {
            private final Button deleteStudentRecordButton = new Button("Delete");

            {
                // Button's event handler and delete logic
                deleteStudentRecordButton.setOnAction(event -> {
                    inputFieldSearchStudentRecordByStudentId.setText("");
                    inputFieldSearchStudentRecordByStudentName.setText("");

                    StudentRecord studentRecord = getTableView().getItems().get(getIndex());

                    StudentRecord.deleteGrade(connection, studentRecord, student_records);
                    tableStudentRecords.setItems(student_records);
                    buttonDeleteStudentRecord();
                    tableStudentRecords.getSelectionModel().clearSelection();
                    btnShowStudentRecord.setVisible(false);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteStudentRecordButton);
            }
        });
    }

    /**
     * TAB: Student records
     * If a column containing a student's first or last name is clicked, the student record for that student is opened.
     */
    @FXML
    void studentRecordsTableRowClicked(MouseEvent event) {
        if (!tableStudentRecords.getSelectionModel().isEmpty()) {
            TablePosition<?, ?> pos = tableStudentRecords.getSelectionModel().getSelectedCells().get(0);
            int columnIndex = pos.getColumn();

            if (columnIndex > 0 && columnIndex < 3) {
                StudentRecord selectedStudentRecord = tableStudentRecords.getSelectionModel().getSelectedItem();
                ObservableList<StudentRecord> studentRecord = StudentRecord.searchRecordForStudent(selectedStudentRecord, student_records);
                tableStudentRecords.setItems(studentRecord);
                tableStudentRecords.getSelectionModel().clearSelection();
                btnShowStudentRecord.setVisible(true);
            }
        }
    }

    /**
     * Initialization
     *
     * If the program is run for the first time: creates a new SQLite database and adds sample data to it.
     * After that uses the database already created and won't add the sample data again.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize database and open connection
        try {
            connection = DatabaseManager.openConnection();
            DatabaseManager.createTables(connection);

            // Add sample data if needed
            if (!ConfigurationManager.isSampleDataAdded()) {
                DatabaseManager.addSampleData(connection);
                ConfigurationManager.setSampleDataAdded(true);
            }

            // Fetch students, fields of studies and courses and records from the database
            students = StudentDAO.readAllStudents(connection);
            fieldsOfStudies = FieldOfStudyDAO.readAllFieldsOfStudies(connection);
            courses = CourseDAO.readAllCourses(connection);
            student_records = StudentRecordDAO.readAllStudentRecords(connection);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Initialize comboboxes and tables
        initializeComboBoxes();
        initializeStudentsTable();
        initializeCoursesTable();
        initializeStudentRecordsTable();
    }

    /**
     * Initialization of the "Students" table
     * Content of the table and editable columns are defined, edit logic is added.
     */
    public void initializeStudentsTable() {
        tableStudents.setItems(students);
        tableStudents.setPlaceholder(new Label(""));

        // Cell value definitions
        tableColumnStudentnumber.setCellValueFactory(cellData ->
                new SimpleObjectProperty<Integer>(cellData.getValue().getStudentId()));

        tableColumnStudentFirstname.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getFirstname()));

        tableColumnStudentLastname.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getLastname()));

        tableColumnStudentHomeadress.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getHomeaddress()));

        tableColumnStudentPostalcode.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getPostalcode()));

        tableColumnStudentCity.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getCity()));

        tableColumnStudentMail.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getMail()));

        tableColumnStudentPhone.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getPhone()));

        buttonDeleteStudent(); // the last column's cell values are delete buttons

        // Editable column cells
        tableColumnStudentFirstname.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnStudentLastname.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnStudentHomeadress.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnStudentPostalcode.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnStudentCity.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnStudentMail.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnStudentPhone.setCellFactory(TextFieldTableCell.forTableColumn());

        // Event handlers for editable cells
        tableColumnStudentFirstname.setOnEditCommit(event -> {
            Student student = event.getRowValue();
            student.setFirstname(event.getNewValue());
            Student.updateStudent(connection, student, students);
        });

        tableColumnStudentLastname.setOnEditCommit(event -> {
            Student student = event.getRowValue();
            student.setLastname(event.getNewValue());
            Student.updateStudent(connection, student, students);
        });

        tableColumnStudentHomeadress.setOnEditCommit(event -> {
            Student student = event.getRowValue();
            student.setHomeaddress(event.getNewValue());
            Student.updateStudent(connection, student, students);
        });

        tableColumnStudentPostalcode.setOnEditCommit(event -> {
            Student student = event.getRowValue();
            String newPostalCode = event.getNewValue();
            try {
                // Parsing the postal code to ensure it's numeric and the length is correct
                Integer.parseInt(newPostalCode);
                if (newPostalCode.length() == 5) {
                    student.setPostalcode(newPostalCode);
                    Student.updateStudent(connection, student, students);
                }
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        });

        tableColumnStudentCity.setOnEditCommit(event -> {
            Student student = event.getRowValue();
            student.setCity(event.getNewValue());
            Student.updateStudent(connection, student, students);
        });

        tableColumnStudentMail.setOnEditCommit(event -> {
            Student student = event.getRowValue();
            student.setMail(event.getNewValue());
            Student.updateStudent(connection, student, students);
        });

        tableColumnStudentPhone.setOnEditCommit(event -> {
            Student student = event.getRowValue();
            student.setPhone(event.getNewValue());
            Student.updateStudent(connection, student, students);
        });
    }

    /**
     * Initialization of the "Courses" table
     * Content of the table and editable columns are defined, edit logic is added.
     */
    public void initializeCoursesTable() {
        tableCourses.setItems(courses);
        tableCourses.setPlaceholder(new Label(""));

        // Cell value definitions
        tableColumnCourseid.setCellValueFactory(cellData ->
                new SimpleObjectProperty<Integer>(cellData.getValue().getCourseId()));

        tableColumnCourseName.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getName()));

        tableColumnCourseEducationprogram.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getFieldOfStudy()));

        tableColumnCourseCredits.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getCredits() + " op"));

        buttonDeleteCourse(); // the last column's cell values are delete buttons

        // Editable columns
        tableColumnCourseName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnCourseEducationprogram.setCellFactory(ComboBoxTableCell.forTableColumn(comboboxFieldofstudy.getItems()));
        tableColumnCourseCredits.setCellFactory(ComboBoxTableCell.forTableColumn(comboboxCredits.getItems()));

        // Event handlers for editable cells
        tableColumnCourseName.setOnEditCommit(event -> {
            Course course = event.getRowValue();
            course.setName(event.getNewValue());
            Course.updateCourse(connection, course, courses);
        });

        tableColumnCourseEducationprogram.setOnEditCommit(event -> {
            Course course = event.getRowValue();
            course.setFieldOfStudy(event.getNewValue());
            Course.updateCourse(connection, course, courses);
        });

        tableColumnCourseCredits.setOnEditCommit(event -> {
            Course course = event.getRowValue();
            course.setCredits(Integer.parseInt(event.getNewValue()));
            Course.updateCourse(connection, course, courses);
        });
    }

    /**
     * Initialization of the "Student Records" table
     * Content of the table and editable columns are defined, edit logic is added.
     */
    public void initializeStudentRecordsTable() {
        tableStudentRecords.setItems(student_records);
        tableStudentRecords.setPlaceholder(new Label(""));

        // Cell value definitions
        tableColumnStudentGradesGradeId.setCellValueFactory(cellData ->
                new SimpleObjectProperty<Integer>(cellData.getValue().getStudent().getStudentId()));

        tableColumnStudentGradesStudentFirstname.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getStudent().getFirstname()));

        tableColumnStudentGradesStudentLastname.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getStudent().getLastname()));

        tableColumnStudentGradesCourseName.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getCourse().getName()));

        tableColumnStudentGradesCoursesCredits.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getCourse().getCredits() + " op"));

        tableColumnStudentGradesCourseEvaluationdate.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getEvaluationdate()));

        tableColumnStudentGradesCourseGrade.setCellValueFactory(cellData ->
                new SimpleObjectProperty<Integer>(cellData.getValue().getGrading()));

        buttonDeleteStudentRecord(); // the last column's cell values are delete buttons

        // Editable column cells
        tableColumnStudentGradesCourseEvaluationdate.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnStudentGradesCourseGrade.setCellFactory(ComboBoxTableCell.forTableColumn(comboboxGrade.getItems()));

        // Event handlers for editable cells
        tableColumnStudentGradesCourseEvaluationdate.setOnEditCommit(event -> {
            try {
                StudentRecord studentRecord = event.getRowValue();

                // The date from date picker is formatted to string
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate localDate = LocalDate.parse(event.getNewValue(), formatter);
                String formattedDate = localDate.format(formatter);
                studentRecord.setEvaluationdate(formattedDate);
                StudentRecord.updateStudentRecord(connection, studentRecord, student_records);
            } catch (DateTimeParseException e) {
                System.out.println(e);
            }
        });

        tableColumnStudentGradesCourseGrade.setOnEditCommit(event -> {
            StudentRecord studentRecord = event.getRowValue();
            studentRecord.setGrading(event.getNewValue());
            StudentRecord.updateStudentRecord(connection, studentRecord, student_records);
        });
    }

    /**
     * Initialization of the combo boxes
     */
    public void initializeComboBoxes() {
        ObservableList<String> creditsList = FXCollections.observableArrayList();
        for (int i = min_credits; i <= max_credits; i++) {
            creditsList.add(String.valueOf(i));
        }
        comboboxCredits.setItems(creditsList);

        ObservableList<Integer> gradesList = FXCollections.observableArrayList();
        for (int i = min_grade; i <= max_grade; i++) {
            gradesList.add(i);
        }
        comboboxStudent.setItems(students);
        comboboxFieldofstudy.setItems(fieldsOfStudies);
        comboboxCourse.setItems(courses);
        comboboxGrade.setItems(gradesList);
    }
}