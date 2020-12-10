package calculator;

import java.util.HashMap;

/**
 * This is the class to encapsulate the data that will be stored in a stack.
 * 
 * @author Cougar Tasker
 *
 */
public class Entry {
  private float number;
  private Symbol other;
  private String str;
  private Type type;
  private static final HashMap<Symbol, Entry> SYMBOL_MAP = new HashMap<Symbol, Entry>();

  /**
   * Create a type: number entry that stores a floating point value.
   * 
   * @param value the value to be stored.
   * @return
   */
  public static Entry getEntry(float value) {
    return new Entry(value);
  }

  private Entry(float value) {
    this.number = value;
    this.type = Type.NUMBER;
  }

  /**
   * Create a type: symbol entry that stores a symbol enum value. this method will return the same
   * entry for the same symbol type
   * 
   * @param which the symbol to be stored.
   */
  public static Entry getEntry(Symbol which) {
    Entry e = SYMBOL_MAP.get(which);
    if (e == null) {
      e = new Entry(which);
      SYMBOL_MAP.put(which, e);
    }
    return e;
  }

  private Entry(Symbol which) {
    this.other = which;
    this.type = Type.SYMBOL;
  }

  /**
   * create a type: String entry that stores a string value.
   * 
   * @param str the string to be stored.
   */
  public static Entry getEntry(String str) {
    return new Entry(str);
  }

  private Entry(String str) {
    this.str = str;
    this.type = Type.STRING;
  }

  /**
   * Get the floating point value of a number entry.
   * 
   * @return the value of this entry
   * @throws BadTypeException Thrown if the type of this entry isn't number
   */
  public float getValue() throws BadTypeException {
    if (this.type != Type.NUMBER) {
      throw new BadTypeException(Type.NUMBER, this.type);
    }
    return number;
  }

  /**
   * Get the enum value of a symbol entry.
   * 
   * @return the symbol stored in this entry
   * @throws BadTypeException Thrown if the type of this entry isn't symbol
   */
  public Symbol getSymbol() throws BadTypeException {
    if (this.type != Type.SYMBOL) {
      throw new BadTypeException(Type.SYMBOL, this.type);
    }
    return other;
  }

  /**
   * Get the value of a string entry.
   * 
   * @return the string stored in this entry
   * @throws BadTypeException Thrown if the type of this entry isn't string
   */
  public String getString() throws BadTypeException {
    if (this.type != Type.STRING) {
      throw new BadTypeException(Type.STRING, this.type);
    }
    return str;
  }

  /**
   * get the data type of this entry.
   * 
   * @return the type of this entry.
   */
  public Type getType() {
    return type;
  }

  /**
   * Generates a hash code based on the values of the attributes in the class.
   * 
   * @return The hash code for this entry
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Float.floatToIntBits(number);
    result = prime * result + ((other == null) ? 0 : other.hashCode());
    result = prime * result + ((str == null) ? 0 : str.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  /**
   * checks if two entries have equal value. This means they have the same class type and value.
   * 
   * @return returns true if they are equal.
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

    Entry other = (Entry) obj;

    if (type != other.type) {
      // check type first so only need to compare relevant results
      return false;
    } else {
      switch (type) {
        case NUMBER:
          return Float.floatToIntBits(number) == Float.floatToIntBits(other.number);
        case SYMBOL:
          return this.other == other.other;
        case STRING:
          return str.equals(other.str);
        default:
          return false;
      }
    }
  }

  /**
   * get a String representation of the entry based on its value.
   * 
   * @return the value of the entry for its given type.
   */
  @Override
  public String toString() {
    switch (type) {
      case NUMBER:
        return Float.toString(number);
      case SYMBOL:
        return other.toString();
      case STRING:
        return '"' + str + '"';
      default:
        return "[empty entry]";
    }
  }
}
