package calculator;

/**
 * Enum for the for representing the different types equation notations.
 * 
 * @author Cougar Tasker
 *
 */
public enum OpType {
  /**
   * This is the operation type for the usual (infix) notation. binary operators take there operands
   * from before and after.
   * 
   * {@code 1 + 1 = 2}
   */
  INFIX,
  /**
   * This is the operation type Reverse polish notation. operators take the most recent operands
   * before them {@code 1 1 + = 2}
   */
  POSTFIX, // also known as reverse polish notation
  /**
   * This is the operation type polish notation. operators take the most recent operands after them.
   * {@code + 1 1 = 2}
   */
  PREFIX
}
