package de.uni.bonn.iai.eis.rdf.obeu.classification;

import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.ComponentProperty;

public class AdministrativeClassification implements ComponentProperty {
    @Override
    public String getLabel() {
        return "Administrative Classification";
    }

    @Override
    public String getIri() {
        return Obeu.Classification.ADMINISTRATIVE_CLASSIFICATION.toString();
    }

}
