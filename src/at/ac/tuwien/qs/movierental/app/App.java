package at.ac.tuwien.qs.movierental.app;

import at.ac.tuwien.qs.movierental.*;
import at.ac.tuwien.qs.movierental.DummyCustomerDAO;
import at.ac.tuwien.qs.movierental.DummyMovieDAO;
import at.ac.tuwien.qs.movierental.DummyRentalDAO;
import at.ac.tuwien.qs.movierental.Customer;
import at.ac.tuwien.qs.movierental.Movie;
import at.ac.tuwien.qs.movierental.CustomerManagementController;
import at.ac.tuwien.qs.movierental.MovieManagementController;
import at.ac.tuwien.qs.movierental.ui.controller.MovieRentalController;
import at.ac.tuwien.qs.movierental.RentalController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private MovieRentalController movieRentalController;
    private ObservableList<Movie> movies;
    private ObservableList<Customer> customers;
    private DummyMovieDAO movieDAO;
    private DummyCustomerDAO customerDAO;
    private DummyRentalDAO rentalDAO;

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.info("Videorental application started");
        primaryStage.setTitle("Filmverleih");
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/rent.png")));

        movieDAO = new DummyMovieDAO();
        movies = FXCollections.observableArrayList(movieDAO.read());
        customerDAO = new DummyCustomerDAO();
        customers = FXCollections.observableArrayList(customerDAO.read());
        rentalDAO = new DummyRentalDAO(movieDAO, customerDAO);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(this.getClass().getResource("/fxml/movieRental.fxml"));
        Parent root = fxmlLoader.load();

        this.movieRentalController = fxmlLoader.getController();
        this.movieRentalController.setPrimaryStage(primaryStage);
        setupMovieManagement();
        setupCustomerManagement();
        setupRental();

        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }

    private void setupMovieManagement() throws java.io.IOException {
        LOGGER.info("setup MovieManagement");
        MovieDataService movieDataService = new TheMovieDbMovieDataService();
        RatingService ratingService = new SimpleRatingService(movieDataService);

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/movieManagement.fxml"));
        this.movieRentalController.setMoviesTabContent(fxmlLoader.load());
        MovieManagementController movieManagementController = fxmlLoader.getController();
        movieManagementController.setMovies(this.movies);
        movieManagementController.setMovieDAO(this.movieDAO);
        movieManagementController.setRatingService(ratingService);
    }

    private void setupCustomerManagement() throws java.io.IOException {
        LOGGER.info("setup CustomerManagement");
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/customerManagement.fxml"));
        this.movieRentalController.setCustomersTabContent(fxmlLoader.load());
        CustomerManagementController customerManagementController = fxmlLoader.getController();
        customerManagementController.setCustomers(this.customers);
        customerManagementController.setCustomerDAO(this.customerDAO);
    }

    private void setupRental() throws java.io.IOException {
        LOGGER.info("setup Rentals");
        InvoiceService invoiceService = new SimpleInvoiceService();

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/rental.fxml"));
        this.movieRentalController.setRentalTabContent(fxmlLoader.load());
        RentalController rentalController = fxmlLoader.getController();
        rentalController.setMovies(this.movies);
        rentalController.setCustomers(this.customers);
        rentalController.setRentalDAO(this.rentalDAO);
        rentalController.setInvoiceService(invoiceService);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
