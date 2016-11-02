package de.uni.bonn.iai.eis.web.model.example;

import de.uni.bonn.iai.eis.web.model.CodelistSlice;
import de.uni.bonn.iai.eis.web.model.CodelistTransformation;

import java.util.Arrays;

public class ExampleCodelistTransformation2 extends CodelistTransformation {

    public ExampleCodelistTransformation2() {
        setName("Aragon functional classification codelist 2016");
        setUri("http://data.openbudgets.eu/resource/codelist/estructura_functional_aragon_2016/");
        setSource("https://raw.githubusercontent.com/openbudgets/datasets/master/Aragon/2016/raw/Estructura%20Funcional%20Prepared.csv");
        setDescription("The functional classification codelist from Aragon of 2016");
        setColumns(Arrays.asList("EJERCICIO;GRUPO;FUNCION;SUBFUNCION;PROGRAMA;DESCRIPCION CORTA;DESCRIPCION LARGA".split(";")));
        setDelimiterChar(";");
        setTableType("csv");
        setCharset("ISO-8859-2");

        CodelistSlice groupoSlice = new CodelistSlice();
        groupoSlice.setDoSlice(true);
        groupoSlice.setStartRow(2);
        groupoSlice.setEndRow(9);
        groupoSlice.setKeyColumn("GRUPO");
        groupoSlice.setLabelColumn("DESCRIPCION CORTA");
        addSlice(groupoSlice);

        CodelistSlice functionSlice = new CodelistSlice();
        functionSlice.setDoSlice(true);
        functionSlice.setStartRow(10);
        functionSlice.setEndRow(35);
        functionSlice.setKeyColumn("FUNCION");
        functionSlice.setLabelColumn("DESCRIPCION CORTA");
        addSlice(functionSlice);

        CodelistSlice subfunctionSlice = new CodelistSlice();
        subfunctionSlice.setDoSlice(true);
        subfunctionSlice.setStartRow(36);
        subfunctionSlice.setEndRow(91);
        subfunctionSlice.setKeyColumn("SUBFUNCION");
        subfunctionSlice.setLabelColumn("DESCRIPCION CORTA");
        addSlice(subfunctionSlice);

        CodelistSlice programaSlice = new CodelistSlice();
        programaSlice.setDoSlice(true);
        programaSlice.setStartRow(92);
        programaSlice.setEndRow(222);
        programaSlice.setKeyColumn("PROGRAMA");
        programaSlice.setLabelColumn("DESCRIPCION CORTA");
        addSlice(programaSlice);
    }
}
