package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalcControllerTest {
  CalcController controllerInstance = null;

  @BeforeEach
  void before() {
    controllerInstance = new CalcController();
  }
  @Test
  void testMethods() {
    controllerInstance.expressionType();
    controllerInstance.calculate();
  }
}
