package calculator;

/**
 * This error is to be thrown by the model if there is an error in the calculation. to give feedback
 * to the use this error should be reported back to the user.
 * 
 * @author Cougar Tasker
 *
 */
public class CalculationException extends Exception {
  private static final long serialVersionUID = -8510083781355747076L;

  /**
   * Create a new Calculation exception with a message explaining what has gone wrong.
   * 
   * @param message A string explaining the error. this string should be well written and concise as
   *        it will be given to a user
   */
  public CalculationException(String message) {
    super(message);
  }

}
