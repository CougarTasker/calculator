package calculator;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * the enum of non-number tokens possible in an expression.
 * 
 * @author Cougar Tasker
 *
 */
public enum Symbol {
  // brackets and invalid aren't operators so have no function
  LEFT_BRACKET, RIGHT_BRACKET, INVALID,
  // lowest precedence
  PLUS(2, (a, b) -> a + b, true), MINUS(2, (a, b) -> a - b, true),
  // middle precedence
  TIMES(3, (a, b) -> a * b, true), DIVIDE(3, (a, b) -> b == 0 ? "Cannot divide by zero" : null,
      (a, b) -> a / b, true),
  // Highest precedence
  POWER(4, (a,
      b) -> Double.compare(Math.pow(a, b), Double.NaN) == 0 ? "Result must be a real number" : null,
      (a, b) -> (float) Math.pow(a, b), false);

  /**
   * When called returns weather a given symbol is an operator.
   * 
   * @return true if this symbol is an operator
   */
  public boolean isOperator() {
    return precedence > 0;
  }


  /**
   * when called on an operator it will use that operator to calculate a new value.
   * 
   * @param a the <b>left</b> parameter for this operator. this matters for some functions.
   * @param b the <b>right</b> parameter for this operator.
   * @return the result of apply this operator .
   * @throws CalculationException if there is a problem with performing this operation. i.e. divide
   *         by zero.
   */
  public float calc(float a, float b) throws CalculationException {
    if (this.test.apply(a, b) == null) {
      return this.function.apply(a, b);
    } else {
      throw new CalculationException(this.test.apply(a, b));
    }
  }

  /**
   * returns weather this operator is left associative. that means that if in sequence with an
   * operator of equal precedence should the operator on the left be calculated first.
   * 
   * @return true if the operator is left associative.
   */
  public boolean isLeftAssociative() {
    return this.leftAssociative;
  }

  /**
   * gets the precedence of this operator. a higher precedence operator should be executed before a
   * lower precedence one. non operators have a precedence of 0.
   * 
   * @return the precedence value associated with this operator.
   */
  public int getPrecedence() {
    return this.precedence;
  }

  private final int precedence;
  private final BinaryOperator<Float> function;
  private final BiFunction<Float, Float, String> test;
  private final boolean leftAssociative;

  private Symbol() {
    this.precedence = 0;
    this.function = null;
    this.leftAssociative = true;
    this.test = (a, b) -> null;
  }

  private Symbol(int precedence, BinaryOperator<Float> function, boolean leftAssociative) {
    this.precedence = precedence;
    this.function = function;
    this.leftAssociative = leftAssociative;
    this.test = (a, b) -> null;
  }

  private Symbol(int precedence, BiFunction<Float, Float, String> test,
      BinaryOperator<Float> function, boolean leftAssociative) {
    this.precedence = precedence;
    this.function = function;
    this.leftAssociative = leftAssociative;
    this.test = test;
  }

}
