package calculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.util.Pair;

/**
 * this class is designed to be extended by classes that intend to parse and evaluate and expression
 * string. This is a class will separate a string into individual tokens before giving them to the
 * individual implementation of evaluate.This class will separate an expression string into
 * {@link Entry entries} of symbols and numbers where possible. null entries will be Ignored.
 * 
 * @author Cougar Tasker
 *
 */
public abstract class Tokenizer {
  // standardized states for the fsm to use
  private static final int[] SUCCESS = {-2, -3, -4, -5, -6};
  private static final int START = 0;
  private static final int FAIL = -1;

  // simple container class for transitions to simplify describing fsm's
  private static class Transition {
    public int from;
    public Predicate<Character> acc;
    public int to;

    Transition(int from, Predicate<Character> acc, int to) {
      this.from = from;
      this.acc = acc;
      this.to = to;
    }

    Transition(int from, Character acc, int to) {
      this.from = from;
      this.acc = c -> c == acc;
      this.to = to;
    }
  }

  /**
   * A simple class structure for the storing functions for recognising and parsing different
   * tokens.
   * 
   * @author Cougar Tasker
   *
   */
  private static class Token {
    // mutable state



    // unmutable state
    private HashMap<Integer, LinkedList<Function<Character, Integer>>> test = null;
    // a map from the state and char to state
    private Function<String, Entry> produce;

    /**
     * Create a Token with with a given production function and set of transitions.
     * 
     * @param test A predicate that returns true if the given string can be parsed by this token.
     * @param produce Produce a token from this given string.
     */
    private Token(Function<String, Entry> produce, Transition... ts) {
      this.test = new HashMap<Integer, LinkedList<Function<Character, Integer>>>();
      this.produce = produce;
      for (Transition t : ts) {
        addTransition(t);
      }
    }

    /**
     * Create a token which returns a fixed given Symbol entry for recognising a given character.
     * 
     * @param val the Symbol this token represents.
     * @param c the character that this token recognises.
     */
    private Token(Symbol val, char c) {
      this(s -> Entry.getEntry(val), new Transition(START, c, SUCCESS[0]));
    }

    /**
     * Create a token which returns a fixed given Symbol entry for recognising a given character
     * with a fsm defined by a set of transtions.
     * 
     * @param val the Symbol this token represents.
     * @param c the character that this token recognises.
     */
    private Token(Symbol val, Transition... ts) {
      this(s -> Entry.getEntry(val), ts);
    }


    private void addTransition(Transition t) {
      if (test.containsKey(t.from)) {
        test.get(t.from).add(c -> t.acc.test(c) ? t.to : null);
        // add new transition from this state if acc is correct
      } else {
        LinkedList<Function<Character, Integer>> tmp =
            new LinkedList<Function<Character, Integer>>();
        tmp.add(c -> t.acc.test(c) ? t.to : null);
        // add new transition from this state if acc is correct
        test.put(t.from, tmp);
        // create the state with this transition
      }
    }


    /**
     * Get the next state that the token would be in under the given input character c. If there
     * isn't a transition isn't defined or will accept a given input it will transtion to the FAIL
     * state.
     * 
     * @param curState the state the the token is in.
     * @param c the character that will change the state
     * @return the next state.
     */
    public int nextState(int curState, char c) {
      if (curState == FAIL) {
        return FAIL;
      }
      LinkedList<Function<Character, Integer>> now = test.get(curState);
      // get the transitions for the current state
      if (now == null) {
        return FAIL;
        // this shouldn't happen but if the token is in an undefined state assume it has failed
      }
      Optional<Integer> next =
          now.stream().map(f -> f.apply(c)).filter(nextstate -> nextstate != null).findFirst();
      // apply the current character to get the next state
      if (next.isEmpty()) {
        // there was no next state this is a dead end.
        return FAIL;
      } else {
        return next.get();
      }
    }

    /**
     * when called this will search a string from a given starting location and return the entry
     * that consumes the most characters.if no match is found then it will return with length of
     * zero. if start is outside the bounds of the string a match ot will return the same as not
     * finding any match.
     * 
     * @param c the input string of characters.
     * @param start the integer location of the character where to start the search.
     * @return a pair containing the length of characters consumed and and the entry that has been
     *         produced. the len will be zero if no match has been found and then the Entry may be
     *         null
     */
    public Pair<Integer, Entry> accept(String c, int start) {
      int i = start;
      int state = START;
      int next = FAIL;
      while (i < c.length() && i >= 0 && (next = nextState(state, c.charAt(i))) != FAIL) {
        state = next;
        i++;
      }
      if (isSuccess(state)) {
        int len = i - start;
        return new Pair<Integer, Entry>(len, this.produce.apply(c.substring(start, start + len)));
      } else {
        return new Pair<Integer, Entry>(0, null);
      }
    }

    /**
     * Finds weather a given state is successful. if this is true then the string that has been
     * accepted so far can be parsed.
     * 
     * @param state the state to check.
     * @return true if the state indicates that the accepted characters are valid.
     */
    public boolean isSuccess(int state) {
      return state < FAIL;
    }
  }

  private static final Transition[] number = new Transition[] {
      // any single digit is accepted
      new Transition(START, c -> Character.isDigit(c), SUCCESS[0]),
      // repeated digits are allowed
      new Transition(SUCCESS[0], c -> Character.isDigit(c), SUCCESS[0]),
      // a dot can be part of a number but it is not a success on its own
      new Transition(SUCCESS[0], '.', 1),
      // you must follow a dot with a number
      new Transition(1, c -> Character.isDigit(c), SUCCESS[1]),
      // you can have any number of digits after a dot
      new Transition(SUCCESS[1], c -> Character.isDigit(c), SUCCESS[1]),
      // you can start a float with a -
      new Transition(START, '-', 2),
      // if a digit follows a - then it is a valid number ie -2
      new Transition(2, c -> Character.isDigit(c), SUCCESS[0])};

  private static final Transition[] assignment =
      new Transition[] {new Transition(START, '=', 1), new Transition(1, '>', SUCCESS[0])};


  /**
   * An array of all tokens that are recognised.
   */
  public static final Token[][] tokens = new Token[][] {
      // whitespace characters should be recognised otherwise it would throw an error but they are
      // invalid so will be ignored
      {new Token(Symbol.INVALID,
          new Transition(START, c -> Character.isWhitespace(c), SUCCESS[0]))},
      // recognise numbers with the number fsm
      {new Token(s -> Entry.getEntry(Float.parseFloat(s)), number), new Token(Symbol.MINUS, '-')},
      // each of the symbols require there own token
      {new Token(Symbol.PLUS, '+'), new Token(Symbol.TIMES, '*'), new Token(Symbol.DIVIDE, '/'),
          new Token(Symbol.POWER, '^')},

      // different types of brackets should be accepted to make it easer to understand nesting.
      {new Token(Symbol.LEFT_BRACKET, '('), new Token(Symbol.RIGHT_BRACKET, ')'),
          new Token(Symbol.LEFT_BRACKET, '['), new Token(Symbol.RIGHT_BRACKET, ']')},
      // new level for assignment and varibles as this is an unlikely to happen often
      {new Token(s -> Entry.getEntry(s),
          new Transition(START, c -> Character.isLetter(c), SUCCESS[0])),
          new Token(Symbol.ASSIGNMENT, assignment)}};


  // save a reference to invalid so we don't need to look it up each time
  private static final Entry INVALID = Entry.getEntry(Symbol.INVALID);

  /**
   * When called on a valid input expression this method will separate it into individual
   * {@link Entry entries}. These entries are returned in reverse order on a stack. If there are
   * unrecognised tokens within the string a invalid expression exception will be thrown.
   * 
   * @param input the expression string to be parsed.
   * @return a list of parsed tokens in order. The {@link Entry entry} on the top of the stack will
   *         be the last token in the string.
   * @throws InvalidExpressionException if there are unrecognisable tokens within the expression.
   */
  static LinkedList<Entry> parse(String input) throws InvalidExpressionException {
    if (input.isEmpty()) {
      throw new InvalidExpressionException("cannot evaluate an empty expression");
    }
    int base = 0;
    LinkedList<Entry> out = new LinkedList<Entry>();
    boolean fistComeFirstServed = true;
    Pair<Integer, Entry> cur = null;
    while (base < input.length()) { // while there are still unparsed characters
      fistComeFirstServed = cur == null
          || cur.getValue().getType() != Type.NUMBER && cur.getValue().getType() != Type.STRING;
      for (Token[] level : tokens) {
        // for each level
        if (fistComeFirstServed) {
          // loop forwards
          for (int i = 0; i < level.length; i++) {
            // for each token in each level
            cur = level[i].accept(input, base);
            if (cur.getKey() > 0) {
              break;
            }
          }
        } else {
          // loop backwards
          for (int i = level.length - 1; i >= 0; i--) {
            // for each token in each level
            cur = level[i].accept(input, base);
            if (cur.getKey() > 0) {
              break;
            }
          }
        }
        if (cur.getKey() > 0) {
          break;
        }
      }
      if (cur.getKey() == 0) {
        // if there was no match for the flowing characters then then this expression is wrong
        throw new InvalidExpressionException(
            "unrecognised symbol in expression" + input.substring(base));
      }
      // move forward through the string
      base += cur.getKey();
      if (INVALID != cur.getValue()) { // ignore any symbol that isn'tt relevant
        out.add(cur.getValue());
      }
    }
    if (out.size() == 0) {
      throw new InvalidExpressionException("cannot evaluate a blank expression");
    }
    return out;
  }

  

  /**
   * When called on a valid expression string its value will be calculated. However on an invalid
   * expression a calculation exception will be thrown. The syntax of the expression is dependent on
   * the individual implementations of this class.
   * 
   * @param what The expression to be evaluated.
   * @return the value calculated from the expression.
   * @throws CalculationException thrown if there is an error during the calculation.
   */
  public float evaluate(String what) throws CalculationException {
    // this is to find varibles and repalce them with the correct values or setup assignments
    Pair<LinkedList<Entry>, String> in = VariableStorage.convertVaribles(parse(what));
    
    float out = this.evaluate(in.getKey());
    if (!Float.isFinite(out)) {
      throw new CalculationException("arthmetic overflow");
    }
    if (in.getValue() != null) {
      VariableStorage.setVar(in.getValue(), out);
    }
    return out;
  }

  /**
   * When called this will reduce the list of entry down to a single result. this is the subclasses
   * individual interpretation of evaluate. the result of this method must be the same for the same
   * input every time. it must always throw a calculation exception if if a calculation cannot be
   * performed
   * 
   * @param input the set of entries in the expression in order. The parser will only return entrys
   *        of type symbol and number
   * @return the number that this expression represents.
   * @throws CalculationException when there has been an error during computation. i.e divide by
   *         zero
   */
  protected abstract float evaluate(LinkedList<Entry> input) throws CalculationException;
}
