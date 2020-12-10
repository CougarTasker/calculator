package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntryTest {
  Entry entryNumberInstance;
  Entry entrySymbolInstance;
  Entry entryStringInstance;

  static final float ENTRY_NUMBER = 1;
  static final Symbol ENTRY_SYMBOL = Symbol.DIVIDE;
  static final String ENTRY_STRING = "Test";

  @BeforeEach
  void before() {
    // 01.5
    entryNumberInstance = Entry.getEntry(ENTRY_NUMBER);
    entrySymbolInstance = Entry.getEntry(ENTRY_SYMBOL);
    entryStringInstance = Entry.getEntry(ENTRY_STRING);
    // added this to simplify the tests
  }

  @Test
  void testConstructor() {
    // 01
    Entry.getEntry((float) 2.5);
    Entry.getEntry((float) 1.2);
    Entry.getEntry((float) -2.5);
    Entry.getEntry((float) 0);
    for (Symbol val : Symbol.values()) {
      Entry.getEntry(val);
    }
    Entry.getEntry("");
    Entry.getEntry("abc");
    Entry.getEntry("123");
    // wrote a constructor that just accepts each of the three different types
    // faked storing the data
  }

  @Test
  void testGetType() {
    // 02
    assertEquals(entryNumberInstance.getType(), Type.NUMBER,
        "an entry constructed with a number should have the type of number");
    assertEquals(entrySymbolInstance.getType(), Type.SYMBOL,
        "an entry constructed with a symbol should have the type of symbol");
    assertEquals(entryStringInstance.getType(), Type.STRING,
        "an entry constructed with a string should have the type of string");
    // made the type enum and added type attribute the the class and to each of the constructors
    // of different types
  }

  @Test
  void testGetValue() throws BadTypeException {
    // 03
    assertEquals(entryNumberInstance.getValue(), ENTRY_NUMBER,
        "the returned value should be what was stored");
    // added number attribute to constructor and class
    // created function that returns this attribute
    assertThrows(BadTypeException.class, () -> entrySymbolInstance.getValue(),
        "cant get a number from a symbol entry");
    assertThrows(BadTypeException.class, () -> entryStringInstance.getValue(),
        "cant get a number from a string entry");
    // added a check that the type of the class = number
    // created bad type exception
  }

  @Test
  void testGetSymbol() throws BadTypeException {
    // 04
    assertEquals(entrySymbolInstance.getSymbol(), ENTRY_SYMBOL,
        "the returned symbol should be what was stored");
    assertThrows(BadTypeException.class, () -> entryNumberInstance.getSymbol(),
        "cant get a symbol from a number entry");
    assertThrows(BadTypeException.class, () -> entryStringInstance.getSymbol(),
        "cant get a symbol from a string entry");
    // same as 03 just with the symbol type
  }

  @Test
  void testGetString() throws BadTypeException {
    // 05
    assertEquals(entryStringInstance.getString(), ENTRY_STRING,
        "the returned string should be what was stored");
    assertThrows(BadTypeException.class, () -> entrySymbolInstance.getString(),
        "cant get a string from a symbol entry");
    assertThrows(BadTypeException.class, () -> entryNumberInstance.getString(),
        "cant get a string from a number entry");
    // same as 03 just with the string type
  }

  @Test
  void testEquals() {
    // 06
    assertFalse(entryStringInstance.equals(entryNumberInstance),
        "diffrent entries should not be equal");
    assertTrue(entryStringInstance.equals(entryStringInstance), "same entries should be equal");
    assertTrue(entryStringInstance.equals(Entry.getEntry(ENTRY_STRING)),
        "diffrent entries with the same value should be equal");
    // auto generated equals stub and tweaked it to work with this class.
  }

  @Test
  void testToString() {
    // 07
    assertEquals(entryNumberInstance.toString(), Float.toString(ENTRY_NUMBER),
        "number should be properly converted");
    assertEquals(entryStringInstance.toString(), '"' + ENTRY_STRING + '"',
        "strings should be properly converted");
    assertEquals(entrySymbolInstance.toString(), ENTRY_SYMBOL.toString(),
        "symbols should be properly converted");
    // used the switch statement to return the right value converted to string.
  }

  @Test 
  void testSameSymbol(){
    assertSame(Entry.getEntry(Symbol.INVALID),Entry.getEntry(Symbol.INVALID));
  }
}
