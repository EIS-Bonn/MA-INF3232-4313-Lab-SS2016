package de.uni.bonn.iai.eis.rdf.obeu.classification;

import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.ComponentProperty;

public class Classification implements ComponentProperty {
    @Override
    public String getLabel() {
        return "Classification";
    }

    @Override
    public String getIri() {
        return Obeu.Classification.CLASSIFICATION.toString();
    }

}
