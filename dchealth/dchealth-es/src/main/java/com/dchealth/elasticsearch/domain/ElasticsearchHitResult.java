package com.dchealth.elasticsearch.domain;

import java.util.List;
import java.util.Map;

public class ElasticsearchHitResult {

    private Double score;
    private Map<String, List<String>> highlight;
    private List<String> matchedQueries;
    private List<String> ignored;
    private Map<String, List<String>> ignoredFieldValues;
    private Long version;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Map<String, List<String>> getHighlight() {
        return highlight;
    }

    public void setHighlight(Map<String, List<String>> highlight) {
        this.highlight = highlight;
    }

    public List<String> getMatchedQueries() {
        return matchedQueries;
    }

    public void setMatchedQueries(List<String> matchedQueries) {
        this.matchedQueries = matchedQueries;
    }

    public List<String> getIgnored() {
        return ignored;
    }

    public void setIgnored(List<String> ignored) {
        this.ignored = ignored;
    }

    public Map<String, List<String>> getIgnoredFieldValues() {
        return ignoredFieldValues;
    }

    public void setIgnoredFieldValues(Map<String, List<String>> ignoredFieldValues) {
        this.ignoredFieldValues = ignoredFieldValues;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
