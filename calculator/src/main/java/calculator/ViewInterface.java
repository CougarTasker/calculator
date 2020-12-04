package calculator;

import java.util.function.Consumer;

/**
 * interface for interacting with the calculator program. classes that implement this interface can
 * be calculate the values to expressions and change the calculators mode.
 * 
 * @author Cougar Tasker
 */
public interface ViewInterface {
  /**
   * When called this method should return the current expression that the user has entered.
   * 
   * @return the expression the user has entered as a string
   */
  public String getExpression();

  /**
   * When called this method should set the answer that will be returned to the user.
   * 
   * @param s the value of the expression formatted as a string
   */
  public void setAnswer(String s);

  /**
   * Calling this method will alert the user to a error that has occurred with the calculation.
   * 
   * @param e The exception that has been thrown
   */
  public void alertCalculationError(CalculationException e);

  /**
   * When called this will associate a calculator observer to this view. a calculator observer will
   * be called when a calculation is requested.
   * 
   * @param f the runnable object to be called when a calculation is requested.
   */
  public void addCalcObserver(Runnable f);

  /**
   * When called this will associate a type observer to this view. this type observer will called
   * with the OpType when there has been a change or on setup. This value should default to infix.
   * 
   * @param i the type observer to be called when there is an update expression type.
   */
  public void addTypeObserver(Consumer<OpType> i);
}
