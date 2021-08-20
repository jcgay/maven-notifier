package fr.jcgay.maven.notifier

import org.apache.maven.eventspy.EventSpy


class FakeContext implements EventSpy.Context {

    private Map<String, Object> internal = [:]

    @Override
    Map<String, Object> getData() {
        return internal
    }
}

