package de.uni.bonn.iai.eis.rdf.obeu.classification;

import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.ComponentProperty;

public class ProgrammeClassification implements ComponentProperty {
    @Override
    public String getLabel() {
        return "Programme Classification";
    }

    @Override
    public String getIri() {
        return Obeu.Classification.PROGRAMME_CLASSIFICATION.toString();
    }

}
