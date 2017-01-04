import at.ac.tuwien.qs.movierental.Movie;
import at.ac.tuwien.qs.movierental.NoMovieFoundException;
import at.ac.tuwien.qs.movierental.ServiceNotAvailableException;
import at.ac.tuwien.qs.movierental.TooManyMoviesFound;
import at.ac.tuwien.qs.movierental.MovieDataService;
import at.ac.tuwien.qs.movierental.RatingService;
import at.ac.tuwien.qs.movierental.SimpleRatingService;
import at.ac.tuwien.qs.movierental.TheMovieDbMovieDataService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Year;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestRatingService {

    private static RatingService ratingService;

    @BeforeClass
    public static void setUp() {
        MovieDataService movieDataService = new TheMovieDbMovieDataService();
        ratingService = new SimpleRatingService(movieDataService);
    }

    @Test(expected=TooManyMoviesFound.class)
    public void testTooManyMoviesFound() throws NoMovieFoundException, TooManyMoviesFound, ServiceNotAvailableException {
        Movie movie = new Movie();
        movie.setTitle("Bambi");
        ratingService.laodRatingForMovie(movie);
    }

    @Test(expected=NoMovieFoundException.class)
    public void testNoMovieFoundException() throws NoMovieFoundException, TooManyMoviesFound, ServiceNotAvailableException {
        Movie movie = new Movie();
        movie.setTitle("ABCDEFGHIJKLM");
        ratingService.laodRatingForMovie(movie);
    }

    // Remember to disconnect before running this test
    @Test(expected=ServiceNotAvailableException.class)
    public void testServiceNotAvailableException() throws NoMovieFoundException, TooManyMoviesFound, ServiceNotAvailableException {
        Movie movie = new Movie();
        movie.setTitle("Bambi");
        ratingService.laodRatingForMovie(movie);
    }

    // Remember to check TMDb if the rating value has changed when this test fails
    @Test
    public void testRatingValueIsCorrect() throws NoMovieFoundException, TooManyMoviesFound, ServiceNotAvailableException {
        Movie movie = new Movie();
        movie.setTitle("Bambi");
        movie.setYearPublished(Year.of(1942));
        assertThat(ratingService.laodRatingForMovie(movie), is(3.2f));
    }


}
