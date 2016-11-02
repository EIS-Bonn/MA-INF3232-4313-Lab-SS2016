package de.uni.bonn.iai.eis.rdf.obeu.classification;

import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.ComponentProperty;

public class FunctionalClassification implements ComponentProperty {
    @Override
    public String getLabel() {
        return "Functional Classification";
    }

    @Override
    public String getIri() {
        return Obeu.Classification.FUNCTIONAL_CLASSIFICATION.toString();
    }

}
