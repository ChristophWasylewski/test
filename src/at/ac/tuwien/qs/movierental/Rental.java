package at.ac.tuwien.qs.movierental;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;

public class Rental {

    private ObjectProperty<Long> id = new SimpleObjectProperty<>(null);
    private ObjectProperty<LocalDate> dateLent = new SimpleObjectProperty<>(null);
    private Movie movie;
    private Customer customer;

    public Long getId() {
        return id.get();
    }

    public ObjectProperty<Long> idProperty() {
        return id;
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public LocalDate getDateLent() {
        return dateLent.get();
    }

    public ObjectProperty<LocalDate> dateLentProperty() {
        return dateLent;
    }

    public void setDateLent(LocalDate dateLent) {
        this.dateLent.set(dateLent);
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rental rental = (Rental) o;

        return !(id != null ? !id.equals(rental.id) : rental.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
