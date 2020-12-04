package calculator;

import org.junit.jupiter.api.BeforeAll;

class GuiViewTest {
  ViewInterface guiInstance = null;
  @BeforeAll
  void before() {
    guiInstance = new GuiView();
  }

}
