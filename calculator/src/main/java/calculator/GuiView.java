package calculator;

import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * A class for controlling the javafx graphical window. through this class the user will interact
 * with the program.
 * 
 * @author Cougar Tasker
 *
 */
public class GuiView extends Application implements ViewInterface {
  private ArrayList<Runnable> calcObservers = new ArrayList<Runnable>();
  private ArrayList<Consumer<OpType>> typeObservers = new ArrayList<Consumer<OpType>>();
  @FXML
  private GridPane root;

  @FXML
  private TextField userInput;

  @FXML
  private Button calculate;

  @FXML
  private Label userOutput;

  @FXML
  private RadioButton infix;

  @FXML
  private ToggleGroup notation;

  @FXML
  private RadioButton postfix;

  @FXML
  private Label errorLabel;

  @FXML
  void calculateMouseClicked(MouseEvent event) {
    calcObservers.forEach((Runnable observer) -> {
      observer.run();
    });
    userInput.requestFocus();
    userInput.positionCaret(userInput.lengthProperty().get());
  }

  @FXML
  void userInputkeyPress(KeyEvent event) {
    errorLabel.setText("");
    userInput.getStyleClass().remove("error");
    // if the expression has change the error is no longer applicable
    if (event.getCode() == KeyCode.ENTER) {
      calculateMouseClicked(null);
    }

  }

  @FXML
  void infixMouseClicked(MouseEvent event) {
    typeObservers.forEach((Consumer<OpType> observer) -> {
      observer.accept(OpType.INFIX);
    });
  }

  @FXML
  void postfixMouseClicked(MouseEvent event) {
    typeObservers.forEach((Consumer<OpType> observer) -> {
      observer.accept(OpType.POSTFIX);
    });
  }

  /**
   * This method is called by the {@link CalcController} to create the user interface. This method
   * can also be called by calling launch. this method contains the details specific to creating the
   * GUI view so is kept separate from the start method in calcController for cohesion.
   * 
   * @param primaryStage this is the main window of the application.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader l = new FXMLLoader();
    l.setLocation(getClass().getResource("/fxml/guiView.fxml"));
    l.setController(this);
    Parent root = l.load();
    Scene scene = new Scene(root, 450, 150);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getExpression() {
    return userInput.getText();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAnswer(String s) {
    userOutput.setText(s);

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
    userInput.getStyleClass().add("error");
    userOutput.setText("");
    errorLabel.setText(e.getMessage());
    
    // show the error
  }
}
