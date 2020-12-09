package calculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.util.Pair;

/**
 * This is a class designed to separate a string into individual tokens. This class will separate an
 * expression string into {@link Entry entries} of symbols and numbers where possible. This class
 * Requires a FSM to test the input and a function to produce the given entry. null entries will be
 * Ignored.
 * 
 * @author Cougar Tasker
 *
 */
public class Tokenizer {
  // standardized states for the fsm to use
  private static final int[] SUCCESS = {-2, -3, -4, -5, -6};
  private static final int START = 0;
  private static final int FAIL = -1;

  // simple container class for transitions to simplify describing fsm's
  static class Transition {
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
  static class Token {
    // mutable state
    private static final HashMap<Symbol, Entry> SYMBOL_MAP = new HashMap<Symbol, Entry>();

    /**
     * Get a Entry that contains the symbol s. This method will return the same entry for the same
     * symbol. this means:
     * <ul>
     * <li>direct comparisons can be made between symbol entries</li>
     * <li>faster instantiation</li>
     * <li>capped memory usage</li>
     * </ul>
     * 
     * @param s the symbol you want the corresponding entry for.
     * @return
     */
    public static Entry getSymbolEntry(Symbol s) {
      Entry e = SYMBOL_MAP.get(s);
      if (e == null) {
        e = new Entry(s);
        SYMBOL_MAP.put(s, e);
      }
      return e;
    }


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
      this(s -> getSymbolEntry(val), new Transition(START, c, SUCCESS[0]));
    }

    /**
     * Create a token which returns a fixed given Symbol entry for recognising a given character
     * with a fsm defined by a set of transtions.
     * 
     * @param val the Symbol this token represents.
     * @param c the character that this token recognises.
     */
    private Token(Symbol val, Transition... ts) {
      this(s -> getSymbolEntry(val), ts);
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

  /**
   * An array of all tokens that are recognised.
   */
  public static final Token[] tokens = new Token[] {
      // recognise numbers with the number fsm
      new Token(s -> new Entry(Float.parseFloat(s)), number),
      // each of the symbols require there own token
      new Token(Symbol.PLUS, '+'), new Token(Symbol.MINUS, '-'), new Token(Symbol.TIMES, '*'),
      new Token(Symbol.DIVIDE, '/'), new Token(Symbol.POWER, '^'),
      // whitespace characters should be recognised otherwise it would throw an error but they are
      // invalid so will be ignored
      new Token(Symbol.INVALID, new Transition(START, c -> Character.isWhitespace(c), SUCCESS[0])),
      // different types of brackets should be accepted to make it easer to understand nesting.
      new Token(Symbol.LEFT_BRACKET, '('), new Token(Symbol.RIGHT_BRACKET, ')'),
      new Token(Symbol.LEFT_BRACKET, '['), new Token(Symbol.RIGHT_BRACKET, ']')};


  // save a reference to invalid so we don't need to look it up each time
  private static final Entry INVALID = Token.getSymbolEntry(Symbol.INVALID);

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
  public static LinkedList<Entry> parse(String input) throws InvalidExpressionException {
    int base = 0;
    LinkedList<Entry> out = new LinkedList<Entry>();
    Pair<Integer, Entry> best; // will record the longest token for each round
    while (base < input.length()) { // while there are still unparsed characters
      best = new Pair<Integer, Entry>(0, null);
      for (Token t : tokens) {
        // check each token for the longest one. we want to do this in the least checks
        Pair<Integer, Entry> cur = t.accept(input, base);
        // save the result we will need it a couple of times
        // another approach would be to stop after the first match. then sort the tokens in order of
        // priority. but i've already spent wayyyy to much time on this.
        if (cur.getKey() > best.getKey()) {
          best = cur;
          // if this is a new longest token we will accept it
        }
      }
      if (best.getKey() == 0) {
        // if there was no match for the flowing characters then then this expression is wrong
        throw new InvalidExpressionException("unrecognised symbol in expression");
      }
      // move forward through the string
      base += best.getKey();
      if (INVALID != best.getValue()) { // ignore any symbol that isn'tt relevant
        out.add(best.getValue());
      }
    }
    return out;
  }
}
