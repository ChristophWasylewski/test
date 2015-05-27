package at.ac.tuwien.qs.movierental;

import java.util.List;

public interface RentalDAO extends DAO<Rental>{

    List<Rental> readByCustomer(Customer customer);
}
