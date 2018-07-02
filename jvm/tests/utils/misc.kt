package shared

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MiscTest {
    @Test
    fun roundTest() {
        assertThat((1.0).roundBy(10.0)).isEqualTo(1.0)
        assertThat((1.4).roundBy(10.0)).isEqualTo(1.4)
        assertThat((1.44).roundBy(10.0)).isEqualTo(1.4)
        assertThat((1.46).roundBy(10.0)).isEqualTo(1.5)

        assertThat((-1.0).roundBy(10.0)).isEqualTo(-1.0)
        assertThat((-1.4).roundBy(10.0)).isEqualTo(-1.4)
        assertThat((-1.44).roundBy(10.0)).isEqualTo(-1.4)
        assertThat((-1.46).roundBy(10.0)).isEqualTo(-1.5)

        assertThat((0.0).roundBy(10.0)).isEqualTo(0.0)
    }
}

