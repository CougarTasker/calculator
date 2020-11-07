package calculator;

/**
 * This is an exception for when a stack is empty and there has been an attempt to do something to
 * an element.
 * 
 * @author Cougar Tasker
 *
 */
public class EmptyStackException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * create an empty stack exception with the default message.
   */
  public EmptyStackException() {
    super("stack is empty can't remove element");
  }
}
