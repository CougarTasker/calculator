package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SymbolTest {

  @Test
  void testErrors() {
    assertThrows(CalculationException.class, () -> {
      Symbol.PLUS.calc(Float.POSITIVE_INFINITY, 1);
    }, "operators should throw an exception if there is an overflow");
    assertThrows(CalculationException.class, () -> {
      Symbol.MINUS.calc(Float.POSITIVE_INFINITY, 1);
    }, "operators should throw an exception if there is an overflow");
    assertThrows(CalculationException.class, () -> {
      Symbol.TIMES.calc(Float.POSITIVE_INFINITY, 1);
    }, "operators should throw an exception if there is an overflow");
    assertThrows(CalculationException.class, () -> {
      Symbol.DIVIDE.calc(Float.POSITIVE_INFINITY, 1);
    }, "operators should throw an exception if there is an overflow");
    assertThrows(CalculationException.class, () -> {
      Symbol.POWER.calc(Float.POSITIVE_INFINITY, 1);
    }, "operators should throw an exception if there is an overflow");
    assertThrows(CalculationException.class, () -> {
      Symbol.DIVIDE.calc(1, 0);
    }, "operators should throw an exception if the user tries to divide by zero");
  }

}
