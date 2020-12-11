package calculator;

/**
 * the enum of non-number tokens possible in an expression.
 * 
 * @author Cougar Tasker
 *
 */
public enum Symbol {
  // brackets and invalid aren't operators so have no function
  LEFT_BRACKET, RIGHT_BRACKET, INVALID, ASSIGNMENT,
  // lowest precedence
  PLUS(2, true) {
    @Override
    float operation(float a, float b) throws CalculationException {
      return a + b;
    }
  },
  MINUS(2, true) {
    @Override
    float operation(float a, float b) throws CalculationException {
      return a - b;
    }
  },
  // middle precedence
  TIMES(3, true) {
    @Override
    float operation(float a, float b) throws CalculationException {
      return a * b;
    }
  },
  DIVIDE(3, true) {
    @Override
    float operation(float a, float b) throws CalculationException {
      if (b == 0) {
        throw new CalculationException("cannot divide by zero");
      }
      return a / b;
    }
  },
  // Highest precedence
  POWER(4, false) {
    @Override
    float operation(float a, float b) throws CalculationException {
      float f = (float) Math.pow(a, b);
      if (Float.isNaN(f)) {
        throw new CalculationException("result must be a real number");
      }
      return f;
    }
  };

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
    float out = operation(a, b);
    if (Float.isFinite(out) && !Float.isNaN(out)) {
      return out;
    } else {
      throw new CalculationException("arthmetic overflow");
    }
  }

  float operation(float a, float b) throws CalculationException {
    throw new CalculationException(toString() + " operator calc operation not impemented");
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
  private final boolean leftAssociative;

  private Symbol() {
    this.precedence = 0;
    this.leftAssociative = true;
  }

  private Symbol(int precedence, boolean leftAssociative) {
    this.precedence = precedence;
    this.leftAssociative = leftAssociative;
  }



}
