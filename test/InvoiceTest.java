import at.ac.tuwien.qs.movierental.Customer;
import at.ac.tuwien.qs.movierental.Movie;
import at.ac.tuwien.qs.movierental.Rental;
import at.ac.tuwien.qs.movierental.SimpleInvoiceService;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class InvoiceTest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd. MM. yyyy");

    @Test
    public void testIncvoicePreview() {
        LocalDateTime now = LocalDateTime.parse("2007-12-03T10:15:30");
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Johann");
        customer.setLastName("Fischer");
        customer.setEmail("angler@gmail.com");
        customer.setPhone("+43 674 156 45 78");
        customer.setBirthday(LocalDate.parse("23. 05. 1960", DATE_TIME_FORMATTER));
        customer.setAddress("Neustiftgasse 31");
        customer.setZipCode("1070");
        customer.setCity("Wien");
        customer.setPatron(true);
        customer.setPhoto(null);
        customer.setVideopoints(12);
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Der Herr der Ringe");
        movie1.setSubtitle("Die Gefährten");
        movie1.setGenre("Fantasy");
        movie1.setAgeRating("FSK 12");
        movie1.setLanguage("German");
        movie1.setPriceInCents(300);
        movie1.setDirector("Peter Jackson");
        movie1.setRating(4.9F);
        movie1.setYearPublished(Year.of(2001));
        movie1.setSeries(true);
        movie1.setStock(10);
        movie1.setCover(null);
        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Der Herr der Ringe");
        movie2.setSubtitle("Die zwei Türme");
        movie2.setGenre("Fantasy");
        movie2.setAgeRating("FSK 12");
        movie2.setLanguage("German");
        movie2.setPriceInCents(400);
        movie2.setDirector("Peter Jackson");
        movie2.setRating(4.6F);
        movie2.setYearPublished(Year.of(2002));
        movie2.setSeries(true);
        movie2.setStock(8);
        movie2.setCover(null);
        Rental rental1 = new Rental();
        rental1.setId(1L);
        rental1.setDateLent(now.toLocalDate().minusDays(1));
        rental1.setCustomer(customer);
        rental1.setMovie(movie1);
        Rental rental2 = new Rental();
        rental2.setId(2L);
        rental2.setDateLent(now.toLocalDate());
        rental2.setCustomer(customer);
        rental2.setMovie(movie2);
        SimpleInvoiceService simpleInvoiceService = new SimpleInvoiceService();
        ArrayList<Rental> rentalList1 = new ArrayList<>();
        rentalList1.add(rental1);
        ArrayList<Rental> rentalList2 = new ArrayList<>();
        rentalList2.add(rental2);
        assertThat(simpleInvoiceService.generateIncoicePreview(customer, rentalList1, rentalList2, now), is(
                "Kundennummer:                                    1\n" +
                        "Kunde:                             FISCHER, Johann\n" +
                        "Datum/Zeit:                   03. Dez 2007 / 10:15\n" +
                        "\n" +
                        "----------------- Zurückgebracht -----------------\n" +
                        "\n" +
                        "1; Der Herr der Ringe - Die Ge...           € 7,50\n" +
                        "\n" +
                        "------------------- Ausgeborgt -------------------\n" +
                        "\n" +
                        "2; Der Herr der Ringe - Die zw...       € 5,00/tag\n" +
                        "\n" +
                        "------------------- Videopoints ------------------\n" +
                        "\n" +
                        "Bisherige Videopoints:                          12\n" +
                        "Verbrauch Videopoints:                          10\n" +
                        "Gutschrift Videopoints:                          4\n" +
                        "Neue Videopoints:                                6\n" +
                        "\n" +
                        "--------------------- Rabatte --------------------\n" +
                        "\n" +
                        "Stammkunde:                                     Ja\n" +
                        "Rabatt:                                       3,5%\n" +
                        "\n" +
                        "------------------- Abrechnung -------------------\n" +
                        "\n" +
                        "Zu zahlender Betrag:                        € 7,25\n"
        ));
    }
}
