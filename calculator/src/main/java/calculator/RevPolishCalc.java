package calculator;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A class to parse and evaluate Reverse polish expressions. This class implements the evaluate
 * interface just a single method that takes in a single string an calculates its value.
 * 
 * @author Cougar Tasker
 *
 */
public class RevPolishCalc {


  /**
   * When called on a valid Reverse polish expression this method will calculate is result.
   * 
   * @param what The expression to be evaluated.
   * @return the value calculated from the expression.
   * @throws CalculationException thrown if there is an error during the calculation.
   */
  public float evaluate(String what) throws CalculationException {
    // this method uses two stacks the input is parsed from the string and the output.
    // numbers are just moved across
    // when a operator is found its preceding numbers are taken from the output i.e in 1 1 + that
    // would have been 1 1
    // then the new value is calculated from the two operators i.e 2
    // finally the 2 is added to the output
    // when all input has been consumed and there hasn't been an issue the final value on the output
    // stack is returned.

    Stack input = parse(what);
    NumStack output = new NumStack();
    while (input.size() > 0) {
      Entry cur;
      try {
        cur = input.pop();
      } catch (EmptyStackException e1) {
        // this exception cannot be thrown since we check the size before using it.
        throw new CalculationException("unknown error");
      }
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
          output.push(calc(cur.getSymbol(), a, b));
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

  private float calc(Symbol s, float a, float b) throws CalculationException {
    switch (s) {
      case DIVIDE:
        if (b == 0) {
          throw new CalculationException("cannot divide by zero");
        }
        return a / b;
      case MINUS:
        return a - b;
      case PLUS:
        return a + b;
      case TIMES:
        return a * b;
      case RIGHT_BRACKET:
      case LEFT_BRACKET:
        throw new CalculationException("parentheses are not required. Are you in the right mode?");
      case INVALID:
        throw new CalculationException("invalid symbol in expression");
      default:
        throw new CalculationException(
            "unimplemented symbol '" + s.toString() + "' in expression.");
    }
  }

  /**
   * A simple class structure for the storing functions for recognising and parsing different
   * tokens.
   * 
   * @author Cougar Tasker
   *
   */
  static class Token {
    private Predicate<String> test;
    private Function<String, Entry> produce;

    /**
     * Create a Token with a given test entry.
     * 
     * @param test A predicate that returns true if the given string can be parsed by this token.
     * @param produce Produce a token from this given string.
     */
    private Token(Predicate<String> test, Function<String, Entry> produce) {
      this.test = test;
      this.produce = produce;
    }

    /**
     * A simplified interface for Symbol tokens since they all return the same type of entry. This
     * Token will only match strings that are equivalent to the match string.
     * 
     * @param match A string that matches this token
     * @param val The Symbol s this token represents.
     */
    private Token(String match, Symbol val) {
      // simplified interface for symbols
      this.test = s -> s.equals(match);
      this.produce = s -> new Entry(val);
    }

    /**
     * A simplified interface for Symbol tokens since they all return the same type of entry.
     * 
     * @param test A predicate that returns true if the given string can be parsed by this token.
     * @param val The Symbol s this token represents.
     */
    private Token(Predicate<String> test, Symbol val) {
      // simplified interface for symbols
      this.test = test;
      this.produce = s -> new Entry(val);
    }
  }

  /**
   * An array of all tokens that are recognised.
   */
  public static final Token[] tokens = new Token[] {
      new Token(s -> !s.chars().anyMatch(c -> c < '0' || c > '9'),
          s -> new Entry(Float.parseFloat(s))),
      new Token("+", Symbol.PLUS), new Token("-", Symbol.MINUS), new Token("*", Symbol.TIMES),
      new Token("/", Symbol.DIVIDE),
      new Token(s -> s.equals("(") || s.equals("["), Symbol.LEFT_BRACKET),
      new Token(s -> s.equals(")") || s.equals("]"), Symbol.RIGHT_BRACKET)};


  /**
   * When called on a valid input expression this method will separate it into individual
   * {@link Entry entries}. These entries are returned in reverse order on a stack. If there are
   * unrecognised tokens within the string a invalid expression exception will be thrown.
   * 
   * @param input the expression string to be parsed.
   * @return Stack of parsed tokens in reverse order. The {@link Entry entry} on the top of the
   *         stack will be the last token in the string.
   * @throws InvalidExpressionException if there are unrecognisable tokens within the expression.
   */
  public Stack parse(String input) throws InvalidExpressionException {
    // parse the input from the string and place them on a stack with the first elements on the top.
    Stack out = new Stack();
    String[] parts = input.trim().split("(\\s|,)+");
    boolean found;
    for (int i = parts.length - 1; i >= 0; i--) {
      found = false;
      for (Token t : tokens) {
        if (t.test.test(parts[i])) {
          out.push(t.produce.apply(parts[i]));
          found = true;
          break;
        }
      }
      if (!found) {
        throw new InvalidExpressionException(
            "unrecognised symbol '" + parts[i] + "' in expression.");
      }

    }
    return out;
  }
}


