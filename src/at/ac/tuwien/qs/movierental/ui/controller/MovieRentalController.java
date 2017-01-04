package at.ac.tuwien.qs.movierental.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class MovieRentalController {

    @FXML
    private Tab tabMovies;
    @FXML
    private Tab tabCustomers;
    @FXML
    private Tab tabRental;

    private Stage primaryStage;

    public void setMoviesTabContent(Node moviesTabContent) {
        this.tabMovies.setContent(moviesTabContent);
    }

    public void setCustomersTabContent(Node customersTabContent) {
        this.tabCustomers.setContent(customersTabContent);
    }

    public void setRentalTabContent(Node rentalTabContent) {
        this.tabRental.setContent(rentalTabContent);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(event ->
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Filmverleih | Programm beenden");
            alert.setHeaderText("Programm beenden?");
            alert.setContentText("Wollen Sie das Programm wirklich beenden?\nDabei gehen alle nicht gespeicherten Daten verloren.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                event.consume();
            }
        });
    }

    public void showLicense() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Filmverleih | Lizenzinformationen");
        alert.setHeaderText("Lizenzinformationen");
        alert.setContentText("");
        alert.showAndWait();
    }

    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Filmverleih | Über das Programm");
        alert.setHeaderText("Über das Programm");
        alert.setContentText("Dies ist ein Beispielprogramm zu Lehrzwecken das im Rahmen der Lehrveranstaltung " +
                "Software-Qualitätssicherung an der Technischen Universität Wien entwickelt wurde.");
        alert.showAndWait();
    }

    public void reinitDatabase() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Filmverleih | Datenbank zurücksetzen");
        alert.setHeaderText("Datenbank zurücksetzen?");
        alert.setContentText("Wollen Sie die Datenbank wirklich zurücksetzen?\nDabei gehen alle Daten verloren.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // TODO: replace dummyDAOs with real database
        }
    }

    public void exitApplication() {
        this.primaryStage.fireEvent(new WindowEvent(this.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
