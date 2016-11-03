package de.uni.bonn.iai.eis.etl.linkedpipes;

import com.linkedpipes.etl.component.api.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to help find the ports of a linkedpipes component. Ports are represented by fields in the component class.
 * <p>
 * InputPorts are annotated with Component.InputPort.class
 * OutputPorts are annotated with Component.OutputPort.class
 */
public final class PortHelper {
    private PortHelper() {

    }

    /**
     * Gets the input port of a given linkedpipes component class. For named ports.
     *
     * @param clazz    the linkedpipes component
     * @param portName the name of the field representing the desired port (if multiple ports are present)
     * @return the name of the input port
     */
    public static String getInputPort(Class<? extends Component.Sequential> clazz, String portName) {
        List<Field> allFields = getFields(clazz, Component.InputPort.class);
        List<Field> fieldsFilteredByName = allFields.stream()
                .filter(field -> field.getAnnotation(Component.InputPort.class).id().equals(portName))
                .collect(Collectors.toList());

        Assert.isTrue(fieldsFilteredByName.size() == 1);

        return fieldsFilteredByName.get(0).getAnnotation(Component.InputPort.class).id();
    }

    /**
     * Gets the input port of a given linkedpipes component class.
     *
     * @param clazz the linkedpipes component
     * @return the name of the input port
     */
    public static String getInputPort(Class<? extends Component.Sequential> clazz) {
        List<Field> fields = getFields(clazz, Component.InputPort.class);
        Assert.isTrue(fields.size() == 0 || fields.size() == 1,
                String.format("Component: %s has multiple InputPorts specified.", clazz.getSimpleName()));

        String result = null;

        if (fields.size() == 1) {
            result = fields.get(0).getAnnotation(Component.InputPort.class).id();
        }

        return result;
    }

    /**
     * Gets the output port of a given linkedpipes component class.
     *
     * @param clazz The linkedpipes component class.
     * @return the output ports name.
     */
    public static String getOutputPort(Class<? extends Component.Sequential> clazz) {
        List<Field> fields = getFields(clazz, Component.OutputPort.class);
        Assert.isTrue(fields.size() == 0 || fields.size() == 1,
                String.format("Component: %s has multiple OutputPorts specified.", clazz.getSimpleName()));

        String result = null;

        if (fields.size() == 1) {
            result = fields.get(0).getAnnotation(Component.OutputPort.class).id();
        }

        return result;
    }

    /**
     * Function to find all fields in a class that are annotated with a given annotation
     * @param clazz the class to scan
     * @param annotationClass the annotation to scan for
     * @return the list of fields annotated
     */
    private static List<Field> getFields(Class<? extends Component.Sequential> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }
}
