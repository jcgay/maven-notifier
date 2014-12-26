package fr.jcgay.maven.notifier;

import com.google.common.base.Objects;

public class Configuration {

    private String implementation;
    private boolean shortDescription;

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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("implementation", implementation)
                .add("shortDescription", shortDescription)
                .toString();
    }
}
