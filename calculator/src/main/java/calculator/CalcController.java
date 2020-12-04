package calculator;

import javafx.application.Application;

/**
 * The CalcController class is the entry point into this program. This class is used to create and
 * connect the model and the view.
 * 
 * @author Cougar Tasker
 *
 */
public class CalcController {

  /**
   * Starts the calculator program. This method will determine weather a console has been attached
   * and will launch a GUI or ASCII interface appropriately.
   * 
   * @param args the launch arguments. these will be ignored.
   */
  public static void main(String[] args) {
    System.out.println("the program has been started");
    if (System.console() != null) {
      System.out.println("the program has a console attached");

    } else {
      // if there is no console then start the graphical interface
      Application.launch(GuiView.class, args);
    }
  }

  /**
   * Starts the calculator program. This method will determine weather a console has been attached
   * and will launch a GUI or ASCII interface appropriately.
   */
  public void main() {
    main(null);
  }

  /**
   * Called when there is a change of expression type.
   */
  public void expressionType() {

  }

  /**
   * Called when a calculation is required.
   */
  public void calculate() {

  }


}
