package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StrStackTest {
  StrStack stackInstance = null;
  public static final int MAX = 100000;
  @BeforeEach
  void before() {
    stackInstance = new StrStack();
  }

  @Test
  void testPush() {
    stackInstance.push("a");
    stackInstance.push("b");
    stackInstance.push("c");
  }
  @Test
  void testPop() {
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "cannot remove operator from an empty stack");
  }
  @Test
  void testPushPop() throws EmptyStackException {
    stackInstance.push("a");
    stackInstance.push("b");
    stackInstance.push("c");

    assertEquals("c", stackInstance.pop(), "pop should return the last element");
    assertEquals("b", stackInstance.pop(),
        "pop should return elements in the reverse order");
    assertEquals("a", stackInstance.pop(),
        "pop should return elements in the reverse order");
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "after pushing and poping an empty stack should still throw and exception");
  }
  @Test
  void testEmpty() throws EmptyStackException {
    assertTrue(stackInstance.isEmpty(), "a new stack should be empty");
    stackInstance.push("");
    assertFalse(stackInstance.isEmpty(), "a stack containting somthing shouldnt be empty");
    stackInstance.pop();
    assertTrue(stackInstance.isEmpty(),
        "a stack should be empty after the last element is removed");
  }
  @Test
  void testMany() throws EmptyStackException {
    for (int i = 0; i <= MAX; i++) {
      stackInstance.push(Integer.toString(i));
    }
    for (int i = MAX; i >= 0; i--) {
      assertFalse(stackInstance.isEmpty(), "a stack containting somthing shouldnt be empty");
      assertEquals(Integer.toString(i), stackInstance.pop(),
          "the order should be maintaned even with large numbers");
    }
    assertTrue(stackInstance.isEmpty(),
        "a stack should be empty after the last element is removed");
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "cannot remove an entry from an empty stack");
  }

  @Test
  void testPerformance() {
    assertTimeout(Duration.ofNanos(MAX * 1000), () -> {
      for (int i = 0; i <= MAX; i++) {
        stackInstance.push(Integer.toString(i));
        stackInstance.pop();
        stackInstance.isEmpty();
      }
    }, "The stack should return quickly");
  }
}
