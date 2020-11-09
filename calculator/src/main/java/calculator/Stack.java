package calculator;

import java.util.ArrayList;

/**
 * This stack class represents a stack of entries. This stack allows for the storage of entries and
 * their retrieval in FIFO order in constant time.
 * 
 * @author Cougar Tasker
 *
 */
public class Stack {
  ArrayList<Entry> entries;

  /**
   * Initialise an empty stack.
   */
  public Stack() {
    entries = new ArrayList<Entry>();
  }

  /**
   * Gets the number of elements contained in this stack.
   * 
   * @return number of elements.
   */
  public int size() {
    return entries.size();
  }

  /**
   * Add entry to the top of the stack.
   * 
   * @param i the entry to be pushed to the stack.
   */
  public void push(Entry i) {
    entries.add(i);
  }

  /**
   * Peek at the entry on the top of the stack without removing it.
   * 
   * @return The entry that is currently on the top of the stack
   */
  public Entry top() throws EmptyStackException {
    if (entries.size() < 1) {
      // there isn't an element to get
      throw new EmptyStackException();
    }
    return entries.get(entries.size() - 1);
  }

  /**
   * Retrieve the entry on the top of the stack. This removes the entry from the stack and returns
   * it.
   * 
   * @return the entry on the top of the stack.
   * @throws EmptyStackException thrown when this method is called on a empty stack.
   */
  public Entry pop() throws EmptyStackException {
    if (entries.size() < 1) {
      // there isn't an element to remove
      throw new EmptyStackException();
    }
    return entries.remove(entries.size() - 1);
  }

  /**
   * Generates String representation from each of the values stored in this stack. <b>Warning</b>
   * this method will include every element in the output this means that the string might be larger
   * than expected and may be slow.
   * 
   * @return the string representation of the entries separated by comma.
   */
  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    out.append("[ ");
    for (Entry i : entries) {
      out.append(i);
      out.append(", ");
    }
    out.append("]");
    return out.toString();
  }
}
