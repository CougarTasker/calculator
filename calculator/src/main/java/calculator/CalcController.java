package calculator;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The CalcController class is the entry point into this program. This class is used to create and
 * connect the model and the view.
 * 
 * @author Cougar Tasker
 *
 */
public class CalcController extends Application {
  /**
   * The view that the user is interacting with.
   */
  ViewInterface view = null;

  /**
   * Starts the calculator program. This method will determine weather a console has been attached
   * and will launch a GUI or ASCII interface appropriately.
   * 
   * @param args the launch arguments. these will be ignored.
   */
  public static void main(String[] args) {

    if (System.console() != null) {
      new CalcController(args);
    } else {
      // if there is no console then start the graphical interface
      launch(args);
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
   * Basic constructor for the {@link Application#launch(String...)} method to construct the
   * application.
   */
  public CalcController() {
    super();
  }

  /**
   * When called creates a calcController object and creates a {@link AsciiView}. This method should
   * only be called if there is a console attached. to check weather there is a console attached
   * check {@code System.console() == null}
   * 
   * @see System#console()
   * @param args the command line arguments used to launch the program.
   */
  public CalcController(String[] args) {
    if (System.console() != null) {
      System.out.println("the program has a console attached");
    }
    // this.view = new AsciiView();
  }

  /**
   * called by javafx when the {@link javafx.application.Appliation Appliation} is created. This
   * method should be called by javafx when the program is launched graphically.
   * 
   * 
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    GuiView view = new GuiView();
    view.start(primaryStage);
    this.view = view;
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
