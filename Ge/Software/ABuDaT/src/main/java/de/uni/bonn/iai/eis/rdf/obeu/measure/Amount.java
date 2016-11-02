package de.uni.bonn.iai.eis.rdf.obeu.measure;

import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.ComponentProperty;

public class Amount implements ComponentProperty {

    @Override
    public String getLabel() {
        return "Amount";
    }

    @Override
    public String getIri() {
        return Obeu.Measure.AMOUNT.toString();
    }

}
