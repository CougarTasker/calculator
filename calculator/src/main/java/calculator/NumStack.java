package calculator;


/**
 * A facade class just allowing the storage of float to hide the details of the stack class. this
 * class like Stack is FIFO.
 * 
 * @see Stack
 * @author Cougar Tasker
 *
 */
public class NumStack {
  private Stack numStack = null;

  /**
   * Constructor for initialising an empty stack of floating point numbers.
   */
  public NumStack() {
    this.numStack = new Stack();
  }

  /**
   * Add a floating point number onto the top of the stack.
   * 
   * @param i floating point number to be stored in the stack.
   */
  public void push(float i) {
    this.numStack.push(Entry.getEntry(i));
  }

  /**
   * Retrieve the floating point number that is on the top of the stack. This removes the entry from
   * the stack and returns it.
   * 
   * @return the floating point number from the top of the stack.
   * @throws EmptyStackException thrown when this method is called on a empty stack.
   */
  public float pop() throws EmptyStackException {
    try {
      return numStack.pop().getValue();
    } catch (BadTypeException e) {
      // there cannot bad type since the class only supports pushing floats
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * Checks if the stack is empty.
   * 
   * @return true if the stack has no contents. false if there is at least one number.
   */
  public boolean isEmpty() {
    return numStack.size() == 0;
  }

  /**
   * Returns the number of floats on the stack. 
   * @return the size of the stack as an integer.
   */
  public int size() {
    return numStack.size();
  }
}
