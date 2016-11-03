package de.uni.bonn.iai.eis.web.model.mapping;

import javax.persistence.Embeddable;

@Embeddable
public class Mapping {
    private String name;
    private String type = "Auto";
    private String lang = "de";
    private String valuePrefix;

    // TODO: 04.10.16 component property uri
    private String uri;

    // TODO: 04.10.16 parent component property uri
    private String componentProperty;

    private boolean isAmount = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getUri() {
        if (componentProperty != null && !componentProperty.isEmpty()) {
            return componentProperty;
        }

        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getValuePrefix() {
        return valuePrefix;
    }

    public void setValuePrefix(String valuePrefix) {
        this.valuePrefix = valuePrefix;
    }

    public String getComponentProperty() {
        return componentProperty;
    }

    public void setComponentProperty(String componentProperty) {
        this.componentProperty = componentProperty;
    }

    public boolean getIsAmount() {
        return isAmount;
    }

    public void setIsAmount(boolean amount) {
        isAmount = amount;
    }
}
