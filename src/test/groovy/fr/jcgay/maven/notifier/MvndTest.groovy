package fr.jcgay.maven.notifier


import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assumptions.assumeThat

class MvndTest {

    private static final String MVND_PROP = "mvnd.something"

    @BeforeMethod
    void setUp() {
        System.clearProperty(MVND_PROP)
    }

    @Test
    void 'return true when mvnd property is present'() {
        System.setProperty(MVND_PROP, "ok")

        assertThat(Mvnd.isRunningWithMvnd()).isTrue()
    }

    @Test
    void 'return false when no mvnd property is present'() {
        assumeThat(System.getProperties()).isNotEmpty()

        assertThat(Mvnd.isRunningWithMvnd()).isFalse()
    }
}
