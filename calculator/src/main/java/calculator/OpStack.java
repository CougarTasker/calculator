package calculator;

/**
 * A facade class just allowing the storage of operator symbols to hide the details of the stack
 * class. this class will be used in the shunting yard algorithm as it needs a stack just for the
 * symbols.
 * 
 * @see Stack
 * @author Cougar Tasker
 *
 */
public class OpStack {
  private Stack opStack;

  /**
   * Constructor for creating a new stack of operator symbols.
   */
  public OpStack() {
    this.opStack = new Stack();
  }

  /**
   * Add an operator to be stored at the top of the stack.
   * 
   * @param i the operator symbol to be added.
   */
  public void push(Symbol i) {
    opStack.push(new Entry(i));
  }

  /**
   * Return the operator that is on the top of the stack. this will remove this operator from the
   * stack. if the stack is empty this will throw an exception.
   * 
   * @return the operator symbol that was on the stack
   * @throws EmptyStackException Thrown when the stack is empty. cannot remove an element from an
   *         empty stack.
   */
  public Symbol pop() throws EmptyStackException {
    try {
      return opStack.pop().getSymbol();
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
    return opStack.size() == 0;
  }

  /**
   * get the element on the top of the stack without removing it.
   * 
   * @return the element on the top of the stack
   */
  public Symbol top() throws EmptyStackException {
    try {
      return opStack.top().getSymbol();
    } catch (BadTypeException e) {
      // this block cannot be reached since this class does not support adding other types of data
      return null;
    }
  }
}
