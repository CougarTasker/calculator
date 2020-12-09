package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.fail;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StandardCalcTest {
  StandardCalc calcInstance = null;
  public static final int MAX = 10000;
  @BeforeEach
  void before() {
    calcInstance = new StandardCalc();
  }

  @Test
  void testCalculationExceptions() {
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("not a expression");
    }, "a calculation exception should be thrown if you try to evaluate somthing that insn't an expression");
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("1 1 1 1");
    }, "an exception should be thrown if the program can't do any calculation");
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("1 + ");
    }, "an exception should be thrown if there arent enough operands");
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("1 / 0");
    }, "you cannot divide by zero");
  }

  @Test
  void testSimpleCalculationExmaples() {
    // calculation examples without parentheses
    try {
      assertEquals(2, calcInstance.evaluate("1 + 1"), 0.001,
          "evaluate should calcualte the correct value of simple expressions");
      assertEquals(3, calcInstance.evaluate("1 + 1 * 2"), 0.001,
          "evaluate should respect the order of operations");
    } catch (CalculationException e) {
      fail("valid expressions shouldn't throw an exception" + e.toString());
    }
  }

  @Test
  void testBrackets() {
    // calculation examples with parentheses
    try {
      assertEquals(10, calcInstance.evaluate("(1 + 1) * 5"), 0.001,
          "evaluate should understand parathesis and not follow the order of operations");
      assertEquals(3, calcInstance.evaluate("1 + (1 * 2)"), 0.001,
          "evaluate should not be changed by the use of parthesis where they dont change the order of operations");
      assertEquals(24, calcInstance.evaluate("(10 + 2) + ((1 * 2) + 1) * 4"), 0.001,
          "evaluate should be able to deal with nested and sequenced brackets");
    } catch (CalculationException e) {
      fail("valid expressions shouldn't throw an exception" + e.toString());
    }
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("1 + 2 )");
    }, "the calcualtor should detect an expression with unballanced brackets");
  }

  @Test
  void testPow() throws CalculationException {
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("-1^0.5");
    }, "producing imaginary numbers should throw an error");
    assertEquals(4, calcInstance.evaluate("2 ^ 2"), 0.001,
        "stanadard calc should be able to deal with the power operator");
    assertEquals(27, calcInstance.evaluate("3^3"), 0.001,
        "stanadard calc should be able to deal with the power operato");
    
  }

  @Test
  void testAssociative() throws CalculationException {
    // operations should have the correct associativity
    assertEquals(0.5, calcInstance.evaluate("1 / 1 / 2"), 0.001,
        "operations should have the correct associativity");
    assertEquals(65536, calcInstance.evaluate("2 ^ 4 ^ 2"), 0.001,
        "stnadard calc should be able to deal with right assoatvie operators too");
  }
  @Test
  void testPerformance() {
    int wpm = 40; // Average number of words per minute
    int milli = (60 * 1000) / (wpm * 10) / 10;
    // the calculation should take a fraction of the time it takes to press a key so the user gets
    // immediate results. this is the number of milliseconds evaluate has to execute in
    assertTimeout(Duration.ofMillis(MAX * milli), () -> {
      for (int i = 0; i <= MAX; i++) {
        calcInstance.evaluate("1^2* 3/(1.3-(1 + 2))");
      }
    }, "The calculator shouldnt leave the user waiting");
  }
}
