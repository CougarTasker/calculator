package calculator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StackTest {
  Stack stackInstance;
  Entry entryInstance;
  Entry entryInstances[];

  @BeforeEach
  void before() {
    stackInstance = new Stack();
    entryInstance = new Entry(Symbol.DIVIDE);
    entryInstances = new Entry[100000];
    for (int i = 0; i < entryInstances.length; i++) {
      entryInstances[i] = new Entry((float) i);
    }
  }

  @Test
  void testSize() {
    // 01
    assertEquals(stackInstance.size(), 0, "an empty stack should have size 0");
    // added size method and faked returning 0;
    // and added blank constructor
  }

  @Test
  void testPush() {
    // 02
    stackInstance.push(entryInstance);
    assertEquals(stackInstance.size(), 1, "The size of the stack should increse");
    stackInstance.push(entryInstance);
    assertEquals(stackInstance.size(), 2, "The size of the stack should increse again");
    // added size variable set it to 0 in constructor.
    // push just increments the size
    // size just returns the size value
  }

  @Test
  void testPop() throws EmptyStackException {
    // 03
    stackInstance.push(entryInstance);
    assertSame(stackInstance.pop(), entryInstance,
        "returned entry should be the same as what was pushed");
    assertEquals(stackInstance.size(), 0,
        "the stack should be empty again. the entry should have been removed from the stack");
    // created attribute for storing what was pushed
    // pop returns this attribute and decrements size
  }

  @Test
  void testPopOrderd() throws EmptyStackException {
    // 04
    stackInstance.push(entryInstances[0]);
    stackInstance.push(entryInstances[1]);
    stackInstance.push(entryInstances[2]);

    assertSame(stackInstance.pop(), entryInstances[2],
        "returned entry should be the last one that was pushed");
    assertSame(stackInstance.pop(), entryInstances[1],
        "returned entry should be the in the reverse order");
    assertSame(stackInstance.pop(), entryInstances[0],
        "returned entry should be the last one that was pushed");
    // replaced the attribute with an array of entries. push adds the entry at index size
    // pop returns the element at size - 1
  }

  @Test
  void testPopUnderflow() throws EmptyStackException {
    // 05
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "cannot remove an entry from an empty stack");
    assertEquals(stackInstance.size(), 0, "if pop fails the size shouldnt change");
    stackInstance.push(entryInstance);
    stackInstance.pop();
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "cannot remove an entry from an empty stack");
    assertEquals(stackInstance.size(), 0, "if pop fails the size shouldnt change");
    // created empty stack exception
    // at beginning of pop check the size is < 0 then throw this error
  }

  @Test
  void testTop() throws EmptyStackException {
    // 06
    assertThrows(EmptyStackException.class, () -> stackInstance.top(),
        "cannot get an entry from an empty stack");
    stackInstance.push(entryInstance);
    assertSame(stackInstance.top(), entryInstance,
        "after pushing an entry that entry should be returned by top");
    assertSame(stackInstance.top(), entryInstance, "top shouldnt remove an entry");

    stackInstance.push(entryInstances[1]);
    stackInstance.push(entryInstances[2]);
    assertSame(stackInstance.top(), entryInstances[2],
        "after pushing an entry that entry should be returned by top");
    stackInstance.pop();
    assertSame(stackInstance.top(), entryInstances[1],
        "after poping an entry the previous entry should be returned by top");
    // copy of pop but this doesn't decrement the size
  }



  @Test
  void testManyPushPop() throws EmptyStackException {
    // 07
    for (Entry i : entryInstances) {
      stackInstance.push(i);
      assertSame(stackInstance.top(), i, "the top entry should be the last entry added");
    }
    assertEquals(stackInstance.size(), entryInstances.length,
        "the size should be correct regardless of how many entrys in the stack");
    while (stackInstance.size() > 0) {
      stackInstance.pop();
    }
    assertThrows(EmptyStackException.class, () -> stackInstance.pop(),
        "cannot remove an entry from an empty stack");
    assertEquals(stackInstance.size(), 0, "if pop fails the size shouldnt change");
    // changed from the array to array list and updated push, pop, top, and the constructor
    // push now uses add without the size to add the the array
    // pop uses remove to remove on the same index as before to remove and retrive the entry on top
    // top uses get to get the element on top without removing it
    // constructor needs to initialise the empty arraylist
  }

  @Test
  void testToString() throws EmptyStackException {
    // 08
    stackInstance.push(entryInstances[0]);
    stackInstance.push(entryInstances[1]);
    stackInstance.push(entryInstances[2]);
    stackInstance.push(entryInstances[3]);
    assertTrue(stackInstance.toString().contains(entryInstances[0].toString()),
        "the to String method should include the first element");
    assertTrue(stackInstance.toString().contains(entryInstances[1].toString()),
        "the to String method should include all elements");
    assertTrue(stackInstance.toString().contains(entryInstances[2].toString()),
        "the to String method should include all elements");
    assertTrue(stackInstance.toString().contains(entryInstances[3].toString()),
        "the to String method should include the last element");
    stackInstance.pop();
    assertFalse(stackInstance.toString().contains(entryInstances[3].toString()),
        "the to String method should not include an element thats been removed");
    // add toString method that builds the string up by looping though the elements in the stack
  }
}
