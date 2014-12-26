package fr.jcgay.maven.notifier;

import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.BuildSuccess;
import org.apache.maven.execution.BuildSummary;

import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;

public enum Status {

    SUCCESS("/dialog-clean.png", "Success"),
    FAILURE("/dialog-error-5.png", "Failure"),
    SKIPPED(null, "Skipped");

    private final String icon;
    private final String message;

    private Status(String icon, String message) {
        this.icon = icon;
        this.message = checkNotNull(message);
    }

    public String message() {
        return message;
    }

    public static Status of(BuildSummary summary) {
        if (summary == null) {
            return SKIPPED;
        } else if (summary instanceof BuildSuccess) {
            return SUCCESS;
        } else if (summary instanceof BuildFailure) {
            return FAILURE;
        }
        throw new IllegalArgumentException(String.format("Summary status type [%s] is not handle.", summary.getClass().getName()));
    }

    public URL url() {
        return getClass().getResource(icon);
    }
}
