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
    return entries.get(entries.size() - 1); // get the element that is at the end of ArrayList
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
    // get the element at the top of the stack and remove it
    // remove also returns the element that has been removed
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
    // use a string builder for efficiency
    out.append("[ ");
    for (Entry i : entries) {
      // add each element of the stack to the string
      out.append(i);
      out.append(", ");
    }
    out.append("]");
    return out.toString();
    // return the string builder as a string
  }
  
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((entries == null) ? 0 : entries.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Stack other = (Stack) obj;
    if (entries == null) {
      if (other.entries != null) {
        return false;
      }
    } else if (!entries.equals(other.entries)) {
      return false;
    }
    return true;
  }

}
