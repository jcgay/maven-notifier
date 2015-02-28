package fr.jcgay.maven.notifier;

import com.google.common.base.Objects;

public class Configuration {

    private String implementation;
    private boolean shortDescription;
    private int threshold;

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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("implementation", implementation)
                .add("shortDescription", shortDescription)
                .add("threshold", threshold)
                .toString();
    }
}
