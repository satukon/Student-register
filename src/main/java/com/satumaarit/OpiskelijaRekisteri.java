package com.satumaarit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main class
 * @author satu
 */
public class OpiskelijaRekisteri extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML("OpiskelijaRekisteriFXML"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Student register application");
        stage.show();
    }

    /**
     * FXML Loader
     * @param fxml the .fxml filename in resources folder
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OpiskelijaRekisteri.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Application launch
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
