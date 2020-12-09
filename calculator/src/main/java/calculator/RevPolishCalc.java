package calculator;

import java.util.LinkedList;

/**
 * A class to parse and evaluate reverse polish (postfix) expressions. This class is a Tokenizer, it
 * contains a method evaluate that will do all the parsing and evaluation and return the result.
 * 
 * @author Cougar Tasker
 *
 */
public class RevPolishCalc extends Tokenizer {
  /**
   * When called on a valid Reverse polish expression this method will calculate is result.
   * 
   * @param input The expression to be evaluated. This is a linked list of entries.
   * @return the value calculated from the expression.
   * @throws CalculationException thrown if there is an error during the calculation.
   */
  public float evaluate(LinkedList<Entry> input) throws CalculationException {
    // this method uses two stacks the input is parsed from the string and the output.
    // numbers are just moved across
    // when a operator is found its preceding numbers are taken from the output i.e in 1 1 + that
    // would have been 1 1
    // then the new value is calculated from the two operators i.e 2
    // finally the 2 is added to the output
    // when all input has been consumed and there hasn't been an issue the final value on the output
    // stack is returned.


    NumStack output = new NumStack();
    while (input.size() > 0) {
      Entry cur;
      cur = input.pop();
      if (cur.getType() == Type.NUMBER) {
        try {
          output.push(cur.getValue());
        } catch (BadTypeException e) {
          // this exception cannot be thrown since we check the type before using it.
          throw new CalculationException("unknown error");
        }
      } else if (cur.getType() == Type.SYMBOL) {
        try {
          float b = output.pop();
          float a = output.pop();
          output.push(cur.getSymbol().calc(a, b));
        } catch (EmptyStackException e) {
          throw new CalculationException("unballanced expression. not enough operands.");
        } catch (BadTypeException e) {
          // this exception cannot be thrown since we check the type before using it.
          throw new CalculationException("unknown error");
        }
      }
    }
    if (output.isEmpty()) {
      // every time we successfully remove from the output we add back to it as well so this
      // shouldn't happen
      throw new CalculationException("unknown error");
    } else if (output.size() > 1) {
      // the calculation should have condensed down to one final result.
      throw new CalculationException("unballanced expression. not enough operators.");
    } else {
      try {
        return output.pop();
      } catch (EmptyStackException e) {
        // we just checked the stack isn't empty.
        throw new CalculationException("unknown error");
      }
    }
  }



}


