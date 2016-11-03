package de.uni.bonn.iai.eis.rdf.obeu;

import de.uni.bonn.iai.eis.rdf.Obeu;
import org.openrdf.model.IRI;

public enum OperationCharacter {
    EXPENDITURE("Expenditure", Obeu.Operation.EXPENDITURE),
    REVENUE("Revenue", Obeu.Operation.REVENUE);

    public IRI iri;
    public String name;

    OperationCharacter(String name, IRI iri) {
        this.name = name;
        this.iri = iri;
    }
}

