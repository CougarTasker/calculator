package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.fail;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RevPolishCalcTest {
  RevPolishCalc calcInstance = null;
  Stack expressionAstackIntance = null;
  Stack expressionBstackIntance = null;
  String expressionA;
  String expressionB;
  float expressionAanswer;
  float expressionBanswer;
  public static final int MAX = 10000;

  @BeforeEach
  void before() {
    calcInstance = new RevPolishCalc();
    expressionA = " 1 2 +";
    expressionAanswer = 3;
    expressionAstackIntance = new Stack();
    expressionAstackIntance.push(new Entry(Symbol.PLUS));
    expressionAstackIntance.push(new Entry(2));
    expressionAstackIntance.push(new Entry(1));
    expressionB = " 1 2 + 3 * 5 4 - +";
    expressionBstackIntance = new Stack();
    expressionBstackIntance.push(new Entry(Symbol.PLUS));
    expressionBstackIntance.push(new Entry(Symbol.MINUS));
    expressionBstackIntance.push(new Entry(4));
    expressionBstackIntance.push(new Entry(5));
    expressionBstackIntance.push(new Entry(Symbol.TIMES));
    expressionBstackIntance.push(new Entry(3));
    expressionBstackIntance.push(new Entry(Symbol.PLUS));
    expressionBstackIntance.push(new Entry(2));
    expressionBstackIntance.push(new Entry(1));
    expressionBanswer = 10;

  }

  @Test
  void testCalculationException() {
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("not a expression");
    }, "a calculation exception should be thrown if you try to evaluate somthing that insn't an expression");
  }

  

  @Test
  void testEvaluate() {
    try {
      assertEquals(expressionAanswer, calcInstance.evaluate(expressionA), 0.001,
          "evaluate should return the correct value for the given expression");
      assertEquals(expressionBanswer, calcInstance.evaluate(expressionB), 0.001,
          "evaluate should return the correct value for complex expressions too");
    } catch (CalculationException e) {
      fail("valid expressions shouldn't throw an exception" + e.toString());
    }
    // yay !!!!!!
  }

  void testManyEvaluate() {
    String base = "1 1 + ";
    String ten = "1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + ";
    String exp = base + ten + ten + ten + ten;
    try {
      assertEquals(calcInstance.evaluate(exp), 42, 0.001,
          "The calcualtor should even be able to calcualte extremely long expressions");

    } catch (CalculationException e) {
      fail("valid expressions shouldn't throw an exception" + e.toString());
    }
  }

  @Test
  void testPerformance() {
    int wpm = 40; // Average number of words per minute
    int milli = (60 * 1000) / (wpm * 10) / 10;
    // the calculation should take a fraction of the time it takes to press a key so the user gets
    // immediate results. this is the number of milliseconds evaluate has to execute in
    assertTimeout(Duration.ofMillis(MAX * milli), () -> {
      for (int i = 0; i <= MAX; i++) {
        calcInstance.evaluate(expressionB);
      }
    }, "The calculator shouldnt leave the user waiting");
  }
  @Test
  void testAdvancedCalculationExceptions() throws CalculationException {
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("1 1 1 1");
    },"an exception should be thrown if the program can't do any calculation");
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("1 + / *");
    },"an exception should be thrown if there arent enough operands");
    assertThrows(CalculationException.class, () -> {
      calcInstance.evaluate("1 + / * ) (");
    },"an exception should be thrown if there are symbols that dont make sense");
    assertThrows(CalculationException.class,() -> {
      calcInstance.evaluate("1 0 /");
    },"you cannot divide by zero");
  }
  @Test 
  void testNewEvaluate() {
    try {
      assertEquals(expressionAanswer, calcInstance.evaluate(Tokenizer.parse(expressionA)), 0.001,
          "evaluate should return the correct value for the given expression");
      assertEquals(expressionBanswer, calcInstance.evaluate(Tokenizer.parse(expressionB)), 0.001,
          "evaluate should return the correct value for complex expressions too");
    } catch (CalculationException e) {
      fail("valid expressions shouldn't throw an exception" + e.toString());
    }
  }
}
