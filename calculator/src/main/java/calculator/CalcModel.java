package calculator;

/**
 * A class for interacting with the whole model. allowing access to the values of each of the
 * calculators memories.
 * 
 * @author Cougar Tasker
 *
 */
public class CalcModel {
  private Tokenizer revPolish = null;
  private Tokenizer standard = null;

  CalcModel() {
    revPolish = new RevPolishCalc();
    standard = new StandardCalc();
  }

  /**
   * Evaluates an expression assuming it has a given syntax.
   * 
   * @param expr the expression to be evaluated.
   * @param type the syntax type the expression is using.
   * @return the value calculated from the expression.
   * @throws CalculationException if there has been an error during the calculation
   */
  public float evaluate(String expr, OpType type) throws CalculationException {
    switch (type) {
      case INFIX:
        return standard.evaluate(expr);
      case POSTFIX:
        return revPolish.evaluate(expr);
      default:
        return 0;

    }
  }

}
