package de.uni.bonn.iai.eis.rdf.obeu.classification;

import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.ComponentProperty;

public class EconomicClassification implements ComponentProperty {
    @Override
    public String getLabel() {
        return "Economic Classification";
    }

    @Override
    public String getIri() {
        return Obeu.Classification.ECONOMIC_CLASSIFICATION.toString();
    }

}
