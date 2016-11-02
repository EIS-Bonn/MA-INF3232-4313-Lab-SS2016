package de.uni.bonn.iai.eis.web.model.example;

import de.uni.bonn.iai.eis.web.model.CodelistSlice;
import de.uni.bonn.iai.eis.web.model.CodelistTransformation;

import java.util.Arrays;

public class ExampleCodelistTransformation1 extends CodelistTransformation {

    public ExampleCodelistTransformation1() {
        super.setName("Aragon organizational classification codelist 2016");
        super.setUri("http://data.openbudgets.eu/resource/codelist/estructura_organica_aragon_2016/");
        super.setSource("https://raw.githubusercontent.com/openbudgets/datasets/master/Aragon/2016/raw/Estructura%20Organica.csv");
        super.setDescription("The organizational classification codelist from Aragon of 2016");
        super.setColumns(Arrays.asList("EJERCICIO;CENTRO GESTOR;DESCRIPCION CORTA;DESCRIPCION LARGA".split(";")));
        super.setDelimiterChar(";");
        super.setTableType("csv");
        super.setCharset("ISO-8859-2");

        CodelistSlice codelistSlice = new CodelistSlice();
        codelistSlice.setDoSlice(false);
        codelistSlice.setKeyColumn("CENTRO GESTOR");
        codelistSlice.setLabelColumn("DESCRIPCION LARGA");
        super.addSlice(codelistSlice);
    }
}
