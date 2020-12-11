package calculator;

import java.io.Console;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * This class is another interface to the calculator program through a console. This class is
 * designed to be as simple as possible to make it as accessible as possible since many bail and
 * other devices use the console to interact with a user. This class relies on there being a console
 * attached and will not be useful without it.
 * 
 * @author Cougar Tasker
 *
 */
public class AsciiView implements ViewInterface, Runnable {
  private ArrayList<Runnable> calcObservers = new ArrayList<Runnable>();
  private ArrayList<Consumer<OpType>> typeObservers = new ArrayList<Consumer<OpType>>();
  String currentExpression = "";
  OpType currentType = OpType.INFIX;
  private static AsciiView instance = null;


  private AsciiView() {

  }

  private void printHelp() {
    System.out.println("Help:\n");
    System.out.println(" - type the expression you want to evaluate before the (>) prompt.");
    System.out.println(" - press return (enter) to compleate an action");
    System.out.println(" - to exit type quit or exit");
    System.out.println(" - to change the expression mode type 'set <expression_type>'");
    System.out.println("    + set infix -> for infix notation");
    System.out.println("    + set rpn   -> for reverse polish notation");
    System.out.println(" - to assign a varible follow your expression with => x");
    System.out.println("    + x could be any letter you want ");
    System.out.println("    + you can use the varible in an expression");
    System.out.println("    + x + 1 => x   will increment x by one everytime you run it");
    System.out.println("    + to see the value of a varible just type its character");
    System.out.println(" - to print this message type help or ?\n");
  }

  /**
   * Get the current instance of AsciiView. there will only be one AsciiView to avoid conflicts with
   * the console.
   */
  public static AsciiView getAsciiView() {
    if (instance == null) {
      instance = new AsciiView();
    }
    return instance;
  }

  private void notifyCalcObservers() {
    calcObservers.forEach(r -> r.run());
  }

  private void notifyTypeObservers() {
    typeObservers.forEach(t -> t.accept(currentType));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getExpression() {
    return currentExpression;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAnswer(String s) {
    System.out.print("=" + s + "\n");

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addCalcObserver(Runnable f) {
    calcObservers.add(f);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTypeObserver(Consumer<OpType> i) {
    typeObservers.add(i);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void alertCalculationError(CalculationException e) {
    System.out.println("Error: " + e.getMessage());
  }

  @Override
  public void run() {
    boolean running = true;
    System.out.println("CS2800 - Calculator Porgram\n");
    printHelp();
    Console con = System.console();
    while (running) {
      System.out.print(">");
      String line = con.readLine();
      if (line.contains("set")) {
        if (line.contains("infix")) {
          currentType = OpType.INFIX;
          notifyTypeObservers();
        } else if (line.contains("rpn")) {
          currentType = OpType.POSTFIX;
          notifyTypeObservers();
        } else {
          System.out.println("unrecognised mode type ? to get help");
        }
      } else if (line.trim().equals("exit") || line.trim().equals("quit")) {
        running = false;
      } else if (line.contains("help") || line.trim().equals("?")) {
        printHelp();
      } else {
        currentExpression = line;
        notifyCalcObservers();
      }
    }

  }

}
