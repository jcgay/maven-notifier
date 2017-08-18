package fr.jcgay.maven.notifier

import org.testng.annotations.Test

import static org.assertj.core.api.Assertions.assertThat

class VersionTest {

    @Test
    void 'should read version from existing resource'() {
        def version = new Version('/version-test')

        assertThat(version.get()).isEqualTo('1.0.0')
    }

    @Test
    void 'should return unknown version when resource does not exist'() {
        def version = new Version('/does-not-exist')

        assertThat(version.get()).isEqualTo('unknown-version')
    }

    @Test
    void 'should return unknown version when resource is empty'() {
        def version = new Version('/version-test-empty')

        assertThat(version.get()).isEqualTo('unknown-version')
    }
}
