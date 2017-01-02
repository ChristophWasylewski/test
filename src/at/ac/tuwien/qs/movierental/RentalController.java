package at.ac.tuwien.qs.movierental;

import at.ac.tuwien.qs.movierental.ui.controls.RentalActionButton;
import at.ac.tuwien.qs.movierental.ui.controls.TemporalAccessorCell;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RentalController {

    @FXML
    private TextField txtSearchCustomer;
    @FXML
    private ListView<Customer> lstCustomers;
    @FXML
    private ImageView imgSelectCustomer;
    @FXML
    private ToggleButton tglSelectCustomer;
    @FXML
    private TextField txtSearchMovie;
    @FXML
    private ListView<Movie> lstMovies;
    @FXML
    private Button btnAddMovie;
    @FXML
    private TableView<Rental> tblRentalOverview;
    @FXML
    private TableColumn<Rental, Long> tcMovieID;
    @FXML
    private TableColumn<Rental, String> tcTitle;
    @FXML
    private TableColumn<Rental, LocalDate> tcLentDate;
    @FXML
    private TableColumn<Rental, Rental> tcAction;
    @FXML
    private TextArea txtInvoice;
    @FXML
    private Button btnCompleteRental;

    private static final Image CUSTOMER_SELECTED = new Image(RentalController.class.getResourceAsStream("/images/deselectUser.png"));
    private static final Image NO_CUSTOMER_SELECTED = new Image(RentalController.class.getResourceAsStream("/images/selectUser.png"));

    private ObservableList<Rental> rentalObservableList = FXCollections.observableArrayList();
    private FilteredList<Movie> filteredMovies;
    private FilteredList<Customer> filteredCustomers;
    private Customer currentCustomer;

    private ObservableList<Rental> rentalsToPay = FXCollections.observableArrayList();
    private ObservableList<Rental> newRentals = FXCollections.observableArrayList();
    private RentalDAO rentalDAO;
    private InvoiceService invoiceService;

    public void setRentalDAO(RentalDAO rentalDAO) {
        this.rentalDAO = rentalDAO;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * We needed this class ages ago
     */
    /**
     * FindBugs hat hier einen Fehler gefunden:
     * es sollte sich hierbei um eine statische innere Klasse handeln
     */
    static class RentalWrapper {
        public Rental rental;
        public boolean pay = false;
    }

    @FXML
    private void initialize() {
        // init table columns
        this.tcMovieID.setCellValueFactory(cellData -> cellData.getValue().getMovie().idProperty());
        this.tcTitle.setCellValueFactory(cellData -> cellData.getValue().getMovie().titleProperty());
        this.tcLentDate.setCellValueFactory(cellData -> cellData.getValue().dateLentProperty());
        this.tcLentDate.setCellFactory(param -> new TemporalAccessorCell(DateTimeFormatter.ofPattern("dd. LLL yyyy")));
        this.tcAction.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        this.tcAction.setCellFactory(param -> new RentalActionButton(rental -> this.removeRentalButtonPressed((Rental) rental), rental -> this.payRentalButtonPressed((Rental) rental)));
        this.txtSearchMovie.textProperty().addListener((observable, oldValue, newValue) -> filterMovieList(newValue));
        this.txtSearchCustomer.textProperty().addListener((observable, oldValue, newValue) -> filterCustomerList(newValue));
        this.tblRentalOverview.setItems(this.rentalObservableList);
        // init customer and movie list and add button behaviour
        this.lstMovies.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.btnAddMovie.setDisable(true);
            } else {
                this.btnAddMovie.setDisable(false);
            }
        });
        this.lstCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.tglSelectCustomer.setDisable(true);
            } else {
                this.tglSelectCustomer.setDisable(false);
            }
        });
        // init customer list rendering
        this.lstCustomers.setCellFactory(param -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (customer == null || empty) {
                    setText(null);
                } else {
                    setText(customer.getId() + "; " + customer.getLastName().toUpperCase() + ",  " + customer.getFirstName());
                }
            }
        });
        // init movie list rendering
        this.lstMovies.setCellFactory(param -> new ListCell<Movie>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (movie == null || empty) {
                    setText(null);
                } else {
                    setText(movie.getId() + "; " + movie.getTitle() + ((movie.getSubtitle() != null && !movie.getSubtitle().isEmpty()) ? " - " + movie.getSubtitle() : ""));
                }
            }
        });
        // add change listeners to pay/new- Rental lists to recalulcate the invoice
        this.rentalsToPay.addListener((ListChangeListener<Rental>) c -> this.previewInvoice());
        this.newRentals.addListener((ListChangeListener<Rental>) c -> this.previewInvoice());
    }

    private void removeRentalButtonPressed(Rental rental) {
        this.rentalObservableList.remove(rental);
        this.newRentals.remove(rental);
    }

    private void payRentalButtonPressed(Rental rental) {
        if (rentalsToPay.contains(rental)) {
            rentalsToPay.remove(rental);
        } else {
            rentalsToPay.add(rental);
        }
    }

    private void filterCustomerList(String filter) {
        RentalController.this.filteredCustomers.setPredicate(customer -> {
            if (filter == null || filter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = filter.toLowerCase();
            // same as in CustomerManagementController make sure that this filter is always up to date
            return (customer.getId() != null && customer.getId().toString().toLowerCase().contains(lowerCaseFilter)) ||
                    (customer.getFirstName() != null && customer.getFirstName().toLowerCase().contains(lowerCaseFilter)) ||
                    (customer.getLastName() != null && customer.getLastName().toLowerCase().contains(lowerCaseFilter)) ||
                    (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(lowerCaseFilter));
        });
    }

    private void filterMovieList(String filter) {
        RentalController.this.filteredMovies.setPredicate(movie -> {
            if (filter == null || filter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = filter.toLowerCase();
            // same as in MovieManagementController make sure that this filter is always up to date
            return (movie.getId() != null && movie.getId().toString().toLowerCase().contains(lowerCaseFilter)) ||
                    (movie.getTitle() != null && movie.getTitle().toLowerCase().contains(lowerCaseFilter)) ||
                    (movie.getSubtitle() != null && movie.getSubtitle().toLowerCase().contains(lowerCaseFilter)) ||
                    (movie.getGenre() != null && movie.getGenre().toLowerCase().contains(lowerCaseFilter)) ||
                    (movie.getDirector() != null && movie.getDirector().toLowerCase().contains(lowerCaseFilter) ||
                            (movie.getYearPublished() != null && movie.getYearPublished().toString().toLowerCase().contains(lowerCaseFilter)));
        });
    }

    public void selectCustomer() {
        if (tglSelectCustomer.isSelected()) {
            this.currentCustomer = this.lstCustomers.getSelectionModel().getSelectedItem();
            this.rentalObservableList.setAll(this.rentalDAO.readByCustomer(this.currentCustomer));
            imgSelectCustomer.setImage(CUSTOMER_SELECTED);
        } else {
            this.currentCustomer = null;
            this.rentalObservableList.clear();
            this.rentalsToPay.clear();
            this.newRentals.clear();
            imgSelectCustomer.setImage(NO_CUSTOMER_SELECTED);
        }
        this.btnAddMovie.setDisable(true);
        this.txtSearchCustomer.setDisable(!this.txtSearchCustomer.isDisabled());
        this.txtSearchMovie.setDisable(!this.txtSearchMovie.isDisabled());
        this.lstCustomers.setDisable(!this.lstCustomers.isDisabled());
        this.lstMovies.setDisable(!this.lstMovies.isDisabled());
        this.tblRentalOverview.setDisable(!this.tblRentalOverview.isDisabled());
        this.txtInvoice.setDisable(!this.txtInvoice.isDisabled());
        this.btnCompleteRental.setDisable(!this.btnCompleteRental.isDisabled());
        this.previewInvoice();
    }

    public void addMovie() {
        Rental rental = new Rental();
        rental.setCustomer(this.currentCustomer);
        rental.setMovie(this.lstMovies.getSelectionModel().getSelectedItem());
        rental.setDateLent(LocalDate.now());
        try {
            this.validateRental(rental);
            this.rentalObservableList.add(rental);
            this.newRentals.add(rental);
        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filmverleih | Entleihfehler");
            alert.setHeaderText("Der Film kann nicht entliehen werden.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void completeRental() {
        this.invoiceService.payInvoice(currentCustomer, this.rentalsToPay, LocalDate.now());
        for (Rental rental : this.newRentals) {
            this.rentalDAO.create(rental);
        }
        for (Rental rental : this.rentalsToPay) {
            this.rentalDAO.delete(rental);
            this.rentalObservableList.remove(rental);
        }
        this.rentalObservableList.clear();
        this.newRentals.clear();
        this.rentalsToPay.clear();
        this.rentalObservableList.setAll(this.rentalDAO.readByCustomer(this.currentCustomer));
    }

    public void setMovies(ObservableList<Movie> movieObservableList) {
        this.filteredMovies = new FilteredList<>(movieObservableList, p -> true);
        SortedList<Movie> sortedData = new SortedList<>(this.filteredMovies);
        this.lstMovies.setItems(sortedData);
    }

    public void setCustomers(ObservableList<Customer> customerObservableList) {
        this.filteredCustomers = new FilteredList<>(customerObservableList, p -> true);
        SortedList<Customer> sortedData = new SortedList<>(this.filteredCustomers);
        this.lstCustomers.setItems(sortedData);
    }

    private void previewInvoice() {
        if (this.currentCustomer == null) {
            this.txtInvoice.setText("");
        } else {
            this.txtInvoice.setText(this.invoiceService.generateIncoicePreview(this.currentCustomer, rentalsToPay, newRentals, LocalDateTime.now()));
        }
    }

    /**
     * Validates a rental and checks if customer and movie are set and if the age rating is met.
     * @param rental to validate
     * @throws ValidationException if the validation fails
     */
    public void validateRental(Rental rental) throws ValidationException {
        if (rental != null) {
            if (rental.getCustomer() == null) {
                throw new ValidationException("Kunde muss gesetzt sein.");
            }
            if (rental.getMovie() == null) {
                throw new ValidationException("Film muss gesetzt sein.");
            }
            if (rental.getDateLent() == null) {
                throw new ValidationException("Verleihtag muss gesetzt sein.");
            }
            long minAge = 0;
            switch (rental.getMovie().getAgeRating()) {
                case "FSK 0":
                    minAge = 0;
                    break;
                case "FSK 6":
                    minAge = 6;
                    break;
                case "FSK 12":
                    minAge = 12;
                    break;
                case "FSk 16":
                    minAge = 16;
                    break;
                case "FSK 18":
                    minAge = 18;
                    break;
                case "RATED 21":
                    minAge = 2l;
                    break;
            }
            if (rental.getDateLent().minusYears(minAge).isBefore(rental.getCustomer().getBirthday())) {
                throw new ValidationException("Der Kunde erfüllt die Altersfreigabe nicht! (" + rental.getMovie().getAgeRating() + ")");
            }
        } else {
            throw new IllegalArgumentException("Rental has to be not null");
        }

    }
}
