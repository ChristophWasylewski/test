package at.ac.tuwien.qs.movierental;

import at.ac.tuwien.qs.movierental.ui.controls.BooleanCell;
import at.ac.tuwien.qs.movierental.ui.controls.EuroCell;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.time.Year;
import java.time.format.DateTimeParseException;

public class MovieManagementController {

    private static final Image NO_COVER = new Image(RentalController.class.getResourceAsStream("/images/cover.png"));

    @FXML
    private TableView<Movie> tblMovies;
    @FXML
    private TableColumn<Movie, Long> tcMovieID;
    @FXML
    private TableColumn<Movie, String> tcTitle;
    @FXML
    private TableColumn<Movie, String> tcSubtitle;
    @FXML
    private TableColumn<Movie, String> tcGenre;
    @FXML
    private TableColumn<Movie, String> tcAgeRating;
    @FXML
    private TableColumn<Movie, String> tcLanguage;
    @FXML
    private TableColumn<Movie, Year> tcYearPublished;
    @FXML
    private TableColumn<Movie, String> tcDirector;
    @FXML
    private TableColumn<Movie, Integer> tcPrice;
    @FXML
    private TableColumn<Movie, Float> tcRating;
    @FXML
    private TableColumn<Movie, Boolean> tcSeries;
    @FXML
    private TableColumn<Movie, Integer> tcStock;
    @FXML
    private TableColumn<Movie, Integer> tcAvailable;
    @FXML
    private TableColumn<Movie, Integer> tcLent;

    @FXML
    private Label lblMovieID;
    @FXML
    private TextField txtTitle;
    @FXML
    private ComboBox<String> cbxGenre;
    @FXML
    private ComboBox<String> cbxLanguage;
    @FXML
    private TextField txtDirector;
    @FXML
    private TextField txtYearPublished;
    @FXML
    private Slider sliStock;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtSubtitle;
    @FXML
    private ComboBox<String> cbxAgeRating;
    @FXML
    private TextField txtPriceInCent;
    @FXML
    private TextField txtRating;
    @FXML
    private CheckBox chkSeries;
    @FXML
    private Label lblAvailable;
    @FXML
    private Label lblLent;

    @FXML
    private ImageView imgCover;

    @FXML
    private TextField txtFilter;
    @FXML
    private ToggleButton tglFilter;

    private File fileCover;

    private Movie currentMovie;

    private ObservableList<Movie> movieObservableList;
    private FilteredList<Movie> filteredMovies;
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    static {
        NUMBER_FORMAT.setMaximumFractionDigits(0);
    }

    private MovieDAO movieDAO;
    private RatingService ratingService;

    @FXML
    private void initialize() {
        this.cbxGenre.getItems().addAll("Normal", "Kinder", "Klassiker", "Horror", "SciFi", "Fantasy");
        this.cbxLanguage.getItems().addAll("Deutsch", "English", "Français", "Español");
        this.cbxAgeRating.getItems().addAll("FSK 0", "FSK 6", "FSK 12", "FSK 16", "FSK 18", "RATED 21");
        this.showMovieDetails(null);
        this.tcMovieID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        this.tcTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        this.tcSubtitle.setCellValueFactory(cellData -> cellData.getValue().subtitleProperty());
        this.tcGenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        this.tcAgeRating.setCellValueFactory(cellData -> cellData.getValue().ageRatingProperty());
        this.tcLanguage.setCellValueFactory(cellData -> cellData.getValue().languageProperty());
        this.tcYearPublished.setCellValueFactory(cellData -> cellData.getValue().yearPublishedProperty());
        this.tcPrice.setCellValueFactory(cellData -> cellData.getValue().priceInCentsProperty());
        this.tcPrice.setCellFactory(param -> new EuroCell());
        this.tcDirector.setCellValueFactory(cellData -> cellData.getValue().directorProperty());
        this.tcRating.setCellValueFactory(cellData -> cellData.getValue().ratingProperty());
        this.tcSeries.setCellValueFactory(cellData -> cellData.getValue().seriesProperty());
        this.tcSeries.setCellFactory(param -> new BooleanCell());
        this.tcStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty());
        this.txtStock.textProperty().bindBidirectional(sliStock.valueProperty(), NUMBER_FORMAT);
        this.txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                this.tglFilter.setSelected(false);
            } else {
                this.tglFilter.setSelected(true);
            }
            filterTable(newValue);
        });
        this.tblMovies.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showMovieDetails(newValue));
    }

    public void setMovies(ObservableList<Movie> movieObservableList) {
        this.movieObservableList = movieObservableList;
        this.tblMovies.setItems(this.movieObservableList);
        this.filteredMovies = new FilteredList<>(this.movieObservableList, p -> true);
        SortedList<Movie> sortedData = new SortedList<>(this.filteredMovies);
        sortedData.comparatorProperty().bind(tblMovies.comparatorProperty());
        tblMovies.setItems(sortedData);
    }

    private void filterTable(String filter) {
        MovieManagementController.this.filteredMovies.setPredicate(movie -> {
            if (filter == null || filter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = filter.toLowerCase();
            return (movie.getId() != null && movie.getId().toString().toLowerCase().contains(lowerCaseFilter)) ||
                    (movie.getTitle() != null && movie.getTitle().toLowerCase().contains(lowerCaseFilter)) ||
                    (movie.getSubtitle() != null && movie.getSubtitle().toLowerCase().contains(lowerCaseFilter)) ||
                    (movie.getGenre() != null && movie.getGenre().toLowerCase().contains(lowerCaseFilter)) ||
                    (movie.getDirector() != null && movie.getDirector().toLowerCase().contains(lowerCaseFilter) ||
                            (movie.getYearPublished() != null && movie.getYearPublished().toString().toLowerCase().contains(lowerCaseFilter)));
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

    private void showMovieDetails(Movie movie) {
        this.currentMovie = movie;
        if (movie != null) {
            this.lblMovieID.setText((movie.getId() != null) ? movie.getId().toString() : null);
            this.txtTitle.setText((movie.getTitle() != null) ? movie.getTitle() : null);
            if (movie.getGenre() != null) {
                this.cbxGenre.getSelectionModel().select(movie.getGenre());
            } else {
                this.cbxGenre.getSelectionModel().clearSelection();
            }
            if (movie.getLanguage() != null) {
                this.cbxLanguage.getSelectionModel().select(movie.getLanguage());
            } else {
                this.cbxLanguage.getSelectionModel().clearSelection();
            }
            this.txtDirector.setText((movie.getDirector() != null) ? movie.getDirector() : null);
            this.txtYearPublished.setText((movie.getYearPublished() != null) ? movie.getYearPublished().toString() : null);
            this.sliStock.setValue((movie.getStock() != null) ? movie.getStock() : 0);
            this.txtStock.setText((movie.getStock() != null) ? movie.getStock().toString() : null);
            this.txtSubtitle.setText((movie.getSubtitle() != null) ? movie.getSubtitle() : null);
            if (movie.getAgeRating() != null) {
                this.cbxAgeRating.getSelectionModel().select(movie.getAgeRating());
            } else {
                this.cbxAgeRating.getSelectionModel().clearSelection();
            }
            this.txtPriceInCent.setText((movie.getPriceInCents() != null) ? movie.getPriceInCents().toString() : null);
            this.txtRating.setText((movie.getRating() != null) ? movie.getRating().toString() : null);
            this.chkSeries.setSelected((movie.getSeries() != null) ? movie.getSeries() : false);
            this.lblAvailable.setText("0");
            this.lblLent.setText("0");
            try {
                this.fileCover = movie.getCover();
                this.imgCover.setImage((this.fileCover != null) ? new Image(new FileInputStream(this.fileCover)) : NO_COVER);
            } catch (FileNotFoundException e) {
                this.imgCover.setImage(NO_COVER);
                this.fileCover = null;
            }
        } else {
            this.lblMovieID.setText("");
            this.txtTitle.setText("");
            this.cbxGenre.getSelectionModel().clearSelection();
            this.cbxLanguage.getSelectionModel().clearSelection();
            this.txtDirector.setText("");
            this.txtYearPublished.setText("");
            this.sliStock.setValue(0);
            this.txtStock.setText("0");
            this.txtSubtitle.setText("");
            this.cbxAgeRating.getSelectionModel().clearSelection();
            this.txtPriceInCent.setText("0");
            this.txtRating.setText("");
            this.chkSeries.setSelected(false);
            this.lblAvailable.setText("0");
            this.lblLent.setText("0");
            this.imgCover.setImage(NO_COVER);
            this.fileCover = null;
        }

    }

    @FXML
    private void selectCover(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Filmverleih | Lade Foto");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bilddaten", "*.png", "*.jpg", "*.jepg"));
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (file != null && file.exists()) {
            try {
                this.imgCover.setImage(new Image(new FileInputStream(file)));
                this.fileCover = file;
            } catch (FileNotFoundException e) {
                this.imgCover.setImage(NO_COVER);
            }
        }
    }

    @FXML
    private void delete() {
        Movie selectedMovie = this.tblMovies.getSelectionModel().getSelectedItem();
        this.movieObservableList.remove(selectedMovie);
        this.tblMovies.getSelectionModel().clearSelection();
    }

    @FXML
    private void persist() {
        String errorMessage = "";
        if (this.txtTitle.getText().length() < 3 || this.txtTitle.getText().length() > 250) {
            errorMessage += "Der Filmtitel darf nicht mit einem Leerzeichen beginnen oder enden.\n";
        }
        if (this.txtTitle.getText().length() < 3 || this.txtTitle.getText().length() > 250) {
            errorMessage += "Der Filmtitel muss zwischen 3 und 250 Zeichen lang sein.\n";
        }
        if (this.txtSubtitle.getText() != null && !this.txtSubtitle.getText().isEmpty() && (this.txtSubtitle.getText().length() < 3 || this.txtSubtitle.getText().length() > 250)) {
            errorMessage += "Der Subtitel darf nicht mit einem Leerzeichen beginnen oder enden.\n";
        }
        if (this.txtSubtitle.getText() != null && !this.txtSubtitle.getText().isEmpty() && this.txtSubtitle.getText().length() < 3 || this.txtSubtitle.getText().length() > 250) {
            errorMessage += "Der Subtitel muss zwischen 3 und 250 Zeichen lang sein.\n";
        }
        if (this.cbxGenre.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Es muss ein Genre ausgewählt werden.\n";
        }
        if (this.cbxAgeRating.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Es muss eine Altersfreigabe ausgewählt werden.\n";
        }
        if (this.cbxLanguage.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Es muss eine Sprache ausgewählt werden.\n";
        }
        try {
            float rating = Float.parseFloat(this.txtRating.getText());
            if (rating < 0 || rating > 5) {
                errorMessage += "Das Rating muss zwischen 0 und 5 liegen.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Es muss ein gültiges Rating eingegeben werden.\n";
        }
        if (this.txtYearPublished.getText() != null && !this.txtYearPublished.getText().isEmpty()) {
            try {
                Year year = Year.parse(this.txtYearPublished.getText());
                if (year.isBefore(Year.parse("1800")) || year.isAfter(Year.now())) {
                    errorMessage += "Das Erscheinungsjahr muss nach 1800 und nicht in der Zukunft liegen.\n";
                }
            } catch (DateTimeParseException e) {
                errorMessage += "Das eingegebene Erscheinungsjahr ist ungültig.\n";
            }
        }
        if (this.txtDirector.getText() != null && !this.txtDirector.getText().isEmpty() && (this.txtDirector.getText().length() < 3 || this.txtDirector.getText().length() > 250)) {
            errorMessage += "Der Name des Regisseur darf nicht mit einem Leerzeichen beginnen oder enden.\n";
        }
        if (this.txtDirector.getText() != null && !this.txtDirector.getText().isEmpty() && this.txtDirector.getText().length() < 3 || this.txtSubtitle.getText().length() > 250) {
            errorMessage += "Der Name des Regisseur muss zwischen 3 und 250 Zeichen lang sein.\n";
        }
        try {
            int stock = Integer.parseInt(this.txtStock.getText());
            if (stock < 0 || stock > 50) {
                errorMessage += "Die Anzahl der verfügbaren Filme muss zwischen 0 und 50 liegen.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Die Anzahl der verfügbaren Filme muss zwischen 0 und 50 liegen.\n";
        }
        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filmverleih | Eingabefehler");
            alert.setHeaderText("Folgende Daten der Eingabe sind Fehlerhaft");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        } else {
            Movie movie = currentMovie;
            if (currentMovie == null) {
                movie = new Movie();
            }
            this.fillMovieWithFieldData(movie);
            if (currentMovie == null) {
                currentMovie = this.movieDAO.create(movie);
                this.movieObservableList.add(currentMovie);
            } else {
                this.movieDAO.update(movie);
            }
            this.tblMovies.getSelectionModel().select(currentMovie);
        }
    }

    private void fillMovieWithFieldData(Movie movie) {
        movie.setTitle(this.txtTitle.getText());
        movie.setSubtitle(this.txtSubtitle.getText());
        movie.setGenre(this.cbxGenre.getSelectionModel().getSelectedItem());
        movie.setAgeRating(this.cbxAgeRating.getSelectionModel().getSelectedItem());
        movie.setLanguage(this.cbxLanguage.getSelectionModel().getSelectedItem());
        movie.setPriceInCents((this.txtPriceInCent.getText() != null && !this.txtPriceInCent.getText().isEmpty()) ? Integer.parseInt(this.txtPriceInCent.getText()) : null);
        movie.setDirector(this.txtDirector.getText());
        movie.setRating((this.txtRating.getText() != null && !this.txtRating.getText().isEmpty()) ? Float.parseFloat(this.txtRating.getText()) : null);
        movie.setYearPublished((this.txtYearPublished.getText() != null && !this.txtYearPublished.getText().isEmpty()) ? Year.parse(this.txtYearPublished.getText()) : null);
        movie.setSeries(this.chkSeries.isSelected());
        movie.setStock(Integer.parseInt(NUMBER_FORMAT.format(this.sliStock.getValue())));
    }

    @FXML
    private void reset() {
        this.tblMovies.getSelectionModel().clearSelection();
        this.showMovieDetails(null);
    }

    @FXML
    private void loadFromService() {
        Movie movie = new Movie();
        this.fillMovieWithFieldData(movie);
        try {
            String rating = this.ratingService.laodRatingForMovie(movie).toString();
            System.out.println(rating);
            this.txtRating.setText(rating);
        } catch (TooManyMoviesFound tooManyMoviesFound) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filmverleih | Bewertungsservice Fehler");
            alert.setHeaderText("Es wurden zu viele passende Filme gefunden.");
            alert.setContentText("Versuchen Sie die Suchanfrage zu konkretisieren.");
            alert.showAndWait();
        } catch (NoMovieFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filmverleih | Bewertungsservice Fehler");
            alert.setHeaderText("Es wurden keine passende Filme gefunden.");
            alert.setContentText("Versuchen Sie die Suchanfrage zu konkretisieren.");
            alert.showAndWait();
        } catch (ServiceNotAvailableException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filmverleih | Bewertungsservice Fehler");
            alert.setHeaderText("Ein unerwarteter Fehler ist aufgetreten.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setMovieDAO(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }
}

