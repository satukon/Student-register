module com.satumaarit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires java.desktop;

    opens com.satumaarit to javafx.fxml;
    exports com.satumaarit;
}
