package at.ac.tuwien.qs.movierental;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceService {

    void payInvoice(Customer customer, List<Rental> returnedList, LocalDate generationDate);

    String generateIncoicePreview(Customer customer, List<Rental> returnedList, List<Rental> lentList, LocalDateTime generationDateTime);
}
