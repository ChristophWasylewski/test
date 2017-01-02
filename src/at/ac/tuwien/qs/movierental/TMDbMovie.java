package at.ac.tuwien.qs.movierental;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDbMovie {

    /**
     * CheckStyle
     * originalTitel muss private sein
     */
    @JsonProperty("original_title")
    private String originalTitle;
    /**
     * CheckStyle
     * popularity muss private sein
     */
    @JsonProperty("popularity")
    private Float popularity;
    /** CheckStyle
     * voteAverage muss private sein
     */
    @JsonProperty("vote_average")
    private Float voteAverage;
    /**
     * CheckStyle
     * voteCount muss private sein
     */
    @JsonProperty("vote_count")
    private Integer voteCount;
    /**
     * CheckStyle
     * releaseDate muss private sein
     */
    @JsonProperty("release_date")
    private Date releaseDate;

}