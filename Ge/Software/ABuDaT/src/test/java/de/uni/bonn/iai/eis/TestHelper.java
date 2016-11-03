package de.uni.bonn.iai.eis;

import org.junit.Assert;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class TestHelper {
    public static void assertModelsEqual(Model expectedModel, Model actualModel) {
        String expected = modelToString((Set<Statement>) expectedModel);
        String actual = modelToString((Set<Statement>) actualModel);

        assertModelsEqual(expected, actual);
    }

    private static void assertModelsEqual(String expected, String actual) {
        Assert.assertEquals(expected, actual);
    }


    public static void assertModelsEqual(String expected, Model actualModel, Map<String, String> replacements) {
        StringBuilder stringBuilder = new StringBuilder();
        actualModel.stream().sorted(new StatementComparator()).forEach(statement -> stringBuilder.append(statement.toString() + "\n"));
        String actual = stringBuilder.toString();

        assertModelsEqual(expected, actual);
    }

    private static String modelToString(Set<Statement> model) {
        StringBuilder stringBuilder = new StringBuilder();
        model.stream().sorted(new StatementComparator()).forEach(statement -> stringBuilder.append(statement.toString() + "\n"));
        return stringBuilder.toString();
    }


    public static Model buildNormalizedModel(Model model, Map<String, String> replacements) throws IOException {
        String json = modelToString(model);

        for (String regex : replacements.keySet()) {
            String pattern = Pattern.quote(regex);
            String replacement = replacements.get(regex);

            json = json.replaceAll(pattern, replacement);
        }

        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        return modelFromStream(inputStream);

    }

    public static String modelToString(Model model) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        RDFWriter writer = Rio.createWriter(RDFFormat.JSONLD, byteArrayOutputStream);
        writer.startRDF();

        for (Statement statement: model) {
            writer.handleStatement(statement);
        }

        writer.endRDF();

        byteArrayOutputStream.close();

        return new String(byteArrayOutputStream.toByteArray());
    }

    public static Model modelFromStream(InputStream inputStream, RDFFormat rdfFormat) throws IOException {
        RDFParser parser = Rio.createParser(rdfFormat);
        Model result = new LinkedHashModel();
        StatementCollector collector = new StatementCollector(result);
        parser.setRDFHandler(collector);

        parser.parse(inputStream, "http://example.com");

        inputStream.close();
        return result;
    }

    public static Model modelFromStream(InputStream inputStream) throws IOException {
        return modelFromStream(inputStream, RDFFormat.JSONLD);
    }


    // FIXME: 27.08.16
//    public static Map<String, String> createMaskings(Pipeline pipeline) {
//        Map<String, String> replacements = new HashMap<>();
//
//        replacements.put(pipeline.getId(), "[MASKED_PIPELINE_ID]");
//
//        for (Component component : pipeline.getComponents()) {
//            replacements.put(component.getId(), "[MASKED_COMPONENT_ID]");
//        }
//
//        for (Connection connection : pipeline.getConnections()) {
//            replacements.put(connection.getId(), "[MASKED_CONNECTION_ID]");
//        }
//
//        return replacements;
//    }
}