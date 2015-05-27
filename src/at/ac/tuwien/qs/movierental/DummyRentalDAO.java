package at.ac.tuwien.qs.movierental;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DummyRentalDAO implements RentalDAO {
    private AtomicLong atomicLong = new AtomicLong(0);

    public DummyRentalDAO(MovieDAO movieDAO, CustomerDAO customerDAO) {
        Rental rental;
        {
            rental = new Rental();
            rental.setId(atomicLong.addAndGet(1));
            rental.setDateLent(LocalDate.now().minusDays(1));
            rental.setCustomer(customerDAO.read(1L));
            rental.setMovie(movieDAO.read(4L));
            rentals.add(rental);
        }
        {
            rental = new Rental();
            rental.setId(atomicLong.addAndGet(1));
            rental.setDateLent(LocalDate.now().minusDays(5));
            rental.setCustomer(customerDAO.read(4L));
            rental.setMovie(movieDAO.read(1L));
            rentals.add(rental);
        }
        {
            rental = new Rental();
            rental.setId(atomicLong.addAndGet(1));
            rental.setDateLent(LocalDate.now().minusDays(3));
            rental.setCustomer(customerDAO.read(1L));
            rental.setMovie(movieDAO.read(2L));
            rentals.add(rental);
        }
        {
            rental = new Rental();
            rental.setId(atomicLong.addAndGet(1));
            rental.setDateLent(LocalDate.now().minusDays(14));
            rental.setCustomer(customerDAO.read(2L));
            rental.setMovie(movieDAO.read(5L));
            rentals.add(rental);
        }
    }

    private HashSet<Rental> rentals = new HashSet<>();

    @Override
    public Rental create(Rental rental) {
        rental.setId(atomicLong.addAndGet(1));
        rentals.add(rental);
        return rental;
    }

    @Override
    public Rental read(Long id) {
        for (Rental rental : rentals) {
            if (rental.getId() == id) {
                return rental;
            }
        }
        return null;
    }

    @Override
    public List<Rental> read() {
        ArrayList<Rental> r = new ArrayList<>(this.rentals);
        r.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
        return r;
    }

    @Override
    public Rental update(Rental rental) {
        this.rentals.add(rental);
        return rental;
    }

    @Override
    public void delete(Rental rental) {
        this.rentals.remove(rental);
    }

    @Override
    public List<Rental> readByCustomer(Customer customer) {
        ArrayList<Rental> r = new ArrayList<>();
        System.out.println("Looking up rentals for customer: " + customer);
        for (Rental rental : rentals) {
            if (rental.getCustomer().equals(customer)) {
                System.out.println("Adding rental:" + rental);
                r.add(rental);
            }
        }
        r.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
        return r;
    }
}
