package at.ac.tuwien.qs.movierental;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.time.Year;

public class Movie {

    private ObjectProperty<Long> id = new SimpleObjectProperty<>(null);
    private ObjectProperty<String> title = new SimpleObjectProperty<>(null);
    private ObjectProperty<String> subtitle = new SimpleObjectProperty<>(null);
    private ObjectProperty<String> genre = new SimpleObjectProperty<>(null);
    private ObjectProperty<String> ageRating = new SimpleObjectProperty<>(null);
    private ObjectProperty<String> language = new SimpleObjectProperty<>(null);
    private ObjectProperty<Integer> priceInCents = new SimpleObjectProperty<>(null);
    private ObjectProperty<String> director = new SimpleObjectProperty<>(null);
    private ObjectProperty<Float> rating = new SimpleObjectProperty<>(null);
    private ObjectProperty<Year> yearPublished = new SimpleObjectProperty<>(null);
    private ObjectProperty<Boolean> series = new SimpleObjectProperty<>(false);
    private ObjectProperty<Integer> stock = new SimpleObjectProperty<>(0);
    private ObjectProperty<File> cover = new SimpleObjectProperty<>(null);

    public Long getId() {
        return id.get();
    }

    public ObjectProperty<Long> idProperty() {
        return id;
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public ObjectProperty<String> titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getSubtitle() {
        return subtitle.get();
    }

    public ObjectProperty<String> subtitleProperty() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle.set(subtitle);
    }

    public String getGenre() {
        return genre.get();
    }

    public ObjectProperty<String> genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public String getAgeRating() {
        return ageRating.get();
    }

    public ObjectProperty<String> ageRatingProperty() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating.set(ageRating);
    }

    public String getLanguage() {
        return language.get();
    }

    public ObjectProperty<String> languageProperty() {
        return language;
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public Integer getPriceInCents() {
        return priceInCents.get();
    }

    public ObjectProperty<Integer> priceInCentsProperty() {
        return priceInCents;
    }

    public void setPriceInCents(Integer priceInCents) {
        this.priceInCents.set(priceInCents);
    }

    public String getDirector() {
        return director.get();
    }

    public ObjectProperty<String> directorProperty() {
        return director;
    }

    public void setDirector(String director) {
        this.director.set(director);
    }

    public Float getRating() {
        return rating.get();
    }

    public ObjectProperty<Float> ratingProperty() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating.set(rating);
    }

    public Year getYearPublished() {
        return yearPublished.get();
    }

    public ObjectProperty<Year> yearPublishedProperty() {
        return yearPublished;
    }

    public void setYearPublished(Year yearPublished) {
        this.yearPublished.set(yearPublished);
    }

    public Boolean getSeries() {
        return series.get();
    }

    public ObjectProperty<Boolean> seriesProperty() {
        return series;
    }

    public void setSeries(Boolean series) {
        this.series.set(series);
    }

    public Integer getStock() {
        return stock.get();
    }

    public ObjectProperty<Integer> stockProperty() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock.set(stock);
    }

    public File getCover() {
        return cover.get();
    }

    public ObjectProperty<File> coverProperty() {
        return cover;
    }

    public void setCover(File cover) {
        this.cover.set(cover);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return !(id != null ? !id.equals(movie.id) : movie.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title=" + title +
                ", subtitle=" + subtitle +
                ", genre=" + genre +
                ", ageRating=" + ageRating +
                ", language=" + language +
                ", priceInCents=" + priceInCents +
                ", director=" + director +
                ", rating=" + rating +
                ", yearPublished=" + yearPublished +
                ", series=" + series +
                ", stock=" + stock +
                ", cover=" + cover +
                '}';
    }
}
