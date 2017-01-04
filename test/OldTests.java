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
    private static Customer youngCustomer, oldCustomer;
    private static Movie badMovie, goodMovie;
    private static Rental rental;
    private static RentalController rentalController;

    @BeforeClass
    public static void setUp() {
        youngCustomer = new Customer();
        oldCustomer = new Customer();
        badMovie = new Movie();
        goodMovie = new Movie();
        rental = new Rental();
        rentalController = new RentalController();
    }
    @Before
    public void beforeEachTest() {
        //yountCustomer
       initialYoungCustomer();
        //badMovie
       initalBadMovie();
        //goodMovie
       initialGoodMovie();
       //oldCustomer
        initialOldCustomer();
       rental= new Rental();
       rentalController=new RentalController();

    }
    @After
    public void afterEachTest(){
        System.out.println("");
    }

    /**
     * rental is null --> ValidationException("Rental has to be not null");
     */
    @Test
    public void testRentalValidationRentalIsNull() {
        System.out.println("testRentalValidationRentalIsNull(): ");
        rental =null;
        testvalidateRental(rental);
    }

    /**
     * Only movie is set --> ValidationException("Kunde muss gesetzt sein.");
     */
    @Test
    public void testRentalValidationOnlyMovieIsSet() {
        System.out.println("testRentalValidationOnlyMovieIsSet(): ");
        rental.setMovie(goodMovie);
        testvalidateRental(rental);
    }
    /**
     * Only customer is set --> ValidationException("Film muss gesetzt sein.");
     */
    @Test
    public void testRentalValidationOnlyCostumerIsSet() {
        System.out.println("testRentalValidationOnlyCostumerIsSet(): ");
        rental.setCustomer(youngCustomer);
        testvalidateRental(rental);
    }

    /**
     * Nothing is set --> ValidationException("Kunde muss gesetzt sein.");
     */
    @Test
    public void testRentalValidationNothingIsSet() {
        System.out.println("testRentalValidationNothingIsSet(): ");
        testvalidateRental(rental);
    }

    /**
     * Customer and Movie is set --> ValidationException("Verleihtag muss gesetzt sein.");
     */
    @Test
    public void testRentalValidationCustomerAndMovieIsSet() {
        System.out.println("testRentalValidationCustomerAndMovieIsSet(): ");
        rental.setCustomer(youngCustomer);
        rental.setMovie(goodMovie);
        testvalidateRental(rental);
    }

    /**
     * All is set but young customer and bad movie --> ValidationException("Der Kunde erfüllt die Altersfreigabe nicht! (FSK 18)");
     */
    @Test
    public void testRentalValidationAllSetBadMovieYoungCustomer() {
        System.out.println("testRentalValidationAllSetBadMovieYoungCustomer(): ");

        rental.setMovie(badMovie);
        rental.setCustomer(youngCustomer);
        rental.setDateLent(LocalDate.now());
        testvalidateRental(rental);
    }

    /**
     * All is set and young customer and good movie --> passed
     */
    @Test
    public void testRentalValidationAllSetGoodMovieYoungCustomer() {
        System.out.println("testRentalValidationAllSetGoodMovieYoungCustomer(): ");
        rental.setMovie(goodMovie);
        rental.setCustomer(youngCustomer);
        rental.setDateLent(LocalDate.now());
        testvalidateRental(rental);
    }

    /**
     * All is set but old customer and bad movie --> passed
     */
    @Test
    public void testRentalValidationAllSetBadMovieOldCustomer() {
        System.out.println("testRentalValidationAllSetBadMovieOldCustomer(): ");
        rental.setMovie(badMovie);
        rental.setCustomer(oldCustomer);
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
                System.out.println("Test passed");
            } catch (ValidationException e) {
                System.out.println(e);
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
    private static void initialOldCustomer(){
        oldCustomer.setId(1L);
        oldCustomer.setFirstName("Linda");
        oldCustomer.setLastName("Mayer");
        oldCustomer.setEmail("Lucy2001@hotmail.com");
        oldCustomer.setPhone("+43 555 61 61");
        oldCustomer.setBirthday(LocalDate.parse("01. 04. 1970", DATE_TIME_FORMATTER));
        oldCustomer.setAddress("Berggasse 11");
        oldCustomer.setZipCode("3002");
        oldCustomer.setCity("Purkersdorf");
        oldCustomer.setPatron(true);
        oldCustomer.setPhoto(null);
        oldCustomer.setVideopoints(2);
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


