package de.uni.bonn.iai.eis.etl.linkedpipes;

import com.linkedpipes.etl.component.api.service.RdfToPojo;
import com.linkedpipes.plugin.transformer.tabularuv.TabularConfig_V2;
import com.linkedpipes.plugin.transformer.tabularuv.parser.ParserType;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.vocabulary.RDF;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class to generate an RDF model from a linkedpipes component wrapper class.
 */
public class ConfigToRdf {
    private ValueFactory valueFactory = SimpleValueFactory.getInstance();

    /**
     * Class implementing functionality for mapping from a field annotated with linkedpipes RdfToPojo.Property.class
     * to an RDF Statement.
     */
    private class Mapper implements Function<Field, Optional<Statement>> {

        private Object parentObject;
        private String configGraphURI;
        private String resourceConfigURI;

        Mapper(Object object, String resourceConfigURI, String configGraphURI) {
            this.parentObject = object;
            this.configGraphURI = configGraphURI;
            this.resourceConfigURI = resourceConfigURI;
        }

        @Override
        public Optional<Statement> apply(Field field) {
            Object object;
            try {
                field.setAccessible(true);
                object = field.get(parentObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (object == null) {
                return Optional.empty();
            }

            String fieldAnnotatedURI = field.getAnnotation(RdfToPojo.Property.class).uri();

            Optional<Literal> valueLiteral = getLiteral(object);

            if (!valueLiteral.isPresent()) {
                throw new RuntimeException("Unknown type, cant write literal for field: " + field.getName()
                        + " with type " + field.getType().getSimpleName() + " and URI " + fieldAnnotatedURI);
            }

            Statement statement = valueFactory.createStatement(
                    valueFactory.createIRI(resourceConfigURI),
                    valueFactory.createIRI(fieldAnnotatedURI),
                    valueLiteral.get(),
                    valueFactory.createIRI(configGraphURI));

            return Optional.of(statement);
        }

        /**
         * Create an rdf literal for a given object
         *
         * @param object
         * @return the rdf literal
         */
        private Optional<Literal> getLiteral(Object object) {
            Literal valueLiteral = null;

            if (object instanceof Boolean) {
                valueLiteral = valueFactory.createLiteral((boolean) object);
            } else if (object instanceof String) {
                valueLiteral = valueFactory.createLiteral((String) object);
            } else if (object instanceof Integer) {
                valueLiteral = valueFactory.createLiteral((int) object);
            } else if (object instanceof ParserType) {
                String enumElemName = ((ParserType) object).name();
                valueLiteral = valueFactory.createLiteral(enumElemName);
            } else if (object instanceof TabularConfig_V2.ColumnType) {
                String enumElemName = ((TabularConfig_V2.ColumnType) object).name();
                valueLiteral = valueFactory.createLiteral(enumElemName);
            }

            return Optional.ofNullable(valueLiteral);
        }
    }

    /**
     * Method to generate an RDF model from an object with fields annotated with the linkedpipes RdfToPojo.Property.class
     *
     * It iterates the annotated fields and creates rdf triples for well-known fields.
     *
     * @param object            the object to generate the model from
     * @param resourceConfigURI the resource config uri
     * @param configGraphURI    the configuration graph uri
     * @return the generated model
     */
    public Model modelFromAnnotatedObject(Object object, String resourceConfigURI, String configGraphURI) {
        Model model = new LinkedHashModel();

        Field[] fields = object.getClass().getDeclaredFields();

        List<Field> allAnnotatedFields = Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(RdfToPojo.Property.class))
                .collect(Collectors.toList());

        allAnnotatedFields.stream().filter(field -> field.getType() != List.class)
                .map(new Mapper(object, resourceConfigURI, configGraphURI))
                .filter(Optional::isPresent)
                .forEach(statement -> model.add(statement.get()));

        allAnnotatedFields.stream().filter(field -> field.getType() == List.class).forEach(field -> {
            try {
                field.setAccessible(true);

                @SuppressWarnings("uncheckd")
                List<Object> annotatedList = (List<Object>) field.get(object);

                annotatedList.forEach(o -> {
                    String temporaryResourceConfigURI = "http://localhost/resources/temp/" + UUID.randomUUID().toString();
                    Model m = modelFromAnnotatedObject(o, temporaryResourceConfigURI, configGraphURI);

                    String fieldAnnotatedURI = field.getAnnotation(RdfToPojo.Property.class).uri();

                    Statement statement = valueFactory.createStatement(
                            valueFactory.createIRI(resourceConfigURI),
                            valueFactory.createIRI(fieldAnnotatedURI),
                            valueFactory.createIRI(temporaryResourceConfigURI),
                            valueFactory.createIRI(configGraphURI));

                    model.add(statement);
                    model.addAll(m);
                });
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        if (object.getClass().isAnnotationPresent(RdfToPojo.Type.class)) {
            String configURI = object.getClass().getAnnotation(RdfToPojo.Type.class).uri();
            model.add(valueFactory.createStatement(valueFactory.createIRI(resourceConfigURI), RDF.TYPE, valueFactory.createIRI(configURI), valueFactory.createIRI(configGraphURI)));
        }

        return model;
    }

}
