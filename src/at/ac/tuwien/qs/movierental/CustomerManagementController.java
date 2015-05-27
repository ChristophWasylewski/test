package at.ac.tuwien.qs.movierental;

import at.ac.tuwien.qs.movierental.ui.controls.BooleanCell;
import at.ac.tuwien.qs.movierental.ui.controls.TemporalAccessorCell;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomerManagementController {

    private static final Image NO_PHOTO = new Image(RentalController.class.getResourceAsStream("/images/user.png"));

    @FXML
    private TableView<Customer> tblCustomers;
    @FXML
    private TableColumn<Customer, Long> tcCustomerId;
    @FXML
    private TableColumn<Customer, String> tcFirstName;
    @FXML
    private TableColumn<Customer, String> tcLastName;
    @FXML
    private TableColumn<Customer, String> tcEmail;
    @FXML
    private TableColumn<Customer, LocalDate> tcBirthday;
    @FXML
    private TableColumn<Customer, String> tcPhone;
    @FXML
    private TableColumn<Customer, String> tcZipCode;
    @FXML
    private TableColumn<Customer, Boolean> tcPatron;
    @FXML
    private TableColumn<Customer, Integer> tcVideopoints;
    @FXML
    private TableColumn<Customer, Integer> tcRent;
    @FXML
    private TableColumn<Customer, Integer> tcOverdue;
    @FXML
    private Label lblCustomerID;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtBirthday;
    @FXML
    private CheckBox chkPatron;
    @FXML
    private TextArea txtAddress;
    @FXML
    private TextField txtZipCode;
    @FXML
    private TextField txtCity;
    @FXML
    private Label lblVideopoints;
    @FXML
    private ImageView imgPhoto;

    @FXML
    private TextField txtFilter;
    @FXML
    private ToggleButton tglFilter;

    private File filePhoto;

    private Customer currentCustomer;

    private ObservableList<Customer> customerObservableList;
    private FilteredList<Customer> filteredCustomers;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd. MM. yyyy");
    private CustomerDAO customerDAO;

    @FXML
    private void initialize() {
        this.showCustomerDetails(null);
        this.tcCustomerId.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        this.tcFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        this.tcLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        this.tcEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        this.tcBirthday.setCellValueFactory(cellData -> cellData.getValue().birthdayProperty());
        this.tcBirthday.setCellFactory(param -> new TemporalAccessorCell(DateTimeFormatter.ofPattern("dd. LLL yyyy")));
        this.tcPhone.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        this.tcZipCode.setCellValueFactory(cellData -> cellData.getValue().zipCodeProperty());
        this.tcPatron.setCellValueFactory(cellData -> cellData.getValue().patronProperty());
        this.tcPatron.setCellFactory(param -> new BooleanCell());
        this.tcVideopoints.setCellValueFactory(cellData -> cellData.getValue().videopointsProperty());
        this.txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                this.tglFilter.setSelected(false);
            } else {
                this.tglFilter.setSelected(true);
            }
            filterTable(newValue);
        });
        this.tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showCustomerDetails(newValue));
    }

    public void setCustomers(ObservableList<Customer> customerObservableList) {
        this.customerObservableList = customerObservableList;
        this.tblCustomers.setItems(this.customerObservableList);
        this.filteredCustomers = new FilteredList<>(this.customerObservableList, p -> true);
        SortedList<Customer> sortedData = new SortedList<>(this.filteredCustomers);
        sortedData.comparatorProperty().bind(tblCustomers.comparatorProperty());
        tblCustomers.setItems(sortedData);
    }

    private void filterTable(String filter) {
        CustomerManagementController.this.filteredCustomers.setPredicate(customer -> {
            if (filter == null || filter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = filter.toLowerCase();
            return (customer.getId() != null && customer.getId().toString().toLowerCase().contains(lowerCaseFilter)) ||
                    (customer.getFirstName() != null && customer.getFirstName().toLowerCase().contains(lowerCaseFilter)) ||
                    (customer.getLastName() != null && customer.getLastName().toLowerCase().contains(lowerCaseFilter)) ||
                    (customer.getEmail() != null && customer.getEmail().toLowerCase().contains(lowerCaseFilter));
        });
    }

    @FXML
    private void filter() {
        if (this.tglFilter.isSelected()) {
            this.filterTable(this.txtFilter.getText());
        } else {
            this.filterTable(null);
        }
    }

    @FXML
    private void showCustomerDetails(Customer customer) {
        this.currentCustomer = customer;
        if (customer != null) {
            this.lblCustomerID.setText((customer.getId() != null) ? customer.getId().toString() : "");
            this.txtFirstName.setText((customer.getFirstName() != null) ? customer.getFirstName() : "");
            this.txtLastName.setText((customer.getLastName() != null) ? customer.getLastName() : "");
            this.txtEmail.setText((customer.getEmail() != null) ? customer.getEmail() : "");
            this.txtPhone.setText((customer.getPhone() != null) ? customer.getPhone() : "");
            this.txtBirthday.setText((customer.getBirthday() != null) ? DATE_TIME_FORMATTER.format(customer.getBirthday()) : "");
            this.chkPatron.setSelected((customer.getPatron() != null) ? customer.getPatron() : false);
            this.txtAddress.setText((customer.getAddress() != null) ? customer.getAddress() : "");
            this.txtZipCode.setText((customer.getZipCode() != null) ? customer.getZipCode() : "");
            this.txtCity.setText((customer.getCity() != null) ? customer.getCity() : "");
            this.lblVideopoints.setText((customer.getVideopoints() != null) ? customer.getVideopoints().toString() : "0");
            try {
                this.filePhoto = customer.getPhoto();
                this.imgPhoto.setImage((this.filePhoto != null) ? new Image(new FileInputStream(this.filePhoto)) : NO_PHOTO);
            } catch (FileNotFoundException e) {
                this.imgPhoto.setImage(NO_PHOTO);
                this.filePhoto = null;
            }
        } else {
            this.lblCustomerID.setText("");
            this.txtFirstName.setText("");
            this.txtLastName.setText("");
            this.txtEmail.setText("");
            this.txtPhone.setText("");
            this.txtBirthday.setText("");
            this.chkPatron.setSelected(false);
            this.txtAddress.setText("");
            this.txtZipCode.setText("");
            this.txtCity.setText("");
            this.lblVideopoints.setText("0");
            this.imgPhoto.setImage(NO_PHOTO);
            this.filePhoto = null;
        }
    }

    @FXML
    private void selectPhoto(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Filmverleih | Lade Foto");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Bilddaten", "*.png", "*.jpg", "*.jepg"));
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (file != null && file.exists()) {
            try {
                this.imgPhoto.setImage(new Image(new FileInputStream(file)));
                this.filePhoto = file;
            } catch (FileNotFoundException e) {
                this.imgPhoto.setImage(NO_PHOTO);
            }
        }
    }

    @FXML
    private void delete() {
        Customer selectedCustomer = this.tblCustomers.getSelectionModel().getSelectedItem();
        this.customerObservableList.remove(selectedCustomer);
        this.tblCustomers.getSelectionModel().clearSelection();
    }

    @FXML
    private void persist() {
        String errorMessage = "";
        if (this.txtFirstName.getText().startsWith(" ") || this.txtFirstName.getText().endsWith(" ")) {
            errorMessage += "Der Vorname darf nicht mit einem Leerzeichen beginnen oder enden.\n";
        }
        if (this.txtFirstName.getText().length() < 3 || this.txtFirstName.getText().length() > 250) {
            errorMessage += "Der Vorname muss zwischen 3 und 250 Zeichen lang sein.\n";
        }
        if (this.txtLastName.getText().startsWith(" ") || this.txtLastName.getText().endsWith(" ")) {
            errorMessage += "Der Nachname darf nicht mit einem Leerzeichen beginnen oder enden.\n";
        }
        if (this.txtLastName.getText().length() < 3 || this.txtLastName.getText().length() > 250) {
            errorMessage += "Der Nachname muss zwischen 3 und 250 Zeichen lang sein.\n";
        }
        if (!this.txtEmail.getText().isEmpty() && !this.txtEmail.getText().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            errorMessage += "Die eingegebene Email Adresse ist ungültig.\n";
        }
        if (!this.txtPhone.getText().isEmpty() && this.txtPhone.getText().length() < 3 || this.txtPhone.getText().length() > 250) {
            errorMessage += "Der Telefonnummer muss zwischen 3 und 250 Zeichen lang sein.\n";
        }
        if (this.txtAddress.getText().startsWith(" ") || this.txtAddress.getText().endsWith(" ")) {
            errorMessage += "Die Adresse darf nicht mit einem Leerzeichen beginnen oder enden.\n";
        }
        if (this.txtAddress.getText().length() < 3 || this.txtAddress.getText().length() > 1000) {
            errorMessage += "Die Adresse muss zwischen 3 und 1000 Zeichen lang sein.\n";
        }
        if (this.txtCity.getText().startsWith(" ") || this.txtCity.getText().endsWith(" ")) {
            errorMessage += "Die Stadt darf nicht mit einem Leerzeichen beginnen oder enden.\n";
        }
        if (this.txtCity.getText().length() < 3 || this.txtCity.getText().length() > 250) {
            errorMessage += "Die Stadt muss zwischen 3 und 250 Zeichen lang sein.\n";
        }
        if (this.txtZipCode.getText().startsWith(" ") || this.txtZipCode.getText().endsWith(" ")) {
            errorMessage += "Die Postleitzahl darf nicht mit einem Leerzeichen beginnen oder enden.\n";
        }
        if (this.txtZipCode.getText().length() < 2 || this.txtZipCode.getText().length() > 250) {
            errorMessage += "Die Postleitzahl muss zwischen 2 und 250 Zeichen lang sein.\n";
        }
        try {
            LocalDate bday = LocalDate.parse(this.txtBirthday.getText()
                    .replaceAll("\\s", "")
                    .replaceAll("(^\\d\\.)", "0$1")
                    .replaceAll("(\\.)(\\d\\.)", ".0$2")
                    .replaceAll("\\.", ". "), DATE_TIME_FORMATTER);
            if (bday.isBefore(LocalDate.now().minusYears(120L)) || bday.isAfter(LocalDate.now())) {
                errorMessage += "Das eingegebene Geburtsdatum darf maximal 120 Jahre in der Vergangenheit liegen.\n";
            }
        } catch (DateTimeParseException e) {
            errorMessage += "Das eingegebene Geburtsdatum ist ungültig.\n";
        }
        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filmverleih | Eingabefehler");
            alert.setHeaderText("Folgende Daten der Eingabe sind Fehlerhaft");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        } else {
            Customer customer = currentCustomer;
            if (currentCustomer == null) {
                customer = new Customer();
            }
            customer.setFirstName(this.txtFirstName.getText());
            customer.setLastName(this.txtLastName.getText());
            customer.setEmail(this.txtEmail.getText());
            customer.setPhone(this.txtPhone.getText());
            customer.setBirthday((this.txtBirthday.getText() != null && !this.txtBirthday.getText().isEmpty()) ?
                    LocalDate.parse(this.txtBirthday.getText()
                            .replaceAll("\\s", "")
                            .replaceAll("(^\\d\\.)", "0$1")
                            .replaceAll("(\\.)(\\d\\.)", ".0$2")
                            .replaceAll("\\.", ". "), DATE_TIME_FORMATTER) : null);
            customer.setAddress(this.txtAddress.getText());
            customer.setZipCode(this.txtZipCode.getText());
            customer.setCity(this.txtCity.getText());
            customer.setPatron(this.chkPatron.isSelected());
            customer.setPhoto(this.filePhoto);
            if (this.currentCustomer == null) {
                this.currentCustomer = this.customerDAO.create(customer);
                this.customerObservableList.add(currentCustomer);
            } else {
                this.customerDAO.update(customer);
            }
            this.tblCustomers.getSelectionModel().select(currentCustomer);
        }
    }

    @FXML
    private void reset() {
        this.tblCustomers.getSelectionModel().clearSelection();
        this.showCustomerDetails(null);
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
}
