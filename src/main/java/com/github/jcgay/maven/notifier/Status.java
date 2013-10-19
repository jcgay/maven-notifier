package com.github.jcgay.maven.notifier;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.BuildSuccess;
import org.apache.maven.execution.BuildSummary;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

public enum Status {

    SUCCESS("/dialog-clean.png", "Success"),
    FAILURE("/dialog-error-5.png", "Failure"),
    SKIPPED(null, "Skipped");

    private String icon;
    private String message;

    private Status(String icon, String message) {
        this.icon = icon;
        this.message = checkNotNull(message);
    }

    public RenderedImage icon() {
        InputStream is = getClass().getResourceAsStream(icon);
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading status icon.", e);
        } finally {
            Closeables.closeQuietly(is);
        }
    }

    public byte[] toByteArray() {
        InputStream is = getClass().getResourceAsStream(icon);
        try {
            return ByteStreams.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading status icon.", e);
        } finally {
            Closeables.closeQuietly(is);
        }
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
}
