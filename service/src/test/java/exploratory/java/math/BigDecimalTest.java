package exploratory.java.math;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class BigDecimalTest {

    @Test
    public void oneShouldEqualToOneTrailingZeroes() {
        Assertions.assertThat(BigDecimal.ONE).isEqualByComparingTo(BigDecimal.valueOf(1.00d));
    }
}
