package calculator;

/**
 * A facade class just allowing the storage of String objects to hide the details of the stack
 * class. this class will be used in the shunting yard algorithm for reversing the order of a
 * expression in place.
 * 
 * @see Stack
 * @author Cougar Tasker
 *
 */
public class StrStack {
  private Stack strStack = null;


  /**
   * Constructor for the string stack class. Creates a new empty stack of strings.
   */
  public StrStack() {
    strStack = new Stack();
  }

  /**
   * Add a string to be stored at the top of the stack.
   * 
   * @param str the string to be added.
   */
  public void push(String str) {
    strStack.push(Entry.getEntry(str));
  }

  /**
   * Return the string that is on the top of the stack. this will remove this string from the stack.
   * if the stack is empty this will throw an exception.
   * 
   * @return the string that was on the stack
   * @throws EmptyStackException Thrown when the stack is empty. cannot remove an element from an
   *         empty stack.
   */
  public String pop() throws EmptyStackException {
    try {
      return strStack.pop().getString();
    } catch (BadTypeException e) {
      // this block cannot be reached since this class does not support adding other types of data
      return null;
    }
  }

  /**
   * Check if the stack is empty.
   * 
   * @return true if there is no operators on the stack
   */
  public boolean isEmpty() {
    return strStack.size() == 0;
  }
}
