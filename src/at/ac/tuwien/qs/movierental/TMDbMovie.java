package at.ac.tuwien.qs.movierental;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDbMovie {

    @JsonProperty("original_title")
    public String originalTitle;

    @JsonProperty("popularity")
    public Float popularity;

    @JsonProperty("vote_average")
    public Float voteAverage;

    @JsonProperty("vote_count")
    public Integer voteCount;

    @JsonProperty("release_date")
    public Date releaseDate;

}