package at.ac.tuwien.qs.movierental;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Year;
import java.util.*;


/**
 * Based on the Implementation on: http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
 */
public class TheMovieDbMovieDataService implements MovieDataService {

    private static final String API_KEY = "7cf77b2fecb0e4e630aa326f9eaafbe3";
    private static final String SEARCH_URL = "http://api.themoviedb.org/3/search/movie?api_key=" + API_KEY;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3";
    private static final Calendar CALENDAR = Calendar.getInstance();

    /**
     * Search movies in TMDb.
     * Don't forget that TMDb uses a different naming scheme instead of a title and a subtitle in different fields,#
     * they use on field for both values, separated by " - ".
     * Don't forget that TMDb uses a different rating range from 0 to 10.
     * @param movie which should be looked up
     * @return
     * @throws ServiceNotAvailableException
     */
    @Override
    public List<Movie> searchMovies(Movie movie) throws ServiceNotAvailableException {
        ArrayList<Movie> returnMovies = new ArrayList<>();
        try {
            String title = movie.getTitle();
            if (movie.getSubtitle() != null && !movie.getSubtitle().isEmpty()) {
                title += " " + movie.getSubtitle();
            }
            String year = "";
            if (movie.getYearPublished() != null) {
                year += movie.getYearPublished().toString();
            }
            URL url = null;
            url = new URL(SEARCH_URL + "&query=" + URLEncoder.encode(title, "UTF-8").replace("+", "%20") + "&primary_release_year=" + year);
            System.out.println("Sending Query: " + url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response was: " + response);
            ObjectMapper mapper = new ObjectMapper();
            TMDbResult tmDbResult = mapper.readValue(response.toString(), TMDbResult.class);
            for (TMDbMovie tmDbMovie : tmDbResult.movies) {
                Movie resultMovie = new Movie();
                String[] titles = tmDbMovie.originalTitle.split("\\-", 2);
                resultMovie.setTitle(titles[0].trim());
                if (titles.length == 2) {
                    resultMovie.setSubtitle(titles[1].trim());
                }
                resultMovie.setRating(tmDbMovie.voteAverage);
                CALENDAR.setTime(tmDbMovie.releaseDate);
                resultMovie.setYearPublished(Year.of(CALENDAR.get(Calendar.YEAR)));
                returnMovies.add(resultMovie);
            }
        } catch (IOException e) {
            throw new ServiceNotAvailableException(e);
        }
        return returnMovies;
    }
}
