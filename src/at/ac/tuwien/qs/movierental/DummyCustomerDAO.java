package at.ac.tuwien.qs.movierental;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DummyCustomerDAO implements CustomerDAO {

    private AtomicLong atomicLong = new AtomicLong(0);

    public DummyCustomerDAO() {
        Customer customer;
        {
            customer = new Customer();
            customer.setId(atomicLong.addAndGet(1));
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
            customers.add(customer);
        }
        {
            customer = new Customer();
            customer.setId(atomicLong.addAndGet(1));
            customer.setFirstName("Linda");
            customer.setLastName("Mayer");
            customer.setEmail("Lucy2001@hotmail.com");
            customer.setPhone("+43 555 61 61");
            customer.setBirthday(LocalDate.parse("01. 04. 2010", DATE_TIME_FORMATTER));
            customer.setAddress("Berggasse 11");
            customer.setZipCode("3002");
            customer.setCity("Purkersdorf");
            customer.setPatron(true);
            customer.setPhoto(null);
            customer.setVideopoints(2);
            customers.add(customer);
        }
        {
            customer = new Customer();
            customer.setId(atomicLong.addAndGet(1));
            customer.setFirstName("Paul");
            customer.setLastName("Wagner");
            customer.setEmail("paul.wagner@spambot.com");
            customer.setPhone("+43 222 12 12");
            customer.setBirthday(LocalDate.parse("21. 04. 2004", DATE_TIME_FORMATTER));
            customer.setAddress("Währinger Straße 102");
            customer.setZipCode("1118");
            customer.setCity("Wien");
            customer.setPatron(false);
            customer.setPhoto(null);
            customer.setVideopoints(50);
            customers.add(customer);
        }
        {
            customer = new Customer();
            customer.setId(atomicLong.addAndGet(1));
            customer.setFirstName("Sabine");
            customer.setLastName("Wild");
            customer.setEmail("swild@gmx.at");
            customer.setPhone(null);
            customer.setBirthday(LocalDate.parse("12. 12. 1999", DATE_TIME_FORMATTER));
            customer.setAddress("Kleine Pfarrgasse 26");
            customer.setZipCode("1020");
            customer.setCity("Wien");
            customer.setPatron(true);
            customer.setPhoto(null);
            customer.setVideopoints(42);
            customers.add(customer);
        }
        {
            customer = new Customer();
            customer.setId(atomicLong.addAndGet(1));
            customer.setFirstName("Veronika");
            customer.setLastName("Raab");
            customer.setEmail("veronika@raab.at");
            customer.setPhone("+43 664 500 800");
            customer.setBirthday(LocalDate.parse("30. 11. 1986", DATE_TIME_FORMATTER));
            customer.setAddress("Wallnerstraße 1-1A");
            customer.setZipCode("1010");
            customer.setCity("Wien");
            customer.setPatron(false);
            customer.setPhoto(null);
            customer.setVideopoints(21);
            customers.add(customer);
        }
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd. MM. yyyy");

    private HashSet<Customer> customers = new HashSet<>();

    @Override
    public Customer create(Customer customer) {
        customer.setId(atomicLong.addAndGet(1));
        customers.add(customer);
        return customer;
    }

    @Override
    public Customer read(Long id) {
        for(Customer customer : customers) {
            if(customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public List<Customer> read() {
        ArrayList<Customer> c = new ArrayList<>(this.customers);
        c.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
        return c;
    }

    @Override
    public Customer update(Customer customer) {
        this.customers.add(customer);
        return customer;
    }

    @Override
    public void delete(Customer customer) {
        this.customers.remove(customer);
    }

}
