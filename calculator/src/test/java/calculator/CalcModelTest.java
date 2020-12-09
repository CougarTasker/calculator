package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalcModelTest {
  CalcModel modelInstance = null;
  public final int MAX = 100;
  @BeforeEach
  void before() {
    modelInstance = new CalcModel();
  }

  @Test
  void testEvaluate() throws CalculationException {
    assertEquals(2, modelInstance.evaluate("1 1 +", OpType.POSTFIX),
        0.001,"evaluate should be able to work with reverse polish");
    assertEquals(3, modelInstance.evaluate("1 + 2", OpType.INFIX),
        0.001,"evaluate should be able to work with infix notation");
  }
  @Test 
  void testMany() throws CalculationException {
    float f = 1;
    String infix = "1";
    String rpn = "1";
    for(int i =0; i < MAX; i++) {
      f+=1;
      infix += "+1";
      rpn += " 1+";
      assertEquals(f, modelInstance.evaluate(rpn, OpType.POSTFIX),
          0.001,"evaluate should be able to work many times with large expressions");
      assertEquals(f, modelInstance.evaluate(infix, OpType.INFIX),
          0.001,"evaluate should be able to work many times with large expressions");
    }
  }
}
