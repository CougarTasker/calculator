package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenizerTest {
  Tokenizer tokenizerInstance = null;
  String testA = "1 1 1";
  LinkedList<Entry> testAinstance;
  String testB = "1 + 2 + 3";
  LinkedList<Entry> testBinstance;

  @BeforeEach
  void before() {
    tokenizerInstance = new Tokenizer();
    testAinstance = new LinkedList<Entry>();
    testAinstance.add(new Entry(1));
    testAinstance.add(new Entry(1));
    testAinstance.add(new Entry(1));

    testBinstance = new LinkedList<Entry>();
    testBinstance.add(new Entry(1));
    testBinstance.add(new Entry(Symbol.PLUS));
    testBinstance.add(new Entry(2));
    testBinstance.add(new Entry(Symbol.PLUS));
    testBinstance.add(new Entry(3));

  }

  @Test
  void testParseSimple() throws InvalidExpressionException {
    assertEquals(testAinstance, Tokenizer.parse(testA),
        "tokenizer should correctly parse simple strings");
    assertEquals(testBinstance, Tokenizer.parse(testB),
        "tokenizer should return items in the correct order");
  }

  @Test
  void testParseException() {
    assertThrows(InvalidExpressionException.class, () -> {
      Tokenizer.parse("not a expression");
    }, "an invalid expression should throw an invalid expression exception");
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
    floatTest.addAll(Arrays.asList(new Entry[] {new Entry(Symbol.LEFT_BRACKET),
        new Entry((float) 1.23), new Entry(Symbol.MINUS), new Entry(-2),
        new Entry(Symbol.RIGHT_BRACKET), new Entry(Symbol.TIMES), new Entry((float) 62.3),
        new Entry(Symbol.DIVIDE), new Entry(8)}));
    assertEquals(floatTest, Tokenizer.parse(testText),
        "tokenizer should parse flotaing point number such as -1.23");
  }

  @Test
  void testAlternateBrackets() throws InvalidExpressionException {
    LinkedList<Entry> bracketTest = new LinkedList<Entry>();
    String testText = "[]";
    bracketTest.add(new Entry(Symbol.LEFT_BRACKET));
    bracketTest.add(new Entry(Symbol.RIGHT_BRACKET));
    assertEquals(bracketTest, Tokenizer.parse(testText),
        "tokenizer should parse altenate types of brackets");
  }
}
