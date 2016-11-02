package de.uni.bonn.iai.eis.rdf.obeu;

import de.uni.bonn.iai.eis.rdf.Obeu;
import org.openrdf.model.IRI;

public enum BudgetPhase {
    DRAFT("Draft", Obeu.Dimension.BudgetPhase.DRAFT),
    REVISED("Revised", Obeu.Dimension.BudgetPhase.REVISED),
    APPROVED("Approved", Obeu.Dimension.BudgetPhase.APPROVED),
    EXECUTED("Executed", Obeu.Dimension.BudgetPhase.EXECUTED);

    public String name;
    public IRI iri;

    BudgetPhase(String name, IRI iri){
        this.name = name;
        this.iri = iri;
    }
}
