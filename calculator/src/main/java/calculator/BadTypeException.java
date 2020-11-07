package calculator;

/**
 * This is an exception that is thrown when an action is done on an incorrect type of data.
 * 
 * @author Cougar Tasker
 *
 */
public class BadTypeException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * Create a Bad type Exception with a custom error message.
   * 
   * @param errorMessage a string to be displayed to someone debugging when this error is
   *        encountered
   */
  public BadTypeException(String errorMessage) {
    super(errorMessage);
  }

  /**
   * Create a new bad type Exception with with a message generated from the two types that didn't
   * match.
   * 
   * @param tried The type that caused the error.
   * @param actual The type its supposed to be.
   */
  public BadTypeException(Type tried, Type actual) {
    super("Trying to get data of the incorrect type " + tried.toString() + " when data is of type "
        + actual.toString());
  }
}
