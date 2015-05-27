package at.ac.tuwien.qs.movierental;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SimpleInvoiceService implements InvoiceService {

    private static final DecimalFormat EURO_FORMATTER = new DecimalFormat("'€ '0.00");
    private static final DecimalFormat PERCENT_FORMATTER = new DecimalFormat("0.0'%'");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd. LLL yyyy / HH:mm");

    public void payInvoice(Customer customer, List<Rental> returnedList, LocalDate generationDate) {
        long priceInCent = getTotalPriceInCents(customer, returnedList, generationDate);
        if (returnedList.size() > 0) {
            if (customer.getVideopoints() > 10) {
                customer.setVideopoints(customer.getVideopoints() - 10);
            }
            if (customer.getVideopoints() > 20) {
                customer.setVideopoints(customer.getVideopoints() - 20);
            }
        }
        customer.setVideopoints(customer.getVideopoints() + customer.calculateVideopointsGain(priceInCent, customer.getPatron()));
    }

    public long getTotalPriceInCents(Customer customer, List<Rental> returnedList, LocalDate generationDate) {
        long priceInCent = 0;
        for (Rental rental : returnedList) {
            priceInCent = customer.calculatePriceForRental(rental, generationDate);
        }
        priceInCent = (long) (priceInCent - (priceInCent / 100 * customer.calculateDiscount()));
        return priceInCent;
    }


    public String generateIncoicePreview(Customer customer, List<Rental> returnedList, List<Rental> lentList, LocalDateTime generationTime) {
        StringBuilder invoiceStringBuilder = new StringBuilder("");
        invoiceStringBuilder.append("Kundennummer:").append(String.format("%37s", customer.getId())).append("\n");
        invoiceStringBuilder.append("Kunde:").append(String.format("%44s", customer.getLastName().toUpperCase() + ", " + customer.getFirstName())).append("\n");
        invoiceStringBuilder.append("Datum/Zeit:").append(String.format("%39s", DATE_TIME_FORMATTER.format(generationTime))).append("\n");
        if (!returnedList.isEmpty()) {
            invoiceStringBuilder.append("\n").append("----------------- Zurückgebracht -----------------").append("\n\n");
            for (Rental rental : returnedList) {
                Movie movie = rental.getMovie();
                StringBuilder movieBuilder = new StringBuilder();
                movieBuilder.append(movie.getId())
                        .append("; ")
                        .append(movie.getTitle())
                        .append((movie.getSubtitle() != null && !movie.getSubtitle().isEmpty()) ? " - " + movie.getSubtitle() : "");
                String movieShortname = movieBuilder.toString();
                if (movieShortname.length() > 33) {
                    movieShortname = movieShortname.substring(0, 30) + "...";
                }
                String price = EURO_FORMATTER.format(customer.calculatePriceForRental(rental, generationTime.toLocalDate()) / 100.0);
                invoiceStringBuilder.append(String.format("%-33s %16s", movieShortname, price))
                        .append("\n");
            }
        }
        if (!lentList.isEmpty()) {
            invoiceStringBuilder.append("\n").append("------------------- Ausgeborgt -------------------").append("\n\n");
            for (Rental rental : lentList) {
                Movie movie = rental.getMovie();
                StringBuilder movieBuilder = new StringBuilder();
                movieBuilder.append(movie.getId())
                        .append("; ")
                        .append(movie.getTitle())
                        .append((movie.getSubtitle() != null && !movie.getSubtitle().isEmpty()) ? " - " + movie.getSubtitle() : "");
                String movieShortname = movieBuilder.toString();
                if (movieShortname.length() > 33) {
                    movieShortname = movieShortname.substring(0, 30) + "...";
                }
                String price = EURO_FORMATTER.format(customer.calculatePriceForRental(rental, generationTime.toLocalDate()) / 100.0) + "/tag";
                invoiceStringBuilder.append(String.format("%-33s %16s", movieShortname, price))
                        .append("\n");
            }
        }
        long totalPriceInCents = this.getTotalPriceInCents(customer, returnedList, generationTime.toLocalDate());
        int oldVideopoints = customer.getVideopoints();
        int videopointsUsed = 0;
        int videopointsGained = 0;
        if (returnedList.size() > 0) {
            if (oldVideopoints > 20) {
                videopointsUsed = 20;
            } else if (oldVideopoints > 10) {
                videopointsUsed = 10;
            }
            videopointsGained = customer.calculateVideopointsGain(totalPriceInCents, customer.getPatron());
        }
        int newVideopoints = oldVideopoints + videopointsGained - videopointsUsed;
        invoiceStringBuilder.append("\n");
        invoiceStringBuilder.append("------------------- Videopoints ------------------").append("\n\n");
        invoiceStringBuilder.append("Bisherige Videopoints:").append(String.format("%28s", oldVideopoints)).append("\n");
        invoiceStringBuilder.append("Verbrauch Videopoints:").append(String.format("%28s", videopointsUsed)).append("\n");
        invoiceStringBuilder.append("Gutschrift Videopoints:").append(String.format("%27s", videopointsGained)).append("\n");
        invoiceStringBuilder.append("Neue Videopoints:").append(String.format("%33s", newVideopoints)).append("\n");
        invoiceStringBuilder.append("\n");
        invoiceStringBuilder.append("--------------------- Rabatte --------------------").append("\n\n");
        invoiceStringBuilder.append("Stammkunde:").append(String.format("%39s", (customer.getPatron()) ? "Ja" : "Nein")).append("\n");
        invoiceStringBuilder.append("Rabatt:").append(String.format("%43s", PERCENT_FORMATTER.format(customer.calculateDiscount()))).append("\n");
        invoiceStringBuilder.append("\n");
        invoiceStringBuilder.append("------------------- Abrechnung -------------------").append("\n\n");
        invoiceStringBuilder.append("Zu zahlender Betrag:").append(String.format("%30s", EURO_FORMATTER.format(totalPriceInCents / 100.0))).append("\n");
        return invoiceStringBuilder.toString();
    }

}
