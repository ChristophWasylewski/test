package at.ac.tuwien.qs.movierental;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDbResult {

    @JsonProperty("results")
    public ArrayList<TMDbMovie> movies;

    @JsonProperty("total_results")
    public Integer totalResults;

}