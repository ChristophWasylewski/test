package at.ac.tuwien.qs.movierental;

import java.util.List;

public class SimpleRatingService implements RatingService {

    private MovieDataService movieDataService;

    public SimpleRatingService(MovieDataService movieDataService) {
        this.movieDataService = movieDataService;
    }

    @Override
    public Float laodRatingForMovie(Movie movie) throws TooManyMoviesFound, NoMovieFoundException, ServiceNotAvailableException {
        List<Movie> movieList = movieDataService.searchMovies(movie);
        if (movieList.size() == 0) {
            throw new NoMovieFoundException();
        } else if (movieList.size() > 1) {
            throw new TooManyMoviesFound();
        } else {
            Movie ratedMovie = movieList.get(0);
            if (ratedMovie.getRating() == null || ratedMovie.getRating() < 0 || ratedMovie.getRating() > 5) {
                throw new ServiceNotAvailableException("The service returned an unexpected Value.");
            }
            return ratedMovie.getRating();
        }
    }
}
