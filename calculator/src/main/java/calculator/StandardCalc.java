package calculator;

import java.util.LinkedList;

/**
 * A class to parse and evaluate regular infix expressions. This class is a Tokenizer, it contains a
 * method evaluate that will do all the parsing and evaluation and return the result.
 * 
 * @author Cougar Tasker
 *
 */
public class StandardCalc extends Tokenizer {
  RevPolishCalc reversePolishCalc = new RevPolishCalc();

  /**
   * When called on a valid algebraic, infix, expression this method will calculate its result.
   * 
   * @param input The expression to be evaluated as a set of entries
   * @return the value calculated from the expression.
   * @throws CalculationException thrown if there is an error during the calculation.
   */
  public float evaluate(LinkedList<Entry> input) throws CalculationException {
    return reversePolishCalc.evaluate(shuntingYard(input));

  }

  private LinkedList<Entry> shuntingYard(LinkedList<Entry> input) throws CalculationException {
    LinkedList<Entry> out = new LinkedList<Entry>();
    OpStack ops = new OpStack();
    for (Entry e : input) {
      switch (e.getType()) {
        case NUMBER:
          out.add(e);
          break;
        case SYMBOL:
          try {
            Symbol s = e.getSymbol();
            if (s.isOperator()) {
              // operator
              try {
                while (shouldBeRemoved(ops, s)) {
                  out.add(Entry.getEntry(ops.pop()));
                }
                ops.push(s);
              } catch (EmptyStackException e1) {
                // we check the stack is not empty before we pop from it. this cannot happen.
                throw new CalculationException("unknown error");
              }
            } else {
              // bracket
              if (s == Symbol.LEFT_BRACKET) {
                ops.push(s);
              } else {
                try {
                  while (ops.top() != Symbol.LEFT_BRACKET) {
                    out.add(Entry.getEntry(ops.pop()));
                  }
                  if (ops.top() == Symbol.LEFT_BRACKET) {
                    ops.pop();
                  }
                } catch (EmptyStackException e1) {
                  throw new CalculationException("unbalanced brackets");
                }


              }
            }
          } catch (BadTypeException e1) {
            // this is cannot happen we know the type of the entry to be Symbol otherwise we
            // wouldn't have called it
            throw new CalculationException("unknown error");
          }
          break;

        // unrecognised inputs
        case STRING:
          try {
            throw new CalculationException("unparsed symbol in expression: " + e.getString());
          } catch (BadTypeException e1) {
            // this is cannot happen we know the type of the entry to be a String otherwise we
            // wouldn't have called it
            throw new CalculationException("unknown error");
          }
        case INVALID:
          throw new CalculationException("invalid symbol in expression");
        default:
          throw new CalculationException(
              "unimplemented symbol '" + e.toString() + "' in expression.");
      }
    }
    while (!ops.isEmpty()) {
      try {
        if (ops.top() == Symbol.LEFT_BRACKET) {
          throw new CalculationException("unbalanced brackets");
        }
        out.add(Entry.getEntry(ops.pop()));
      } catch (EmptyStackException e1) {
        throw new CalculationException("unknown error");
        // we just checked the size of the ops it can't be empty
      }
    }
    if (out.size() == 0) {
      //since we remove brackets we must check were not giving an empty expression.
      throw new CalculationException("expression only contains brackets");
    }
    return out;
  }

  private boolean shouldBeRemoved(OpStack ops, Symbol current) throws CalculationException {
    try {
      if (!ops.isEmpty()) {
        int topPrecedence = ops.top().getPrecedence();
        if (topPrecedence == 0) {
          return false;
        }
        int currentPrecedence = current.getPrecedence();

        return topPrecedence > currentPrecedence
            || (topPrecedence == currentPrecedence && current.isLeftAssociative());

      } else {
        return false;
      }

    } catch (EmptyStackException e) {
      throw new CalculationException("unknown error");
    }
  }
}
