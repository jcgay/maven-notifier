package fr.jcgay.maven.notifier;

import com.google.common.base.Objects;

import java.util.Properties;

public class Configuration {

    private String implementation;
    private boolean shortDescription;
    private int threshold;
    private Properties notifierProperties;

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getImplementation() {
        return implementation;
    }

    public boolean isShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(boolean shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setNotifierProperties(Properties notifierProperties) {
        this.notifierProperties = notifierProperties;
    }

    public Properties getNotifierProperties() {
        return notifierProperties;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("implementation", implementation)
            .add("shortDescription", shortDescription)
            .add("threshold", threshold)
            .add("notifier-properties", notifierProperties)
            .toString();
    }
}
