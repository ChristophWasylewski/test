import at.ac.tuwien.qs.movierental.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertTrue;

/**
 * These are some old tests. I refactored the name of the class.
 * If anyone has the time please refactor these tests.
 */
public class OldTests {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd. MM. yyyy");
    private static Customer youngCustomer;
    private static Movie badMovie, goodMovie;
    private static Rental rental;
    private static RentalController rentalController;

    @BeforeClass
    public static void setUp() {
        youngCustomer = new Customer();
        badMovie = new Movie();
        goodMovie = new Movie();
        rental = new Rental();
        rentalController = new RentalController();
    }
    @Before
    public static void beforeEachTest() {
        //yountCustomer
       initialYoungCustomer();
        //badMovie
       initalBadMovie();
        //goodMovie
       initialGoodMovie();
    }

    @Test
    public void testRentalValidationGoodMovie() {
        // movie set
        rental.setMovie(goodMovie);
        testvalidateRental(rental);
    }

    @Test
    public void testRentalValidationNull() {
        // nothing set
        rental.setMovie(null);
        testvalidateRental(rental);
    }

    @Test
    public void testRentalValidationYoungCustomer() {
        // customer set
        rental.setCustomer(youngCustomer);
        testvalidateRental(rental);
    }
    @Test
    public void testRentalValidationYoungCustomerAndBadMovie() {
        // customer and bad movie set
        rental.setMovie(badMovie);
        testvalidateRental(rental);
    }
    @Test
    public void testRentalValidationYoungCustomerAndBadMovieLentDateSet() {
        // customer, bad movie and lent date set
        rental.setDateLent(LocalDate.now());
        testvalidateRental(rental);
    }
    @Test
    public void testRentalValidationYoungCustomerAndGoodMovie() {
        // customer and good movie set
        rental.setDateLent(null);
        rental.setMovie(goodMovie);
        testvalidateRental(rental);
    }
    @Test
    public void testRentalValidationYoungCustomerAndGoodMovieLentDateSet(){
        // customer, good movie and lent date set
        rental.setDateLent(LocalDate.now());
        testvalidateRental(rental);
    }

    /**
     * um doppelten Code zu vermeide
     * die Methode testvalidateRental(Rental rental) erstellt
     */
    private void testvalidateRental(Rental rental){
        try {
            rentalController.validateRental(rental);
            assertTrue(false);
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }
    /**
     * Auslagerung der initialisierung
     */
    private static void initialYoungCustomer(){
        youngCustomer.setId(1L);
        youngCustomer.setFirstName("Linda");
        youngCustomer.setLastName("Mayer");
        youngCustomer.setEmail("Lucy2001@hotmail.com");
        youngCustomer.setPhone("+43 555 61 61");
        youngCustomer.setBirthday(LocalDate.parse("01. 04. 2010", DATE_TIME_FORMATTER));
        youngCustomer.setAddress("Berggasse 11");
        youngCustomer.setZipCode("3002");
        youngCustomer.setCity("Purkersdorf");
        youngCustomer.setPatron(true);
        youngCustomer.setPhoto(null);
        youngCustomer.setVideopoints(2);
    }
    private static void initalBadMovie(){
        badMovie.setId(1L);
        badMovie.setTitle("SAW III");
        badMovie.setSubtitle("");
        badMovie.setGenre("Horror");
        badMovie.setAgeRating("FSK 18");
        badMovie.setLanguage("English");
        badMovie.setPriceInCents(200);
        badMovie.setDirector("");
        badMovie.setRating(3.2F);
        badMovie.setYearPublished(Year.of(2002));
        badMovie.setSeries(false);
        badMovie.setStock(3);
        badMovie.setCover(null);
    }
    private static void initialGoodMovie(){
        goodMovie.setId(2L);
        goodMovie.setTitle("Der Lorax");
        goodMovie.setSubtitle("");
        goodMovie.setGenre("Kinder");
        goodMovie.setAgeRating("FSK 0");
        goodMovie.setLanguage("German");
        goodMovie.setPriceInCents(120);
        goodMovie.setDirector("Chris Renaud");
        goodMovie.setRating(3.0F);
        goodMovie.setYearPublished(Year.of(2012));
        goodMovie.setSeries(false);
        goodMovie.setStock(8);
        goodMovie.setCover(null);
    }
}


