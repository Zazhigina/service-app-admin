package igc.mirror.utils.qfilter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SearchCriteria {
    public SearchCriteria() {
    }

    @JsonIgnore
    public String getLikePattern(String value){
        return new StringBuilder("%")
                .append(value)
                .append("%")
                .toString();
    }

    @JsonIgnore
    public String getLikePatternLeft(String value){
        return new StringBuilder("%")
                .append(value)
                .toString();
    }

    @JsonIgnore
    public String getLikePatternRight(String value){
        return new StringBuilder()
                .append(value)
                .append("%")
                .toString();
    }
}
