package calculator;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpStackTest {
  OpStack stackInstance = null;
  public static final int MAX = 100000;
  int symbolCount = 0;

  @BeforeEach
  void before() {
    stackInstance = new OpStack();
    symbolCount = Symbol.values().length;
  }

  @Test
  void testPush() {
    stackInstance.push(Symbol.PLUS);
    stackInstance.push(Symbol.MINUS);
    stackInstance.push(Symbol.TIMES);
  }

  @Test
  void testPop() {
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "cannot remove operator from an empty stack");
  }

  @Test
  void testPushPop() throws EmptyStackException {
    stackInstance.push(Symbol.PLUS);
    stackInstance.push(Symbol.MINUS);
    stackInstance.push(Symbol.TIMES);

    assertEquals(Symbol.TIMES, stackInstance.pop(), "pop should return the last element");
    assertEquals(Symbol.MINUS, stackInstance.pop(),
        "pop should return elements in the reverse order");
    assertEquals(Symbol.PLUS, stackInstance.pop(),
        "pop should return elements in the reverse order");
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "after pushing and poping an empty stack should still throw and exception");
  }

  @Test
  void testEmpty() throws EmptyStackException {
    assertTrue(stackInstance.isEmpty(), "a new stack should be empty");
    stackInstance.push(Symbol.INVALID);
    assertFalse(stackInstance.isEmpty(), "a stack containting somthing shouldnt be empty");
    stackInstance.pop();
    assertTrue(stackInstance.isEmpty(),
        "a stack should be empty after the last element is removed");
  }

  @Test
  void testMany() throws EmptyStackException {
    for (int i = 0; i <= MAX; i++) {
      stackInstance.push(Symbol.values()[i % symbolCount]);
    }
    for (int i = MAX; i >= 0; i--) {
      assertFalse(stackInstance.isEmpty(), "a stack containting somthing shouldnt be empty");
      assertEquals(i % symbolCount, stackInstance.pop().ordinal(),
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
        stackInstance.push(Symbol.values()[i % symbolCount]);
        stackInstance.pop();
        stackInstance.isEmpty();
      }
    }, "The stack should return quickly");
  }
  @Test
  void testTop() throws EmptyStackException {
    stackInstance.push(Symbol.PLUS);
    assertEquals(Symbol.PLUS, stackInstance.top(), "top should return the last element added");
    stackInstance.push(Symbol.MINUS);
    assertEquals(Symbol.MINUS, stackInstance.top(), "top should update after push");
    stackInstance.push(Symbol.TIMES);

    assertEquals(Symbol.TIMES, stackInstance.top(), "top should return the last element");
    stackInstance.pop();
    assertEquals(Symbol.MINUS, stackInstance.top(),
        "top should return elements in the reverse order");
    stackInstance.pop();
    assertEquals(Symbol.PLUS, stackInstance.top(),
        "top should return elements in the reverse order");
    stackInstance.pop();
    assertThrows(EmptyStackException.class, () -> stackInstance.top(),
        "after pushing and top an empty stack should still throw and exception");
  }
}
