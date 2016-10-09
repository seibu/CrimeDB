package crime.entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Attribute {
    private boolean required;
    private String regex;
    private StringProperty value = new SimpleStringProperty();

    public Attribute(String value, boolean required, String regex) {
        this.value.setValue(value);
        this.required = required;
        this.regex = regex;
    }

    public String getValue() {
        return value.getValue();
    }
    public boolean setValue(String value) {
        if (checkRegex(value)) {
            this.value.setValue(value);
            return true;
        }

        return false;
    }

    public StringProperty getProperty() {
        return value;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean checkRegex(String value) {
        try {
            return value.matches(regex);
        } catch (NullPointerException e) {
            //e.printStackTrace();
            if (required) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    public boolean checkRequiredRegex(String value) {
        if (isRequired()) {
            return checkRegex(value);
        }
        if (!"".equals(value)) {
            return checkRegex(value);
        }
        return true;
    }
}
