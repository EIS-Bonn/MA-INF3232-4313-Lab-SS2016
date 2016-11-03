package de.uni.bonn.iai.eis.etl.component;


import de.uni.bonn.iai.eis.etl.linkedpipes.GeneralConfiguration;
import de.uni.bonn.iai.eis.etl.linkedpipes.PortHelper;
import org.openrdf.model.IRI;
import org.openrdf.model.impl.SimpleValueFactory;

import java.util.UUID;

/**
 * Representation (base) of a component of a LinkedPipes Pipeline. Components act as wrappers for linkedpipes components.
 */
public abstract class SimpleComponent extends Component {
    /**
     * The id of the component
     */
    private String id;

    /**
     * The general configuration of the component
     */
    private GeneralConfiguration generalConfiguration = new GeneralConfiguration();

    /**
     * Constructor for a component that creates a uuid as id for the component
     */
    public SimpleComponent() {
        id = UUID.randomUUID().toString();
    }

    /**
     * Setter for the preferred label of the component
     *
     * @param prefLabel the label to set
     */
    public void setPrefLabel(String prefLabel) {
        generalConfiguration.prefLabel = prefLabel;
    }

    /**
     * Setter for the description of the component
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        generalConfiguration.description = description;
    }

    /**
     * Setter for the x - axis  value of the component
     *
     * @param x
     */
    public void setX(int x) {
        generalConfiguration.x = x;
    }

    /**
     * Setter for the y - axis  value of the component
     *
     * @param y
     */
    public void setY(int y) {
        generalConfiguration.y = y;
    }

    /**
     * Setter for the linkedpipes component template uri
     *
     * @param templateURI the template uri
     */
    public void setTemplateURI(String templateURI) {
        generalConfiguration.template = SimpleValueFactory.getInstance().createIRI(templateURI);
    }

    /**
     * Getter for the pipeline uri
     *
     * @return
     */
    IRI getPipelineIRI() {
        return generalConfiguration.pipeline;
    }

    @Override
    public void setParentPipelineURI(String pipelineURI) {
        super.setParentPipelineURI(pipelineURI);

        generalConfiguration.pipeline = SimpleValueFactory.getInstance().createIRI(pipelineURI);

        String componentURL = pipelineURI + "/components/" + getId();
        setUri(componentURL);

        generalConfiguration.id = SimpleValueFactory.getInstance().createIRI(componentURL);

        addStatements(generalConfiguration.getModel());
    }

    /**
     * Getter for the components id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter fot the components id
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * The linkedpipes equivalent base class.
     *
     * @return the base class
     */
    public abstract Class<? extends com.linkedpipes.etl.component.api.Component.Sequential> getBase();

    /**
     * Getter for the name of the template
     *
     * @return the name of the template
     */
    public abstract String getTemplateName();

    /**
     * Getter for the input port of the component. Incoming connections will connect to these port.
     *
     * @return the port
     */
    String getInputPort() {
        return PortHelper.getInputPort(getBase());
    }

    /**
     * Getter for the output port of the component. Ougoing connections will start at this port.
     *
     * @return the port
     */
    String getOutputPort() {
        return PortHelper.getOutputPort(getBase());
    }
}
