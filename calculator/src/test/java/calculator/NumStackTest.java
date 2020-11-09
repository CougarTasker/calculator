package calculator;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NumStackTest {
  NumStack stackInstance;
  public static final int MAX = 100000;

  @BeforeEach
  void before() {
    stackInstance = new NumStack();
  }

  @Test
  void testPush() {
    stackInstance.push(0);
    stackInstance.push((float) 1.5);
    stackInstance.push(2);
    stackInstance.push(-1);
  }

  @Test
  void testPop() throws EmptyStackException {
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "cannot remove an entry from an empty stack");
    stackInstance.push(0);
    assertEquals(0, stackInstance.pop(), 0.001,
        "the retured value must be close to what was pushed");
    stackInstance.push(1);
    assertEquals(1, stackInstance.pop(), 0.001,
        "the retured value must be close to what was pushed");
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "cannot remove an entry from an empty stack");
  }

  @Test
  void testPushPopOrder() throws EmptyStackException {
    stackInstance.push(0);
    stackInstance.push(1);
    stackInstance.push(2);

    assertEquals(2, stackInstance.pop(), 0.001, "the retured value must be in the reverse order");
    assertEquals(1, stackInstance.pop(), 0.001, "the retured value must be in the reverse order");
    assertEquals(0, stackInstance.pop(), 0.001, "the retured value must be in the reverse order");
  }

  @Test
  void testEmpty() throws EmptyStackException {
    assertTrue(stackInstance.isEmpty(), "a new stack should be empty");
    stackInstance.push(0);
    assertFalse(stackInstance.isEmpty(), "a stack containting somthing shouldnt be empty");
    stackInstance.pop();
    assertTrue(stackInstance.isEmpty(),
        "a stack should be empty after the last element is removed");
  }

  @Test
  void testMany() throws EmptyStackException {
    for (int i = 0; i <= MAX; i++) {
      stackInstance.push((float) i);
    }
    for (int i = MAX; i >= 0; i--) {
      assertFalse(stackInstance.isEmpty(), "a stack containting somthing shouldnt be empty");
      assertEquals((float) i, stackInstance.pop(), 0.001,
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
        stackInstance.push((float) i);
        stackInstance.pop();
        stackInstance.isEmpty();
      }
    }, "The stack should return quickly");
  }
}
