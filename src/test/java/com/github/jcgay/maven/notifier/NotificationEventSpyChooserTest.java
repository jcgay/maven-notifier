package com.github.jcgay.maven.notifier;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verifyZeroInteractions;

public class NotificationEventSpyChooserTest {

    @InjectMocks
    private NotificationEventSpyChooser chooser = new NotificationEventSpyChooser();

    @Mock
    private Notifier notifier;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_not_notify_if_event_is_not_a_build_result() throws Exception {

        chooser.onEvent("this is not a build result");

        verifyZeroInteractions(notifier);
    }
}
