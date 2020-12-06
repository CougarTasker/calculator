package calculator;

/**
 * A more specific type of calculationException for testing purposes.
 * 
 * @author Cougar Tasker
 *
 */
public class InvalidExpressionException extends CalculationException {
  private static final long serialVersionUID = 1944082334437244666L;

  /**
   * Create a new invalid expression exception with a message explaining what has gone wrong.
   * 
   * @param message A string explaining the error. this string should be well written and concise as
   *        it will be given to a user
   */
  public InvalidExpressionException(String message) {
    super(message);
  }

}
