package calculator;

import java.util.HashMap;
import java.util.LinkedList;
import javafx.util.Pair;

/**
 * A class for centralised variable storage so that all Tokenizers have the same varibles.
 * 
 * @author Cougar Tasker
 *
 */
public class VariableStorage {
  private static final Entry ASSIGNMENT = Entry.getEntry(Symbol.ASSIGNMENT);
  private static HashMap<String, Float> varibles = new HashMap<String, Float>();

  /**
   * Converts a String entries into there corresponding variable value.
   * 
   * @param varSymbols collection of entries with string entries representing variables.
   * @return a collection on entries without variables. and the variables to be set if there is an
   *         Assignment
   * @throws CalculationException if there is an issue with the variables i.e not declared2
   */
  public static Pair<LinkedList<Entry>, String> convertVaribles(LinkedList<Entry> varSymbols)
      throws CalculationException {
    String target = null;
    // replace variables with values
    for (int i = 0; i < varSymbols.size(); i++) {
      // cycle through the parts converting the variables
      Entry old = varSymbols.removeFirst();
      if (old.getType() == Type.STRING) {
        try {
          Float f = varibles.get(old.getString());
          if (f != null) {
            old = Entry.getEntry(f);
          } else {
            throw new CalculationException(old.getString() + " is not defined yet");
          }
        } catch (BadTypeException e) {
          // we check the type first this shouldn't happen
          throw new CalculationException("unknown error");
        }
      }
      if (old == ASSIGNMENT) {
        if (i != varSymbols.size() - 1) {
          throw new CalculationException("assignment symbol in the wrong place");
        } else {
          try {
            target = varSymbols.removeFirst().getString();
          } catch (BadTypeException e) {
            throw new CalculationException("target of assignment must be a varible");
          }
          break;
        }
      }
      varSymbols.addLast(old);
    }
    return new Pair<LinkedList<Entry>, String>(varSymbols, target);
  }

  /**
   * Set a variable value.
   * 
   * @param var the name of the variable you want to set
   * @param val the value it should have
   */
  public static void setVar(String var, float val) {
    varibles.put(var, val);
  }
}
