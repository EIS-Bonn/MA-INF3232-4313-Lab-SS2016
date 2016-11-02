package de.uni.bonn.iai.eis.web.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Embeddable
public class SparqlUpdate {
    @Type(type="text")
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
