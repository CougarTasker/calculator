package calculator;

import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * A class for controlling the javafx graphical window. through this class the user will interacting
 * with the program.
 * 
 * @author Cougar Tasker
 *
 */
public class GuiView extends Application implements ViewInterface, ChangeListener<Toggle> {
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
    alertCalculationError(new CalculationException("not implemented yet"));
    calcObservers.forEach((Runnable observer) -> {
      observer.run();
    });
  }

  @FXML
  void userInputkeyPress(KeyEvent event) {
    if (userInput.getText().length() == 0) {
      userOutput.setText("2");
    } else {
      userOutput.setText("");
    }
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

  @Override
  public String getExpression() {
    return userInput.getText();
  }

  @Override
  public void setAnswer(String s) {
    userOutput.setText(s);

  }

  @Override
  public void addCalcObserver(Runnable f) {
    calcObservers.add(f);
  }

  @Override
  public void addTypeObserver(Consumer<OpType> i) {
    typeObservers.add(i);
  }

  @Override
  public void alertCalculationError(CalculationException e) {
    errorLabel.setText(e.getMessage());
    // show the error
  }
}
