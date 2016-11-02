package de.uni.bonn.iai.eis;

import org.openrdf.model.Statement;

import java.util.Comparator;
import java.util.Objects;

public class StatementComparator implements Comparator<Statement> {
    @Override
    public int compare(Statement o1, Statement o2) {
        final int result;

        int subjectComparison = o1.getSubject().toString().compareTo(o2.getSubject().toString());

        if (subjectComparison == 0) {
            int predicateComparison = o1.getPredicate().toString().compareTo(o2.getPredicate().toString());

            if (predicateComparison == 0) {
                result =  o1.getObject().toString().compareTo(o2.getObject().toString());
            } else {
                result = predicateComparison;
            }

        } else {
            result = subjectComparison;
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(null, obj);
    }
}