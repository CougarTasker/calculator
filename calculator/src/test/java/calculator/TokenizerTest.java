package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenizerTest {
  String testA = "1 1 1";
  LinkedList<Entry> testAinstance;
  String testB = "1 + 2 + 3";
  LinkedList<Entry> testBinstance;

  @BeforeEach
  void before() {
    testAinstance = new LinkedList<Entry>();
    testAinstance.add(Entry.getEntry(1));
    testAinstance.add(Entry.getEntry(1));
    testAinstance.add(Entry.getEntry(1));

    testBinstance = new LinkedList<Entry>();
    testBinstance.add(Entry.getEntry(1));
    testBinstance.add(Entry.getEntry(Symbol.PLUS));
    testBinstance.add(Entry.getEntry(2));
    testBinstance.add(Entry.getEntry(Symbol.PLUS));
    testBinstance.add(Entry.getEntry(3));

  }

  @Test
  void testParseSimple() throws InvalidExpressionException {
    assertEquals(testAinstance, Tokenizer.parse(testA),
        "tokenizer should correctly parse simple strings");
    assertEquals(testBinstance, Tokenizer.parse(testB),
        "tokenizer should return items in the correct order");
  }

  @Test
  void testSpacelessInput() throws InvalidExpressionException {
    assertEquals(testBinstance, Tokenizer.parse("1+2+3"),
        "tokenizer should parse sequentual input");
  }

  @Test
  void testFloatNumbers() throws InvalidExpressionException {
    LinkedList<Entry> floatTest = new LinkedList<Entry>();
    String testText = " (1.23--2.00) *62.3/ 8 ";
    floatTest.addAll(Arrays.asList(new Entry[] {Entry.getEntry(Symbol.LEFT_BRACKET),
        Entry.getEntry((float) 1.23), Entry.getEntry(Symbol.MINUS), Entry.getEntry(-2),
        Entry.getEntry(Symbol.RIGHT_BRACKET), Entry.getEntry(Symbol.TIMES),
        Entry.getEntry((float) 62.3), Entry.getEntry(Symbol.DIVIDE), Entry.getEntry(8)}));
    assertEquals(floatTest, Tokenizer.parse(testText),
        "tokenizer should parse flotaing point number such as -1.23");
  }

  @Test
  void testAlternateBrackets() throws InvalidExpressionException {
    LinkedList<Entry> bracketTest = new LinkedList<Entry>();
    String testText = "[]";
    bracketTest.add(Entry.getEntry(Symbol.LEFT_BRACKET));
    bracketTest.add(Entry.getEntry(Symbol.RIGHT_BRACKET));
    assertEquals(bracketTest, Tokenizer.parse(testText),
        "tokenizer should parse altenate types of brackets");
  }

  @Test
  void testOperators() throws InvalidExpressionException {
    LinkedList<Entry> opTest = new LinkedList<Entry>();
    opTest.add(Entry.getEntry(Symbol.PLUS));
    opTest.add(Entry.getEntry(Symbol.MINUS));
    opTest.add(Entry.getEntry(Symbol.DIVIDE));
    opTest.add(Entry.getEntry(Symbol.TIMES));
    opTest.add(Entry.getEntry(Symbol.POWER));
    assertEquals(opTest, Tokenizer.parse("+-/*^"),
        "tokenizer should be able to parse all these operators");
  }

  @Test
  void testBlank() {
    assertThrows(InvalidExpressionException.class, () -> {
      Tokenizer.parse("");
    }, "an empty expression should throw an error");
    assertThrows(InvalidExpressionException.class, () -> {
      Tokenizer.parse("    ");
    }, "an blank expression should throw an error");
  }

  @Test
  void testSubractionNegativeNumbers() throws InvalidExpressionException {
    // the string 1-1 should be the same as 1 - 1 not 1 -1
    // whereas 1*-1 should be 1 * -1
    LinkedList<Entry> opTest = new LinkedList<Entry>();
    opTest.add(Entry.getEntry(1));
    opTest.add(Entry.getEntry(Symbol.MINUS));
    opTest.add(Entry.getEntry(1));
    assertEquals(opTest, Tokenizer.parse("1-1"),
        "should be able to tell if is a negative number or a subtraction");

    opTest = new LinkedList<Entry>();
    opTest.add(Entry.getEntry(1));
    opTest.add(Entry.getEntry(Symbol.TIMES));
    opTest.add(Entry.getEntry(-1));
    assertEquals(opTest, Tokenizer.parse("1*-1"),
        "should be able to tell if is a negative number or a subtraction");
  }

  @Test
  void testVaribles() throws CalculationException {
    LinkedList<Entry> opTest = new LinkedList<Entry>();
    opTest.add(Entry.getEntry(1));
    opTest.add(Entry.getEntry(Symbol.ASSIGNMENT));
    opTest.add(Entry.getEntry("a"));
    assertEquals(opTest, Tokenizer.parse("1 => a"),
        "should be able to parse a varible decloration");
    Tokenizer t = new StandardCalc();
    t.evaluate("1=>x");
    assertEquals(2, t.evaluate("x+1"), "should be able to remember varible");
    t.evaluate("1+x=>x");
    assertEquals(2, t.evaluate("x"), "should be able update varible with varible");
    t.evaluate("-1=>y");
    assertEquals(1, t.evaluate("x+y"), "should be able to use multiple varibles");
  }

  @Test
  void testVariblesError() throws CalculationException {
    Tokenizer t = new StandardCalc();
    assertThrows(CalculationException.class, () -> {
      t.evaluate("=>");
    }, "an assinment should have a varible");
    assertThrows(CalculationException.class, () -> {
      t.evaluate("1+1=>");
    }, "an assinment should have a varible");
    assertThrows(CalculationException.class, () -> {
      t.evaluate("x=>1+1");
    }, "an assinment should be at the end");
    assertThrows(CalculationException.class, () -> {
      t.evaluate("k");
    }, "an assinment should throw an error if not initalized");
  }
  @Test
  void testSameStorage() throws CalculationException {
    //the two calculation modes should share the same storage.
    Tokenizer t = new StandardCalc();
    Tokenizer r = new RevPolishCalc();
    t.evaluate("1=>x");
    r.evaluate("2=>z");
    assertEquals(1,r.evaluate("x"));
    assertEquals(2,t.evaluate("z"));
  }
}
